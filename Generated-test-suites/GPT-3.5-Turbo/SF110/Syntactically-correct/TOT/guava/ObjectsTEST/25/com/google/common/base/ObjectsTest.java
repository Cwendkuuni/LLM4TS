import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual_nullObjects() {
        assertTrue(Objects.equal(null, null));
    }
    
    @Test
    public void testEqual_objectAndNull() {
        assertFalse(Objects.equal(new Object(), null));
    }
    
    @Test
    public void testEqual_sameObject() {
        Object obj = new Object();
        assertTrue(Objects.equal(obj, obj));
    }
    
    @Test
    public void testEqual_equalObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertTrue(Objects.equal(obj1, obj2));
    }
    
    @Test
    public void testHashCode_emptyArray() {
        assertEquals(0, Objects.hashCode());
    }
    
    @Test
    public void testHashCode_singleObject() {
        Object obj = new Object();
        assertEquals(obj.hashCode(), Objects.hashCode(obj));
    }
    
    @Test
    public void testToStringHelper_omitNullValues() {
        String expected = "ObjectsTest{value=true}";
        String result = Objects.toStringHelper(this.getClass().getSimpleName())
                            .add("value1", true)
                            .add("value2", null)
                            .omitNullValues()
                            .toString();
        assertEquals(expected, result);
    }
    
    @Test
    public void testToStringHelper_addValue() {
        String expected = "ObjectsTest{value=true}";
        String result = Objects.toStringHelper(this.getClass().getSimpleName())
                            .addValue(true)
                            .toString();
        assertEquals(expected, result);
    }
    
    @Test
    public void testFirstNonNull_bothNull() {
        assertNull(Objects.firstNonNull(null, null));
    }
    
    @Test
    public void testFirstNonNull_firstNull() {
        Object obj = new Object();
        assertSame(obj, Objects.firstNonNull(null, obj));
    }
    
    @Test
    public void testFirstNonNull_secondNull() {
        Object obj = new Object();
        assertSame(obj, Objects.firstNonNull(obj, null));
    }

}