package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvaluationContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testCoreOperationCompare() {
        Expression arg1 = new ConstantExpression(new Integer(1));
        Expression arg2 = new ConstantExpression(new Integer(1));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        EvaluationContext context = new EvaluationContext(InitialContext.INSTANCE, InitialContext.INSTANCE, SelfContext.INSTANCE);
        assertTrue(coreOperationCompare.evaluate(context).booleanValue());

        arg2 = new ConstantExpression(new Integer(2));
        assertFalse(coreOperationCompare.evaluate(context).booleanValue());

        arg1 = new ConstantExpression("test1");
        arg2 = new ConstantExpression("test1");
        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertTrue(coreOperationCompare.evaluate(context).booleanValue());

        arg2 = new ConstantExpression("test2");
        assertFalse(coreOperationCompare.evaluate(context).booleanValue());

        arg1 = new ConstantExpression(new Integer(1));
        arg2 = new ConstantExpression(new Integer[0]);
        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertFalse(coreOperationCompare.evaluate(context).booleanValue());

        arg1 = new ConstantExpression(new Integer[0]);
        arg2 = new ConstantExpression(new Integer(1));
        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertFalse(coreOperationCompare.evaluate(context).booleanValue());

        arg1 = new ConstantExpression(new Integer(1));
        arg2 = new ConstantExpression(new Integer(1));
        arg1 = new ArrayIndexExpression(arg1, new ConstantExpression(0));
        arg2 = new ArrayIndexExpression(arg2, new ConstantExpression(0));
        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        EvaluationContext context1 = new EvaluationContext(InitialContext.INSTANCE, InitialContext.INSTANCE, SelfContext.INSTANCE);
        Pointer pointer1 = new Pointer(1);
        Pointer pointer2 = new Pointer(1);
        context1.setSingleNodePointer(pointer1);
        assertTrue(coreOperationCompare.evaluate(context1).booleanValue());

        arg1 = new ConstantExpression(new Integer(2));
        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        EvaluationContext context2 = new EvaluationContext(InitialContext.INSTANCE, InitialContext.INSTANCE, SelfContext.INSTANCE);
        context2.setSingleNodePointer(pointer2);
        assertFalse(coreOperationCompare.evaluate(context2).booleanValue());

        arg1 = new ConstantExpression(new Integer(1));
        arg2 = new ConstantExpression(new Integer(1));
        arg1 = new ArrayIndexExpression(arg1, new ConstantExpression(1));
        arg2 = new ArrayIndexExpression(arg2, new ConstantExpression(1));
        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        EvaluationContext context3 = new EvaluationContext(InitialContext.INSTANCE, InitialContext.INSTANCE, SelfContext.INSTANCE);
        Pointer pointer3 = new Pointer(2);
        context3.setSingleNodePointer(pointer3);
        assertFalse(coreOperationCompare.evaluate(context3).booleanValue());

        arg1 = new ConstantExpression(new Integer(1));
        arg2 = new ConstantExpression(new Integer(1));
        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        EvaluationContext context4 = new EvaluationContext(InitialContext.INSTANCE, InitialContext.INSTANCE, SelfContext.INSTANCE);
        Pointer pointer4 = new Pointer(1);
        context4.setSingleNodePointer(pointer4);
        assertTrue(coreOperationCompare.evaluate(context4).booleanValue());

        arg1 = new ConstantExpression(new Integer(1));
        arg2 = new ConstantExpression(new Integer(2));
        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        EvaluationContext context5 = new EvaluationContext(InitialContext.INSTANCE, InitialContext.INSTANCE, SelfContext.INSTANCE);
        Pointer pointer5 = new Pointer(1);
        context5.setSingleNodePointer(pointer5);
        assertFalse(coreOperationCompare.evaluate(context5).booleanValue());
    }

    @Test
    public void testContains() {
        Integer[] intArray = {1, 2, 3, 4, 5};
        Iterator<Integer> it = new java.util.Arrays.Iterator(intArray);
        Expression arg1 = new ConstantExpression(new Integer(3));
        assertTrue(CoreOperationCompare.contains(it, arg1));
        assertFalse(CoreOperationCompare.contains(it, new ConstantExpression(new Integer(6))));
    }

    @Test
    public void testFindMatch() {
        Integer[] intArray1 = {1, 2, 3, 4, 5};
        Integer[] intArray2 = {2, 3, 4, 5, 6, 7, 8};
        Iterator<Integer> lit = new java.util.Arrays.Iterator(intArray1);
        Iterator<Integer> rit = new java.util.Arrays.iterator(intArray2);
        assertTrue(CoreOperationCompare.findMatch(lit, rit));
        assertFalse(CoreOperationCompare.findMatch(lit, new java.util.Arrays.Iterator(new Integer[]{1, 9})));
    }

    @Test
    public void testEqual() {
        assertFalse(CoreOperationCompare.equal(null, null));
        Integer num1 = new Integer(1);
        Integer num2 = new Integer(1);
        assertTrue(CoreOperationCompare.equal(num1, num2));
        Integer num3 = new Integer(2);
        assertFalse(CoreOperationCompare.equal(num1, num3));
        String str1 = "test";
        String str2 = "test";
        assertTrue(CoreOperationCompare.equal(str1, str2));
        String str3 = "test1";
        assertFalse(CoreOperationCompare.equal(str1, str3));
        Boolean bool1 = new Boolean(true);
        Boolean bool2 = new Boolean(true);
        assertTrue(CoreOperationCompare.equal(bool1, bool2));
        Boolean bool3 = new Boolean(false);
        assertFalse(CoreOperationCompare.equal(bool1, bool3));
        Pointer pointer1 = new Pointer(1);
        Pointer pointer2 = new Pointer(1);
        assertTrue(CoreOperationCompare.equal(pointer1, pointer2));
        Pointer pointer3 = new Pointer(2);
        assertFalse(CoreOperationCompare.equal(pointer1, pointer3));
    }
}