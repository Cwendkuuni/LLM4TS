import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrepareAstTest {

  private PrepareAst prepareAst;

  @Before
  public void setUp() {
    prepareAst = new PrepareAst(new AbstractCompiler());
  }

  @Test
  public void testProcess() {
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    prepareAst.process(externs, root);
    // Assert that the process method does not throw any exceptions
  }

  @Test
  public void testNormalizeNodeTypes() {
    Node root = new Node(Token.ROOT);
    Node child1 = new Node(Token.STRING);
    Node child2 = new Node(Token.TRUE);
    root.addChildToBack(child1);
    root.addChildToBack(child2);

    prepareAst.normalizeNodeTypes(root);

    assertEquals(Token.STRING, child1.getType());
    assertEquals(Token.TRUE, child2.getType());
  }

  @Test
  public void testNormalizeBlocks() {
    Node root = new Node(Token.ROOT);
    Node controlStructure = new Node(Token.IF);
    Node codeBlock = new Node(Token.BLOCK);
    controlStructure.addChildToBack(codeBlock);
    root.addChildToBack(controlStructure);

    prepareAst.normalizeBlocks(controlStructure);

    assertEquals(Token.BLOCK, controlStructure.getFirstChild().getType());
  }

  @Test
  public void testPrepareAnnotations() {
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    NodeTraversal t = new NodeTraversal(new AbstractCompiler(), null);
    Node n = new Node(Token.CALL);
    Node parent = new Node(Token.ROOT);

    // Set up the nodes with necessary properties
    n.putBooleanProp(Node.DIRECT_EVAL, true);
    parent.setJSDocInfo(new JSDocInfoBuilder(true).build());

    // Call the visit method
    prepareAnnotations.visit(t, n, parent);

    // Assert that the necessary properties are set
    assertTrue(n.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testAnnotateCalls() {
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    Node n = new Node(Token.CALL);
    Node firstChild = new Node(Token.NAME, "eval");
    n.addChildToBack(firstChild);

    prepareAnnotations.annotateCalls(n);

    assertTrue(n.getBooleanProp(Node.FREE_CALL));
  }

  @Test
  public void testAnnotateDispatchers() {
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(n);
    parent.setJSDocInfo(new JSDocInfoBuilder(true).build());

    prepareAnnotations.annotateDispatchers(n, parent);

    assertTrue(n.getBooleanProp(Node.IS_DISPATCHER));
  }

  @Test
  public void testNormalizeObjectLiteralAnnotations() {
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    Node objlit = new Node(Token.OBJECTLIT);
    Node key1 = new Node(Token.STRING, "a");
    Node key2 = new Node(Token.STRING, "b");
    Node value1 = new Node(Token.STRING, "value1");
    Node value2 = new Node(Token.STRING, "value2");
    key1.addChildToBack(value1);
    key2.addChildToBack(value2);
    objlit.addChildToBack(key1);
    objlit.addChildToBack(key2);

    prepareAnnotations.normalizeObjectLiteralAnnotations(objlit);

    assertNull(key1.getJSDocInfo());
    assertNotNull(value1.getJSDocInfo());
  }

  @Test
  public void testNormalizeObjectLiteralKeyAnnotations() {
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    Node objlit = new Node(Token.OBJECTLIT);
    Node key = new Node(Token.STRING, "a");
    Node value = new Node(Token.STRING, "value");
    key.setJSDocInfo(new JSDocInfoBuilder(true).build());
    objlit.addChildToBack(key);
    objlit.addChildToBack(value);

    prepareAnnotations.normalizeObjectLiteralKeyAnnotations(objlit, key, value);

    assertNull(key.getJSDocInfo());
    assertNotNull(value.getJSDocInfo());
  }

}