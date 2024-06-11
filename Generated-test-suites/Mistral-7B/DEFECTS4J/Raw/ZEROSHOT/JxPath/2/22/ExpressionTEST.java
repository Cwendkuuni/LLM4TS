import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ExpressionTest {

    private Expression expression;
    private EvalContext context;

    @Before
    public void setUp() {
        context = Mockito.mock(EvalContext.class);
        expression = Mockito.spy(new Expression() {
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
                return "result";
            }
        });
    }

    @Test
    public void testIsContextDependent() {
        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        Mockito.when(context.getRootContext()).thenReturn(Mockito.mock(NodePointer.class));
        Mockito.when(context.getRootContext().getCurrentNodePointer()).thenReturn(Mockito.mock(NodePointer.class));

        Object result = expression.computeValue(context);
        assertEquals("value", result);
    }

    @Test
    public void testCompute() {
        Mockito.when(context.getRootContext()).thenReturn(Mockito.mock(NodePointer.class));
        Mockito.when(context.getRootContext().getCurrentNodePointer()).thenReturn(Mockito.mock(NodePointer.class));

        Object result = expression.compute(context);
        assertEquals("result", result);
    }

    @Test
    public void testIterate() {
        Mockito.when(context.getRootContext()).thenReturn(Mockito.mock(NodePointer.class));
        Mockito.when(context.getRootContext().getCurrentNodePointer()).thenReturn(Mockito.mock(NodePointer.class));

        Iterator iterator = expression.iterate(context);
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals("value", iterator.next());
        assertFalse(iterator.hasNext());

        iterator = expression.iteratePointers(context);
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }
}