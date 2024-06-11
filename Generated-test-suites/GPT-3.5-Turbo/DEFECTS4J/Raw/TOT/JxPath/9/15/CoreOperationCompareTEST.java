import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        // Typical use case
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOp = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue(coreOp.equal(context, arg1, arg2));
        
        // Edge case: Empty collections
        context.setSingleNodePointer(new Pointer());
        assertTrue(coreOp.equal(context, new LinkedList<>(), new ArrayList<>()));
        
        // Edge case: Different types
        assertTrue(coreOp.equal(context, "Hello", 5));
        
        // Error scenario: Null values
        assertFalse(coreOp.equal(context, null, null));
    }

    @Test
    public void testContains() {
        // Typical use case
        Iterator<String> it = Arrays.asList("a", "b", "c").iterator();
        CoreOperationCompare coreOp = new CoreOperationCompare(new Expression(), new Expression());
        assertTrue(coreOp.contains(it, "b"));
        
        // Edge case: Empty iterator
        it = Collections.emptyIterator();
        assertFalse(coreOp.contains(it, "a"));
        
        // Error scenario: Null value
        assertFalse(coreOp.contains(it, null));
    }

    @Test
    public void testFindMatch() {
        // Typical use case
        Iterator<String> lit = Arrays.asList("a", "b", "c").iterator();
        Iterator<String> rit = Arrays.asList("x", "y", "z").iterator();
        CoreOperationCompare coreOp = new CoreOperationCompare(new Expression(), new Expression());
        assertFalse(coreOp.findMatch(lit, rit));
        
        // Edge case: Empty iterators
        lit = Collections.emptyIterator();
        rit = Collections.emptyIterator();
        assertFalse(coreOp.findMatch(lit, rit));
        
        // Error scenario: Null values
        lit = null;
        assertFalse(coreOp.findMatch(lit, rit));
    }

    @Test
    public void testEqualValue() {
        // Typical use case
        CoreOperationCompare coreOp = new CoreOperationCompare(new Expression(), new Expression());
        assertTrue(coreOp.equal("Hello", "Hello"));
        
        // Edge cases: NaN values
        assertTrue(coreOp.equal(Double.NaN, Double.NaN));
        assertFalse(coreOp.equal(Double.NaN, 5.0));
        
        // Error scenario: Null values
        assertFalse(coreOp.equal(null, "Hello"));
    }

}