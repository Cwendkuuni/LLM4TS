package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationEquals;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationNotEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Test class for CoreOperationRelationalExpression
 */
public class CoreOperationRelationalExpressionTest {

    @org.junit.Test
    public void testCoreOperationRelationalExpressionConstructor() {
        Expression[] args1 = { new CoreOperationLiteralExpression(1), new CoreOperationLiteralExpression(2) };
        CoreOperationGreaterThan op1 = new CoreOperationGreaterThan(args1);
        assertNotNull(op1);

        Expression[] args2 = { new CoreOperationLiteralExpression(1), new CoreOperationLiteralExpression(1) };
        CoreOperationGreaterThanOrEqual op2 = new CoreOperationGreaterThanOrEqual(args2);
        assertNotNull(op2);

        Expression[] args3 = { new CoreOperationLiteralExpression(1), new CoreOperationLiteralExpression(2) };
        CoreOperationLessThan op3 = new CoreOperationLessThan(args3);
        assertNotNull(op3);

        Expression[] args4 = { new CoreOperationLiteralExpression(1), new CoreOperationLiteralExpression(1) };
        CoreOperationLessThanOrEqual op4 = new CoreOperationLessThanOrEqual(args4);
        assertNotNull(op4);

        Expression[] args5 = { new CoreOperationLiteralExpression(1), new CoreOperationLiteralExpression("1") };
        CoreOperationEquals op5 = new CoreOperationEquals(args5);
        assertNotNull(op5);

        Expression[] args6 = { new CoreOperationLiteralExpression(1), new CoreOperationLiteralExpression("1") };
        CoreOperationNotEquals op6 = new CoreOperationNotEquals(args6);
        assertNotNull(op6);
    }

    @org.junit.Test
    public void testCoreOperationRelationalExpressionComputeValue() {
        CoreOperationGreaterThan opGreaterThan = new CoreOperationGreaterThan(new CoreOperationLiteralExpression(1), new CoreOperationLiteralExpression(2));
        EvalContext context = new EvalContext();
        Object result = opGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        CoreOperationGreaterThanOrEqual opGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new CoreOperationLiteralExpression(1), new CoreOperationLiteralExpression(1));
        Result result1 = opGreaterThanOrEqual.computeValue(context);
        assertTrue(result1 instanceof Boolean);
        assertTrue((Boolean) result1);

        CoreOperationLessThan opLessThan = new CoreOperationLessThan(new CoreOperationLiteralExpression(1), new CoreOperationLiteralExpression(2));
        Result result2 = opLessThan.computeValue(context);
        assertTrue(result2 instanceof Boolean);
        assertFalse((Boolean) result2);

        CoreOperationLessThanOrEqual opLessThanOrEqual = new CoreOperationLessThanOrEqual(new CoreOperationLiteralExpression(1), new CoreOperationLiteralExpression(1));
        Result result3 = opLessThanOrEqual.computeValue(context);
        assertTrue(result3 instanceof Boolean);
        assertTrue((Boolean) result3);

        CoreOperationEquals opEquals = new CoreOperationEquals(new CoreOperationLiteralExpression(1), new CoreOperationLiteralExpression(1));
        Result result4 = opEquals.computeValue(context);
        assertTrue(result4 instanceof Boolean);
        assertTrue((Boolean) result4);

        CoreOperationNotEquals opNotEquals = new CoreOperationNotEquals(new CoreOperationLiteralExpression(1), new CoreOperationLiteralExpression(1));
        Result result5 = opNotEquals.computeValue(context);
        assertTrue(result5 instanceof Boolean);
        assertFalse((Boolean) result5);
    }

    @org.junit.Test
    public void testCoreOperationRelationalExpressionGetPrecedence() {
        CoreOperationGreaterThan opGreaterThan = new CoreOperationGreaterThan(new CoreOperationLiteralExpression(1), new CoreOperationLiteralExpression(2));
        int precedence = opGreaterThan.getPrecedence();
        assertEquals(CoreOperationRelationalExpression.RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    @org.junit.Test
    public void testCoreOperationRelationalExpressionIsSymmetric() {
        CoreOperationGreaterThan opGreaterThan = new CoreOperationGreaterThan(new CoreOperationLiteralExpression(1), new CoreOperationLiteralExpression(2));
        boolean symmetric = opGreaterThan.isSymmetric();
        assertFalse(symmetric);
    }

    @org.junit.Test
    public void testCoreOperationRelationalExpressionEvaluateCompare() {
        CoreOperationGreaterThan opGreaterThan = new CoreOperationGreaterThan(new CoreOperationLiteralExpression(1), new CoreOperationLiteralExpression(2));
        assertTrue(opGreaterThan.evaluateCompare(0));
        assertTrue(opGreaterThan.evaluateCompare(-1));
        assertFalse(opGreaterThan.evaluateCompare(1));
    }

    @org.junit.Test
    public void testCoreOperationRelationalExpressionCompute() {
        CoreOperationGreaterThan opGreaterThan = new CoreOperationGreaterThan(new CoreOperationLiteralExpression(1), new CoreOperationLiteralExpression(2));
        Object left = new Integer(1);
        Object right = new Integer(2);
        Object result = opGreaterThan.compute(left, right);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        CoreOperationGreaterThanOrEqual opGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new CoreOperationLiteralExpression(1), new CoreOperationLiteralExpression(1));
        Object left1 = new Integer(1);
        Object right1 = new Integer(1);
        Object result1 = opGreaterThanOrEqual.compute(left1, right1);
        assertTrue(result1 instanceof Boolean);
        assertTrue((Boolean) result1);

        CoreOperationLessThan opLessThan = new CoreOperationLessThan(new CoreOperationLiteralExpression(1), new CoreOperationLiteralExpression(2));
        Object left2 = new Integer(1);
        Object right2 = new Integer(2);
        Object result2 = opLessThan.compute(left2, right2);
        assertTrue(result2 instanceof Boolean);
        assertFalse((Boolean) result2);

        CoreOperationLessThanOrEqual opLessThanOrEqual = new CoreOperationLessThanOrEqual(new CoreOperationLiteralExpression(1), new CoreOperationLiteralExpression(1));
        Object left3 = new Integer(1);
        Object right3 = new Integer(1);
        Object result3 = opLessThanOrEqual.compute(left3, right3);
        assertTrue(result3 instanceof Boolean);
        assertTrue((Boolean) result3);

        CoreOperationEquals opEquals = new CoreOperationEquals(new CoreOperationLiteralExpression(1), new CoreOperationLiteralExpression(1));
        Object left4 = new Integer(1);
        Object right4 = new Integer(1);
        Object result4 = opEquals.compute(left4, right4);
        assertTrue(result4 instanceof Boolean);
        assertTrue((Boolean) result4);

        CoreOperationNotEquals opNotEquals = new CoreOperationNotEquals(new CoreOperationLiteralExpression(1), new CoreOperationLiteralExpression(1));
        Object left5 = new Integer(1);
        Object right5 = new Integer(1);
        Object result5 = opNotEquals.compute(left5, right5);
        assertTrue(result5 instanceof Boolean);
        assertFalse((Boolean) result5);
    }

    @org.junit.Test
    public void testCoreOperationRelationalExpressionReduce() {
        Object left = new CoreOperationLiteralExpression(1);
        Object result = new CoreOperationRelationalExpression().reduce(left);
        assertEquals(left, result);

        Object left1 = new InitialContext();
        Object result1 = new CoreOperationRelationalExpression().reduce(left1);
        assertNotNull(result1);

        Object left2 = new ArrayList<>();
        Object result2 = new CoreOperationRelationalExpression().reduce(left2);
        assertNotNull(result2);
        assertTrue(result2 instanceof Iterator);
    }

    @org.junit.Test
    public void testCoreOperationRelationalExpressionContainsMatch() {
        Iterator it = new ArrayList<>().iterator();
        Object value = new Integer(1);
        Object result = new CoreOperationRelationalExpression().containsMatch(it, value);
        assertFalse(result);

        it = new ArrayList<>().iterator();
        it.next();
        it.next();
        it.add(new Integer(1));
        result = new CoreOperationRelationalExpression().containsMatch(it, value);
        assertTrue(result);
    }

    @org.junit.Test
    public void testCoreOperationRelationalExpressionFindMatch() {
        Iterator lit = new ArrayList<>().iterator();
        Iterator rit = new ArrayList<>().iterator();
        HashSet left = new HashSet();
        Object value = new Integer(1);
        Object result = new CoreOperationRelationalExpression().findMatch(lit, rit);
        assertFalse(result);

        lit.add(new Integer(1));
        lit.add(new Integer(2));
        result = new CoreOperationRelationalExpression().findMatch(lit, rit);
        assertFalse(result);

        rit.add(new Integer(1));
        result = new CoreOperationRelationalExpression().findMatch(lit, rit);
        assertTrue(result);

        lit.add(new Integer(1));
        result = new CoreOperationRelationalExpression().findMatch(lit, rit);
        assertTrue(result);
    }
}