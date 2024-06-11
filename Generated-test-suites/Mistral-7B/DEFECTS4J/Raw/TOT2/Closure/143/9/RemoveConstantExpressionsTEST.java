import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class RemoveConstantExpressionsTest {

  @Test
  public void testRemoveConstantExpressionsConstructor() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(compiler);
  }

  @Test
  public void testProcessSuccess() {
    Node externs = new Node(Token.PROGRAM, null);
    Node root = new Node(Token.BLOCK, null);

    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(null);
    removeConstantExpressions.process(externs, root);

    assertFalse("No side effects reported", removeConstantExpressions.getRemoveConstantRValuesCallback().getResult().changed);
  }
}