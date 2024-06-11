import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

  @Test
  public void testComputeValue() {
    CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new ExpressionMock(), new ExpressionMock());
    EvalContext context = new EvalContext();

    // Test case 1
    ExpressionMock arg1 = new ExpressionMock(); 
    ExpressionMock arg2 = new ExpressionMock(); 
    arg1.setComputeValueResult(5.0);
    arg2.setComputeValueResult(10.0);
    operation.setArgs(new Expression[]{arg1, arg2});
    assertEquals(Boolean.TRUE, operation.computeValue(context));

    // Test case 2
    arg1.setComputeValueResult(15.0);
    arg2.setComputeValueResult(10.0);
    operation.setArgs(new Expression[]{arg1, arg2});
    assertEquals(Boolean.FALSE, operation.computeValue(context));
  }

  @Test
  public void testGetSymbol() {
    CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new ExpressionMock(), new ExpressionMock());
    assertEquals("<=", operation.getSymbol());
  }
  
}

class ExpressionMock extends Expression {
  private Object computeValueResult;

  public void setComputeValueResult(Object result) {
    this.computeValueResult = result;
  }

  public Object computeValue(EvalContext context) {
    return computeValueResult;
  }
}