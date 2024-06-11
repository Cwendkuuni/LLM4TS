import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    private CoreOperationEqual coreOperationEqual;
    private Expression arg1;
    private Expression arg2;
    private EvalContext context;

    @Before
    public void setUp() {
        arg1 = new Expression();
        arg2 = new Expression();
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        context = new EvalContext();
    }

    @Test
    public void testComputeValue_typicalCase() {
        // Typical case - two equal expressions
        arg1.setResult("a");
        arg2.setResult("a");
        assertEquals(true, coreOperationEqual.computeValue(context));
        // Reverse the arguments
        arg1.setResult("a");
        arg2.setResult("a");
        assertEquals(true, coreOperationEqual.computeValue(context));
    }

    @Test
    public void testComputeValue_typicalCase_notEqual() {
        // Typical case - two unequal expressions
        arg1.setResult("a");
        arg2.setResult("b");
        assertEquals(false, coreOperationEqual.computeValue(context));
        // Reverse the arguments
        arg1.setResult("b");
        arg2.setResult("a");
        assertEquals(false, coreOperationEqual.computeValue(context));
    }

    @Test
    public void testComputeValue_edgeCase_nullArgument() {
        // Edge case - null arguments
        arg1.setResult(null);
        arg2.setResult(null);
        assertEquals(true, coreOperationEqual.computeValue(context));
        // Reverse the arguments
        arg1.setResult(null);
        arg2.setResult(null);
        assertEquals(true, coreOperationEqual.computeValue(context));
    }

    @Test
    public void testComputeValue_edgeCase_oneNullArgument() {
        // Edge case - one null argument
        arg1.setResult("a");
        arg2.setResult(null);
        assertEquals(false, coreOperationEqual.computeValue(context));
        // Reverse the arguments
        arg1.setResult(null);
        arg2.setResult("a");
        assertEquals(false, coreOperationEqual.computeValue(context));
    }

    @Test
    public void testComputeValue_edgeCase_differentTypes() {
        // Edge case - different types for arguments
        arg1.setResult(10);
        arg2.setResult("10");
        assertEquals(false, coreOperationEqual.computeValue(context));
        // Reverse the arguments
        arg1.setResult("10");
        arg2.setResult(10);
        assertEquals(false, coreOperationEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        assertEquals("=", coreOperationEqual.getSymbol());
    }

}