import org.junit.Assert;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression left = new Expression();
        Expression right = new Expression();

        CoreOperationCompare coreCompare = new CoreOperationCompare(left, right);
   
        EvalContext context = new EvalContext();

        // Typical use case
        left.setComputeResult(10);
        right.setComputeResult(10);
        boolean result = coreCompare.equal(context, left, right);
        Assert.assertTrue(result);

        // Edge case with null values
        left.setComputeResult(null);
        right.setComputeResult(null);
        result = coreCompare.equal(context, left, right);
        Assert.assertTrue(result);

        // Error scenario with different types
        left.setComputeResult(10);
        right.setComputeResult("10");
        result = coreCompare.equal(context, left, right);
        Assert.assertFalse(result);
    }

    @Test
    public void testContains() {
        Expression element = new Expression();
        Iterator iterator = new Iterator();

        CoreOperationCompare coreCompare = new CoreOperationCompare(null, null);

        // Typical use case
        iterator.setNextItem(1);
        iterator.setNextItem(2);
        iterator.setNextItem(3);
        element.setComputeResult(2);
        boolean result = coreCompare.contains(iterator, element);
        Assert.assertTrue(result);

        // Edge case with empty iterator
        iterator = new Iterator();
        iterator.setNextItem(1);
        iterator.setNextItem(2);
        iterator.setNextItem(3);
        element.setComputeResult(4);
        result = coreCompare.contains(iterator, element);
        Assert.assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();

        CoreOperationCompare coreCompare = new CoreOperationCompare(null, null);

        // Typical use case
        lit.setNextItem(1);
        lit.setNextItem(2);
        lit.setNextItem(3);
        rit.setNextItem(3);
        rit.setNextItem(4);
        rit.setNextItem(5);
        boolean result = coreCompare.findMatch(lit, rit);
        Assert.assertTrue(result);

        // Edge case with empty iterators
        lit = new Iterator();
        rit = new Iterator();
        lit.setNextItem(1);
        lit.setNextItem(2);
        lit.setNextItem(3);
        rit.setNextItem(4);
        rit.setNextItem(5);
        result = coreCompare.findMatch(lit, rit);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualObjects() {
        CoreOperationCompare coreCompare = new CoreOperationCompare(null, null);

        // Typical use case with same objects
        boolean result = coreCompare.equal("test", "test");
        Assert.assertTrue(result);

        // Typical use case with different objects
        result = coreCompare.equal("test", "TEST");
        Assert.assertFalse(result);

        // Edge case with null objects
        result = coreCompare.equal(null, null);
        Assert.assertTrue(result);

        // Error scenario with different types
        result = coreCompare.equal("test", 10);
        Assert.assertFalse(result);
    }
}