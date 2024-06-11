import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PrepareAst;
import org.junit.Test;

public class PrepareAstTest {

  @Test
  public void testPrepareAst() {
    AbstractCompiler compiler = PrepareAstTest.createTestCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = IR.program();
    prepareAst.process(null, root);

    // Basic functionality test
    assertTrue(root.isProgram());
  }
}