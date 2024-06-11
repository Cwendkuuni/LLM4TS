import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThan opGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();

        assertTrue((Boolean) opGreaterThan.computeValue(context));

        arg1 = new Constant(3);
        arg2 = new Constant(5);
        opGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        assertFalse((Boolean) opGreaterThan.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThan opGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        assertEquals(">", opGreaterThan.getSymbol());
    }

    @Test(expected = NullPointerException.class)
    public void testNullArgument() {
        CoreOperationGreaterThan opGreaterThan = new CoreOperationGreaterThan(null, null);
        opGreaterThan.computeValue(null);
    }

}