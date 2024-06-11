package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual.CoreOperationEqualArgs;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqualWithBooleanArgs() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationConstant(Boolean.TRUE),
                new CoreOperationConstant(Boolean.TRUE));
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));

        coreOperationEqual = new CoreOperationEqual(
                new CoreOperationConstant(Boolean.TRUE),
                new CoreOperationConstant(Boolean.FALSE));
        assertFalse(coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqualWithNumberArgs() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationConstant(1.0),
                new CoreOperationConstant(1.0));
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));

        coreOperationEqual = new CoreOperationEqual(
                new CoreOperationConstant(1.0),
                new CoreOperationConstant(2.0));
        assertFalse(coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqualWithStringArgs() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationConstant("test"),
                new CoreOperationConstant("test"));
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));

        coreOperationEqual = new CoreOperationEqual(
                new CoreOperationConstant("test"),
                new CoreOperationConstant("otherTest"));
        assertFalse(coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqualWithInvalidArgument() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationConstant(1),
                new CoreOperationVariable("undefined"));
        coreOperationEqual.computeValue(new EvalContext());
    }

    @Test
    public void testCoreOperationEqualWithNullArguments() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationConstant(null),
                new CoreOperationConstant(null));
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));

        CoreOperationEqual coreOperationEqual1 = new CoreOperationEqual(
                new CoreOperationConstant(null),
                new CoreOperationConstant(""));
        assertFalse(coreOperationEqual1.computeValue(new EvalContext()));

        CoreOperationEqual coreOperationEqual2 = new CoreOperationEqual(
                new CoreOperationConstant(""),
                new CoreOperationConstant(null));
        assertFalse(coreOperationEqual2.computeValue(new EvalContext()));

        CoreOperationEqual coreOperationEqual3 = new CoreOperationEqual(
                new CoreOperationConstant(null),
                new CoreOperationConstant(new Object()));
        assertNull(coreOperationEqual3.computeValue(new EvalContext()));
    }
}