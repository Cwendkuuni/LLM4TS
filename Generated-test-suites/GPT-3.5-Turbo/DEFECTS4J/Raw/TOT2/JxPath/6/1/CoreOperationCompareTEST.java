import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        Object l = "hello";
        Object r = "hello";
        
        assertTrue(coreOperationCompare.equal(l, r));
    }

    @Test
    public void testContains() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        Iterator it = Arrays.asList(1, 2, 3).iterator();
        Object value = 3;
        
        assertTrue(coreOperationCompare.contains(it, value));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        Iterator lit = Arrays.asList("apple", "banana", "cherry").iterator();
        Iterator rit = Arrays.asList("orange", "banana", "grape").iterator();
        
        assertTrue(coreOperationCompare.findMatch(lit, rit));
    }

    @Test
    public void testEqualWithNull() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        
        assertFalse(coreOperationCompare.equal(null, "test"));
    }

    @Test
    public void testEqualBoolean() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        Object l = true;
        Object r = false;
        
        assertFalse(coreOperationCompare.equal(l, r));
    }

    @Test
    public void testEqualNumber() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        Object l = 10;
        Object r = 10.0;
        
        assertTrue(coreOperationCompare.equal(l, r));
    }

    @Test
    public void testEqualString() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        Object l = "testing";
        Object r = "testing";
        
        assertTrue(coreOperationCompare.equal(l, r));
    }

}