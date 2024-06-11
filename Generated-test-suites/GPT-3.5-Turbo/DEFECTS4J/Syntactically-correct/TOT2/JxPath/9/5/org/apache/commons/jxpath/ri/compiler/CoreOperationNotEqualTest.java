import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_NotEqual_ReturnsTrue() {
        CoreOperationNotEqual coreOperation = new CoreOperationNotEqual(new Expression("value1"), new Expression("value2"));
        boolean result = (Boolean) coreOperation.computeValue(new EvalContext());
        assertTrue(result);
    }

    @Test
    public void testGetSymbol_ReturnsNotEqualSymbol() {
        CoreOperationNotEqual coreOperation = new CoreOperationNotEqual(new Expression("value1"), new Expression("value2"));
        assertEquals("!=", coreOperation.getSymbol());
    }

    @Test
    public void testComputeValue_Equal_ReturnsFalse() {
        CoreOperationNotEqual coreOperation = new CoreOperationNotEqual(new Expression("value"), new Expression("value"));
        boolean result = (Boolean) coreOperation.computeValue(new EvalContext());
        assertFalse(result);
    }

}