import org.junit.Assert;
import org.junit.Test;
import java.util.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual_NullValues() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext(null);
        boolean result = coreOperationCompare.equal(context, null, null);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_PointerValues() {
        Pointer pointer1 = new Pointer();
        pointer1.setValue("value1");
        Pointer pointer2 = new Pointer();
        pointer2.setValue("value2");

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext(null);
        boolean result = coreOperationCompare.equal(context, pointer1, pointer2);
        
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_BooleanValues() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext(null);
        boolean result = coreOperationCompare.equal(context, true, false);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_NumericValues() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext(null);
        boolean result = coreOperationCompare.equal(context, 10, 20);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_StringValues() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext(null);
        boolean result = coreOperationCompare.equal(context, "Hello", "World");
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_CollectionValues() {
        List<String> list1 = new ArrayList<>();
        list1.add("value1");
        list1.add("value2");

        List<String> list2 = new ArrayList<>();
        list2.add("value1");
        list2.add("value2");

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext(null);
        boolean result = coreOperationCompare.equal(context, list1, list2);
        Assert.assertTrue(result);
    }

    @Test
    public void testContains_CollectionContainsElement() {
        List<String> list = new ArrayList<>();
        list.add("value1");
        list.add("value2");

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.contains(list.iterator(), "value1");
        Assert.assertTrue(result);
    }

    @Test
    public void testContains_CollectionDoesNotContainElement() {
        List<String> list = new ArrayList<>();
        list.add("value1");
        list.add("value2");

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.contains(list.iterator(), "value3");
        Assert.assertFalse(result);
    }

    @Test
    public void testContains_EmptyCollection() {
        List<String> list = new ArrayList<>();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.contains(list.iterator(), "value1");
        Assert.assertFalse(result);
    }

    @Test
    public void testFindMatch_BothCollectionsMatch() {
        List<String> list1 = new ArrayList<>();
        list1.add("value1");
        list1.add("value2");

        List<String> list2 = new ArrayList<>();
        list2.add("value2");
        list2.add("value1");

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.findMatch(list1.iterator(), list2.iterator());
        Assert.assertTrue(result);
    }

    @Test
    public void testFindMatch_CollectionsDoNotMatch() {
        List<String> list1 = new ArrayList<>();
        list1.add("value1");
        list1.add("value2");

        List<String> list2 = new ArrayList<>();
        list2.add("value3");
        list2.add("value4");

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.findMatch(list1.iterator(), list2.iterator());
        Assert.assertFalse(result);
    }

    @Test
    public void testFindMatch_OneCollectionIsEmpty() {
        List<String> list1 = new ArrayList<>();
        list1.add("value1");
        list1.add("value2");

        List<String> list2 = new ArrayList<>();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.findMatch(list1.iterator(), list2.iterator());
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_EqualValues() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.equal("value1", "value1");
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_UnequalValues() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.equal("value1", "value2");
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_NullValue() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.equal(null, "value1");
        Assert.assertFalse(result);
    }

}