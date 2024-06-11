package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.Iterate;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.axes.Axis;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new VariableExpression("a"), new VariableExpression("b"));
        EvaluationContext context = new EvaluationContext(new Path(""), new Axis(Axis.CHILD, null, null, null), new SelfContext(), new InitialContext(), null);

        Object left = new Integer(5);
        Object right = new Integer(3);
        assertEquals(Boolean.TRUE, expression.computeValue(context, left, right));

        left = new Double(3.1);
        right = new Double(5.2);
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));

        left = new String("A");
        right = new String("B");
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList(1, 2, 3, 4, 5).iterator());
        right = new Iterate(Arrays.asList(2, 3, 4, 5, 6).iterator());
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList("A", "B", "C", "D", "E").iterator());
        right = new Iterate(Arrays.asList("B", "C", "D").iterator());
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList(1.1, 2.1, 3.1, 4.1, 5.1).iterator());
        right = new Iterate(Arrays.asList(2.2, 3.2, 4.1, 4.1, 5.2).iterator());
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList(1, 2, null, 4, 5).iterator());
        right = new Iterate(Arrays.asList(2, 3, null, 4, 5).iterator());
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList("A", null, "C", "D", "E").iterator());
        right = new Iterate(Arrays.asList("B", null, "C", "D", "F").iterator());
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList(1.1, Double.NaN, 3.1, 4.1, 5.1).iterator());
        right = new Iterate(Arrays.asList(2.2, Double.NaN, 4.1, 4.1, 5.2).iterator());
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList(new Integer(5), "A", new Double(3.1), null, new Integer(5)).iterator());
        right = new Iterate(Arrays.asList(new Integer(3), "B", new Double(5.2), "C", new Integer(4)).iterator());
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList(new Double(3.1), new Double(3.1), new Double(3.1), null, new Double(3.1)).iterator());
        right = new Iterate(Arrays.asList(new Double(4.1), new Double(3.1), new Double(3.1), new Double(4.2), new Double(3.1)).iterator());
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));
    }

    @Test
    public void testGreaterThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new VariableExpression("a"), new VariableExpression("b"));
        EvaluationContext context = new EvaluationContext(new Path(""), new Axis(Axis.CHILD, null, null, null), new SelfContext(), new InitialContext(), null);

        Object left = new Integer(5);
        Object right = new Integer(3);
        assertEquals(Boolean.TRUE, expression.computeValue(context, left, right));

        left = new Double(3.1);
        right = new Double(5.2);
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));

        left = new Integer(5);
        right = new Integer(5);
        assertEquals(Boolean.TRUE, expression.computeValue(context, left, right));

        left = new String("A");
        right = new String("A");
        assertEquals(Boolean.TRUE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList(1, 2, 3, 4, 5).iterator());
        right = new Iterate(Arrays.asList(2, 3, 4, 5, 6).iterator());
        assertEquals(Boolean.TRUE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList("A", "B", "C", "D", "E").iterator());
        right = new Iterate(Arrays.asList("B", "C", "D", "E", "F").iterator());
        assertEquals(Boolean.TRUE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList(1.1, 2.1, 3.1, 4.1, 5.1).iterator());
        right = new Iterate(Arrays.asList(2.2, 3.1, 4.1, 4.1, 5.2).iterator());
        assertEquals(Boolean.TRUE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList(1, 2, null, 4, 5).iterator());
        right = new Iterate(Arrays.asList(2, 3, null, 4, 5).iterator());
        assertEquals(Boolean.TRUE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList("A", null, "C", "D", "E").iterator());
        right = new Iterate(Arrays.asList("B", null, "C", "D", "F").iterator());
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList(1.1, Double.NaN, 3.1, 4.1, 5.1).iterator());
        right = new Iterate(Arrays.asList(2.2, Double.NaN, 4.1, 4.1, 5.2).iterator());
        assertEquals(Boolean.TRUE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList(new Integer(5), "A", new Double(3.1), null, new Integer(5)).iterator());
        right = new Iterate(Arrays.asList(new Integer(3), "B", new Double(5.2), "C", new Integer(4)).iterator());
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList(new Double(3.1), new Double(3.1), new Double(3.1), null, new Double(3.1)).iterator());
        right = new Iterate(Arrays.asList(new Double(4.1), new Double(3.1), new Double(3.1), new Double(4.2), new Double(3.1)).iterator());
        assertEquals(Boolean.TRUE, expression.computeValue(context, left, right));
    }

    @Test
    public void testLessThan() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new VariableExpression("a"), new VariableExpression("b"));
        EvaluationContext context = new EvaluationContext(new Path(""), new Axis(Axis.CHILD, null, null, null), new SelfContext(), new InitialContext(), null);

        Object left = new Integer(5);
        Object right = new Integer(3);
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));

        left = new Double(3.1);
        right = new Double(5.2);
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));

        left = new Integer(5);
        right = new Integer(5);
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));

        left = new String("A");
        right = new String("A");
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList(1, 2, 3, 4, 5).iterator());
        right = new Iterate(Arrays.asList(2, 3, 4, 5, 6).iterator());
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList("A", "B", "C", "D", "E").iterator());
        right = new Iterate(Arrays.asList("B", "C", "D", "E", "F").iterator());
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList(1.1, 2.1, 3.1, 4.1, 5.1).iterator());
        right = new Iterate(Arrays.asList(2.2, 3.1, 4.1, 4.1, 5.2).iterator());
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList(1, 2, null, 4, 5).iterator());
        right = new Iterate(Arrays.asList(2, 3, null, 4, 5).iterator());
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList("A", null, "C", "D", "E").iterator());
        right = new Iterate(Arrays.asList("B", null, "C", "D", "F").iterator());
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList(1.1, Double.NaN, 3.1, 4.1, 5.1).iterator());
        right = new Iterate(Arrays.asList(2.2, Double.NaN, 4.1, 4.1, 5.2).iterator());
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList(new Integer(5), "A", new Double(3.1), null, new Integer(5)).iterator());
        right = new Iterate(Arrays.asList(new Integer(3), "B", new Double(5.2), "C", new Integer(4)).iterator());
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList(new Double(3.1), new Double(3.1), new Double(3.1), null, new Double(3.1)).iterator());
        right = new Iterate(Arrays.asList(new Double(4.1), new Double(3.1), new Double(3.1), new Double(4.2), new Double(3.1)).iterator());
        assertEquals(Boolean.FALSE, expression.computeValue(context, left, right));
    }

    @Test
    public void testLessThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(new VariableExpression("a"), new VariableExpression("b"));
        EvaluationContext context = new EvaluationContext(new Path(""), new Axis(Axis.CHILD, null, null, null), new SelfContext(), new InitialContext(), null);

        Object left = new Integer(5);
        Object right = new Integer(3);
        assertEquals(Boolean.TRUE, expression.computeValue(context, left, right));

        left = new Double(3.1);
        right = new Double(5.2);
        assertEquals(Boolean.TRUE, expression.computeValue(context, left, right));

        left = new Integer(5);
        right = new Integer(5);
        assertEquals(Boolean.TRUE, expression.computeValue(context, left, right));

        left = new String("A");
        right = new String("A");
        assertEquals(Boolean.TRUE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList(1, 2, 3, 4, 5).iterator());
        right = new Iterate(Arrays.asList(2, 3, 4, 5, 6).iterator());
        assertEquals(Boolean.TRUE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList("A", "B", "C", "D", "E").iterator());
        right = new Iterate(Arrays.asList("B", "C", "D", "E", "F").iterator());
        assertEquals(Boolean.TRUE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList(1.1, 2.1, 3.1, 4.1, 5.1).iterator());
        right = new Iterate(Arrays.asList(2.2, 3.1, 4.1, 4.1, 5.2).iterator());
        assertEquals(Boolean.TRUE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList(1, 2, null, 4, 5).iterator());
        right = new Iterate(Arrays.asList(2, 3, null, 4, 5).iterator());
        assertEquals(Boolean.TRUE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList("A", null, "C", "D", "E").iterator());
        right = new Iterate(Arrays.asList("B", null, "C", "D", "F").iterator());
        assertEquals(Boolean.TRUE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList(1.1, Double.NaN, 3.1, 4.1, 5.1).iterator());
        right = new Iterate(Arrays.asList(2.2, Double.NaN, 4.1, 4.1, 5.2).iterator());
        assertEquals(Boolean.TRUE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList(new Integer(5), "A", new Double(3.1), null, new Integer(5)).iterator());
        right = new Iterate(Arrays.asList(new Integer(3), "B", new Double(5.2), "C", new Integer(4)).iterator());
        assertEquals(Boolean.TRUE, expression.computeValue(context, left, right));

        left = new Iterate(Arrays.asList(new Double(3.1), new Double(3.1), new Double(3.1), null, new Double(3.1)).iterator());
        right = new Iterate(Arrays.asList(new Double(4.1), new Double(3.1), new Double(3.1), new Double(4.2), new Double(3.1)).iterator());
        assertEquals(Boolean.TRUE, expression.computeValue(context, left, right));
    }
}