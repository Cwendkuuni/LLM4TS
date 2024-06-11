import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new ExpressionMock(), new ExpressionMock());
        EvalContext context = new EvalContext();
        
        // Typical use case where arguments are not equal
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);
        
        // Typical use case where arguments are equal
        CoreOperationNotEqual operation2 = new CoreOperationNotEqual(new ExpressionMock(), new ExpressionMock());
        operation2.getArgs()[1].setObjectValue(operation2.getArgs()[0].computeValue(context));
        result = operation2.computeValue(context);
        assertFalse((Boolean) result);
        
        // Edge case where both arguments are null
        CoreOperationNotEqual operation3 = new CoreOperationNotEqual(new ExpressionMock(), new ExpressionMock());
        operation3.getArgs()[0].setObjectValue(null);
        operation3.getArgs()[1].setObjectValue(null);
        result = operation3.computeValue(context);
        assertFalse((Boolean) result);
        
        // Error scenario where first argument is null
        CoreOperationNotEqual operation4 = new CoreOperationNotEqual(new ExpressionMock(), new ExpressionMock());
        operation4.getArgs()[0].setObjectValue(null);
        result = operation4.computeValue(context);
        assertTrue((Boolean) result);
        
        // Error scenario where second argument is null
        CoreOperationNotEqual operation5 = new CoreOperationNotEqual(new ExpressionMock(), new ExpressionMock());
        operation5.getArgs()[1].setObjectValue(null);
        result = operation5.computeValue(context);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new ExpressionMock(), new ExpressionMock());
        
        // Typical use case
        String symbol = operation.getSymbol();
        assertEquals("!=", symbol);
        
        // Edge case where symbol is an empty string
        CoreOperationNotEqual operation2 = new CoreOperationNotEqual(new ExpressionMock(), new ExpressionMock());
        operation2.setSymbol("");
        symbol = operation2.getSymbol();
        assertEquals("", symbol);
    }
    
    // Mock class for Expression
    private class ExpressionMock extends Expression {
        
        private Object objectValue;
        
        public Object computeValue(EvalContext context) {
            return objectValue;
        }
        
        public void setObjectValue(Object value) {
            objectValue = value;
        }
    }
}