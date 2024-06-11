package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.Iterate;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;

    @Before
    public void setUp() {
        expression = new CoreOperationRelationalExpression(new Expression[]{
                new NodeTest(NodeTest.NODE_TYPE_ELEMENT, "foo"),
                new NodeTest(NodeTest.NODE_TYPE_ELEMENT, "bar")
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext(new NodeInfo[]{
                new NodeInfo("root", Integer.valueOf(1)),
                new NodeInfo("foo", Integer.valueOf(2)),
                new NodeInfo("bar", Integer.valueOf(3))
        }, null, new InitialContext());

        Object left = expression.getArguments()[0].computeValue(context);
        Object right = expression.getArguments()[1].computeValue(context);

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        left = new NodeInfo("foo", Integer.valueOf(2));
        right = new NodeInfo("bar", Integer.valueOf(3));

        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testComputeWithInitialContexts() {
        EvalContext context = new EvalContext(new Object[]{}, null, new InitialContext());

        Object left = expression.getArguments()[0].computeValue(context);
        Object right = expression.getArguments()[1].computeValue(context);

        Object result = expression.computeValue(context);

        assertNull(result);

        context = new EvalContext(new Object[]{
                new SelfContext(),
                new SelfContext()
        }, null, new InitialContext());

        left = expression.getArguments()[0].computeValue(context);
        right = expression.getArguments()[1].computeValue(context);

        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeWithIterators() {
        List<NodeInfo> leftList = Collections.singletonList(new NodeInfo("foo", Integer.valueOf(1)));
        Iterator<NodeInfo> leftIterator = leftList.iterator();

        List<NodeInfo> rightList = Collections.singletonList(new NodeInfo("bar", Integer.valueOf(2)));
        Iterator<NodeInfo> rightIterator = rightList.iterator();

        EvalContext context = new EvalContext(new Object[]{
                new SelfContext(),
                new SelfContext()
        }, null, new InitialContext());

        Object left = expression.getArguments()[0].computeValue(context);
        Object right = expression.getArguments()[1].computeValue(context);

        if (left instanceof Iterator && right instanceof Iterator) {
            ((Iterator) left).next();
            ((Iterator) right).next();
        }

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        leftIterator = leftList.iterator();
        rightIterator = rightList.iterator();

        leftIterator.next();
        rightIterator.next();

        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeWithDoubles() {
        EvalContext context = new EvalContext(new Object[]{
                InfoSetUtil.createNumber(1.5),
                InfoSetUtil.createNumber(2.5)
        }, null, new InitialContext());

        Object left = expression.getArguments()[0].computeValue(context);
        Object right = expression.getArguments()[1].computeValue(context);

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeWithDoubleAndNode() {
        EvalContext context = new EvalContext(new Object[]{
                new NodeInfo("foo", Integer.valueOf(1)),
                InfoSetUtil.createNumber(2.5)
        }, null, new InitialContext());

        Object left = expression.getArguments()[0].computeValue(context);
        Object right = expression.getArguments()[1].computeValue(context);

        Object result = expression.computeValue(context);

        assertNull(result);
    }

    @Test
    public void testComputeWithNodeAndDouble() {
        EvalContext context = new EvalContext(new Object[]{
                InfoSetUtil.createNumber(1.5),
                new NodeInfo("foo", Integer.valueOf(2))
        }, null, new InitialContext());

        Object left = expression.getArguments()[0].computeValue(context);
        Object right = expression.getArguments()[1].computeValue(context);

        Object result = expression.computeValue(context);

        assertNull(result);
    }
}