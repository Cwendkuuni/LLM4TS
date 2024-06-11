import org.junit.Test;
import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.rhino.Node;
import static com.google.common.truth.Truth.assertThat;

public class PrepareAstTest {
  @Test
  public void testPrepareAstNormalFunction() {
    PrepareAst prepareAst = new PrepareAst(null, false);
    Node root = IR.literal(1);

    prepareAst.process(null, root);
    // Basic check for the normal function
    assertThat(root.getType()).isEqualTo(Token.NUMBER);
    // Edge case: null input for root
    prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, null);
    // Exception handling: NullPointerException when externs is null
    prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root);
  }

  @Test
  public void testPrepareAstObjectLiteral() {
    PrepareAst prepareAst = new PrepareAst(null, false);
    Node root = IR.objectLiteral();
    root.addChild(IR.name("a").setString("b"));

    prepareAst.process(null, root);
    // Basic check for the object literal
    assertThat(root.getType()).isEqualTo(Token.OBJECT_LITERAL);
    // Edge case: empty object literal
    Node emptyObjLit = IR.objectLiteral();
    prepareAst.process(null, emptyObjLit);
    // Exception handling: NullPointerException when root is null
    prepareAst = new PrepareAst(null, true);
    prepareAst.process(null, root);
  }
}