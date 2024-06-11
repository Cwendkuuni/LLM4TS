package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link CoreOperationEqual} class.
 */
public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual_whenBothArgsAreBoolean_thenReturnExpectedValue() {
        Expression arg1 = new CoreConstantExpression(Boolean.TRUE);
        Expression arg2 = new CoreConstantExpression(Boolean.TRUE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Assert.assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsBooleanAndSecondArgIsNumber_thenThrowException() {
        Expression arg1 = new CoreConstantExpression(Boolean.TRUE);
        Expression arg2 = new CoreConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        try {
            coreOperationEqual.computeValue(new EvalContext());
            Assert.fail("Expected an exception");
        } catch (ClassCastException e) {
            // Expected exception
        }
    }

    @Test
    public void testCoreOperationEqual_whenBothArgsAreNumbers_thenReturnExpectedValue() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Assert.assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_whenBothArgsAreDifferentNumbers_thenReturnExpectedValue() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreConstantExpression(2);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Assert.assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsStringAndSecondArgIsBoolean_thenThrowException() {
        Expression arg1 = new CoreConstantExpression("test");
        Expression arg2 = new CoreConstantExpression(Boolean.TRUE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        try {
            coreOperationEqual.computeValue(new EvalContext());
            Assert.fail("Expected an exception");
        } catch (ClassCastException e) {
            // Expected exception
        }
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsNumberAndSecondArgIsString_thenThrowException() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreConstantExpression("test");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        try {
            coreOperationEqual.computeValue(new EvalContext());
            Assert.fail("Expected an exception");
        } catch (ClassCastException e) {
            // Expected exception
        }
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsStringAndSecondArgIsString_thenReturnExpectedValue() {
        Expression arg1 = new CoreConstantExpression("test1");
        Expression arg2 = new CoreConstantExpression("test1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Assert.assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsStringAndSecondArgIsDifferentString_thenReturnExpectedValue() {
        Expression arg1 = new CoreConstantExpression("test1");
        Expression arg2 = new CoreConstantExpression("test2");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Assert.assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_whenBothArgsAreNull_thenReturnExpectedValue() {
        Expression arg1 = new CoreConstantExpression(null);
        Expression arg2 = new CoreConstantExpression(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Assert.assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsNotNullAndSecondArgIsNull_thenReturnExpectedValue() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreConstantExpression(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Assert.assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsListAndSecondArgIsList_thenReturnExpectedValue() {
        Expression arg1 = new CoreListExpression();
        Expression arg2 = new CoreListExpression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object value1 = new Integer[1];
        Object value2 = new Integer[1];
        ((CoreListExpression) arg1).setListValue(value1);
        ((CoreListExpression) arg2).setListValue(value2);
        Assert.assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsListAndSecondArgIsNotList_thenThrowException() {
        Expression arg1 = new CoreListExpression();
        Expression arg2 = new CoreConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        try {
            coreOperationEqual.computeValue(new EvalContext());
            Assert.fail("Expected an exception");
        } catch (ClassCastException e) {
            // Expected exception
        }
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsMapAndSecondArgIsMap_thenReturnExpectedValue() {
        Expression arg1 = new CoreMapExpression();
        Expression arg2 = new CoreMapExpression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object mapValue1 = new Object() {
            @Override
            public boolean equals(Object obj) {
                return true;
            }
        };
        Object mapValue2 = new Object() {
            @Override
            public boolean equals(Object obj) {
                return true;
            }
        };
        ((CoreMapExpression) arg1).setMapValue(mapValue1);
        ((CoreMapExpression) arg2).setMapValue(mapValue2);
        Assert.assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsMapAndSecondArgIsNotMap_thenThrowException() {
        Expression arg1 = new CoreMapExpression();
        Expression arg2 = new CoreConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        try {
            coreOperationEqual.computeValue(new EvalContext());
            Assert.fail("Expected an exception");
        } catch (ClassCastException e) {
            // Expected exception
        }
    }
}