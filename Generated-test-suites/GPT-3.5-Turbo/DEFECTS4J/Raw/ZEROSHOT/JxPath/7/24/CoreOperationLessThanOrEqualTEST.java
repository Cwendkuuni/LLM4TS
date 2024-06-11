import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {
    
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
                
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(new ExpressionMock(5), new ExpressionMock(10));
        Object result = expression.computeValue(context);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(new ExpressionMock(5), new ExpressionMock(10));
        String symbol = expression.getSymbol();
        assertEquals("<=", symbol);
    }
    
    // Helper class for creating Expression instances
    class ExpressionMock extends Expression {
        
        private Object value;
        
        public ExpressionMock(Object value) {
            this.value = value;
        }
        
        public Object computeValue(EvalContext context) {
            return value;
        }
    }
}