import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue_EqualValues_ReturnsTrue() {
        Object left = 5;
        Object right = 5;

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        boolean result = expression.compute(left, right);

        assertTrue(result);
    }

    @Test
    public void testComputeValue_LessThan_ReturnsTrue() {
        Object left = 3;
        Object right = 5;

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        boolean result = expression.compute(left, right);

        assertTrue(result);
    }

    @Test
    public void testComputeValue_GreaterThan_ReturnsTrue() {
        Object left = 5;
        Object right = 3;

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        boolean result = expression.compute(left, right);

        assertTrue(result);
    }

    @Test
    public void testComputeValue_LeftIsIterator_ReturnsTrue() {
        Object left = new IteratorStub(5, 3, 1);
        Object right = 3;

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        boolean result = expression.compute(left, right);

        assertTrue(result);
    }

    @Test
    public void testComputeValue_RightIsIterator_ReturnsTrue() {
        Object left = 5;
        Object right = new IteratorStub(3, 5, 1);

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        boolean result = expression.compute(left, right);

        assertTrue(result);
    }

    @Test
    public void testComputeValue_BothAreIterator_ReturnsTrue() {
        Object left = new IteratorStub(5, 10, 15);
        Object right = new IteratorStub(3, 5, 7);

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        boolean result = expression.compute(left, right);

        assertTrue(result);
    }

    @Test
    public void testComputeValue_LeftAndRightContainMatch_ReturnsTrue() {
        Object left = new IteratorStub(5, 10, 15);
        Object right = new IteratorStub(3, 10, 7);

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        boolean result = expression.compute(left, right);

        assertTrue(result);
    }

    @Test
    public void testComputeValue_LeftAndRightDoNotContainMatch_ReturnsFalse() {
        Object left = new IteratorStub(5, 10, 15);
        Object right = new IteratorStub(3, 7, 9);

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        boolean result = expression.compute(left, right);

        assertFalse(result);
    }

    private class IteratorStub implements Iterator {
        private final Object[] values;
        private int currentIndex = 0;

        public IteratorStub(Object... values) {
            this.values = values;
        }

        @Override
        public boolean hasNext() {
            return currentIndex < values.length;
        }

        @Override
        public Object next() {
            return values[currentIndex++];
        }
    }
}