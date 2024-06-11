package org.apache.commons.jxpath.ri.compiler.test;

import junit.framework.Assert;
import org.apache.commons.jxpath.ri.CompilerTest;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;

public class CoreOperationNotEqualTest extends CompilerTest {

    @SuppressWarnings("unchecked")
    public void testCoreOperationNotEqual_Basic() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(2));
        Assert.assertFalse(operation.computeValue(new EvalContext()));

        operation = new CoreOperationNotEqual(new ConstantExpression(2), new ConstantExpression(1));
        Assert.assertTrue(operation.computeValue(new EvalContext()));
    }

    @SuppressWarnings("unchecked")
    public void testCoreOperationNotEqual_EdgeCases() {
        // test null arguments
        CoreOperationNotEqual operation = new CoreOperationNotEqual(null, new ConstantExpression(1));
        Assert.assertFalse(operation.computeValue(new EvalContext()));

        operation = new CoreOperationNotEqual(new ConstantExpression(1), null);
        Assert.assertTrue(operation.computeValue(new EvalContext()));

        // test null values
        operation = new CoreOperationNotEqual(new ConstantExpression(1), new NullExpression());
        Assert.assertTrue(operation.computeValue(new EvalContext()));

        operation = new CoreOperationNotEqual(new NullExpression(), new ConstantExpression(1));
        Assert.assertFalse(operation.computeValue(new EvalContext()));

        // test same value
        operation = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(1));
        Assert.assertFalse(operation.computeValue(new EvalContext()));
    }

    @SuppressWarnings("unchecked")
    public void testCoreOperationNotEqual_ExceptionHandling() {
        try {
            new CoreOperationNotEqual(null, new ConstantExpression(1));
            Assert.fail("Should have thrown an exception");
        } catch (NullPointerException e) {
            // expected
        }

        try {
            new CoreOperationNotEqual(new ConstantExpression(1), null);
            Assert.fail("Should have thrown an exception");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @SuppressWarnings("unchecked")
    public void testCoreOperationNotEqual_Nested() {
        CoreExpression arg1 = new ConstantExpression(new ConstantExpression(1));
        CoreExpression arg2 = new ConstantExpression(new ConstantExpression(2));
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        Assert.assertFalse(operation.computeValue(new EvalContext()));
    }

    @SuppressWarnings("unchecked")
    public void testCoreOperationNotEqual_CompareToSameExpression() {
        CoreExpression arg1 = new VariableExpression("x");
        CoreExpression arg2 = arg1;
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        Assert.assertFalse(operation.computeValue(new EvalContext()));
    }

    @SuppressWarnings("unchecked")
    public void testCoreOperationNotEqual_CompareToDifferentExpressions() {
        CoreExpression arg1 = new ConstantExpression(1);
        CoreExpression arg2 = new ConstantExpression(2);
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        Assert.assertFalse(operation.computeValue(new EvalContext()));
    }

    @SuppressWarnings("unchecked")
    public void testCoreOperationNotEqual_CompareToBoolean() {
        CoreExpression arg1 = new ConstantExpression(Boolean.TRUE);
        CoreExpression arg2 = new ConstantExpression(Boolean.FALSE);
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        Assert.assertTrue(operation.computeValue(new EvalContext()));
    }

    @SuppressWarnings("unchecked")
    public void testCoreOperationNotEqual_CompareToNullBoolean() {
        CoreExpression arg1 = new ConstantExpression(Boolean.TRUE);
        CoreExpression arg2 = new NullExpression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        Assert.assertTrue(operation.computeValue(new EvalContext()));
    }

    @SuppressWarnings("unchecked")
    public void testCoreOperationNotEqual_CompareToNullExpression() {
        CoreExpression arg1 = new NullExpression();
        CoreExpression arg2 = new ConstantExpression(1);
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        Assert.assertFalse(operation.computeValue(new EvalContext()));
    }

    @SuppressWarnings("unchecked")
    public void testCoreOperationNotEqual_CompareToSameBoolean() {
        CoreExpression arg1 = new ConstantExpression(Boolean.TRUE);
        CoreExpression arg2 = arg1;
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        Assert.assertFalse(operation.computeValue(new EvalContext()));
    }

    @SuppressWarnings("unchecked")
    public void testCoreOperationNotEqual_CompareToSameNumber() {
        CoreExpression arg1 = new ConstantExpression(1);
        CoreExpression arg2 = arg1;
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        Assert.assertFalse(operation.computeValue(new EvalContext()));
    }

    @SuppressWarnings("unchecked")
    public void testCoreOperationNotEqual_CompareToDifferentBooleans() {
        CoreExpression arg1 = new ConstantExpression(Boolean.TRUE);
        CoreExpression arg2 = new ConstantExpression(Boolean.FALSE);
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        Assert.assertTrue(operation.computeValue(new EvalContext()));
    }

    @SuppressWarnings("unchecked")
    public void testCoreOperationNotEqual_CompareToDifferentNumbers() {
        CoreExpression arg1 = new ConstantExpression(1);
        CoreExpression arg2 = new ConstantExpression(2);
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        Assert.assertFalse(operation.computeValue(new EvalContext()));
    }

    @SuppressWarnings("unchecked")
    public void testCoreOperationNotEqual_CompareToSameString() {
        CoreExpression arg1 = new ConstantExpression("hello");
        CoreExpression arg2 = arg1;
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        Assert.assertFalse(operation.computeValue(new EvalContext()));
    }

    @SuppressWarnings("unchecked")
    public void testCoreOperationNotEqual_CompareToDifferentStrings() {
        CoreExpression arg1 = new ConstantExpression("hello");
        CoreExpression arg2 = new ConstantExpression("world");
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        Assert.assertTrue(operation.computeValue(new EvalContext()));
    }

    @SuppressWarnings("unchecked")
    public void testCoreOperationNotEqual_CompareToSameObject() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        CoreExpression arg1 = new ConstantExpression(obj1);
        CoreExpression arg2 = arg1;
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        Assert.assertFalse(operation.computeValue(new EvalContext()));
    }

    @SuppressWarnings("unchecked")
    public void testCoreOperationNotEqual_CompareToDifferentObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        CoreExpression arg1 = new ConstantExpression(obj1);
        CoreExpression arg2 = new ConstantExpression(obj2);
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        Assert.assertTrue(operation.computeValue(new EvalContext()));
    }

    @SuppressWarnings("unchecked")
    public void testCoreOperationNotEqual_CompareToCollectionWithSameElements() {
        Object[] elements = new Object[]{1, 2};
        CoreExpression arg1 = new ConstantExpression(new ArrayExpression(elements));
        CoreExpression arg2 = new ConstantExpression(new ArrayExpression(elements));
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        Assert.assertFalse(operation.computeValue(new EvalContext()));
    }

    @SuppressWarnings("unchecked")
    public void testCoreOperationNotEqual_CompareToCollectionWithDifferentElements() {
        Object[] elements1 = new Object[]{1, 2};
        Object[] elements2 = new Object[]{2, 3};
        CoreExpression arg1 = new ConstantExpression(new ArrayExpression(elements1));
        CoreExpression arg2 = new ConstantExpression(new ArrayExpression(elements2));
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        Assert.assertTrue(operation.computeValue(new EvalContext()));
    }

    @SuppressWarnings("unchecked")
    public void testCoreOperationNotEqual_CompareToMapWithSameKeyValue() {
        Object key = new Object();
        Object value = new Object();
        CoreExpression arg1 = new ConstantExpression(new MapExpression(key, value));
        CoreExpression arg2 = new ConstantExpression(new MapExpression(key, value));
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        Assert.assertFalse(operation.computeValue(new EvalContext()));
    }

    @SuppressWarnings("unchecked")
    public void testCoreOperationNotEqual_CompareToMapWithDifferentKeyValue() {
        Object key1 = new Object();
        Object value1 = new Object();
        Object key2 = new Object();
        Object value2 = new Object();
        CoreExpression arg1 = new ConstantExpression(new MapExpression(key1, value1));
        CoreExpression arg2 = new ConstantExpression(new MapExpression(key2, value2));
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        Assert.assertTrue(operation.computeValue(new EvalContext()));
    }
}