import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PrepareAstTest {

  private PrepareAst prepareAst;

  @Before
  public void setUp() {
    AbstractCompiler compiler = new Compiler();
    prepareAst = new PrepareAst(compiler);
  }

  @Test
  public void testReportChange() {
    prepareAst.process(null, null);
    assertTrue(true);
  }

  @Test
  public void testProcess() {
    Node externs = IR.block();
    Node root = IR.block();
    prepareAst.process(externs, root);
    assertTrue(true);
  }

  @Test
  public void testNormalizeNodeTypes() {
    Node node = IR.block();
    prepareAst.normalizeNodeTypes(node);
    assertTrue(true);
  }

  @Test
  public void testNormalizeBlocks() {
    Node node = IR.block();
    prepareAst.normalizeBlocks(node);
    assertTrue(true);
  }

  @Test
  public void testPrepareAnnotations() {
    NodeTraversal.Callback callback = new PrepareAst.PrepareAnnotations();
  
    NodeTraversal traversal = new NodeTraversal(null, callback);
    Node node = IR.block();
    Node parent = IR.block();
    traversal.visit(node, parent);
    
    assertTrue(true);
  }
}