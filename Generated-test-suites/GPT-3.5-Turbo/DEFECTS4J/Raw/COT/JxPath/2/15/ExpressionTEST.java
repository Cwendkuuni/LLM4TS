import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.model.NodePointer;
import org.apache.commons.jxpath.ri.compiler.Expression;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

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

        boolean isContextDependent = expression.isContextDependent();
        Assert.assertFalse(isContextDependent);
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
                return "computed value";
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext context = new EvalContext();
        Object value = expression.computeValue(context);
        Assert.assertEquals("computed value", value);
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
                return "computed result";
            }
        };

        EvalContext context = new EvalContext();
        Object result = expression.compute(context);
        Assert.assertEquals("computed result", result);
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
        Assert.assertEquals("value", iterator.next());
        Assert.assertFalse(iterator.hasNext());
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
                return Collections.singletonList("value").iterator();
            }
        };

        EvalContext context = new EvalContext();
        Iterator iterator = expression.iteratePointers(context);
        Assert.assertFalse(iterator.hasNext());
    }
}