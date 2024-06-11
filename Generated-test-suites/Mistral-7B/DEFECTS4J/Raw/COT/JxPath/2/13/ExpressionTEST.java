import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

/**
 * Test class for Expression.
 */
public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new MyExpression(); // replace with actual subclass
        Assert.assertFalse(expression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        Expression expression = new MyExpression(); // replace with actual subclass
        Object result = expression.computeValue(new EvalContext());
        Assert.assertNotNull(result);
    }

    @Test
    public void testCompute() {
        Expression expression = new MyExpression(); // replace with actual subclass
        Object result = expression.compute(new EvalContext());
        Assert.assertNotNull(result);
    }

    @Test
    public void testIterate() {
        Expression expression = new MyExpression(); // replace with actual subclass
        Iterator iterator = expression.iterate(new EvalContext());
        Assert.assertTrue(iterator.hasNext());
    }

    @Test
    public void testIteratePointers() {
        Expression expression = new MyExpression(); // replace with actual subclass
        Iterator iterator = expression.iteratePointers(new EvalContext());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testPointerIteratorHasNext() {
        Expression expression = new Expression() {
            @Override
            public Iterator iteratePointers(EvalContext context) {
                return new PointerIterator(ValueUtils.iterate(new Object[]{new NodePointer(new QName(null, "root"), null, Locale.US)}),
                        new QName(null, "value"),
                        new Locale("en", "US"));
            }
        };
        PointerIterator pointerIterator = (PointerIterator) expression.iteratePointers(new EvalContext());
        Assert.assertTrue(pointerIterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPointerIteratorRemove() {
        Expression expression = new Expression() {
            @Override
            public Iterator iteratePointers(EvalContext context) {
                return new PointerIterator(ValueUtils.iterate(new Object[]{new NodePointer(new QName(null, "root"), null, Locale.US)}),
                        new QName(null, "value"),
                        new Locale("en", "US"));
            }
        };
        PointerIterator pointerIterator = (PointerIterator) expression.iteratePointers(new EvalContext());
        pointerIterator.remove();
    }

    @Test
    public void testValueIteratorHasNext() {
        Expression expression = new Expression() {
            @Override
            public Iterator iterate(EvalContext context) {
                return new ValueIterator(ValueUtils.iterate(new Object[]{1, 2, 3}));
            }
        };
        ValueIterator valueIterator = (ValueIterator) expression.iterate(new EvalContext());
        Assert.assertTrue(valueIterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueIteratorRemove() {
        Expression expression = new Expression() {
            @Override
            public Iterator iterate(EvalContext context) {
                return new ValueIterator(ValueUtils.iterate(new Object[]{1, 2, 3}));
            }
        };
        ValueIterator valueIterator = (ValueIterator) expression.iterate(new EvalContext());
        valueIterator.remove();
    }

}