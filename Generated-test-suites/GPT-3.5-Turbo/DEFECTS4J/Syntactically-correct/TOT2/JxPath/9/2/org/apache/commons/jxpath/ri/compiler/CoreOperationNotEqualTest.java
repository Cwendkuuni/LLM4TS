import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_NotEqual() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Constant(5), new Constant(10));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testComputeValue_Equal() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Constant("abc"), new Constant("abc"));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Constant(5), new Constant(10));
        assertEquals("!=", operation.getSymbol());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_NullArgument() {
        new CoreOperationNotEqual(null, new Constant(10));
    }
}