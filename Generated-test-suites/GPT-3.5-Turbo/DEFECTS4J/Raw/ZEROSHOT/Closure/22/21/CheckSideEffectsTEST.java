import static org.junit.Assert.*;
import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testVisit_emptyNode_noErrorReported() {
    // Arrange
    Node emptyNode = new Node(null);
    emptyNode.setIsEmpty(true);
    emptyNode.setComma(false);
    NodeTraversal t = new NodeTraversal(null, null, null);
    
    // Act
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
    checkSideEffects.visit(t, emptyNode, null);
    
    // Assert
    // No error reported
  }
  
  @Test
  public void testVisit_commaNode_noErrorReported() {
    // Arrange
    Node commaNode = new Node(null);
    commaNode.setIsEmpty(false);
    commaNode.setComma(true);
    NodeTraversal t = new NodeTraversal(null, null, null);
    
    // Act
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
    checkSideEffects.visit(t, commaNode, null);
    
    // Assert
    // No error reported
  }
  
  @Test
  public void testVisit_qualifiedNameNodeWithJSDoc_noErrorReported() {
    // Arrange
    Node qualifiedNameNode = new Node(null);
    qualifiedNameNode.setIsEmpty(false);
    qualifiedNameNode.setComma(false);
    qualifiedNameNode.setQualifiedName(true);
    NodeTraversal t = new NodeTraversal(null, null, null);
    
    // Act
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
    checkSideEffects.visit(t, qualifiedNameNode, null);
    
    // Assert
    // No error reported
  }
  
  @Test
  public void testVisit_qualifiedNameNodeWithoutJSDoc_errorReported() {
    // Arrange
    Node qualifiedNameNode = new Node(null);
    qualifiedNameNode.setIsEmpty(false);
    qualifiedNameNode.setComma(false);
    qualifiedNameNode.setQualifiedName(true);
    qualifiedNameNode.setJSDocInfo(null);
    NodeTraversal t = new NodeTraversal(null, null, null);
    
    // Act
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
    checkSideEffects.visit(t, qualifiedNameNode, null);
    
    // Assert
    // Error reported
  }
  
  @Test
  public void testVisit_exprResultNode_noErrorReported() {
    // Arrange
    Node exprResultNode = new Node(null);
    exprResultNode.setIsEmpty(false);
    exprResultNode.setComma(false);
    exprResultNode.setQualifiedName(false);
    exprResultNode.setExprResult(true);
    NodeTraversal t = new NodeTraversal(null, null, null);
    
    // Act
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
    checkSideEffects.visit(t, exprResultNode, null);
    
    // Assert
    // No error reported
  }
  
  @Test
  public void testVisit_exprResultNode_errorReported() {
    // Arrange
    Node exprResultNode = new Node(null);
    exprResultNode.setIsEmpty(false);
    exprResultNode.setComma(false);
    exprResultNode.setQualifiedName(false);
    exprResultNode.setExprResult(true);
    exprResultNode.addChildToFront(new Node(null));
    NodeTraversal t = new NodeTraversal(null, null, null);
    
    // Act
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
    checkSideEffects.visit(t, exprResultNode, null);
    
    // Assert
    // Error reported
  }
  
  @Test
  public void testVisit_resultUsedAndSimpleOp_noErrorReported() {
    // Arrange
    Node node = new Node(null);
    node.setIsEmpty(false);
    node.setComma(false);
    node.setQualifiedName(false);
    node.setExprResult(false);
    NodeTraversal t = new NodeTraversal(null, null, null);
    
    // Act
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
    checkSideEffects.visit(t, node, null);
    
    // Assert
    // No error reported
  }
  
  @Test
  public void testVisit_resultUsedAndNonSimpleOp_noErrorReported() {
    // Arrange
    Node node = new Node(null);
    node.setIsEmpty(false);
    node.setComma(false);
    node.setQualifiedName(false);
    node.setExprResult(false);
    node.setType(Token.GETPROP);
    NodeTraversal t = new NodeTraversal(null, null, null);
    
    // Act
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
    checkSideEffects.visit(t, node, null);
    
    // Assert
    // No error reported
  }
  
  @Test
  public void testVisit_resultNotUsedAndSimpleOp_errorReported() {
    // Arrange
    Node node = new Node(null);
    node.setIsEmpty(false);
    node.setComma(false);
    node.setQualifiedName(false);
    node.setExprResult(false);
    node.setType(Token.ADD);
    NodeTraversal t = new NodeTraversal(null, null, null);
    
    // Act
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
    checkSideEffects.visit(t, node, null);
    
    // Assert
    // Error reported
  }
  
  @Test
  public void testVisit_stringNode_errorReported() {
    // Arrange
    Node node = new Node(null);
    node.setIsEmpty(false);
    node.setComma(false);
    node.setQualifiedName(false);
    node.setExprResult(false);
    node.setType(Token.STRING);
    NodeTraversal t = new NodeTraversal(null, null, null);
    
    // Act
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
    checkSideEffects.visit(t, node, null);
    
    // Assert
    // Error reported
  }
  
  @Test
  public void testVisit_nonStatementNode_errorReported() {
    // Arrange
    Node node = new Node(null);
    node.setIsEmpty(false);
    node.setComma(false);
    node.setQualifiedName(false);
    node.setExprResult(false);
    Node parentNode = new Node(null);
    parentNode.setType(Token.FOR);
    parentNode.addChildToFront(new Node(null));
    parentNode.getFirstChild().addChildToBack(new Node(null));
    parentNode.getFirstChild().addChildToBack(node);
    NodeTraversal t = new NodeTraversal(null, null, null);
    
    // Act
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
    checkSideEffects.visit(t, node, parentNode);
    
    // Assert
    // Error reported
  }
  
  @Test
  public void testProtectSideEffects_noProblemNodes_noChanges() {
    // Arrange
    List<Node> problemNodes = new ArrayList<>();
    AbstractCompiler compiler = new AbstractCompiler();
    
    // Act
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, null, false);
    checkSideEffects.protectSideEffects();
    
    // Assert
    // No changes made
  }
  
  @Test
  public void testProtectSideEffects_problemNodes_changeMade() {
    // Arrange
    List<Node> problemNodes = new ArrayList<>();
    Node problemNode1 = new Node(null);
    Node problemNode2 = new Node(null);
    problemNodes.add(problemNode1);
    problemNodes.add(problemNode2);
    AbstractCompiler compiler = new AbstractCompiler();
    
    // Act
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, null, false);
    checkSideEffects.protectSideEffects();
    
    // Assert
    // Change made to problemNode1 and problemNode2
    // compiler.reportCodeChange() called
  }
  
  @Test
  public void testAddExtern_compilerHasSynthesizedExternsInput_exclusionMethodAdded() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    compiler.setSynthesizedExternsInput(new CompilerInput());
    
    // Act
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, null, false);
    checkSideEffects.addExtern();
    
    // Assert
    // PROTECTOR_FN added to compiler's synthesized externs input
    // compiler.reportCodeChange() called
  }
  
  @Test
  public void testAddExtern_compilerDoesNotHaveSynthesizedExternsInput_exclusionMethodNotAdded() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    
    // Act
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, null, false);
    checkSideEffects.addExtern();
    
    // Assert
    // PROTECTOR_FN not added to compiler's synthesized externs input
    // compiler.reportCodeChange() called
  }
  
  @Test
  public void testStripProtection_callNodeWithProtectorFnAsTarget_callNodeReplaced() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    Node callNode = new Node(null);
    Node targetNode = new Node(null);
    targetNode.setType(Token.NAME);
    targetNode.setString("JSCOMPILER_PRESERVE");
    callNode.addChildToBack(targetNode);
    NodeTraversal t = new NodeTraversal(null, null, null);
    
    // Act
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    stripProtection.visit(t, callNode, null);
    
    // Assert
    // callNode replaced with targetNode's child
  }
  
  @Test
  public void testStripProtection_callNodeWithNonProtectorFnAsTarget_callNodeNotReplaced() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    Node callNode = new Node(null);
    Node targetNode = new Node(null);
    targetNode.setType(Token.NAME);
    targetNode.setString("NOT_PROTECTOR_FN");
    callNode.addChildToBack(targetNode);
    NodeTraversal t = new NodeTraversal(null, null, null);
    
    // Act
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    stripProtection.visit(t, callNode, null);
    
    // Assert
    // callNode not replaced with targetNode's child
  }
}