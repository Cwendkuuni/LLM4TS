import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Test when both values are equal
        CoreOperationRelationalExpression coreOp = new CoreOperationRelationalExpression(null);
        boolean result = coreOp.computeValue("value", "value");
        assertTrue(result);

        // Test when left value is less than right value
        result = coreOp.computeValue(10, 20);
        assertTrue(result);

        // Test when left value is greater than right value
        result = coreOp.computeValue(30, 15);
        assertFalse(result);
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression coreOp = new CoreOperationRelationalExpression(null);

        // Test when compare value is 0
        boolean result = coreOp.evaluateCompare(0);
        assertTrue(result);

        // Test when compare value is negative
        result = coreOp.evaluateCompare(-1);
        assertFalse(result);

        // Test when compare value is positive
        result = coreOp.evaluateCompare(1);
        assertFalse(result);
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression coreOp = new CoreOperationRelationalExpression(null);

        // Test when iterator contains matching value
        boolean result = coreOp.containsMatch(new Iterator("value"), "value");
        assertTrue(result);

        // Test when iterator does not contain matching value
        result = coreOp.containsMatch(new Iterator("test"), "value");
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression coreOp = new CoreOperationRelationalExpression(null);

        // Test when iterators contain matching values
        boolean result = coreOp.findMatch(new Iterator("value1", "value2"), new Iterator("value2"));
        assertTrue(result);

        // Test when iterators do not contain matching values
        result = coreOp.findMatch(new Iterator("value1", "value2"), new Iterator("value3"));
        assertFalse(result);
    }
}