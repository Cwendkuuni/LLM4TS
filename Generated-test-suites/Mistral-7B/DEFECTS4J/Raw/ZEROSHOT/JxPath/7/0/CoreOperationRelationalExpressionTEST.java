package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.IntegerValue;
import org.apache.commons.jxpath.ri.compiler.VariableRef;
import org.apache.commons.jxpath.ri.core.Context;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        CoreOperationRelationalExpression gt = new CoreOperationGT(new VariableRef("x"), new VariableRef("y"));
        Expression args[] = {new VariableRef("x"), new VariableRef("y")};
        gt.setArgs(args);

        IntegerValue x = new IntegerValue(5);
        IntegerValue y = new IntegerValue(3);

        Context context = new Context();
        context.setValues("x", x);
        context.setValues("y", y);

        JXPathContext jxpathContext = new JXPathContext(context);
        boolean result = gt.evaluateToBoolean(jxpathContext);

        assertTrue(result);
    }

    @Test
    public void testGreaterThanWithFalseResult() {
        CoreOperationRelationalExpression gt = new CoreOperationGT(new VariableRef("x"), new VariableRef("y"));
        Expression args[] = {new VariableRef("x"), new VariableRef("y")};
        gt.setArgs(args);

        IntegerValue x = new IntegerValue(3);
        IntegerValue y = new IntegerValue(5);

        Context context = new Context();
        context.setValues("x", x);
        context.setValues("y", y);

        JXPathContext jxpathContext = new JXPathContext(context);
        boolean result = gt.evaluateToBoolean(jxpathContext);

        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGreaterThanWithInvalidArgs() {
        CoreOperationRelationalExpression gt = new CoreOperationGT(new VariableRef("x"), null);
        Expression args[] = {new VariableRef("x")};
        gt.setArgs(args);
    }

    @Test
    public void testGreaterThanOrEqual() {
        CoreOperationRelationalExpression gte = new CoreOperationGTE(new VariableRef("x"), new VariableRef("y"));
        Expression args[] = {new VariableRef("x"), new VariableRef("y")};
        gte.setArgs(args);

        IntegerValue x = new IntegerValue(5);
        IntegerValue y = new IntegerValue(3);

        Context context = new Context();
        context.setValues("x", x);
        context.setValues("y", y);

        JXPathContext jxpathContext = new JXPathContext(context);
        boolean result = gte.evaluateToBoolean(jxpathContext);

        assertTrue(result);
    }

    @Test
    public void testGreaterThanOrEqualWithFalseResult() {
        CoreOperationRelationalExpression gte = new CoreOperationGTE(new VariableRef("x"), new VariableRef("y"));
        Expression args[] = {new VariableRef("x"), new VariableRef("y")};
        gte.setArgs(args);

        IntegerValue x = new IntegerValue(3);
        IntegerValue y = new IntegerValue(5);

        Context context = new Context();
        context.setValues("x", x);
        context.setValues("y", y);

        JXPathContext jxpathContext = new JXPathContext(context);
        boolean result = gte.evaluateToBoolean(jxpathContext);

        assertFalse(result);
    }

    @Test
    public void testLessThan() {
        CoreOperationRelationalExpression lt = new CoreOperationLT(new VariableRef("x"), new VariableRef("y"));
        Expression args[] = {new VariableRef("x"), new VariableRef("y")};
        lt.setArgs(args);

        IntegerValue x = new IntegerValue(3);
        IntegerValue y = new IntegerValue(5);

        Context context = new Context();
        context.setValues("x", x);
        context.setValues("y", y);

        JXPathContext jxpathContext = new JXPathContext(context);
        boolean result = lt.evaluateToBoolean(jxpathContext);

        assertFalse(result);
    }

    @Test
    public void testLessThanWithTrueResult() {
        CoreOperationRelationalExpression lt = new CoreOperationLT(new VariableRef("x"), new VariableRef("y"));
        Expression args[] = {new VariableRef("x"), new VariableRef("y")};
        lt.setArgs(args);

        IntegerValue x = new IntegerValue(3);
        IntegerValue y = new IntegerValue(4);

        Context context = new Context();
        context.setValues("x", x);
        context.setValues("y", y);

        JXPathContext jxpathContext = new JXPathContext(context);
        boolean result = lt.evaluateToBoolean(jxpathContext);

        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLessThanWithInvalidArgs() {
        CoreOperationRelationalExpression lt = new CoreOperationLT(new VariableRef("x"), null);
        Expression args[] = {new VariableRef("x")};
        lt.setArgs(args);
    }

    @Test
    public void testLessThanOrEqual() {
        CoreOperationRelationalExpression lte = new CoreOperationLTE(new VariableRef("x"), new VariableRef("y"));
        Expression args[] = {new VariableRef("x"), new VariableRef("y")};
        lte.setArgs(args);

        IntegerValue x = new IntegerValue(3);
        IntegerValue y = new IntegerValue(5);

        Context context = new Context();
        context.setValues("x", x);
        context.setValues("y", y);

        JXPathContext jxpathContext = new JXPathContext(context);
        boolean result = lte.evaluateToBoolean(jxpathContext);

        assertFalse(result);
    }

    @Test
    public void testLessThanOrEqualWithTrueResult() {
        CoreOperationRelationalExpression lte = new CoreOperationLTE(new VariableRef("x"), new VariableRef("y"));
        Expression args[] = {new VariableRef("x"), new VariableRef("y")};
        lte.setArgs(args);

        IntegerValue x = new IntegerValue(3);
        IntegerValue y = new IntegerValue(3);

        Context context = new Context();
        context.setValues("x", x);
        context.setValues("y", y);

        JXPathContext jxpathContext = new JXPathContext(context);
        boolean result = lte.evaluateToBoolean(jxpathContext);

        assertTrue(result);
    }

    @Test
    public void testPreferences() {
        CoreOperationRelationalExpression lte = new CoreOperationLTE(new VariableRef("x"), new VariableRef("y"));
        CoreOperationRelationalExpression gte = new CoreOperationGTE(new VariableRef("x"), new VariableRef("y"));
        CoreOperationRelationalExpression gt = new CoreOperationGT(new VariableRef("x"), new VariableRef("y"));

        Expression[] argsLTE = {new VariableRef("x"), new VariableRef("y")};
        Expression[] argsGTE = {new VariableRef("x"), new VariableRef("y")};
        Expression[] argsGT = {new VariableRef("x"), new VariableRef("y")};

        lte.setArgs(argsLTE);
        gte.setArgs(argsGTE);
        gt.setArgs(argsGT);

        int precedenceLTE = lte.getPrecedence();
        int precedenceGTE = gte.getPrecedence();
        int precedenceGT = gt.getPrecedence();

        assertEquals(3, precedenceLTE);
        assertEquals(3, precedenceGTE);
        assertEquals(3, precedenceGT);

        assertFalse(lte.isSymmetric());
        assertFalse(gte.isSymmetric());
        assertFalse(gt.isSymmetric());
    }

}