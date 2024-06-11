import org.junit.Assert;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

  @Test
  public void testComputeValue() {
    EvalContext context = new EvalContext();
    
    Expression arg1 = new Expression(null);
    Expression arg2 = new Expression(null);
    CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
    
    context.setVariable("arg1", "5");
    context.setVariable("arg2", "10");
    Object result = expression.computeValue(context);
    Assert.assertEquals(Boolean.TRUE, result);
    
    context.setVariable("arg1", "10");
    context.setVariable("arg2", "5");
    result = expression.computeValue(context);
    Assert.assertEquals(Boolean.FALSE, result);
    
    context.setVariable("arg1", "5");
    context.setVariable("arg2", "5");
    result = expression.computeValue(context);
    Assert.assertEquals(Boolean.TRUE, result);
  }

  @Test
  public void testGetSymbol() {
    Expression arg1 = new Expression(null);
    Expression arg2 = new Expression(null);
    CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
    
    String symbol = expression.getSymbol();
    Assert.assertEquals("<=", symbol);
  }
}