import static org.junit.Assert.*;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, new Supplier<String>() {
      public String get() {
        return "";
      }
    });

    Node externs = new Node(Token.EXTERN, new Node(Token.BLOCK));
    Node root = new Node(Token.ROOT, new Node(Token.BLOCK));

    inlineObjectLiterals.process(externs, root);

    // Add your assertions here
  }

}