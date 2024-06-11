import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;

    @Before
    public void setUp() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
    }

    @Test
    public void testEqual_Literals_ReturnsTrue() {
        EvalContext context = getContext();
        Expression left = new Expression("value1");
        Expression right = new Expression("value1");

        boolean result = coreOperationCompare.equal(context, left, right);

        assertTrue(result);
    }

    @Test
    public void testEqual_NullAndNull_ReturnsTrue() {
        EvalContext context = getContext();
        Expression left = new Expression(null);
        Expression right = new Expression(null);

        boolean result = coreOperationCompare.equal(context, left, right);

        assertTrue(result);
    }

    @Test
    public void testEqual_CollectionAndElementInCollection_ReturnsTrue() {
        EvalContext context = getContext();
        Expression left = new Expression(getCollection());
        Expression right = new Expression("element1");

        boolean result = coreOperationCompare.equal(context, left, right);

        assertTrue(result);
    }

    @Test
    public void testEqual_CollectionAndElementNotInCollection_ReturnsFalse() {
        EvalContext context = getContext();
        Expression left = new Expression(getCollection());
        Expression right = new Expression("element4");

        boolean result = coreOperationCompare.equal(context, left, right);

        assertFalse(result);
    }

    @Test
    public void testEqual_IteratorAndElementInIterator_ReturnsTrue() {
        EvalContext context = getContext();
        Expression left = new Expression(getIterator());
        Expression right = new Expression("element1");

        boolean result = coreOperationCompare.equal(context, left, right);

        assertTrue(result);
    }

    @Test
    public void testEqual_IteratorAndElementNotInIterator_ReturnsFalse() {
        EvalContext context = getContext();
        Expression left = new Expression(getIterator());
        Expression right = new Expression("element4");

        boolean result = coreOperationCompare.equal(context, left, right);

        assertFalse(result);
    }

    @Test
    public void testEqual_TwoIteratorsWithSameElements_ReturnsTrue() {
        EvalContext context = getContext();
        Expression left = new Expression(getIterator());
        Expression right = new Expression(getIterator());

        boolean result = coreOperationCompare.equal(context, left, right);

        assertTrue(result);
    }

    @Test
    public void testEqual_PointersWithSameValue_ReturnsTrue() {
        EvalContext context = getContext();
        Expression left = new Expression(new Pointer("value1"));
        Expression right = new Expression(new Pointer("value1"));

        boolean result = coreOperationCompare.equal(context, left, right);

        assertTrue(result);
    }

    @Test
    public void testEqual_PointerAndValue_ReturnsTrue() {
        EvalContext context = getContext();
        Expression left = new Expression(new Pointer("value1"));
        Expression right = new Expression("value1");

        boolean result = coreOperationCompare.equal(context, left, right);

        assertTrue(result);
    }

    @Test
    public void testEqual_DifferentPointerValues_ReturnsFalse() {
        EvalContext context = getContext();
        Expression left = new Expression(new Pointer("value1"));
        Expression right = new Expression(new Pointer("value2"));

        boolean result = coreOperationCompare.equal(context, left, right);

        assertFalse(result);
    }

    private EvalContext getContext() {
        // Return the EvalContext needed for the tests
    }

    private Collection<String> getCollection() {
        // Return a collection with elements "element1", "element2", "element3"
    }

    private Iterator<String> getIterator() {
        // Return an iterator with elements "element1", "element2", "element3"
    }
}