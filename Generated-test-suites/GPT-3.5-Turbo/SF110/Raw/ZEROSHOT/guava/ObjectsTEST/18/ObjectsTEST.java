import static org.junit.Assert.*;
import org.junit.Test;
import javax.annotation.*;
import java.util.*;

public class ObjectsTest {

    // Test equal() method
    @Test
    public void testEqual() {
        // Test when both objects are null
        assertTrue(Objects.equal(null, null));
        
        // Test when either object is null
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));
        
        // Test when both objects are equal
        assertTrue(Objects.equal(new Object(), new Object()));
        
        // Test when objects are not equal
        assertFalse(Objects.equal(new Object(), new ArrayList<>()));
    }
    
    // Test hashCode() method
    @Test
    public void testHashCode() {
        // Test when objects is null
        assertEquals(0, Objects.hashCode((Object[]) null));
        
        // Test when objects is empty
        assertEquals(0, Objects.hashCode());
        
        // Test when objects is not null and not empty
        assertEquals(123, Objects.hashCode(new Object(), new Object()));
    }
    
    // Test toStringHelper() methods
    @Test
    public void testToStringHelper() {
        // Test add() method
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        helper.add("name", "John");
        helper.add("age", 30);
        assertEquals("TestClass{name=John, age=30}", helper.toString());
        
        // Test addValue() method
        helper = Objects.toStringHelper("TestClass");
        helper.addValue("John");
        helper.addValue(30);
        assertEquals("TestClass{John, 30}", helper.toString());
    }
    
    // Test firstNonNull() method
    @Test
    public void testFirstNonNull() {
        // Test when both objects are null
        assertNull(Objects.firstNonNull(null, null));
        
        // Test when first object is null
        assertEquals("Hello", Objects.firstNonNull(null, "Hello"));
        
        // Test when second object is null
        assertEquals("World", Objects.firstNonNull("World", null));
        
        // Test when both objects are not null
        assertEquals("Hello", Objects.firstNonNull("Hello", "World"));
    }
}