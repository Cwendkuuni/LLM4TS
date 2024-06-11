import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression coreOperationRelationalExpression;

    @Before
    public void setUp() {
        coreOperationRelationalExpression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
    }

    @Test
    public void testComputeValue_LeftIsSmaller() {
        EvalContext context = null;
        Object left = 5;
        Object right = 10;
        boolean result = coreOperationRelationalExpression.compute(left, right);
        assertFalse(result);
    }

    @Test
    public void testComputeValue_LeftIsEqual() {
        EvalContext context = null;
        Object left = 10;
        Object right = 10;
        boolean result = coreOperationRelationalExpression.compute(left, right);
        assertTrue(result);
    }

    @Test
    public void testComputeValue_LeftIsGreater() {
        EvalContext context = null;
        Object left = 15;
        Object right = 10;
        boolean result = coreOperationRelationalExpression.compute(left, right);
        assertTrue(result);
    }

    @Test
    public void testComputeValue_LeftIsNaN() {
        EvalContext context = null;
        Object left = Double.NaN;
        Object right = 10;
        boolean result = coreOperationRelationalExpression.compute(left, right);
        assertFalse(result);
    }

    @Test
    public void testComputeValue_RightIsNaN() {
        EvalContext context = null;
        Object left = 10;
        Object right = Double.NaN;
        boolean result = coreOperationRelationalExpression.compute(left, right);
        assertFalse(result);
    }

    @Test
    public void testComputeValue_LeftIsIterator_RightIsIterator() {
        EvalContext context = null;
        List<Integer> left = new ArrayList<>();
        left.add(1);
        left.add(2);
        left.add(3);
        List<Integer> right = new ArrayList<>();
        right.add(2);
        right.add(4);
        right.add(6);
        boolean result = coreOperationRelationalExpression.compute(left.iterator(), right.iterator());
        assertTrue(result);
    }

    @Test
    public void testComputeValue_LeftIsIterator_RightIsValue() {
        EvalContext context = null;
        List<Integer> left = new ArrayList<>();
        left.add(1);
        left.add(2);
        left.add(3);
        Object right = 2;
        boolean result = coreOperationRelationalExpression.compute(left.iterator(), right);
        assertTrue(result);
    }

    @Test
    public void testComputeValue_LeftIsValue_RightIsIterator() {
        EvalContext context = null;
        Object left = 2;
        List<Integer> right = new ArrayList<>();
        right.add(1);
        right.add(2);
        right.add(3);
        boolean result = coreOperationRelationalExpression.compute(left, right.iterator());
        assertTrue(result);
    }
}