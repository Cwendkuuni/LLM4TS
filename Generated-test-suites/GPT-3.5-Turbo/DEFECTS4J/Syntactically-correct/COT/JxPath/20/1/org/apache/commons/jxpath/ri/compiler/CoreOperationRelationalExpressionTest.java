import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Test with different input types and values
        Assert.assertTrue(CoreOperationRelationalExpression.computeValue(new EvalContext("test")) instanceof Boolean);
        
        // Test with equal values
        Assert.assertEquals(Boolean.FALSE, CoreOperationRelationalExpression.computeValue(new EvalContext("test", "test")));
        
        // Test with greater than and less than values
        Assert.assertEquals(Boolean.TRUE, CoreOperationRelationalExpression.computeValue(new EvalContext(5, 3)));
        
        // Test with null input values
        Assert.assertEquals(Boolean.FALSE, CoreOperationRelationalExpression.computeValue(new EvalContext(null, "test")));
        
        // Test with NaN values
        Assert.assertEquals(Boolean.FALSE, CoreOperationRelationalExpression.computeValue(new EvalContext(Double.NaN, Double.NaN)));
    }

    @Test
    public void testGetPrecedence() {
        Assert.assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, CoreOperationRelationalExpression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Assert.assertFalse(CoreOperationRelationalExpression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        // Test with compare values of 0, positive, and negative integers
        Assert.assertTrue(CoreOperationRelationalExpression.evaluateCompare(0));
        Assert.assertTrue(CoreOperationRelationalExpression.evaluateCompare(5));
        Assert.assertFalse(CoreOperationRelationalExpression.evaluateCompare(-5));
    }

    @Test
    public void testCompute() {
        // Test with different combinations of types and values for left and right operands
        Assert.assertTrue(CoreOperationRelationalExpression.compute(5, 3));
        Assert.assertFalse(CoreOperationRelationalExpression.compute(3, 5));
        Assert.assertFalse(CoreOperationRelationalExpression.compute(new HashSet<>(Arrays.asList(1, 2, 3)), 5));
        Assert.assertFalse(CoreOperationRelationalExpression.compute(5, new HashSet<>(Arrays.asList(1, 2, 3))));
        
        Iterator<Integer> emptyIterator = new HashSet<Integer>().iterator();
        Iterator<Integer> matchIterator = new HashSet<>(Arrays.asList(1, 2, 3)).iterator();
        
        Assert.assertFalse(CoreOperationRelationalExpression.compute(emptyIterator, 5));
        Assert.assertTrue(CoreOperationRelationalExpression.compute(matchIterator, 2));
        
        Assert.assertFalse(CoreOperationRelationalExpression.compute(emptyIterator, matchIterator));
        Assert.assertTrue(CoreOperationRelationalExpression.compute(matchIterator, emptyIterator));
    }

    @Test
    public void testContainsMatch() {
        Iterator<Integer> emptyIterator = new HashSet<Integer>().iterator();
        Iterator<Integer> matchIterator = new HashSet<>(Arrays.asList(1, 2, 3)).iterator();
        
        // Test with an empty iterator
        Assert.assertFalse(CoreOperationRelationalExpression.containsMatch(emptyIterator, 2));
        
        // Test with an iterator that contains a match
        Assert.assertTrue(CoreOperationRelationalExpression.containsMatch(matchIterator, 2));
        
        // Test with an iterator that does not contain a match
        Assert.assertFalse(CoreOperationRelationalExpression.containsMatch(matchIterator, 5));
    }

    @Test
    public void testFindMatch() {
        Iterator<Integer> emptyIterator = new HashSet<Integer>().iterator();
        Iterator<Integer> matchIterator = new HashSet<>(Arrays.asList(1, 2, 3)).iterator();
        
        // Test with two empty iterators
        Assert.assertFalse(CoreOperationRelationalExpression.findMatch(emptyIterator, emptyIterator));
        
        // Test with an iterator that contains a match in the other iterator
        Assert.assertTrue(CoreOperationRelationalExpression.findMatch(emptyIterator, matchIterator));
        
        // Test with iterators that do not have any intersection
        Assert.assertFalse(CoreOperationRelationalExpression.findMatch(emptyIterator, new HashSet<>(Arrays.asList(4, 5)).iterator()));
    }
    
}
