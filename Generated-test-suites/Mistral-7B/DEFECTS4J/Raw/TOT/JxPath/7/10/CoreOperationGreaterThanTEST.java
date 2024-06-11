package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.expr.Expression;
import org.apache.commons.jxpath.ri.util.ExpressionUtil;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for CoreOperationGreaterThan class.
 */
public class CoreOperationGreaterThanTest {

    private CoreOperationGreaterThan expression;
    private EvalContext context;
    private NodeInfo arg1NodeInfo;
    private NodeInfo arg2NodeInfo;

    @Before
    public void setUp() {
        Expression arg1 = ExpressionUtil.doubleValueNode(1.5);
        Expression arg2 = ExpressionUtil.doubleValueNode(1.0);
        expression = new CoreOperationGreaterThan(arg1, arg2);

        context = new EvalContext();
        context.setCurrentInfoSet(new InfoSet() {
            @Override
            public NodeInfo getNodeInfo(int index) {
                switch (index) {
                    case 0:
                        return arg1NodeInfo;
                    case 1:
                        return arg2NodeInfo;
                    default:
                        throw new IndexOutOfBoundsException();
                }
            }

            @Override
            public int getSize() {
                return 2;
            }
        });

        arg1NodeInfo = new NodeInfo();
        arg2NodeInfo = new NodeInfo();
    }

    @Test
    public void testGreaterThan() {
        double l = 2.5;
        double r = 1.5;

        context.setCurrentNode(new Document().createElement("root"));
        arg1NodeInfo.setValue(l);
        arg2NodeInfo.setValue(r);

        assertTrue(expression.computeValue(context).booleanValue());
    }

    @Test
    public void testLessThanOrEqual() {
        double l = 2.5;
        double r = 2.5;

        context.setCurrentNode(new Document().createElement("root"));
        arg1NodeInfo.setValue(l);
        arg2NodeInfo.setValue(r);

        assertFalse(expression.computeValue(context).booleanValue());
    }

    @Test
    public void testLessThan() {
        double l = 1.5;
        double r = 2.5;

        context.setCurrentNode(new Document().createElement("root"));
        arg1NodeInfo.setValue(l);
        arg2NodeInfo.setValue(r);

        assertFalse(expression.computeValue(context).booleanValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMismatchedTypes() {
        Expression arg1 = ExpressionUtil.stringValueNode("1.5");
        Expression arg2 = ExpressionUtil.doubleValueNode(1.5);

        context.setCurrentNode(new Document().createElement("root"));
        arg1NodeInfo.setValue(arg1);
        arg2NodeInfo.setValue(arg2);

        new CoreOperationGreaterThan(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArg1() {
        Expression arg1 = null;
        Expression arg2 = ExpressionUtil.doubleValueNode(1.5);

        context.setCurrentNode(new Document().createElement("root"));
        arg1NodeInfo.setValue(arg1);
        arg2NodeInfo.setValue(arg2);

        new CoreOperationGreaterThan(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArg2() {
        Expression arg1 = ExpressionUtil.doubleValueNode(1.5);
        Expression arg2 = null;

        context.setCurrentNode(new Document().createElement("root"));
        arg1NodeInfo.setValue(arg1);
        arg2NodeInfo.setValue(arg2);

        new CoreOperationGreaterThan(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullContext() {
        Expression arg1 = ExpressionUtil.doubleValueNode(1.5);
        Expression arg2 = ExpressionUtil.doubleValueNode(1.5);

        new CoreOperationGreaterThan(arg1, arg2);

        context = null;
        expression.computeValue(context);
    }
}