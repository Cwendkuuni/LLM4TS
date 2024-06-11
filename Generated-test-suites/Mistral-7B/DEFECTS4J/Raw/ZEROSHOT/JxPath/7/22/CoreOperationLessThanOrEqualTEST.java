package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetFactory;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.NodeInfoConstant;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.Value;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.junit.Test;

/**
 * Test class for CoreOperationLessThanOrEqual class.
 */
public class CoreOperationLessThanOrEqualTest {

    /**
     * Test method for CoreOperationLessThanOrEqual constructor.
     */
    @Test
    public void testCoreOperationLessThanOrEqualConstructor() {
        Expression arg1 = ExpressionFactory.newConstantExpression(new NodeInfoConstant(1.0));
        Expression arg2 = ExpressionFactory.newConstantExpression(new NodeInfoConstant(2.0));
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertNotNull(coreOperationLessThanOrEqual);
        assertEquals(CoreOperationRelationalExpression.class, coreOperationLessThanOrEqual.getClass());
        assertEquals(2, coreOperationLessThanOrEqual.getArity());
    }

    /**
     * Test method for computeValue method with double values.
     */
    @Test
    public void testComputeValueDoubleValues() {
        Expression arg1 = ExpressionFactory.newConstantExpression(new NodeInfoConstant(1.0));
        Expression arg2 = ExpressionFactory.newConstantExpression(new NodeInfoConstant(2.0));
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        InfoSet infoSet = InfoSetFactory.createRootInfoSet();
        context.setInfoSet(infoSet);
        Value result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result.getValue().equals(Boolean.FALSE));

        arg1 = ExpressionFactory.newConstantExpression(new NodeInfoConstant(1.0));
        arg2 = ExpressionFactory.newConstantExpression(new NodeInfoConstant(1.0));
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result.getValue().equals(Boolean.TRUE));
    }

    /**
     * Test method for computeValue method with NodeInfo values.
     */
    @Test
    public void testComputeValueNodeInfoValues() {
        Expression arg1 = ExpressionFactory.newConstantExpression(new NodeInfoConstant(1.0));
        Expression arg2 = ExpressionFactory.newConstantExpression(new NodeInfoConstant(2.0));
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        InfoSet infoSet = InfoSetFactory.createRootInfoSet();
        context.setInfoSet(infoSet);
        Value result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result.getValue().equals(Boolean.FALSE));

        arg1 = ExpressionFactory.newVariableExpression("x");
        arg2 = ExpressionFactory.newConstantExpression(new NodeInfoConstant(2.0));
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        infoSet = InfoSetFactory.createRootInfoSet();
        context.setInfoSet(infoSet);
        context.setVariable("x", new NodeInfoConstant(1.0));
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result.getValue().equals(Boolean.FALSE));

        arg1 = ExpressionFactory.newConstantExpression(new NodeInfoConstant(1.0));
        arg2 = ExpressionFactory.newVariableExpression("x");
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        infoSet = InfoSetFactory.createRootInfoSet();
        context.setInfoSet(infoSet);
        context.setVariable("x", new NodeInfoConstant(2.0));
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result.getValue().equals(Boolean.FALSE));

        arg1 = ExpressionFactory.newVariableExpression("x");
        arg2 = ExpressionFactory.newConstantExpression(new NodeInfoConstant(1.0));
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        infoSet = InfoSetFactory.createRootInfoSet();
        context.setInfoSet(infoSet);
        context.setVariable("x", new NodeInfoConstant(2.0));
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result.getValue().equals(Boolean.FALSE));

        arg1 = ExpressionFactory.newVariableExpression("x");
        arg2 = ExpressionFactory.newVariableExpression("y");
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        infoSet = InfoSetFactory.createRootInfoSet();
        context.setInfoSet(infoSet);
        context.setVariable("x", new NodeInfoConstant(2.0));
        context.setVariable("y", new NodeInfoConstant(1.0));
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result.getValue().equals(Boolean.TRUE));
    }

    /**
     * Test method for getSymbol method.
     */
    @Test
    public void testGetSymbol() {
        Expression arg1 = ExpressionFactory.newConstantExpression(new NodeInfoConstant(1.0));
        Expression arg2 = ExpressionFactory.newConstantExpression(new NodeInfoConstant(2.0));
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertEquals("<=", coreOperationLessThanOrEqual.getSymbol());
    }
}