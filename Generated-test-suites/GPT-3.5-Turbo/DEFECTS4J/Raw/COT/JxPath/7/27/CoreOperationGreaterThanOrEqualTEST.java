
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(null);
        context.setVariable("arg1", 5);
        context.setVariable("arg2", 3);
        Object result = operation.computeValue(context);
        assertEquals(true, result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        String symbol = operation.getSymbol();
        assertEquals(">=", symbol);
    }
}
