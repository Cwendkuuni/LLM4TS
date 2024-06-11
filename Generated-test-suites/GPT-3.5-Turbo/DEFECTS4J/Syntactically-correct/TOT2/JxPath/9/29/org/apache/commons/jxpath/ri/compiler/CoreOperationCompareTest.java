import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(arg1), new Expression(arg2));
        EvalContext context = new EvalContext();
        context.setPointerFactory(new PointerFactory());
        context.setSingleNodePointer(new Pointer());
        
        // Test case for equal values
        assertTrue(coreOperationCompare.equal(context, new Expression("value"), new Expression("value")));
        
        // Test case for different values
        assertFalse(coreOperationCompare.equal(context, new Expression("value1"), new Expression("value2")));
        
        // Add more test cases here for typical, edge cases, and error scenarios
    }

    @Test
    public void testContains() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(arg1), new Expression(arg2));
        Iterator it = new Iterator<>();
        
        // Test case for empty iterator
        assertFalse(coreOperationCompare.contains(it, "value"));
        
        // Test case for iterator containing value
        it.add("value");
        assertTrue(coreOperationCompare.contains(it, "value"));
        
        // Add more test cases here for typical, edge cases, and error scenarios
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(arg1), new Expression(arg2));
        Iterator lit = new Iterator<>();
        Iterator rit = new Iterator<>();
        
        // Test case for empty iterators
        assertFalse(coreOperationCompare.findMatch(lit, rit));
        
        // Test case for matching values in iterators
        lit.add("value1");
        rit.add("value1");
        assertTrue(coreOperationCompare.findMatch(lit, rit));
        
        // Add more test cases here for typical, edge cases, and error scenarios
    }

    @Test
    public void testEqualObjects() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(arg1), new Expression(arg2));
        
        // Test case for equal pointers
        assertTrue(coreOperationCompare.equal(new Pointer(), new Pointer()));
        
        // Test case for different pointers
        assertFalse(coreOperationCompare.equal(new Pointer(), new Pointer("value")));
        
        // Add more test cases here for typical, edge cases, and error scenarios
    }

    // Add more test methods for other public methods in CoreOperationCompare class

}