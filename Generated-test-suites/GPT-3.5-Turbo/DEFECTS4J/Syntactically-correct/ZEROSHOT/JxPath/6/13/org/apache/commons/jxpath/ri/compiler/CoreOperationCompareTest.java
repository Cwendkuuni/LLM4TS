import org.junit.Assert;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        boolean result = operation.equal(context, left, right);
        
        Assert.assertEquals(true, result);
    }
    
    @Test
    public void testContains() {
        Iterator it = new Iterator();
        Object value = new Object();
        
        CoreOperationCompare operation = new CoreOperationCompare();
        boolean result = operation.contains(it, value);
        
        Assert.assertEquals(false, result);
    }
    
    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();
        
        CoreOperationCompare operation = new CoreOperationCompare();
        boolean result = operation.findMatch(lit, rit);
        
        Assert.assertEquals(false, result);
    }
    
    @Test
    public void testEqualWithPointer() {
        Object l = new Object();
        Object r = new Object();
        
        CoreOperationCompare operation = new CoreOperationCompare();
        boolean result = operation.equal(l, r);
        
        Assert.assertEquals(false, result);
    }
}