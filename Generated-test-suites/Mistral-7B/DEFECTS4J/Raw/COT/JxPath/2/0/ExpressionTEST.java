import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test class for Expression class.
 */
@RunWith(JUnit4.class)
public class ExpressionTest {

    private EvalContext context;

    @Before
    public void setUp() {
        context = mock(EvalContext.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIsContextDependent() {
        Expression expression = mock(Expression.class);
        when(expression.computeContextDependent()).thenReturn(true);

        assertTrue(expression.isContextDependent());
    }

    @Test
    public void testIsContextDependentWhenContextDependentIsFalse() {
        Expression expression = mock(Expression.class);
        when(expression.computeContextDependent()).thenReturn(false);

        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        Expression expression = mock(Expression.class);
        when(expression.computeValue(context)).thenReturn(1.0);

        assertEquals(1.0, expression.computeValue(context));
    }

    @Test
    public void testComputeValueWhenContextIsEvalContext() {
        Expression expression = mock(Expression.class);
        when(expression.computeValue(context)).thenReturn(context);

        assertSame(context, expression.computeValue(context));
    }

    @Test
    public void testComputeValueWhenContextIsNotEvalContext() {
        Expression expression = mock(Expression.class);
        when(expression.computeValue(context)).thenThrow(IllegalArgumentException.class);

        try {
            expression.computeValue(new Object() {});
            fail();
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testIterate() {
        Expression expression = mock(Expression.class);
        when(expression.iterate(context)).thenReturn(Collections.singletonList(new NodePointer("test")).iterator());

        Iterator<Pointer> iterator = expression.iterate(context);
        Pointer next = iterator.next();

        assertNotNull(next);
        assertEquals("test", next.getNodeName());
        assertNull(next.getValue());
        assertNull(next.getNamespaceURI());
    }

    @Test
    public void testIterateWhenNoResult() {
        Expression expression = mock(Expression.class);
        when(expression.iterate(context)).thenReturn(Collections.emptyIterator());

        Iterator<Pointer> iterator = expression.iterate(context);

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers() {
        Expression expression = mock(Expression.class);
        when(expression.iteratePointers(context)).thenReturn(Collections.singletonList(new NodePointer("test", 1.0, Locale.ENGLISH)).iterator());

        Iterator<Pointer> iterator = expression.iteratePointers(context);
        Pointer next = iterator.next();

        assertNotNull(next);
        assertEquals("test", next.getNodeName());
        assertEquals(1.0, next.getValue(), 0.001);
        assertEquals("", next.getNamespaceURI());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratePointersWhenNoResult() {
        Expression expression = mock(Expression.class);
        when(expression.iteratePointers(context)).thenReturn(Collections.emptyIterator());

        expression.iteratePointers(context);
    }
}