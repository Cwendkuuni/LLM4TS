import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 10;
            }
        };

        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };

        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Object result = greaterThanOrEqual.computeValue(null);
        
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(null, null);
        String symbol = greaterThanOrEqual.getSymbol();

        assertEquals(">=", symbol);
    }
}