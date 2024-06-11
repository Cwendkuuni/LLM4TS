import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PrepareAst;

public class PrepareAstTest {

  @Test
  public void testPrepareAst() {
    PrepareAst preparer = new PrepareAst(null, false);

    // Test with null externs and root
    preparer.process(null, null);

    // Test with valid externs and root
    Node externs = IR.objectLit().addMember("foo", IR.stringLit("bar")).toNode();
    Node root = IR.program().addChild(externs).toNode();
    preparer.process(externs, root);

    // Test with checkOnly set to true
    preparer = new PrepareAst(null, true);
    preparer.process(externs, root);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPrepareAstWithNullRoot() {
    PrepareAst preparer = new PrepareAst(null, false);
    preparer.process(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPrepareAstWithNullExterns() {
    PrepareAst preparer = new PrepareAst(null, false);
    preparer.process(null, IR.program().toNode());
  }
}