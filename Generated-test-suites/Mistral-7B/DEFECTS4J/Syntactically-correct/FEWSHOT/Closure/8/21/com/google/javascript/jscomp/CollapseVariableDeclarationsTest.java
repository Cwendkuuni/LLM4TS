package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.test.base.AbstractCompilerTest;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

/**
 * Test case for CollapseVariableDeclarations class.
 */
public class CollapseVariableDeclarationsTest extends AbstractCompilerTest {

  public CollapseVariableDeclarationsTest() {
    super(CompilerOptions.standard());
  }

  @Override
  protected CompilerPass getProcessor(Compiler compiler) {
    return new CollapseVariableDeclarations(compiler);
  }

  /**
   * Test collapsing multiple variable declarations into a single one.
   */
  @Test
  public void testCollapseMultipleVariableDeclarations() {
    String sourceCode =
        "function foo() {"
            + "var a;"
            + "var b = 1;"
            + "var c = 2;"
            + "}";

    Node rootNode = parse(sourceCode);

    process(rootNode);

    Node functionNode = findNodeByTypeAndName(rootNode, Node.FUNCTION, "foo");
    List<Node> variables = Lists.newArrayList();
    Node currentVariable = null;
    for (Node child = functionNode.getFirstChild(); child != null;
        child = child.getNext()) {
      if (child.isVar()) {
        if (currentVariable != null) {
          variables.add(currentVariable);
          currentVariable = null;
        }
        currentVariable = child;
      }
    }

    // There should be only one variable node left.
    Node expectedVariableNode = variables.get(0);
    Node actualVariableNode = functionNode.getLastChild();

    // Check that the type and name are the same.
    assertThat(expectedVariableNode.getType()).isEqualTo(actualVariableNode.getType());
    assertThat(expectedVariableNode.getString()).isEqualTo(actualVariableNode.getString());

    // Check that all the previous variable nodes have been added as children of
    // the new variable node.
    for (Node variable : variables.subList(0, variables.size() - 1)) {
      expectedVariableNode.addChildToBack(variable);
    }

    // Check that the order of the children is the same.
    Node expectedChild = null;
    for (Node child = expectedVariableNode.getFirstChild(); child != null;
        child = child.getNext()) {
      Node actualChild = actualVariableNode.getFirstChild();
      if (expectedChild == null) {
        expectedChild = child;
      } else {
        Node nextExpectedChild = expectedChild.getNext();
        Node nextActualChild = actualChild.getNext();
        assertThat(nextExpectedChild).isEqualTo(nextActualChild);
        expectedChild = nextExpectedChild;
      }
      // Check that the information is the same.
      assertThat(child.getJSDocInfo()).isEqualTo(actualChild.getJSDocInfo());
      // Check that the children are the same.
      Node childFirstChild = child.getFirstChild();
      Node actualChildFirstChild = actualChild.getFirstChild();
      if (childFirstChild != null) {
        Node nextChildFirstChild = childFirstChild.getNext();
        Node nextActualChildFirstChild = actualChildFirstChild.getNext();
        // Recursively check that the children are the same.
        testChildrenAreEqual(nextChildFirstChild, nextActualChildFirstChild);
      }
      child = nextChildFirstChild;
      actualChild = nextActualChild;
    }
  }

  /**
   * Test collapsing multiple variable assignments into a single one.
   */
  @Test
  public void testCollapseMultipleVariableAssignments() {
    String sourceCode =
        "function foo() {"
            + "a = true;"
            + "b = true;"
            + "var c = true;"
            + "}";

    Node rootNode = parse(sourceCode);

    process(rootNode);

    Node functionNode = findNodeByTypeAndName(rootNode, Node.FUNCTION, "foo");
    Node expectedVariableNode = null;
    Node actualVariableNode = null;
    for (Node child = functionNode.getFirstChild(); child != null;
        child = child.getNext()) {
      if (child.isAssign()) {
        Node assign = child;
        Node lhs = assign.getFirstChild();
        if (lhs.isName()) {
          if (expectedVariableNode == null) {
            expectedVariableNode = assign;
          } else {
            // The previous assignment should have been collapsed into this one.
            Node previousAssignment = expectedVariableNode;
            expectedVariableNode = assign;
            Node previousAssignmentLhs = previousAssignment.getFirstChild();
            Node previousAssignmentRhs = previousAssignment.getLastChild();

            // Check that the LHS of the previous assignment is a child of the new
            // variable node.
            Node expectedChild = null;
            for (Node grandChild = previousAssignmentLhs.getFirstChild();
                grandChild != null;
                grandChild = grandChild.getNext()) {
              if (grandChild.isName() && grandChild.getString().equals(lhs.getString())) {
                expectedChild = grandChild;
                break;
              }
            }
            Preconditions.checkState(expectedChild != null);
            expectedVariableNode.addChildToBack(expectedChild);

            // Collapse the RHS of the previous assignment into the new variable node.
            Node nextChild = lhs;
            while (nextChild.getNext() != null) {
              nextChild = nextChild.getNext();
            }
            Node nextChildRhs = nextChild.getNext();
            nextChild.setNext(nextChildRhs.getNext());
            nextChildRhs.setNext(null);
            expectedVariableNode.addChildToBack(nextChildRhs);
          }
        }
      } else if (child.isVar()) {
        if (expectedVariableNode == null) {
          // This is the first variable declaration.
          Node variable = child;
          expectedVariableNode = variable;
        } else {
          // This is a redundant variable declaration.
          assertNull(child);
        }
      }
    }

    // Check that there is only one variable node left.
    Node actualVariableNode = functionNode.getLastChild();

    // Check that all the children of the new variable node are the RHS of the
    // collapsible assignments.
    Node expectedChild = null;
    for (Node child = expectedVariableNode.getFirstChild(); child != null;
        child = child.getNext()) {
      Node actualChild = actualVariableNode.getFirstChild();
      if (expectedChild == null) {
        expectedChild = child;
      } else {
        Node nextExpectedChild = expectedChild.getNext();
        Node nextActualChild = actualChild.getNext();
        // Recursively check that the children are the same.
        testChildrenAreEqual(nextExpectedChild, nextActualChild);
        expectedChild = nextExpectedChild;
      }
    }
  }

  private void testChildrenAreEqual(Node expectedChild, Node actualChild) {
    if (expectedChild == null) {
      assertNull(actualChild);
      return;
    }

    if (expectedChild.isName()) {
      assertThat(expectedChild.getString()).isEqualTo(actualChild.getString());
    } else if (expectedChild.isNumberLiteral()) {
      assertThat(expectedChild.getNumberValue()).isEqualTo(actualChild.getNumberValue());
    } else if (expectedChild.isStringLiteral()) {
      assertThat(expectedChild.getStringValue()).isEqualTo(actualChild.getStringValue());
    } else if (expectedChild.isBooleanLiteral()) {
      assertThat(expectedChild.getBooleanValue()).isEqualTo(actualChild.getBooleanValue());
      // Recursively check that the children are the same.
      Node expectedChildFirstChild = expectedChild.getFirstChild();
      Node actualChildFirstChild = actualChild.getFirstChild();
      testChildrenAreEqual(expectedChildFirstChild, actualChildFirstChild);
    }
  }
}