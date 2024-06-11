import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Test when both values are equal
        // Edge Case: Integer values
        assertEquals(true, new TestCoreOperationRelationalExpression().computeValue(5, 5));

        // Test when values are not equal
        // Typical Case: Integer values
        assertEquals(false, new TestCoreOperationRelationalExpression().computeValue(5, 10));

        // Test when one value is negative
        // Edge Case: Integer values
        assertEquals(true, new TestCoreOperationRelationalExpression().computeValue(-5, 10));

        // Test when both values are floating point numbers
        // Typical Case: Float values
        assertEquals(false, new TestCoreOperationRelationalExpression().computeValue(5.5, 5.6));
    }

    @Test
    public void testEvaluateCompare() {
        // Test when compare is equal
        // Typical Case: Integer values
        assertEquals(true, new TestCoreOperationRelationalExpression().evaluateCompare(0));

        // Test when compare is less than
        // Edge Case: Integer values
        assertEquals(false, new TestCoreOperationRelationalExpression().evaluateCompare(-1));

        // Test when compare is greater than
        // Edge Case: Float values
        assertEquals(true, new TestCoreOperationRelationalExpression().evaluateCompare(1));
    }

    @Test
    public void testContainsMatch() {
        // Test when match is found in the iterator
        // Typical Case: Integer values
        assertEquals(true, new TestCoreOperationRelationalExpression().containsMatch(new TestIterator(5, 10), 5));

        // Test when match is not found in the iterator
        // Edge Case: Integer values
        assertEquals(false, new TestCoreOperationRelationalExpression().containsMatch(new TestIterator(5, 10), 15));
    }

    @Test
    public void testFindMatch() {
        // Test when match is found between two iterators
        // Typical Case: Integer values
        assertEquals(true, new TestCoreOperationRelationalExpression().findMatch(new TestIterator(5, 10), new TestIterator(10, 15)));

        // Test when match is not found between two iterators
        // Edge Case: Float values
        assertEquals(false, new TestCoreOperationRelationalExpression().findMatch(new TestIterator(5.5, 10.5), new TestIterator(10.6, 15.6)));
    }

    // Custom Iterator class for testing
    private class TestIterator implements Iterator<Object> {
        private Object[] values;
        private int index;

        public TestIterator(Object... values) {
            this.values = values;
        }

        @Override
        public boolean hasNext() {
            return index < values.length;
        }

        @Override
        public Object next() {
            return values[index++];
        }
    }

    // Custom CoreOperationRelationalExpression class for testing
    private class TestCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
        TestCoreOperationRelationalExpression() {
            super(new Expression[2]);
        }

        boolean computeValue(Object left, Object right) {
            return compute(left, right);
        }

        boolean evaluateCompare(int compare) {
            return evaluateCompare(compare);
        }
    }
}