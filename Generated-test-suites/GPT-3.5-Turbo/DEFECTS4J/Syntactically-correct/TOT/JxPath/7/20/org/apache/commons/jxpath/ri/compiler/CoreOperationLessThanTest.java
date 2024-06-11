import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();

        // Typical use cases
        // arg1 < arg2, returns true
        context.setNodeSet(new NodeSet(arg1, arg2));
        assertTrue(lessThan.computeValue(context));

        // arg1 >= arg2, returns false
        context.setNodeSet(new NodeSet(arg2, arg1));
        assertFalse(lessThan.computeValue(context));

        // Edge cases
        // arg1 = 0, arg2 = 0, returns false
        context.setNodeSet(new NodeSet(0, 0));
        assertFalse(lessThan.computeValue(context));

        // arg1 = -1, arg2 = 0, returns true
        context.setNodeSet(new NodeSet(-1, 0));
        assertTrue(lessThan.computeValue(context));

        // arg1 = Double.MAX_VALUE, arg2 = Double.MAX_VALUE, returns false
        context.setNodeSet(new NodeSet(Double.MAX_VALUE, Double.MAX_VALUE));
        assertFalse(lessThan.computeValue(context));

        // arg1 = Double.MIN_VALUE, arg2 = Double.MAX_VALUE, returns true
        context.setNodeSet(new NodeSet(Double.MIN_VALUE, Double.MAX_VALUE));
        assertTrue(lessThan.computeValue(context));

        // Error scenarios
        // Null argument, throws NullPointerException
        try {
            CoreOperationLessThan lessThan = new CoreOperationLessThan(null, arg2);
            fail("NullPointerException should have been thrown");
        } catch (NullPointerException e) {
            // exception thrown as expected
        }

        // Non-numeric arguments, throws ClassCastException
        try {
            CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, "string");
            fail("ClassCastException should have been thrown");
        } catch (ClassCastException e) {
            // exception thrown as expected
        }
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);

        // Symbol should always be "<"
        assertEquals("<", lessThan.getSymbol());
    }

}