import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for Expression class
 */
public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        AbstractExpression expression = new MyContextDependentExpression();
        assertTrue(expression.isContextDependent());

        AbstractExpression expression1 = new MyContextIndependentExpression();
        assertFalse(expression1.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        MyContextDependentExpression expression = new MyContextDependentExpression();
        EvalContext context = new EvalContext();
        context.setRootContext(new NodePointer(new QName(null, "root"), null));

        Object result = expression.computeValue(context);
        assertNull(result);

        context.setCurrentNodePointer(new NodePointer(new QName(null, "child"), null));
        result = expression.computeValue(context);
        assertSame(context.getCurrentNodePointer(), result);
    }

    @Test
    public void testCompute() {
        MyContextDependentExpression expression = new MyContextDependentExpression();
        EvalContext context = new EvalContext();

        Object result = expression.compute(context);
        assertNull(result);

        context.setRootContext(new NodePointer(new QName(null, "root"), null));
        result = expression.compute(context);
        assertNull(result);

        context.setCurrentNodePointer(new NodePointer(new QName(null, "child"), null));
        result = expression.compute(context);
        assertSame(context.getCurrentNodePointer(), result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterate_unsupportedOperationException() {
        AbstractExpression expression = new Expression();
        expression.iterate(new EvalContext());
    }

    @Test
    public void testIterate_emptyIterator() {
        MyContextIndependentExpression expression = new MyContextIndependentExpression();
        EvalContext context = new EvalContext();

        Iterator iterator = expression.iterate(context);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterate_singleValue() {
        MyContextIndependentExpression expression = new MyContextIndependentExpression();
        EvalContext context = new EvalContext();

        context.setRootContext(new NodePointer(new QName(null, "root"), null));
        context.setCurrentNodePointer(new NodePointer(new QName(null, "child"), null));

        Iterator iterator = expression.iterate(context);
        Object value = iterator.next();
        assertSame(context.getCurrentNodePointer().getValue(), value);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterate_multipleValues() {
        MyContextIndependentExpression expression = new MyContextIndependentExpression();
        EvalContext context = new EvalContext();

        context.setRootContext(new NodePointer(new QName(null, "root"), null));
        context.setCurrentNodePointer(new NodePointer(new QName(null, "child1"), null));

        Iterator iterator = expression.iterate(context);
        Object value1 = iterator.next();
        Object value2 = iterator.next();
        assertEquals(context.getCurrentNodePointer().getValue(), value1);
        assertEquals(context.getNextSibling(context.getCurrentNodePointer()).getValue(), value2);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers_emptyIterator() {
        AbstractExpression expression = new Expression();
        EvalContext context = new EvalContext();

        Iterator iterator = expression.iteratePointers(context);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers_singlePointer() {
        MyContextIndependentExpression expression = new MyContextIndependentExpression();
        EvalContext context = new EvalContext();

        context.setRootContext(new NodePointer(new QName(null, "root"), null));
        context.setCurrentNodePointer(new NodePointer(new QName(null, "child"), null));

        Iterator iterator = expression.iteratePointers(context);
        Pointer pointer = (Pointer) iterator.next();
        assertEquals(context.getCurrentNodePointer(), pointer);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers_multiplePointers() {
        MyContextIndependentExpression expression = new MyContextIndependentExpression();
        EvalContext context = new EvalContext();

        context.setRootContext(new NodePointer(new QName(null, "root"), null));
        context.setCurrentNodePointer(new NodePointer(new QName(null, "child1"), null));

        Iterator iterator = expression.iteratePointers(context);
        Pointer pointer1 = (Pointer) iterator.next();
        Pointer pointer2 = (Pointer) iterator.next();
        assertEquals(context.getCurrentNodePointer(), pointer1);
        assertEquals(context.getNextSibling(context.getCurrentNodePointer()), pointer2);
        assertFalse(iterator.hasNext());
    }

    private static class MyContextDependentExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return true;
        }
    }

    private static class MyContextIndependentExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return ValueUtils.iterate(context.getCurrentNodePointer().getValue());
        }

        @Override
        public Object compute(EvalContext context) {
            return computeValue(context);
        }
    }
}