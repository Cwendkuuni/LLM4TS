package org.apache.commons.jxpath.ri.compiler.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.info.InfoSetFactory;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;

public class CoreOperationGreaterThanTest extends TestCase {

    // Test methods for CoreOperationGreaterThan class

    public void testCoreOperationGreaterThan_Double_Double() {
        Expression arg1 = new CoreConstantExpression(new Double(5.0));
        Expression arg2 = new CoreConstantExpression(new Double(3.0));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        InfoSet infoSet = InfoSetFactory.createRootInfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    public void testCoreOperationGreaterThan_Float_Double() {
        Expression arg1 = new CoreConstantExpression(new Float(5.0f));
        Expression arg2 = new CoreConstantExpression(new Double(3.0));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        InfoSet infoSet = InfoSetFactory.createRootInfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    public void testCoreOperationGreaterThan_Double_Float() {
        Expression arg1 = new CoreConstantExpression(new Double(5.0));
        Expression arg2 = new CoreConstantExpression(new Float(3.0f));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        InfoSet infoSet = InfoSetFactory.createRootInfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    public void testCoreOperationGreaterThan_Double_Double_False() {
        Expression arg1 = new CoreConstantExpression(new Double(3.0));
        Expression arg2 = new CoreConstantExpression(new Double(5.0));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        InfoSet infoSet = InfoSetFactory.createRootInfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    public void testCoreOperationGreaterThan_Float_Float_False() {
        Expression arg1 = new CoreConstantExpression(new Float(3.0f));
        Expression arg2 = new CoreConstantExpression(new Float(5.0f));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        InfoSet infoSet = InfoSetFactory.createRootInfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    public void testCoreOperationGreaterThan_NullArg1() {
        Expression arg1 = new CoreNullExpression();
        Expression arg2 = new CoreConstantExpression(new Double(5.0));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        InfoSet infoSet = InfoSetFactory.createRootInfoSet();
        EvalContext context = new EvalContext(infoSet);
        try {
            coreOperationGreaterThan.computeValue(context);
            fail("Expected IllegalArgumentException when arg1 is null");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    public void testCoreOperationGreaterThan_NullArg2() {
        Expression arg1 = new CoreConstantExpression(new Double(5.0));
        Expression arg2 = new CoreNullExpression();
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        InfoSet infoSet = InfoSetFactory.createRootInfoSet();
        EvalContext context = new EvalContext(infoSet);
        try {
            coreOperationGreaterThan.computeValue(context);
            fail("Expected IllegalArgumentException when arg2 is null");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    public void testCoreOperationGreaterThan_MixedTypes() {
        Expression arg1 = new CoreConstantExpression(new Double(5.0));
        Expression arg2 = new CoreConstantExpression(new Integer(3));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        InfoSet infoSet = InfoSetFactory.createRootInfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    public void testCoreOperationGreaterThan_NaN() {
        Expression arg1 = new CoreConstantExpression(Double.NaN);
        Expression arg2 = new CoreConstantExpression(new Double(3.0));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        InfoSet infoSet = InfoSetFactory.createRootInfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }
}