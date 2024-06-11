package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.expressions.Constant;
import org.apache.commons.jxpath.ri.expressions.Variable;

import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_false() {
        CoreOperationNotEqual expression = new CoreOperationNotEqual(new Constant(1), new Constant(2));
        EvalContext context = new EvalContext();
        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValue_true() {
        CoreOperationNotEqual expression = new CoreOperationNotEqual(new Constant(1), new Constant(1));
        EvalContext context = new EvalContext();
        assertTrue(expression.computeValue(context).equals(Boolean.FALSE));
    }

    @Test
    public void testComputeValue_null() {
        CoreOperationNotEqual expression = new CoreOperationNotEqual(new Variable("x"), new Constant(null));
        EvalContext context = new EvalContext();
        assertNull(context.setVariable("x", "1"));
        expression.setVariables(new String[]{"x"});
        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValue_nullAndNull() {
        CoreOperationNotEqual expression = new CoreOperationNotEqual(new Constant(null), new Constant(null));
        EvalContext context = new EvalContext();
        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual expression = new CoreOperationNotEqual(new Variable("x"), new Variable("y"));
        String symbol = expression.getSymbol();
        assertTrue(symbol.equals("!="));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullArg() {
        new CoreOperationNotEqual(null, new Constant(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullArg2() {
        new CoreOperationNotEqual(new Constant(1), null);
    }

    @Test
    public void testToString() {
        CoreOperationNotEqual expression = new CoreOperationNotEqual(new Variable("x"), new Variable("y"));
        String expected = String.format("!=%s != %s", "x", "y");
        String result = expression.toString();
        assertTrue(result.equals(expected));
    }
}