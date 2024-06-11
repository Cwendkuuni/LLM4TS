
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testConstructor() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertNotNull(coreOperationCompare);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        int precedence = coreOperationCompare.getPrecedence();
        assertEquals(2, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        boolean symmetric = coreOperationCompare.isSymmetric();
        assertTrue(symmetric);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullArguments() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
    }

    @Test
    public void testGetPrecedenceWithNullContext() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        EvalContext context = null;
        int precedence = coreOperationCompare.getPrecedence(context);
        assertNull(precedence);
    }

    @Test
    public void testIsSymmetricWithNullContext() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        EvalContext context = null;
        boolean symmetric = coreOperationCompare.isSymmetric(context);
        assertFalse(symmetric);
    }

    // Test different types
    @Test
    public void testEqualsWithDifferentTypes() {
        EvalContext context = new EvalContext();
        Expression left = new StringLiteralExpression("hello");
        Expression right = new NumberLiteralExpression(123);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        boolean result = coreOperationCompare.equal(context, left, right);
        assertFalse(result);
    }

    // Test empty collections
    @Test
    public void testContainsWithEmptyCollection() {
        EvalContext context = new EvalContext();
        Iterator iterator = new ArrayList<Object>().iterator();
        Object value = new StringLiteralExpression("world");
        boolean result = new CoreOperationCompare(iterator, value).contains(iterator, value);
        assertFalse(result);
    }

    // Test that findMatch correctly handles cases where one iterator has fewer elements than the other
    @Test
    public void testFindMatchWithUnequalSizeIterators() {
        EvalContext context = new EvalContext();
        Iterator lit = new ArrayList<Object>().iterator();
        Iterator rit = new ArrayList<Object>(Arrays.asList(1, 2, 3)).iterator();
        boolean result = new CoreOperationCompare(lit, rit).findMatch(lit, rit);
        assertFalse(result);
    }

    // Test that equals() correctly handles the case where both arguments are Iterators over the same collection
    @Test
    public void testEqualsWithSameIterator() {
        EvalContext context = new EvalContext();
        Iterator iterator = new ArrayList<Object>(Arrays.asList(1, 2, 3)).iterator();
        boolean result = new CoreOperationCompare(iterator, iterator).equal(context, iterator, iterator);
        assertTrue(result);
    }

    // Test that equals() correctly handles the case where both arguments are pointers to the same node
    @Test
    public void testEqualsWithSamePointer() {
        EvalContext context = new EvalContext();
        Pointer pointer1 = new Pointer(new String("hello"));
        Pointer pointer2 = pointer1;
        boolean result = new CoreOperationCompare(pointer1, pointer2).equal(context, pointer1, pointer2);
        assertTrue(result);
    }

    // Test that contains() correctly returns true when the collection contains the value
    @Test
    public void testContainsWithValueInCollection() {
        EvalContext context = new EvalContext();
        Iterator iterator = new ArrayList<Object>(Arrays.asList("hello", "world", "foo")).iterator();
        Object value = new StringLiteralExpression("world");
        boolean result = new CoreOperationCompare(iterator, value).contains(iterator, value);
        assertTrue(result);
    }

    // Test that contains() correctly returns false when the collection does not contain the value
    @Test
    public void testContainsWithValueNotInCollection() {
        EvalContext context = new EvalContext();
        Iterator iterator = new ArrayList<Object>(Arrays.asList("hello", "world", "foo")).iterator();
        Object value = new StringLiteralExpression("bar");
        boolean result = new CoreOperationCompare(iterator, value).contains(iterator, value);
        assertFalse(result);
    }

    // Test that equals() correctly compares different types, such as strings and numbers
    @Test
    public void testEqualsWithDifferentTypesStringAndNumber() {
        EvalContext context = new EvalContext();
        Expression left = new StringLiteralExpression("hello");
        Expression right = new NumberLiteralExpression(123);
        boolean result = new CoreOperationCompare(left, right).equal(context, left, right);
        assertFalse(result);
    }

}