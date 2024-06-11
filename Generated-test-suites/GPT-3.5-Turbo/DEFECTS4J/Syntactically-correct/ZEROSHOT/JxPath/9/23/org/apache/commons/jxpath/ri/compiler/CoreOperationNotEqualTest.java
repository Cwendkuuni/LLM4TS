import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationNotEqualTest {
  
  private CoreOperationNotEqual operation;

  @Before
  public void setUp() {
    Expression arg1 = new ExpressionStub();
    Expression arg2 = new ExpressionStub();
    operation = new CoreOperationNotEqual(arg1, arg2);
  }
  
  @Test
  public void testComputeValue() {
    EvalContext context = new EvalContext();
    
    // Test when the two arguments are equal
    ExpressionStub stub1 = new ExpressionStub();
    ExpressionStub stub2 = new ExpressionStub();
    stub1.setValue(context, "value");
    stub2.setValue(context, "value");
    operation.setArguments(new Expression[] { stub1, stub2 });
    assertFalse((Boolean) operation.computeValue(context));
    
    // Test when the two arguments are not equal
    ExpressionStub stub3 = new ExpressionStub();
    ExpressionStub stub4 = new ExpressionStub();
    stub3.setValue(context, "value1");
    stub4.setValue(context, "value2");
    operation.setArguments(new Expression[] { stub3, stub4 });
    assertTrue((Boolean) operation.computeValue(context));
  }
  
  @Test
  public void testGetSymbol() {
    assertEquals("!=", operation.getSymbol());
  }
  
  private class ExpressionStub extends Expression {
    private Object[] values;
    
    public Object computeValue(EvalContext context) {
      return values;
    }
    
    public void setValues(Object[] values) {
      this.values = values;
    }
    
    public void setValue(EvalContext context, Object value) {
      values = new Object[] { value };
    }
  }
}