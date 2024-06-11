import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_globalContext_returnTrue() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.ERROR;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_functionWithConstructorAnnotation_returnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.ERROR;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setConstructor(true);
    n.setJSDocInfo(jsDocInfo);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_functionWithThisAnnotation_returnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.ERROR;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.recordThisType();
    n.setJSDocInfo(jsDocInfo);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_functionWithOverrideAnnotation_returnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.ERROR;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setOverride(true);
    n.setJSDocInfo(jsDocInfo);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_functionNotInValidContext_returnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.ERROR;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    Node gramps = new Node(Token.VAR);
    Node greatGramps = new Node(Token.BLOCK);
    parent.addChildToFront(gramps);
    gramps.addChildToFront(greatGramps);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_notAssignment_returnTrue() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.ERROR;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node(Token.BLOCK);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_assignmentAndNonNestedAssignment_returnTrue() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.ERROR;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToFront(new Node(Token.NAME, "prototype"));
    parent.addChildToFront(lhs);
    Node rhs = new Node(Token.NAME);
    lhs.addChildToBack(rhs);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_assignmentAndNestedAssignment_returnFalse() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.ERROR;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME, "a");
    parent.addChildToFront(lhs);
    Node nestedAssign = new Node(Token.ASSIGN);
    lhs.addChildToBack(nestedAssign);
    Node nestedLhs = new Node(Token.NAME, "this");
    nestedAssign.addChildToFront(nestedLhs);
    Node nestedRhs = new Node(Token.NAME, "property");
    nestedAssign.addChildToBack(nestedRhs);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testVisit_nodeIsThisAndShouldReportThis_reportError() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.ERROR;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    Node grandParent = new Node();
    grandParent.setType(Token.GETPROP);
    parent.addChildToFront(grandParent);
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    // Your assertion here
  }
  
  @Test
  public void testVisit_nodeIsAssignLhsChild_assignLhsChildIsNull() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.ERROR;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    checkGlobalThis.visit(t, n, parent);
    
    // Act
    Node result = checkGlobalThis.assignLhsChild;
    
    // Assert
    assertNull(result);
  }
  
  @Test
  public void testVisit_nodeIsNotAssignLhsChild_assignLhsChildIsNotNull() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.ERROR;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    checkGlobalThis.assignLhsChild = n;
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    Node result = checkGlobalThis.assignLhsChild;
    
    // Assert
    assertNotNull(result);
  }
  
  private void initializeCheckGlobalThis(AbstractCompiler compiler, CheckLevel level) {
    // Your initialization code here
  }
  
  private JSDocInfo initializeJSDocInfo() {
    // Your initialization code here
  }
  
  private Node initializeNode() {
    // Your initialization code here
  }
  
  private NodeTraversal initializeNodeTraversal() {
    // Your initialization code here
  }
  
}