import org.junit.Test;
import static org.junit.Assert.*;

public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    PrepareAst prepareAst = new PrepareAst();

    // Test case 1
    Node node1 = new Node(Token.EXPR_VOID);
    Node child1 = new Node(Token.NAME, "test");
    node1.addChildToBack(child1);
    prepareAst.normalizeNodeTypes(node1);
    assertEquals(Token.EXPR_RESULT, node1.getToken());

    // Test case 2
    Node node2 = new Node(Token.BLOCK);
    Node child2 = new Node(Token.EXPR_VOID);
    Node grandChild2 = new Node(Token.NAME, "test");
    child2.addChildToBack(grandChild2);
    node2.addChildToBack(child2);
    prepareAst.normalizeNodeTypes(node2);
    assertEquals(Token.BLOCK, node2.getToken());
    assertEquals(1, node2.getChildCount());
    assertEquals(Token.EXPR_RESULT, node2.getFirstChild().getToken());
    assertEquals(1, node2.getFirstChild().getChildCount());
    assertEquals(Token.NAME, node2.getFirstChild().getFirstChild().getToken());

    // Test case 3
    Node node3 = new Node(Token.IF);
    Node child3 = new Node(Token.EXPR_VOID);
    Node grandChild3 = new Node(Token.NAME, "test");
    child3.addChildToBack(grandChild3);
    node3.addChildToBack(child3);
    prepareAst.normalizeNodeTypes(node3);
    assertEquals(Token.IF, node3.getToken());
    assertEquals(1, node3.getChildCount());
    assertEquals(Token.BLOCK, node3.getFirstChild().getToken());
    assertEquals(1, node3.getFirstChild().getChildCount());
    assertEquals(Token.EXPR_RESULT, node3.getFirstChild().getFirstChild().getToken());
    assertEquals(1, node3.getFirstChild().getFirstChild().getChildCount());
    assertEquals(Token.NAME, node3.getFirstChild().getFirstChild().getFirstChild().getToken());
  }

  @Test
  public void testNormalizeBlocks() {
    PrepareAst prepareAst = new PrepareAst();

    // Test case 1
    Node node1 = new Node(Token.IF);
    Node child1 = new Node(Token.BLOCK);
    node1.addChildToBack(child1);
    prepareAst.normalizeBlocks(node1);
    assertEquals(Token.IF, node1.getToken());
    assertEquals(1, node1.getChildCount());
    assertEquals(Token.BLOCK, node1.getFirstChild().getToken());

    // Test case 2
    Node node2 = new Node(Token.IF);
    Node child21 = new Node(Token.NAME, "test1");
    Node child22 = new Node(Token.BLOCK);
    Node child221 = new Node(Token.NAME, "test2");
    child22.addChildToBack(child221);
    node2.addChildToBack(child21);
    node2.addChildToBack(child22);
    prepareAst.normalizeBlocks(node2);
    assertEquals(Token.IF, node2.getToken());
    assertEquals(2, node2.getChildCount());
    assertEquals(Token.NAME, node2.getFirstChild().getToken());
    assertEquals(Token.BLOCK, node2.getLastChild().getToken());
    assertEquals(Token.NAME, node2.getLastChild().getFirstChild().getToken());
  }

  @Test
  public void testPrepareAnnotations() {
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();

    // Test case 1
    NodeTraversal t1 = new NodeTraversal(null);
    Node n1 = new Node(Token.OBJECTLIT);
    Node key1 = new Node(Token.STRING, "key1");
    Node value1 = new Node(Token.STRING, "value1");
    key1.addChildToBack(value1);
    n1.addChildToBack(key1);
    prepareAnnotations.normalizeObjectLiteralAnnotations(n1);
    assertEquals(Token.OBJECTLIT, n1.getToken());
    assertEquals(Token.STRING, n1.getFirstChild().getToken());
    assertEquals("key1", n1.getFirstChild().getString());
    assertEquals(Token.STRING, n1.getFirstChild().getFirstChild().getToken());
    assertEquals("value1", n1.getFirstChild().getFirstChild().getString());

    // Test case 2
    NodeTraversal t2 = new NodeTraversal(null);
    Node n2 = new Node(Token.CALL);
    Node child2 = new Node(Token.NAME, "test");
    n2.addChildToBack(child2);
    prepareAnnotations.annotateCalls(n2);
    assertEquals(Token.CALL, n2.getToken());
    assertEquals(Token.NAME, n2.getFirstChild().getToken());
    assertEquals("test", n2.getFirstChild().getString());
    assertTrue(n2.getBooleanProp(Node.FREE_CALL));

    // Test case 3
    NodeTraversal t3 = new NodeTraversal(null);
    Node n3 = new Node(Token.CALL);
    Node child3 = new Node(Token.NAME, "eval");
    n3.addChildToBack(child3);
    prepareAnnotations.annotateCalls(n3);
    assertEquals(Token.CALL, n3.getToken());
    assertEquals(Token.NAME, n3.getFirstChild().getToken());
    assertEquals("eval", n3.getFirstChild().getString());
    assertTrue(n3.getFirstChild().getBooleanProp(Node.DIRECT_EVAL));

    // Test case 4
    NodeTraversal t4 = new NodeTraversal(null);
    Node n4 = new Node(Token.ASSIGN);
    Node child4 = new Node(Token.FUNCTION);
    n4.addChildToBack(child4);
    n4.setJSDocInfo(new JSDocInfo());
    prepareAnnotations.annotateDispatchers(n4, n4);
    assertEquals(Token.ASSIGN, n4.getToken());
    assertTrue(child4.getBooleanProp(Node.IS_DISPATCHER));
  }
}