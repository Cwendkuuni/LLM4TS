import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.util.ValueUtils;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return true;
            }

            public Object computeValue(EvalContext context) {
                return null;
            }

            public Object compute(EvalContext context) {
                return null;
            }
        };

        boolean result = expression.isContextDependent();

        Assert.assertEquals(true, result);
    }

    @Test
    public void testIterate() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }

            public Object computeValue(EvalContext context) {
                return null;
            }

            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext context = new EvalContext();

        Iterator iterator = expression.iterate(context);

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }

            public Object computeValue(EvalContext context) {
                return null;
            }

            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext context = new EvalContext();

        Iterator iterator = expression.iteratePointers(context);

        Assert.assertFalse(iterator.hasNext());
    }
}