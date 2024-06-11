package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri. literals.BooleanLiteral;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        Expression arg1 = new NumberLiteral(1);
        Expression arg2 = new NumberLiteral(2);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertFalse("Expected false", result instanceof Boolean);
        assertNull("Expected null", result);

        arg1 = new StringLiteral("foo");
        arg2 = new StringLiteral("bar");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);
        assertFalse("Expected false", result instanceof Boolean);
        assertNull("Expected null", result);

        arg1 = new BooleanLiteral(true);
        arg2 = new BooleanLiteral(false);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);
        assertTrue("Expected true", result instanceof Boolean);
        assertTrue("Expected true", ((Boolean) result).booleanValue());

        arg1 = new NumberLiteral(1);
        arg2 = new NumberLiteral(1);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);
        assertFalse("Expected false", result instanceof Boolean);
        assertNull("Expected null", result);

        arg1 = new StringLiteral("");
        arg2 = new StringLiteral("");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);
        assertFalse("Expected false", result instanceof Boolean);
        assertNull("Expected null", result);

        arg1 = new NumberLiteral(1);
        arg2 = new NumberLiteral(null);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);
        assertTrue("Expected true", result instanceof Boolean);
        assertTrue("Expected true", ((Boolean) result).booleanValue());

        arg1 = null;
        arg2 = new NumberLiteral(1);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);
        assertTrue("Expected true", result instanceof Boolean);
        assertTrue("Expected true", ((Boolean) result).booleanValue());
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new NumberLiteral(1), new NumberLiteral(2));
        String symbol = coreOperationNotEqual.getSymbol();
        assertEquals("!=", symbol);
    }
}