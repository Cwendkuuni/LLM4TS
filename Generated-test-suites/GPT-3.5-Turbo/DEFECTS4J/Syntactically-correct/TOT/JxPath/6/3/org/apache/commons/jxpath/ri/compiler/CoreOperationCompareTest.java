import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;

    @Before
    public void setup() {
        Expression arg1 = new CoreOperation();
        Expression arg2 = new CoreOperation();
        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
    }

    @Test
    public void testEqual_BothNull_ReturnsTrue() {
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.equal(context, null, null));
    }

    @Test
    public void testEqual_BothSingleNodePointers_ReturnsTrue() {
        EvalContext context = new EvalContext();
        SelfContext selfContext1 = new SelfContext(new InitialContext());
        SelfContext selfContext2 = new SelfContext(new InitialContext());
        assertTrue(coreOperationCompare.equal(context, selfContext1, selfContext2));
    }

    @Test
    public void testEqual_LeftSingleNodePointer_RightCollection_ReturnsTrue() {
        EvalContext context = new EvalContext();
        SelfContext selfContext = new SelfContext(new InitialContext());
        Collection<Object> collection = new ArrayList<>();
        collection.add("test");
        assertTrue(coreOperationCompare.equal(context, selfContext, collection));
    }

    @Test
    public void testEqual_LeftCollection_RightSingleNodePointer_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Collection<Object> collection = new ArrayList<>();
        collection.add("test");
        SelfContext selfContext = new SelfContext(new InitialContext());
        assertTrue(coreOperationCompare.equal(context, collection, selfContext));
    }

    @Test
    public void testEqual_LeftIterator_RightSingleNodePointer_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Iterator<Object> iterator = List.of("test", "value").iterator();
        SelfContext selfContext = new SelfContext(new InitialContext());
        assertTrue(coreOperationCompare.equal(context, iterator, selfContext));
    }

    @Test
    public void testEqual_LeftSingleNodePointer_RightIterator_ReturnsTrue() {
        EvalContext context = new EvalContext();
        SelfContext selfContext = new SelfContext(new InitialContext());
        Iterator<Object> iterator = List.of("test", "value").iterator();
        assertTrue(coreOperationCompare.equal(context, selfContext, iterator));
    }

    @Test
    public void testEqual_LeftIterator_RightIterator_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Iterator<Object> iterator1 = List.of("test", "value").iterator();
        Iterator<Object> iterator2 = List.of("value", "test").iterator();
        assertTrue(coreOperationCompare.equal(context, iterator1, iterator2));
    }

    @Test
    public void testEqual_LeftNumber_RightNumber_ReturnsTrue() {
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.equal(context, 10, 10));
    }

    @Test
    public void testEqual_LeftBoolean_RightBoolean_ReturnsTrue() {
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.equal(context, true, true));
    }

    @Test
    public void testEqual_LeftString_RightString_ReturnsTrue() {
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.equal(context, "test", "test"));
    }

    @Test
    public void testEqual_LeftString_RightString_ReturnsFalse() {
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.equal(context, "test", "value"));
    }

    @Test
    public void testEqual_LeftObject_RightObject_ReturnsFalse() {
        EvalContext context = new EvalContext();
        Object object1 = new Object();
        Object object2 = new Object();
        assertFalse(coreOperationCompare.equal(context, object1, object2));
    }

    @Test
    public void testContains_EmptyIterator_ReturnsFalse() {
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.contains(new ArrayList<>().iterator(), "test"));
    }

    @Test
    public void testContains_IteratorWithDifferentValues_ReturnsFalse() {
        EvalContext context = new EvalContext();
        Iterator<Object> iterator = List.of("test1", "test2", "test3").iterator();
        assertFalse(coreOperationCompare.contains(iterator, "test"));
    }

    @Test
    public void testContains_IteratorWithMatchingValue_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Iterator<Object> iterator = List.of("test1", "test2", "test").iterator();
        assertTrue(coreOperationCompare.contains(iterator, "test"));
    }

    @Test
    public void testFindMatch_BothEmptyIterators_ReturnsFalse() {
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.findMatch(new ArrayList<>().iterator(), new ArrayList<>().iterator()));
    }

    @Test
    public void testFindMatch_LeftEmptyIterator_ReturnsFalse() {
        EvalContext context = new EvalContext();
        Iterator<Object> leftIterator = new ArrayList<>().iterator();
        Iterator<Object> rightIterator = List.of("test1", "test2", "test3").iterator();
        assertFalse(coreOperationCompare.findMatch(leftIterator, rightIterator));
    }

    @Test
    public void testFindMatch_RightEmptyIterator_ReturnsFalse() {
        EvalContext context = new EvalContext();
        Iterator<Object> leftIterator = List.of("test1", "test2", "test3").iterator();
        Iterator<Object> rightIterator = new ArrayList<>().iterator();
        assertFalse(coreOperationCompare.findMatch(leftIterator, rightIterator));
    }

    @Test
    public void testFindMatch_LeftIteratorWithMatchingValue_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Iterator<Object> leftIterator = List.of("test1", "test2", "test3").iterator();
        Iterator<Object> rightIterator = List.of("test4", "test5", "test3").iterator();
        assertTrue(coreOperationCompare.findMatch(leftIterator, rightIterator));
    }

    @Test
    public void testEqual_LeftPointerAndRightPointer_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Pointer pointer = new Pointer();
        assertTrue(coreOperationCompare.equal(context, pointer, pointer));
    }

    @Test
    public void testEqual_LeftPointerAndRightValue_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Pointer pointer = new Pointer();
        assertTrue(coreOperationCompare.equal(context, pointer, "test"));
    }

    @Test
    public void testEqual_LeftValueAndRightPointer_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Pointer pointer = new Pointer();
        assertTrue(coreOperationCompare.equal(context, "test", pointer));
    }

    @Test
    public void testEqual_LeftValueAndRightValue_ReturnsTrue() {
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.equal(context, "test1", "test1"));
    }

    @Test
    public void testEqual_LeftBooleanAndRightBoolean_ReturnsTrue() {
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.equal(context, true, false));
    }

    @Test
    public void testEqual_LeftNumberAndRightNumber_ReturnsTrue() {
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.equal(context, 10, 20));
    }

    @Test
    public void testEqual_LeftBooleanAndRightBoolean_ReturnsFalse() {
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.equal(context, true, false));
    }

    @Test
    public void testEqual_LeftNumberAndRightNumber_ReturnsFalse() {
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.equal(context, 10, 20));
    }

    @Test
    public void testEqual_LeftObjectAndRightObject_ReturnsFalse() {
        EvalContext context = new EvalContext();
        Object object1 = new Object();
        Object object2 = new Object();
        assertFalse(coreOperationCompare.equal(context, object1, object2));
    }

}