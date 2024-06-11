import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationLessThanTest {
    
    private CoreOperationLessThan operationLessThan;
    private EvalContext context;
    
    @Before
    public void setup() {
        operationLessThan = new CoreOperationLessThan(new ExpressionMock(), new ExpressionMock());
        context = new EvalContextMock();
    }
    
    @Test
    public void testComputeValueLessThan() {
        // Arrange
        double arg1Value = 5.0;
        double arg2Value = 10.0;
        ((ExpressionMock)operationLessThan.getArguments()[0]).setValue(arg1Value);
        ((ExpressionMock)operationLessThan.getArguments()[1]).setValue(arg2Value);
        
        // Act
        Object result = operationLessThan.computeValue(context);
        
        // Assert
        assertTrue((boolean)result);
    }
    
    @Test
    public void testComputeValueNotLessThan() {
        // Arrange
        double arg1Value = 10.0;
        double arg2Value = 5.0;
        ((ExpressionMock)operationLessThan.getArguments()[0]).setValue(arg1Value);
        ((ExpressionMock)operationLessThan.getArguments()[1]).setValue(arg2Value);
        
        // Act
        Object result = operationLessThan.computeValue(context);
        
        // Assert
        assertFalse((boolean)result);
    }
    
    @Test
    public void testComputeValueEqual() {
        // Arrange
        double arg1Value = 5.0;
        double arg2Value = 5.0;
        ((ExpressionMock)operationLessThan.getArguments()[0]).setValue(arg1Value);
        ((ExpressionMock)operationLessThan.getArguments()[1]).setValue(arg2Value);
        
        // Act
        Object result = operationLessThan.computeValue(context);
        
        // Assert
        assertFalse((boolean)result);
    }
    
    @Test
    public void testGetSymbol() {
        // Act
        String symbol = operationLessThan.getSymbol();
        
        // Assert
        assertEquals("<", symbol);
    }

    private class ExpressionMock extends Expression {
        
        private double value;
        
        public void setValue(double value) {
            this.value = value;
        }
        
        @Override
        public Object computeValue(EvalContext context) {
            return value;
        }
    }
    
    private class EvalContextMock extends EvalContext {
        
        @Override
        public double doubleValue(Object object) {
            return (double)object;
        }
    }
}