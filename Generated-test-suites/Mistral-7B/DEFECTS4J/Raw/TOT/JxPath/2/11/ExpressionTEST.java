import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.apache.commons.jxpath.ri.compiler.Expression.computeContextDependent;
import static org.apache.commons.jxpath.ri.compiler.Expression.computeValue;
import static org.apache.commons.jxpath.ri.compiler.Expression.compute;
import static org.apache.commons.jxpath.ri.compiler.Expression.ZERO;
import static org.apache.commons.jxpath.ri.compiler.Expression.ONE;
import static org.apache.commons.jxpath.ri.compiler.Expression.NOT_A_NUMBER;

/**
 * Test class for {@link Expression} abstract class.
 */
public class ExpressionTest {

    /**
     * Test for {@link Expression#isContextDependent()} method.
     */
    @Test
    public void testIsContextDependent() {
        AbstractExpression expression = new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return true;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return ZERO;
            }

            @Override
            public Object compute(EvalContext context) {
                return ZERO;
            }
        };

        assertTrue(expression.isContextDependent());

        AbstractExpression constantExpression = new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return ONE;
            }

            @Override
            public Object compute(EvalContext context) {
                return ONE;
            }
        };

        assertFalse(constantExpression.isContextDependent());
    }

    /**
     * Test for {@link Expression#computeValue(EvalContext)} method.
     */
    @Test
    public void testComputeValue() {
        AbstractExpression constantExpression = new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return ONE;
            }

            @Override
            public Object compute(EvalContext context) {
                return ONE;
            }
        };

        EvalContext context = new EvalContext(new NodePointer(null, "root"));
        Object result = constantExpression.computeValue(context);

        Assert.assertEquals(ONE, result);
    }

    /**
     * Test for {@link Expression#compute(EvalContext)} method.
     */
    @Test
    public void testCompute() {
        AbstractExpression constantExpression = new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return ONE;
            }

            @Override
            public Object compute(EvalContext context) {
                return ONE;
            }
        };

        EvalContext context = new EvalContext(new NodePointer(null, "root"));
        Object result = constantExpression.compute(context);

        Assert.assertEquals(ONE, result);
    }

    /**
     * Test for {@link Expression#iterate(EvalContext)} method.
     */
    @Test
    public void testIterate() {
        Iterator<Object> iterator = Collections.singletonList(new NodePointer(null, "node")).iterator();
        AbstractExpression expression = new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return iterator;
            }

            @Override
            public Object compute(EvalContext context) {
                return iterator;
            }

            @Override
            public Iterator iterate(EvalContext context) {
                return null;
            }
        };

        Iterator<Pointer> pointersIterator = expression.iteratePointers(new EvalContext(new NodePointer(null, "root")));

        assertNotNull(pointersIterator);
        assertTrue(pointersIterator.hasNext());
        Pointer pointer = pointersIterator.next();
        assertEquals("node", pointer.getLocalName());
        assertNull(pointer.getNamespaceURI());
        assertEquals(Locale.ENGLISH, pointer.getLocale());
        assertFalse(pointersIterator.hasNext());
    }

    /**
     * Test for {@link Expression#computeValue(EvalContext)} and {@link Iterator#next()} methods.
     */
    @Test
    public void testIterateValue() {
        Iterator<Object> iterator = new Iterator<Object>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < 3;
            }

            @Override
            public Object next() {
                return new NodePointer(null, "node" + index);
            }
        };

        AbstractExpression expression = new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return iterator;
            }

            @Override
            public Object compute(EvalContext context) {
                return iterator;
            }

            @Override
            public Iterator iterate(EvalContext context) {
                return new ValueIterator(iterator);
            }
        };

        EvalContext context = new EvalContext(new NodePointer(null, "root"));
        Iterator<Object> result = expression.iterate(context);

        assertNotNull(result);
        assertTrue(result.hasNext());
        Object node1 = result.next();
        assertEquals("node0", node1.getClass().getSimpleName());
        assertTrue(node1 instanceof NodePointer);
        Pointer pointer1 = (Pointer) node1;
        assertEquals("node0", pointer1.getNodeName());
        assertNull(pointer1.getNamespaceURI());
        assertEquals(Locale.ENGLISH, pointer1.getLocale());

        assertTrue(result.hasNext());
        Object node2 = result.next();
        assertEquals("node1", node2.getClass().getSimpleName());
        assertTrue(node2 instanceof NodePointer);
        Pointer pointer2 = (Pointer) node2;
        assertEquals("node1", pointer2.getNodeName());
        assertNull(pointer2.getNamespaceURI());
        assertEquals(Locale.ENGLISH, pointer2.getLocale());

        assertTrue(result.hasNext());
        Object node3 = result.next();
        assertEquals("node2", node3.getClass().getSimpleName());
        assertTrue(node3 instanceof NodePointer);
        Pointer pointer3 = (Pointer) node3;
        assertEquals("node2", pointer3.getNodeName());
        assertNull(pointer3.getNamespaceURI());
        assertEquals(Locale.ENGLISH, pointer3.getLocale());

        assertFalse(result.hasNext());
    }

    /**
     * Test for {@link Expression#compute(EvalContext)} method with exception.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testComputeException() {
        AbstractExpression expression = new AbstractExpression() {
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
                throw new UnsupportedOperationException();
            }
        };

        EvalContext context = new EvalContext(new NodePointer(null, "root"));
        expression.compute(context);
    }

    /**
     * Test for {@link Expression#computeValue(EvalContext)} method with exception.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testComputeValueException() {
        AbstractExpression expression = new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext context = new EvalContext(new NodePointer(null, "root"));
        expression.computeValue(context);
    }

    /**
     * Test for {@link Expression#iteratePointers(EvalContext)} method with null context.
     */
    @Test
    public void testIteratePointersNullContext() {
        AbstractExpression expression = new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return new NodePointer(null, "node");
            }

            @Override
            public Object compute(EvalContext context) {
                return new NodePointer(null, "node");
            }

            @Override
            public Iterator iteratePointers(EvalContext context) {
                throw new NullPointerException("context is null");
            }
        };

        Iterator<Pointer> result = expression.iteratePointers(null);

        assertNull(result);
    }

    /**
     * Test for {@link Expression#iteratePointers(EvalContext)} method with null result.
     */
    @Test
    public void testIteratePointersNullResult() {
        AbstractExpression expression = new AbstractExpression() {
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
                return new NodePointer(null, "node");
            }

            @Override
            public Iterator iteratePointers(EvalContext context) {
                return null;
            }
        };

        EvalContext context = new EvalContext(new NodePointer(null, "root"));
        Iterator<Pointer> result = expression.iteratePointers(context);

        assertNull(result);
    }

    /**
     * Test for {@link Expression#computeValue(EvalContext)} method with NaN.
     */
    @Test
    public void testComputeValueNaN() {
        AbstractExpression expression = new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return NOT_A_NUMBER;
            }

            @Override
            public Object compute(EvalContext context) {
                return NOT_A_NUMBER;
            }

            @Override
            public Iterator iterate(EvalContext context) {
                return ValueUtils.iterate(NOT_A_NUMBER);
            }

            @Override
            public Iterator iteratePointers(EvalContext context) {
                return new PointerIterator(ValueUtils.iterate(NOT_A_NUMBER),
                        new QName(null, "value"),
                        context.getRootContext().getCurrentNodePointer().getLocale());
            }
        };

        EvalContext context = new EvalContext(new NodePointer(null, "root"));
        Object result = expression.computeValue(context);

        assertEquals(NOT_A_NUMBER, result);
    }
}