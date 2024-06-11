package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.TestSupport;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreExpression;
import org.apache.commons.jxpath.ri.compiler.CoreArgument;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        CoreExpression arg1 = new CoreLiteral(2.5);
        CoreExpression arg2 = new CoreLiteral(3.5);
        CoreOperationRelationalExpression op = new CoreOperationLessThan(arg1, arg2);
        InfoSet is = TestSupport.getTestInfoset();
        EvalContext context = new EvalContext(is);
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanTrue() {
        CoreExpression arg1 = new CoreLiteral(2.5);
        CoreExpression arg2 = new CoreLiteral(2.0);
        CoreOperationRelationalExpression op = new CoreOperationLessThan(arg1, arg2);
        InfoSet is = TestSupport.getTestInfoset();
        EvalContext context = new EvalContext(is);
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationLessThanInvalidTypes() {
        CoreExpression arg1 = new CoreLiteral(2);
        CoreExpression arg2 = new CoreLiteral("three");
        CoreOperationRelationalExpression op = new CoreOperationLessThan(arg1, arg2);
        InfoSet is = TestSupport.getTestInfoset();
        EvalContext context = new EvalContext(is);
        op.computeValue(context);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationLessThanInvalidTypes2() {
        CoreExpression arg1 = new CoreLiteral("two");
        CoreExpression arg2 = new CoreLiteral(3.5);
        CoreOperationRelationalExpression op = new CoreOperationLessThan(arg1, arg2);
        InfoSet is = TestSupport.getTestInfoset();
        EvalContext context = new EvalContext(is);
        op.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanNullArgument() {
        CoreExpression arg1 = new CoreNull();
        CoreExpression arg2 = new CoreLiteral(3.5);
        CoreOperationRelationalExpression op = new CoreOperationLessThan(arg1, arg2);
        InfoSet is = TestSupport.getTestInfoset();
        EvalContext context = new EvalContext(is);
        op.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanNullArgument2() {
        CoreExpression arg1 = new CoreLiteral(3.5);
        CoreExpression arg2 = new CoreNull();
        CoreOperationRelationalExpression op = new CoreOperationLessThan(arg1, arg2);
        InfoSet is = TestSupport.getTestInfoset();
        EvalContext context = new EvalContext(is);
        op.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanInvalidArguments() {
        CoreExpression arg1 = new CoreArgument(new CoreVariable("x"));
        CoreExpression arg2 = new CoreArgument(new CoreVariable("y"));
        CoreOperationRelationalExpression op = new CoreOperationLessThan(arg1, arg2);
        InfoSet is = TestSupport.getTestInfoset();
        EvalContext context = new EvalContext(is);
        op.computeValue(context);
    }

}