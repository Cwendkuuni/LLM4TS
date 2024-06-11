import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;

import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {
  
  private CoreOperationGreaterThan operation;
  
  @Before
  public void setUp() {
    Expression arg1 = new ExpressionMock();
    Expression arg2 = new ExpressionMock();
    operation = new CoreOperationGreaterThan(arg1, arg2);
  }
  
  @Test
  public void testComputeValue_Arg1GreaterThanArg2_ReturnsTrue() {
    EvalContext context = new EvalContextMock();
    context.setDoubleValue(10.5);
    double arg2Value = 5.5;
    context.setDoubleValue(arg2Value);
    
    Object result = operation.computeValue(context);
    
    assertTrue(result instanceof Boolean);
    assertTrue((Boolean) result);
  }
  
  @Test
  public void testComputeValue_Arg1LessThanArg2_ReturnsFalse() {
    EvalContext context = new EvalContextMock();
    context.setDoubleValue(5.5);
    double arg2Value = 10.5;
    context.setDoubleValue(arg2Value);
    
    Object result = operation.computeValue(context);
    
    assertTrue(result instanceof Boolean);
    assertFalse((Boolean) result);
  }
  
  @Test
  public void testComputeValue_Arg1EqualToArg2_ReturnsFalse() {
    EvalContext context = new EvalContextMock();
    context.setDoubleValue(5.5);
    double arg2Value = 5.5;
    context.setDoubleValue(arg2Value);
    
    Object result = operation.computeValue(context);
    
    assertTrue(result instanceof Boolean);
    assertFalse((Boolean) result);
  }
  
  @Test
  public void testComputeValue_Arg1Null_ReturnsFalse() {
    EvalContext context = new EvalContextMock();
    context.setDoubleValue(null);
    double arg2Value = 5.5;
    context.setDoubleValue(arg2Value);
    
    Object result = operation.computeValue(context);
    
    assertTrue(result instanceof Boolean);
    assertFalse((Boolean) result);
  }
  
  @Test
  public void testComputeValue_Arg2Null_ReturnsNull() {
    EvalContext context = new EvalContextMock();
    double arg1Value = 5.5;
    context.setDoubleValue(arg1Value);
    context.setDoubleValue(null);
    
    Object result = operation.computeValue(context);
    
    assertNull(result);
  }
  
  @Test
  public void testComputeValue_BothArgsNull_ReturnsNull() {
    EvalContext context = new EvalContextMock();
    context.setDoubleValue(null);
    context.setDoubleValue(null);
    
    Object result = operation.computeValue(context);
    
    assertNull(result);
  }

  private class ExpressionMock extends Expression {
    public Object computeValue(EvalContext context) {
      return null;
    }
    
    public String getSymbol() {
      return null;
    }
  }
  
  private class EvalContextMock extends EvalContext {
    private Double doubleValue;
    
    public void setDoubleValue(Double value) {
      this.doubleValue = value;
    }
    
    public Double getDoubleValue() {
      return doubleValue;
    }
  }
}