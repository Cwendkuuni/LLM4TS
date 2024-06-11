import org.junit.Test;
import org.junit.Assert;

public class CoreOperationCompareTest {
    
    @Test
    public void testEqual() {
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        
        // Test case 1
        Object result = operation.equal(context, left, right);
        Assert.assertEquals(false, result);
        
        // Test case 2
        Object result2 = operation.equal(context, left, right);
        Assert.assertEquals(false, result2);
    }
    
    @Test
    public void testContains() {
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        Iterator it = new Iterator();
        Object value = new Object();
        
        // Test case 1
        boolean result = operation.contains(it, value);
        Assert.assertEquals(false, result);
        
        // Test case 2
        boolean result2 = operation.contains(it, value);
        Assert.assertEquals(false, result2);
    }
    
    @Test
    public void testFindMatch() {
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();
        
        // Test case 1
        boolean result = operation.findMatch(lit, rit);
        Assert.assertEquals(false, result);
        
        // Test case 2
        boolean result2 = operation.findMatch(lit, rit);
        Assert.assertEquals(false, result2);
    }
    
    @Test
    public void testEqualTwoObjects() {
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        Object l = new Object();
        Object r = new Object();
        
        // Test case 1
        boolean result = operation.equal(l, r);
        Assert.assertEquals(false, result);
        
        // Test case 2
        boolean result2 = operation.equal(l, r);
        Assert.assertEquals(false, result2);
    }
}