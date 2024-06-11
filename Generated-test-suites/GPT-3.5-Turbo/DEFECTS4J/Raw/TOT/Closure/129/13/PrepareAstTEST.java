import static org.junit.Assert.*;
import org.junit.Test;

public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    // Typical use case
    // Test when given node has EXPR_VOID, it should be converted to EXPR_RESULT
    Node node = new Node(Token.EXPR_VOID);
    PrepareAst prepareAst = new PrepareAst(null, true);
    prepareAst.normalizeNodeTypes(node);
    assertEquals(Token.EXPR_RESULT, node.getType());

    // Edge case
    // Test when given node is null, it should not throw any exception
    Node nullNode = null;
    prepareAst.normalizeNodeTypes(nullNode);
    assertNull(nullNode);

    // Error scenario
    // Test when given node is not EXPR_VOID, it should not make any changes to the node
    Node otherNode = new Node(Token.ADD);
    prepareAst.normalizeNodeTypes(otherNode);
    assertEquals(Token.ADD, otherNode.getType());
  }

  @Test
  public void testNormalizeBlocks() {
    // Typical use case
    // Test when given node is an IF statement without a block, a new block should be added to the IF statement
    Node ifNode = new Node(Token.IF, new Node(Token.TRUE));
    Node statement = new Node(Token.EXPR_RESULT);
    ifNode.addChildToBack(statement);
    PrepareAst prepareAst = new PrepareAst(null, true);
    prepareAst.normalizeBlocks(ifNode);
    assertEquals(Token.BLOCK, ifNode.getLastChild().getType());
    assertEquals(statement, ifNode.getLastChild().getFirstChild());

    // Edge case
    // Test when given node is an empty IF statement, it should not throw any exception
    Node emptyIfNode = new Node(Token.IF, new Node(Token.TRUE));
    PrepareAst prepareAst = new PrepareAst(null, true);
    prepareAst.normalizeBlocks(emptyIfNode);
    assertTrue(emptyIfNode.getLastChild().isBlock());

    // Error scenario
    // Test when given node is not an IF statement, it should not make any changes to the node
    Node whileNode = new Node(Token.WHILE, new Node(Token.TRUE));
    PrepareAst prepareAst = new PrepareAst(null, true);
    prepareAst.normalizeBlocks(whileNode);
    assertFalse(whileNode.getLastChild().isBlock());
  }

  @Test
  public void testPrepareAnnotations() {
    // Typical use case
    // Test when given node is an object literal, the annotations on the keys and values should be normalized
    Node objLit = new Node(Token.OBJECTLIT);
    Node key1 = new Node(Token.STRING, new Node(Token.NAME, "a"));
    Node key2 = new Node(Token.STRING, new Node(Token.NAME, "b"));
    Node value1 = new Node(Token.NAME, "x");
    Node value2 = new Node(Token.NAME, "y");
    key1.setJSDocInfo(new JSDocInfoBuilder(true).build());
    key2.setJSDocInfo(new JSDocInfoBuilder(true).build());
    objLit.addChildToBack(key1);
    objLit.addChildToBack(value1);
    objLit.addChildToBack(key2);
    objLit.addChildToBack(value2);
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    prepareAnnotations.normalizeObjectLiteralAnnotations(objLit);
    assertNull(key1.getJSDocInfo());
    assertNotNull(value1.getJSDocInfo());
    assertNull(key2.getJSDocInfo());
    assertNotNull(value2.getJSDocInfo());

    // Edge case
    // Test when given object literal is empty, it should not throw any exception
    Node emptyObjLit = new Node(Token.OBJECTLIT);
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    prepareAnnotations.normalizeObjectLiteralAnnotations(emptyObjLit);
    assertTrue(emptyObjLit.isEmpty());

    // Error scenario
    // Test when given node is not an object literal, it should not make any changes to the node
    Node otherNode = new Node(Token.ADD);
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    prepareAnnotations.normalizeObjectLiteralAnnotations(otherNode);
    assertFalse(otherNode.isObjectLit());
  }

}