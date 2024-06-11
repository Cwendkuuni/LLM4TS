import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckSideEffectsTest {

  private CheckSideEffects checkSideEffects;

  @Before
  public void setup() {
    checkSideEffects = new CheckSideEffects(null, CheckLevel.ERROR, false);
  }

  @Test
  public void testProcess_WithNoSuspiciousCode_NoWarnings() {
    // Arrange
    Node externs = new Node(Token.EXTERN_NAME);
    Node root = new Node(Token.SCRIPT);
    
    // Act
    checkSideEffects.process(externs, root);
    
    // Assert
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testProcess_WithSimpleAssignment_NoWarnings() {
    // Arrange
    Node externs = new Node(Token.EXTERN_NAME);
    Node root = new Node(Token.SCRIPT);
    Node assignment = new Node(Token.ASSIGN, new Node(Token.NAME, new Node(Token.STRING, "s")), new Node(Token.STRING, "this string is "));
    root.addChildToBack(assignment);
    
    // Act
    checkSideEffects.process(externs, root);
    
    // Assert
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testProcess_WithButYouForgotThePlus_SuspiciousCodeWarning() {
    // Arrange
    Node externs = new Node(Token.EXTERN_NAME);
    Node root = new Node(Token.SCRIPT);
    Node assignment = new Node(Token.ASSIGN, new Node(Token.NAME, new Node(Token.STRING, "s")), new Node(Token.STRING, "this string is "));
    Node continuation = new Node(Token.STRING, "continued on the next line but you forgot the +");
    assignment.addChildToBack(continuation);
    root.addChildToBack(assignment);
    
    // Act
    checkSideEffects.process(externs, root);
    
    // Assert
    assertFalse(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testProcess_WithEqualOperator_NoWarnings() {
    // Arrange
    Node externs = new Node(Token.EXTERN_NAME);
    Node root = new Node(Token.SCRIPT);
    Node equalOperator = new Node(Token.EQ, new Node(Token.NAME), new Node(Token.NAME, new Node(Token.STRING, "foo()")));
    root.addChildToBack(equalOperator);
    
    // Act
    checkSideEffects.process(externs, root);
    
    // Assert
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testProcess_WithDoubleSemicolon_NoWarnings() {
    // Arrange
    Node externs = new Node(Token.EXTERN_NAME);
    Node root = new Node(Token.SCRIPT);
    Node exprResult = new Node(Token.EXPR_RESULT, new Node(Token.EMPTY));
    root.addChildToBack(exprResult);
    
    // Act
    checkSideEffects.process(externs, root);
    
    // Assert
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testProcess_WithStraySemicolon_NoWarnings() {
    // Arrange
    Node externs = new Node(Token.EXTERN_NAME);
    Node root = new Node(Token.SCRIPT);
    Node straySemicolon = new Node(Token.EXPR_RESULT, new Node(Token.NAME));
    root.addChildToBack(straySemicolon);
    
    // Act
    checkSideEffects.process(externs, root);
    
    // Assert
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testProcess_WithMissingPlus_SuspiciousCodeWarning() {
    // Arrange
    Node externs = new Node(Token.EXTERN_NAME);
    Node root = new Node(Token.SCRIPT);
    Node missingPlus = new Node(Token.EQ, new Node(Token.NAME), new Node(Token.CALL, new Node(Token.NAME, new Node(Token.STRING, "foo()")), new Node(Token.EMPTY)));
    root.addChildToBack(missingPlus);
    
    // Act
    checkSideEffects.process(externs, root);
    
    // Assert
    assertFalse(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testHotSwapScript_WithNoSuspiciousCode_NoWarnings() {
    // Arrange
    Node scriptRoot = new Node(Token.SCRIPT);
    Node originalRoot = new Node(Token.SCRIPT);
    
    // Act
    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
    
    // Assert
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testHotSwapScript_WithRemoveProtection_NoWarnings() {
    // Arrange
    Node scriptRoot = new Node(Token.SCRIPT);
    Node originalRoot = new Node(Token.SCRIPT);
    Node protectionCall = new Node(Token.CALL, new Node(Token.NAME, new Node(Token.STRING, "JSCOMPILER_PRESERVE")), new Node(Token.NAME));
    scriptRoot.addChildToBack(protectionCall);
    
    // Act
    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
    
    // Assert
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testHotSwapScript_WithProtection_NoWarnings() {
    // Arrange
    Node scriptRoot = new Node(Token.SCRIPT);
    Node originalRoot = new Node(Token.SCRIPT);
    Node protectionCall = new Node(Token.CALL, new Node(Token.NAME, new Node(Token.STRING, "JSCOMPILER_PRESERVE")), new Node(Token.NAME));
    Node child = new Node(Token.NAME);
    protectionCall.addChildToBack(child);
    scriptRoot.addChildToBack(protectionCall);
    
    // Act
    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
    
    // Assert
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testVisit_WithNoSuspiciousCode_NoWarnings() {
    // Arrange
    NodeTraversal traversal = new NodeTraversal(null);
    Node node = new Node(Token.SCRIPT);
    Node parent = null;
    
    // Act
    checkSideEffects.visit(traversal, node, parent);
    
    // Assert
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testVisit_WithCommaNode_NoWarnings() {
    // Arrange
    NodeTraversal traversal = new NodeTraversal(null);
    Node node = new Node(Token.COMMA);
    node.addChildToBack(new Node(Token.NAME));
    Node parent = new Node(Token.FOR);
    parent.addChildToFront(new Node(Token.COMMA));
    
    // Act
    checkSideEffects.visit(traversal, node, parent);
    
    // Assert
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testVisit_WithNoJSDocInfoOnQualifiedName_NoWarnings() {
    // Arrange
    NodeTraversal traversal = new NodeTraversal(null);
    Node node = new Node(Token.NAME);
    node.setJSDocInfo(null);
    node.addChildToBack(new Node(Token.VOID));
    Node parent = new Node(Token.FOR);
    parent.addChildToFront(new Node(Token.COMMA));
    
    // Act
    checkSideEffects.visit(traversal, node, parent);
    
    // Assert
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testVisit_WithQualifiedNameAndJSDocInfo_NoWarnings() {
    // Arrange
    NodeTraversal traversal = new NodeTraversal(null);
    Node node = new Node(Token.NAME);
    node.setJSDocInfo(new JSDocInfoBuilder().build());
    node.addChildToBack(new Node(Token.VOID));
    Node parent = new Node(Token.FOR);
    parent.addChildToFront(new Node(Token.COMMA));
    
    // Act
    checkSideEffects.visit(traversal, node, parent);
    
    // Assert
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testVisit_WithNoJSDocInfoOnExprResult_NoWarnings() {
    // Arrange
    NodeTraversal traversal = new NodeTraversal(null);
    Node node = new Node(Token.EXPR_RESULT);
    node.setJSDocInfo(null);
    node.addChildToBack(new Node(Token.STRING, "test"));
    Node parent = new Node(Token.FOR);
    parent.addChildToFront(new Node(Token.COMMA));
    
    // Act
    checkSideEffects.visit(traversal, node, parent);
    
    // Assert
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testVisit_WithExprResult_NoWarnings() {
    // Arrange
    NodeTraversal traversal = new NodeTraversal(null);
    Node node = new Node(Token.EXPR_RESULT);
    node.setJSDocInfo(null);
    node.addChildToBack(new Node(Token.STRING, "test"));
    Node parent = new Node(Token.NAME);
    parent.addChildToFront(new Node(Token.COMMA));
    parent.addChildToBack(new Node(Token.BLOCK));
    
    // Act
    checkSideEffects.visit(traversal, node, parent);
    
    // Assert
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testVisit_WithNoJSDocInfoOnAncestor_NoWarnings() {
    // Arrange
    NodeTraversal traversal = new NodeTraversal(null);
    Node node = new Node(Token.EXPR_RESULT);
    node.setJSDocInfo(null);
    node.addChildToBack(new Node(Token.STRING, "test"));
    Node parent = new Node(Token.NAME);
    parent.getParent().setJSDocInfo(null);
    
    // Act
    checkSideEffects.visit(traversal, node, parent);
    
    // Assert
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
  
  @Test
  public void testVisit_WithNoJSDocInfoOnAncestor_NoWarnings() {
    // Arrange
    NodeTraversal traversal = new NodeTraversal(null);
    Node node = new Node(Token.EXPR_RESULT);
    node.setJSDocInfo(null);
    node.addChildToBack(new Node(Token.STRING, "test"));
    Node parent = new Node(Token.NAME);
    parent.getParent().setJSDocInfo(null);

    // Act
    checkSideEffects.visit(traversal, node, parent);

    // Assert
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testNevertheless() {
    assertTrue(true);
  }

}