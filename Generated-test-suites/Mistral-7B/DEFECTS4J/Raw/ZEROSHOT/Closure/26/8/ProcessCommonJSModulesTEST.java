import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerContext;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerPassManager.PassName;
import com.google.javascript.jscomp.CompilerPassManager.PassRequirement;
import com.google.javascript.jscomp.CompilerPassStatus;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.CompilerUnit;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeVisitor;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.NodeFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private static final String TEST_SOURCE = "test.js";
  private static final String TEST_MODULE = "testModule";
  private static final String TEST_FILENAME_PREFIX = "test_prefix/";

  @org.junit.Test
  public void testProcessCommonJSModules() throws URISyntaxException, IOException {
    CompilerContext context = new CompilerContext();
    CompilerUnit unit = context.addSourceUnit(TEST_SOURCE, new File(TEST_SOURCE));

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerPass pass = new ProcessCommonJSModules(compiler, TEST_FILENAME_PREFIX);

    CompilerPassManager manager = new CompilerPassManager(context);
    manager.addPass(PassName.PROCESS_COMMON_JS_MODULES, 1, new PassRequirement(PassName.PARSE),
        new PassRequirement(PassName.TYPE_CHECK), new PassRequirement(PassName.GEN_SOURCEMAP),
        new PassRequirement(PassName.OPTIMIZE), new PassRequirement(PassName.CLOSURE_REWRITE),
        new PassRequirement(PassName.EXTERNAL_DEFER), new PassRequirement(PassName.VALIDATE_JS));
    manager.addPass(PassName.PROCESS_COMMON_JS_MODULES, 1, pass);

    NodeFactory factory = context.getOptions().nodeFactory;
    Node root = factory.parseTree(unit.getContent());

    manager.process(root, Collections.emptyList());

    assertEquals(CompilerPassStatus.OK, manager.getStatus());
    assertEquals(1, manager.getPassCount());

    NodeTraversal traversal = new NodeTraversal(context, root, null);
    Node processCommonJSModulesCallback = new Node(Node.CALL, null);
    Node processCommonJSModules = new Node(Node.NAME, "ProcessCommonJSModules");
    processCommonJSModules.setQualifiedName("com.google.javascript.jscomp.ProcessCommonJSModules");
    processCommonJSModules.setProperty(Node.ORIGINALNAME_PROP, "ProcessCommonJSModules");
    processCommonJSModules.addChild(processCommonJSModulesCallback);
    processCommonJSModulesCallback.addChild(root);
    traversal.getScope().setVar("ProcessCommonJSModules", processCommonJSModules);

    new NodeVisitor() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() && n.getChildCount() > 0 && n.getChildAtIndex(0)
            .isName() && n.getChildAtIndex(0).getString().equals("ProcessCommonJSModules")) {
          assertEquals(2, n.getChildCount());
          Node arguments = n.getChildAtIndex(1);
          assertEquals(2, arguments.getChildCount());
          Node abstractCompiler = arguments.getChildAtIndex(0);
          assertEquals(1, abstractCompiler.getChildCount());
          Node filenamePrefix = abstractCompiler.getChildAtIndex(0);
          assertEquals(1, filenamePrefix.getChildCount());
          Node stringLiteral = filenamePrefix.getChildAtIndex(0);
          assertEquals("test_prefix/", stringLiteral.getString());
          assertEquals(TEST_FILENAME_PREFIX, stringLiteral.getString());
        }
      }
    }.traverse(traversal);
  }

  @org.junit.Test
  public void testGuessCJSModuleName() throws URISyntaxException {
    String filename = "test_prefix/test.js";
    String expected = "testModule";

    assertEquals(expected, ProcessCommonJSModules.toModuleName(filename));
    assertEquals(expected, ProcessCommonJSModules.toModuleName("./test.js", filename));
    assertEquals(expected, ProcessCommonJSModules.toModuleName("../test.js", filename));
  }

  @org.junit.Test
  public void testNormalizeSourceName() throws URISyntaxException {
    String filename = "test_prefix/test.js";
    String expected = "test.js";

    assertEquals(expected, ProcessCommonJSModules.normalizeSourceName(filename));
  }

  @org.junit.Test
  public void testProcess() {
    CompilerContext context = new CompilerContext();
    CompilerUnit unit = context.addSourceUnit(TEST_SOURCE, new File(TEST_SOURCE));

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerPass pass = new ProcessCommonJSModules(compiler, TEST_FILENAME_PREFIX, false);

    CompilerPassManager manager = new CompilerPassManager(context);
    manager.addPass(PassName.PROCESS_COMMON_JS_MODULES, 1, new PassRequirement(PassName.PARSE),
        new PassRequirement(PassName.TYPE_CHECK), new PassRequirement(PassName.GEN_SOURCEMAP),
        new PassRequirement(PassName.OPTIMIZE), new PassRequirement(PassName.CLOSURE_REWRITE),
        new PassRequirement(PassName.EXTERNAL_DEFER), new PassRequirement(PassName.VALIDATE_JS),
        new PassRequirement(PassName.PROCESS_COMMON_JS_MODULES));

    NodeFactory factory = context.getOptions().nodeFactory;
    Node root = factory.parseTree(unit.getContent());

    manager.process(root, Collections.emptyList());

    assertEquals(CompilerPassStatus.OK, manager.getStatus());
    assertEquals(1, manager.getPassCount());

    NodeTraversal traversal = new NodeTraversal(context, root, null);
    new NodeVisitor() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isScript()) {
          Set<String> expectedModules = Sets.newHashSet("testModule");
          Set<String> actualModules = Sets.newHashSet();
          Node processCommonJSModulesCallback = n.getFirstChild();
          while (processCommonJSModulesCallback.isCall() && processCommonJSModulesCallback.getChildCount() > 0) {
            if (processCommonJSModulesCallback.getChildAtIndex(0)
                .isName() && processCommonJSModulesCallback.getChildAtIndex(0).getString()
                .equals("ProcessCommonJSModules")) {
              Node processCommonJSModules = processCommonJSModulesCallback.getChildAtIndex(1);
              Node exports = null;
              for (int i = 0; i < processCommonJSModules.getChildCount(); i++) {
                Node child = processCommonJSModules.getChildAtIndex(i);
                if (child.isGetProp() && child.getChildAtIndex(0)
                    .isName() && child.getChildAtIndex(0).getString().equals("module.exports")) {
                  exports = child.getChildAtIndex(1);
                  break;
                }
                if (child.isGetProp() && child.getChildAtIndex(0)
                    .isName() && child.getChildAtIndex(0).getString().equals("exports")) {
                  actualModules.add(child.getChildAtIndex(1)
                      .getString());
                  break;
                }
              }
              assertEquals(1, processCommonJSModules.getChildCount());
              assertEquals(expectedModules, actualModules);
            }
            processCommonJSModulesCallback = processCommonJSModulesCallback.getParent();
          }
        }
      }
    }.traverse(traversal);
  }

  @org.junit.Test
  public void testProcessRequire() {
    CompilerContext context = new CompilerContext();
    CompilerUnit unit = context.addSourceUnit("test.js", new File("test.js"));

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerPass pass = new ProcessCommonJSModules(compiler, TEST_FILENAME_PREFIX, false);

    CompilerPassManager manager = new CompilerPassManager(context);
    manager.addPass(PassName.PROCESS_COMMON_JS_MODULES, 1, new PassRequirement(PassName.PARSE),
        new PassRequirement(PassName.TYPE_CHECK), new PassRequirement(PassName.GEN_SOURCEMAP),
        new PassRequirement(PassName.OPTIMIZE), new PassRequirement(PassName.CLOSURE_REWRITE),
        new PassRequirement(PassName.EXTERNAL_DEFER), new PassRequirement(PassName.VALIDATE_JS),
        new PassRequirement(PassName.PROCESS_COMMON_JS_MODULES));

    NodeFactory factory = context.getOptions().nodeFactory;
    Node root = factory.parseTree(unit.getContent());

    manager.process(root, Collections.emptyList());

    assertEquals(CompilerPassStatus.OK, manager.getStatus());
    assertEquals(1, manager.getPassCount());

    NodeTraversal traversal = new NodeTraversal(context, root, null);

    String requiredModule = "requiredModule";
    String currentModule = "testModule";

    new NodeVisitor() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() && n.getChildCount() > 1 && n.getChildAtIndex(0)
            .isString() && n.getChildAtIndex(1)
            .isGetProp() && n.getChildAtIndex(1).getChildAtIndex(0)
            .isName() && n.getChildAtIndex(1).getChildAtIndex(0).getString().equals("require")) {
          Node requireCall = n;
          Node requiredModuleNameNode = n.getChildAtIndex(1).getChildAtIndex(1);
          assertEquals("requiredModule", requiredModuleNameNode.getString());
          String expectedModuleName = ProcessCommonJSModules.toModuleName(requiredModuleNameNode.getString(), currentModule);
          Node expectedModuleReference = IR.name(expectedModuleName).srcref(requireCall);
          Node actualModuleReference = requireCall.getChildAtIndex(0);
          assertEquals(expectedModuleReference.getSource(), actualModuleReference.getSource());
          assertEquals(expectedModuleName, actualModuleReference.getString());
        }
      }
    }.traverse(traversal);
  }
}