import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TernaryValue;
import org.junit.Test;

public class MinimizeExitPointsTest {

  @Test
  public void testMinimizeExitPoints() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass = new MinimizeExitPoints(compiler);

    // Simple test case with no explicit exits.
    String input = "function test() {}";
    Node root = Node.parse(input);
    pass.process(null, root);
    assertFalse(minimizeExitsPresent(root));

    // Test case with a return statement.
    input = "function test() { return 1; }";
    root = Node.parse(input);
    pass.process(null, root);
    assertFalse(minimizeExitsPresent(root));

    // Test case with a break statement outside a loop.
    input = "function test() { break; }";
    root = Node.parse(input);
    pass.process(null, root);
    assertFalse(minimizeExitsPresent(root));

    // Test case with a break statement inside a loop.
    input = "function test() { for (var i = 0; i < 10; i++) { break; } }";
    root = Node.parse(input);
    pass.process(null, root);
    assertFalse(minimizeExitsPresent(root));

    // Test case with a continue statement outside a loop.
    input = "function test() { continue; }";
    root = Node.parse(input);
    pass.process(null, root);
    assertFalse(minimizeExitsPresent(root));

    // Test case with a continue statement inside a loop.
    input = "function test() { for (var i = 0; i < 10; i++) { continue; } }";
    root = Node.parse(input);
    pass.process(null, root);
    assertFalse(minimizeExitsPresent(root));

    // Test case with a nested loop.
    input = "function test() { for (var i = 0; i < 10; i++) { for (var j = 0; j < 10; j++) { continue; } } }";
    root = Node.parse(input);
    pass.process(null, root);
    assertFalse(minimizeExitsPresent(root));

    // Test case with a label and a break statement.
    input = "test: function test() { while (true) { break test; } }";
    root = Node.parse(input);
    pass.process(null, root);
    assertFalse(minimizeExitsPresent(root));

    // Test case with a label and a break statement inside a loop.
    input = "test: function test() { while (true) { for (var i = 0; i < 10; i++) { break test; } } }";
    root = Node.parse(input);
    pass.process(null, root);
    assertFalse(minimizeExitsPresent(root));

    // Test case with a try-catch block.
    input = "function test() { try { throw new Error(); } catch (e) {} }";
    root = Node.parse(input);
    pass.process(null, root);
    assertFalse(minimizeExitsPresent(root));

    // Test case with a try-catch block and a finally block.
    input = "function test() { try { throw new Error(); } finally {} }";
    root = Node.parse(input);
    pass.process(null, root);
    assertFalse(minimizeExitsPresent(root));

    // Test case with a switch statement.
    input = "function test() { switch (5) { case 5: return; } }";
    root = Node.parse(input);
    pass.process(null, root);
    assertFalse(minimizeExitsPresent(root));

    // Test case with a nested if statement.
    input = "function test() { if (true) { if (true) { return; } } }";
    root = Node.parse(input);
    pass.process(null, root);
    assertFalse(minimizeExitsPresent(root));

    // Test case with nested if-else statements.
    input = "function test() { if (true) { if (false) { return; } else { return; } } }";
    root = Node.parse(input);
    pass.process(null, root);
    assertFalse(minimizeExitsPresent(root));

    // Test case with a complex input.
    input = "function test() {"
      + "  var _intercepted = false;"
      + "  try {"
      + "    for (var i = 0; i < 10; i++) {"
      + "      if (i % 2 == 0) {"
      + "        if (_intercepted) {"
      + "          continue;"
      + "        } else {"
      + "          _intercepted = true;"
      + "          continue;"
      + "        }"
      + "      } else {"
      + "        if (_intercepted)"
      + "          return;"
      + "      }"
      + "    }"
      + "  } finally {"
      + "    if (_intercepted) {"
      + "      return;"
      + "    }"
      + "  }"
      + "}";
    root = Node.parse(input);
    pass.process(null, root);
    assertFalse(minimizeExitsPresent(root));
  }

  private static boolean minimizeExitsPresent(Node root) {
    NodeTraversal nt = new NodeTraversal();
    nt.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal traversal, Node node, Node parent) {
        switch (node.getType()) {
          case Token.LABEL:
          case Token.RETURN:
          case Token.BREAK:
          case Token.CONTINUE:
            assertNotNull(node);
            return true;
        }
      }
    }, root);
    return false;
  }
}