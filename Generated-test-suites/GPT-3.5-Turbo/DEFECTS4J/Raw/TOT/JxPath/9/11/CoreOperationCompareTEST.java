import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression left = new Expression(arg1);
        Expression right = new Expression(arg2);
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();
        
        // Typical use case
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(operation.equal(context, arr1, arr2));
        
        // Edge case: empty collections
        int[] emptyArr1 = {};
        int[] emptyArr2 = {};
        assertTrue(operation.equal(context, emptyArr1, emptyArr2));
        
        // Error scenario: null values
        Object nullObj = null;
        assertFalse(operation.equal(context, arr1, nullObj));
        assertFalse(operation.equal(context, nullObj, arr2));
    }
    
    @Test
    public void testContains() {
        Expression left = new Expression(arg1);
        Expression right = new Expression(arg2);
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();
        
        // Typical use case
        int[] arr = {1, 2, 3};
        int value1 = 2;
        int value2 = 4;
        assertTrue(operation.contains(arr.iterator(), value1));
        assertFalse(operation.contains(arr.iterator(), value2));
        
        // Edge case: empty collection
        Object[] emptyArr = {};
        assertFalse(operation.contains(emptyArr.iterator(), value1));
    }
    
    @Test
    public void testFindMatch() {
        Expression left = new Expression(arg1);
        Expression right = new Expression(arg2);
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();
        
        // Typical use case
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {3, 4, 5};
        assertTrue(operation.findMatch(arr1.iterator(), arr2.iterator()));
        
        // Edge case: empty collections
        int[] emptyArr1 = {};
        int[] emptyArr2 = {};
        assertFalse(operation.findMatch(emptyArr1.iterator(), emptyArr2.iterator()));
        
        // Error scenario: null collection
        Object[] nullArr = null;
        assertFalse(operation.findMatch(nullArr.iterator(), arr2.iterator()));
        assertFalse(operation.findMatch(arr1.iterator(), nullArr.iterator()));
    }
    
    @Test
    public void testEqualObjects() {
        Expression left = new Expression(arg1);
        Expression right = new Expression(arg2);
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();
        
        // Typical use case
        Object obj1 = "test";
        Object obj2 = "test";
        assertTrue(operation.equal(obj1, obj2));
        
        // Edge case: empty strings
        Object emptyStr1 = "";
        Object emptyStr2 = "";
        assertTrue(operation.equal(emptyStr1, emptyStr2));
        
        // Error scenario: null objects
        Object nullObj = null;
        assertFalse(operation.equal(obj1, nullObj));
        assertFalse(operation.equal(nullObj, obj2));
    }
}