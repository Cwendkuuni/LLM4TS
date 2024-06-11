import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PrepareAstTest {
  
  private PrepareAst prepareAst;
  private AbstractCompiler compiler;
  
  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    prepareAst = new PrepareAst(compiler);
  }
  
  @Test
  public void testProcess() {
    Node externs = new Node(Token.EXTERN_NAME);
    Node root = new Node(Token.ROOT);

    prepareAst.process(externs, root);

    // assert statements
    // ...
  }
  
  @Test
  public void testNormalizeNodeTypes() {
    Node node = new Node(Token.NAME);
    prepareAst.normalizeNodeTypes(node);

    // assert statements
    // ...
  }
  
  @Test
  public void testNormalizeBlocks() {
    Node node = new Node(Token.NUMBER);
    prepareAst.normalizeBlocks(node);

    // assert statements
    // ...
  }
  
  @Test
  public void testPrepareAnnotations() {
    PrepareAst.PrepareAnnotations annotations = new PrepareAst.PrepareAnnotations();
    NodeTraversal traversal = new NodeTraversal();
    Node node = new Node(Token.STRING);

    annotations.shouldTraverse(traversal, node, null);

    // assert statements
    // ...
  }
  
  @Test
  public void testAnnotateCalls() {
    Node node = new Node(Token.CALL);
    prepareAst.annotateCalls(node);

    // assert statements
    // ...
  }
  
  @Test
  public void testAnnotateDispatchers() {
    Node function = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    parent.addChildToFront(function);

    prepareAst.annotateDispatchers(function, parent);

    // assert statements
    // ...
  }
  
  @Test
  public void testNormalizeObjectLiteralAnnotations() {
    Node objlit = new Node(Token.OBJECTLIT);

    prepareAst.normalizeObjectLiteralAnnotations(objlit);

    // assert statements
    // ...
  }
  
  @Test
  public void testNormalizeObjectLiteralKeyAnnotations() {
    Node objlit = new Node(Token.OBJECTLIT);
    Node key = new Node(Token.NAME);
    Node value = new Node(Token.STRING);
    key.addChildToFront(value);
    objlit.addChildToFront(key);

    prepareAst.normalizeObjectLiteralKeyAnnotations(objlit, key, value);

    // assert statements
    // ...
  }
}