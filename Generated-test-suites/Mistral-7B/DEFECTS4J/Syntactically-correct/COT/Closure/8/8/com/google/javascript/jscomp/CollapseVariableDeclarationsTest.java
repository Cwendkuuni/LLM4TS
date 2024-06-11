import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope.Var.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.junit.Test;

import java.util.List;
import java.util.Set;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseVariableDeclarationsBasic() {
    CollapseVariableDeclarations collapsedVariableDeclarations = new CollapseVariableDeclarations(null);

    Node root = new Node(Token.PROGRAM);
    Node node1 = new Node(Token.VAR, new Node("a"));
    Node node2 = new Node(Token.VAR, new Node("b"));
    Node node3 = new Node(Token.VAR, new Node("c"));

    root.addChildToBack(node1);
    root.addChildToBack(node2);
    root.addChildToBack(node3);

    collapsedVariableDeclarations.process(null, root);

    Node expected = new Node(Token.VAR, Lists.newArrayList(
        new Node("a"),
        new Node("b"),
        new Node("c")
    ));

    Node actual = root;
    while (actual.getFirstChild() != null) {
      actual = actual.getFirstChild();
    }

    assertEquals(expected, actual);
  }

  @Test
  public void testCollapseVariableAssignmentsBasic() {
    CollapseVariableDeclarations collapsedVariableDeclarations = new CollapseVariableDeclarations(null);

    Node root = new Node(Token.PROGRAM);
    Node node1 = new Node(Token.ASSIGN, new Node("a"), new Node(Token.TRUE));
    Node node2 = new Node(Token.ASSIGN, new Node("b"), new Node(Token.TRUE));
    Node node3 = new Node(Token.VAR, new Node("c"));

    root.addChildToBack(node1);
    root.addChildToBack(node2);
    root.addChildToBack(node3);

    collapsedVariableDeclarations.process(null, root);

    Node expected = new Node(Token.VAR, Lists.newArrayList(
        new Node("c"),
        new Node("a"),
        new Node("b")
    ));

    Node actual = root;
    while (actual.getFirstChild() != null) {
      actual = actual.getFirstChild();
    }

    assertEquals(expected, actual);
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariableDeclarationsInsideIfStatement() {
    CollapseVariableDeclarations collapsedVariableDeclarations = new CollapseVariableDeclarations(null);

    Node root = new Node(Token.PROGRAM);
    Node ifNode = new Node(Token.IF, new Node(Token.LT, new Node("x"), new Node(Token.INT_LITERAL, 10)));
    Node varNode1 = new Node(Token.VAR, new Node("a"));
    Node varNode2 = new Node(Token.VAR, new Node("b"));

    ifNode.addChildToBack(varNode1);
    ifNode.addChildToBack(varNode2);
    root.addChildToBack(ifNode);

    collapsedVariableDeclarations.process(null, root);

    fail("Variable declarations inside an If statement should not be collapsed.");
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariableDeclarationsInsideFunction() {
    CollapseVariableDeclarations collapsedVariableDeclarations = new CollapseVariableDeclarations(null);

    Node root = new Node(Token.PROGRAM);
    Node functionNode = new Node(Token.FUNCTION, null, new Node("myFunction"), null, null);
    Node varNode1 = new Node(Token.VAR, new Node("a"));
    Node varNode2 = new Node(Token.VAR, new Node("b"));

    functionNode.addChildToBack(varNode1);
    functionNode.addChildToBack(varNode2);
    root.addChildToBack(functionNode);

    collapsedVariableDeclarations.process(null, root);

    fail("Variable declarations inside a function should not be collapsed.");
  }

  @Test
  public void testCollapseVariableDeclarationsWithSameVariableName() {
    CollapseVariableDeclarations collapsedVariableDeclarations = new CollapseVariableDeclarations(null);

    Node root = new Node(Token.PROGRAM);
    Node node1 = new Node(Token.VAR, new Node("a"));
    Node node2 = new Node(Token.VAR, new Node("a"));

    root.addChildToBack(node1);
    root.addChildToBack(node2);

    collapsedVariableDeclarations.process(null, root);

    Node expected = new Node(Token.VAR, Lists.newArrayList(
        new Node("a")
    ));

    Node actual = root;
    while (actual.getFirstChild() != null) {
      actual = actual.getFirstChild();
    }

    assertEquals(expected, actual);
  }

  @Test
  public void testCollapseVariableDeclarationsWithDifferentVariableNames() {
    CollapseVariableDeclarations collapsedVariableDeclarations = new CollapseVariableDeclarations(null);

    Node root = new Node(Token.PROGRAM);
    Node node1 = new Node(Token.VAR, new Node("a"));
    Node node2 = new Node(Token.VAR, new Node("b"));

    root.addChildToBack(node1);
    root.addChildToBack(node2);

    collapsedVariableDeclarations.process(null, root);

    Node expected = new Node(Token.VAR, Lists.newArrayList(
        new Node("a"),
        new Node("b")
    ));

    Node actual = root;
    while (actual.getFirstChild() != null) {
      actual = actual.getFirstChild();
    }

    assertEquals(expected, actual);
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariableDeclarationsWithDuplicateVariableDeclarations() {
    CollapseVariableDeclarations collapsedVariableDeclarations = new CollapseVariableDeclarations(null);

    Node root = new Node(Token.PROGRAM);
    Node node1 = new Node(Token.VAR, new Node("a"));
    Node node2 = new Node(Token.VAR, new Node("a"));

    root.addChildToBack(node1);
    root.addChildToBack(node2);

    collapsedVariableDeclarations.process(null, root);

    fail("Duplicate variable declarations should throw an exception.");
  }

  @Test
  public void testCollapseVariableDeclarationsWithNodesToCollapse() {
    CollapseVariableDeclarations collapsedVariableDeclarations = new CollapseVariableDeclarations(null);

    Node root = new Node(Token.PROGRAM);
    Node node1 = new Node(Token.VAR, new Node("a"));
    Node node2 = new Node(Token.VAR, new Node("b"));
    Node node3 = new Node(Token.VAR, new Node("c"));
    Node node4 = new Node(Token.VAR, new Node("d"));

    root.addChildToBack(node1);
    root.addChildToBack(node2);
    root.addChildToBack(node3);
    root.addChildToBack(node4);

    collapsedVariableDeclarations.process(null, root);

    Node expected = new Node(Token.VAR, Lists.newArrayList(
        new Node("a"),
        new Node("b"),
        new Node("c"),
        new Node("d")
    ));

    Node actual = root;
    while (actual.getFirstChild() != null) {
      actual = actual.getFirstChild();
    }

    assertEquals(expected, actual);
  }
}