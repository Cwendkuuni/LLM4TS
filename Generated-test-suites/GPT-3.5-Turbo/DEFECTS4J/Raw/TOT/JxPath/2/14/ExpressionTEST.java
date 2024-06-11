import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.Locale;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new ExpressionImpl();
        boolean result = expression.isContextDependent();
        Assert.assertTrue(result);
    }

    @Test
    public void testComputeValue() {
        Expression expression = new ExpressionImpl();
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        Assert.assertNull(result);
    }

    @Test
    public void testCompute() {
        Expression expression = new ExpressionImpl();
        EvalContext context = new EvalContext();
        Object result = expression.compute(context);
        Assert.assertNull(result);
    }

    @Test
    public void testIterate() {
        Expression expression = new ExpressionImpl();
        EvalContext context = new EvalContext();
        Iterator iterator = expression.iterate(context);
        Assert.assertNotNull(iterator);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers() {
        Expression expression = new ExpressionImpl();
        EvalContext context = new EvalContext();
        Iterator iterator = expression.iteratePointers(context);
        Assert.assertNotNull(iterator);
        Assert.assertFalse(iterator.hasNext());
    }

    private static class ExpressionImpl extends Expression {

        @Override
        public boolean computeContextDependent() {
            return true;
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
}