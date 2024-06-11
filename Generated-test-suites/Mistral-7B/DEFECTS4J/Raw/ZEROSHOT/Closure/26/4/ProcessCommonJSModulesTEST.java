import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessorTest.processAndCompile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Processor;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceName;

@RunWith(JUnit4.class)
public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws Exception {
    String input = "// @filename: test.js\n" +
        "var m1 = require('./m1');\n" +
        "exports.foo = m1.bar;\n" +
        "exports.baz = 42;\n" +
        "\n" +
        "// @filename: m1.js\n" +
        "var x = 1;\n" +
        "exports.bar = function() {\n" +
        "  return x++;\n" +
        "};\n";

    CompilerOptions options = new CompilerOptions(CompilerOptions.Mode.COMPILE);
    Node externs = NodeFactory.createExterns();
    Compiler compiler = new Compiler(externs, null, options);
    ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "");
    Node root = NodeFactory.parse(input, SourceName.getBootstrapSourceName(), null);

    processor: {
      Processor processor = new Processor(compiler);
      Node result = processAndCompile(processor, root);
      assertNotNull(result);

      // Check that module names have been changed.
      assertEquals("test$m1", result.getChildAtIndex(1).getString());
      assertEquals("test$exports", result.getChildAtIndex(2).getQualifiedName());

      // Check that exports have been moved to the top level.
      Node exportsNode = result.getChildAtIndex(2);
      Node exports = exportsNode.getFirstChild();
      assertEquals("foo", exports.getChildAtIndex(0).getQualifiedName());
      assertEquals("function (){\n  return x++;\n}",
          exports.getChildAtIndex(1).toString());
      assertEquals("baz", exports.getChildAtIndex(2).getString());

      // Check that require calls have been replaced with direct references.
      Node requireCall = result.getChildAtIndex(1).getChildAtIndex(0);
      assertNull(requireCall);

      // Check that goog.provide and goog.require have been emitted.
      Node googProvide = findNodeByType(result, Node.CALL, "goog.provide");
      assertNotNull(googProvide);
      Node googRequire = findNodeByType(result, Node.CALL, "goog.require");
      assertNotNull(googRequire);

      // Check that module exports have been renamed to avoid conflicts.
      Node moduleExports = findNodeByType(result, Node.GET_PROP, "exports");
      Node module = moduleExports.getParent();
      assertEquals("test$exports", moduleExports.getQualifiedName());
      assertEquals("test$", module.getQualifiedName());
    }
  }

  private Node findNodeByType(Node node, int nodeType, String nodeName) {
    for (int i = 0; i < node.getChildCount(); i++) {
      Node child = node.getChildAtIndex(i);
      if (child.getNodeType() == nodeType && child.getString().equals(nodeName)) {
        return child;
      }
      Node result = findNodeByType(child, nodeType, nodeName);
      if (result != null) {
        return result;
      }
    }
    return null;
  }
}