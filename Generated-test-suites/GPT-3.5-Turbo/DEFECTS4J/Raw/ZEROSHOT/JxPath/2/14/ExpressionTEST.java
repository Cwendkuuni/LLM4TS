import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.util.ValueUtils;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.*;

public class ExpressionTest {

    private Expression expression;

    @Before
    public void setUp() {
        expression = new Expression() {
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
    }

    @Test
    public void testIsContextDependent() {
        boolean result = expression.isContextDependent();
        assertFalse(result);
    }

    @Test
    public void testComputeContextDependent() {
        boolean result = expression.computeContextDependent();
        assertFalse(result);
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testCompute() {
        EvalContext context = new EvalContext();
        Object result = expression.compute(context);
        assertNull(result);
    }

    @Test
    public void testIterate() {
        EvalContext context = new EvalContext();
        Iterator result = expression.iterate(context);
        assertNotNull(result);
    }

    @Test
    public void testIteratePointers() {
        EvalContext context = new EvalContext();
        Iterator result = expression.iteratePointers(context);
        assertNotNull(result);
    }

    @Test
    public void testPointerIterator() {
        Iterator iterator = Collections.emptyIterator();
        QName qname = new QName(null, "value");
        Locale locale = Locale.getDefault();
        Expression.PointerIterator pointerIterator = new Expression.PointerIterator(iterator, qname, locale);
        assertFalse(pointerIterator.hasNext());
    }

    @Test
    public void testValueIterator() {
        Iterator iterator = Collections.emptyIterator();
        Expression.ValueIterator valueIterator = new Expression.ValueIterator(iterator);
        assertFalse(valueIterator.hasNext());
    }
}