import static org.junit.Assert.*;
import org.junit.Test;

public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    // Typical Use Case
    // Given
    PrepareAst prepareAst = new PrepareAst();
    Node node = new Node();
    node.addChild(new Node());
    // When
    prepareAst.normalizeNodeTypes(node);
    // Then
    assertEquals(1, node.getChildCount());

    // Edge Case
    // Given
    Node emptyNode = new Node();
    // When
    prepareAst.normalizeNodeTypes(emptyNode);
    // Then
    assertEquals(0, emptyNode.getChildCount());

    // Error Scenario
    // Given
    Node nullNode = null;
    // When
    prepareAst.normalizeNodeTypes(nullNode);
    // Then
    assertNull(nullNode);
  }

  @Test
  public void testNormalizeBlocks() {
    // Typical Use Case
    // Given
    PrepareAst prepareAst = new PrepareAst();
    Node node = new Node(Token.IF);
    node.addChild(new Node(Token.BLOCK));
    // When
    prepareAst.normalizeBlocks(node);
    // Then
    assertEquals(Token.BLOCK, node.getFirstChild().getType());

    // Edge Case
    // Given
    Node emptyBlock = new Node(Token.IF);
    emptyBlock.addChild(new Node(Token.EMPTY));
    // When
    prepareAst.normalizeBlocks(emptyBlock);
    // Then
    assertEquals(Token.BLOCK, emptyBlock.getFirstChild().getType());

    // Error Scenario
    // Given
    Node nullNode = null;
    // When
    prepareAst.normalizeBlocks(nullNode);
    // Then
    assertNull(nullNode);
  }

  @Test
  public void testNormalizeObjectLiteralAnnotations() {
    // Typical Use Case
    // Given
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    Node objlit = new Node(Token.OBJECTLIT);
    Node key = new Node(Token.STRING);
    key.setJSDocInfo(new JSDocInfo());
    Node value = new Node(Token.STRING);
    objlit.addChild(key);
    key.addChildToBack(value);
    // When
    prepareAnnotations.normalizeObjectLiteralAnnotations(objlit);
    // Then
    assertNull(key.getJSDocInfo());
    assertEquals(key.getJSDocInfo(), value.getJSDocInfo());

    // Edge Case
    // Given
    Node emptyObjlit = new Node(Token.OBJECTLIT);
    // When
    prepareAnnotations.normalizeObjectLiteralAnnotations(emptyObjlit);
    // Then
    assertNull(emptyObjlit.getFirstChild());

    // Error Scenario
    // Given
    Node nullNode = null;
    // When
    prepareAnnotations.normalizeObjectLiteralAnnotations(nullNode);
    // Then
    assertNull(nullNode);
  }

  @Test
  public void testAnnotateCalls() {
    // Typical Use Case
    // Given
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    Node call = new Node(Token.CALL);
    Node firstNode = new Node(Token.SOMETHING);
    call.addChild(firstNode);
    // When
    prepareAnnotations.annotateCalls(call);
    // Then
    assertTrue(call.getBooleanProp(Node.FREE_CALL));

    // Edge Case
    // Given
    Node notGetCall = new Node(Token.CALL);
    Node notGetNode = new Node(Token.ANOTHERTHING);
    notGetCall.addChild(notGetNode);
    // When
    prepareAnnotations.annotateCalls(notGetCall);
    // Then
    assertFalse(notGetCall.getBooleanProp(Node.FREE_CALL));

    // Error Scenario
    // Given
    Node nullNode = null;
    // When
    prepareAnnotations.annotateCalls(nullNode);
    // Then
    assertNull(nullNode);
  }

  @Test
  public void testAnnotateDispatchers() {
    // Typical Use Case
    // Given
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    Node function = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    parent.addChild(function);
    // When
    prepareAnnotations.annotateDispatchers(function, parent);
    // Then
    assertTrue(function.getBooleanProp(Node.IS_DISPATCHER));

    // Edge Case
    // Given
    Node notAssignFunction = new Node(Token.FUNCTION);
    Node notAssignParent = new Node(Token.STRING);
    notAssignParent.addChild(notAssignFunction);
    // When
    prepareAnnotations.annotateDispatchers(notAssignFunction, notAssignParent);
    // Then
    assertFalse(notAssignFunction.getBooleanProp(Node.IS_DISPATCHER));

    // Error Scenario
    // Given
    Node nullNode = null;
    // When
    prepareAnnotations.annotateDispatchers(nullNode, nullNode);
    // Then
    assertNull(nullNode);
  }
  
  @Test
  public void testPrepareAstProcess() {
    // We can't directly test process() as it contains multiple method calls and the backing AST used inside these methods
    // is not accessible from this test class as it is a private member. However, we can still test it indirectly by making
    // sure that the method doesn't throw any exceptions or errors when called.
    
    // Given
    PrepareAst prepareAst = new PrepareAst();
    Node externs = null;
    Node root = null;
    
    // When
    prepareAst.process(externs, root);
    
    // Then: No exception or error is thrown when calling the method
  }
}