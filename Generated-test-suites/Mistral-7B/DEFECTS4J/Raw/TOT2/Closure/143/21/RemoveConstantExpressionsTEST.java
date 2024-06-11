import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class RemoveConstantExpressionsTest {
  private AbstractCompiler compiler;
  private RemoveConstantExpressions removeConstantExpressions;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    removeConstantExpressions = new RemoveConstantExpressions(compiler);
  }

  @Test
  public void testRemoveConstantExpressionsConstructor() {
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(compiler);
  }

  @Test
  public void testProcess() {
    Node externs = new Node(Token.PROGRAM, null);
    Node root = new Node(Token.EXPR_STATEMENT, null);

    removeConstantExpressions.process(externs, root);

    ArgumentCaptor<Result> resultCaptor = ArgumentCaptor.forClass(Result.class);
    verify(compiler).notifyCompiler(resultCaptor.capture());
    assertFalse(resultCaptor.getValue().changed);
  }
}