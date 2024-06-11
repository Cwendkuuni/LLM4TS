import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    private CoreOperationCompare operationCompare;

    @Test
    public void testEqual() {
        // Typical use case
        Expression left = new Expression();
        Expression right = new Expression();
        operationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        boolean result = operationCompare.equal(context, left, right);

        assertTrue(result);
    }

    @Test
    public void testEqual_WithNullValues() {
        Expression left = new Expression();
        Expression right = new Expression();
        operationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        // Edge case: both values are null
        Object l = null;
        Object r = null;
        boolean result = operationCompare.equal(context, l, r);

        assertTrue(result);
    }

    @Test
    public void testEqual_WithDifferentTypes() {
        Expression left = new Expression();
        Expression right = new Expression();
        operationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        // Edge case: comparing different types
        Object l = new Object();
        Object r = "test";
        boolean result = operationCompare.equal(context, l, r);

        assertFalse(result);
    }

    @Test
    public void testEqual_WithNaN() {
        Expression left = new Expression();
        Expression right = new Expression();
        operationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        // Edge case: comparing NaN values
        Object l = Double.NaN;
        Object r = Double.NaN;
        boolean result = operationCompare.equal(context, l, r);

        assertTrue(result);
    }

    @Test
    public void testContains() {
        Expression left = new Expression();
        Expression right = new Expression();
        operationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        // Typical use case
        Iterator it = new Iterator();
        Object value = new Object();
        boolean result = operationCompare.contains(it, value);

        assertFalse(result);
    }

    @Test
    public void testContains_WithEqualValue() {
        Expression left = new Expression();
        Expression right = new Expression();
        operationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        // Edge case: value exists in the iterator
        Iterator it = new Iterator();
        Object value = new Object();
        it.add(value);
        boolean result = operationCompare.contains(it, value);

        assertTrue(result);
    }

    @Test
    public void testFindMatch() {
        Expression left = new Expression();
        Expression right = new Expression();
        operationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        // Typical use case
        Iterator leftIterator = new Iterator();
        leftIterator.add(new Object());
        Iterator rightIterator = new Iterator();
        rightIterator.add(new Object());
        boolean result = operationCompare.findMatch(leftIterator, rightIterator);

        assertFalse(result);
    }

    @Test
    public void testFindMatch_WithEqualValue() {
        Expression left = new Expression();
        Expression right = new Expression();
        operationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        // Edge case: value exists in both iterators
        Iterator leftIterator = new Iterator();
        Object value = new Object();
        leftIterator.add(value);
        Iterator rightIterator = new Iterator();
        rightIterator.add(value);
        boolean result = operationCompare.findMatch(leftIterator, rightIterator);

        assertTrue(result);
    }

    @Test
    public void testEqual_ForPointerValues() {
        Expression left = new Expression();
        Expression right = new Expression();
        operationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        // Typical use case with Pointer values
        Pointer l = new Pointer();
        Pointer r = new Pointer();
        boolean result = operationCompare.equal(context, l, r);

        assertTrue(result);
    }

    @Test
    public void testEqual_ForPointerValues_WithDifferentValues() {
        Expression left = new Expression();
        Expression right = new Expression();
        operationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        // Edge case: different Pointer values
        Pointer l = new Pointer();
        l.setValue("test");
        Pointer r = new Pointer();
        r.setValue("different");
        boolean result = operationCompare.equal(context, l, r);

        assertFalse(result);
    }

    @Test
    public void testEqual_ForNumbers() {
        Expression left = new Expression();
        Expression right = new Expression();
        operationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        // Typical use case with Number values
        Object l = 10;
        Object r = 10;
        boolean result = operationCompare.equal(context, l, r);

        assertTrue(result);
    }

    @Test
    public void testEqual_ForNumbers_WithDifferentValues() {
        Expression left = new Expression();
        Expression right = new Expression();
        operationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        // Edge case: different Number values
        Object l = 10;
        Object r = 20;
        boolean result = operationCompare.equal(context, l, r);

        assertFalse(result);
    }

    @Test
    public void testEqual_ForStrings() {
        Expression left = new Expression();
        Expression right = new Expression();
        operationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        // Typical use case with String values
        Object l = "test";
        Object r = "test";
        boolean result = operationCompare.equal(context, l, r);

        assertTrue(result);
    }

    @Test
    public void testEqual_ForStrings_WithDifferentValues() {
        Expression left = new Expression();
        Expression right = new Expression();
        operationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        // Edge case: different String values
        Object l = "test";
        Object r = "different";
        boolean result = operationCompare.equal(context, l, r);

        assertFalse(result);
    }

    @Test
    public void testEqual_ForNullValue() {
        Expression left = new Expression();
        Expression right = new Expression();
        operationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        // Edge case: one value is null
        Object l = null;
        Object r = "test";
        boolean result = operationCompare.equal(context, l, r);

        assertFalse(result);
    }

    @Test
    public void testEqual_ForNonComparableValues() {
        Expression left = new Expression();
        Expression right = new Expression();
        operationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        // Edge case: non-comparable values
        Object l = new Object();
        Object r = new Object();
        boolean result = operationCompare.equal(context, l, r);

        assertFalse(result);
    }

}