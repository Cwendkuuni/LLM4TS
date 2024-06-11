import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        Expression arg2 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 3;
            }
        };
        
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        boolean result = (boolean) operation.computeValue(new EvalContext());
        
        assertTrue(result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(null, null);
        String symbol = operation.getSymbol();
        
        assertEquals(">", symbol);
    }
    
    @Test
    public void testComputeValueWithEqualValues() {
        Expression arg1 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        Expression arg2 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        boolean result = (boolean) operation.computeValue(new EvalContext());
        
        assertFalse(result);
    }
    
    @Test
    public void testComputeValueWithDoubleValues() {
        Expression arg1 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 3.14;
            }
        };
        Expression arg2 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 2.7;
            }
        };
        
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        boolean result = (boolean) operation.computeValue(new EvalContext());
        
        assertTrue(result);
    }
    
    @Test
    public void testComputeValueWithNegativeValues() {
        Expression arg1 = new Expression() {
            public Object computeValue(EvalContext context) {
                return -5;
            }
        };
        Expression arg2 = new Expression() {
            public Object computeValue(EvalContext context) {
                return -10;
            }
        };
        
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        boolean result = (boolean) operation.computeValue(new EvalContext());
        
        assertTrue(result);
    }
}


