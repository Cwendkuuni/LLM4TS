import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression operation;

    @Before
    public void setUp() {
        operation = new CoreOperationRelationalExpressionMock(new Expression[2]);
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new SingleNodePointerMock());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        int precedence = operation.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        boolean symmetric = operation.isSymmetric();
        assertFalse(symmetric);
    }

    @Test
    public void testEvaluateCompareLessThan() {
        boolean compare = operation.evaluateCompare(-1);
        assertTrue(compare);
    }

    @Test
    public void testEvaluateCompareEquals() {
        boolean compare = operation.evaluateCompare(0);
        assertFalse(compare);
    }

    @Test
    public void testEvaluateCompareGreaterThan() {
        boolean compare = operation.evaluateCompare(1);
        assertFalse(compare);
    }

    @Test
    public void testComputeEqualNumbers() {
        Object left = 5;
        Object right = 5;
        boolean result = operation.compute(left, right);
        assertFalse(result);
    }

    @Test
    public void testComputeDifferentNumbers() {
        Object left = 2;
        Object right = 5;
        boolean result = operation.compute(left, right);
        assertTrue(result);
    }

    @Test
    public void testComputeEqualStrings() {
        Object left = "test";
        Object right = "test";
        boolean result = operation.compute(left, right);
        assertFalse(result);
    }

    @Test
    public void testComputeDifferentStrings() {
        Object left = "test1";
        Object right = "test2";
        boolean result = operation.compute(left, right);
        assertTrue(result);
    }

    @Test
    public void testComputeEqualCollections() {
        Object left = new HashSet<>();
        ((HashSet) left).add(1);
        ((HashSet) left).add(2);

        Object right = new HashSet<>();
        ((HashSet) right).add(2);
        ((HashSet) right).add(1);

        boolean result = operation.compute(left, right);
        assertFalse(result);
    }

    @Test
    public void testComputeDifferentCollections() {
        Object left = new HashSet<>();
        ((HashSet) left).add(1);
        ((HashSet) left).add(2);

        Object right = new HashSet<>();
        ((HashSet) right).add(2);
        ((HashSet) right).add(3);

        boolean result = operation.compute(left, right);
        assertTrue(result);
    }

}

class CoreOperationRelationalExpressionMock extends CoreOperationRelationalExpression {
    public CoreOperationRelationalExpressionMock(Expression[] args) {
        super(args);
    }

    protected boolean evaluateCompare(int compare) {
        if (compare < 0) {
            return true;
        } else {
            return false;
        }
    }
}

class SingleNodePointerMock extends NodePointer {
    // Implement necessary methods for testing purposes
}