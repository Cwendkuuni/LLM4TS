package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.IterateIter;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.Nodes;
import org.apache.commons.jxpath.ri.axes.Axis;
import org.apache.commons.jxpath.ri.axes.SelfAxis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Object left = new Nodes(new SelfAxis(new Axis("root"), "self::element()"));
        Object right = new Nodes(new SelfAxis(new Axis("root"), "self::element()[1]"));
        EvalContext context = new EvalContext(new InfoSet(), new InitialContext("root"));
        CoreOperationRelationalExpression expr = new GreaterThanExpression(new CoreExpression[] {new VariableExpression("left"), new VariableExpression("right")});

        Object result = expr.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        context.setSingleNodePointer(right);
        left = new Nodes(new SelfAxis(new Axis("root"), "self::element()[2]"));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new GreaterThanExpression(new CoreExpression[0]);
        assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, expr.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new GreaterThanExpression(new CoreExpression[0]);
        assertFalse(expr.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expr = new GreaterThanExpression(new CoreExpression[0]);
        assertFalse(expr.evaluateCompare(0));
        assertFalse(expr.evaluateCompare(-1));
        assertTrue(expr.evaluateCompare(1));
    }

    @Test
    public void testCompute_Iterators() {
        Collection<Object> left = new ArrayList<>();
        left.add(1);
        left.add(2);
        Iterator<Object> leftIter = left.iterator();

        Collection<Object> right = new ArrayList<>();
        right.add(2);
        right.add(3);
        Iterator<Object> rightIter = right.iterator();

        CoreOperationRelationalExpression expr = new GreaterThanExpression(new CoreExpression[] {
                new VariableExpression("left"),
                new VariableExpression("right")
        });

        assertFalse(expr.compute(leftIter, rightIter));

        left.add(3);
        assertTrue(expr.compute(leftIter, rightIter));
    }

    @Test
    public void testCompute_Collections() {
        List<Number> left = new ArrayList<>();
        left.add(1.0);
        left.add(2.0);

        List<Number> right = new ArrayList<>();
        right.add(2.0);
        right.add(3.0);

        CoreOperationRelationalExpression expr = new GreaterThanExpression(new CoreExpression[] {
                new VariableExpression("left"),
                new VariableExpression("right")
        });

        assertFalse(expr.compute(left.iterator(), right.iterator()));

        left.add(3.0);
        assertTrue(expr.compute(left.iterator(), right.iterator()));
    }

    @Test
    public void testCompute_Numbers() {
        double left = 1.0;
        double right = 2.0;

        CoreOperationRelationalExpression expr = new GreaterThanExpression(new CoreExpression[] {
                new VariableExpression("left"),
                new VariableExpression("right")
        });

        assertFalse(expr.compute(left, right));

        left = 3.0;
        assertTrue(expr.compute(left, right));
    }

    @Test
    public void testCompute_SelfContexts() {
        EvalContext context = new EvalContext(new InfoSet(), new InitialContext("root"));
        Object left = new Nodes(new SelfAxis(new Axis("root"), "self::element()[1]"));
        Object right = new Nodes(new SelfAxis(new Axis("root"), "self::element()[2]"));
        context.setSingleNodePointer(right);

        CoreOperationRelationalExpression expr = new GreaterThanExpression(new CoreExpression[] {
                new VariableExpression("left"),
                new VariableExpression("right")
        });

        assertFalse(expr.computeValue(context));

        context.setSingleNodePointer(left);
        left = new Nodes(new SelfAxis(new Axis("root"), "self::element()[2]"));
        context.setSingleNodePointer(left);

        assertTrue(expr.computeValue(context));
    }
}