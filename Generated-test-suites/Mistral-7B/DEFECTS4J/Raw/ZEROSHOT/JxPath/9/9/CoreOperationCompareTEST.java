import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationCompareTest {

    @Mock
    private EvalContext context;

    @Mock
    private Expression arg1;

    @Mock
    private Expression arg2;

    @Mock
    private InitialContext initialContext;

    @Mock
    private SelfContext selfContext;

    private CoreOperationCompare coreOperationCompare;

    @Before
    public void setUp() {
        when(context.getSingleNodePointer()).thenReturn(selfContext);
        when(selfContext.getValue()).thenReturn(Collections.singletonList("test"));

        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
    }

    @Test
    public void testGetPrecedence() {
        assertTrue(coreOperationCompare.getPrecedence() == 2);
    }

    @Test
    public void testIsSymmetric() {
        assertTrue(coreOperationCompare.isSymmetric());
    }

    @Test
    public void testEqualWithInitialContexts() {
        when(arg1.compute(context)).thenReturn(initialContext);
        when(arg2.compute(context)).thenReturn(initialContext);

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithSelfContexts() {
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithIteratorsAndMatches() {
        when(arg1.compute(context)).thenReturn(Collections.singletonList("test1").iterator());
        when(arg2.compute(context)).thenReturn(Collections.singletonList("test1").iterator());

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithIteratorsAndDoesNotMatch() {
        when(arg1.compute(context)).thenReturn(Collections.singletonList("test1").iterator());
        when(arg2.compute(context)).thenReturn(Collections.singletonList("test2").iterator());

        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testContainsWithIteratorAndMatches() {
        when(arg1.compute(context)).thenReturn(Collections.singletonList("test1").iterator());
        when(arg2.compute(context)).thenReturn("test1");

        assertTrue(coreOperationCompare.contains((Iterator) arg1.compute(context), arg2));
    }

    @Test
    public void testContainsWithIteratorAndDoesNotMatch() {
        when(arg1.compute(context)).thenReturn(Collections.singletonList("test1").iterator());
        when(arg2.compute(context)).thenReturn("test2");

        assertFalse(coreOperationCompare.contains((Iterator) arg1.compute(context), arg2));
    }

    @Test
    public void testFindMatchWithIteratorsAndMatches() {
        when(arg1.compute(context)).thenReturn(Collections.singletonList("test1").iterator());
        when(arg2.compute(context)).thenReturn(Collections.singletonList("test1").iterator());

        assertTrue(coreOperationCompare.findMatch((Iterator) arg1.compute(context), (Iterator) arg2.compute(context)));
    }

    @Test
    public void testFindMatchWithIteratorsAndDoesNotMatch() {
        when(arg1.compute(context)).thenReturn(Collections.singletonList("test1").iterator());
        when(arg2.compute(context)).thenReturn(Collections.singletonList("test2").iterator());

        assertFalse(coreOperationCompare.findMatch((Iterator) arg1.compute(context), (Iterator) arg2.compute(context)));
    }

    @Test
    public void testEqualWithStrings() {
        when(arg1.compute(context)).thenReturn("test1");
        when(arg2.compute(context)).thenReturn("test1");

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithStringsAndDoesNotMatch() {
        when(arg1.compute(context)).thenReturn("test1");
        when(arg2.compute(context)).thenReturn("test2");

        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithPointers() {
        when(arg1.compute(context)).thenReturn(new Pointer("test1"));
        when(arg2.compute(context)).thenReturn(new Pointer("test1"));

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithPointersAndDoesNotMatch() {
        when(arg1.compute(context)).thenReturn(new Pointer("test1"));
        when(arg2.compute(context)).thenReturn(new Pointer("test2"));

        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithNulls() {
        when(arg1.compute(context)).thenReturn(null);
        when(arg2.compute(context)).thenReturn(null);

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithNullAndPointer() {
        when(arg1.compute(context)).thenReturn(null);
        when(arg2.compute(context)).thenReturn(new Pointer("test"));

        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithPointerAndNull() {
        when(arg1.compute(context)).thenReturn(new Pointer("test"));
        when(arg2.compute(context)).thenReturn(null);

        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }
}