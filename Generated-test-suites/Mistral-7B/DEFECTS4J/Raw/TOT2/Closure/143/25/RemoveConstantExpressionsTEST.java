import static org.junit.Assert.*;

public class RemoveConstantExpressionsTest {
  @Test
  public void testConstructor() {
    AbstractCompiler compiler = mock(AbstractCompiler.class); // Assuming AbstractCompiler is mocked
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(compiler);
    assertNotNull(removeConstantExpressions);
  }
}