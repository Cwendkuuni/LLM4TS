import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        // Typical use case: Both values are equal
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        boolean result = CoreOperationCompare.equal(context, left, right);
        assertTrue(result);
        
        // Typical use case: Both values are not equal
        left = new Expression();
        right = new Expression();
        result = CoreOperationCompare.equal(context, left, right);
        assertFalse(result);
    }

    @Test
    public void testContains() {
        // Typical use case: Iterator contains the value
        Iterator it = new Iterator();
        Object value = new Object();
        boolean result = CoreOperationCompare.contains(it, value);
        assertTrue(result);
        
        // Typical use case: Iterator does not contain the value
        it = new Iterator();
        value = new Object();
        result = CoreOperationCompare.contains(it, value);
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        // Typical use case: Iterator contains the matching value
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();
        boolean result = CoreOperationCompare.findMatch(lit, rit);
        assertTrue(result);
        
        // Typical use case: Iterator does not contain the matching value
        lit = new Iterator();
        rit = new Iterator();
        result = CoreOperationCompare.findMatch(lit, rit);
        assertFalse(result);
    }

    @Test
    public void testEqualForPointers() {
        // Typical use case: Pointers are equal
        Object l = new Pointer();
        Object r = new Pointer();
        boolean result = CoreOperationCompare.equal(l, r);
        assertTrue(result);
        
        // Typical use case: Pointers are not equal
        l = new Pointer();
        r = new Pointer();
        result = CoreOperationCompare.equal(l, r);
        assertFalse(result);
    }

    @Test
    public void testEqualForValues() {
        // Typical use case: Values are equal
        Object l = new Object();
        Object r = new Object();
        boolean result = CoreOperationCompare.equal(l, r);
        assertTrue(result);
        
        // Typical use case: Values are not equal
        l = new Object();
        r = new Object();
        result = CoreOperationCompare.equal(l, r);
        assertFalse(result);
    }

}