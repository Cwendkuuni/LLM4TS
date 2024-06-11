import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal("test", "test"));
        assertFalse(Objects.equal(null, "test"));
        assertFalse(Objects.equal("test", null));
        assertFalse(Objects.equal("test", "another"));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(0, Objects.hashCode((Object)null));
        assertEquals(1, Objects.hashCode(true));
        assertEquals(-1, Objects.hashCode(false));
        assertEquals(101, Objects.hashCode((char)'A'));
        assertEquals(234343, Objects.hashCode(123.45));
        assertEquals(987654321, Objects.hashCode(12.34f));
        assertEquals(12345, Objects.hashCode(12345));
        assertEquals(67890, Objects.hashCode(67890L));
        assertEquals(1367862874, Objects.hashCode("test", 123, true));
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        helper.add("name", "John");
        helper.add("age", 25);
        helper.add("isStudent", false);
        assertEquals("TestClass{name=John, age=25, isStudent=false}", helper.toString());
        
        helper = Objects.toStringHelper(this.getClass());
        helper.addValue("test");
        assertEquals("ObjectsTest{test}", helper.toString());
        
        helper = Objects.toStringHelper(getClass().getSimpleName());
        helper.addValue(123);
        assertEquals("ObjectsTest{123}", helper.toString());
    }

    @Test
    public void testFirstNonNull() {
        assertNull(Objects.firstNonNull(null, null));
        assertEquals("test", Objects.firstNonNull("test", "another"));
        assertEquals(123, (int) Objects.firstNonNull(null, 123));
        assertEquals(true, Objects.firstNonNull(false, true));
    }

}