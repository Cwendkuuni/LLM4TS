import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;
    private Expression arg1;
    private Expression arg2;
    private EvalContext context;

    @Before
    public void setup() {
        arg1 = Mockito.mock(Expression.class);
        arg2 = Mockito.mock(Expression.class);
        context = Mockito.spy(new EvalContext() {
            @Override
            public Object compute(Expression expr) {
                return null;
            }
        });

        coreOperationCompare = new CoreOperationCompare(arg1, arg2) {
            @Override
            protected boolean equal(EvalContext context, Expression left, Expression right) {
                return super.equal(context, left, right);
            }
        };
    }

    @Parameterized.Parameters(name = "{index}: arg1 = {0}, arg2 = {1}")
    public static Iterator<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {new InitialContext(), new InitialContext()},
                {new SelfContext(), new SelfContext()},
                {new Integer(1), new Integer(1)},
                {new Integer(1), new Integer(2)},
                {new String("test"), new String("test")},
                {new String("test"), new String("test1")},
                {new Boolean(true), new Boolean(true)},
                {new Boolean(true), new Boolean(false)},
                {new HashSet<>(Arrays.asList(1, 2, 3)), new HashSet<>(Arrays.asList(2, 3, 4))},
                {new ArrayList<>(Arrays.asList(1, 2, 3)), new ArrayList<>(Arrays.asList(2, 3, 4))},
                {new Pointer("path/to/node1"), new Pointer("path/to/node2")},
                {new Pointer("path/to/node1"), new Pointer("path/to/node1")},
                {new Pointer("path/to/node1"), null},
                {null, new Pointer("path/to/node1")},
                {new Pointer("path/to/node1"), new Pointer("path/to/node1")},
                {new Pointer("path/to/node1"), new Pointer("path/to/node2")}
        }).iterator();
    }

    @Test
    public void testEqual() {
        Mockito.when(arg1.compute(context)).thenReturn(1);
        Mockito.when(arg2.compute(context)).thenReturn(1);
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testNotEqual() {
        Mockito.when(arg1.compute(context)).thenReturn(1);
        Mockito.when(arg2.compute(context)).thenReturn(2);
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testCollectionEqual() {
        Collection coll1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        Collection coll2 = new ArrayList<>(Arrays.asList(2, 3, 4));
        Mockito.when(arg1.compute(context)).thenReturn(coll1);
        Mockito.when(arg2.compute(context)).thenReturn(coll2);
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));

        coll1 = new HashSet<>(Arrays.asList(1, 2, 3));
        coll2 = new HashSet<>(Arrays.asList(2, 3, 4));
        Mockito.when(arg1.compute(context)).thenReturn(coll1);
        Mockito.when(arg2.compute(context)).thenReturn(coll2);
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));

        coll1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        coll2 = new ArrayList<>(Arrays.asList(1, 2, 3));
        Mockito.when(arg1.compute(context)).thenReturn(coll1);
        Mockito.when(arg2.compute(context)).thenReturn(coll2);
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));

        coll1 = new HashSet<>(Arrays.asList(1, 2, 3));
        coll2 = new HashSet<>(Arrays.asList(1, 2, 3));
        Mockito.when(arg1.compute(context)).thenReturn(coll1);
        Mockito.when(arg2.compute(context)).thenReturn(coll2);
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testPointerEqual() {
        Pointer pointer1 = new Pointer("path/to/node1");
        Pointer pointer2 = new Pointer("path/to/node2");
        Mockito.when(arg1.compute(context)).thenReturn(pointer1);
        Mockito.when(arg2.compute(context)).thenReturn(pointer2);
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));

        pointer1 = new Pointer("path/to/node1");
        pointer2 = new Pointer("path/to/node1");
        Mockito.when(arg1.compute(context)).thenReturn(pointer1);
        Mockito.when(arg2.compute(context)).thenReturn(pointer2);
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));

        pointer1 = new Pointer("path/to/node1");
        Mockito.when(arg1.compute(context)).thenReturn(pointer1);
        Mockito.when(arg2.compute(context)).thenReturn(null);
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));

        Mockito.when(arg1.compute(context)).thenReturn(null);
        Mockito.when(arg2.compute(context)).thenReturn(new Pointer("path/to/node1"));
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));

        pointer1 = new Pointer("path/to/node1");
        Mockito.when(arg1.compute(context)).thenReturn(pointer1);
        Mockito.when(arg2.compute(context)).thenReturn(pointer1);
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));

        pointer1 = new Pointer("path/to/node1");
        Mockito.when(arg1.compute(context)).thenReturn(pointer1);
        Mockito.when(arg2.compute(context)).thenReturn(new Pointer("path/to/node2"));
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }
}