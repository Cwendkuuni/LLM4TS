
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression coreOperationRelationalExpression;

    @Before
    public void setup() {
        coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[2]) {

            @Override
            protected boolean evaluateCompare(int compare) {
                if (compare < 0) {
                    return false;
                } else if (compare > 0) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public int getPrecedence() {
                return 0;
            }

            @Override
            public boolean isSymmetric() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }
        };
    }

    @Test
    public void testCompute_LessThanEqualTo_True() {
        Object result = coreOperationRelationalExpression.compute(5, 10);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCompute_LessThanEqualTo_False() {
        Object result = coreOperationRelationalExpression.compute(10, 5);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCompute_LessThan_True() {
        Object result = coreOperationRelationalExpression.compute(5, 10);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCompute_LessThan_False() {
        Object result = coreOperationRelationalExpression.compute(10, 10);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCompute_GreaterThanEqualTo_True() {
        Object result = coreOperationRelationalExpression.compute(10, 5);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCompute_GreaterThanEqualTo_False() {
        Object result = coreOperationRelationalExpression.compute(5, 10);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCompute_GreaterThan_True() {
        Object result = coreOperationRelationalExpression.compute(10, 5);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCompute_GreaterThan_False() {
        Object result = coreOperationRelationalExpression.compute(10, 10);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCompute_BothNaN_False() {
        Object result = coreOperationRelationalExpression.compute(Double.NaN, Double.NaN);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCompute_LeftNaN_False() {
        Object result = coreOperationRelationalExpression.compute(Double.NaN, 10);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCompute_RightNaN_False() {
        Object result = coreOperationRelationalExpression.compute(10, Double.NaN);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCompute_BothEqual_True() {
        Object result = coreOperationRelationalExpression.compute(10, 10);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCompute_LeftListRightList_Match() {
        Object result = coreOperationRelationalExpression.compute(Arrays.asList(1, 2, 3).iterator(), Arrays.asList(2, 3, 4).iterator());
        assertTrue((Boolean) result);
    }

    @Test
    public void testCompute_LeftListRightList_NoMatch() {
        Object result = coreOperationRelationalExpression.compute(Arrays.asList(1, 2, 3).iterator(), Arrays.asList(4, 5, 6).iterator());
        assertFalse((Boolean) result);
    }

    @Test
    public void testCompute_RightListLeftList_Match() {
        Object result = coreOperationRelationalExpression.compute(Arrays.asList(2, 3, 4).iterator(), Arrays.asList(1, 2, 3).iterator());
        assertTrue((Boolean) result);
    }

    @Test
    public void testCompute_LeftListRightObject_Match() {
        Object result = coreOperationRelationalExpression.compute(Arrays.asList(1, 2, 3).iterator(), 2);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCompute_LeftListRightObject_NoMatch() {
        Object result = coreOperationRelationalExpression.compute(Arrays.asList(1, 2, 3).iterator(), 4);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCompute_RightListLeftObject_Match() {
        Object result = coreOperationRelationalExpression.compute(2, Arrays.asList(1, 2, 3).iterator());
        assertTrue((Boolean) result);
    }

    private boolean containsMatch(Iterator it, Object value) {
        while (it.hasNext()) {
            Object element = it.next();
            if (coreOperationRelationalExpression.compute(element, value)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testContainsMatch_NoMatch() {
        assertFalse(containsMatch(Arrays.asList(1, 2, 3).iterator(), 4));
    }

    @Test
    public void testContainsMatch_Match() {
        assertTrue(containsMatch(Arrays.asList(1, 2, 3).iterator(), 2));
    }

    private boolean findMatch(Iterator lit, Iterator rit) {
        HashSet<Object> left = new HashSet<>();
        while (lit.hasNext()) {
            left.add(lit.next());
        }
        while (rit.hasNext()) {
            if (containsMatch(left.iterator(), rit.next())) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testFindMatch_NoMatch() {
        assertFalse(findMatch(Arrays.asList(1, 2, 3).iterator(), Arrays.asList(4, 5, 6).iterator()));
    }

    @Test
    public void testFindMatch_Match() {
        assertTrue(findMatch(Arrays.asList(1, 2, 3).iterator(), Arrays.asList(2, 3, 4).iterator()));
    }
}
