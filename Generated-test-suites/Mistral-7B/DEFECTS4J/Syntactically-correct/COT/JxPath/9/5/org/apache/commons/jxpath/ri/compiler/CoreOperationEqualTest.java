package org.apache.commons.jxpath.ri.compiler.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualTest;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreTypes;

public class CoreOperationEqualTest extends TestCase {

    CoreOperationEqual operation;
    CoreOperationCompare arg1, arg2;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        arg1 = new CoreConstantExpression(new Integer(1));
        arg2 = new CoreConstantExpression(new Integer(1));
        operation = new CoreOperationEqual(arg1, arg2);
    }

    @Test
    public void testCoreOperationEqual_equalValues() {
        Object result = operation.computeValue(new EvalContext());
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_inequalValues() {
        arg2 = new CoreConstantExpression(new Integer(2));
        Object result = operation.computeValue(new EvalContext());
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_invalidArgumentTypes() {
        arg1 = new CoreConstantExpression(new String("test"));
        arg2 = new CoreConstantExpression(new Integer(1));
        operation = new CoreOperationEqual(arg1, arg2);
        operation.computeValue(new EvalContext());
    }

    @Test
    public void testCoreOperationEqual_nullArg1() {
        arg1 = new CoreConstantExpression(null);
        Object result = operation.computeValue(new EvalContext());
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_nullArg2() {
        arg2 = new CoreConstantExpression(null);
        Object result = operation.computeValue(new EvalContext());
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_nullValues() {
        arg1 = new CoreConstantExpression(new String("null"));
        arg2 = new CoreConstantExpression(new String("null"));
        Object result = operation.computeValue(new EvalContext());
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_booleanValues() {
        arg1 = new CoreConstantExpression(Boolean.TRUE);
        arg2 = new CoreConstantExpression(Boolean.TRUE);
        Object result = operation.computeValue(new EvalContext());
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_booleanValueAndInteger() {
        arg1 = new CoreConstantExpression(Boolean.TRUE);
        arg2 = new CoreConstantExpression(new Integer(1));
        Object result = operation.computeValue(new EvalContext());
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_integerValues() {
        arg1 = new CoreConstantExpression(new Integer(1));
        arg2 = new CoreConstantExpression(new Integer(1));
        Object result = operation.computeValue(new EvalContext());
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_integerAndString() {
        arg1 = new CoreConstantExpression(new Integer(1));
        arg2 = new CoreConstantExpression(new CoreConstantExpression("1"));
        Object result = operation.computeValue(new EvalContext());
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_stringValues() {
        arg1 = new CoreConstantExpression(new CoreConstantExpression("test"));
        arg2 = new CoreConstantExpression(new CoreConstantExpression("test"));
        Object result = operation.computeValue(new EvalContext());
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_stringValueAndInteger() {
        arg1 = new CoreConstantExpression(new CoreConstantExpression("1"));
        arg2 = new CoreConstantExpression(new Integer(1));
        Object result = operation.computeValue(new EvalContext());
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_stringValueAndBoolean() {
        arg1 = new CoreConstantExpression(new CoreConstantExpression("true"));
        arg2 = new CoreConstantExpression(Boolean.TRUE);
        Object result = operation.computeValue(new EvalContext());
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_booleanValueAndString() {
        arg1 = new CoreConstantExpression(Boolean.TRUE);
        arg2 = new CoreConstantExpression(new CoreConstantExpression("true"));
        Object result = operation.computeValue(new EvalContext());
        Assert.assertEquals(Boolean.TRUE, result);
    }
}