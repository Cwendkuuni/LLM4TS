package org.apache.commons.jxpath.ri.compiler;
import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    private CoreOperationRelationalExpression expression;

    @Before
    public void setup() {
        expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        boolean symmetric = expression.isSymmetric();
        assertEquals(false, symmetric);
    }

    @Test
    public void testCompute() {
        Object left = new Object();
        Object right = new Object();
        boolean result = expression.compute(left, right);
        assertEquals(false, result);
    }

    @Test
    public void testReduce() {
        Object o = new Object();
        Object result = expression.reduce(o);
        assertEquals(o, result);
    }

    @Test
    public void testContainsMatch() {
        Iterator it = new ArrayList().iterator();
        Object value = new Object();
        boolean result = expression.containsMatch(it, value);
        assertEquals(false, result);
    }

    @Test
    public void testFindMatch() {
        Iterator lit = new ArrayList().iterator();
        Iterator rit = new ArrayList().iterator();
        boolean result = expression.findMatch(lit, rit);
        assertEquals(false, result);
    }
}