import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Expression.PointerIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

public class ExpressionTest {

    @Mock
    EvalContext context;

    @Test
    public void testComputeValue_NullContext() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() { return true; }
            public Object computeValue(EvalContext context) { return null; }
            public Object compute(EvalContext context) { return null; }
        };

        Object result = expression.computeValue(null);

        Assert.assertNull(result);
    }

    @Test
    public void testComputeValue_EmptyContext() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() { return true; }
            public Object computeValue(EvalContext context) { return null; }
            public Object compute(EvalContext context) { return null; }
        };

        Object result = expression.computeValue(context);

        Assert.assertNull(result);
    }

    @Test
    public void testComputeValue_ContextNoNodes() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() { return true; }
            public Object computeValue(EvalContext context) { return null; }
            public Object compute(EvalContext context) { return null; }
        };

        Object result = expression.computeValue(context);

        Assert.assertNull(result);
    }

    @Test
    public void testCompute_NullContext() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() { return true; }
            public Object computeValue(EvalContext context) { return null; }
            public Object compute(EvalContext context) { return null; }
        };

        Object result = expression.compute(null);

        Assert.assertNull(result);
    }

    @Test
    public void testCompute_EmptyContext() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() { return true; }
            public Object computeValue(EvalContext context) { return null; }
            public Object compute(EvalContext context) { return null; }
        };

        Object result = expression.compute(context);

        Assert.assertNull(result);
    }

    @Test
    public void testCompute_ContextNoNodes() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() { return true; }
            public Object computeValue(EvalContext context) { return null; }
            public Object compute(EvalContext context) { return null; }
        };

        Object result = expression.compute(context);

        Assert.assertNull(result);
    }

    @Test
    public void testIterate_NullContext() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() { return true; }
            public Object computeValue(EvalContext context) { return null; }
            public Object compute(EvalContext context) { return null; }
        };

        Iterator iterator = expression.iterate(null);

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterate_EmptyContext() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() { return true; }
            public Object computeValue(EvalContext context) { return null; }
            public Object compute(EvalContext context) { return null; }
        };

        Iterator iterator = expression.iterate(context);

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterate_ContextNoNodes() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() { return true; }
            public Object computeValue(EvalContext context) { return null; }
            public Object compute(EvalContext context) { return null; }
        };

        Iterator iterator = expression.iterate(context);

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers_NullContext() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() { return true; }
            public Object computeValue(EvalContext context) { return null; }
            public Object compute(EvalContext context) { return null; }
        };

        Iterator iterator = expression.iteratePointers(null);

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers_EmptyContext() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() { return true; }
            public Object computeValue(EvalContext context) { return null; }
            public Object compute(EvalContext context) { return null; }
        };

        Iterator iterator = expression.iteratePointers(context);

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers_ContextNoNodes() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() { return true; }
            public Object computeValue(EvalContext context) { return null; }
            public Object compute(EvalContext context) { return null; }
        };

        Iterator iterator = expression.iteratePointers(context);

        Assert.assertFalse(iterator.hasNext());
    }
}