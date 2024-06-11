
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {
    
    private CoreOperationCompare coreOperationCompare;
    
    @Before
    public void setup() {
        coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
    }
    
    @Test
    public void testEqual_Success() {
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        
        // Test case with equal values
        Object value = "value";
        left.setValue(value);
        right.setValue(value);
        boolean result = coreOperationCompare.equal(context, left, right);
        assertTrue(result);
    }
    
    @Test
    public void testEqual_DifferentTypes() {
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        
        // Test case with different types of values
        Object value1 = "10";
        Object value2 = 10;
        left.setValue(value1);
        right.setValue(value2);
        boolean result = coreOperationCompare.equal(context, left, right);
        assertFalse(result);
    }
    
    @Test
    public void testEqual_NullValues() {
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        
        // Test case with null values
        Object value = null;
        left.setValue(value);
        right.setValue(value);
        boolean result = coreOperationCompare.equal(context, left, right);
        assertTrue(result);
    }
    
    @Test
    public void testEqual_BooleanValues() {
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();

        // Test case with boolean values
        Object value1 = true;
        Object value2 = false;
        left.setValue(value1);
        right.setValue(value2);
        boolean result = coreOperationCompare.equal(context, left, right);
        assertFalse(result);
    }
    
    @Test
    public void testContains_Success() {
        Iterator it = null;
        Object value = "value";
        
        // Test case where iterator contains the value
        it = new ArrayList<String>().add(value).iterator();
        boolean result = coreOperationCompare.contains(it, value);
        assertTrue(result);
    }
    
    @Test
    public void testContains_NotFound() {
        Iterator it = null;
        Object value = "value";
        
        // Test case where iterator does not contain the value
        it = new ArrayList<String>().add("otherValue").iterator();
        boolean result = coreOperationCompare.contains(it, value);
        assertFalse(result);
    }
    
    @Test
    public void testFindMatch_Success() {
        Iterator lit = null;
        Iterator rit = null;
        
        // Test case where there is a match between left and right iterator
        lit = new ArrayList<String>().add("value1").iterator();
        rit = new ArrayList<String>().add("value1").iterator();
        boolean result = coreOperationCompare.findMatch(lit, rit);
        assertTrue(result);
    }
    
    @Test
    public void testFindMatch_NoMatch() {
        Iterator lit = null;
        Iterator rit = null;
        
        // Test case where there is no match between left and right iterator
        lit = new ArrayList<String>().add("value1").iterator();
        rit = new ArrayList<String>().add("value2").iterator();
        boolean result = coreOperationCompare.findMatch(lit, rit);
        assertFalse(result);
    }
    
    @Test
    public void testEqual_PointerValues() {
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        
        Pointer pointer1 = new Pointer();
        Pointer pointer2 = new Pointer();
        
        // Test case with Pointer values
        left.setValue(pointer1);
        right.setValue(pointer2);
        boolean result = coreOperationCompare.equal(context, left, right);
        assertFalse(result);
    }
    
    @Test
    public void testEqual_NaNValues() {
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        
        // Test case with NaN values
        Object value1 = Double.NaN;
        Object value2 = Double.NaN;
        left.setValue(value1);
        right.setValue(value2);
        boolean result = coreOperationCompare.equal(context, left, right);
        assertFalse(result);
    }
    
    @Test
    public void testEqual_SameObjects() {
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        
        // Test case where left and right are the same object
        Object value = new Object();
        left.setValue(value);
        right.setValue(value);
        boolean result = coreOperationCompare.equal(context, left, right);
        assertTrue(result);
    }
    
    @Test
    public void testEqual_DifferentObjects() {
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        
        // Test case where left and right are different objects
        Object value1 = new Object();
        Object value2 = new Object();
        left.setValue(value1);
        right.setValue(value2);
        boolean result = coreOperationCompare.equal(context, left, right);
        assertFalse(result);
    }
}

