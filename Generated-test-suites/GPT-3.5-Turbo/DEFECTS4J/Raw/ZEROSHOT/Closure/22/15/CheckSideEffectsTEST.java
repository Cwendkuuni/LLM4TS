import static org.junit.Assert.*;
import org.junit.Test;

public class CheckSideEffectsTest {
  
  @Test
  public void testVisit_noSideEffectNode_returnsNoError() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    CheckSideEffects checkSideEffects = new CheckSideEffects();
    
    // Execute
    checkSideEffects.visit(t, n, parent);
    
    // Assert
    assertTrue(t.getErrors().isEmpty());
  }
  
  @Test
  public void testVisit_emptyNode_returnsNoError() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    CheckSideEffects checkSideEffects = new CheckSideEffects();
    
    // Execute
    checkSideEffects.visit(t, n, parent);
    
    // Assert
    assertTrue(t.getErrors().isEmpty());
  }
  
  @Test
  public void testVisit_commaNode_returnsNoError() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.COMMA);
    Node parent = new Node();
    CheckSideEffects checkSideEffects = new CheckSideEffects();
    
    // Execute
    checkSideEffects.visit(t, n, parent);
    
    // Assert
    assertTrue(t.getErrors().isEmpty());
  }
  
  @Test
  public void testVisit_exprResultNode_returnsNoError() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.EXPR_RESULT);
    Node parent = new Node();
    CheckSideEffects checkSideEffects = new CheckSideEffects();
    
    // Execute
    checkSideEffects.visit(t, n, parent);
    
    // Assert
    assertTrue(t.getErrors().isEmpty());
  }
  
  @Test
  public void testVisit_qualifiedNameNodeWithJSDoc_returnsNoError() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setQualifiedName(true);
    n.setJSDocInfo(new JSDocInfoBuilder().build());
    Node parent = new Node();
    CheckSideEffects checkSideEffects = new CheckSideEffects();
    
    // Execute
    checkSideEffects.visit(t, n, parent);
    
    // Assert
    assertTrue(t.getErrors().isEmpty());
  }
  
  @Test
  public void testVisit_qualifiedNameNodeWithoutJSDoc_returnsError() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setQualifiedName(true);
    Node parent = new Node();
    CheckSideEffects checkSideEffects = new CheckSideEffects();
    
    // Execute
    checkSideEffects.visit(t, n, parent);
    
    // Assert
    assertFalse(t.getErrors().isEmpty());
  }
  
  @Test
  public void testVisit_nonExpressionResultNodeWithoutAncestor_returnsError() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.ADD);
    Node parent = new Node();
    CheckSideEffects checkSideEffects = new CheckSideEffects();
    
    // Execute
    checkSideEffects.visit(t, n, parent);
    
    // Assert
    assertFalse(t.getErrors().isEmpty());
  }
  
  @Test
  public void testVisit_nonExpressionResultNodeWithCommaAncestor_returnsNoError() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.ADD);
    Node parent = new Node();
    parent.setType(Token.COMMA);
    Node gramps = new Node();
    gramps.setType(Token.CALL);
    gramps.addChildToFront(parent);
    parent.addChildToBack(n);
    Node greatGramps = new Node();
    greatGramps.addChildToBack(gramps);
    CheckSideEffects checkSideEffects = new CheckSideEffects();
    
    // Execute
    checkSideEffects.visit(t, n, parent);
    
    // Assert
    assertTrue(t.getErrors().isEmpty());
  }
  
  @Test
  public void testVisit_nonExpressionResultNodeWithExprResultAncestor_returnsNoError() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.ADD);
    Node parent = new Node();
    parent.setType(Token.EXPR_RESULT);
    Node gramps = new Node();
    gramps.setType(Token.CALL);
    gramps.addChildToFront(parent);
    parent.addChildToBack(n);
    Node greatGramps = new Node();
    greatGramps.addChildToBack(gramps);
    CheckSideEffects checkSideEffects = new CheckSideEffects();
    
    // Execute
    checkSideEffects.visit(t, n, parent);
    
    // Assert
    assertTrue(t.getErrors().isEmpty());
  }
  
  @Test
  public void testVisit_nonExpressionResultNodeWithBlockAncestor_returnsNoError() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.ADD);
    Node parent = new Node();
    parent.setType(Token.BLOCK);
    Node gramps = new Node();
    gramps.setType(Token.CALL);
    gramps.addChildToFront(parent);
    parent.addChildToBack(n);
    Node greatGramps = new Node();
    greatGramps.addChildToBack(gramps);
    CheckSideEffects checkSideEffects = new CheckSideEffects();
    
    // Execute
    checkSideEffects.visit(t, n, parent);
    
    // Assert
    assertTrue(t.getErrors().isEmpty());
  }
  
  @Test
  public void testVisit_nonExpressionResultNodeWithNoOpStatement_returnsNoError() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.ADD);
    Node parent = new Node();
    parent.setType(Token.BLOCK);
    Node gramps = new Node();
    gramps.setType(Token.NO_OP);
    gramps.addChildToFront(parent);
    parent.addChildToBack(n);
    Node greatGramps = new Node();
    greatGramps.addChildToBack(gramps);
    CheckSideEffects checkSideEffects = new CheckSideEffects();
    
    // Execute
    checkSideEffects.visit(t, n, parent);
    
    // Assert
    assertTrue(t.getErrors().isEmpty());
  }
  
  @Test
  public void testVisit_nonExpressionResultNodeWithNoOpStatementWithJSDoc_returnsNoError() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.ADD);
    Node parent = new Node();
    parent.setType(Token.BLOCK);
    Node gramps = new Node();
    gramps.setType(Token.NO_OP);
    gramps.addChildToFront(parent);
    gramps.setJSDocInfo(new JSDocInfoBuilder().build());
    parent.addChildToBack(n);
    Node greatGramps = new Node();
    greatGramps.addChildToBack(gramps);
    CheckSideEffects checkSideEffects = new CheckSideEffects();
    
    // Execute
    checkSideEffects.visit(t, n, parent);
    
    // Assert
    assertTrue(t.getErrors().isEmpty());
  }
  
  @Test
  public void testVisit_nonExpressionResultNodeWithNoOpStatementWithoutJSDoc_returnsError() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.ADD);
    Node parent = new Node();
    parent.setType(Token.BLOCK);
    Node gramps = new Node();
    gramps.setType(Token.NO_OP);
    gramps.addChildToFront(parent);
    parent.addChildToBack(n);
    parent.addChildToFront(gramps);
    Node greatGramps = new Node();
    greatGramps.addChildToBack(gramps);
    CheckSideEffects checkSideEffects = new CheckSideEffects();
    
    // Execute
    checkSideEffects.visit(t, n, parent);
    
    // Assert
    assertFalse(t.getErrors().isEmpty());
  }
  
  @Test
  public void testVisit_nonExpressionResultNode_underForAndFirstChild_returnsNoError() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.ADD);
    Node parent = new Node();
    parent.setType(Token.FOR);
    parent.addChildToBack(n);
    Node sibling1 = new Node();
    parent.addChildAfter(sibling1, n);
    CheckSideEffects checkSideEffects = new CheckSideEffects();
    
    // Execute
    checkSideEffects.visit(t, n, parent);
    
    // Assert
    assertTrue(t.getErrors().isEmpty());
  }
  
  @Test
  public void testVisit_nonExpressionResultNode_underForAndSecondChild_returnsNoError() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.ADD);
    Node parent = new Node();
    parent.setType(Token.FOR);
    Node sibling1 = new Node();
    parent.addChildToFront(sibling1);
    parent.addChildToBack(n);
    CheckSideEffects checkSideEffects = new CheckSideEffects();
    
    // Execute
    checkSideEffects.visit(t, n, parent);
    
    // Assert
    assertTrue(t.getErrors().isEmpty());
  }
  
  @Test
  public void testVisit_nonExpressionResultNode_underForAndNotFirstChildOrSecondChild_returnsError() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.ADD);
    Node parent = new Node();
    parent.setType(Token.FOR);
    Node sibling1 = new Node();
    parent.addChildToFront(sibling1);
    Node sibling2 = new Node();
    parent.addChildAfter(sibling2, sibling1);
    parent.addChildToBack(n);
    CheckSideEffects checkSideEffects = new CheckSideEffects();
    
    // Execute
    checkSideEffects.visit(t, n, parent);
    
    // Assert
    assertFalse(t.getErrors().isEmpty());
  }
}