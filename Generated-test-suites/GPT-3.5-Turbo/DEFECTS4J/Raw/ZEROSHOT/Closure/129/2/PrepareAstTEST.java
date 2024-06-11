import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class PrepareAstTest {

  private AbstractCompiler compiler;
  private PrepareAst prepareAst;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    prepareAst = new PrepareAst(compiler);
  }

  @Test
  public void testProcess() {
    Node externs = mock(Node.class);
    Node root = mock(Node.class);

    prepareAst.process(externs, root);

    verify(compiler).traverse(compiler, externs, new PrepareAst.PrepareAnnotations());
    verify(compiler).traverse(compiler, root, new PrepareAst.PrepareAnnotations());
  }

  @Test
  public void testNormalizeNodeTypes() {
    Node n = mock(Node.class);

    prepareAst.normalizeNodeTypes(n);

    // Assert something here
  }

  @Test
  public void testNormalizeBlocks() {
    Node n = mock(Node.class);

    prepareAst.normalizeBlocks(n);

    // Assert something here
  }

  @Test
  public void testPrepareAnnotations() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);

    PrepareAst.PrepareAnnotations annotations = new PrepareAst.PrepareAnnotations();

    annotations.shouldTraverse(t, n, parent);

    // Assert something here
  }

  @Test
  public void testAnnotateCalls() {
    Node n = mock(Node.class);

    prepareAst.annotateCalls(n);

    // Assert something here
  }

  @Test
  public void testAnnotateDispatchers() {
    Node n = mock(Node.class);
    Node parent = mock(Node.class);

    prepareAst.annotateDispatchers(n, parent);

    // Assert something here
  }

  @Test
  public void testNormalizeObjectLiteralAnnotations() {
    Node objlit = mock(Node.class);

    prepareAst.normalizeObjectLiteralAnnotations(objlit);

    // Assert something here
  }

  @Test
  public void testNormalizeObjectLiteralKeyAnnotations() {
    Node objlit = mock(Node.class);
    Node key = mock(Node.class);
    Node value = mock(Node.class);

    prepareAst.normalizeObjectLiteralKeyAnnotations(objlit, key, value);

    // Assert something here
  }
}