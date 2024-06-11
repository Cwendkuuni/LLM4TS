import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {
  
  private CoreOperationLessThan operation;
  
  @Before
  public void setUp() {
    operation = new CoreOperationLessThan(arg1, arg2);
  }
  
  @Test
  public void testComputeValueLessThan() {
    EvalContext context = new EvalContext();
    context.setNodeSet(new NodeSet());
    Double value1 = 5.0;
    Double value2 = 10.0;
    expression1.setComputedValue(value1);
    expression2.setComputedValue(value2);
    Boolean result = (Boolean) operation.computeValue(context);
    assertTrue(result);
  }
  
  @Test
  public void testComputeValueEqualTo() {
    EvalContext context = new EvalContext();
    context.setNodeSet(new NodeSet());
    Double value1 = 5.0;
    Double value2 = 5.0;
    expression1.setComputedValue(value1);
    expression2.setComputedValue(value2);
    Boolean result = (Boolean) operation.computeValue(context);
    assertFalse(result);
  }
  
  @Test
  public void testComputeValueGreaterThan() {
    EvalContext context = new EvalContext();
    context.setNodeSet(new NodeSet());
    Double value1 = 10.0;
    Double value2 = 5.0;
    expression1.setComputedValue(value1);
    expression2.setComputedValue(value2);
    Boolean result = (Boolean) operation.computeValue(context);
    assertFalse(result);
  }
  
  @Test
  public void testGetSymbol() {
    String symbol = operation.getSymbol();
    assertEquals("<", symbol);
  }
}