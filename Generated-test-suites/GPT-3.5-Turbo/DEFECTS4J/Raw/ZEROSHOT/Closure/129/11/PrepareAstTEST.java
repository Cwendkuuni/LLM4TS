import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class PrepareAstTest {

  private PrepareAst prepareAst;

  @Before
  public void setUp() {
    prepareAst = new PrepareAst(/* compiler */ null);
  }

  @Test
  public void testReportChange() {
    prepareAst.reportChange();
    // Add assertions here
  }

  @Test
  public void testProcess() {
    Node externs = IR.externs(IR.empty());
    Node root = IR.root(IR.empty());
    prepareAst.process(externs, root);
    // Add assertions here
  }

  @Test
  public void testNormalizeNodeTypes() {
    Node node = IR.empty();
    prepareAst.normalizeNodeTypes(node);
    // Add assertions here
  }

  @Test
  public void testNormalizeBlocks() {
    Node node = IR.empty();
    prepareAst.normalizeBlocks(node);
    // Add assertions here
  }

  @Test
  public void testPrepareAnnotations() {
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    NodeTraversal traversal = new NodeTraversal(/* compiler */ null, prepareAnnotations);
    Node objectLit = IR.objectlit(IR.empty());
    prepareAnnotations.shouldTraverse(traversal, objectLit, /* parent */ null);
    // Add assertions here
  }

  @Test
  public void testAnnotateCalls() {
    Node node = IR.call(IR.empty());
    prepareAst.annotateCalls(node);
    // Add assertions here
  }

  @Test
  public void testAnnotateDispatchers() {
    Node functionNode = IR.function(IR.name("foo"), IR.paramList(), IR.block());
    Node parent = IR.assign(IR.empty(), functionNode);
    prepareAst.annotateDispatchers(functionNode, parent);
    // Add assertions here
  }

  @Test
  public void testNormalizeObjectLiteralKeyAnnotations() {
    Node objectLit = IR.objectlit(IR.empty());
    Node key = IR.string("key");
    Node value = IR.empty();
    prepareAst.normalizeObjectLiteralKeyAnnotations(objectLit, key, value);
    // Add assertions here
  }
}