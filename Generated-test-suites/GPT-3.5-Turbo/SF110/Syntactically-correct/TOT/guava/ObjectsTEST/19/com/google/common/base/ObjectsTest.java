import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual_sameObject() {
        Object obj = new Object();
        assertTrue(Objects.equal(obj, obj));
    }
    
    @Test
    public void testEqual_nullObjects() {
        assertTrue(Objects.equal(null, null));
    }
    
    @Test
    public void testEqual_oneNullObject() {
        assertFalse(Objects.equal(null, new Object()));
    }
    
    @Test
    public void testEqual_sameValue() {
        assertTrue(Objects.equal("value", "value"));
    }
    
    @Test
    public void testEqual_differentValue() {
        assertFalse(Objects.equal("value1", "value2"));
    }
    
    @Test
    public void testEqual_mixedObjects() {
        assertFalse(Objects.equal("value", new Object()));
    }
    
    @Test
    public void testHashCode_noObjects() {
        assertEquals(0, Objects.hashCode());
    }
    
    @Test
    public void testHashCode_singleObject() {
        Object obj = new Object();
        assertEquals(obj.hashCode(), Objects.hashCode(obj));
    }
    
    @Test
    public void testHashCode_multipleObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertEquals(Objects.hashCode(obj1, obj2), Objects.hashCode(obj2, obj1));
    }
    
    @Test
    public void testToStringHelper_addValue() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        helper.addValue("value");
        assertEquals("TestClass{value}", helper.toString());
    }
    
    @Test
    public void testToStringHelper_add() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        helper.add("name", "value");
        assertEquals("TestClass{name=value}", helper.toString());
    }
    
    // Test omitted null values
    @Test
    public void testToStringHelper_omitNullValues() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass")
                .omitNullValues()
                .add("name", "value")
                .add("nullValue", null);
        assertEquals("TestClass{name=value}", helper.toString());
    }
    
    @Test
    public void testFirstNonNull_firstNotNull() {
        String result = Objects.firstNonNull("notNull", "nullValue");
        assertEquals("notNull", result);
    }
    
    @Test
    public void testFirstNonNull_bothNull() {
        Object result = Objects.firstNonNull(null, null);
        assertNull(result);
    }
    
    @Test
    public void testFirstNonNull_secondNotNull() {
        Integer result = Objects.firstNonNull(null, 5);
        assertEquals(Integer.valueOf(5), result);
    }
}