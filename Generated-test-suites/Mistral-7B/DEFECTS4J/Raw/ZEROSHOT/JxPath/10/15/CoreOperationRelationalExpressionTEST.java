package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.TestBase;

public class CoreOperationRelationalExpressionTest extends TestBase {

    public CoreOperationRelationalExpressionTest() {
        super();
    }

    @org.junit.Test
    public void testComputeValue() {
        Expression left = new LiteralExpression("5");
        Expression right = new LiteralExpression("10");
        CoreOperationRelationalExpression op = new GtExpression(new CoreOperation[] { left, right });
        Object result = op.computeValue(new EvalContext(evalCtx));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        left = new LiteralExpression("10");
        right = new LiteralExpression("5");
        op = new GtExpression(new CoreOperation[] { left, right });
        result = op.computeValue(new EvalContext(evalCtx));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression op = new GtExpression(new CoreOperation[0]);
        assertEquals(3, op.getPrecedence());
    }

    @org.junit.Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression op = new GtExpression(new CoreOperation[0]);
        assertFalse(op.isSymmetric());
    }

    @org.junit.Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression op = new GtExpression(new CoreOperation[0]);
        assertTrue(op.evaluateCompare(1));
        assertFalse(op.evaluateCompare(0));
        assertFalse(op.evaluateCompare(-1));
    }

    @org.junit.Test
    public void testCompute() {
        Object left = new Integer(5);
        Object right = new Integer(10);
        CoreOperationRelationalExpression op = new GtExpression(new CoreOperation[] { new LiteralExpression(left), new LiteralExpression(right) });
        boolean result = op.compute(left, right);
        assertFalse(result);

        left = new Integer(10);
        right = new Integer(5);
        op = new GtExpression(new CoreOperation[] { new LiteralExpression(left), new LiteralExpression(right) });
        result = op.compute(left, right);
        assertTrue(result);

        left = new Double(3.14);
        right = new Double(3.14);
        op = new EqExpression(new CoreOperation[] { new LiteralExpression(left), new LiteralExpression(right) });
        result = op.compute(left, right);
        assertTrue(result);

        left = new Double(3.14);
        right = new Double(3.15);
        op = new NeExpression(new CoreOperation[] { new LiteralExpression(left), new LiteralExpression(right) });
        result = op.compute(left, right);
        assertFalse(result);

        left = new LiteralExpression(new Integer(5));
        right = new LiteralExpression(new ArrayList());
        op = new InExpression(new CoreOperation[] { new LiteralExpression(left), new LiteralExpression(right) });
        result = op.compute(left, right);
        assertFalse(result);

        List list = new ArrayList();
        list.add(new Integer(5));
        right = new LiteralExpression(list);
        op = new InExpression(new CoreOperation[] { new LiteralExpression(left), new LiteralExpression(right) });
        result = op.compute(left, right);
        assertTrue(result);
    }

    @org.junit.Test
    public void testReduce() {
        Object o = new Integer(5);
        Object result = ((CoreOperationRelationalExpression) new GtExpression(new CoreOperation[] { new LiteralExpression(o), new LiteralExpression("5") })).reduce(o);
        assertEquals(o, result);

        o = new ArrayList();
        ((Collection) o).add(new Integer(5));
        result = ((CoreOperationRelationalExpression) new GtExpression(new CoreOperation[] { new LiteralExpression(o), new LiteralExpression("5") })).reduce(o);
        assertNotNull(result);
        assertTrue(result instanceof Iterator);
    }

    @org.junit.Test
    public void testContainsMatch() {
        Object one = new Integer(1);
        Object two = new Integer(2);
        Iterator it = new ArrayList().iterator();
        it.next();
        it.add(one);

        boolean result = ((CoreOperationRelationalExpression) new EqExpression(new CoreOperation[] { it, new LiteralExpression(two) })).containsMatch(it, two);
        assertFalse(result);

        result = ((CoreOperationRelationalExpression) new EqExpression(new CoreOperation[] { it, new LiteralExpression(one) })).containsMatch(it, one);
        assertTrue(result);
    }

    @org.junit.Test
    public void testFindMatch() {
        Object leftOne = new Integer(1);
        Object leftTwo = new Integer(2);
        Object rightOne = new Integer(1);
        Object rightTwo = new Integer(2);
        Iterator leftIt = new ArrayList().iterator();
        leftIt.add(leftOne);
        leftIt.add(leftTwo);
        Iterator rightIt = new ArrayList().iterator();
        rightIt.add(rightOne);
        rightIt.add(rightTwo);

        boolean result = ((CoreOperationRelationalExpression) new InExpression(new CoreOperation[] { leftIt, new LiteralExpression(rightOne) })).findMatch(leftIt, rightIt);
        assertTrue(result);

        result = ((CoreOperationRelationalExpression) new InExpression(new CoreOperation[] { leftIt, new LiteralExpression(rightTwo) })).findMatch(leftIt, rightIt);
        assertFalse(result);
    }

}