import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.lang.reflect.Method;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;
import com.google.common.base.Preconditions;

public class PrepareAstTest {
  
  private PrepareAst prepareAst;
  private AbstractCompiler compiler;
  
  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    prepareAst = new PrepareAst(compiler);
  }
  
  @After
  public void tearDown() {
    compiler = null;
    prepareAst = null;
  }
  
  @Test
  public void testProcess() {
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    prepareAst.process(externs, root);
    // Add your assertions here
  }
  
  @Test
  public void testNormalizeNodeTypes() {
    Node node = new Node(Token.EXPR_RESULT);
    prepareAst.normalizeNodeTypes(node);
    // Add your assertions here
  }
  
  @Test
  public void testNormalizeBlocks() {
    Node node = new Node(Token.IF);
    prepareAst.normalizeBlocks(node);
    // Add your assertions here
  }
  
  @Test
  public void testPrepareAnnotations() {
    PrepareAst.PrepareAnnotations annotations = new PrepareAst.PrepareAnnotations();
    NodeTraversal t = new NodeTraversal(compiler, annotations);
    Node n = new Node(Token.CALL);
    Node parent = new Node(Token.ROOT);
    annotations.visit(t, n, parent);
    // Add your assertions here
  }
  
  @Test
  public void testShouldTraverse() {
    PrepareAst.PrepareAnnotations annotations = new PrepareAst.PrepareAnnotations();
    NodeTraversal t = new NodeTraversal(compiler, annotations);
    Node n = new Node(Token.OBJECTLIT);
    Node parent = new Node(Token.ROOT);
    assertTrue(annotations.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testVisit() {
    PrepareAst.PrepareAnnotations annotations = new PrepareAst.PrepareAnnotations();
    NodeTraversal t = new NodeTraversal(compiler, annotations);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ROOT);
    annotations.visit(t, n, parent);
    // Add your assertions here
  }
  
  @Test
  public void testNormalizeObjectLiteralAnnotations() {
    Node objlit = new Node(Token.OBJECTLIT);
    prepareAst.normalizeObjectLiteralAnnotations(objlit);
    // Add your assertions here
  }
  
  @Test
  public void testAnnotateCalls() {
    Node n = new Node(Token.CALL);
    prepareAst.annotateCalls(n);
    // Add your assertions here
  }
  
  @Test
  public void testAnnotateDispatchers() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    prepareAst.annotateDispatchers(n, parent);
    // Add your assertions here
  }
  
  @Test
  public void testNormalizeObjectLiteralKeyAnnotations() {
    Node objlit = new Node(Token.OBJECTLIT);
    Node key = new Node(Token.NAME);
    Node value = new Node(Token.FUNCTION);
    prepareAst.normalizeObjectLiteralKeyAnnotations(objlit, key, value);
    // Add your assertions here
  }
}