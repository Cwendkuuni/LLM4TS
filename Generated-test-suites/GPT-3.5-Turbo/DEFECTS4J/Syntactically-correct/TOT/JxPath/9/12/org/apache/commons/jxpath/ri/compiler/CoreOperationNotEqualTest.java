import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    private CoreOperationNotEqual operation;

    @Before
    public void setUp() {
        operation = new CoreOperationNotEqual(null, null);
    }

    // Test computeValue() method
    @Test
    public void testComputeValue_EqualArgs_ReturnsFalse() {
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_NotEqualArgs_ReturnsTrue() {
        EvalContext context = new EvalContext();
        context.setContextNode(null); // Set context node based on test case
        context.setPosition(1); // Set position based on test case

        // Test case 1: Different Strings
        operation = new CoreOperationNotEqual(new Expression("string1"), new Expression("string2"));
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);

        // Test case 2: Different Numbers
        operation = new CoreOperationNotEqual(new Expression(123), new Expression(456));
        result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }

    // Test getSymbol() method
    @Test
    public void testGetSymbol_ReturnsNotEqualSymbol() {
        String symbol = operation.getSymbol();
        assertEquals("!=", symbol);
    }

    // Additional Test Cases
    // ...

}