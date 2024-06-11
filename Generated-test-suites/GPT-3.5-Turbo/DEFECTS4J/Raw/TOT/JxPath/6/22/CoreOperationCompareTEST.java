import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    // Test cases for equal method
    @Test
    public void testEqual_NullValues_ReturnsTrue() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.equal(context, null, null));
    }

    @Test
    public void testEqual_BothExpressionsNull_ReturnsFalse() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.equal(context, new Expression(), new Expression()));
    }

    @Test
    public void testEqual_BothExpressionsNotNull_ReturnsTrue() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.equal(context, new Expression(), new Expression()));
    }
    
    @Test
    public void testEqual_LeftExpressionNull_ReturnsFalse() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.equal(context, null, new Expression()));
    }

    @Test
    public void testEqual_RightExpressionNull_ReturnsFalse() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.equal(context, new Expression(), null));
    }

    @Test
    public void testEqual_CollectionsWithDifferentValues_ReturnsFalse() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();

        Collection<String> coll1 = new ArrayList<>();
        coll1.add("value1");

        Collection<String> coll2 = new ArrayList<>();
        coll2.add("value2");

        assertFalse(coreOperationCompare.equal(context, coll1, coll2));
    }

    @Test
    public void testEqual_CollectionsWithSameValues_ReturnsTrue() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();

        Collection<String> coll1 = new ArrayList<>();
        coll1.add("value1");

        Collection<String> coll2 = new ArrayList<>();
        coll2.add("value1");

        assertTrue(coreOperationCompare.equal(context, coll1, coll2));
    }

    @Test
    public void testEqual_CollectionsWithDifferentOrder_ReturnsTrue() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();

        Collection<String> coll1 = new ArrayList<>();
        coll1.add("value1");
        coll1.add("value2");

        Collection<String> coll2 = new ArrayList<>();
        coll2.add("value2");
        coll2.add("value1");

        assertTrue(coreOperationCompare.equal(context, coll1, coll2));
    }

    @Test
    public void testEqual_CollectionsWithSameElementsInDifferentCollections_ReturnsTrue() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();

        Collection<String> coll1 = new ArrayList<>();
        coll1.add("value1");
        coll1.add("value1");

        Collection<String> coll2 = new HashSet<>();
        coll2.add("value1");
        coll2.add("value1");

        assertTrue(coreOperationCompare.equal(context, coll1, coll2));
    }

    @Test
    public void testEqual_CollectionsWithDifferentElements_ReturnsFalse() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();

        Collection<String> coll1 = new ArrayList<>();
        coll1.add("value1");

        Collection<String> coll2 = new ArrayList<>();
        coll2.add("value2");

        assertFalse(coreOperationCompare.equal(context, coll1, coll2));
    }

    // Add test cases for other methods

}