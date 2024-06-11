import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Processor;
import com.google.javascript.jscomp.Scope;

@RunWith(JUnit4.class)
public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    AbstractCompiler compiler = new Processor();
    String filenamePrefix = "test";
    ProcessCommonJSModules pcjs = new ProcessCommonJSModules(compiler, filenamePrefix);

    String input = "// Test file\n" +
        "require('./test2');\n" +
        "goog.provide('test.main');\n" +
        "goog.require('test2');\n" +
        "exports.add = function(a, b) { return a + b; };";

    Node externs = Node.rootNode();
    Node root = Node.parse(input);

    pcjs.process(externs, root);

    Node expectedOutput = Node.parse("" +
        "var test$main = {};\n" +
        "test$main.module$exports = {\n" +
        "  add: function(a, b) {\n" +
        "    return a + b;\n" +
        "  }\n" +
        "};\n" +
        "goog.provide('test.main');\n" +
        "goog.require('test2');\n" +
        "goog.require('test2');");

    NodeTraversal nt = NodeTraversal.create(compiler, root);
    Node actualOutput = NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isScript()) {
          assertEquals(expectedOutput.getText(), n.getText());
          return;
        }
        super.visit(t, n, parent);
      }
    });

    assertEquals(expectedOutput.getText(), actualOutput.getText());
  }

  @Test(expected = RuntimeException.class)
  public void testInvalidUri() {
    AbstractCompiler compiler = new Processor();
    String filenamePrefix = "test";
    ProcessCommonJSModules pcjs = new ProcessCommonJSModules(compiler, filenamePrefix);

    String input = "// Test file\n" +
        "require('test:invalid_uri.js');";

    Node externs = Node.rootNode();
    Node root = Node.parse(input);

    pcjs.process(externs, root);
  }

  @Test
  public void testModuleExports() {
    AbstractCompiler compiler = new Processor();
    String filenamePrefix = "test";
    ProcessCommonJSModules pcjs = new ProcessCommonJSModules(compiler, filenamePrefix, false);

    String input = "// Test file\n" +
        "exports.add = function(a, b) { return a + b; };\n" +
        "goog.provide('test.main');";

    Node externs = Node.rootNode();
    Node root = Node.parse(input);

    pcjs.process(externs, root);

    Node expectedOutput = Node.parse("" +
        "var test$main = {\n" +
        "  module$exports: {\n" +
        "    add: function(a, b) {\n" +
        "      return a + b;\n" +
        "    }\n" +
        "  }\n" +
        "};\n" +
        "goog.provide('test.main');");

    NodeTraversal nt = NodeTraversal.create(compiler, root);
    Node actualOutput = NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isScript()) {
          assertEquals(expectedOutput.getText(), n.getText());
          return;
        }
        super.visit(t, n, parent);
      }
    });

    assertEquals(expectedOutput.getText(), actualOutput.getText());
  }

  @Test
  public void testModuleExportsOverride() {
    AbstractCompiler compiler = new Processor();
    String filenamePrefix = "test";
    ProcessCommonJSModules pcjs = new ProcessCommonJSModules(compiler, filenamePrefix);

    String input = "// Test file\n" +
        "goog.provide('test.main');\n" +
        "exports = {'add': function(a, b) { return a + b; }};";

    Node externs = Node.rootNode();
    Node root = Node.parse(input);

    pcjs.process(externs, root);

    Node expectedOutput = Node.parse("" +
        "var test$main = {\n" +
        "  module$exports: {\n" +
        "    add: function(a, b) {\n" +
        "      return a + b;\n" +
        "    }\n" +
        "  }\n" +
        "};\n" +
        "goog.provide('test.main');");

    NodeTraversal nt = NodeTraversal.create(compiler, root);
    Node actualOutput = NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isScript()) {
          assertEquals(expectedOutput.getText(), n.getText());
          return;
        }
        super.visit(t, n, parent);
      }
    });

    assertEquals(expectedOutput.getText(), actualOutput.getText());
  }
}