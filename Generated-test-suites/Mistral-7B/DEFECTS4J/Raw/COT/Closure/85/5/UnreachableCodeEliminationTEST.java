import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.NodeType.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ScopedCallback;
import com.google.javascript.rhino.Token;

public class UnreachableCodeEliminationTest {

  @Test
  public void testUnreachableCodeElimination() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);

    // Test on a simple function
    Node root = createTestNode("/testFunction");
    traverse(compiler, root, elimination);
    assertEquals(expectedTestFunction(), root.toString(), "Test function validation");

    // Test on a function with a return statement and dead code
    Node rootWithDeadCode = createTestNode("/testFunctionWithDeadCode");
    traverse(compiler, rootWithDeadCode, elimination);
    assertEquals(expectedTestFunctionWithDeadCode(), rootWithDeadCode.toString(),
        "Test function with dead code validation");

    // Test on a function with a break statement and dead code
    Node rootWithBreak = createTestNode("/testFunctionWithBreak");
    traverse(compiler, rootWithBreak, elimination);
    assertEquals(expectedTestFunctionWithBreak(), rootWithBreak.toString(),
        "Test function with break statement validation");

    // Test on a function with a continue statement and dead code
    Node rootWithContinue = createTestNode("/testFunctionWithContinue");
    traverse(compiler, rootWithContinue, elimination);
    assertEquals(expectedTestFunctionWithContinue(), rootWithContinue.toString(),
        "Test function with continue statement validation");

    // Test on a function with exceptions
    Node rootWithException = createTestNode("/testFunctionWithException");
    traverse(compiler, rootWithException, elimination);
    assertEquals(expectedTestFunctionWithException(), rootWithException.toString(),
        "Test function with exception validation");
  }

  private Node createTestNode(String nodeName) {
    Node root = new Node(Token.SCRIPT);
    root.setPrintName(nodeName);

    Node function = new Node(Token.FUNCTION_DECLARATION);
    function.setPrintName("testFunction");
    root.addChild(function);

    Node body = new Node(BLOCK);
    function.addChild(body);

    return root;
  }

  private String expectedTestFunction() {
    String expected = "function testFunction() {}\n";
    return expected;
  }

  private String expectedTestFunctionWithDeadCode() {
    String expected = "function testFunctionWithDeadCode() {\n" +
        "  if (true) {\n" +
        "    return;\n" +
        "  } else if (false) {\n" +
        "    alert('Dead code');\n" +
        "  }\n" +
        "  return;\n" +
        "}\n";
    return expected;
  }

  private String expectedTestFunctionWithBreak() {
    String expected = "function testFunctionWithBreak() {\n" +
        "  for (var i = 0; i < 5; i++) {\n" +
        "    if (i === 3) {\n" +
        "      break;\n" +
        "    }\n" +
        "    alert(i);\n" +
        "  }\n" +
        "}\n";
    return expected;
  }

  private String expectedTestFunctionWithContinue() {
    String expected = "function testFunctionWithContinue() {\n" +
        "  for (var i = 0; i < 5; i++) {\n" +
        "    if (i === 3) {\n" +
        "      continue;\n" +
        "    }\n" +
        "    alert(i);\n" +
        "  }\n" +
        "}\n";
    return expected;
  }

  private String expectedTestFunctionWithException() {
    String expected = "function testFunctionWithException() {\n" +
        "  try {\n" +
        "    throw new Error('Test error');\n" +
        "  } catch (e) {\n" +
        "    return;\n" +
        "  } finally {\n" +
        "    return;\n" +
        "  }\n" +
        "}\n";
    return expected;
  }
}