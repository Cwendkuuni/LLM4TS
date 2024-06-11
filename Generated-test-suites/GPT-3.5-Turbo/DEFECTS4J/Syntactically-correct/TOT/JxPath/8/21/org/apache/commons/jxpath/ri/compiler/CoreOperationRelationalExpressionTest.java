import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression coreOperationRelationalExpression;

    @Test
    public void testComputeValue_Equals() {
        coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        EvalContext context = new EvalContext();
        Object result = coreOperationRelationalExpression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_LessThan() {
        coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        EvalContext context = new EvalContext();
        Object result = coreOperationRelationalExpression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_GreaterThan() {
        coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        EvalContext context = new EvalContext();
        Object result = coreOperationRelationalExpression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testReduce_SelfContext() {
        coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Object o = new SelfContext();
        Object result = coreOperationRelationalExpression.reduce(o);
        assertEquals(((SelfContext) o).getSingleNodePointer(), result);
    }

    @Test
    public void testReduce_Collection() {
        coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Object o = new ArrayList<>();
        ((ArrayList) o).add(1);
        Object result = coreOperationRelationalExpression.reduce(o);
        assertTrue(result instanceof Iterator);
    }

    @Test
    public void testCompute_WithIterator() {
        coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Iterator leftIterator = Arrays.asList(1, 2, 3, 4).iterator();
        Iterator rightIterator = Arrays.asList(5, 6, 7, 8).iterator();

        boolean result = coreOperationRelationalExpression.compute(leftIterator, rightIterator);
        assertFalse(result);
    }

    @Test
    public void testCompute_LeftIterator() {
        coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Iterator leftIterator = Arrays.asList(1, 2, 3, 4).iterator();
        Object right = 2;

        boolean result = coreOperationRelationalExpression.compute(leftIterator, right);
        assertTrue(result);
    }

    @Test
    public void testCompute_RightIterator() {
        coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Object left = 3;
        Iterator rightIterator = Arrays.asList(1, 2, 3, 4).iterator();

        boolean result = coreOperationRelationalExpression.compute(left, rightIterator);
        assertTrue(result);
    }

    @Test
    public void testCompute_Compare() {
        coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Object left = 3;
        Object right = 2;

        boolean result = coreOperationRelationalExpression.compute(left, right);
        assertTrue(result);
    }

    // Add more test cases for edge cases and error scenarios
    
}