import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.Compiler.AbstractCompiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import com.google.common.collect.Sets;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    String input = "// A simple CommonJS module.\n" +
        "define(function() {\n" +
        "  function square(x) {\n" +
        "    return x * x;\n" +
        "  }\n" +
        "  return {\n" +
        "    square: square\n" +
        "  };\n" +
        "});\n" +
        "\n" +
        "// A file that uses the above CommonJS module.\n" +
        "require(['modules/square'], function(square) {\n" +
        "  console.log(square(3)); // Output: 9\n" +
        "});";

    CompilerInput ci = new CompilerInput("input.js", input, null);
    AbstractCompiler compiler = new Compiler();
    ProcessCommonJSModules pcjm = new ProcessCommonJSModules(compiler, "");

    pcjm.process(ci.getExterns(), ci.getRoot());

    Node root = ci.getRoot();
    Node output = root.getFirstChild();
    Node modules = output.getFirstChild();

    // Check that module 'square' was correctly processed
    Node moduleNode = null;
    for (Node child = modules.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getString().equals("square")) {
        moduleNode = child;
        break;
      }
    }
    assertNotNull(moduleNode);

    // Check that 'square' function was correctly emitted
    Node squareFunction = moduleNode.getFirstChild().getFirstChild();
    assertEquals(Node.FUNCTION, squareFunction.getNodeType());
    assertEquals("function square(x)", squareFunction.toString());

    // Check that 'module.exports' was correctly replaced with 'square'
    Node exports = moduleNode.getLastChild();
    assertEquals(Node.OBJECT, exports.getNodeType());
    assertEquals(1, exports.getChildCount());
    Node exportsValue = exports.getFirstChild();
    assertEquals("square", exportsValue.getNodeType());
    assertEquals("object", exportsValue.toString());

    // Check that 'require' calls were correctly replaced with 'goog.require'
    Node requireCall = null;
    for (Node node = root; node != null; node = node.getNext()) {
      if (node.isCall() && node.getChildCount() == 2 &&
          "require".equals(node.getFirstChild().getQualifiedName())) {
        requireCall = node;
        break;
      }
    }
    assertNotNull(requireCall);

    Node requireFunction = node.getFirstChild().getFirstChild();
    assertEquals(Node.FUNCTION, requireFunction.getNodeType());
    assertEquals("function require(modules)", requireFunction.toString());

    Node requireCallArg = requireCall.getChildAtIndex(1);
    assertEquals("modules/square", requireCallArg.getString());

    // Check that 'goog.require' calls were correctly added
    Node googRequireCall = null;
    for (Node node = root; node != null; node = node.getNext()) {
      if (node.toString().startsWith("goog.provide")) {
        Node googProvideCall = node;
        for (Node child = googProvideCall.getLastChild(); child != null; child = child.getPrevious()) {
          if (child.isCall() &&
              "goog.require".equals(child.getFirstChild().getQualifiedName())) {
            googRequireCall = child;
            break;
          }
        }
        assertNotNull(googRequireCall);

        Node requireArgs = googRequireCall.getChildAtIndex(1);
        Set<String> requiredModules = Sets.newHashSet();
        for (Node arg = requireArgs; arg != null; arg = arg.getNext()) {
          requiredModules.add(arg.getString());
        }
        assertTrue(requiredModules.contains("square"));

        // Check that 'goog.require' calls are executed after 'require' calls
        Node requireCallExecuted = false;
        for (Node node = root; node != null; node = node.getNext()) {
          if (node.toString().startsWith("(function")) {
            Node requireCallFound = null;
            for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
              if (child.isCall() && node.toString().contains("require('square')")) {
                requireCallFound = child;
                requireCallExecuted = true;
                break;
              }
            }
            assertTrue(requireCallFound != null && requireCallExecuted);
            break;
          }
        }

        break;
      }
    }

    // Check that 'goog.provide' calls were correctly added
    Node googProvideCall = null;
    for (Node node = root; node != null; node = node.getNext()) {
      if (node.isCall() && node.getChildAtIndex(0).toString().startsWith("goog.provide")) {
        googProvideCall = node;
        break;
      }
    }
    assertNotNull(googProvideCall);

    Node googProvideArgs = googProvideCall.getChildAtIndex(1);
    assertEquals("square", googProvideArgs.getString());
  }
}