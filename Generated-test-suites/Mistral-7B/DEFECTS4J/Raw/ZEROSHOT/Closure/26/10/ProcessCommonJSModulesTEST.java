import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.AbstractPostOrderCallbackFlag.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.IR;

import java.util.Set;

public class ProcessCommonJSModulesTest extends CompilerPassTestBase {

  private static final String TEST_SRC_PREFIX = "test_src/";

  @Override
  protected CompilerPass getProcessorUnderTest() {
    return new ProcessCommonJSModules(compiler, TEST_SRC_PREFIX);
  }

  public void testProcessCommonJSModules() throws Exception {
    String input = "// test file\n" +
        "require('./module1');\n" +
        "function foo() {\n" +
        "  var x = require('./module2');\n" +
        "  var y = require('module3');\n" +
        "}\n" +
        "// module1.js\n" +
        "goog.provide('module1');\n" +
        "goog.require('module2');\n" +
        "exports.bar = function() {\n" +
        "  return 'module1 bar';\n" +
        "};\n" +
        "// module2.js\n" +
        "goog.provide('module2');\n" +
        "exports.baz = function() {\n" +
        "  return 'module2 baz';\n" +
        "};\n" +
        "// module3.js\n" +
        "goog.provide('module3');\n" +
        "exports.qux = function() {\n" +
        "  return 'module3 qux';\n" +
        "};\n";

    Node externs = parseTree(input);
    ProcessCommonJSModules processor = getProcessorUnderTest();
    processor.process(externs, parseTree(""));

    Node root = externs;
    Node processedRoot = NodeTraversal.traverse(compiler, root, new NodeCheckerCallback())
        .getOutput();

    assertEquals(input.length() + 52, processedRoot.getTreeLength());

    Node module1 = findNodeByName(processedRoot, "module1");
    Node module2 = findNodeByName(processedRoot, "module2");
    Node module3 = findNodeByName(processedRoot, "module3");
    Node foo = findNodeByName(processedRoot, "foo");

    // Check that require calls have been replaced with direct references.
    assertEquals(IR.name("module1"), foo.getChildAtIndex(0).getChildAtIndex(1));
    assertEquals(IR.name("module2"), foo.getChildAtIndex(1).getChildAtIndex(1));
    assertEquals(IR.name("module3"), foo.getChildAtIndex(2).getChildAtIndex(1));

    // Check that module exports have been renamed to avoid conflicts.
    Node module1Bar = findNodeByName(processedRoot, "module1.exports.bar");
    Node module2Baz = findNodeByName(processedRoot, "module2.module$$exports.baz");
    Node module3Qux = findNodeByName(processedRoot, "module3.module$$exports.qux");

    assertEquals("module1 bar", module1Bar.toString());
    assertEquals("module2 baz", module2Baz.toString());
    assertEquals("module3 qux", module3Qux.toString());

    // Check that goog.provide has been emitted for each module.
    Set<String> provides = Sets.newHashSet();
    NodeTraversal.traverse(compiler, processedRoot, new ProvideCollectorCallback(provides));
    assertEquals(3, provides.size());
    assertTrue(provides.containsAll(Sets.newHashSet("module1", "module2", "module3")));

    // Check that goog.require has been emitted for each required module.
    Set<String> requires = Sets.newHashSet();
    NodeTraversal.traverse(compiler, processedRoot, new RequireCollectorCallback(requires));
    assertEquals(3, requires.size());
    assertTrue(requires.containsAll(Sets.newHashSet("module1", "module2", "module3")));

    // Check that global variables have been renamed to avoid conflicts.
    Node x = findNodeByName(processedRoot, "module2.module$$exports");
    Node y = findNodeByName(processedRoot, "module3.module$$exports");
    Node xVar = findNodeByName(processedRoot, "x$$module2");
    Node yVar = findNodeByName(processedRoot, "y$$module3");

    assertEquals(x, xVar.getParent());
    assertEquals(y, yVar.getParent());
    assertEquals(x, xVar.getChildAtIndex(0));
    assertEquals(y, yVar.getChildAtIndex(0));
  }

  private static Node findNodeByName(Node root, String name) {
    return NodeTraversal.findNode(root, n -> n.isName() && n.getString().equals(name));
  }

  private static class NodeCheckerCallback extends AbstractPostOrderCallback {

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isName() && n.getString().equals("foo")) {
        fail("Expected 'foo' to be a function");
      }
    }
  }

  private static class ProvideCollectorCallback extends AbstractPostOrderCallback {

    private final Set<String> provides;

    ProvideCollectorCallback(Set<String> provides) {
      this.provides = provides;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isCall() && n.getChildCount() == 2 &&
          "goog.provide".equals(n.getFirstChild().getQualifiedName())) {
        provides.add(n.getChildAtIndex(1).getString());
      }
    }
  }

  private static class RequireCollectorCallback extends AbstractPostOrderCallback {

    private final Set<String> requires;

    RequireCollectorCallback(Set<String> requires) {
      this.requires = requires;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isCall() && n.getChildCount() == 2 &&
          "goog.require".equals(n.getFirstChild().getQualifiedName())) {
        requires.add(n.getChildAtIndex(1).getString());
      }
    }
  }
}