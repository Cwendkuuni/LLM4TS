import org.junit.Assert;
import org.junit.Test;

public class CoreOperationCompareTest {
    
    @Test
    public void testEqual() {
        Expression arg1 = new ExpressionImpl();
        Expression arg2 = new ExpressionImpl();
        CoreOperationCompare operationCompare = new CoreOperationEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new PointerImpl());
        
        boolean result = operationCompare.equal(context, arg1, arg2);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEqualWithNullValues() {
        Expression arg1 = new ExpressionImpl();
        Expression arg2 = new ExpressionImpl();
        CoreOperationCompare operationCompare = new CoreOperationEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(null);
        
        boolean result = operationCompare.equal(context, arg1, arg2);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testContains() {
        Iterator<Object> it = new IteratorImpl();
        Object value = new Object();
        CoreOperationCompare operationCompare = new CoreOperationCompareImpl();
        
        boolean result = operationCompare.contains(it, value);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testContainsWithMatch() {
        Iterator<Object> it = new IteratorImpl();
        Object value = new Object();
        it.add(value);
        CoreOperationCompare operationCompare = new CoreOperationCompareImpl();
        
        boolean result = operationCompare.contains(it, value);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testFindMatch() {
        Iterator<Object> lit = new IteratorImpl();
        Iterator<Object> rit = new IteratorImpl();
        CoreOperationCompare operationCompare = new CoreOperationCompareImpl();
        
        boolean result = operationCompare.findMatch(lit, rit);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testFindMatchWithMatch() {
        Iterator<Object> lit = new IteratorImpl();
        Iterator<Object> rit = new IteratorImpl();
        Object value = new Object();
        lit.add(value);
        rit.add(value);
        CoreOperationCompare operationCompare = new CoreOperationCompareImpl();
        
        boolean result = operationCompare.findMatch(lit, rit);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEqualWithPointers() {
        Object l = new PointerImpl();
        Object r = new PointerImpl();
        CoreOperationCompare operationCompare = new CoreOperationCompareImpl();
        
        boolean result = operationCompare.equal(l, r);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEqualWithDifferentValues() {
        Object l = "abc";
        Object r = "def";
        CoreOperationCompare operationCompare = new CoreOperationCompareImpl();
        
        boolean result = operationCompare.equal(l, r);
        
        Assert.assertFalse(result);
    }
}