package org.apache.commons.jxpath.ri.compiler.test;

import junit.framework.Assert;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Expression;

/**
 * Test class for CoreOperationNotEqual.
 */
public class CoreOperationNotEqualTest {

    // Test case for constructor
    @org.junit.Test
    public void testCoreOperationNotEqualConstructor() {
        Expression arg1 = new CoreOperationAdd();
        Expression arg2 = new CoreOperationAdd();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Assert.assertNotNull(coreOperationNotEqual);
    }

    // Test case for computeValue method with boolean values
    @org.junit.Test
    public void testComputeValueWithBooleanValues() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new CoreBooleanOperation(Boolean.TRUE), new CoreBooleanOperation(Boolean.TRUE));
        Assert.assertEquals(Boolean.FALSE, coreOperationNotEqual.computeValue(new EvalContext()));

        coreOperationNotEqual = new CoreOperationNotEqual(new CoreBooleanOperation(Boolean.TRUE), new CoreBooleanOperation(Boolean.FALSE));
        Assert.assertEquals(Boolean.TRUE, coreOperationNotEqual.computeValue(new EvalContext()));
    }

    // Test case for computeValue method with number values
    @org.junit.Test
    public void testComputeValueWithNumberValues() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new CoreNumericOperation(5), new CoreNumericOperation(5));
        Assert.assertEquals(Boolean.FALSE, coreOperationNotEqual.computeValue(new EvalContext()));

        coreOperationNotEqual = new CoreOperationNotEqual(new CoreNumericOperation(5), new CoreNumericOperation(6));
        Assert.assertEquals(Boolean.TRUE, coreOperationNotEqual.computeValue(new EvalContext()));
    }

    // Test case for computeValue method with string values
    @org.junit.Test
    public void testComputeValueWithStringValues() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new CoreStringOperation("hello"), new CoreStringOperation("hello"));
        Assert.assertEquals(Boolean.FALSE, coreOperationNotEqual.computeValue(new EvalContext()));

        coreOperationNotEqual = new CoreOperationNotEqual(new CoreStringOperation("hello"), new CoreStringOperation("world"));
        Assert.assertEquals(Boolean.TRUE, coreOperationNotEqual.computeValue(new EvalContext()));
    }

    // Test case for computeValue method with different types
    @org.junit.Test
    public void testComputeValueWithDifferentTypes() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new CoreNumericOperation(5), new CoreStringOperation("5"));
        Assert.assertEquals(Boolean.TRUE, coreOperationNotEqual.computeValue(new EvalContext()));
    }

    // Test case for getSymbol method
    @org.junit.Test
    public void testGetSymbol() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new CoreOperationAdd(), new CoreOperationAdd());
        Assert.assertEquals("!=", coreOperationNotEqual.getSymbol());
    }

    // Test case for exception when arg1 is null
    @org.junit.Test(expected = NullPointerException.class)
    public void testExceptionWhenArg1IsNull() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(null, new CoreBooleanOperation(Boolean.TRUE));
    }

    // Test case for exception when arg2 is null
    @org.junit.Test(expected = NullPointerException.class)
    public void testExceptionWhenArg2IsNull() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new CoreBooleanOperation(Boolean.TRUE), null);
    }
}