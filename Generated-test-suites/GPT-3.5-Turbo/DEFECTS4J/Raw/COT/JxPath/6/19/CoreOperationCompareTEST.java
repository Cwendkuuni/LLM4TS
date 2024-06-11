import org.junit.Assert;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqualWithNullValues() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        boolean result = coreOperationCompare.equal(context, null, null);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualWithDifferentDataTypes() {
        // Testing boolean values
        CoreOperationCompare coreOperationCompare1 = new CoreOperationCompare(null, null);
        EvalContext context1 = new EvalContext();
        boolean result1 = coreOperationCompare1.equal(context1, true, false);
        Assert.assertFalse(result1);

        // Testing number values
        CoreOperationCompare coreOperationCompare2 = new CoreOperationCompare(null, null);
        EvalContext context2 = new EvalContext();
        boolean result2 = coreOperationCompare2.equal(context2, 5, 5.0);
        Assert.assertTrue(result2);

        // Testing string values
        CoreOperationCompare coreOperationCompare3 = new CoreOperationCompare(null, null);
        EvalContext context3 = new EvalContext();
        boolean result3 = coreOperationCompare3.equal(context3, "test", "test");
        Assert.assertTrue(result3);
    }

    @Test
    public void testEqualWithCollections() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();

        // Testing Collections with equal values
        boolean result1 = coreOperationCompare.equal(context, createCollection(1, 2, 3), createCollection(1, 2, 3));
        Assert.assertTrue(result1);

        // Testing Collections with different values
        boolean result2 = coreOperationCompare.equal(context, createCollection(1, 2, 3), createCollection(4, 5, 6));
        Assert.assertFalse(result2);
    }

    @Test
    public void testEqualWithPointers() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();

        // Testing Pointers with equal values
        boolean result1 = coreOperationCompare.equal(context, createPointer(1), createPointer(1));
        Assert.assertTrue(result1);

        // Testing Pointers with different values
        boolean result2 = coreOperationCompare.equal(context, createPointer(1), createPointer(2));
        Assert.assertFalse(result2);
    }

    @Test
    public void testEqualWithEqualValues() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();

        // Testing equal values
        boolean result1 = coreOperationCompare.equal(context, 5, 5);
        Assert.assertTrue(result1);

        boolean result2 = coreOperationCompare.equal(context, "test", "test");
        Assert.assertTrue(result2);

        boolean result3 = coreOperationCompare.equal(context, true, true);
        Assert.assertTrue(result3);
    }

    private Collection<Object> createCollection(Object... values) {
        Collection<Object> collection = new ArrayList<>();
        for (Object value : values) {
            collection.add(value);
        }
        return collection;
    }

    private Pointer createPointer(Object value) {
        return new Pointer(value);
    }

}