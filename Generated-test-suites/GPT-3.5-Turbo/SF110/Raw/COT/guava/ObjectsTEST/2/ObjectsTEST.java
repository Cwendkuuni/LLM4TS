
import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual_basic() {
        assertTrue(Objects.equal("abc", "abc"));
        assertFalse(Objects.equal("abc", "def"));
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal("abc", null));
        assertFalse(Objects.equal(null, "abc"));
    }
    
    @Test
    public void testHashCode_basic() {
        assertEquals(0, Objects.hashCode());
        assertEquals(1, Objects.hashCode(null));
        assertEquals(1234, Objects.hashCode("abc", 123, true));
    }
    
    @Test
    public void testToStringHelper_basic() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        assertNotNull(helper);
    }
    
    @Test
    public void testFirstNonNull_basic() {
        String first = "abc";
        String second = null;
        String result = Objects.firstNonNull(first, second);
        assertEquals(first, result);
        
        first = null;
        second = "def";
        result = Objects.firstNonNull(first, second);
        assertEquals(second, result);
        
        first = null;
        second = null;
        result = Objects.firstNonNull(first, second);
        assertNull(result);
    }
    
    @Test
    public void testToStringHelper_omitNullValues() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass").omitNullValues();
        assertNotNull(helper);
    }
    
    @Test
    public void testToStringHelper_add() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass")
                .add("name", "Alice")
                .add("age", 25)
                .add("isStudent", true);
        String expected = "TestClass{name=Alice, age=25, isStudent=true}";
        assertEquals(expected, helper.toString());
    }
    
    @Test
    public void testToStringHelper_addValue() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass")
                .addValue("Alice")
                .addValue(25)
                .addValue(true);
        String expected = "TestClass{Alice, 25, true}";
        assertEquals(expected, helper.toString());
    }
}
