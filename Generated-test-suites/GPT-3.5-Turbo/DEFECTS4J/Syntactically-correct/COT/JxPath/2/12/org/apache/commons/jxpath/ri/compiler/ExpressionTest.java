import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.util.ValueUtils;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new Expression() {
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
        };

        boolean result = expression.isContextDependent();
        Assert.assertTrue(result);
    }

    @Test
    public void testComputeValue() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }
            @Override
            public Object computeValue(EvalContext context) {
                return "test";
            }
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        Assert.assertEquals("test", result);
    }

    @Test
    public void testCompute() {
        Expression expression = new Expression() {
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
                return "test";
            }
        };

        EvalContext context = new EvalContext();
        Object result = expression.compute(context);
        Assert.assertEquals("test", result);
    }

    @Test
    public void testIterate() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(Collections.singletonList("test").iterator());

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("test", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(Collections.singletonList("test").iterator(), new QName(null, "value"), Locale.getDefault());

        Assert.assertFalse(iterator.hasNext());
    }
}