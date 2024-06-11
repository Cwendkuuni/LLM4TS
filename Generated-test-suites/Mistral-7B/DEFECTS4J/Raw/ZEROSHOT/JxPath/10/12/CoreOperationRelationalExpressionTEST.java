import org.apache.commons.jxpath.ri.Axes;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionTest;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.InitialContext;
import org.apache.commons.jxpath.ri.compiler.SelfContext;
import org.apache.commons.jxpath.ri.compiler.TestSupport;
import org.apache.commons.jxpath.ri.eval.Context;
import org.apache.commons.jxpath.ri.iter.ArrayIterator;
import org.apache.commons.jxpath.ri.iter.Iteratorex;
import org.apache.commons.jxpath.ri.iter.SingletonIterator;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link CoreOperationRelationalExpression}.
 */
public class CoreOperationRelationalExpressionTest extends TestSupport {

    @Test
    public void testGreaterThan() {
        Expression left = new LiteralExpression(5);
        Expression right = new LiteralExpression(10);
        CoreOperationRelationalExpression expression = new GreaterThanExpression(left, right);
        Object result = expression.computeValue(createContext(new InitialContext(new Object[] { new Object[] { new Integer(5), new Integer(10) } })));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        left = new LiteralExpression(10);
        Expression greaterThanOrEqualExpression = new GreaterThanOrEqualExpression(left, right);
        result = greaterThanOrEqualExpression.computeValue(createContext(new InitialContext(new Object[] { new Object[] { new Integer(10), new Integer(10) } })));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        left = new LiteralExpression(5);
        result = greaterThanOrEqualExpression.computeValue(createContext(new InitialContext(new Object[] { new Object[] { new Integer(5), new Integer(10) } })));
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testLessThan() {
        Expression left = new LiteralExpression(1);
        Expression right = new LiteralExpression(5);
        CoreOperationRelationalExpression expression = new LessThanExpression(left, right);
        Object result = expression.computeValue(createContext(new InitialContext(new Object[] { new Object[] { new Integer(1), new Integer(5) } })));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        left = new LiteralExpression(5);
        expression = new LessThanOrEqualExpression(left, right);
        result = expression.computeValue(createContext(new InitialContext(new Object[] { new Object[] { new Integer(5), new Integer(5) } })));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        left = new LiteralExpression(6);
        result = expression.computeValue(createContext(new InitialContext(new Object[] { new Object[] { new Integer(6), new Integer(5) } })));
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testEquals() {
        Expression left = new LiteralExpression(5);
        Expression right = new LiteralExpression(5);
        CoreOperationRelationalExpression expression = new EqualsExpression(left, right);
        Object result = expression.computeValue(createContext(new InitialContext(new Object[] { new Object[] { new Integer(5), new Integer(5) } })));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        left = new LiteralExpression(5);
        right = new LiteralExpression(10);
        result = expression.computeValue(createContext(new InitialContext(new Object[] { new Object[] { new Integer(5), new Integer(10) } })));
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(null);
        right = new LiteralExpression(null);
        result = expression.computeValue(createContext(new InitialContext(new Object[] { new Object[] { null, null } })));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        left = new LiteralExpression(null);
        result = expression.computeValue(createContext(new InitialContext(new Object[] { new Object[] { null, new Integer(5) } })));
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testNotEquals() {
        Expression left = new LiteralExpression(5);
        Expression right = new LiteralExpression(5);
        CoreOperationRelationalExpression expression = new NotEqualsExpression(left, right);
        Object result = expression.computeValue(createContext(new InitialContext(new Object[] { new Object[] { new Integer(5), new Integer(5) } })));
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(5);
        right = new LiteralExpression(10);
        result = expression.computeValue(createContext(new InitialContext(new Object[] { new Object[] { new Integer(5), new Integer(10) } })));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        left = new LiteralExpression(null);
        right = new LiteralExpression(null);
        result = expression.computeValue(createContext(new InitialContext(new Object[] { new Object[] { null, null } })));
        assertFalse(result instanceof Boolean);

        left = new LiteralExpression(null);
        right = new LiteralExpression(new Integer(5));
        result = expression.computeValue(createContext(new InitialContext(new Object[] { new Object[] { null, new Integer(5) } })));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testIterators() {
        Expression left = new AxisExpression(Axes.SELF, null);
        Expression right = new LiteralExpression(5);
        CoreOperationRelationalExpression expression = new GreaterThanExpression(left, right);

        Context context = createContext(new SelfContext(new Object[] { new Integer(4) }));
        Iterator iter1 = createIterable(new Integer[] { 3, 4, 5 });
        context.setCurrentNodePointer(iter1);
        Iterator iter2 = new SingletonIterator(new Integer(5));
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = createContext(new SelfContext(new Object[] { new Integer(4) }));
        iter1 = createIterable(new Integer[] { 3, 4, 5 });
        context.setCurrentNodePointer(iter1);
        Iterator iter3 = new SingletonIterator(new Integer(6));
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        context = createContext(new SelfContext(new Object[] { new Integer(5) }));
        iter1 = createIterable(new Integer[] { 3, 4, 5 });
        context.setCurrentNodePointer(iter1);
        iter2 = new SingletonIterator(new Integer(5));
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        context = createContext(new SelfContext(new Object[] { new Integer(5) }));
        iter1 = createIterable(new Integer[] { 3, 4, 5 });
        context.setCurrentNodePointer(iter1);
        iter3 = new SingletonIterator(new Integer(6));
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        context = createContext(new SelfContext(new Object[] { new Integer(5) }));
        iter1 = createIterable(new Integer[] { 3, 4, 5 });
        context.setCurrentNodePointer(iter1);
        iter2 = new ArrayIterator(new Integer[] { 5, 5 });
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = createContext(new SelfContext(new Object[] { new Integer(5) }));
        iter1 = createIterable(new Integer[] { 3, 4, 5 });
        context.setCurrentNodePointer(iter1);
        iter2 = new ArrayIterator(new Integer[] { 3, 5, 5 });
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        context = createContext(new SelfContext(new Object[] { new Integer(5) }));
        iter1 = Collections.emptyIterator();
        context.setCurrentNodePointer(iter1);
        iter2 = new SingletonIterator(new Integer(5));
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testContains() {
        Expression left = new AxisExpression(Axes.SELF, null);
        Expression right = new AxisExpression(Axes.SELF, null);
        CoreOperationRelationalExpression expression = new ContainsExpression(left, right);

        Context context = createContext(new SelfContext(new Object[] { new Integer(5) }));
        Iterator iter1 = createIterable(new Integer[] { 3, 4, 5 });
        context.setCurrentNodePointer(iter1);
        Iterator iter2 = createIterable(new Integer[] { 3, 4, 5 });
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = createContext(new SelfContext(new Object[] { new Integer(5) }));
        iter1 = createIterable(new Integer[] { 3, 4, 5 });
        context.setCurrentNodePointer(iter1);
        iter2 = new SingletonIterator(new Integer(6));
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        context = createContext(new SelfContext(new Object[] { new Integer(5) }));
        iter1 = createIterable(new Integer[] { 3, 4, 5 });
        context.setCurrentNodePointer(iter1);
        iter2 = createIterable(new Integer[] { 5, 5 });
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = createContext(new SelfContext(new Object[] { new Integer(5) }));
        iter1 = createIterable(new Integer[] { 3, 4, 5 });
        context.setCurrentNodePointer(iter1);
        iter2 = Collections.emptyIterator();
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        context = createContext(new SelfContext(new Object[] { new Integer(5) }));
        iter1 = createIterable(new Integer[] { 3, 4, 5, 5 });
        context.setCurrentNodePointer(iter1);
        iter2 = createIterable(new Integer[] { 3, 4, 5 });
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    private Context createContext(Object... nodes) {
        return new Context(new EvalContext(true, nodes, new InitialContext(nodes), new SelfContext(nodes)));
    }

    private Iterator createIterable(Object... elements) {
        return new Iteratorex(Arrays.asList(elements));
    }
}