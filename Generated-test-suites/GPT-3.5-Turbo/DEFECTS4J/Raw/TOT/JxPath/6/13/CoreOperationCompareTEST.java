import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {

    // Test cases for equal() method

    @Test
    public void testEqual_NullContext_ReturnsFalse() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = null;
        Expression left = null;
        Expression right = null;

        boolean result = coreOperationCompare.equal(context, left, right);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_NullValues_ReturnsTrue() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new InitialContext();
        Expression left = new SelfContext();
        Expression right = new SelfContext();

        boolean result = coreOperationCompare.equal(context, left, right);

        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_LAndREquals_ReturnsTrue() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new InitialContext();
        Expression left = new SelfContext();
        Expression right = new SelfContext();

        context.setSingleNodePointer(new Pointer("test1"));

        boolean result = coreOperationCompare.equal(context, left, right);

        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_LAndRNotEquals_ReturnsFalse() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new InitialContext();
        Expression left = new SelfContext();
        Expression right = new SelfContext();

        context.setSingleNodePointer(new Pointer("test1"));
        right.getSingleNodePointer().setValue("test2");

        boolean result = coreOperationCompare.equal(context, left, right);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_LeftIsCollectionAndContains_Right_ReturnsTrue() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new InitialContext();
        Expression left = new SelfContext();
        Expression right = new SelfContext();

        context.setValue(Arrays.asList("test1", "test2"));

        boolean result = coreOperationCompare.equal(context, left, right);

        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_LeftIsCollectionAndDoesNotContain_Right_ReturnsFalse() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new InitialContext();
        Expression left = new SelfContext();
        Expression right = new SelfContext();

        context.setValue(Arrays.asList("test1", "test2"));
        right.getSingleNodePointer().setValue("test3");

        boolean result = coreOperationCompare.equal(context, left, right);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_BothAreIteratorsAndHaveMatchingValues_ReturnsTrue() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new InitialContext();
        Expression left = new SelfContext();
        Expression right = new SelfContext();

        context.setValue(Arrays.asList("test1").iterator());
        right.getSingleNodePointer().setValue("test1");

        boolean result = coreOperationCompare.equal(context, left, right);

        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_BothAreIteratorsAndDoNotHaveMatchingValues_ReturnsFalse() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new InitialContext();
        Expression left = new SelfContext();
        Expression right = new SelfContext();

        context.setValue(Arrays.asList("test1").iterator());
        right.getSingleNodePointer().setValue("test2");

        boolean result = coreOperationCompare.equal(context, left, right);

        Assert.assertFalse(result);
    }

    // Test cases for contains() method

    @Test
    public void testContains_EmptyIteratorAndValue_ReturnsFalse() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        Iterator iterator = new HashSet<String>().iterator();
        Object value = "";

        boolean result = coreOperationCompare.contains(iterator, value);

        Assert.assertFalse(result);
    }

    @Test
    public void testContains_IteratorContainsValue_ReturnsTrue() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        Iterator iterator = Arrays.asList("test1", "test2").iterator();
        Object value = "test2";

        boolean result = coreOperationCompare.contains(iterator, value);

        Assert.assertTrue(result);
    }

    @Test
    public void testContains_IteratorDoesNotContainValue_ReturnsFalse() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        Iterator iterator = Arrays.asList("test1", "test2").iterator();
        Object value = "test3";

        boolean result = coreOperationCompare.contains(iterator, value);

        Assert.assertFalse(result);
    }

    // Test cases for findMatch() method

    @Test
    public void testFindMatch_EmptyIterators_ReturnsFalse() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        Iterator leftIterator = new HashSet<String>().iterator();
        Iterator rightIterator = new HashSet<String>().iterator();

        boolean result = coreOperationCompare.findMatch(leftIterator, rightIterator);

        Assert.assertFalse(result);
    }

    @Test
    public void testFindMatch_LeftIteratorContainsRightIteratorValues_ReturnsTrue() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        Iterator leftIterator = Arrays.asList("test1", "test2").iterator();
        Iterator rightIterator = Arrays.asList("test2").iterator();

        boolean result = coreOperationCompare.findMatch(leftIterator, rightIterator);

        Assert.assertTrue(result);
    }

    @Test
    public void testFindMatch_LeftIteratorDoesNotContainRightIteratorValues_ReturnsFalse() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        Iterator leftIterator = Arrays.asList("test1", "test2").iterator();
        Iterator rightIterator = Arrays.asList("test3").iterator();

        boolean result = coreOperationCompare.findMatch(leftIterator, rightIterator);

        Assert.assertFalse(result);
    }

    // Test cases for equal() method

    @Test
    public void testEqual_BooleanValues_ReturnsCorrectResult() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        boolean result1 = coreOperationCompare.equal(Boolean.TRUE, Boolean.TRUE);
        boolean result2 = coreOperationCompare.equal(Boolean.FALSE, Boolean.FALSE);
        boolean result3 = coreOperationCompare.equal(Boolean.TRUE, Boolean.FALSE);

        Assert.assertTrue(result1);
        Assert.assertTrue(result2);
        Assert.assertFalse(result3);
    }

    @Test
    public void testEqual_NumberValues_ReturnsCorrectResult() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        boolean result1 = coreOperationCompare.equal(1, 1);
        boolean result2 = coreOperationCompare.equal(1, 2);
        boolean result3 = coreOperationCompare.equal(1, 1.0);
        boolean result4 = coreOperationCompare.equal(1, 1.1);

        Assert.assertTrue(result1);
        Assert.assertFalse(result2);
        Assert.assertTrue(result3);
        Assert.assertFalse(result4);
    }

    @Test
    public void testEqual_StringValues_ReturnsCorrectResult() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        boolean result1 = coreOperationCompare.equal("test", "test");
        boolean result2 = coreOperationCompare.equal("test", "TEST");
        boolean result3 = coreOperationCompare.equal("test1", "test2");

        Assert.assertTrue(result1);
        Assert.assertFalse(result2);
        Assert.assertFalse(result3);
    }

    @Test
    public void testEqual_PointerValues_ReturnsCorrectResult() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        Pointer pointer1 = new Pointer("test");
        Pointer pointer2 = new Pointer("test");

        boolean result1 = coreOperationCompare.equal(pointer1, pointer2);

        Assert.assertTrue(result1);
    }

    @Test
    public void testEqual_NonEqualValues_ReturnsFalse() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        boolean result1 = coreOperationCompare.equal(null, 1);
        boolean result2 = coreOperationCompare.equal(1, null);
        boolean result3 = coreOperationCompare.equal(1, "test");
        boolean result4 = coreOperationCompare.equal("test", 1);

        Assert.assertFalse(result1);
        Assert.assertFalse(result2);
        Assert.assertFalse(result3);
        Assert.assertFalse(result4);
    }

    
}