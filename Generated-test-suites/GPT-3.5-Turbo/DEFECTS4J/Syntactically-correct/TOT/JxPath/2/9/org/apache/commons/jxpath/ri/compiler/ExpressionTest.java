import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.junit.Assert;
import org.junit.Test;
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

        Assert.assertTrue(expression.isContextDependent());
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
                return "testValue";
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext testContext = new EvalContext(null);
        Assert.assertEquals("testValue", expression.computeValue(testContext));
    }

    @Test
    public void testIterate() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return new String[] {"1", "2", "3"};
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext testContext = new EvalContext(null);
        Assert.assertTrue(expression.iterate(testContext).hasNext());
        Assert.assertEquals("1", expression.iterate(testContext).next());
    }

    @Test
    public void testIteratePointers() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return new String[] {"1", "2", "3"};
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext testContext = new EvalContext(null);
        Assert.assertTrue(expression.iteratePointers(testContext).hasNext());
        Object next = expression.iteratePointers(testContext).next();
        Assert.assertTrue(next instanceof Pointer);
        Assert.assertEquals("1", ((Pointer) next).getValue());
    }

    @Test
    public void testPointerIterator() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(null, null, Locale.ENGLISH);

        Assert.assertFalse(iterator.hasNext());

        iterator = new Expression.PointerIterator(new String[] {"1", "2", "3"}.iterator(), null, Locale.ENGLISH);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("1", iterator.next());
    }

    @Test
    public void testValueIterator() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(null);

        Assert.assertFalse(iterator.hasNext());

        iterator = new Expression.ValueIterator(new String[] {"1", "2", "3"}.iterator());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("1", iterator.next());
    }
}