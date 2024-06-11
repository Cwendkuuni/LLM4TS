import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual_equalObjects_returnTrue() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        
        assertTrue(Objects.equal(obj1, obj2));
    }
    
    @Test
    public void testEqual_differentObjects_returnFalse() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        
        assertFalse(Objects.equal(obj1, obj2));
    }
    
    @Test
    public void testEqual_nullObjects_returnTrue() {
        Object obj1 = null;
        Object obj2 = null;
        
        assertTrue(Objects.equal(obj1, obj2));
    }
    
    @Test
    public void testHashCode_singleObject_returnHashCode() {
        Object obj1 = new Object();
        
        assertEquals(obj1.hashCode(), Objects.hashCode(obj1));
    }
    
    @Test
    public void testHashCode_multipleObjects_returnHashCode() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        
        int expectedHashCode = 31 * (31 + obj1.hashCode()) + obj2.hashCode();
        
        assertEquals(expectedHashCode, Objects.hashCode(obj1, obj2));
    }
    
    @Test
    public void testToStringHelper_addNullValue() {
        Object obj1 = null;
        String expectedString = "ObjectsTest{obj1=null}";
        
        String actualString = Objects.toStringHelper(this.getClass()).add("obj1", obj1).toString();
        
        assertEquals(expectedString, actualString);
    }
    
    @Test
    public void testToStringHelper_addValue() {
        Object obj1 = new Object();
        String expectedString = "ObjectsTest{obj1=" + obj1.toString() + "}";
        
        String actualString = Objects.toStringHelper(this.getClass()).add("obj1", obj1).toString();
        
        assertEquals(expectedString, actualString);
    }
    
    @Test
    public void testToStringHelper_omitNullValues() {
        Object obj1 = null;
        String expectedString = "ObjectsTest{}";
        
        String actualString = Objects.toStringHelper(this.getClass()).omitNullValues().add("obj1", obj1).toString();
        
        assertEquals(expectedString, actualString);
    }
    
    @Test
    public void testFirstNonNull_firstNotNullObject_returnFirstObject() {
        Object obj1 = new Object();
        Object obj2 = null;
        
        Object result = Objects.firstNonNull(obj1, obj2);
        
        assertEquals(obj1, result);
    }
    
    @Test
    public void testFirstNonNull_nullObjects_returnNull() {
        Object obj1 = null;
        Object obj2 = null;
        
        Object result = Objects.firstNonNull(obj1, obj2);
        
        assertNull(result);
    }

}