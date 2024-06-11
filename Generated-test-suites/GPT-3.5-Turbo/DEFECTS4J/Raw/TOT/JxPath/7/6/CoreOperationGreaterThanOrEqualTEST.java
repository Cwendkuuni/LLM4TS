import static org.junit.Assert.*;

import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue_TypicalCase() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ExpressionMock(5), new ExpressionMock(3));
        
        boolean result = (boolean) operation.computeValue(new EvalContext());
        
        assertTrue(result);
    }
    
    @Test
    public void testComputeValue_EdgeCase() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ExpressionMock(Double.MAX_VALUE), new ExpressionMock(Double.MAX_VALUE));
        
        boolean result = (boolean) operation.computeValue(new EvalContext());
        
        assertTrue(result);
    }
    
    @Test
    public void testComputeValue_ErrorCase() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ExpressionMock("5"), new ExpressionMock(3));
        
        boolean result = (boolean) operation.computeValue(new EvalContext());
        
        assertFalse(result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ExpressionMock(5), new ExpressionMock(3));
        
        String symbol = operation.getSymbol();
        
        assertEquals(">=", symbol);
    }

    private class ExpressionMock extends Expression {
        
        private Object value;
        
        public ExpressionMock(Object value) {
            this.value = value;
        }
        
        @Override
        public Object computeValue(EvalContext context) {
            return value;
        }
    }
}