import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationEqualTest {

    private CoreOperationEqual coreOperationEqual;

    @Test
    public void testComputeValueWithEqualArgumentsShouldReturnTrue() {
        EvalContext context = new EvalContext(null); // create a new instance of EvalContext
        String arg1 = "value";
        String arg2 = "value";
        coreOperationEqual = new CoreOperationEqual(new Expression(arg1), new Expression(arg2));
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(context));
    }

    @Test
    public void testComputeValueWithNotEqualArgumentsShouldReturnFalse() {
        EvalContext context = new EvalContext(null); // create a new instance of EvalContext
        String arg1 = "value1";
        String arg2 = "value2";
        coreOperationEqual = new CoreOperationEqual(new Expression(arg1), new Expression(arg2));
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(context));
    }

    @Test
    public void testGetSymbolShouldReturnEqualSymbol() {
        String expectedSymbol = "=";
        coreOperationEqual = new CoreOperationEqual(new Expression(), new Expression());
        assertEquals(expectedSymbol, coreOperationEqual.getSymbol());
    }

}