import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;

    @Before
    public void setUp() {
        Expression[] args = new Expression[2];
        expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
    }

    @Test
    public void testComputeValue_sameObject_returnsTrue() {
        EvalContext context = createContext();
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_differentObjects_returnsFalse() {
        EvalContext context = createContext();
        context.setArg(new Object(), 1); // different object
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetPrecedence_returnsRelationalPrecedence() {
        int precedence = expression.getPrecedence();
        assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    @Test
    public void testIsSymmetric_returnsFalse() {
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testCompute_twoIterators_returnsTrue() {
        Iterator left = Arrays.asList(1, 2, 3).iterator();
        Iterator right = Arrays.asList(4, 5, 6).iterator();
        boolean result = expression.compute(left, right);
        assertTrue(result);
    }

    @Test
    public void testCompute_leftIteratorContainsValue_returnsTrue() {
        Iterator left = Arrays.asList(1, 2, 3).iterator();
        boolean result = expression.compute(left, 2);
        assertTrue(result);
    }

    @Test
    public void testCompute_rightIteratorContainsValue_returnsTrue() {
        Iterator right = Arrays.asList(4, 5, 6).iterator();
        boolean result = expression.compute(5, right);
        assertTrue(result);
    }

    @Test
    public void testCompute_bothIteratorsEmpty_returnsFalse() {
        Iterator left = Arrays.asList().iterator();
        Iterator right = Arrays.asList().iterator();
        boolean result = expression.compute(left, right);
        assertFalse(result);
    }

    @Test
    public void testCompute_bothIteratorsDifferentValues_returnsFalse() {
        Iterator left = Arrays.asList(1, 2, 3).iterator();
        Iterator right = Arrays.asList(4, 5, 6).iterator();
        boolean result = expression.compute(left, right);
        assertFalse(result);
    }

    @Test
    public void testCompute_leftValueNaN_returnsFalse() {
        boolean result = expression.compute(Double.NaN, 5);
        assertFalse(result);
    }

    @Test
    public void testCompute_rightValueNaN_returnsFalse() {
        boolean result = expression.compute(5, Double.NaN);
        assertFalse(result);
    }

    @Test
    public void testCompute_leftValueLessThanRight_returnsTrue() {
        boolean result = expression.compute(2, 5);
        assertTrue(result);
    }

    @Test
    public void testCompute_leftValueEqualToRight_returnsFalse() {
        boolean result = expression.compute(5, 5);
        assertFalse(result);
    }

    @Test
    public void testCompute_leftValueGreaterThanRight_returnsFalse() {
        boolean result = expression.compute(5, 2);
        assertFalse(result);
    }

    @Test
    public void testReduce_selfContext_returnsSingleNodePointer() {
        EvalContext context = createContext();
        context.setSingleNodePointer(new Object());
        Object result = expression.reduce(new SelfContext(context));
        assertTrue(result instanceof Object);
    }

    @Test
    public void testReduce_collection_returnsIterator() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Object result = expression.reduce(list);
        assertTrue(result instanceof Iterator);
    }

    private EvalContext createContext() {
        return new EvalContext(null);
    }

    @Test
    public void testContainsMatch_iteratorContainsValue_returnsTrue() {
        Iterator it = Arrays.asList(1, 2, 3).iterator();
        boolean result = expression.containsMatch(it, 2);
        assertTrue(result);
    }

    @Test
    public void testContainsMatch_iteratorDoesNotContainValue_returnsFalse() {
        Iterator it = Arrays.asList(1, 2, 3).iterator();
        boolean result = expression.containsMatch(it, 4);
        assertFalse(result);
    }

    @Test
    public void testFindMatch_bothIteratorsEmpty_returnsFalse() {
        Iterator lit = Arrays.asList().iterator();
        Iterator rit = Arrays.asList().iterator();
        boolean result = expression.findMatch(lit, rit);
        assertFalse(result);
    }

    @Test
    public void testFindMatch_intersectionExists_returnsTrue() {
        Iterator lit = Arrays.asList(1, 2, 3).iterator();
        Iterator rit = Arrays.asList(3, 4, 5).iterator();
        boolean result = expression.findMatch(lit, rit);
        assertTrue(result);
    }

    @Test
    public void testFindMatch_noIntersection_returnsFalse() {
        Iterator lit = Arrays.asList(1, 2, 3).iterator();
        Iterator rit = Arrays.asList(4, 5, 6).iterator();
        boolean result = expression.findMatch(lit, rit);
        assertFalse(result);
    }

}