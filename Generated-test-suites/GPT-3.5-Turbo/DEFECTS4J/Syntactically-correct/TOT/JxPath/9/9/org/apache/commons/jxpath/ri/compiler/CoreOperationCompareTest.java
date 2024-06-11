import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {
    
    private CoreOperationCompare operationCompare;
    
    @Before
    public void setUp() {
        Expression arg1 = new ExpressionMock();
        Expression arg2 = new ExpressionMock();
        operationCompare = new CoreOperationCompare(arg1, arg2);
    }
    
    @Test
    public void testGetPrecedence() {
        assertEquals(2, operationCompare.getPrecedence());
    }
    
    @Test
    public void testIsSymmetric() {
        assertTrue(operationCompare.isSymmetric());
    }
    
    @Test
    public void testEqual() {
        EvalContext context = new EvalContext();
        Expression left = new ExpressionMock();
        Expression right = new ExpressionMock();
        
        // Test case 1: Collection values
        Collection<Object> collection1 = new ArrayList<>();
        collection1.add("value1");
        collection1.add("value2");
        collection1.add("value3");
        
        Collection<Object> collection2 = new ArrayList<>();
        collection2.add("value3");
        collection2.add("value4");
        collection2.add("value5");
        
        left.setValue(collection1);
        right.setValue(collection2);
        assertTrue(operationCompare.equal(context, left, right));
        
        // Test case 2: Iterator values
        Iterator<Object> iterator1 = collection1.iterator();
        Iterator<Object> iterator2 = collection2.iterator();
        
        left.setValue(iterator1);
        right.setValue(iterator2);
        assertTrue(operationCompare.equal(context, left, right));
        
        // Test case 3: Pointer values
        Pointer pointer1 = new Pointer();
        pointer1.setValue("value");
        
        Pointer pointer2 = new Pointer();
        pointer2.setValue("value");
        
        left.setValue(pointer1);
        right.setValue(pointer2);
        assertTrue(operationCompare.equal(context, left, right));
        
        // Test case 4: Other types of values
        left.setValue("string1");
        right.setValue("string1");
        assertTrue(operationCompare.equal(context, left, right));
        
        left.setValue(10);
        right.setValue(3);
        assertFalse(operationCompare.equal(context, left, right));
        
        left.setValue(true);
        right.setValue(false);
        assertFalse(operationCompare.equal(context, left, right));
    }
    
    @Test
    public void testContains() {
        Iterator<Object> iterator = new ArrayList<>().iterator();
        Object value1 = "value1";
        Object value2 = "value2";
        Object value3 = "value3";
        
        assertFalse(operationCompare.contains(iterator, value1));
        
        ((ArrayList<Object>) iterator).add(value1);
        ((ArrayList<Object>) iterator).add(value2);
        ((ArrayList<Object>) iterator).add(value3);
        
        assertTrue(operationCompare.contains(iterator, value1));
        assertFalse(operationCompare.contains(iterator, "value4"));
    }
    
    @Test
    public void testFindMatch() {
        Iterator<Object> lit = new ArrayList<>().iterator();
        Iterator<Object> rit = new ArrayList<>().iterator();
        
        assertFalse(operationCompare.findMatch(lit, rit));
        
        ((ArrayList<Object>) lit).add("value1");
        ((ArrayList<Object>) lit).add("value2");
        ((ArrayList<Object>) lit).add("value3");
        
        ((ArrayList<Object>) rit).add("value3");
        ((ArrayList<Object>) rit).add("value4");
        ((ArrayList<Object>) rit).add("value5");
        
        assertTrue(operationCompare.findMatch(lit, rit));
        
        ((ArrayList<Object>) rit).add("value6");
        
        assertFalse(operationCompare.findMatch(lit, rit));
    }
    
    @Test
    public void testEqualForObject() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        
        assertFalse(operationCompare.equal(obj1, obj2));
        
        Object obj3 = obj1;
        
        assertTrue(operationCompare.equal(obj1, obj3));
    }
    
    @Test
    public void testEqualForPointer() {
        Pointer pointer1 = new Pointer();
        Pointer pointer2 = new Pointer();
        
        assertFalse(operationCompare.equal(pointer1, pointer2));
        
        pointer1.setValue("value");
        pointer2.setValue("value");
        
        assertTrue(operationCompare.equal(pointer1, pointer2));
    }
    
    @Test
    public void testEqualForNumber() {
        assertTrue(operationCompare.equal(3, 3));
        assertFalse(operationCompare.equal(10, 3));
    }
    
    @Test
    public void testEqualForBoolean() {
        assertTrue(operationCompare.equal(true, true));
        assertFalse(operationCompare.equal(true, false));
    }
    
    @Test
    public void testEqualForString() {
        assertTrue(operationCompare.equal("string1", "string1"));
        assertFalse(operationCompare.equal("string1", "string2"));
    }
    
    @Test
    public void testEqualForNull() {
        assertFalse(operationCompare.equal(null, "value"));
        assertFalse(operationCompare.equal("value", null));
        assertTrue(operationCompare.equal(null, null));
    }
    
    private class ExpressionMock extends Expression {
        
        private Object value;
        
        public void setValue(Object value) {
            this.value = value;
        }
        
        public Object compute(EvalContext context) {
            return value;
        }
    }
}