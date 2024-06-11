import org.junit.Test;
import static org.junit.Assert.*;

public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    // Typical use case
    Node root = new Node(Token.ROOT);
    Node child1 = new Node(Token.CALL);
    Node child2 = new Node(Token.EXPR_VOID);
    root.addChildToFront(child1);
    root.addChildToFront(child2);
    PrepareAst prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root);
    assertEquals(Token.EXPR_RESULT, child2.getToken());
  }

  @Test
  public void testNormalizeBlocks() {
    // Typical use case
    Node root = new Node(Token.ROOT);
    Node child1 = new Node(Token.IF);
    Node child2 = new Node(Token.WHILE);
    root.addChildToFront(child1);
    root.addChildToFront(child2);
    PrepareAst prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root);
    assertEquals(Token.BLOCK, child1.getFirstChild().getToken());
    assertEquals(Token.BLOCK, child2.getFirstChild().getToken());

    // Edge case
    Node child3 = new Node(Token.LABEL);
    child2.getLastChild().addChildToFront(child3);
    prepareAst.process(null, root);
    assertEquals(Token.BLOCK, child3.getFirstChild().getToken());

    // Error scenario
    Node child4 = new Node(Token.SWITCH);
    root.addChildToFront(child4);
    prepareAst.process(null, root);
    assertNotEquals(Token.BLOCK, child4.getFirstChild().getToken());
  }

  @Test
  public void testPrepareAnnotations() {
    // Typical use case
    Node root = new Node(Token.ROOT);
    Node child1 = new Node(Token.OBJECTLIT);
    root.addChildToFront(child1);
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    NodeTraversal t = new NodeTraversal(null, prepareAnnotations, null, null);
    t.traverse(root);
    assertNull(child1.getJSDocInfo());
    assertNull(child1.getFirstChild().getJSDocInfo());

    // Typical use case
    Node child2 = new Node(Token.CALL, new Node(Token.NAME, "eval"));
    root.addChildToFront(child2);
    t.traverse(root);
    assertTrue(child2.getBooleanProp(Node.DIRECT_EVAL));

    // Typical use case
    Node child3 = new Node(Token.CALL);
    root.addChildToFront(child3);
    t.traverse(root);
    assertTrue(child3.getBooleanProp(Node.FREE_CALL));

    // Error scenario
    Node child4 = new Node(Token.FUNCTION);
    child4.setJSDocInfo(new JSDocInfo());
    root.addChildToFront(child4);
    Node parent = new Node(Token.ASSIGN);
    parent.setFirstChild(child4);
    child4.setParent(parent);
    t.traverse(root);
    assertFalse(child4.getBooleanProp(Node.IS_DISPATCHER));
  }

  @Test
  public void testProcess() {
    // Error scenario
    Node externs = new Node(Token.ROOT);
    Node root = new Node(Token.ROOT);
    PrepareAst prepareAst = new PrepareAst(null);
    prepareAst.process(externs, null);
    assertNull(root.getJSDocInfo());

    // Error scenario
    prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root);
    assertNotNull(root.getJSDocInfo());
  }
}