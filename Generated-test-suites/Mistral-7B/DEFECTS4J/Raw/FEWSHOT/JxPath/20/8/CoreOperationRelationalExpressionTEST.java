package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression left = new Const("5");
        Expression right = new Const("10");
        CoreOperationRelationalExpression expr = new GreaterThanExpression(new CoreOperation[] { left, right });
        EvalContext context = new EvalContext(new SelfContext(), Collections.emptyList());
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());

        left = new Const("5");
        right = new Const("5");
        expr = new GreaterThanOrEqualExpression(new CoreOperation[] { left, right });
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());

        left = new Const("5");
        right = new Const("10");
        expr = new LessThanExpression(new CoreOperation[] { left, right });
        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result instanceof Number);

        left = new Const("5");
        right = new Const("5");
        expr = new LessThanOrEqualExpression(new CoreOperation[] { left, right });
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertTrue(expr.evaluateCompare(2));
        assertTrue(expr.evaluateCompare(3));
        assertFalse(expr.evaluateCompare(0));
        assertFalse(expr.evaluateCompare(-1));

        expr = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        assertFalse(expr.evaluateCompare(2));
        assertFalse(expr.evaluateCompare(3));
        assertTrue(expr.evaluateCompare(0));
        assertTrue(expr.evaluateCompare(-1));
    }

    @Test
    public void testCompute() {
        Object left = new Integer(5);
        Object right = new Integer(10);
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreOperation[] {
                new Const(left), new Const(right) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertTrue(expr.compute(left, right));

        left = new Double(5.0);
        right = new Double(10.0);
        expr = new CoreOperationRelationalExpression(new CoreOperation[] {
                new Const(left), new Const(right) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertTrue(expr.compute(left, right));

        left = new SelfContext();
        right = new SelfContext();
        expr = new CoreOperationRelationalExpression(new CoreOperation[] { left, right }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertFalse(expr.compute(left, right));

        left = new InitialContext();
        right = new InitialContext();
        expr = new CoreOperationRelationalExpression(new CoreOperation[] { left, right }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertFalse(expr.compute(left, right));

        List leftList = new LinkedList();
        leftList.add(new Integer(5));
        leftList.add(new Integer(5));
        Iterator leftIterator = leftList.iterator();

        List rightList = new LinkedList();
        rightList.add(new Integer(10));
        rightList.add(new Integer(5));
        Iterator rightIterator = rightList.iterator();

        CoreOperationRelationalExpression listCompareExpr = new CoreOperationRelationalExpression(
                new CoreOperation[] { new Const(leftIterator), new Const(rightIterator) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertFalse(listCompareExpr.compute(leftIterator, rightIterator));

        leftList = new LinkedList();
        leftList.add(new Integer(5));
        leftList.add(new Integer(5));
        leftIterator = leftList.iterator();

        rightList = new LinkedList();
        rightList.add(new Integer(5));
        rightList.add(new Integer(5));
        rightIterator = rightList.iterator();

        CoreOperationRelationalExpression listEqualExpr = new CoreOperationRelationalExpression(
                new CoreOperation[] { new Const(leftIterator), new Const(rightIterator) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        assertTrue(listEqualExpr.compute(leftIterator, rightIterator));

        leftList = new LinkedList();
        leftList.add(new Integer(5));
        leftList.add(new Integer(5));
        leftIterator = leftList.iterator();

        rightList = new LinkedList();
        rightList.add(new Integer(5));
        rightList.add(new Integer(10));
        rightIterator = rightList.iterator();

        CoreOperationRelationalExpression listNotEqualExpr = new CoreOperationRelationalExpression(
                new CoreOperation[] { new Const(leftIterator), new Const(rightIterator) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare != 0;
            }
        };
        assertFalse(listNotEqualExpr.compute(leftIterator, rightIterator));

        leftList = new LinkedList();
        leftList.add(new Integer(5));
        leftList.add(new Integer(5));
        leftIterator = leftList.iterator();

        rightList = new LinkedList();
        rightList.add(new Integer(5));
        rightList.add(new Integer(5));
        rightIterator = rightList.iterator();

        CoreOperationRelationalExpression listIntersectExpr = new CoreOperationRelationalExpression(
                new CoreOperation[] { new Const(leftIterator), new Const(rightIterator) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 1;
            }
        };
        assertTrue(listIntersectExpr.compute(leftIterator, rightIterator));

        leftList = new LinkedList();
        leftList.add(new Integer(5));
        leftList.add(new Integer(5));
        leftIterator = leftList.iterator();

        rightList = new LinkedList();
        rightList.add(new Integer(5));
        rightList.add(new Integer(6));
        rightIterator = rightList.iterator();

        CoreOperationRelationalExpression listIntersectExpr2 = new CoreOperationRelationalExpression(
                new CoreOperation[] { new Const(leftIterator), new Const(rightIterator) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 1;
            }
        };
        assertFalse(listIntersectExpr2.compute(leftIterator, rightIterator));

        leftList = new LinkedList();
        leftList.add(new Integer(5));
        leftList.add(new Integer(5));
        leftIterator = leftList.iterator();

        rightList = new LinkedList();
        rightList.add(new Integer(5));
        rightList.add(new Integer(5));
        rightIterator = rightList.iterator();

        CoreOperationRelationalExpression listContainsExpr = new CoreOperationRelationalExpression(
                new CoreOperation[] { new Const(leftIterator), new Const(rightIterator) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };
        assertTrue(listContainsExpr.compute(leftIterator, rightIterator));

        leftList = new LinkedList();
        leftList.add(new Integer(5));
        leftList.add(new Integer(5));
        leftIterator = leftList.iterator();

        rightList = new LinkedList();
        rightList.add(new Integer(5));
        rightList.add(new Integer(6));
        rightIterator = rightList.iterator();

        CoreOperationRelationalExpression listContainsExpr2 = new CoreOperationRelationalExpression(
                new CoreOperation[] { new Const(leftIterator), new Const(rightIterator) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };
        assertTrue(listContainsExpr2.compute(leftIterator, rightIterator));

        leftList = new LinkedList();
        leftList.add(new Integer(5));
        leftList.add(new Integer(5));
        leftIterator = leftList.iterator();

        rightList = new LinkedList();
        rightList.add(new Integer(6));
        rightList.add(new Integer(5));
        rightIterator = rightList.iterator();

        CoreOperationRelationalExpression listContainsExpr3 = new CoreOperationRelationalExpression(
                new CoreOperation[] { new Const(leftIterator), new Const(rightIterator) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };
        assertFalse(listContainsExpr3.compute(leftIterator, rightIterator));
    }

}