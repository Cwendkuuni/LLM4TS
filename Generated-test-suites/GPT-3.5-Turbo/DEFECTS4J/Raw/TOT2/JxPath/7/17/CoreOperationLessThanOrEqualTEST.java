import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual coreOperation = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, coreOperation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual coreOperation = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertEquals("<=", coreOperation.getSymbol());
    }

    @Test
    public void testComputeValueWithNegativeValues() {
        CoreOperationLessThanOrEqual coreOperation = new CoreOperationLessThanOrEqual(arg1, arg2);
        context.setVariable("arg1", -5);
        context.setVariable("arg2", -10);
        assertEquals(Boolean.FALSE, coreOperation.computeValue(context));
    }

    @Test
    public void testComputeValueWithEqualValues() {
        CoreOperationLessThanOrEqual coreOperation = new CoreOperationLessThanOrEqual(arg1, arg2);
        context.setVariable("arg1", 10);
        context.setVariable("arg2", 10);
        assertEquals(Boolean.TRUE, coreOperation.computeValue(context));
    }
}