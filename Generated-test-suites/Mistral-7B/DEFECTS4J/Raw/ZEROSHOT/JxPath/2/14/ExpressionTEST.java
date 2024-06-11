import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.RIUtil;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Literal;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test file for org.apache.commons.jxpath.ri.compiler.Expression class.
 */
public class ExpressionTest {

    private Expression expression;
    private EvalContext context;

    @Before
    public void setUp() {
        expression = new Literal(1);
        context = new EvalContext(RIUtil.createDocument());
    }

    @Test
    public void testIsContextDependent() {
        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testComputeContextDependent() {
        // A subclass should implement this method
        throw new UnsupportedOperationException();
    }

    @Test
    public void testComputeValue() {
        Object result = expression.computeValue(context);
        assertEquals(1, result.doubleValue());
    }

    @Test
    public void testCompute() {
        Object result = expression.compute(context);
        assertEquals(1, result.doubleValue());
    }

    @Test
    public void testIterate() {
        Iterator iter = expression.iterate(context);
        List list = new ArrayList();
        list.add(expression);
        list.add(context);
        for (Object o : list) {
            iter.next();
        }
        assertSame(context, iter.next());
        assertNull(iter.next());
    }

    @Test
    public void testIteratePointers() {
        Iterator iter = expression.iteratePointers(context);
        assertFalse(iter.hasNext());
    }
}