import static org.junit.Assert.assertNotNull;

public class RemoveConstantExpressionsTest {
  //...

  @Test
  public void testRemoveConstantExpressionsConstructor() {
    AbstractCompiler abstractCompiler = new AbstractCompiler() {};
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(abstractCompiler);
    assertNotNull(removeConstantExpressions);
  }
}