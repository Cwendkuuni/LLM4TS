import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    private CoreOperationCompare operationCompare;

    @Before
    public void setup() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        operationCompare = new CoreOperationCompare(arg1, arg2);
    }

    @Test
    public void testEqual_NullValues_ReturnsTrue() {
        EvalContext context = new EvalContext();
        
        boolean result = operationCompare.equal(context, null, null);
        
        assertTrue(result);
    }

    @Test
    public void testEqual_BothValuesArePointers_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Pointer pointer1 = new Pointer();
        Pointer pointer2 = new Pointer();

        boolean result = operationCompare.equal(context, pointer1, pointer2);

        assertTrue(result);
    }

    @Test
    public void testEqual_LeftValueIsPointer_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Pointer pointer = new Pointer();
        Object value = new Object();

        boolean result = operationCompare.equal(context, pointer, value);

        assertTrue(result);
    }

    @Test
    public void testEqual_RightValueIsPointer_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Object value = new Object();
        Pointer pointer = new Pointer();

        boolean result = operationCompare.equal(context, value, pointer);

        assertTrue(result);
    }

    @Test
    public void testEqual_BothValuesAreIterators_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Iterator iterator1 = new Iterator();
        Iterator iterator2 = new Iterator();

        boolean result = operationCompare.equal(context, iterator1, iterator2);

        assertTrue(result);
    }

    @Test
    public void testEqual_LeftValueIsIterator_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Iterator iterator = new Iterator();
        Object value = new Object();

        boolean result = operationCompare.equal(context, iterator, value);

        assertTrue(result);
    }

    @Test
    public void testEqual_RightValueIsIterator_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Object value = new Object();
        Iterator iterator = new Iterator();

        boolean result = operationCompare.equal(context, value, iterator);

        assertTrue(result);
    }

    @Test
    public void testEqual_LeftValueIsIteratorAndRightValueIsNot_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Iterator iterator = new Iterator();
        Object value = new Object();

        boolean result = operationCompare.equal(context, iterator, value);

        assertTrue(result);
    }

    @Test
    public void testEqual_RightValueIsIteratorAndLeftValueIsNot_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Object value = new Object();
        Iterator iterator = new Iterator();

        boolean result = operationCompare.equal(context, value, iterator);

        assertTrue(result);
    }

    @Test
    public void testEqual_LeftValueIsEmptyIteratorAndRightValueIsNotEmpty_ReturnsFalse() {
        EvalContext context = new EvalContext();
        Iterator iterator = new Iterator();
        Object value = new Object();
        iterator.add(value);

        boolean result = operationCompare.equal(context, iterator, value);

        assertFalse(result);
    }

    @Test
    public void testEqual_LeftValueIsNotEmptyIteratorAndRightValueIsEmpty_ReturnsFalse() {
        EvalContext context = new EvalContext();
        Object value = new Object();
        Iterator iterator = new Iterator();
        iterator.add(value);

        boolean result = operationCompare.equal(context, value, iterator);

        assertFalse(result);
    }

    @Test
    public void testEqual_LeftValueIsNotEmptyIteratorAndRightValueIsNotInIterator_ReturnsFalse() {
        EvalContext context = new EvalContext();
        Iterator iterator = new Iterator();
        Object value1 = new Object();
        Object value2 = new Object();
        iterator.add(value1);

        boolean result = operationCompare.equal(context, iterator, value2);

        assertFalse(result);
    }

    @Test
    public void testEqual_LeftValueIsInIteratorAndRightValueIsNotEmptyIterator_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Iterator iterator = new Iterator();
        Object value1 = new Object();
        Object value2 = new Object();
        iterator.add(value1);

        boolean result = operationCompare.equal(context, value1, iterator);

        assertTrue(result);
    }

    @Test
    public void testEqual_BothNullOrEqualBooleans_ReturnsTrue() {
        EvalContext context = new EvalContext();
        boolean value1 = true;
        boolean value2 = true;

        boolean result = operationCompare.equal(context, value1, value2);

        assertTrue(result);
    }

    @Test
    public void testEqual_BothNullOrDifferentBooleans_ReturnsFalse() {
        EvalContext context = new EvalContext();
        boolean value1 = true;
        boolean value2 = false;

        boolean result = operationCompare.equal(context, value1, value2);

        assertFalse(result);
    }

    @Test
    public void testEqual_BothNullOrEqualNumbers_ReturnsTrue() {
        EvalContext context = new EvalContext();
        double value1 = 1.5;
        double value2 = 1.5;

        boolean result = operationCompare.equal(context, value1, value2);

        assertTrue(result);
    }

    @Test
    public void testEqual_BothNullOrDifferentNumbers_ReturnsFalse() {
        EvalContext context = new EvalContext();
        double value1 = 1.5;
        double value2 = 2.0;

        boolean result = operationCompare.equal(context, value1, value2);

        assertFalse(result);
    }

    @Test
    public void testEqual_BothNullOrEqualStrings_ReturnsTrue() {
        EvalContext context = new EvalContext();
        String value1 = "hello";
        String value2 = "hello";

        boolean result = operationCompare.equal(context, value1, value2);

        assertTrue(result);
    }

    @Test
    public void testEqual_BothNullOrDifferentStrings_ReturnsFalse() {
        EvalContext context = new EvalContext();
        String value1 = "hello";
        String value2 = "world";

        boolean result = operationCompare.equal(context, value1, value2);

        assertFalse(result);
    }

    @Test
    public void testEqual_BothNullOrEqualObjects_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Object value1 = new Object();
        Object value2 = new Object();

        boolean result = operationCompare.equal(context, value1, value2);

        assertTrue(result);
    }

    @Test
    public void testEqual_BothNullOrDifferentObjects_ReturnsFalse() {
        EvalContext context = new EvalContext();
        Object value1 = new Object();
        Object value2 = new Object();

        boolean result = operationCompare.equal(context, value1, value2);

        assertFalse(result);
    }

    @Test
    public void testEqual_BothNullOrEqualValues_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Object value1 = null;
        Object value2 = null;

        boolean result = operationCompare.equal(context, value1, value2);

        assertTrue(result);
    }

    @Test
    public void testEqual_BothNullOrDifferentValues_ReturnsFalse() {
        EvalContext context = new EvalContext();
        Object value1 = null;
        Object value2 = new Object();

        boolean result = operationCompare.equal(context, value1, value2);

        assertFalse(result);
    }

    @Test
    public void testEqual_LeftValueIsNotNullAndEqualToRightValue_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Object value1 = new Object();
        Object value2 = value1;

        boolean result = operationCompare.equal(context, value1, value2);

        assertTrue(result);
    }

    @Test
    public void testEqual_LeftValueIsNotNullAndDifferentFromRightValue_ReturnsFalse() {
        EvalContext context = new EvalContext();
        Object value1 = new Object();
        Object value2 = new Object();

        boolean result = operationCompare.equal(context, value1, value2);

        assertFalse(result);
    }

}