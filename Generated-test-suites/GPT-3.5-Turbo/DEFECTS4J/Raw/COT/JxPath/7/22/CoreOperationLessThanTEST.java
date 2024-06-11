
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new ExpressionStub(4), new ExpressionStub(5));
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertTrue((boolean) result);
    }
    
    @Test
    public void testComputeValueEqual() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new ExpressionStub(5), new ExpressionStub(5));
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertFalse((boolean) result);
    }
    
    @Test
    public void testComputeValueNotLessThan() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new ExpressionStub(6), new ExpressionStub(5));
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertFalse((boolean) result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new ExpressionStub(4), new ExpressionStub(5));
        String symbol = expression.getSymbol();
        assertEquals("<", symbol);
    }
    
    // Additional test cases for edge cases and exception handling scenarios can be added here
    
}

// Stub class for Expression used in the tests
class ExpressionStub extends Expression {
  
  private final Object value;

  public ExpressionStub(Object value) {
    this.value = value;
  }

  @Override
  public Object computeValue(EvalContext evalContext) {
    return value;
  }
  
}
