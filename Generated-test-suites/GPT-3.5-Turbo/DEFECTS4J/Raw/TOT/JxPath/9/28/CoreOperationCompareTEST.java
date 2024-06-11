import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        // Typical use case
        arg1.setValue(5);
        arg2.setValue(5);
        assertTrue(operation.equal(arg1, arg2));
        
        // Edge case
        arg1.setValue(null);
        arg2.setValue(null);
        assertTrue(operation.equal(arg1, arg2));
        
        // Error scenario
        arg1.setValue("hello");
        arg2.setValue(5);
        assertFalse(operation.equal(arg1, arg2));
    }
    
    @Test
    public void testContains() {
        Iterator it = new Iterator();
        
        // Typical use case
        it.addItem(1);
        it.addItem(2);
        it.addItem(3);
        it.addItem(4);
        assertTrue(operation.contains(it, 2));
        
        // Edge case
        it.addItem(null);
        assertTrue(operation.contains(it, null));
        
        // Error scenario
        it.addItem("hello");
        assertFalse(operation.contains(it, 5));
    }
    
    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();
        
        // Typical use case
        lit.addItem(1);
        lit.addItem(2);
        lit.addItem(3);
        rit.addItem(4);
        rit.addItem(5);
        rit.addItem(2);
        assertTrue(operation.findMatch(lit, rit));
        
        // Edge case
        lit.addItem(null);
        assertTrue(operation.findMatch(lit, rit));
        
        // Error scenario
        lit.addItem("hello");
        rit.addItem("world");
        assertFalse(operation.findMatch(lit, rit));
    }
    
    @Test
    public void testEqualWithIntegers() {
        Object l = 5;
        Object r = 5;
        assertTrue(operation.equal(l, r));
    }
    
    @Test
    public void testEqualWithPointers() {
        Pointer l = new Pointer();
        Pointer r = new Pointer();
        
        // Typical use case
        l.setValue("hello");
        r.setValue("hello");
        assertTrue(operation.equal(l, r));
        
        // Edge case
        l.setValue(null);
        r.setValue(null);
        assertTrue(operation.equal(l, r));
        
        // Error scenario
        l.setValue("hello");
        r.setValue("world");
        assertFalse(operation.equal(l, r));
    }
    
    @Test
    public void testEqualWithBoolean() {
        Object l = true;
        Object r = false;
        assertFalse(operation.equal(l, r));
    }
    
    @Test
    public void testEqualWithStrings() {
        Object l = "hello";
        Object r = "hello";
        assertTrue(operation.equal(l, r));
    }
    
    @Test
    public void testEqualWithNaN() {
        Object l = Double.NaN;
        Object r = Double.NaN;
        assertFalse(operation.equal(l, r));
    }
    
    @Test
    public void testEqualWithNull() {
        Object l = null;
        Object r = null;
        assertTrue(operation.equal(l, r));
    }
    
    @Test
    public void testEqualWithDifferentTypes() {
        Object l = "hello";
        Object r = 5;
        assertFalse(operation.equal(l, r));
    }
}