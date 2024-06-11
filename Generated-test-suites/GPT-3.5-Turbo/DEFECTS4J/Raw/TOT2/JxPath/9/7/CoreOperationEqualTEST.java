import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression(), new Expression());
        // Typical use case
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));

        // Edge cases
        // test when both arguments are null
        CoreOperationEqual nullArgs = new CoreOperationEqual(null, null);
        assertEquals(Boolean.TRUE, nullArgs.computeValue(new EvalContext()));
        
        // test when arguments are equal but of different types
        CoreOperationEqual differentTypes = new CoreOperationEqual(new Expression("1"), new Expression(1));
        assertEquals(Boolean.TRUE, differentTypes.computeValue(new EvalContext()));

        // Error scenarios
        // test when context is null
        CoreOperationEqual nullContext = new CoreOperationEqual(new Expression(), new Expression());
        assertEquals(Boolean.FALSE, nullContext.computeValue(null));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression(), new Expression());
        // Typical use case
        assertEquals("=", coreOperationEqual.getSymbol());

        // Edge cases - none in this method

        // Error scenarios - none in this method
    }
}