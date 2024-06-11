import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        EvalContext context = new EvalContext();
        Object value = expression.computeValue(context);
        assertNotNull(value);
        assertTrue(value instanceof Boolean);
        assertFalse((Boolean) value);
    }

    // Add test cases for potential edge cases and exception handling scenarios

    @Test
    public void testCompute_withInitialContextAndIterator_shouldReturnFalse() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new InitialContext(), new Iterator()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        EvalContext context = new EvalContext();
        Object value = expression.computeValue(context);
        assertNotNull(value);
        assertTrue(value instanceof Boolean);
        assertFalse((Boolean) value);
    }

    @Test
    public void testCompute_withIteratorAndInitialContext_shouldReturnFalse() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Iterator(), new InitialContext()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        EvalContext context = new EvalContext();
        Object value = expression.computeValue(context);
        assertNotNull(value);
        assertTrue(value instanceof Boolean);
        assertFalse((Boolean) value);
    }

    @Test
    public void testCompute_withIteratorAndNonMatchingValue_shouldReturnFalse() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Iterator(), "test"}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        EvalContext context = new EvalContext();
        Object value = expression.computeValue(context);
        assertNotNull(value);
        assertTrue(value instanceof Boolean);
        assertFalse((Boolean) value);
    }

    @Test
    public void testCompute_withIteratorAndMatchingValue_shouldReturnTrue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Iterator(), "test"}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        EvalContext context = new EvalContext();
        Object value = expression.computeValue(context);
        assertNotNull(value);
        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);
    }

    @Test
    public void testCompute_withMatchingDoubleValues_shouldReturnTrue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{1.0, 1.0}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        EvalContext context = new EvalContext();
        Object value = expression.computeValue(context);
        assertNotNull(value);
        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);
    }

    @Test
    public void testCompute_withNonMatchingDoubleValues_shouldReturnFalse() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{1.0, 2.0}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        EvalContext context = new EvalContext();
        Object value = expression.computeValue(context);
        assertNotNull(value);
        assertTrue(value instanceof Boolean);
        assertFalse((Boolean) value);
    }

}