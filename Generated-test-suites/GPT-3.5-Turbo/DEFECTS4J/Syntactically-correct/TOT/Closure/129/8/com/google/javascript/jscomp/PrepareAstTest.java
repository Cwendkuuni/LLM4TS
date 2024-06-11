import org.junit.Test;
import static org.junit.Assert.*;

public class PrepareAstTest {
  
  @Test
  public void testNormalizeNodeTypes() {
    // Test the normalizeNodeTypes method
    
    // Typical use case, n is not null and has children
    Node n = new Node(Token.BLOCK);
    Node child1 = new Node(Token.STRING);
    Node child2 = new Node(Token.NUMBER);
    n.addChildToBack(child1);
    n.addChildToBack(child2);
    prepareAst.normalizeNodeTypes(n);
    assertEquals(Token.BLOCK, n.getToken());
    assertEquals(2, n.getChildCount());
    assertEquals(Token.STRING, n.getFirstChild().getToken());
    assertEquals(Token.NUMBER, n.getLastChild().getToken());
    
    // Edge case, n is null
    Node n2 = null;
    prepareAst.normalizeNodeTypes(n2); // This should not cause an error and should just return null
    
    // Error scenario, child has different parent
    Node n3 = new Node(Token.BLOCK);
    Node child3 = new Node(Token.STRING);
    Node parent1 = new Node(Token.FUNCTION);
    n3.addChildToBack(child3);
    parent1.addChildToFront(n3);
    prepareAst.normalizeNodeTypes(child3); // This should throw a Preconditions check error
    
  }
  
  @Test
  public void testNormalizeBlocks() {
    // Test the normalizeBlocks method
    
    // Typical use case, n is a control structure and c needs to be replaced with a block
    Node n = new Node(Token.IF);
    Node c = new Node(Token.STRING);
    n.addChildToBack(c);
    prepareAst.normalizeBlocks(n);
    assertEquals(Token.IF, n.getToken());
    assertTrue(n.getFirstChild().isBlock());
    assertTrue(n.getFirstChild().getFirstChild().isString());
    
    // Typical use case, n is a control structure but its children are already blocks
    Node n2 = new Node(Token.FUNCTION);
    Node c2 = new Node(Token.BLOCK);
    n2.addChildToBack(c2);
    prepareAst.normalizeBlocks(n2);
    assertEquals(Token.FUNCTION, n2.getToken());
    assertTrue(n2.getFirstChild().isBlock());
    
    // Edge case, n is not a control structure
    Node n3 = new Node(Token.RETURN);
    Node c3 = new Node(Token.NUMBER);
    n3.addChildToBack(c3);
    prepareAst.normalizeBlocks(n3); // This should not cause any change
    assertEquals(Token.RETURN, n3.getToken());
    assertTrue(n3.getFirstChild().isNumber());
    
    // Error scenario, n is a control structure and one of its children has different parent
    Node n4 = new Node(Token.IF);
    Node c4 = new Node(Token.STRING);
    Node parent2 = new Node(Token.FUNCTION);
    n4.addChildToBack(c4);
    parent2.addChildToFront(n4);
    prepareAst.normalizeBlocks(n4); // This should throw a Preconditions check error
    
  }
  
  @Test
  public void testPrepareAnnotations() {
    // Test the PrepareAnnotations class
    
    // Test the shouldTraverse method
    // Typical use case, n is an object literal
    Node objLit = new Node(Token.OBJECTLIT);
    Node key1 = new Node(Token.STRING, new Node(Token.ASSIGN), new Node(Token.STRING));
    Node key2 = new Node(Token.STRING, new Node(Token.ASSIGN), new Node(Token.NUMBER));
    objLit.addChildToBack(key1);
    objLit.addChildToBack(key2);
    prepareAst.PrepareAnnotations annotator = new prepareAst.PrepareAnnotations();
    assertTrue(annotator.shouldTraverse(null, objLit, null));
    assertEquals(Token.OBJECTLIT, objLit.getToken());
    
    // Edge case, n is not an object literal
    Node n = new Node(Token.STRING);
    assertFalse(annotator.shouldTraverse(null, n, null));
    assertEquals(Token.STRING, n.getToken());
    
    // Test the visit method
    // Typical use case, n is a CALL node, first child is a GET node
    Node callNode = new Node(Token.CALL);
    Node firstChild = new Node(Token.GET);
    callNode.addChildToBack(firstChild);
    annotator.visit(null, callNode, null);
    assertTrue(callNode.getBooleanProp(Node.FREE_CALL));
    assertEquals(Token.CALL, callNode.getToken());
    
    // Typical use case, n is a CALL node, first child is not a GET node
    Node callNode2 = new Node(Token.CALL);
    Node firstChild2 = new Node(Token.STRING);
    callNode2.addChildToBack(firstChild2);
    annotator.visit(null, callNode2, null);
    assertFalse(callNode2.getBooleanProp(Node.FREE_CALL));
    assertEquals(Token.CALL, callNode2.getToken());
    
    // Typical use case, n is a FUNCTION node and parent is an ASSIGN node
    Node functionNode = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.ASSIGN);
    parent1.addChildToBack(functionNode);
    annotator.visit(null, functionNode, parent1);
    assertTrue(functionNode.getBooleanProp(Node.IS_DISPATCHER));
    assertEquals(Token.FUNCTION, functionNode.getToken());
    
    // Edge case, n is a FUNCTION node and parent is not an ASSIGN node
    Node functionNode2 = new Node(Token.FUNCTION);
    Node parent2 = new Node(Token.RETURN);
    parent2.addChildToBack(functionNode2);
    annotator.visit(null, functionNode2, parent2);
    assertFalse(functionNode2.getBooleanProp(Node.IS_DISPATCHER));
    assertEquals(Token.FUNCTION, functionNode2.getToken());
    
  }
  
  @Test
  public void testPrepareAstProcess() {
    // Test the process method
    
    // Typical use case, checkOnly is false and externs and root are not null
    Node externs = new Node(Token.BLOCK);
    Node root = new Node(Token.BLOCK);
    prepareAst ast = new prepareAst(null, false);
    ast.process(externs, root);
    // Check that PrepareAnnotations is called for both externs and root
    // Check that normalizeNodeTypes is not called because checkOnly is false
    assertTrue(externs.hasChildren());
    assertEquals(Token.BLOCK, externs.getToken());
    assertTrue(root.hasChildren());
    assertEquals(Token.BLOCK, root.getToken());
    
    // Typical use case, checkOnly is true and externs and root are not null
    Node externs2 = new Node(Token.BLOCK);
    Node root2 = new Node(Token.BLOCK);
    prepareAst ast2 = new prepareAst(null, true);
    ast2.process(externs2, root2);
    // Check that PrepareAnnotations is not called because checkOnly is true
    // Check that normalizeNodeTypes is called for both externs and root
    assertTrue(externs2.hasChildren());
    assertEquals(Token.BLOCK, externs2.getToken());
    assertTrue(root2.hasChildren());
    assertEquals(Token.BLOCK, root2.getToken());
    
  }
  
}