import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompareTest();
        EvalContext context = new EvalContext();
        Pointer pointer1 = new Pointer("value1");
        Pointer pointer2 = new Pointer("value1");
        
        boolean result = coreOperationCompare.equal(context, pointer1, pointer2);
        
        assertTrue(result);
    }

    @Test
    public void testContains() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompareTest();
        Iterator it = new ArrayList().iterator();
        Object value = "test";
        
        boolean result = coreOperationCompare.contains(it, value);
        
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompareTest();
        Iterator lit = Arrays.asList(1, 2, 3).iterator();
        Iterator rit = Arrays.asList(4, 5, 2).iterator();
        
        boolean result = coreOperationCompare.findMatch(lit, rit);
        
        assertTrue(result);
    }

    @Test
    public void testEqualWithPointer() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompareTest();
        Pointer pointer1 = new Pointer("value");
        Pointer pointer2 = new Pointer("value");
        
        boolean result = coreOperationCompare.equal(pointer1, pointer2);
        
        assertTrue(result);
    }

    @Test
    public void testEqualWithNumbers() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompareTest();
        boolean result = coreOperationCompare.equal(5, 5.0);
        
        assertTrue(result);
    }

    @Test
    public void testEqualWithStrings() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompareTest();
        boolean result = coreOperationCompare.equal("hello", "hello");
        
        assertTrue(result);
    }

    @Test
    public void testEqualWithNull() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompareTest();
        boolean result = coreOperationCompare.equal(null, null);
        
        assertTrue(result);
    }

}