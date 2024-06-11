import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new DummyExpression(5);
        Expression arg2 = new DummyExpression(10);
        CoreOperationLessThan lt = new CoreOperationLessThan(arg1, arg2);
        
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, lt.computeValue(context));
    }
    
    // Additional tests for edge cases and exception handling scenarios
    
    @Test
    public void testComputeValueEqual() {
        Expression arg1 = new DummyExpression(5);
        Expression arg2 = new DummyExpression(5);
        CoreOperationLessThan lt = new CoreOperationLessThan(arg1, arg2);
        
        EvalContext context = new EvalContext();
        assertEquals(Boolean.FALSE, lt.computeValue(context));
    }
    
    @Test
    public void testComputeValueNotEqual() {
        Expression arg1 = new DummyExpression(10);
        Expression arg2 = new DummyExpression(5);
        CoreOperationLessThan lt = new CoreOperationLessThan(arg1, arg2);
        
        EvalContext context = new EvalContext();
        assertEquals(Boolean.FALSE, lt.computeValue(context));
    }
    
    // Dummy Expression class for testing purposes
    private class DummyExpression extends Expression {
        private double value;
        
        public DummyExpression(double value) {
            this.value = value;
        }
        
        @Override
        public Object computeValue(EvalContext context) {
            return value;
        }
        
        @Override
        public String toString() {
            return Double.toString(value);
        }
    }
}