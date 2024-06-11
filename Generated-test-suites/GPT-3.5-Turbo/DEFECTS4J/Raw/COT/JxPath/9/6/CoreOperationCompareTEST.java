import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        boolean result = operation.equal(context, arg1, arg2);
        assertTrue(result);
    }
    
    @Test
    public void testContains() {
        Iterator it = new Iterator();
        Object value = new Object();
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        boolean result = operation.contains(it, value);
        assertFalse(result);
    }
    
    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        boolean result = operation.findMatch(lit, rit);
        assertFalse(result);
    }
    
    @Test
    public void testEqualWithPointer() {
        Object l = new Pointer();
        Object r = new Pointer();
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        boolean result = operation.equal(l, r);
        assertFalse(result);
    }
    
    @Test
    public void testEqualWithBoolean() {
        Object l = true;
        Object r = false;
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        boolean result = operation.equal(l, r);
        assertFalse(result);
    }
    
    @Test
    public void testEqualWithNumber() {
        Object l = 1;
        Object r = 2;
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        boolean result = operation.equal(l, r);
        assertFalse(result);
    }
    
    @Test
    public void testEqualWithString() {
        Object l = "hello";
        Object r = "world";
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        boolean result = operation.equal(l, r);
        assertFalse(result);
    }
    
    @Test
    public void testEqualWithNull() {
        Object l = null;
        Object r = "hello";
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        boolean result = operation.equal(l, r);
        assertFalse(result);
    }
    
    @Test
    public void testEqualWithSameObject() {
        Object l = new Object();
        Object r = l;
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        boolean result = operation.equal(l, r);
        assertTrue(result);
    }

}