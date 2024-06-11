package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Object left = new Integer(5);
        Object right = new Integer(10);
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new CoreExpression[]{new CoreNumberExpression(left), new CoreNumberExpression(right)});
        EvalContext context = new EvalContext(new SelfContext());
        Boolean result = (Boolean) expression.computeValue(context);
        assertEquals(false, result);

        left = new Integer(10);
        right = new Integer(5);
        expression = new MyCoreOperationRelationalExpression(new CoreExpression[]{new CoreNumberExpression(left), new CoreNumberExpression(right)});
        context = new EvalContext(new SelfContext());
        result = (Boolean) expression.computeValue(context);
        assertEquals(false, result);

        left = new Integer(10);
        right = new Integer(10);
        expression = new MyCoreOperationRelationalExpression(new CoreExpression[]{new CoreNumberExpression(left), new CoreNumberExpression(right)});
        context = new EvalContext(new SelfContext());
        result = (Boolean) expression.computeValue(context);
        assertEquals(true, result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new CoreExpression[]{new CoreNumberExpression(1), new CoreNumberExpression(2)});
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new CoreExpression[]{new CoreNumberExpression(1), new CoreNumberExpression(2)});
        boolean symmetric = expression.isSymmetric();
        assertEquals(false, symmetric);
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new CoreExpression[]{new CoreNumberExpression(1), new CoreNumberExpression(2)});
        boolean result = expression.evaluateCompare(0);
        assertFalse(result);

        result = expression.evaluateCompare(-1);
        assertTrue(result);

        result = expression.evaluateCompare(1);
        assertFalse(result);
    }

    @Test
    public void testComputeWithInitialContext() {
        Object left = new Integer(5);
        Object right = new Integer(10);
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new CoreExpression[]{new CoreNumberExpression(left), new CoreNumberExpression(right)});

        EvalContext context = new EvalContext(new InitialContext());
        Boolean result = (Boolean) expression.computeValue(context);
        assertEquals(false, result);

        context = new EvalContext(new InitialContext());
        left = new Integer(10);
        right = new Integer(5);
        expression = new MyCoreOperationRelationalExpression(new CoreExpression[]{new CoreNumberExpression(left), new CoreNumberExpression(right)});
        result = (Boolean) expression.computeValue(context);
        assertEquals(false, result);

        context = new EvalContext(new InitialContext());
        left = new Integer(10);
        right = new Integer(10);
        expression = new MyCoreOperationRelationalExpression(new CoreExpression[]{new CoreNumberExpression(left), new CoreNumberExpression(right)});
        result = (Boolean) expression.computeValue(context);
        assertEquals(true, result);
    }

    @Test
    public void testComputeWithIterators() {
        Collection left = new ArrayList<>();
        left.add(new Integer(5));
        left.add(new Integer(10));
        Collection right = new ArrayList<>();
        right.add(new Integer(1));
        right.add(new Integer(10));

        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new CoreExpression[]{new CoreListExpression(left), new CoreListExpression(right)});

        Iterator itLeft = left.iterator();
        Iterator itRight = right.iterator();
        EvalContext context = new EvalContext(new SelfContext());
        Boolean result = (Boolean) expression.computeValue(context);
        assertEquals(false, result);

        left.clear();
        left.add(new Integer(10));
        left.add(new Integer(5));
        right.clear();
        right.add(new Integer(10));
        right.add(new Integer(10));

        itLeft = left.iterator();
        itRight = right.iterator();
        context = new EvalContext(new SelfContext());
        result = (Boolean) expression.computeValue(context);
        assertEquals(true, result);
    }

    @Test
    public void testComputeWithDoubleValues() {
        Object left = 5.5;
        Object right = 10.5;
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new CoreExpression[]{new CoreNumberExpression(left), new CoreNumberExpression(right)});

        EvalContext context = new EvalContext(new SelfContext());
        Boolean result = (Boolean) expression.computeValue(context);
        assertEquals(false, result);

        left = 10.5;
        right = 5.5;
        expression = new MyCoreOperationRelationalExpression(new CoreExpression[]{new CoreNumberExpression(left), new CoreNumberExpression(right)});
        context = new EvalContext(new SelfContext());
        result = (Boolean) expression.computeValue(context);
        assertEquals(false, result);

        left = 10.5;
        right = 10.5;
        expression = new MyCoreOperationRelationalExpression(new CoreExpression[]{new CoreNumberExpression(left), new CoreNumberExpression(right)});
        context = new EvalContext(new SelfContext());
        result = (Boolean) expression.computeValue(context);
        assertEquals(true, result);
    }

    private static class MyCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
        MyCoreOperationRelationalExpression(CoreExpression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return compare > 0;
        }
    }

}