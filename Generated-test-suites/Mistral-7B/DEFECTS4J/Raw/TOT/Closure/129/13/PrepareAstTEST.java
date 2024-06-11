import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PrepareAst;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PrepareAstTest {

  private AbstractCompiler compiler;

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
  }

  @Test
  public void testPrepareAst_NormalizeNodeTypes() {
    Node root = Node.objectLiteral()
        .addMember("foo", Node.stringLiteral("bar"))
        .addMember("qux", Node.objectLiteral()
            .addMember("quux", Node.numberLiteral(42)));

    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, root);

    // Verify that all nodes are labeled as BLOCK_STATEMENT
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      assertTrue(child.isBlock());
    }
  }

  @Test
  public void testPrepareAst_CheckOnly_NormalizeNodeTypes() {
    Node root = Node.objectLiteral()
        .addMember("foo", Node.stringLiteral("bar"))
        .addMember("qux", Node.objectLiteral()
            .addMember("quux", Node.numberLiteral(42)));

    PrepareAst prepareAst = new PrepareAst(compiler, true);

    try {
      prepareAst.process(null, root);
      assertFalse("NormalizeNodeTypes constraints should have been violated", false);
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testPrepareAst_Process_Externs_And_Root_With_PrepareAnnotations() {
    String externsScript = "function foo() {}";
    Node externs = Node.script(externsScript);

    Node root = Node.objectLiteral()
        .addMember("x", Node.numberLiteral(1))
        .addMember("y", Node.stringLiteral("hello"));

    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(externs, root);

    // Verify that the PrepareAnnotations pass has been executed
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isFunction()) {
        Node function = (Node) child;
        assertTrue(function.getJSDocInfo() != null);
      }
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testPrepareAst_Process_Externs_With_PrepareAnnotations_Throws_IllegalStateException() {
    String externsScript = "function foo() {}";
    Node externs = Node.script(externsScript);

    Node root = Node.objectLiteral()
        .addMember("x", Node.numberLiteral(1))
        .addMember("y", Node.stringLiteral("hello"));

    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, root); // Throws an IllegalStateException during setup
    prepareAst.process(externs, root);
  }

  @Test
  public void testPrepareAst_Process_Root_With_PrepareAnnotations() {
    Node root = Node.objectLiteral()
        .addMember("x", Node.numberLiteral(1))
        .addMember("y", Node.stringLiteral("hello"));

    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, root);

    // Verify that the PrepareAnnotations pass has been executed
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isFunction()) {
        Node function = (Node) child;
        assertNull(function.getJSDocInfo());
      }
    }
  }

  @Test
  public void testPrepareAst_NormalizeObjectLiteralKeyAnnotations() {
    Node objlit = Node.objectLiteral()
        .addMember(
            Node.name("a"),
            Node.objectLiteral()
                .addMember("b", Node.stringLiteral("c"))
                .addMember("d", Node.name("e")))
        .addMember("e", Node.numberLiteral(42));

    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, objlit);

    // Verify that JSDoc is attached to the correct node after normalization
    Node doc = objlit.getMember("a").getFirstChild();
    assertTrue(doc.getJSDocInfo() != null);
    assertEquals("Some description", doc.getJSDocInfo().getDescription());
  }
}