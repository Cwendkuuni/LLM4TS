import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Expression.
 */
public class ExpressionTest {

    private EvalContext context;

    @Before
    public void setUp() {
        context = new EvalContext();
    }

    @Test
    public void testIsContextDependentDefault() {
        Expression expression = new DefaultExpression();
        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testComputeContextDependentDefault() {
        Expression expression = new DefaultExpression();
        assertFalse(expression.computeContextDependent());
    }

    @Test
    public void testComputeValueDefault() {
        Expression expression = new DefaultExpression();
        assertNull(expression.computeValue(context));
    }

    @Test
    public void testComputeContextDependentSubClassContextDependent() {
        Expression expression = new ContextDependentExpression();
        assertTrue(expression.isContextDependent());
    }

    @Test
    public void testComputeContextDependentSubClassContextIndependent() {
        Expression expression = new ContextIndependentExpression();
        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testIterateNull() {
        Expression expression = new ContextIndependentExpression();
        Iterator<Object> iterator = expression.iterate(context);
        assertNull(iterator);
    }

    @Test
    public void testIterateEmpty() {
        Expression expression = new ContextIndependentExpression();
        Iterator<Object> iterator = expression.iterate(context);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterateSingleValue() {
        Expression expression = new ContextIndependentExpression();
        expression.setCurrentValue(Integer.valueOf(10));
        Iterator<Object> iterator = expression.iterate(context);
        assertTrue(iterator.hasNext());
        Object next = iterator.next();
        assertEquals(Integer.valueOf(10), next);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterateSimpleNodeSet() {
        Expression expression = new ContextIndependentExpression();
        NodePointer nodePointer = new NodePointer(new QName("root"), null);
        expression.setCurrentValue(nodePointer);
        Iterator<Object> iterator = expression.iterate(context);
        assertTrue(iterator.hasNext());
        Object next = iterator.next();
        assertSame(nodePointer, next);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterateComplexNodeSet() {
        Expression expression = new ContextIndependentExpression();
        NodePointer nodePointer1 = new NodePointer(new QName("root1"), null);
        NodePointer nodePointer2 = new NodePointer(new QName("root2"), null);
        expression.setCurrentValue(Collections.singletonList(nodePointer1, nodePointer2));
        Iterator<Object> iterator = expression.iterate(context);
        assertTrue(iterator.hasNext());
        Object next = iterator.next();
        assertSame(nodePointer1, next);
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertSame(nodePointer2, next);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointersNull() {
        Expression expression = new ContextIndependentExpression();
        Iterator<Object> iterator = expression.iteratePointers(context);
        assertNull(iterator);
    }

    @Test
    public void testIteratePointersEmpty() {
        Expression expression = new ContextIndependentExpression();
        Iterator<Object> iterator = expression.iteratePointers(context);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointersSinglePointer() {
        Expression expression = new ContextIndependentExpression();
        NodePointer nodePointer = new NodePointer(new QName("root"), null);
        expression.setCurrentValue(nodePointer);
        Iterator<Object> iterator = expression.iteratePointers(context);
        assertTrue(iterator.hasNext());
        Object next = iterator.next();
        assertSame(nodePointer, next);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointersMultiplePointers() {
        Expression expression = new ContextIndependentExpression();
        NodePointer nodePointer1 = new NodePointer(new QName("root1"), null);
        NodePointer nodePointer2 = new NodePointer(new QName("root2"), null);
        expression.setCurrentValue(new NodePointer[]{nodePointer1, nodePointer2});
        Iterator<Object> iterator = expression.iteratePointers(context);
        assertTrue(iterator.hasNext());
        Object next = iterator.next();
        assertSame(nodePointer1, next);
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertSame(nodePointer2, next);
        assertFalse(iterator.hasNext());
    }

    private static class DefaultExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return null;
        }

        @Override
        public Object compute(EvalContext context) {
            return null;
        }
    }

    private static class ContextDependentExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return true;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return context.getNodeSet().size();
        }

        @Override
        public Object compute(EvalContext context) {
            return context.getNodeSet().size();
        }
    }

    private static class ContextIndependentExpression extends Expression {
        private Integer currentValue;

        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return currentValue;
        }

        @Override
        public Object compute(EvalContext context) {
            return currentValue;
        }

        public void setCurrentValue(Integer currentValue) {
            this.currentValue = currentValue;
        }
    }
}