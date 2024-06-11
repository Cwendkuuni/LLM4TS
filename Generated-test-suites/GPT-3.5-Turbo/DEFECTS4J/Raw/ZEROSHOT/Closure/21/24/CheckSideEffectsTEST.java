import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testVisit() {
    // Test case 1
    Node n1 = new Node(Token.EMPTY);
    NodeTraversal t1 = new NodeTraversal();
    Node parent1 = new Node(Token.BLOCK);
    CheckSideEffects cse1 = new CheckSideEffects(new AbstractCompiler(), CheckLevel.ERROR, false);
    cse1.visit(t1, n1, parent1);
    assertEquals(0, parent1.getChildCount());
    
    // Test case 2
    Node n2 = new Node(Token.IF);
    NodeTraversal t2 = new NodeTraversal();
    Node parent2 = new Node(Token.BLOCK);
    CheckSideEffects cse2 = new CheckSideEffects(new AbstractCompiler(), CheckLevel.ERROR, false);
    cse2.visit(t2, n2, parent2);
    assertEquals(1, parent2.getChildCount());
  }

  @Test
  public void testProtectSideEffects() {
    // Test case 1
    CheckSideEffects cse1 = new CheckSideEffects(new AbstractCompiler(), CheckLevel.ERROR, false);
    cse1.protectSideEffects();
    assertEquals(0, cse1.problemNodes.size());

    // Test case 2
    CheckSideEffects cse2 = new CheckSideEffects(new AbstractCompiler(), CheckLevel.ERROR, false);
    cse2.problemNodes.add(new Node(Token.EMPTY));
    cse2.protectSideEffects();
    assertEquals(1, cse2.problemNodes.size());
  }

  @Test
  public void testAddExtern() {
    CheckSideEffects cse = new CheckSideEffects(new AbstractCompiler(), CheckLevel.ERROR, false);
    cse.addExtern();

    // TODO: Assertion
  }

  @Test
  public void testStripProtection() {
    // Test case 1
    Node n1 = new Node(Token.EXPR_RESULT);
    n1.addChildToFront(new Node(Token.CALL));
    CheckSideEffects.StripProtection sp1 = new CheckSideEffects.StripProtection(new AbstractCompiler());
    NodeTraversal t1 = new NodeTraversal();
    sp1.visit(t1, n1, null);
    // TODO: Assertion

    // Test case 2
    Node n2 = new Node(Token.EXPR_RESULT);
    n2.addChildToFront(new Node(Token.CALL));
    CheckSideEffects.StripProtection sp2 = new CheckSideEffects.StripProtection(new AbstractCompiler());
    NodeTraversal t2 = new NodeTraversal();
    sp2.visit(t2, n2, null);
    // TODO: Assertion
  }

}