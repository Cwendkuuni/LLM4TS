import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RemoveConstantExpressionsTest {
  @Test
  public void testProcess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(compiler);
    Node root = new Node(Token.PROGRAM);

    removeConstantExpressions.process(null, root);
    assertDoesNotThrow(() -> removeConstantExpressions.getResult().getChanged());
  }
}