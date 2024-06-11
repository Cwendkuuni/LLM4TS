package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualTest;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri. compiler.CoreOperationCompare;

/**
 * Test class for CoreOperationEqual.
 *
 * @author John Doe
 */
public class CoreOperationEqualTest {

    @org.junit.Test
    public void testCoreOperationEqual_whenBothArgumentsAreEqual_thenReturnsTrue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(1));
        Boolean result = (Boolean) coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result);
    }

    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgumentIsGreater_thenReturnsFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(2), new ConstantExpression(1));
        Boolean result = (Boolean) coreOperationEqual.computeValue(new EvalContext());
        assertFalse(result);
    }

    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgumentIsLess_thenReturnsFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(2));
        Boolean result = (Boolean) coreOperationEqual.computeValue(new EvalContext());
        assertFalse(result);
    }

    @org.junit.Test
    public void testCoreOperationEqual_whenBothArgumentsAreDifferentInstancesOfSameClass_thenReturnsFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(new Object()), new ConstantExpression(new Object()));
        Boolean result = (Boolean) coreOperationEqual.computeValue(new EvalContext());
        assertFalse(result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_whenOneArgumentIsNull_thenThrowsIllegalArgumentException() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(1), null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_whenBothArgumentsAreNull_thenThrowsIllegalArgumentException() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(null, null);
    }

    @org.junit.Test(expected = ClassCastException.class)
    public void testCoreOperationEqual_whenArgumentsAreDifferentTypes_thenThrowsClassCastException() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression("1"), new ConstantExpression(1));
    }

    @org.junit.Test
    public void testCoreOperationEqual_whenBothArgumentsAreInstancesOfSameClassAndAreEquals_thenReturnsTrue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(new String("test")), new ConstantExpression(new String("test")));
        Boolean result = (Boolean) coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result);
    }

    @org.junit.Test
    public void testCoreOperationEqual_whenBothArgumentsAreInstancesOfSameClassAndAreNotEquals_thenReturnsFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(new String("test1")), new ConstantExpression(new String("test2")));
        Boolean result = (Boolean) coreOperationEqual.computeValue(new EvalContext());
        assertFalse(result);
    }

    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgumentIsInstanceOfCoreOperationCompare_thenThrowsIllegalArgumentException() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(1), new CoreOperationCompare(new ConstantExpression(2), new ConstantExpression(3)));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(1), coreOperationCompare);
        assertFalse(coreOperationEqual.isValidExpression());
    }

    @org.junit.Test
    public void testCoreOperationEqual_whenSecondArgumentIsInstanceOfCoreOperationCompare_thenThrowsIllegalArgumentException() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(1), new ConstantExpression(2));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(coreOperationCompare, new ConstantExpression(1));
        assertFalse(coreOperationEqual.isValidExpression());
    }
}