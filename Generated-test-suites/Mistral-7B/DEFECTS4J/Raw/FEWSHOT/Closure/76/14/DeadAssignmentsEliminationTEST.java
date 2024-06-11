package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DeadAssignmentsEliminationTest {
  @Test
  public void testDeadAssignmentsElimination() {
    // Test cases for dead assignments elimination

    // Case 1: Dead assignment with no read after
    String code1 = "function foo() {\n" +
        "  var x = 1;\n" +
        "  x = 2;\n" +
        "}";

    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(null);
    AbstractCompiler compiler = new Compiler(null);
    Node root = Parser.parse(code1, null, null);
    elimination.process(null, root);
    assertTrue(NodeUtil.getChildrenCount(root) == 1);

    // Case 2: Dead assignment with read after
    String code2 = "function foo() {\n" +
        "  var x = 1;\n" +
        "  x = 2;\n" +
        "  print(x);\n" +
        "}";

    elimination = new DeadAssignmentsElimination(null);
    compiler = new Compiler(null);
    root = Parser.parse(code2, null, null);
    elimination.process(null, root);
    assertTrue(NodeUtil.getChildrenCount(root) == 3);

    // Case 3: Dead assignment within loop
    String code3 = "function foo() {\n" +
        "  for (var i = 0; i < 10; i++) {\n" +
        "    var x = i;\n" +
        "    x = i + 1;\n" +
        "  } \n" +
        "}";

    elimination = new DeadAssignmentsElimination(null);
    compiler = new Compiler(null);
    root = Parser.parse(code3, null, null);
    elimination.process(null, root);
    assertTrue(NodeUtil.getChildrenCount(root) == 2);

    // Case 4: Dead assignment within conditional statement
    String code4 = "function foo() {\n" +
        "  if (true) {\n" +
        "    var x = 1;\n" +
        "    x = 2;\n" +
        "  } else {\n" +
        "    var y = 1;\n" +
        "  }\n" +
        "}";

    elimination = new DeadAssignmentsElimination(null);
    compiler = new Compiler(null);
    root = Parser.parse(code4, null, null);
    elimination.process(null, root);
    assertTrue(NodeUtil.getChildrenCount(root) == 2);

    // Case 5: Dead assignment within switch statement
    String code5 = "function foo() {\n" +
        "  var x = 1;\n" +
        "  switch (true) {\n" +
        "    case 1:\n" +
        "      x = 2;\n" +
        "      break;\n" +
        "    case 2:\n" +
        "      break;\n" +
        "  }\n" +
        "}";

    elimination = new DeadAssignmentsElimination(null);
    compiler = new Compiler(null);
    root = Parser.parse(code5, null, null);
    elimination.process(null, root);
    assertTrue(NodeUtil.getChildrenCount(root) == 1);

    // Case 6: Dead assignment within function call
    String code6 = "function foo() {\n" +
        "  function bar() {\n" +
        "    var x = 1;\n" +
        "    x = 2;\n" +
        "  }\n" +
        "  bar();\n" +
        "}";

    elimination = new DeadAssignmentsElimination(null);
    compiler = new Compiler(null);
    root = Parser.parse(code6, null, null);
    elimination.process(null, root);
    assertTrue(NodeUtil.getChildrenCount(root) == 1);

    // Case 7: Dead assignment within return statement
    String code7 = "function foo() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "  x = 2;\n" +
        "}";

    elimination = new DeadAssignmentsElimination(null);
    compiler = new Compiler(null);
    root = Parser.parse(code7, null, null);
    elimination.process(null, root);
    assertTrue(NodeUtil.getChildrenCount(root) == 1);

    // Case 8: Dead assignment within increment/decrement operator
    String code8 = "function foo() {\n" +
        "  var x = 1;\n" +
        "  x++;\n" +
        "  x = 2;\n" +
        "}";

    elimination = new DeadAssignmentsElimination(null);
    compiler = new Compiler(null);
    root = Parser.parse(code8, null, null);
    elimination.process(null, root);
    assertTrue(NodeUtil.getChildrenCount(root) == 1);

    // Case 9: Dead assignment within assignment operator
    String code9 = "function foo() {\n" +
        "  var x = 1;\n" +
        "  x = x + 1;\n" +
        "  x = 2;\n" +
        "}";

    elimination = new DeadAssignmentsElimination(null);
    compiler = new Compiler(null);
    root = Parser.parse(code9, null, null);
    elimination.process(null, root);
    assertTrue(NodeUtil.getChildrenCount(root) == 1);

    // Case 10: Dead assignment within expression
    String code10 = "function foo() {\n" +
        "  var x = 1;\n" +
        "  if (x = 2) {\n" +
        "    print('X is 2');\n" +
        "  } else {\n" +
        "    print('X is not 2');\n" +
        "  }\n" +
        "}";

    elimination = new DeadAssignmentsElimination(null);
    compiler = new Compiler(null);
    root = Parser.parse(code10, null, null);
    elimination.process(null, root);
    assertTrue(NodeUtil.getChildrenCount(root) == 2);

    // Case 11: Dead assignment within complex expression
    String code11 = "function foo() {\n" +
        "  var x = 1;\n" +
        "  var y = x > 1 ? 2 : 3;\n" +
        "  x = 2;\n" +
        "  print(y);\n" +
        "}";

    elimination = new DeadAssignmentsElimination(null);
    compiler = new Compiler(null);
    root = Parser.parse(code11, null, null);
    elimination.process(null, root);
    assertTrue(NodeUtil.getChildrenCount(root) == 3);

    // Case 12: Dead assignment within complex control flow
    String code12 = "function foo() {\n" +
        "  if (true) {\n" +
        "    var x = 1;\n" +
        "    if (false) {\n" +
        "      x = 2;\n" +
        "    } else {\n" +
        "      x = 3;\n" +
        "    }\n" +
        "  } else {\n" +
        "    var y = 1;\n" +
        "  }\n" +
        "  print(x);\n" +
        "}";

    elimination = new DeadAssignmentsElimination(null);
    compiler = new Compiler(null);
    root = Parser.parse(code12, null, null);
    elimination.process(null, root);
    assertTrue(NodeUtil.getChildrenCount(root) == 3);
  }

  @Test
  public void testIsVariableStillLiveWithinExpression() {
    // Test cases for isVariableStillLiveWithinExpression() method

    // Case 1: Dead assignment within simple expression
    Node n1 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NUMBER, 2));
    String variable1 = "x";
    assertTrue(DeadAssignmentsElimination.isVariableStillLiveWithinExpression(n1, n1, variable1));

    // Case 2: Dead assignment within complex expression
    Node n2 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.BINARY_AND, new Node(Token.NAME, "y"), new Node(Token.NAME, "z")));
    String variable2 = "x";
    assertFalse(DeadAssignmentsElimination.isVariableStillLiveWithinExpression(n2, n2, variable2));

    // Case 3: Dead assignment within complex expression with read before
    Node n3 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.BINARY_OR, new Node(Token.NAME, "y"), new Node(Token.NAME, "z")));
    String variable3 = "x";
    assertTrue(DeadAssignmentsElimination.isVariableStillLiveWithinExpression(n3, n3, variable3));

    // Case 4: Dead assignment within complex expression with write after
    Node n4 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.BINARY_OR, new Node(Token.ASSIGN, new Node(Token.NAME, "y"), new Node(Token.NUMBER, 1)), new Node(Token.NAME, "z")));
    String variable4 = "x";
    assertFalse(DeadAssignmentsElimination.isVariableStillLiveWithinExpression(n4, n4, variable4));
  }
}