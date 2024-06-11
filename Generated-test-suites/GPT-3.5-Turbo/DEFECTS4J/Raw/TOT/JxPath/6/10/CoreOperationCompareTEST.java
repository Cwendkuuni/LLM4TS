import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {
    private CoreOperationCompare coreOperationCompare;

    @Before
    public void setUp() {
        coreOperationCompare = new CoreOperationCompare(null, null);
    }

    @After
    public void tearDown() {
        coreOperationCompare = null;
    }

    @Test
    public void testEqual_BothNull_ReturnsTrue() {
        EvalContext context = null;
        Expression left = null;
        Expression right = null;

        boolean result = coreOperationCompare.equal(context, left, right);

        assertTrue(result);
    }

    @Test
    public void testEqual_LeftNullRightNotNull_ReturnsFalse() {
        EvalContext context = null;
        Expression left = null;
        Expression right = new Expression();

        boolean result = coreOperationCompare.equal(context, left, right);

        assertFalse(result);
    }

    @Test
    public void testEqual_LeftNotNullRightNull_ReturnsFalse() {
        EvalContext context = null;
        Expression left = new Expression();
        Expression right = null;

        boolean result = coreOperationCompare.equal(context, left, right);

        assertFalse(result);
    }

    @Test
    public void testEqual_LeftEqualsRight_ReturnsTrue() {
        EvalContext context = null;
        Expression left = new Expression("value");
        Expression right = new Expression("value");

        boolean result = coreOperationCompare.equal(context, left, right);

        assertTrue(result);
    }

    @Test
    public void testEqual_LeftNotEqualsRight_ReturnsFalse() {
        EvalContext context = null;
        Expression left = new Expression("value1");
        Expression right = new Expression("value2");

        boolean result = coreOperationCompare.equal(context, left, right);

        assertFalse(result);
    }

    @Test
    public void testContains_IteratorContainsValue_ReturnsTrue() {
        Iterator it = new ArrayList<String>().add("value1").add("value2").iterator();
        Object value = "value2";

        boolean result = coreOperationCompare.contains(it, value);

        assertTrue(result);
    }

    @Test
    public void testContains_IteratorDoesNotContainValue_ReturnsFalse() {
        Iterator it = new ArrayList<String>().add("value1").add("value2").iterator();
        Object value = "value3";

        boolean result = coreOperationCompare.contains(it, value);

        assertFalse(result);
    }

    @Test
    public void testFindMatch_IteratorsWithMatchingElements_ReturnsTrue() {
        Iterator lit = new ArrayList<String>().add("value1").add("value2").iterator();
        Iterator rit = new ArrayList<String>().add("value2").iterator();

        boolean result = coreOperationCompare.findMatch(lit, rit);

        assertTrue(result);
    }

    @Test
    public void testFindMatch_IteratorsWithoutMatchingElements_ReturnsFalse() {
        Iterator lit = new ArrayList<String>().add("value1").add("value2").iterator();
        Iterator rit = new ArrayList<String>().add("value3").iterator();

        boolean result = coreOperationCompare.findMatch(lit, rit);

        assertFalse(result);
    }

    @Test
    public void testEqual_PointersWithEqualValues_ReturnsTrue() {
        Object l = new Pointer("value");
        Object r = new Pointer("value");

        boolean result = coreOperationCompare.equal(l, r);

        assertTrue(result);
    }

    @Test
    public void testEqual_PointersWithDifferentValues_ReturnsFalse() {
        Object l = new Pointer("value1");
        Object r = new Pointer("value2");

        boolean result = coreOperationCompare.equal(l, r);

        assertFalse(result);
    }

    @Test
    public void testEqual_LeftPointerWithNullValue_ReturnsFalse() {
        Object l = new Pointer(null);
        Object r = new Pointer("value");

        boolean result = coreOperationCompare.equal(l, r);

        assertFalse(result);
    }

    @Test
    public void testEqual_RightPointerWithNullValue_ReturnsFalse() {
        Object l = new Pointer("value");
        Object r = new Pointer(null);

        boolean result = coreOperationCompare.equal(l, r);

        assertFalse(result);
    }

    @Test
    public void testEqual_BothValuesEqualToBoolean_True() {
        Object l = true;
        Object r = false;

        boolean result = coreOperationCompare.equal(l, r);

        assertFalse(result);
    }

    @Test
    public void testEqual_BothValuesNotEqualToBoolean_False() {
        Object l = true;
        Object r = "true";

        boolean result = coreOperationCompare.equal(l, r);

        assertFalse(result);
    }

    @Test
    public void testEqual_BothValuesEqualToNumber_True() {
        Object l = 10;
        Object r = 10.0;

        boolean result = coreOperationCompare.equal(l, r);

        assertTrue(result);
    }

    @Test
    public void testEqual_BothValuesNotEqualToNumber_False() {
        Object l = 10;
        Object r = "10";

        boolean result = coreOperationCompare.equal(l, r);

        assertFalse(result);
    }

    @Test
    public void testEqual_BothValuesEqualToString_True() {
        Object l = "value";
        Object r = "value";

        boolean result = coreOperationCompare.equal(l, r);

        assertTrue(result);
    }

    @Test
    public void testEqual_BothValuesNotEqualToString_False() {
        Object l = "value";
        Object r = "Value";

        boolean result = coreOperationCompare.equal(l, r);

        assertFalse(result);
    }

    @Test
    public void testEqual_LeftValueEqualToRightObject_True() {
        Object l = new Object();
        Object r = l;

        boolean result = coreOperationCompare.equal(l, r);

        assertTrue(result);
    }

    @Test
    public void testEqual_LeftValueNotEqualToRightObject_False() {
        Object l = new Object();
        Object r = new Object();

        boolean result = coreOperationCompare.equal(l, r);

        assertFalse(result);
    }
}