import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

  private static CoreOperationGreaterThanOrEqual expression;

  @BeforeClass
  public static void setUp() {
    expression = new CoreOperationGreaterThanOrEqual(new Expression(arg1), new Expression(arg2));
  }

  @Test
  public void testComputeValue_typicalCase() {
    EvalContext context = new EvalContext();

    // Set up arg1 and arg2 to be typical values
    expression.getArgs()[0] = new Expression(5);
    expression.getArgs()[1] = new Expression(3);

    // Compute result
    Object result = expression.computeValue(context);

    // Assert that the result is expected
    assertEquals(Boolean.TRUE, result);
  }

  @Test
  public void testComputeValue_edgeCase() {
    EvalContext context = new EvalContext();

    // Set up arg1 and arg2 to be equal values
    expression.getArgs()[0] = new Expression(5);
    expression.getArgs()[1] = new Expression(5);

    // Compute result
    Object result = expression.computeValue(context);

    // Assert that the result is expected
    assertEquals(Boolean.TRUE, result);
  }

  @Test
  public void testComputeValue_errorCase() {
    EvalContext context = new EvalContext();

    // Set up arg1 and arg2 to be null
    expression.getArgs()[0] = new Expression(null);
    expression.getArgs()[1] = new Expression(null);

    // Compute result
    Object result = expression.computeValue(context);

    // Assert that the result is expected
    assertEquals(Boolean.FALSE, result);
  }

  @Test
  public void testGetSymbol_typicalCase() {
    // Get symbol
    String symbol = expression.getSymbol();

    // Assert that the symbol is expected
    assertEquals(">=", symbol);
  }
}