import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    private CoreOperationEqual coreOperationEqual;

    @Before
    public void setup() {
        coreOperationEqual = new CoreOperationEqual(null, null);
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = coreOperationEqual.getSymbol();
        assertEquals("=", symbol);
    }
}