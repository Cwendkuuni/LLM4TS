import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;
import java.util.Iterator;
import java.util.Locale;
import java.util.Collections;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
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
                return null;
            }
        };

        assert !expression.isContextDependent();
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
                return "value";
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);

        assert result.equals("value");
    }

    @Test
    public void testComputeValue_NullResult() {
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
                return null;
            }
        };

        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);

        assert result == null;
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
                return "result";
            }
        };

        EvalContext context = new EvalContext();
        Object result = expression.compute(context);

        assert result.equals("result");
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
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
              return Collections.singletonList("value").iterator();
            }
        };

        EvalContext context = new EvalContext();
        Iterator iterator = expression.iterate(context);
        Object result = iterator.next();

        assert result.equals("value");
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
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                return new PointerIterator(Collections.singletonList("value").iterator(), new QName(null, "value"), Locale.getDefault());
            }
        };

        EvalContext context = new EvalContext();
        Iterator iterator = expression.iteratePointers(context);
        Object result = iterator.next();

        assert result instanceof NodePointer;
    }

    @Test
    public void testIteratePointers_NullResult() {
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
                return null;
            }
        };

        EvalContext context = new EvalContext();
        Iterator iterator = expression.iteratePointers(context);

        assert !iterator.hasNext();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPointerIteratorRemove() {
        PointerIterator iterator = new PointerIterator(Collections.singletonList("value").iterator(), new QName(null, "value"), Locale.getDefault());
        iterator.remove();
    }

    @Test
    public void testValueIterator() {
        ValueIterator iterator = new ValueIterator(Collections.singletonList(new Pointer() {
            @Override
            public Object getValue() {
                return "value";
            }

            @Override
            public Object setValue(Object value) {
                return null;
            }

            @Override
            public String asPath() {
                return null;
            }

            @Override
            public Pointer getParent() {
                return null;
            }

            @Override
            public Object getNode() {
                return null;
            }

            @Override
            public NodePointer getValuePointer() {
                return null;
            }

            @Override
            public Object getBaseValue() {
                return null;
            }
        }).iterator());

        Object result = iterator.next();

        assert result.equals("value");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueIteratorRemove() {
        ValueIterator iterator = new ValueIterator(Collections.singletonList("value").iterator());
        iterator.remove();
    }
}