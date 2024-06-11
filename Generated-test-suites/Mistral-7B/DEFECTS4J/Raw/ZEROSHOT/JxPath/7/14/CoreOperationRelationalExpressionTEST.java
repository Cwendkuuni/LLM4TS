package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.NumberValue;
import org.apache.commons.jxpath.ri.compiler.StringValue;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        Expression left = new NumberValue(5);
        Expression right = new NumberValue(3);
        CoreOperationRelationalExpression greaterThan = new GreaterThanExpression(left, right);
        assertTrue(greaterThan.evaluate(new Context()));
    }

    @Test
    public void testGreaterThanOrEqual() {
        Expression left = new NumberValue(5);
        Expression right = new NumberValue(5);
        CoreOperationRelationalExpression greaterThanOrEqual = new GreaterThanOrEqualExpression(left, right);
        assertTrue(greaterThanOrEqual.evaluate(new Context()));
        Expression left5 = new NumberValue(5);
        Expression right6 = new NumberValue(6);
        assertFalse(greaterThanOrEqual.evaluate(new Context()));
    }

    @Test
    public void testLessThan() {
        Expression left = new NumberValue(3);
        Expression right = new NumberValue(5);
        CoreOperationRelationalExpression lessThan = new LessThanExpression(left, right);
        assertFalse(lessThan.evaluate(new Context()));
    }

    @Test
    public void testLessThanOrEqual() {
        Expression left = new NumberValue(5);
        Expression right = new NumberValue(5);
        CoreOperationRelationalExpression lessThanOrEqual = new LessThanOrEqualExpression(left, right);
        assertTrue(lessThanOrEqual.evaluate(new Context()));
        Expression left5 = new NumberValue(5);
        Expression right4 = new NumberValue(4);
        assertTrue(lessThanOrEqual.evaluate(new Context()));
    }

    @Test
    public void testStringGreaterThan() {
        Expression left = new StringValue("abc");
        Expression right = new StringValue("def");
        CoreOperationRelationalExpression greaterThan = new GreaterThanExpression(left, right);
        assertFalse(greaterThan.evaluate(new Context()));
        Expression leftX = new StringValue("xyz");
        Expression rightDef = new StringValue("def");
        assertFalse(greaterThan.evaluate(new Context()));
        Expression leftDef = new StringValue("def");
        Expression rightAbc = new StringValue("abc");
        assertFalse(greaterThan.evaluate(new Context()));
        Expression leftXyz = new StringValue("xyz");
        Expression rightXyz = new StringValue("xyz");
        assertTrue(greaterThan.evaluate(new Context()));
    }

    @Test
    public void testStringGreaterThanOrEqual() {
        Expression left = new StringValue("abc");
        Expression right = new StringValue("def");
        CoreOperationRelationalExpression greaterThanOrEqual = new GreaterThanOrEqualExpression(left, right);
        assertFalse(greaterThanOrEqual.evaluate(new Context()));
        Expression leftDef = new StringValue("def");
        Expression rightDef = new StringValue("def");
        assertTrue(greaterThanOrEqual.evaluate(new Context()));
        Expression leftXyz = new StringValue("xyz");
        Expression rightXyz = new StringValue("xyz");
        assertTrue(greaterThanOrEqual.evaluate(new Context()));
        Expression leftAbc = new StringValue("abc");
        Expression rightDef = new StringValue("def");
        assertFalse(greaterThanOrEqual.evaluate(new Context()));
    }

    @Test
    public void testStringLessThan() {
        Expression left = new StringValue("abc");
        Expression right = new StringValue("def");
        CoreOperationRelationalExpression lessThan = new LessThanExpression(left, right);
        assertFalse(lessThan.evaluate(new Context()));
    }

    @Test
    public void testStringLessThanOrEqual() {
        Expression left = new StringValue("abc");
        Expression right = new StringValue("def");
        CoreOperationRelationalExpression lessThanOrEqual = new LessThanOrEqualExpression(left, right);
        assertFalse(lessThanOrEqual.evaluate(new Context()));
        Expression leftDef = new StringValue("def");
        Expression rightDef = new StringValue("def");
        assertTrue(lessThanOrEqual.evaluate(new Context()));
        Expression leftXyz = new StringValue("xyz");
        Expression rightAbc = new StringValue("abc");
        assertTrue(lessThanOrEqual.evaluate(new Context()));
    }
}