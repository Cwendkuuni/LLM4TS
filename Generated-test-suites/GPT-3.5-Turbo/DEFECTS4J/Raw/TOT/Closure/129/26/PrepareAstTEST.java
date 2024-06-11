import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrepareAstTest {

  private PrepareAst prepareAst;
  private AbstractCompiler compiler;
  
  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    prepareAst = new PrepareAst(compiler);
  }
  
  @Test
  public void testProcess_WithNonNullExternsAndRoot_CallsPrepareAnnotations() {
    // Arrange
    Node externs = new Node();
    Node root = new Node();
    
    // Act
    prepareAst.process(externs, root);

    // Assert
    // Verify that the PrepareAnnotations class is called for both externs and root nodes
    assertTrue(compiler.preparedAnnotations.isCalledFor(externs));
    assertTrue(compiler.preparedAnnotations.isCalledFor(root));
  }
  
  @Test
  public void testProcess_WithNonNullExternsAndRoot_DoesNotCallNormalizeNodeTypes() {
    // Arrange
    Node externs = new Node();
    Node root = new Node();
    compiler.preparedAnnotations.setCalledFor(externs);
    compiler.preparedAnnotations.setCalledFor(root);
    
    // Act
    prepareAst.process(externs, root);

    // Assert
    // Verify that the normalizeNodeTypes method is not called for any node
    assertFalse(compiler.normalizeNodeTypes.isCalled());
  }
  
  @Test
  public void testProcess_WithNonNullExternsAndRoot_DoesNotCallNormalizeBlocks() {
    // Arrange
    Node externs = new Node();
    Node root = new Node();
    compiler.preparedAnnotations.setCalledFor(externs);
    compiler.preparedAnnotations.setCalledFor(root);
    
    // Act
    prepareAst.process(externs, root);

    // Assert
    // Verify that the normalizeBlocks method is not called for any node
    assertFalse(compiler.normalizeBlocks.isCalled());
  }
  
  @Test
  public void testProcess_WithNonNullExternsAndRoot_CallsNormalizeNodeTypes_WhenCheckOnlyIsTrue() {
    // Arrange
    Node externs = new Node();
    Node root = new Node();
    prepareAst = new PrepareAst(compiler, true);
    compiler.preparedAnnotations.setCalledFor(externs);
    compiler.preparedAnnotations.setCalledFor(root);
    
    // Act
    prepareAst.process(externs, root);

    // Assert
    // Verify that the normalizeNodeTypes method is called for both externs and root nodes
    assertTrue(compiler.normalizeNodeTypes.isCalledFor(externs));
    assertTrue(compiler.normalizeNodeTypes.isCalledFor(root));
  }
  
  @Test
  public void testNormalizeNodeTypes_AddsBlocksToControlStructures() {
    // Arrange
    Node controlStructure = new Node(Token.IF);
    Node child = new Node(Token.NAME, "a");
    controlStructure.addChildToFront(child);
    
    // Act
    prepareAst.normalizeNodeTypes(controlStructure);

    // Assert
    // Verify that a block is added to the control structure and the child node is moved inside the block
    assertTrue(controlStructure.hasChildOfType(Token.BLOCK));
    assertEquals(Token.BLOCK, controlStructure.getFirstChild().getType());
    assertEquals(child, controlStructure.getFirstChild().getFirstChild());
  }
  
  @Test
  public void testNormalizeNodeTypes_DoesNotAddBlockToLabelNode() {
    // Arrange
    Node labelNode = new Node(Token.LABEL);
    Node child = new Node(Token.NAME, "a");
    labelNode.addChildToFront(child);
    
    // Act
    prepareAst.normalizeNodeTypes(labelNode);

    // Assert
    // Verify that a block is not added to the label node and the child node is not moved inside the block
    assertFalse(labelNode.hasChildOfType(Token.BLOCK));
    assertEquals(child, labelNode.getFirstChild());
  }
  
  @Test
  public void testNormalizeNodeTypes_DoesNotAddBlockToSwitchNode() {
    // Arrange
    Node switchNode = new Node(Token.SWITCH);
    Node child = new Node(Token.NAME, "a");
    switchNode.addChildToFront(child);
    
    // Act
    prepareAst.normalizeNodeTypes(switchNode);

    // Assert
    // Verify that a block is not added to the switch node and the child node is not moved inside the block
    assertFalse(switchNode.hasChildOfType(Token.BLOCK));
    assertEquals(child, switchNode.getFirstChild());
  }
  
  @Test
  public void testNormalizeNodeTypes_NormalizesChildNodes() {
    // Arrange
    Node n = new Node(Token.SCRIPT);
    Node child1 = new Node(Token.VAR);
    Node child2 = new Node(Token.LET);
    n.addChildrenToFront(child1);
    n.addChildrenToFront(child2);
    
    // Act
    prepareAst.normalizeNodeTypes(n);

    // Assert
    // Verify that the normalizeNodeTypes method is called for both child nodes
    assertTrue(compiler.normalizeNodeTypes.isCalledFor(child1));
    assertTrue(compiler.normalizeNodeTypes.isCalledFor(child2));
  }
  
  @Test
  public void testPrepareAnnotations_TraversesObjectLitNode() {
    // Arrange
    Node objectLit = new Node(Token.OBJECTLIT);
    
    // Act
    prepareAst.prepareAnnotations.shouldTraverse(null, objectLit, null);

    // Assert
    // Verify that the normalizeObjectLiteralAnnotations method is called for the object literal node
    assertTrue(compiler.normalizeObjectLiteralAnnotations.isCalledFor(objectLit));
  }
  
  @Test
  public void testPrepareAnnotations_VisitsCallNode_CallsAnnotateCallsMethod() {
    // Arrange
    Node callNode = new Node(Token.CALL);
    
    // Act
    prepareAst.prepareAnnotations.visit(null, callNode, null);

    // Assert
    // Verify that the annotateCalls method is called for the call node
    assertTrue(compiler.annotateCalls.isCalledFor(callNode));
  }
  
  @Test
  public void testPrepareAnnotations_VisitsFunctionNode_CallsAnnotateDispatchersMethod() {
    // Arrange
    Node functionNode = new Node(Token.FUNCTION);
    Node parentNode = new Node();
    
    // Act
    prepareAst.prepareAnnotations.visit(null, functionNode, parentNode);

    // Assert
    // Verify that the annotateDispatchers method is called for the function node
    assertTrue(compiler.annotateDispatchers.isCalledFor(functionNode));
  }
  
  @Test
  public void testPrepareAnnotations_NormalizeObjectLiteralAnnotations_NormalizesKeyValues() {
    // Arrange
    Node objectLit = new Node(Token.OBJECTLIT);
    Node key1 = new Node(Token.STRING);
    Node value1 = new Node(Token.FUNCTION);
    key1.addChildToBack(value1);
    objectLit.addChildToBack(key1);
    
    // Act
    prepareAst.prepareAnnotations.normalizeObjectLiteralAnnotations(objectLit);

    // Assert
    // Verify that the normalizeObjectLiteralKeyAnnotations method is called for the key node
    assertTrue(compiler.normalizeObjectLiteralKeyAnnotations.isCalledFor(objectLit, key1, value1));
  }
  
  // Add more test cases for other methods in the PrepareAst class
  
}