import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual_typical() {
        Object a = new Object();
        Object b = new Object();
        assertFalse(Objects.equal(a, b));
    }

    @Test
    public void testEqual_sameObject() {
        Object a = new Object();
        assertTrue(Objects.equal(a, a));
    }

    @Test
    public void testEqual_null() {
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));
        assertTrue(Objects.equal(null, null));
    }

    @Test
    public void testHashCode_typical() {
        Object[] objects = new Object[] {1, 2, 3};
        int hashCode = Objects.hashCode(objects);
        assertEquals(hashCode, Arrays.hashCode(objects));
    }

    @Test
    public void testHashCode_empty() {
        Object[] objects = new Object[] {};
        assertEquals(0, Objects.hashCode(objects));
    }
    
    @Test
    public void testToStringHelper_typical() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClassName");
        helper.add("name", "John");
        helper.add("age", 25);
        String result = helper.toString();
        assertEquals("TestClassName{name=John, age=25}", result);
    }

    @Test
    public void testToStringHelper_omitNullValues() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClassName");
        helper.add("name", "John");
        helper.add("age", null);
        helper.omitNullValues();
        String result = helper.toString();
        assertEquals("TestClassName{name=John}", result);
    }

    @Test
    public void testFirstNonNull_typical() {
        String first = "Hello";
        String second = "World";
        String result = Objects.firstNonNull(first, second);
        assertEquals(first, result);
    }

    @Test
    public void testFirstNonNull_nullFirst() {
        String first = null;
        String second = "World";
        String result = Objects.firstNonNull(first, second);
        assertEquals(second, result);
    }

    @Test
    public void testFirstNonNull_nullSecond() {
        String first = "Hello";
        String second = null;
        String result = Objects.firstNonNull(first, second);
        assertEquals(first, result);
    }

    @Test
    public void testFirstNonNull_bothNull() {
        String first = null;
        String second = null;
        String result = Objects.firstNonNull(first, second);
        assertNull(result);
    }

}