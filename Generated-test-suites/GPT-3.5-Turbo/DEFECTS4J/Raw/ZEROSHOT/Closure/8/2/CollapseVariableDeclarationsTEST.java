import org.junit.Test;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseVariableDeclarations() {
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();

    // Test case 1: Single variable declaration
    Node varNode1 = new Node(Token.VAR);
    varNode1.addChildToFront(new Node(Token.NAME, "a"));
    Node parent1 = new Node(Token.BLOCK);
    parent1.addChildToBack(varNode1);
    CollapseVariableDeclarations.Collapse collapse1 = collapseVariableDeclarations.new Collapse(varNode1, varNode1, parent1);
    collapseVariableDeclarations.collapses.add(collapse1);

    collapseVariableDeclarations.applyCollapses();

    Node expectedVarNode1 = new Node(Token.VAR);
    expectedVarNode1.addChildToFront(new Node(Token.NAME, "a"));
    Node expectedParent1 = new Node(Token.BLOCK);
    expectedParent1.addChildToBack(expectedVarNode1);

    assertEquals(expectedParent1.toStringTree(), parent1.toStringTree());

    // Test case 2: Multiple variable declarations
    Node varNode2 = new Node(Token.VAR);
    varNode2.addChildToFront(new Node(Token.NAME, "b"));
    Node parent2 = new Node(Token.BLOCK);
    parent2.addChildToBack(varNode2);

    Node varNode3 = new Node(Token.VAR);
    varNode3.addChildToFront(new Node(Token.NAME, "c"));
    parent2.addChildToBack(varNode3);

    CollapseVariableDeclarations.Collapse collapse2 = collapseVariableDeclarations.new Collapse(varNode2, varNode3, parent2);
    collapseVariableDeclarations.collapses.add(collapse2);

    collapseVariableDeclarations.applyCollapses();

    Node expectedVarNode2 = new Node(Token.VAR);
    expectedVarNode2.addChildToFront(new Node(Token.NAME, "b"));
    expectedVarNode2.addChildToBack(new Node(Token.NAME, "c"));
    Node expectedParent2 = new Node(Token.BLOCK);
    expectedParent2.addChildToBack(expectedVarNode2);

    assertEquals(expectedParent2.toStringTree(), parent2.toStringTree());

    // Test case 3: Variable declarations with assignments
    Node varNode4 = new Node(Token.VAR);
    varNode4.addChildToFront(new Node(Token.NAME, "a"));
    parent2.addChildToBack(varNode4);

    Node varNode5 = new Node(Token.VAR);
    varNode5.addChildToFront(new Node(Token.NAME, "b"));
    parent2.addChildToBack(varNode5);

    Node assignNode1 = new Node(Token.ASSIGN);
    assignNode1.addChildToFront(new Node(Token.NAME, "a"));
    assignNode1.addChildToBack(new Node(Token.TRUE));

    Node assignNode2 = new Node(Token.ASSIGN);
    assignNode2.addChildToFront(new Node(Token.NAME, "b"));
    assignNode2.addChildToBack(new Node(Token.TRUE));

    parent2.addChildToBack(assignNode1);
    parent2.addChildToBack(assignNode2);

    CollapseVariableDeclarations.Collapse collapse3 = collapseVariableDeclarations.new Collapse(varNode4, assignNode2, parent2);
    collapseVariableDeclarations.collapses.add(collapse3);

    collapseVariableDeclarations.applyCollapses();

    Node expectedVarNode3 = new Node(Token.VAR);
    expectedVarNode3.addChildToFront(new Node(Token.NAME, "a"));
    expectedVarNode3.addChildToBack(new Node(Token.NAME, "b"));
    Node expectedParent3 = new Node(Token.BLOCK);
    expectedParent3.addChildToBack(expectedVarNode3);

    Node expectedAssignNode1 = new Node(Token.ASSIGN);
    expectedAssignNode1.addChildToFront(new Node(Token.NAME, "a"));
    expectedAssignNode1.addChildToBack(new Node(Token.TRUE));

    Node expectedAssignNode2 = new Node(Token.ASSIGN);
    expectedAssignNode2.addChildToFront(new Node(Token.NAME, "b"));
    expectedAssignNode2.addChildToBack(new Node(Token.TRUE));

    expectedParent3.addChildToBack(expectedAssignNode1);
    expectedParent3.addChildToBack(expectedAssignNode2);

    assertEquals(expectedParent3.toStringTree(), parent2.toStringTree());
  }
}