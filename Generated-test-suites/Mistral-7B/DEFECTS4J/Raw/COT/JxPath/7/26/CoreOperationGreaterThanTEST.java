package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.apache.commons.jxpath.ri.expression.Literal;
import org.apache.commons.jxpath.ri.expression.VariableRef;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testConstructor() {
        Expression arg1 = new VariableRef("arg1");
        Expression arg2 = new VariableRef("arg2");
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertNotNull(coreOperationGreaterThan);
        assertSame(arg1, coreOperationGreaterThan.getLeftArg());
        assertSame(arg2, coreOperationGreaterThan.getRightArg());
    }

    @Test
    public void testComputeValue() {
        Expression arg1 = new Literal(5.0);
        Expression arg2 = new Literal(3.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());

        arg1 = new Literal(3.0);
        arg2 = new Literal(5.0);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new VariableRef("arg1"), new VariableRef("arg2"));
        assertEquals(">", coreOperationGreaterThan.getSymbol());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueWithNullContext() {
        Expression arg1 = new VariableRef("arg1");
        Expression arg2 = new VariableRef("arg2");
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        coreOperationGreaterThan.computeValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueWithNullArg1() {
        Expression arg1 = null;
        Expression arg2 = new VariableRef("arg2");
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        coreOperationGreaterThan.computeValue(new EvalContext(new InfoSet()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueWithNullArg2() {
        Expression arg1 = new VariableRef("arg1");
        Expression arg2 = null;
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        coreOperationGreaterThan.computeValue(new EvalContext(new InfoSet()));
    }

    @Test(expected = ClassCastException.class)
    public void testComputeValueWithNonNumericArg1() {
        Expression arg1 = new VariableRef("arg1");
        Expression arg2 = new Literal(3.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        coreOperationGreaterThan.computeValue(new EvalContext(new InfoSet()));
    }

    @Test(expected = ClassCastException.class)
    public void testComputeValueWithNonNumericArg2() {
        Expression arg1 = new Literal(5.0);
        Expression arg2 = new VariableRef("arg2");
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        coreOperationGreaterThan.computeValue(new EvalContext(new InfoSet()));
    }

    @Test
    public void testComputeValueWithZeroAndNegativeNumbers() {
        Expression arg1 = new Literal(-3.0);
        Expression arg2 = new Literal(5.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        arg1 = new Literal(5.0);
        arg2 = new Literal(-3.0);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }
}