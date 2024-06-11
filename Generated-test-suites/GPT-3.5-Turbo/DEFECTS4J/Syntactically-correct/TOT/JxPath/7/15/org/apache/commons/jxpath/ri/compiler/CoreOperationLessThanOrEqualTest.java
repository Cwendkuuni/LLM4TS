import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

  @Test
  public void testComputeValue() {
    // Typical use case
    CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(new LiteralExpression(5), new LiteralExpression(10));
    assertEquals(Boolean.TRUE, expression.computeValue(null));

    // Edge case
    expression = new CoreOperationLessThanOrEqual(new LiteralExpression(10), new LiteralExpression(5));
    assertEquals(Boolean.FALSE, expression.computeValue(null));

    // Error scenario
    try {
      expression = new CoreOperationLessThanOrEqual(new LiteralExpression("abc"), new LiteralExpression(5));
      expression.computeValue(null);
      fail("Expected Exception");
    } catch (Exception e) {
      assertEquals("Error: Cannot compare 'abc' with 5", e.getMessage());
    }
  }

  @Test
  public void testGetSymbol() {
    CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(null, null);
    assertEquals("<=", expression.getSymbol());
  }
}