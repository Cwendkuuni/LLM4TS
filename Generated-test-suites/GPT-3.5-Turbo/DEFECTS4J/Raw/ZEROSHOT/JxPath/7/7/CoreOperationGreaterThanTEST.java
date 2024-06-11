import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValueGreaterThan() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new ExpressionStub(5), new ExpressionStub(3));
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueNotGreaterThan() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new ExpressionStub(3), new ExpressionStub(5));
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new ExpressionStub(5), new ExpressionStub(3));
        String symbol = expression.getSymbol();
        assertEquals(">", symbol);
    }
    
    private class ExpressionStub implements Expression {
        private Object value;
        
        public ExpressionStub(Object value) {
            this.value = value;
        }
        
        public Object computeValue(EvalContext context) {
            return value;
        }
    }
}