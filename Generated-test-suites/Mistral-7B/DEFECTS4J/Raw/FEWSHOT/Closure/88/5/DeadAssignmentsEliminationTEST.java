package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    // Test case 1: Dead assignment elimination for simple assignments
    Node root = createNode("1; x = 1; y = 1;");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(null);
    deadAssignmentsElimination.process(null, root);
    assertFalse(isAssignmentsPresent(root, "x"));
    assertFalse(isAssignmentsPresent(root, "y"));

    // Test case 2: Dead assignment elimination for increment and decrement operators
    root = createNode("1; x = 1; x++; y = 1; y--;");
    deadAssignmentsElimination.process(null, root);
    assertFalse(isAssignmentsPresent(root, "x"));
    assertFalse(isAssignmentsPresent(root, "y"));

    // Test case 3: Dead assignment elimination for assignments within conditional statements
    root = createNode("if (false) { x = 1; } y = 1;");
    deadAssignmentsElimination.process(null, root);
    assertFalse(isAssignmentsPresent(root, "x"));
    assertFalse(isAssignmentsPresent(root, "y"));

    // Test case 4: Dead assignment elimination for assignments within loops
    root = createNode("for (i = 0; i < 10; i++) { x = i; } y = 1;");
    deadAssignmentsElimination.process(null, root);
    assertFalse(isAssignmentsPresent(root, "x"));
    assertFalse(isAssignmentsPresent(root, "y"));

    // Test case 5: Dead assignment elimination for assignments within switch statements
    root = createNode("switch (1) { case 1: x = 1; case 2: y = 1; }");
    deadAssignmentsElimination.process(null, root);
    assertFalse(isAssignmentsPresent(root, "x"));
    assertFalse(isAssignmentsPresent(root, "y"));

    // Test case 6: Dead assignment elimination for assignments within return statements
    root = createNode("function f() { x = 1; return 1; } y = 1;");
    deadAssignmentsElimination.process(null, root);
    assertFalse(isAssignmentsPresent(root, "x"));
    assertFalse(isAssignmentsPresent(root, "y"));

    // Test case 7: Dead assignment elimination for assignments within function calls
    root = createNode("f(); x = 1; function f() {} y = 1;");
    deadAssignmentsElimination.process(null, root);
    assertFalse(isAssignmentsPresent(root, "x"));
    assertFalse(isAssignmentsPresent(root, "y"));

    // Test case 8: Dead assignment elimination for assignments within expressions
    root = createNode("x = 1; 2 + x; y = 1; z = 3 + y;");
    deadAssignmentsElimination.process(null, root);
    assertFalse(isAssignmentsPresent(root, "x"));
    assertFalse(isAssignmentsPresent(root, "y"));

    // Test case 9: Dead assignment elimination for variables that are not live
    root = createNode("x = 1; if (false) { y = 1; } x = 2;");
    deadAssignmentsElimination.process(null, root);
    assertFalse(isAssignmentsPresent(root, "x"));
    assertFalse(isAssignmentsPresent(root, "y"));

    // Test case 10: Dead assignment elimination for variables that are live but not read after the assignment
    root = createNode("x = 1; if (true) { y = 1; } x = 2; print(x);");
    deadAssignmentsElimination.process(null, root);
    assertFalse(isAssignmentsPresent(root, "x"));
    assertTrue(isAssignmentsPresent(root, "y"));
  }

  private Node createNode(String code) {
    // Create Rhino Node representation of the given JavaScript code.
    JSContext context = JSContext.enter();
    JSFunction func = context.evaluateString(code, "test", 1);
    JSFunction rootNode = JSFunction.getPrototype(func);
    JSContext.exit();
    return Node.toAbstractSyntactic(rootNode);
  }

  private boolean isAssignmentsPresent(Node root, String variable) {
    return NodeUtil.has(root, new SimpleNodePredicate() {
      @Override
      public boolean apply(Node n) {
        if (NodeUtil.isName(n) && n.getString().equals(variable)) {
          return NodeUtil.isAssignmentOp(n.getParent())
                  || NodeUtil.isAssign(n.getParent());
        }
        return false;
      }
    });
  }
}