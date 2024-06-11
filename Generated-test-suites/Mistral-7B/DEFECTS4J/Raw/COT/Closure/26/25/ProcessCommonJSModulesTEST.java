import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.PrintWriter;
import java.net.URI;
import java.util.Arrays;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private Compiler compiler;
  private CompilerOptions options;
  private NodeFactory nodeFactory;
  private PrintWriter writer;

  @Before
  public void setUp() {
    compiler = Mockito.mock(Compiler.class);
    options = Mockito.mock(CompilerOptions.class);
    nodeFactory = Mockito.mock(NodeFactory.class);
    writer = Mockito.mock(PrintWriter.class);
  }

  @Test
  public void testProcess_NoRequireCalls() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
    Node rootNode = nodeFactory.createScript(null, null);
    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    NodeTraversal traversal = new NodeTraversal(rootNode, options, compiler, nodeFactory, callback);
    traversal.traverse();
    assertEquals(0, callback.scriptNodeCount);
  }

  @Test
  public void testProcess_SimpleRequireCall() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
    String sourceName = "test.js";
    String moduleName = "test";
    String requireModuleName = "dependency";
    Node rootNode = nodeFactory.createScript(sourceName, null);
    Node requireCallNode = nodeFactory.createCall(IR.name("require"), Arrays.asList(
        nodeFactory.createString(requireModuleName)));
    Node scriptNode = nodeFactory.createScript(null, null);
    scriptNode.addChild(requireCallNode);
    Node script = nodeFactory.createCall(IR.getprop(IR.name("goog"), IR.string("require")),
        Arrays.asList(nodeFactory.createString(requireModuleName)));
    scriptNode.addChild(script);
    rootNode.addChild(scriptNode);

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    NodeTraversal traversal = new NodeTraversal(rootNode, options, compiler, nodeFactory, callback);
    traversal.traverse();

    assertEquals(1, callback.scriptNodeCount);
    Node requireCall = traversal.getInput().getRequire(requireModuleName);
    assertNotNull(requireCall);
    Node scriptWithRequires = traversal.getInput().getSourceNode(requireCall.getSourcePosition().getFileName());
    assertEquals(2, scriptWithRequires.getChildCount());
    Node require = scriptWithRequires.getChildAtIndex(0);
    Node script = scriptWithRequires.getChildAtIndex(1);
    assertEquals("require", require.getFirstChild().getQualifiedName());
    assertEquals(requireModuleName, require.getChildAtIndex(1).getString());
    assertEquals("goog.require", script.getChildAtIndex(0).getQualifiedName());
    assertEquals(requireModuleName, script.getChildAtIndex(1).getString());
  }

  @Test
  public void testProcess_ModuleExports() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
    String sourceName = "test.js";
    String moduleName = "test";
    String requireModuleName = "dependency";
    String exportsName = "exports";

    Node rootNode = nodeFactory.createScript(sourceName, null);
    Node moduleExportsAssignmentNode = nodeFactory.createSetProp(
        IR.getprop(IR.name("module"), IR.string("exports")),
        IR.name(exportsName));
    rootNode.addChild(moduleExportsAssignmentNode);

    Node requireCallNode = nodeFactory.createCall(IR.name("require"), Arrays.asList(
        nodeFactory.createString(requireModuleName)));
    Node scriptNode = nodeFactory.createScript(null, null);
    scriptNode.addChild(requireCallNode);
    Node require = scriptNode.getChildAtIndex(0);
    Node moduleExports = nodeFactory.createGetProp(require, IR.string("exports"));
    scriptNode.addChild(nodeFactory.createCall(IR.getprop(IR.name("goog"), IR.string("provide")),
        Arrays.asList(nodeFactory.createString(moduleName))));
    scriptNode.addChild(moduleExportsAssignmentNode);
    scriptNode.addChild(moduleExports);
    rootNode.addChild(scriptNode);

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    NodeTraversal traversal = new NodeTraversal(rootNode, options, compiler, nodeFactory, callback);
    traversal.traverse();

    assertEquals(1, callback.scriptNodeCount);
    Node requireCall = traversal.getInput().getRequire(requireModuleName);
    assertNotNull(requireCall);
    Node scriptWithRequires = traversal.getInput().getSourceNode(requireCall.getSourcePosition().getFileName());
    assertEquals(5, scriptWithRequires.getChildCount());
    Node require = scriptWithRequires.getChildAtIndex(0);
    Node script = scriptWithRequires.getChildAtIndex(1);
    assertEquals("require", require.getFirstChild().getQualifiedName());
    assertEquals(requireModuleName, require.getChildAtIndex(1).getString());
    assertEquals("goog.require", script.getChildAtIndex(0).getQualifiedName());
    assertEquals(requireModuleName, script.getChildAtIndex(1).getString());
    Node moduleExports = scriptWithRequires.getChildAtIndex(3);
    assertEquals("exports", moduleExports.getQualifiedName());
    assertEquals(moduleName + "$$exports", moduleExports.getChildAtIndex(0).getString());
  }

  @Test(expected = RuntimeException.class)
  public void testProcess_InvalidURI() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
    String sourceName = "test.js";
    String requireModuleName = "dependency";
    Node rootNode = nodeFactory.createScript(sourceName, null);
    Node requireCallNode = nodeFactory.createCall(IR.name("require"), Arrays.asList(
        nodeFactory.createString("data:text/javascript;charset=utf-8;base64,dGVzdHVzZXJuYW1l")));
    Node scriptNode = nodeFactory.createScript(null, null);
    scriptNode.addChild(requireCallNode);
    Node require = scriptNode.getChildAtIndex(0);
    Node module = nodeFactory.createName(requireModuleName + "$");
    scriptNode.addChild(nodeFactory.createCall(IR.getprop(IR.name("goog"), IR.string("provide")),
        Arrays.asList(module)));
    scriptNode.addChild(require);
    rootNode.addChild(scriptNode);

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    NodeTraversal traversal = new NodeTraversal(rootNode, options, compiler, nodeFactory, callback);
    traversal.traverse();

    fail("Unexpected exception not thrown");
  }

  @Test(expected = RuntimeException.class)
  public void testProcess_InvalidFilename() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
    String sourceName = "test.js";
    String requireModuleName = "dependency";
    Node rootNode = nodeFactory.createScript(sourceName, null);
    Node requireCallNode = nodeFactory.createCall(IR.name("require"), Arrays.asList(
        nodeFactory.createString("test.-js")));
    Node scriptNode = nodeFactory.createScript(null, null);
    scriptNode.addChild(requireCallNode);
    Node require = scriptNode.getChildAtIndex(0);
    Node module = nodeFactory.createName(requireModuleName + "$");
    scriptNode.addChild(nodeFactory.createCall(IR.getprop(IR.name("goog"), IR.string("provide")),
        Arrays.asList(module)));
    scriptNode.addChild(require);
    rootNode.addChild(scriptNode);

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    NodeTraversal traversal = new NodeTraversal(rootNode, options, compiler, nodeFactory, callback);
    traversal.traverse();

    fail("Unexpected exception not thrown");
  }

  @Test
  public void testProcess_RelativeRequire() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
    String currentSourceName = "test.js";
    String requiredSourceName = "../dependency.js";
    String moduleName = "test";
    String requireModuleName = "dependency";

    Node rootNode = nodeFactory.createScript(currentSourceName, null);
    Node requireCallNode = nodeFactory.createCall(IR.name("require"), Arrays.asList(
        nodeFactory.createString(requiredSourceName)));
    Node scriptNode = nodeFactory.createScript(null, null);
    scriptNode.addChild(requireCallNode);
    Node require = scriptNode.getChildAtIndex(0);
    Node module = nodeFactory.createName(moduleName + "$");
    scriptNode.addChild(nodeFactory.createCall(IR.getprop(IR.name("goog"), IR.string("provide")),
        Arrays.asList(module)));
    scriptNode.addChild(require);
    rootNode.addChild(scriptNode);

    String sourceName = "dependency.js";
    Node dependencyRootNode = nodeFactory.createScript(sourceName, null);
    Node dependencyScriptNode = nodeFactory.createScript(null, null);
    dependencyScriptNode.addChild(IR.name("exports").setString("dependencyValue"));
    dependencyRootNode.addChild(dependencyScriptNode);

    NodeTraversal dependencyTraversal = new NodeTraversal(dependencyRootNode, options, compiler, nodeFactory, new ProcessCommonJSModules.ProcessCommonJsModulesCallback());
    dependencyTraversal.traverse();

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    NodeTraversal traversal = new NodeTraversal(rootNode, options, compiler, nodeFactory, callback);
    traversal.traverse();

    assertEquals(1, callback.scriptNodeCount);
    Node requireCall = traversal.getInput().getRequire(requireModuleName);
    assertNotNull(requireCall);
    Node scriptWithRequires = traversal.getInput().getSourceNode(requireCall.getSourcePosition().getFileName());
    assertEquals(3, scriptWithRequires.getChildCount());
    Node require = scriptWithRequires.getChildAtIndex(0);
    Node script = scriptWithRequires.getChildAtIndex(1);
    assertEquals("require", require.getFirstChild().getQualifiedName());
    assertEquals(requiredSourceName, require.getChildAtIndex(1).getString());
    assertEquals("goog.require", script.getChildAtIndex(0).getQualifiedName());
    assertEquals(requireModuleName, script.getChildAtIndex(1).getString());
    Node dependencyExport = scriptWithRequires.getChildAtIndex(2);
    assertEquals("exports", dependencyExport.getQualifiedName());
    assertEquals("dependencyValue", dependencyExport.getChildAtIndex(0).getString());
  }
}