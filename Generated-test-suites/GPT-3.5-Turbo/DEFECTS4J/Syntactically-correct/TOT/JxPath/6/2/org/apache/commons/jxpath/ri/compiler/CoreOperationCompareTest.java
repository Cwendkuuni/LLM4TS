import org.junit.Assert;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual_NullValues() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        boolean result = operation.equal(context, null, null);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_BothNonNullEqualValues() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        boolean result = operation.equal(context, "abc", "abc");
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_BothNonNullDifferentValues() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        boolean result = operation.equal(context, "abc", "def");
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_LeftNullValue() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        boolean result = operation.equal(context, null, "abc");
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_RightNullValue() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        boolean result = operation.equal(context, "abc", null);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_LeftInitialContext() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        Object left = new InitialContext();
        Object right = "abc";
        boolean result = operation.equal(context, left, right);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_LeftSelfContext() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        Object left = new SelfContext();
        Object right = "abc";
        boolean result = operation.equal(context, left, right);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_RightInitialContext() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        Object left = "abc";
        Object right = new InitialContext();
        boolean result = operation.equal(context, left, right);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_RightSelfContext() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        Object left = "abc";
        Object right = new SelfContext();
        boolean result = operation.equal(context, left, right);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_LeftCollectionContainsValue() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        Object left = createIterator("abc");
        Object right = "abc";
        boolean result = operation.equal(context, left, right);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_LeftCollectionDoesNotContainValue() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        Object left = createIterator("def");
        Object right = "abc";
        boolean result = operation.equal(context, left, right);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_RightCollectionContainsValue() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        Object left = "abc";
        Object right = createIterator("abc");
        boolean result = operation.equal(context, left, right);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_RightCollectionDoesNotContainValue() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        Object left = "abc";
        Object right = createIterator("def");
        boolean result = operation.equal(context, left, right);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_LeftAndRightCollectionsMatch() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        Object left = createIterator("abc", "def");
        Object right = createIterator("def", "abc");
        boolean result = operation.equal(context, left, right);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_LeftAndRightCollectionsDoNotMatch() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        Object left = createIterator("abc", "def");
        Object right = createIterator("ghi", "jkl");
        boolean result = operation.equal(context, left, right);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_LeftPointerEqualToRightPointer() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        Object left = createPointer("abc");
        Object right = createPointer("abc");
        boolean result = operation.equal(context, left, right);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_LeftPointerNotEqualToRightPointer() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        Object left = createPointer("abc");
        Object right = createPointer("def");
        boolean result = operation.equal(context, left, right);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_LeftPointerEqualToValue() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        Object left = createPointer("abc");
        Object right = "abc";
        boolean result = operation.equal(context, left, right);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_LeftValueEqualToRightPointer() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        Object left = "abc";
        Object right = createPointer("abc");
        boolean result = operation.equal(context, left, right);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_NotEqualBooleanValues() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        boolean result = operation.equal(context, true, false);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_EqualBooleanValues() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        boolean result = operation.equal(context, true, true);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_NotEqualNumberValues() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        boolean result = operation.equal(context, 1, 2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_EqualNumberValues() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        boolean result = operation.equal(context, 1, 1);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_NotEqualStringValues() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        boolean result = operation.equal(context, "abc", "def");
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_EqualStringValues() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        boolean result = operation.equal(context, "abc", "abc");
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_NullValueAndNonNullValue() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        boolean result = operation.equal(context, null, "abc");
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_NonNullValueAndNullValue() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = createContext();
        boolean result = operation.equal(context, "abc", null);
        Assert.assertFalse(result);
    }

    private EvalContext createContext() {
        // TODO: Implement create context method for test cases
    }

    private Object createIterator(Object... values) {
        // TODO: Implement create iterator method for test cases
    }

    private Object createPointer(Object value) {
        // TODO: Implement create pointer method for test cases
    }
}