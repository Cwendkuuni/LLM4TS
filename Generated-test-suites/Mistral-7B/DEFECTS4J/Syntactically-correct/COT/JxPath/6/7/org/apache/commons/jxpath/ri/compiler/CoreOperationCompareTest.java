import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationCompareTest {

    private Expression arg1;
    private Expression arg2;
    private CoreOperationCompare coreOperationCompare;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        arg1 = Mockito.mock(Expression.class);
        arg2 = Mockito.mock(Expression.class);
        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
    }

    @Test
    public void testEqual() {
        EvalContext context = Mockito.mock(EvalContext.class);
        Object left = "test";
        Object right = "test";
        Mockito.when(arg1.compute(context)).thenReturn(left);
        Mockito.when(arg2.compute(context)).thenReturn(right);

        boolean result = coreOperationCompare.equal(context, arg1, arg2);
        assertTrue(result);
    }

    @Test
    public void testEqualNullLeft() {
        EvalContext context = Mockito.mock(EvalContext.class);
        Object left = null;
        Object right = "test";
        Mockito.when(arg1.compute(context)).thenReturn(left);
        Mockito.when(arg2.compute(context)).thenReturn(right);

        boolean result = coreOperationCompare.equal(context, arg1, arg2);
        assertFalse(result);
    }

    @Test
    public void testEqualNullRight() {
        EvalContext context = Mockito.mock(EvalContext.class);
        Object left = "test";
        Object right = null;
        Mockito.when(arg1.compute(context)).thenReturn(left);
        Mockito.when(arg2.compute(context)).thenReturn(right);

        boolean result = coreOperationCompare.equal(context, arg1, arg2);
        assertFalse(result);
    }

    @Test
    public void testEqualDifferentTypes() {
        EvalContext context = Mockito.mock(EvalContext.class);
        Object left = "test";
        Object right = 1;
        Mockito.when(arg1.compute(context)).thenReturn(left);
        Mockito.when(arg2.compute(context)).thenReturn(right);

        boolean result = coreOperationCompare.equal(context, arg1, arg2);
        assertFalse(result);
    }

    @Test
    public void testEqualSameIterators() {
        EvalContext context = Mockito.mock(EvalContext.class);
        Object left = new ArrayList() {{
            add("test1");
            add("test2");
        }};
        Object right = new ArrayList() {{
            add("test1");
            add("test2");
        }};
        Mockito.when(arg1.compute(context)).thenReturn(new Iterator() {
            @Override
            public boolean hasNext() {
                return ((List) left).hasNext();
            }

            @Override
            public Object next() {
                return ((List) left).next();
            }
        });
        Mockito.when(arg2.compute(context)).thenReturn(new Iterator() {
            @Override
            public boolean hasNext() {
                return ((List) right).hasNext();
            }

            @Override
            public Object next() {
                return ((List) right).next();
            }
        });

        boolean result = coreOperationCompare.equal(context, arg1, arg2);
        assertTrue(result);
    }

    @Test
    public void testEqualDifferentIterators() {
        EvalContext context = Mockito.mock(EvalContext.class);
        Object left = new ArrayList() {{
            add("test1");
            add("test2");
        }};
        Object right = new ArrayList() {{
            add("test3");
            add("test4");
        }};
        Mockito.when(arg1.compute(context)).thenReturn(new Iterator() {
            @Override
            public boolean hasNext() {
                return ((List) left).hasNext();
            }

            @Override
            public Object next() {
                return ((List) left).next();
            }
        });
        Mockito.when(arg2.compute(context)).thenReturn(new Iterator() {
            @Override
            public boolean hasNext() {
                return ((List) right).hasNext();
            }

            @Override
            public Object next() {
                return ((List) right).next();
            }
        });

        boolean result = coreOperationCompare.equal(context, arg1, arg2);
        assertFalse(result);
    }

    @Test
    public void testContains() {
        EvalContext context = Mockito.mock(EvalContext.class);
        Object value = "test";
        Collection collection = new ArrayList() {{
            add("test1");
            add("test2");
            add("test");
        }};
        Mockito.when(arg1.compute(context)).thenReturn(collection.iterator());

        boolean result = coreOperationCompare.contains(context, arg1, value);
        assertTrue(result);
    }

    @Test
    public void testContainsEmptyCollection() {
        EvalContext context = Mockito.mock(EvalContext.class);
        Object value = "test";
        Mockito.when(arg1.compute(context)).thenReturn(new ArrayList().iterator());

        boolean result = coreOperationCompare.contains(context, arg1, value);
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        EvalContext context = Mockito.mock(EvalContext.class);
        Object leftValue = "test1";
        Collection left = new ArrayList() {{
            add(leftValue);
            add("test2");
        }};
        Object rightValue = "test1";
        Collection right = new ArrayList() {{
            add("test3");
            add(rightValue);
            add("test4");
        }};
        Mockito.when(arg1.compute(context)).thenReturn(left.iterator());
        Mockito.when(arg2.compute(context)).thenReturn(right.iterator());

        boolean result = coreOperationCompare.findMatch(left.iterator(), right.iterator());
        assertTrue(result);
    }

    @Test
    public void testFindMatchEmptyLeft() {
        EvalContext context = Mockito.mock(EvalContext.class);
        Object leftValue = "test1";
        Collection left = new ArrayList();
        Mockito.when(arg1.compute(context)).thenReturn(left.iterator());
        Mockito.when(arg2.compute(context)).thenReturn(new ArrayList() {{
            add(leftValue);
            add("test2");
        }}.iterator());

        boolean result = coreOperationCompare.findMatch(left.iterator(), arg2.compute(context).iterator());
        assertFalse(result);
    }

    @Test
    public void testFindMatchEmptyRight() {
        EvalContext context = Mockito.mock(EvalContext.class);
        Object rightValue = "test1";
        Collection right = new ArrayList() {{
            add("test3");
        }};
        Mockito.when(arg1.compute(context)).thenReturn(new ArrayList() {{
            add("test1");
            add("test2");
        }}.iterator());
        Mockito.when(arg2.compute(context)).thenReturn(right.iterator());

        boolean result = coreOperationCompare.findMatch(arg1.compute(context).iterator(), right. iterator());
        assertFalse(result);
    }
}