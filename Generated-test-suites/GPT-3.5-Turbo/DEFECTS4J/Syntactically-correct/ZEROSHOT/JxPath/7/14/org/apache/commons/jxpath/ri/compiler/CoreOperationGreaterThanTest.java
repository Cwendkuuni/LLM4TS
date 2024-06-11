import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    private CoreOperationGreaterThan coreOperationGreaterThan;

    @Before
    public void setup() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
    }
    
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        arg1.computeValue(context);
        arg2.computeValue(context);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
    }
    
    @Test
    public void testGetSymbol() {
        String symbol = coreOperationGreaterThan.getSymbol();
        assertNotNull(symbol);
        assertEquals(">", symbol);
    }
}