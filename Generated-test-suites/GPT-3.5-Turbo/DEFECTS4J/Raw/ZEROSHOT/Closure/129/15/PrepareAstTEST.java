import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.IR;
import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.PrepareAst.PrepareAnnotations;

public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    Node n = IR.root();
    PrepareAst prepareAst = new PrepareAst(null, true);
    prepareAst.normalizeNodeTypes(n);
    // Add assertions here
  }

  @Test
  public void testNormalizeBlocks() {
    Node n = IR.root();
    PrepareAst prepareAst = new PrepareAst(null, true);
    prepareAst.normalizeBlocks(n);
    // Add assertions here
  }

  @Test
  public void testPrepareAnnotations() {
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = IR.root();
    Node parent = IR.root();
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    boolean result = prepareAnnotations.shouldTraverse(t, n, parent);
    assertFalse(result);
    // Add assertions here
  }

  @Test
  public void testShouldTraverse() {
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = IR.root();
    Node parent = IR.root();
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    boolean result = prepareAnnotations.shouldTraverse(t, n, parent);
    assertTrue(result);
    // Add assertions here
  }

  @Test
  public void testVisit() {
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = IR.root();
    Node parent = IR.root();
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    prepareAnnotations.visit(t, n, parent);
    // Add assertions here
  }

  @Test
  public void testNormalizeObjectLiteralAnnotations() {
    Node objlit = IR.root();
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    prepareAnnotations.normalizeObjectLiteralAnnotations(objlit);
    // Add assertions here
  }

  @Test
  public void testAnnotateCalls() {
    Node n = IR.root();
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    prepareAnnotations.annotateCalls(n);
    // Add assertions here
  }

  @Test
  public void testAnnotateDispatchers() {
    Node n = IR.root();
    Node parent = IR.root();
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    prepareAnnotations.annotateDispatchers(n, parent);
    // Add assertions here
  }

  @Test
  public void testNormalizeObjectLiteralKeyAnnotations() {
    Node objlit = IR.root();
    Node key = IR.root();
    Node value = IR.root();
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    prepareAnnotations.normalizeObjectLiteralKeyAnnotations(objlit, key, value);
    // Add assertions here
  }

}