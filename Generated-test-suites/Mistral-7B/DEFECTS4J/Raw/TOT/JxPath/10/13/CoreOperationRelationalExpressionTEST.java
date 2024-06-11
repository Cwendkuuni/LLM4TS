import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        Expression left = new LiteralExpression(new Double(5));
        Expression right = new LiteralExpression(new Double(3));
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new CoreOperation[] {left, right});

        EvalContext context = new EvalContext(new Object[] {new Object[] {new InitialContext(), new SelfContext(), new Integer(1) }});
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new LiteralExpression(new Double(3));
        right = new LiteralExpression(new Double(5));
        expression = new GreaterThanExpression(new CoreOperation[] {left, right});
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(new Double(5));
        right = new LiteralExpression(new Double(5));
        expression = new GreaterThanExpression(new CoreOperation[] {left, right});
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(new Double(Double.NaN));
        right = new LiteralExpression(new Double(3));
        expression = new GreaterThanExpression(new CoreOperation[] {left, right});
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(new Double(3));
        right = new LiteralExpression(new Double(Double.NaN));
        expression = new GreaterThanExpression(new CoreOperation[] {left, right});
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(new Double(3));
        right = new LiteralExpression(new Double(3));
        expression = new GreaterThanExpression(new CoreOperation[] {left, right});
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testGreaterThanOrEqual() {
        Expression left = new LiteralExpression(new Double(5));
        Expression right = new LiteralExpression(new Double(3));
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(new CoreOperation[] {left, right});

        EvalContext context = new EvalContext(new Object[] {new Object[] {new InitialContext(), new SelfContext(), new Integer(1) }});
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(new Double(5));
        right = new LiteralExpression(new Double(5));
        expression = new GreaterThanOrEqualExpression(new CoreOperation[] {left, right});
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new LiteralExpression(new Double(3));
        right = new LiteralExpression(new Double(3));
        expression = new GreaterThanOrEqualExpression(new CoreOperation[] {left, right});
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new LiteralExpression(new Double(5));
        right = new LiteralExpression(new Double(3));
        expression = new GreaterThanOrEqualExpression(new CoreOperation[] {left, right});
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new LiteralExpression(new Double(Double.NaN));
        right = new LiteralExpression(new Double(3));
        expression = new GreaterThanOrEqualExpression(new CoreOperation[] {left, right});
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(new Double(3));
        right = new LiteralExpression(new Double(Double.NaN));
        expression = new GreaterThanOrEqualExpression(new CoreOperation[] {left, right});
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testLessThan() {
        Expression left = new LiteralExpression(new Double(5));
        Expression right = new LiteralExpression(new Double(3));
        CoreOperationRelationalExpression expression = new LessThanExpression(new CoreOperation[] {left, right});

        EvalContext context = new EvalContext(new Object[] {new Object[] {new InitialContext(), new SelfContext(), new Integer(1) }});
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(new Double(3));
        right = new LiteralExpression(new Double(5));
        expression = new LessThanExpression(new CoreOperation[] {left, right});
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(new Double(5));
        right = new LiteralExpression(new Double(5));
        expression = new LessThanExpression(new CoreOperation[] {left, right});
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(new Double(Double.NaN));
        right = new LiteralExpression(new Double(3));
        expression = new LessThanExpression(new CoreOperation[] {left, right});
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(new Double(3));
        right = new LiteralExpression(new Double(Double.NaN));
        expression = new LessThanExpression(new CoreOperation[] {left, right});
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(new Double(3));
        right = new LiteralExpression(new Double(3));
        expression = new LessThanExpression(new CoreOperation[] {left, right});
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testLessThanOrEqual() {
        Expression left = new LiteralExpression(new Double(5));
        Expression right = new LiteralExpression(new Double(3));
        CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(new CoreOperation[] {left, right});

        EvalContext context = new EvalContext(new Object[] {new Object[] {new InitialContext(), new SelfContext(), new Integer(1) }});
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(new Double(5));
        right = new LiteralExpression(new Double(5));
        expression = new LessThanOrEqualExpression(new CoreOperation[] {left, right});
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new LiteralExpression(new Double(3));
        right = new LiteralExpression(new Double(3));
        expression = new LessThanOrEqualExpression(new CoreOperation[] {left, right});
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new LiteralExpression(new Double(5));
        right = new LiteralExpression(new Double(3));
        expression = new LessThanOrEqualExpression(new CoreOperation[] {left, right});
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(new Double(Double.NaN));
        right = new LiteralExpression(new Double(3));
        expression = new LessThanOrEqualExpression(new CoreOperation[] {left, right});
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(new Double(3));
        right = new LiteralExpression(new Double(Double.NaN));
        expression = new LessThanOrEqualExpression(new CoreOperation[] {left, right});
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testIterators() {
        Expression left = new CollectionExpression(new ArrayList<Object>(Arrays.asList(1, 2, 3)));
        Expression right = new CollectionExpression(new ArrayList<Object>(Arrays.asList(2, 3, 4)));
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new CoreOperation[] {left, right});

        EvalContext context = new EvalContext(new Object[] {new Object[] {new InitialContext(), new SelfContext(), new Iterator[] {left.computeValue(context), right.computeValue(context)} }});
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new CollectionExpression(new ArrayList<Object>(Arrays.asList(1, 2, 3)));
        right = new CollectionExpression(new ArrayList<Object>(Arrays.asList(3, 2, 1)));
        expression = new GreaterThanOrEqualExpression(new CoreOperation[] {left, right});

        context = new EvalContext(new Object[] {new Object[] {new InitialContext(), new SelfContext(), new Iterator[] {left.computeValue(context), right.computeValue(context)} }});
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new CollectionExpression(new ArrayList<Object>(Arrays.asList(1, 2, 3)));
        right = new CollectionExpression(new ArrayList<Object>(Arrays.asList(1, 2, 3)));
        expression = new GreaterThanExpression(new CoreOperation[] {left, right});

        context = new EvalContext(new Object[] {new Object[] {new InitialContext(), new SelfContext(), new Iterator[] {left.computeValue(context), right.computeValue(context)} }});
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new CollectionExpression(new ArrayList<Object>(Arrays.asList(1, 2, 3)));
        right = new CollectionExpression(new ArrayList<Object>(Arrays.asList(3, 2, 1)));
        expression = new LessThanExpression(new CoreOperation[] {left, right});

        context = new EvalContext(new Object[] {new Object[] {new InitialContext(), new SelfContext(), new Iterator[] {left.computeValue(context), right.computeValue(context)} }});
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new CollectionExpression(new ArrayList<Object>(Arrays.asList(1, 2, 3)));
        right = new CollectionExpression(new ArrayList<Object>(Arrays.asList(1, 2, 3)));
        expression = new LessThanOrEqualExpression(new CoreOperation[] {left, right});

        context = new EvalContext(new Object[] {new Object[] {new InitialContext(), new SelfContext(), new Iterator[] {left.computeValue(context), right.computeValue(context)} }});
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}