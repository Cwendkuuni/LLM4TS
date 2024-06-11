import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal(null, "abc"));
        assertTrue(Objects.equal("abc", "abc"));
        assertFalse(Objects.equal("abc", "def"));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(123, Objects.hashCode(123));
        assertEquals(123456, Objects.hashCode(123, 456));
        assertEquals(987654321, Objects.hashCode(987, 654, 321));
        assertEquals(555555555, Objects.hashCode(555, 555, 555, 555, 555, 555, 555, 555, 555));
    }
    
    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper1 = Objects.toStringHelper("TestClass");
        assertEquals("TestClass{}", helper1.toString());
        
        Objects.ToStringHelper helper2 = Objects.toStringHelper(TestClass.class);
        assertEquals("TestClass{}", helper2.toString());
        
        Objects.ToStringHelper helper3 = Objects.toStringHelper((Object)null);
        assertEquals("null{}", helper3.toString());
        
        Objects.ToStringHelper helper4 = Objects.toStringHelper("TestClass")
            .add("name", "John")
            .add("age", 30)
            .add("isStudent", true);
        assertEquals("TestClass{name=John, age=30, isStudent=true}", helper4.toString());
        
        Objects.ToStringHelper helper5 = Objects.toStringHelper("EmptyTestClass")
            .omitNullValues()
            .add("name", null)
            .add("age", null)
            .add("isStudent", null);
        assertEquals("EmptyTestClass{}", helper5.toString());
    }
    
    @Test
    public void testFirstNonNull() {
        assertEquals("abc", Objects.firstNonNull(null, "abc"));
        assertEquals(123, Objects.firstNonNull(123, 456));
        assertEquals("def", Objects.firstNonNull(null, null, "def"));
        assertEquals("ghi", Objects.firstNonNull("ghi"));
    }
    
    // Additional test cases can be added to further test all methods in the Objects class

}