import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue_TypicalCase() {
        CoreOperationEqual operation = new CoreOperationEqual(new DummyExpression("Hello"), new DummyExpression("Hello"));
        
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_EdgeCase() {
        CoreOperationEqual operation = new CoreOperationEqual(new DummyExpression(null), new DummyExpression(null));
        
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_ErrorCase() {
        CoreOperationEqual operation = new CoreOperationEqual(new DummyExpression(new Object()), new DummyExpression("Hello"));
        
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationEqual operation = new CoreOperationEqual(new DummyExpression("Hello"), new DummyExpression("Hello"));
        
        String symbol = operation.getSymbol();
        
        assertEquals("=", symbol);
    }
    
    // DummyExpression class is used for testing purposes only
    private class DummyExpression extends Expression {
        private Object value;
        
        public DummyExpression(Object value) {
            this.value = value;
        }
        
        public Object computeValue(EvalContext context) {
            return value;
        }
    }
}