import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompareMock();
        
        // Typical case
        EvalContext context = new EvalContext();
        Expression left = new ConstantExpression("test");
        Expression right = new ConstantExpression("test");
        boolean result = coreOperationCompare.equal(context, left, right);
        assertTrue(result);
        
        // Edge case: Null values
        left = new ConstantExpression(null);
        right = new ConstantExpression(null);
        result = coreOperationCompare.equal(context, left, right);
        assertTrue(result);
        
        // Exception handling scenario: Different types
        left = new ConstantExpression(10);
        right = new ConstantExpression("10");
        result = coreOperationCompare.equal(context, left, right);
        assertFalse(result);
    }
    
    @Test
    public void testContains() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompareMock();
        
        // Typical case
        Iterator it = CollectionUtils.asIterator("test1", "test2", "test3");
        boolean result = coreOperationCompare.contains(it, "test2");
        assertTrue(result);
        
        // Edge case: Empty iterator
        it = CollectionUtils.asIterator();
        result = coreOperationCompare.contains(it, "test");
        assertFalse(result);
    }
    
    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompareMock();
        
        // Typical case
        Iterator lit = CollectionUtils.asIterator("1", "2", "3");
        Iterator rit = CollectionUtils.asIterator("2", "4", "6");
        boolean result = coreOperationCompare.findMatch(lit, rit);
        assertTrue(result);
        
        // Edge case: No match found
        lit = CollectionUtils.asIterator("1", "2", "3");
        rit = CollectionUtils.asIterator("4", "5", "6");
        result = coreOperationCompare.findMatch(lit, rit);
        assertFalse(result);
    }
    
    @Test
    public void testEqualValues() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompareMock();
        
        // Typical case
        boolean result = coreOperationCompare.equalValues("test", "test");
        assertTrue(result);
        
        // Edge case: Null values
        result = coreOperationCompare.equalValues(null, null);
        assertTrue(result);
        
        // Exception handling scenario: Different types
        result = coreOperationCompare.equalValues(10, "10");
        assertFalse(result);
    }
    
    private class CoreOperationCompareMock extends CoreOperationCompare {
        
        public CoreOperationCompareMock() {
            super(null, null);
        }
        
        @Override
        protected boolean equal(EvalContext context, Expression left, Expression right) {
            return super.equal(context, left, right);
        }
        
        @Override
        protected boolean contains(Iterator it, Object value) {
            return super.contains(it, value);
        }
        
        @Override
        protected boolean findMatch(Iterator lit, Iterator rit) {
            return super.findMatch(lit, rit);
        }
        
        protected boolean equalValues(Object l, Object r) {
            return super.equal(l, r);
        }
    }
}