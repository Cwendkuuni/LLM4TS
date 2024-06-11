import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue_EqualIntegers_ReturnsTrue() {
        Expression[] args = {new Integer(5), new Integer(5)};
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        EvalContext context = new EvalContext(null);
        Object result = expr.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean)result);
    }

    @Test
    public void testComputeValue_NotEqualIntegers_ReturnsFalse() {
        Expression[] args = {new Integer(5), new Integer(10)};
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        EvalContext context = new EvalContext(null);
        Object result = expr.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean)result);
    }

    @Test
    public void testComputeValue_EqualDoubles_ReturnsTrue() {
        Expression[] args = {new Double(3.14), new Double(3.14)};
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        EvalContext context = new EvalContext(null);
        Object result = expr.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean)result);
    }

    @Test
    public void testComputeValue_NotEqualDoubles_ReturnsFalse() {
        Expression[] args = {new Double(3.14), new Double(2.71)};
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        EvalContext context = new EvalContext(null);
        Object result = expr.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean)result);
    }

    @Test
    public void testComputeValue_CompareStrings_ReturnsTrue() {
        Expression[] args = {new String("test"), new String("test")};
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        EvalContext context = new EvalContext(null);
        Object result = expr.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean)result);
    }

    @Test
    public void testComputeValue_NotEqualStrings_ReturnsFalse() {
        Expression[] args = {new String("test"), new String("abc")};
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        EvalContext context = new EvalContext(null);
        Object result = expr.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean)result);
    }

    @Test
    public void testComputeValue_CollectionContainsValue_ReturnsTrue() {
        Expression[] args = {new Integer[]{1, 2, 3}, new Integer(2)};
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        EvalContext context = new EvalContext(null);
        Object result = expr.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean)result);
    }

    @Test
    public void testComputeValue_CollectionDoesNotContainValue_ReturnsFalse() {
        Expression[] args = {new Integer[]{1, 2, 3}, new Integer(4)};
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        EvalContext context = new EvalContext(null);
        Object result = expr.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean)result);
    }

    @Test
    public void testComputeValue_LeftCollectionHasValueInRightCollection_ReturnsTrue() {
        Expression[] args = {new Integer[]{1, 2, 3}, new Integer[]{3, 4, 5}};
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        EvalContext context = new EvalContext(null);
        Object result = expr.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean)result);
    }

    @Test
    public void testComputeValue_LeftCollectionDoesNotHaveValueInRightCollection_ReturnsFalse() {
        Expression[] args = {new Integer[]{1, 2, 3}, new Integer[]{4, 5, 6}};
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        EvalContext context = new EvalContext(null);
        Object result = expr.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean)result);
    }
}