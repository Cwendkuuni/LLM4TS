import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        // Typical use case: Comparing two equal values
        EvalContext context = new EvalContext();
        Expression left = new Constant(5); // or any other value
        Expression right = new Constant(5); // or any other value
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        boolean result = operation.equal(context, left, right);
        Assert.assertTrue(result);
    }

    @Test
    public void testNotEqual() {
        // Typical use case: Comparing two unequal values
        EvalContext context = new EvalContext();
        Expression left = new Constant(5);
        Expression right = new Constant(10);
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        boolean result = operation.equal(context, left, right);
        Assert.assertFalse(result);
    }

    @Test
    public void testPointersEqual() {
        // Edge case: Comparing two equal pointer values
        EvalContext context = new EvalContext();
        Pointer pointer1 = new Pointer();
        Pointer pointer2 = new Pointer();
        Expression left = new Constant(pointer1);
        Expression right = new Constant(pointer2);
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        boolean result = operation.equal(context, left, right);
        Assert.assertTrue(result);
    }

    @Test
    public void testPointersNotEqual() {
        // Edge case: Comparing two unequal pointer values
        EvalContext context = new EvalContext();
        Pointer pointer1 = new Pointer();
        pointer1.setValue(5);
        Pointer pointer2 = new Pointer();
        pointer2.setValue(10);
        Expression left = new Constant(pointer1);
        Expression right = new Constant(pointer2);
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        boolean result = operation.equal(context, left, right);
        Assert.assertFalse(result);
    }

    @Test
    public void testBooleanValuesEqual() {
        // Edge case: Comparing two equal boolean values
        EvalContext context = new EvalContext();
        Expression left = new Constant(true);
        Expression right = new Constant(true);
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        boolean result = operation.equal(context, left, right);
        Assert.assertTrue(result);
    }

    @Test
    public void testBooleanValuesNotEqual() {
        // Edge case: Comparing two unequal boolean values
        EvalContext context = new EvalContext();
        Expression left = new Constant(false);
        Expression right = new Constant(true);
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        boolean result = operation.equal(context, left, right);
        Assert.assertFalse(result);
    }

    @Test
    public void testNumberValuesEqual() {
        // Edge case: Comparing two equal number values
        EvalContext context = new EvalContext();
        Expression left = new Constant(5);
        Expression right = new Constant(5);
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        boolean result = operation.equal(context, left, right);
        Assert.assertTrue(result);
    }

    @Test
    public void testNumberValuesNotEqual() {
        // Edge case: Comparing two unequal number values
        EvalContext context = new EvalContext();
        Expression left = new Constant(5);
        Expression right = new Constant(10);
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        boolean result = operation.equal(context, left, right);
        Assert.assertFalse(result);
    }

    @Test
    public void testStringValuesEqual() {
        // Edge case: Comparing two equal string values
        EvalContext context = new EvalContext();
        Expression left = new Constant("test");
        Expression right = new Constant("test");
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        boolean result = operation.equal(context, left, right);
        Assert.assertTrue(result);
    }

    @Test
    public void testStringValuesNotEqual() {
        // Edge case: Comparing two unequal string values
        EvalContext context = new EvalContext();
        Expression left = new Constant("test1");
        Expression right = new Constant("test2");
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        boolean result = operation.equal(context, left, right);
        Assert.assertFalse(result);
    }

    @Test
    public void testNullValuesEqual() {
        // Edge case: Comparing two equal null values
        EvalContext context = new EvalContext();
        Expression left = new Constant(null);
        Expression right = new Constant(null);
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        boolean result = operation.equal(context, left, right);
        Assert.assertTrue(result);
    }

    @Test
    public void testNullValuesNotEqual() {
        // Edge case: Comparing two unequal null values
        EvalContext context = new EvalContext();
        Expression left = new Constant(null);
        Expression right = new Constant(5);
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        boolean result = operation.equal(context, left, right);
        Assert.assertFalse(result);
    }

    @Test
    public void testInitialContextEqual() {
        // Edge case: Comparing two equal InitialContext values
        EvalContext left = new InitialContext();
        EvalContext right = new InitialContext();
        Expression leftExpression = new Constant(left);
        Expression rightExpression = new Constant(right);
        CoreOperationCompare operation = new CoreOperationCompare(leftExpression, rightExpression);
        boolean result = operation.equal(left, leftExpression, rightExpression);
        Assert.assertTrue(result);
    }

    @Test
    public void testInitialContextNotEqual() {
        // Edge case: Comparing two unequal InitialContext values
        EvalContext left = new InitialContext();
        EvalContext right = new InitialContext();
        Expression leftExpression = new Constant(left);
        Expression rightExpression = new Constant(right);
        CoreOperationCompare operation = new CoreOperationCompare(leftExpression, rightExpression);
        boolean result = operation.equal(left, leftExpression, rightExpression);
        Assert.assertFalse(result);
    }

    @Test
    public void testSelfContextEqual() {
        // Edge case: Comparing two equal SelfContext values
        EvalContext left = new SelfContext();
        EvalContext right = new SelfContext();
        Expression leftExpression = new Constant(left);
        Expression rightExpression = new Constant(right);
        CoreOperationCompare operation = new CoreOperationCompare(leftExpression, rightExpression);
        boolean result = operation.equal(left, leftExpression, rightExpression);
        Assert.assertTrue(result);
    }

    @Test
    public void testSelfContextNotEqual() {
        // Edge case: Comparing two unequal SelfContext values
        EvalContext left = new SelfContext();
        EvalContext right = new SelfContext();
        Expression leftExpression = new Constant(left);
        Expression rightExpression = new Constant(right);
        CoreOperationCompare operation = new CoreOperationCompare(leftExpression, rightExpression);
        boolean result = operation.equal(left, leftExpression, rightExpression);
        Assert.assertFalse(result);
    }

    @Test
    public void testCollectionValuesEqual() {
        // Edge case: Comparing two equal Collection values
        EvalContext context = new EvalContext();
        Collection<Integer> collection = new ArrayList<>();
        collection.add(5);
        Expression left = new Constant(collection);
        Expression right = new Constant(collection);
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        boolean result = operation.equal(context, left, right);
        Assert.assertTrue(result);
    }

    @Test
    public void testCollectionValuesNotEqual() {
        // Edge case: Comparing two unequal Collection values
        EvalContext context = new EvalContext();
        Collection<Integer> collection1 = new ArrayList<>();
        collection1.add(5);
        Collection<Integer> collection2 = new ArrayList<>();
        collection2.add(10);
        Expression left = new Constant(collection1);
        Expression right = new Constant(collection2);
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        boolean result = operation.equal(context, left, right);
        Assert.assertFalse(result);
    }

    @Test
    public void testIteratorAndNonIteratorEqual() {
        // Edge case: Comparing Iterator and non-Iterator values, where they are equal
        EvalContext context = new EvalContext();
        Collection<Integer> collection = new ArrayList<>();
        collection.add(5);
        Iterator<Integer> iterator = collection.iterator();
        Expression left = new Constant(iterator);
        Expression right = new Constant(5);
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        boolean result = operation.equal(context, left, right);
        Assert.assertTrue(result);
    }

    @Test
    public void testIteratorAndNonIteratorNotEqual() {
        // Edge case: Comparing Iterator and non-Iterator values, where they are unequal
        EvalContext context = new EvalContext();
        Collection<Integer> collection = new ArrayList<>();
        collection.add(5);
        Iterator<Integer> iterator = collection.iterator();
        Expression left = new Constant(iterator);
        Expression right = new Constant(10);
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        boolean result = operation.equal(context, left, right);
        Assert.assertFalse(result);
    }

    @Test
    public void testTwoIteratorsEqual() {
        // Edge case: Comparing two equal Iterator values
        EvalContext context = new EvalContext();
        Collection<Integer> collection = new ArrayList<>();
        collection.add(5);
        Iterator<Integer> iterator1 = collection.iterator();
        Iterator<Integer> iterator2 = collection.iterator();
        Expression left = new Constant(iterator1);
        Expression right = new Constant(iterator2);
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        boolean result = operation.equal(context, left, right);
        Assert.assertTrue(result);
    }

    @Test
    public void testTwoIteratorsNotEqual() {
        // Edge case: Comparing two unequal Iterator values
        EvalContext context = new EvalContext();
        Collection<Integer> collection1 = new ArrayList<>();
        collection1.add(5);
        Iterator<Integer> iterator1 = collection1.iterator();
        Collection<Integer> collection2 = new ArrayList<>();
        collection2.add(10);
        Iterator<Integer> iterator2 = collection2.iterator();
        Expression left = new Constant(iterator1);
        Expression right = new Constant(iterator2);
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        boolean result = operation.equal(context, left, right);
        Assert.assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        // Typical use case: findMatch method to check if values in one iterator can be found in another iterator
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        Collection<Integer> collection1 = new ArrayList<>();
        collection1.add(5);
        collection1.add(10);
        Iterator<Integer> iterator1 = collection1.iterator();
        Collection<Integer> collection2 = new ArrayList<>();
        collection2.add(15);
        collection2.add(5);
        collection2.add(20);
        Iterator<Integer> iterator2 = collection2.iterator();
        boolean result = operation.findMatch(iterator1, iterator2);
        Assert.assertTrue(result);
    }

    @Test
    public void testContains() {
        // Typical use case: contains method to check if iterable contains a specific value
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        Collection<Integer> collection = new ArrayList<>();
        collection.add(5);
        collection.add(10);
        collection.add(15);
        Iterator<Integer> iterator = collection.iterator();
        boolean result = operation.contains(iterator, 10);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualWithInvalidValues() {
        // Error scenario: Comparing invalid values
        EvalContext context = new EvalContext();
        Expression left = new Constant(new Object());
        Expression right = new Constant(new Object());
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        boolean result = operation.equal(context, left, right);
        Assert.assertFalse(result);
    }
}