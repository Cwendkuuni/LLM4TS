import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link CoreOperationCompare} abstract class.
 */
@RunWith(MockitoJUnitRunner.class)
public class CoreOperationCompareTest {

    /**
     * Test for equals method with two Boolean values.
     */
    @Test
    public void testBooleanEqual() {
        CoreOperationCompare compare = new CoreOperationCompare(
            Mockito.mock(Expression.class),
            Mockito.mock(Expression.class)
        );
        EvalContext context = Mockito.mock(EvalContext.class);
        Mockito.when(context.getSingleNodePointer()).thenReturn(new Pointer(true));

        assertTrue(compare.equal(context, Mockito.mock(Expression.class), Mockito.mock(Expression.class)));
    }

    /**
     * Test for equals method with two different Boolean values.
     */
    @Test
    public void testBooleanNotEqual() {
        CoreOperationCompare compare = new CoreOperationCompare(
            Mockito.mock(Expression.class),
            Mockito.mock(Expression.class)
        );
        EvalContext context = Mockito.mock(EvalContext.class);
        Mockito.when(context.getSingleNodePointer()).thenReturn(new Pointer(true));

        Mockito.when(context.getSingleNodePointer()).stub(ArgumentMatchers.anyInt()).thenReturn(new Pointer(false));

        assertFalse(compare.equal(context, Mockito.mock(Expression.class), Mockito.mock(Expression.class)));
    }

    /**
     * Test for equals method with two Number values.
     */
    @Test
    public void testNumberEqual() {
        CoreOperationCompare compare = new CoreOperationCompare(
            Mockito.mock(Expression.class),
            Mockito.mock(Expression.class)
        );
        EvalContext context = Mockito.mock(EvalContext.class);
        Mockito.when(context.getSingleNodePointer()).thenReturn(new Pointer(1.0));

        assertTrue(compare.equal(context, Mockito.mock(Expression.class), Mockito.mock(Expression.class)));
    }

    /**
     * Test for equals method with two different Number values.
     */
    @Test
    public void testNumberNotEqual() {
        CoreOperationCompare compare = new CoreOperationCompare(
            Mockito.mock(Expression.class),
            Mockito.mock(Expression.class)
        );
        EvalContext context = Mockito.mock(EvalContext.class);
        Mockito.when(context.getSingleNodePointer()).thenReturn(new Pointer(1.0));

        Mockito.when(context.getSingleNodePointer()).stub(ArgumentMatchers.anyInt()).thenReturn(new Pointer(2.0));

        assertFalse(compare.equal(context, Mockito.mock(Expression.class), Mockito.mock(Expression.class)));
    }

    /**
     * Test for equals method with two String values.
     */
    @Test
    public void testStringEqual() {
        CoreOperationCompare compare = new CoreOperationCompare(
            Mockito.mock(Expression.class),
            Mockito.mock(Expression.class)
        );
        EvalContext context = Mockito.mock(EvalContext.class);
        Mockito.when(context.getSingleNodePointer()).thenReturn(new Pointer("Hello"));

        assertTrue(compare.equal(context, Mockito.mock(Expression.class), Mockito.mock(Expression.class)));
    }

    /**
     * Test for equals method with two different String values.
     */
    @Test
    public void testStringNotEqual() {
        CoreOperationCompare compare = new CoreOperationCompare(
            Mockito.mock(Expression.class),
            Mockito.mock(Expression.class)
        );
        EvalContext context = Mockito.mock(EvalContext.class);
        Mockito.when(context.getSingleNodePointer()).thenReturn(new Pointer("Hello"));

        Mockito.when(context.getSingleNodePointer()).stub(ArgumentMatchers.anyInt()).thenReturn(new Pointer("World"));

        assertFalse(compare.equal(context, Mockito.mock(Expression.class), Mockito.mock(Expression.class)));
    }

    /**
     * Test for equals method with two Collection values.
     */
    @Test
    public void testCollectionsEqual() {
        CoreOperationCompare compare = new CoreOperationCompare(
            Mockito.mock(Expression.class),
            Mockito.mock(Expression.class)
        );
        EvalContext context = Mockito.mock(EvalContext.class);
        Mockito.when(context.getSingleNodePointer()).thenReturn(new Pointer(new HashSet(){{add(1); add(2);}}));

        assertTrue(compare.equal(context, Mockito.mock(Expression.class), Mockito.mock(Expression.class)));
    }

    /**
     * Test for equals method with two different Collection values.
     */
    @Test
    public void testCollectionsNotEqual() {
        CoreOperationCompare compare = new CoreOperationCompare(
            Mockito.mock(Expression.class),
            Mockito.mock(Expression.class)
        );
        EvalContext context = Mockito.mock(EvalContext.class);
        Mockito.when(context.getSingleNodePointer()).thenReturn(new Pointer(new HashSet(){{add(1); add(2);}}));

        Mockito.when(context.getSingleNodePointer()).stub(ArgumentMatchers.anyInt()).thenReturn(new Pointer(new HashSet(){{add(3); add(4);}}));

        assertFalse(compare.equal(context, Mockito.mock(Expression.class), Mockito.mock(Expression.class)));
    }

    /**
     * Test for contains method with Collection and Object values.
     */
    @Test
    public void testContainsCollectionAndObject() {
        CoreOperationCompare compare = new CoreOperationCompare(
            Mockito.mock(Expression.class),
            Mockito.mock(Expression.class)
        );
        EvalContext context = Mockito.mock(EvalContext.class);
        Mockito.when(context.getSingleNodePointer()).thenReturn(new InitialContext());

        assertTrue(compare.contains(context, Mockito.mock(Expression.class), Mockito.mock(Expression.class)));
    }

    /**
     * Test for contains method with Collection and Object values when collection does not contain the object.
     */
    @Test
    public void testContainsCollectionAndObjectFalse() {
        CoreOperationCompare compare = new CoreOperationCompare(
            Mockito.mock(Expression.class),
            Mockito.mock(Expression.class)
        );
        EvalContext context = Mockito.mock(EvalContext.class);
        Mockito.when(context.getSingleNodePointer()).thenReturn(new InitialContext());

        Mockito.when(context.getSingleNodePointer()).stub(ArgumentMatchers.anyInt()).thenReturn(new Pointer(new HashSet(){{add(1); add(2);}}));

        assertFalse(compare.contains(context, Mockito.mock(Expression.class), Mockito.mock(Expression.class)));
    }

    /**
     * Test for findMatch method with Iterator and Iterator values.
     */
    @Test
    public void testFindMatchIterators() {
        CoreOperationCompare compare = new CoreOperationCompare(
            Mockito.mock(Expression.class),
            Mockito.mock(Expression.class)
        );
        EvalContext context = Mockito.mock(EvalContext.class);
        Mockito.when(context.getSingleNodePointer()).thenReturn(new Pointer(new HashSet(){{add(1); add(2); add(3);}}));

        Iterator leftIterator = Mockito.mock(Iterator.class);
        Mockito.when(leftIterator.next()).thenReturn(1);
        Mockito.when(leftIterator.hasNext()).thenReturn(true);

        Iterator rightIterator = Mockito.mock(Iterator.class);
        Mockito.when(rightIterator.next()).thenReturn(2);
        Mockito.when(rightIterator.hasNext()).thenReturn(true);

        assertTrue(compare.findMatch(leftIterator, rightIterator));
    }

    /**
     * Test for findMatch method with Iterator and Iterator values when left iterator does not match right iterator.
     */
    @Test
    public void testFindMatchIteratorsFalse() {
        CoreOperationCompare compare = new CoreOperationCompare(
            Mockito.mock(Expression.class),
            Mockito.mock(Expression.class)
        );
        EvalContext context = Mockito.mock(EvalContext.class);
        Mockito.when(context.getSingleNodePointer()).thenReturn(new Pointer(new HashSet(){{add(1); add(2); add(3);}}));

        Iterator leftIterator = Mockito.mock(Iterator.class);
        Mockito.when(leftIterator.next()).thenReturn(3);
        Mockito.when(leftIterator.hasNext()).thenReturn(true);

        Iterator rightIterator = Mockito.mock(Iterator.class);
        Mockito.when(rightIterator.next()).thenReturn(2);
        Mockito.when(rightIterator.hasNext()).thenReturn(true);

        assertFalse(compare.findMatch(leftIterator, rightIterator));
    }
}