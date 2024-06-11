import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExpressionTest {

    private Expression expression;

    @Mock
    private EvalContext context;

    @Before
    public void setup() {
        expression = mock(Expression.class);
    }

    @Test
    public void testIsContextDependent_default() {
        when(expression.computeContextDependent()).thenCallRealMethod();
        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testIsContextDependent_contextDependent() {
        when(expression.computeContextDependent()).thenReturn(true);
        assertTrue(expression.isContextDependent());
    }

    @Test
    public void testIsContextDependent_contextIndependent() {
        when(expression.computeContextDependent()).thenReturn(false);
        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testComputeValue_returnsNull() {
        when(expression.computeValue(context)).thenReturn(null);
        assertNull(expression.computeValue(context));
    }

    @Test
    public void testComputeValue_returnsNumber() {
        double value = 12.3;
        when(expression.computeValue(context)).thenReturn(value);
        assertEquals(value, expression.computeValue(context), 0.001);
    }

    @Test
    public void testComputeValue_returnsNodePointer() {
        NodePointer nodePointer = mock(NodePointer.class);
        when(expression.computeValue(context)).thenReturn(nodePointer);
        assertSame(nodePointer, expression.computeValue(context));
    }

    @Test
    public void testCompute_returnsNull() {
        when(expression.compute(context)).thenReturn(null);
        assertNull(expression.compute(context));
    }

    @Test
    public void testCompute_returnsNumber() {
        double value = 12.3;
        when(expression.compute(context)).thenReturn(value);
        assertEquals(value, expression.compute(context), 0.001);
    }

    @Test
    public void testCompute_returnsNodeSet() {
        NodePointer nodePointer1 = mock(NodePointer.class);
        NodePointer nodePointer2 = mock(NodePointer.class);
        Iterator<NodePointer> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(true).thenReturn(false);
        when(iterator.next()).thenReturn(nodePointer1).thenReturn(nodePointer2);

        when(expression.compute(context)).thenReturn(iterator);
        assertSame(nodePointer1, expression.compute(context).next());
        assertSame(nodePointer2, expression.compute(context).next());
        assertNull(expression.compute(context).hasNext());
    }

    @Test
    public void testIterate_returnsEmptyIterator() {
        when(expression.compute(context)).thenReturn(null);
        Iterator<NodePointer> iterator = expression.iterate(context);
        assertNull(iterator.hasNext());
    }

    @Test
    public void testIterate_returnsNodePointerIterator() {
        NodePointer nodePointer1 = mock(NodePointer.class);
        NodePointer nodePointer2 = mock(NodePointer.class);
        Iterator<NodePointer> nodePointerIterator = mock(Iterator.class);
        when(nodePointerIterator.hasNext()).thenReturn(true).thenReturn(false);
        when(nodePointerIterator.next()).thenReturn(nodePointer1).thenReturn(nodePointer2);

        when(expression.compute(context)).thenReturn(nodePointerIterator);
        Iterator<NodePointer> resultIterator = expression.iterate(context);
        assertSame(nodePointer1, resultIterator.next());
        assertSame(nodePointer2, resultIterator.next());
        assertNull(resultIterator.hasNext());
    }

    @Test
    public void testIteratePointers_returnsEmptyIterator() {
        when(expression.compute(context)).thenReturn(null);
        Iterator<Pointer> resultIterator = expression.iteratePointers(context);
        assertNull(resultIterator.hasNext());
    }

    @Test
    public void testIteratePointers_returnsPointerIterator() {
        NodePointer nodePointer = mock(NodePointer.class);
        Iterator<Pointer> pointerIterator = mock(Iterator.class);
        when(pointerIterator.hasNext()).thenReturn(true).thenReturn(false);
        when(pointerIterator.next()).thenReturn(new Pointer(nodePointer, null));

        when(expression.compute(context)).thenReturn(pointerIterator);
        Iterator<Pointer> resultIterator = expression.iteratePointers(context);
        assertSame(new Pointer(nodePointer, null), resultIterator.next());
        assertNull(resultIterator.hasNext());
    }
}