import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_NotEqual() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new MockExpression(5), new MockExpression(10));
        boolean result = (Boolean) operation.computeValue(null);
        assertTrue(result);
    }

    @Test
    public void testComputeValue_Equal() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new MockExpression(5), new MockExpression(5));
        boolean result = (Boolean) operation.computeValue(null);
        assertFalse(result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(null, null);
        assertEquals("!=", operation.getSymbol());
    }

    // Edge case: Testing with null values
    @Test
    public void testComputeValue_NullValues() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(null, null);
        boolean result = (Boolean) operation.computeValue(null);
        assertTrue(result);
    }

    // Exception handling: Testing with invalid input types
    @Test(expected = ClassCastException.class)
    public void testComputeValue_InvalidInputTypes() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new MockExpression("invalid"), new MockExpression(5));
        operation.computeValue(null);
    }

    // Additional edge case: Testing with negative numbers
    @Test
    public void testComputeValue_NegativeValues() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new MockExpression(-5), new MockExpression(-10));
        boolean result = (Boolean) operation.computeValue(null);
        assertTrue(result);
    }

    // Additional edge case: Testing with large numbers
    @Test
    public void testComputeValue_LargeValues() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new MockExpression(Integer.MAX_VALUE), new MockExpression(Integer.MIN_VALUE));
        boolean result = (Boolean) operation.computeValue(null);
        assertTrue(result);
    }

    // Exception handling: Testing null context
    @Test(expected = NullPointerException.class)
    public void testComputeValue_NullContext() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new MockExpression(5), new MockExpression(10));
        operation.computeValue(null);
    }

    // Edge case: Testing with equal but different types
    @Test
    public void testComputeValue_EqualDifferentTypes() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new MockExpression(5), new MockExpression("5"));
        boolean result = (Boolean) operation.computeValue(null);
        assertTrue(result);
    }
}