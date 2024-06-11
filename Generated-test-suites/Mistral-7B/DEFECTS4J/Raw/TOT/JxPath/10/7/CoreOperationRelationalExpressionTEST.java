import org.apache.commons.jxpath.ri.CompilerContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.IterateContext;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.NodeInfoImpl;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.axes.AxisName;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for CoreOperationRelationalExpression class.
 */
public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;
    private Expression left, right;
    private EvalContext context;

    @Before
    public void setup() {
        expression = new CoreOperationRelationalExpression(new CoreExpressionVariable[] {new CoreExpressionVariable("left"), new CoreExpressionVariable("right")});
        left = new CoreExpressionVariable("left");
        right = new CoreExpressionVariable("right");
        context = new EvalContext();
    }

    @Test
    public void testGreaterThan() {
        Expression leftExpression = new LiteralExpression(5);
        Expression rightExpression = new LiteralExpression(3);
        expression = new GreaterThanExpression(new CoreOperationRelationalExpression[]{leftExpression, rightExpression});
        NodeInfo leftNode = new NodeInfoImpl(new Path("/test"), AxisName.SELF, 0, 5, null);
        NodeInfo rightNode = new NodeInfoImpl(new Path("/test"), AxisName.SELF, 0, 3, null);
        context.setCurrentNode(leftNode);
        context.setVariable("left", leftExpression);
        context.setVariable("right", rightExpression);
        context.setCurrentAxis(AxisName.SELF);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setCurrentNode(rightNode);
        context.setVariable("left", leftExpression);
        context.setVariable("right", rightExpression);
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testGreaterThanOrEqual() {
        Expression leftExpression = new LiteralExpression(5);
        Expression rightExpression = new LiteralExpression(5);
        expression = new GreaterThanOrEqualExpression(new CoreOperationRelationalExpression[]{leftExpression, rightExpression});
        NodeInfo leftNode = new NodeInfoImpl(new Path("/test"), AxisName.SELF, 0, 5, null);
        NodeInfo rightNode = new NodeInfoImpl(new Path("/test"), AxisName.SELF, 0, 5, null);
        context.setCurrentNode(leftNode);
        context.setVariable("left", leftExpression);
        context.setVariable("right", rightExpression);
        context.setCurrentAxis(AxisName.SELF);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setCurrentNode(leftNode);
        context.setVariable("left", leftExpression);
        context.setVariable("right", new LiteralExpression(6));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setCurrentNode(rightNode);
        context.setVariable("left", leftExpression);
        context.setVariable("right", rightExpression);
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setCurrentNode(leftNode);
        context.setVariable("left", leftExpression);
        context.setVariable("right", new LiteralExpression(4));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testLessThan() {
        Expression leftExpression = new LiteralExpression(3);
        Expression rightExpression = new LiteralExpression(5);
        expression = new LessThanExpression(new CoreOperationRelationalExpression[]{leftExpression, rightExpression});
        NodeInfo leftNode = new NodeInfoImpl(new Path("/test"), AxisName.SELF, 0, 3, null);
        NodeInfo rightNode = new NodeInfoImpl(new Path("/test"), AxisName.SELF, 0, 5, null);
        context.setCurrentNode(leftNode);
        context.setVariable("left", leftExpression);
        context.setVariable("right", rightExpression);
        context.setCurrentAxis(AxisName.SELF);
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        context.setCurrentNode(rightNode);
        context.setVariable("left", leftExpression);
        context.setVariable("right", rightExpression);
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        context.setCurrentNode(leftNode);
        context.setVariable("left", leftExpression);
        context.setVariable("right", new LiteralExpression(3));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanOrEqual() {
        Expression leftExpression = new LiteralExpression(3);
        Expression rightExpression = new LiteralExpression(3);
        expression = new LessThanOrEqualExpression(new CoreOperationRelationalExpression[]{leftExpression, rightExpression});
        NodeInfo leftNode = new NodeInfoImpl(new Path("/test"), AxisName.SELF, 0, 3, null);
        NodeInfo rightNode = new NodeInfoImpl(new Path("/test"), AxisName.SELF, 0, 3, null);
        context.setCurrentNode(leftNode);
        context.setVariable("left", leftExpression);
        context.setVariable("right", rightExpression);
        context.setCurrentAxis(AxisName.SELF);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setCurrentNode(leftNode);
        context.setVariable("left", leftExpression);
        context.setVariable("right", new LiteralExpression(3));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setCurrentNode(leftNode);
        context.setVariable("left", leftExpression);
        context.setVariable("right", new LiteralExpression(4));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testIterators() {
        Expression leftExpression = new CoreExpressionVariable("left");
        Expression rightExpression = new LiteralExpression(5);
        expression = new GreaterThanExpression(new CoreOperationRelationalExpression[]{leftExpression, rightExpression});

        List list = Arrays.asList(1, 2, 3, 4, 5, 6);
        IterateContext iterateContext = new IterateContext();
        iterateContext.setCurrentAxis(AxisName.CHILD);
        iterateContext.setCurrentNode(new NodeInfoImpl(new Path("/test"), AxisName.SELF, 0, 0, null));
        context.setCurrentNode(iterateContext);
        context.setVariable("left", leftExpression);
        context.setVariable("right", rightExpression);
        Iterator leftIterator = list.iterator();
        Iterator rightIterator = Collections.singletonList(new NodeInfoImpl(new Path("/test"), AxisName.SELF, 0, 5, null)).iterator();
        context.setVariable("leftIterator", leftIterator);
        context.setVariable("rightIterator", rightIterator);
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        leftIterator = list.iterator();
        rightIterator = Collections.singletonList(new NodeInfoImpl(new Path("/test"), AxisName.SELF, 0, 4, null)).iterator();
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        leftIterator = list.iterator();
        rightIterator = Collections.singletonList(new NodeInfoImpl(new Path("/test"), AxisName.SELF, 0, 5, null)).iterator();
        context.setCurrentNode(iterateContext);
        context.setVariable("leftIterator", leftIterator);
        context.setVariable("rightIterator", rightIterator);
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidUsage() {
        Expression leftExpression = new CoreExpressionVariable("left");
        Expression rightExpression = new CoreExpressionVariable("right");
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{leftExpression, rightExpression});
        context.setCurrentNode(new NodeInfoImpl(new Path("/test"), AxisName.SELF, 0, 0, null));
        context.setVariable("left", leftExpression);
        context.setVariable("right", rightExpression);
        Object result = expression.computeValue(context);
        assertEquals(result, Boolean.FALSE);
    }
}