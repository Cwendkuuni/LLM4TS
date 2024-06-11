import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;
    private CoreOperationRelationalExpression[] expressions;

    @Before
    public void setUp() {
        expressions = new CoreOperationRelationalExpression[4];
        expressions[0] = new GreaterThanExpression();
        expressions[1] = new GreaterThanOrEqualExpression();
        expressions[2] = new LessThanExpression();
        expressions[3] = new LessThanOrEqualExpression();

        expression = new GreaterThanExpression();
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        context.getSingleNodePointer();

        Object result = expression.computeValue(context);

        assertNotNull(result);
        assertTrue(result instanceof Boolean);
    }

    @Test
    public void testGetPrecedence() {
        int precedence = expression.getPrecedence();

        assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    @Test
    public void testIsSymmetric() {
        boolean symmetric = expression.isSymmetric();

        assertFalse(symmetric);
    }

    @Test
    public void testEvaluateCompare() {
        boolean evaluate = expression.evaluateCompare(0);

        assertFalse(evaluate);
    }

    @Test
    public void testCompute() {
        Object left = 5;
        Object right = 10;

        boolean compute = expression.compute(left, right);

        assertFalse(compute);
    }

    @Test
    public void testReduce() {
        Object o = new ArrayList<>();

        Object reduce = expression.reduce(o);

        assertNotNull(reduce);
    }

    @Test
    public void testContainsMatch() {
        Iterator it = Arrays.asList(1, 2, 3, 4, 5).iterator();
        Object value = 3;

        boolean containsMatch = expression.containsMatch(it, value);

        assertTrue(containsMatch);
    }

    @Test
    public void testFindMatch() {
        Iterator lit = Arrays.asList(1, 2, 3, 4, 5).iterator();
        Iterator rit = Arrays.asList(6, 7, 8, 9, 10).iterator();

        boolean findMatch = expression.findMatch(lit, rit);

        assertFalse(findMatch);
    }
}