import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

  private CoreOperationGreaterThanOrEqual greaterThanOrEqual;

  @Before
  public void setUp() {
    greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new MockExpression(5), 
                                                            new MockExpression(3));
  }

  @Test
  public void testComputeValue() {
    EvalContext context = new EvalContext();
    Object result = greaterThanOrEqual.computeValue(context);
    assertTrue(result instanceof Boolean);
    assertTrue((boolean)result);
  }

  @Test
  public void testGetSymbol() {
    String symbol = greaterThanOrEqual.getSymbol();
    assertEquals(">=", symbol);
  }

  // Helper class for Mock Expression
  private class MockExpression extends Expression {
    private Object value;

    public MockExpression(Object value) {
      this.value = value;
    }

    public Object computeValue(EvalContext context) {
      return value;
    }
  }

}