import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CoreOperationCompareTest {

    private Expression left;
    private Expression right;

    public CoreOperationCompareTest() {
        left = new Expression();
        right = new Expression();
    }

    @Test
    public void testEqual_BothNodesPointingToNull_ReturnTrue() {
        EvalContext context = new EvalContext();
        
        boolean result = CoreOperationCompare.equal(context, left, right);

        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_BothNodesPointingToSameValue_ReturnTrue() {
        EvalContext context = new EvalContext();
        Object value = "test";
        left.compute(context);
        right.compute(context);
        context.setValue(value);

        boolean result = CoreOperationCompare.equal(context, left, right);

        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_BothNodesPointingToDifferentValues_ReturnFalse() {
        EvalContext context = new EvalContext();
        Object value1 = "test1";
        Object value2 = "test2";
        left.compute(context);
        right.compute(context);
        context.setValue(value1);

        boolean result = CoreOperationCompare.equal(context, left, right);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_LeftNodePointsToCollectionAndRightNodePointsToNull_ReturnFalse() {
        EvalContext context = new EvalContext();
        List<String> list = new ArrayList<>();
        list.add("value1");
        list.add("value2");
        left.compute(context);
        right.compute(context);
        context.setValue(list);

        boolean result = CoreOperationCompare.equal(context, left, right);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_RightNodePointsToCollectionAndLeftNodePointsToNull_ReturnFalse() {
        EvalContext context = new EvalContext();
        List<String> list = new ArrayList<>();
        list.add("value1");
        list.add("value2");
        left.compute(context);
        right.compute(context);
        context.setValue(list);

        boolean result = CoreOperationCompare.equal(context, right, left);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_BothNodesPointingToCollectionWithSameValues_ReturnTrue() {
        EvalContext context = new EvalContext();
        List<String> list1 = new ArrayList<>();
        list1.add("value1");
        list1.add("value2");
        List<String> list2 = new ArrayList<>();
        list2.add("value1");
        list2.add("value2");
        left.compute(context);
        right.compute(context);
        context.setValue(list1);

        boolean result = CoreOperationCompare.equal(context, left, right);

        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_BothNodesPointingToCollectionWithDifferentValues_ReturnFalse() {
        EvalContext context = new EvalContext();
        List<String> list1 = new ArrayList<>();
        list1.add("value1");
        list1.add("value2");
        List<String> list2 = new ArrayList<>();
        list2.add("value1");
        list2.add("value3");
        left.compute(context);
        right.compute(context);
        context.setValue(list1);

        boolean result = CoreOperationCompare.equal(context, left, right);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_LeftNodePointsToCollectionContainingRightNodeValue_ReturnTrue() {
        EvalContext context = new EvalContext();
        List<String> list = new ArrayList<>();
        list.add("value1");
        list.add("value2");
        left.compute(context);
        right.compute(context);
        context.setValue(list);

        boolean result = CoreOperationCompare.equal(context, left, right.compute(context));

        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_RightNodePointsToCollectionContainingLeftNodeValue_ReturnTrue() {
        EvalContext context = new EvalContext();
        List<String> list = new ArrayList<>();
        list.add("value1");
        list.add("value2");
        left.compute(context);
        right.compute(context);
        context.setValue(list);

        boolean result = CoreOperationCompare.equal(context, right.compute(context), left);

        Assert.assertTrue(result);
    }

    @Test
    public void testContains_IteratorContainsValue_ReturnTrue() {
        List<String> list = new ArrayList<>();
        list.add("value1");
        list.add("value2");
        Iterator<String> iterator = list.iterator();
        Object value = "value2";

        boolean result = CoreOperationCompare.contains(iterator, value);

        Assert.assertTrue(result);
    }

    @Test
    public void testContains_IteratorDoesNotContainValue_ReturnFalse() {
        List<String> list = new ArrayList<>();
        list.add("value1");
        list.add("value2");
        Iterator<String> iterator = list.iterator();
        Object value = "value3";

        boolean result = CoreOperationCompare.contains(iterator, value);

        Assert.assertFalse(result);
    }

    @Test
    public void testFindMatch_TwoIteratorsContainingIntersectingValues_ReturnTrue() {
        List<String> list1 = new ArrayList<>();
        list1.add("value1");
        list1.add("value2");
        List<String> list2 = new ArrayList<>();
        list2.add("value2");
        list2.add("value3");
        Iterator<String> iterator1 = list1.iterator();
        Iterator<String> iterator2 = list2.iterator();

        boolean result = CoreOperationCompare.findMatch(iterator1, iterator2);

        Assert.assertTrue(result);
    }

    @Test
    public void testFindMatch_TwoIteratorsNotContainingIntersectingValues_ReturnFalse() {
        List<String> list1 = new ArrayList<>();
        list1.add("value1");
        list1.add("value2");
        List<String> list2 = new ArrayList<>();
        list2.add("value3");
        list2.add("value4");
        Iterator<String> iterator1 = list1.iterator();
        Iterator<String> iterator2 = list2.iterator();

        boolean result = CoreOperationCompare.findMatch(iterator1, iterator2);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_TwoPointersContainingSameValue_ReturnTrue() {
        Pointer pointer1 = new Pointer();
        pointer1.setValue("test");
        Pointer pointer2 = new Pointer();
        pointer2.setValue("test");

        boolean result = CoreOperationCompare.equal(pointer1, pointer2);

        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_PointerValueMatchesObject_ReturnTrue() {
        Object value = "test";
        Pointer pointer = new Pointer();
        pointer.setValue(value);

        boolean result = CoreOperationCompare.equal(pointer, value);

        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_PointerValueDoesNotMatchObject_ReturnFalse() {
        Object value1 = "test1";
        Object value2 = "test2";
        Pointer pointer = new Pointer();
        pointer.setValue(value1);

        boolean result = CoreOperationCompare.equal(pointer, value2);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_BothValuesAreNull_ReturnTrue() {
        Object value = null;

        boolean result = CoreOperationCompare.equal(value, value);

        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_BothValuesAreBooleansAndEqual_ReturnTrue() {
        Object value1 = true;
        Object value2 = true;

        boolean result = CoreOperationCompare.equal(value1, value2);

        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_BothValuesAreBooleansAndNotEqual_ReturnFalse() {
        Object value1 = true;
        Object value2 = false;

        boolean result = CoreOperationCompare.equal(value1, value2);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_BothValuesAreNumbersAndEqual_ReturnTrue() {
        Object value1 = 1;
        Object value2 = 1.0;

        boolean result = CoreOperationCompare.equal(value1, value2);

        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_BothValuesAreNumbersAndNotEqual_ReturnFalse() {
        Object value1 = 1;
        Object value2 = 2.5;

        boolean result = CoreOperationCompare.equal(value1, value2);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_BothValuesAreStringsAndEqual_ReturnTrue() {
        Object value1 = "test";
        Object value2 = "test";

        boolean result = CoreOperationCompare.equal(value1, value2);

        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_BothValuesAreStringsAndNotEqual_ReturnFalse() {
        Object value1 = "test1";
        Object value2 = "test2";

        boolean result = CoreOperationCompare.equal(value1, value2);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_BothValuesAreDifferentTypes_ReturnFalse() {
        Object value1 = true;
        Object value2 = "test";

        boolean result = CoreOperationCompare.equal(value1, value2);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_OneValueIsNull_ReturnFalse() {
        Object value1 = "test";
        Object value2 = null;

        boolean result = CoreOperationCompare.equal(value1, value2);

        Assert.assertFalse(result);
    }

}