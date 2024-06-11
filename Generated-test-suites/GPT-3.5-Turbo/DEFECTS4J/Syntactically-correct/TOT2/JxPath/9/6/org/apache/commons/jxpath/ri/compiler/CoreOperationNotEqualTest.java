import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testConstructor() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertNotNull(coreOperationNotEqual);
        assertEquals(arg1, coreOperationNotEqual.getArgs()[0]);
        assertEquals(arg2, coreOperationNotEqual.getArgs()[1]);
    }

    @Test
    public void testComputeValue_equalArguments() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new MockExpression(5), new MockExpression(5));
        assertFalse((Boolean)coreOperationNotEqual.computeValue(new MockEvalContext()));
    }

    @Test
    public void testComputeValue_notEqualArguments() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new MockExpression(5), new MockExpression(10));
        assertTrue((Boolean)coreOperationNotEqual.computeValue(new MockEvalContext()));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new MockExpression(), new MockExpression());
        assertEquals("!=", coreOperationNotEqual.getSymbol());
    }

    // Edge test cases
    @Test
    public void testComputeValue_nullArgument() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new MockExpression(null), new MockExpression(null));
        assertFalse((Boolean)coreOperationNotEqual.computeValue(new MockEvalContext()));
    }

    // Exception handling scenarios
    @Test(expected = NullPointerException.class)
    public void testConstructor_nullArgument() {
        new CoreOperationNotEqual(null, new MockExpression());
    }
}