package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual_whenBothArgsAreEqual_thenReturnsTrue() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsGreaterThanSecondArg_thenReturnsFalse() {
        Expression arg1 = new CoreConstantExpression(2);
        Expression arg2 = new CoreConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsLessThanSecondArg_thenReturnsFalse() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreConstantExpression(2);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_whenBothArgsAreNotInstanceOfExpression_thenThrowsIllegalArgumentException() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Integer(1), new String("test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_whenOneArgIsNull_thenThrowsIllegalArgumentException() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreNullExpression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsInstanceOfCoreNumberExpression_andSecondArgIsInstanceOfCoreNumberExpression_thenCallsEqualMethodOfEvalContextCorrectly() {
        Expression arg1 = new CoreNumberExpression(1);
        Expression arg2 = new CoreNumberExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue(context.equals(arg1.getValue(), arg2.getValue()));
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(context));
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsInstanceOfCoreStringExpression_andSecondArgIsInstanceOfCoreStringExpression_thenCallsEqualsMethodOfObjectCorrectly() {
        Expression arg1 = new CoreStringExpression("test");
        Expression arg2 = new CoreStringExpression("test");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(true, arg1.getValue().equals(arg2.getValue()));
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsInstanceOfCoreBooleanExpression_andSecondArgIsInstanceOfCoreBooleanExpression_thenCallsEqualsMethodOfBooleanCorrectly() {
        Expression arg1 = new CoreBooleanExpression(true);
        Expression arg2 = new CoreBooleanExpression(true);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(true, arg1.getValue().equals(arg2.getValue()));
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsInstanceOfCoreNullExpression_andSecondArgIsInstanceOfCoreNullExpression_thenReturnsTrue() {
        Expression arg1 = new CoreNullExpression();
        Expression arg2 = new CoreNullExpression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsInstanceOfCoreArrayExpression_andSecondArgIsInstanceOfCoreArrayExpression_thenReturnsFalse() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new CoreArrayExpression(new Object[]{}), new CoreArrayExpression(new Object[]{}));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(coreOperationCompare.args[0], coreOperationCompare.args[1]);
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsInstanceOfCoreMapExpression_andSecondArgIsInstanceOfCoreMapExpression_thenCallsEqualsMethodOfMapEntryCorrectly() {
        CoreMapExpression map1 = new CoreMapExpression(new EvalContext(), new Object() {}, new Object[]{"key", "value"});
        CoreMapExpression map2 = new CoreMapExpression(new EvalContext(), new Object() {}, new Object[]{"key", "value"});
        Expression arg1 = map1.getEntryAt(0);
        Expression arg2 = map2.getEntryAt(0);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Map.Entry<Object, Object> entry1 = (Map.Entry<Object, Object>) arg1.getValue();
        Map.Entry<Object, Object> entry2 = (Map.Entry<Object, Object>) arg2.getValue();
        assertEquals(entry1.getKey(), entry2.getKey());
        assertEquals(entry1.getValue(), entry2.getValue());
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_whenFirstArgIsInstanceOfCoreRegularPathExpression_andSecondArgIsInstanceOfCoreRegularPathExpression_thenCallsEqualsMethodOfPathSegmentCorrectly() {
        CoreRegularPathExpression path1 = new CoreRegularPathExpression("/test/test1", new EvalContext());
        CoreRegularPathExpression path2 = new CoreRegularPathExpression("/test/test1", new EvalContext());
        Expression arg1 = path1.getPathSegments()[0];
        Expression arg2 = path2.getPathSegments()[0];
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(arg1.getValue(), arg2.getValue());
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test(expected = IllegalStateException.class)
    public void testCoreOperationEqual_whenFirstArgIsInstanceOfCoreVariableExpression_andSecondArgIsInstanceOfCoreVariableExpression_thenThrowsIllegalStateException() {
        CoreVariableExpression variable1 = new CoreVariableExpression("v1");
        CoreVariableExpression variable2 = new CoreVariableExpression("v2");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(variable1, variable2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_whenFirstArgIsInstanceOfCoreFunctionCallExpression_andSecondArgIsNotInstanceOfExpression_thenThrowsIllegalArgumentException() {
        CoreFunctionCallExpression functionCallExpression = new CoreFunctionCallExpression("function", new CoreArrayExpression(new Object[]{new CoreIntegerExpression(1)}));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(functionCallExpression, new Integer(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_whenFirstArgIsNotInstanceOfExpression_andSecondArgIsInstanceOfCoreFunctionCallExpression_thenThrowsIllegalArgumentException() {
        Integer integer = new Integer(1);
        CoreFunctionCallExpression functionCallExpression = new CoreFunctionCallExpression("function", new CoreArrayExpression(new Object[]{integer}));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(integer, functionCallExpression);
    }
}