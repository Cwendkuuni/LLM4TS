package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Scope;

@RunWith(MockitoJUnitRunner.class)
public class ProcessCommonJSModulesTest {

  private AbstractCompiler compilerMock;
  private CompilerInput inputMock;
  private NodeTraversal traversalMock;
  private NodeFactory nodeFactoryMock;
  private Scope scopeMock;

  @Before
  public void setUp() {
    compilerMock = Mockito.mock(AbstractCompiler.class);
    inputMock = Mockito.mock(CompilerInput.class);
    traversalMock = Mockito.mock(NodeTraversal.class);
    nodeFactoryMock = Mockito.mock(NodeFactory.class);
    scopeMock = Mockito.mock(Scope.class);
  }

  @Test
  public void testProcessCommonJSModules() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compilerMock, "");

    String scriptSource = "var foo = 'bar';";
    Node scriptNode = Node.newScript(nodeFactoryMock, scriptSource);

    CompilerInput input = Mockito.spy(inputMock);
    input.setRoot(scriptNode);

    NodeTraversal traversal = new NodeTraversal(compilerMock, input, traversalMock);
    processor.process(input, scriptNode);

    assertNotNull(processor.getModule());

    JSModule module = processor.getModule();
    Preconditions.checkState(module != null, "Module should not be null");

    Node root = module.getRoot();
    assertEquals(scriptNode, root);

    assertEquals(1, root.getChildCount());
    assertEquals(scriptSource, root.getChildAtIndex(0).getScript().toString());
  }

  @Test
  public void testGuessCJSModuleName() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compilerMock, "");
    assertEquals("module$main", processor.guessCJSModuleName("./main.js"));
    assertEquals("module$sub", processor.guessCJSModuleName("./sub/index.js"));
    assertEquals("module$sub2", processor.guessCJSModuleName("./sub/sub2.js"));
  }

  @Test
  public void testToModuleName() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compilerMock, "");
    assertEquals("module$main", processor.toModuleName("./main.js"));
    assertEquals("module$sub", processor.toModuleName("./sub/index.js"));
    assertEquals("module$sub2", processor.toModuleName("./sub/sub2.js"));
    assertEquals("module$sub2$sub3", processor.toModuleName("./sub/sub2/sub3.js"));
  }

  @Test
  public void testToModuleNameWithRelativeRequire() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compilerMock, "");
    assertEquals("module$sub2", processor.toModuleName("./sub/sub2.js", "./main.js"));
  }

  @Test
  public void testNormalizeSourceName() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compilerMock, "");
    assertEquals("main", processor.normalizeSourceName("./main.js"));
    assertEquals("sub/index", processor.normalizeSourceName("./sub/index.js"));
    assertEquals("sub/sub2", processor.normalizeSourceName("./sub/sub2.js"));
    assertEquals("sub/sub2/sub3", processor.normalizeSourceName("./sub/sub2/sub3.js"));
  }

  @Test
  public void testProcessCommonJsModulesCallbackVisitRequireCall() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compilerMock, "");
    String moduleName = "module$main";
    Node requireCallNode = Node.newCall(nodeFactoryMock,
        Node.name("require"),
        Node.string("./main.js"));

    Node scriptNode = Node.newScript(nodeFactoryMock, "");
    scriptNode.addChildToBack(requireCallNode);

    CompilerInput input = Mockito.spy(inputMock);
    input.setRoot(scriptNode);

    NodeTraversal traversal = new NodeTraversal(compilerMock, input, traversalMock);
    processor.process(input, scriptNode);

    Node requireNode = scriptNode.getChildAtIndex(0);
    assertNotNull(requireNode);
    assertNotNull(requireNode.getFirstChild());
    assertEquals(moduleName, requireNode.getFirstChild().getString());
  }

  @Test
  public void testProcessCommonJsModulesCallbackVisitScript() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compilerMock, "");
    String moduleName = "module$main";
    String scriptSource = "var foo = 'bar';";

    Node scriptNode = Node.newScript(nodeFactoryMock, scriptSource);
    scriptNode.putProp(Node.ORIGINALNAME_PROP, "main.js");
    compilerMock.reportCodeChange = Mockito.mock(CodeChangeReporter.class);

    CompilerInput input = Mockito.spy(inputMock);
    input.setRoot(scriptNode);

    NodeTraversal traversal = new NodeTraversal(compilerMock, input, traversalMock);
    processor.process(input, scriptNode);

    JSModule module = processor.getModule();
    Preconditions.checkState(module != null, "Module should not be null");

    Node root = module.getRoot();
    assertEquals(scriptNode, root);

    assertEquals(1, root.getChildCount());
    assertEquals(scriptSource, root.getChildAtIndex(0).getScript().toString());

    assertEquals(moduleName, root.getFirstChild().getString());
    assertEquals("goog.provide(\"" + moduleName + "\");",
        root.getChildAtIndex(1).toString());
  }

  @Test
  public void testProcessCommonJsModulesCallbackVisitModuleExports() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compilerMock, "");
    String moduleName = "module$main";
    String exportsName = "exports";
    String scriptSource = "var module$exports = {};";

    Node scriptNode = Node.newScript(nodeFactoryMock, scriptSource);
    scriptNode.putProp(Node.ORIGINALNAME_PROP, "main.js");
    compilerMock.reportCodeChange = Mockito.mock(CodeChangeReporter.class);

    CompilerInput input = Mockito.spy(inputMock);
    input.setRoot(scriptNode);

    NodeTraversal traversal = new NodeTraversal(compilerMock, input, traversalMock);
    processor.process(input, scriptNode);

    JSModule module = processor.getModule();
    Preconditions.checkState(module != null, "Module should not be null");

    Node root = module.getRoot();
    assertEquals(scriptNode, root);

    assertEquals(1, root.getChildCount());
    assertEquals(scriptSource, root.getChildAtIndex(0).getScript().toString());

    Node exportsNode = root.getChildAtIndex(1);
    assertEquals(exportsName, exportsNode.getString());
    assertEquals(moduleName + "$$" + exportsName,
        root.getChildAtIndex(2).getString());
  }

  @Test
  public void testProcessCommonJsModulesCallbackEmitOptionalModuleExportsOverride() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compilerMock, "");
    String moduleName = "module$main";

    String scriptSource = "var foo = 'bar';\n" +
        "if (module$exports) {\n" +
        "  module$exports = foo;\n" +
        "}";

    Node scriptNode = Node.newScript(nodeFactoryMock, scriptSource);
    scriptNode.putProp(Node.ORIGINALNAME_PROP, "main.js");
    compilerMock.reportCodeChange = Mockito.mock(CodeChangeReporter.class);

    CompilerInput input = Mockito.spy(inputMock);
    input.setRoot(scriptNode);

    NodeTraversal traversal = new NodeTraversal(compilerMock, input, traversalMock);
    processor.process(input, scriptNode);

    JSModule module = processor.getModule();
    Preconditions.checkState(module != null, "Module should not be null");

    Node root = module.getRoot();
    assertEquals(scriptNode, root);

    assertEquals(1, root.getChildCount());
    assertEquals(scriptSource, root.getChildAtIndex(0).getScript().toString());

    Node ifStatementNode = root.getChildAtIndex(1);
    assertEquals("if (module$exports)", ifStatementNode.toString());

    Node ifStatementBlockNode = ifStatementNode.getChildAtIndex(0);
    assertEquals(1, ifStatementBlockNode.getChildCount());
    Node assignmentNode = ifStatementBlockNode.getChildAtIndex(0);
    assertEquals("module$exports = foo;", assignmentNode.toString());

    assertEquals(moduleName, ifStatementBlockNode.getChildAtIndex(1).getString());
  }

  @Test
  public void testSuffixVarsCallback() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compilerMock, "");
    String suffix = "foo";

    String scriptSource = "var foo = 'bar';\n" +
        "var baz = 1;\n" +
        "var qux = exports;";

    Node scriptNode = Node.newScript(nodeFactoryMock, scriptSource);
    compilerMock.reportCodeChange = Mockito.mock(CodeChangeReporter.class);

    CompilerInput input = Mockito.spy(inputMock);
    input.setRoot(scriptNode);

    NodeTraversal traversal = new NodeTraversal(compilerMock, input, traversalMock);
    NodeTraversal traversalWithSuffix = new NodeTraversal(compilerMock, input, traversalMock, new SuffixVarsCallback(suffix));

    processor.process(input, scriptNode);
    traversal.traverse(scriptNode);

    assertEquals(3, scriptNode.getChildCount());

    processor.process(input, scriptNode);
    traversalWithSuffix.traverse(scriptNode);

    assertEquals(6, scriptNode.getChildCount());
    assertEquals("foofoo", scriptNode.getChildAtIndex(3).getString());
    assertEquals("foobaz", scriptNode.getChildAtIndex(4).getString());
    assertEquals("fooqux", scriptNode.getChildAtIndex(5).getString());
  }
}