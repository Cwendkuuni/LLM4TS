package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.functions.FunctionCallExpression;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqualWithNullArguments() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(null, null);
        assertNull(coreOperationNotEqual.getSymbol());
        assertNull(coreOperationNotEqual.computeValue(null));
    }

    @Test
    public void testCoreOperationNotEqualWithNullArgument1() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(null, new FunctionCallExpression("1 + 1"));
        assertNull(coreOperationNotEqual.getSymbol());
        assertNull(coreOperationNotEqual.computeValue(null));
    }

    @Test
    public void testCoreOperationNotEqualWithNullArgument2() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new FunctionCallExpression("1 + 1"), null);
        assertNull(coreOperationNotEqual.getSymbol());
        assertNull(coreOperationNotEqual.computeValue(null));
    }

    @Test
    public void testCoreOperationNotEqualWithBothNullValues() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(null, null);
        assertNull(coreOperationNotEqual.computeValue(null));
    }

    @Test
    public void testCoreOperationNotEqualWithBooleanFalseAndBooleanTrue() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(Boolean.FALSE), new ConstantExpression(Boolean.TRUE));
        assertTrue(coreOperationNotEqual.getSymbol().equals("!="));
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualWithBooleanTrueAndBooleanFalse() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(Boolean.TRUE), new ConstantExpression(Boolean.FALSE));
        assertTrue(coreOperationNotEqual.getSymbol().equals("!="));
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualWithBooleanTrueAndBooleanTrue() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(Boolean.TRUE), new ConstantExpression(Boolean.TRUE));
        assertTrue(coreOperationNotEqual.getSymbol().equals("!="));
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualWithBooleanFalseAndBooleanFalse() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(Boolean.FALSE), new ConstantExpression(Boolean.FALSE));
        assertTrue(coreOperationNotEqual.getSymbol().equals("!="));
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualWithNumber0AndNumber0() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(0), new ConstantExpression(0));
        assertTrue(coreOperationNotEqual.getSymbol().equals("!="));
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualWithNumber0AndNumber1() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(0), new ConstantExpression(1));
        assertTrue(coreOperationNotEqual.getSymbol().equals("!="));
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualWithNumber1AndNumber0() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(0));
        assertTrue(coreOperationNotEqual.getSymbol().equals("!="));
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualWithNumber1AndNumber1() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(1));
        assertTrue(coreOperationNotEqual.getSymbol().equals("!="));
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualWithStringEmptyAndStringEmpty() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(""), new ConstantExpression(""));
        assertTrue(coreOperationNotEqual.getSymbol().equals("!="));
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualWithStringEmptyAndStringOne() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(""), new ConstantExpression("One"));
        assertTrue(coreOperationNotEqual.getSymbol().equals("!="));
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualWithStringOneAndStringEmpty() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression("One"), new ConstantExpression(""));
        assertTrue(coreOperationNotEqual.getSymbol().equals("!="));
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualWithStringOneAndStringOne() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression("One"), new ConstantExpression("One"));
        assertTrue(coreOperationNotEqual.getSymbol().equals("!="));
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualWithObjectsOfDifferentClasses() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(new Object()), new ConstantExpression(new String("One")));
        assertTrue(coreOperationNotEqual.getSymbol().equals("!="));
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualWithObjectsOfSameClass() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(new Object()), new ConstantExpression(new Object()));
        assertTrue(coreOperationNotEqual.getSymbol().equals("!="));
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }
}