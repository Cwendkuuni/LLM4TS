import org.junit.Test;
import static org.junit.Assert.*;

public class InlineObjectLiteralsTest {

  @Test
  public void testProcess_InlineObject_Literals() {
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    inlineObjectLiterals.process(externs, root);

    // Assertions go here
  }

}