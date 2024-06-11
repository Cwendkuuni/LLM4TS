
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal("Hello", "Hello"));
        assertFalse(Objects.equal("Hello", "World"));
        assertTrue(Objects.equal(null, null));
    }

    @Test
    public void testHashCode() {
        assertEquals(Objects.hashCode("Hello", "World"), Objects.hashCode("Hello", "World"));
        assertNotEquals(Objects.hashCode("Hello", "World"), Objects.hashCode("World", "Hello"));
        assertEquals(Objects.hashCode(null), 0);
    }

    @Test
    public void testToStringHelper_singleObject() {
        String result = Objects.toStringHelper("Hello").addValue("World").toString();
        assertEquals("Hello{World}", result);
    }

    @Test
    public void testToStringHelper_multipleObjects() {
        String result = Objects.toStringHelper("Person")
            .add("name", "John")
            .add("age", 30)
            .toString();
        assertEquals("Person{name=John, age=30}", result);
    }

    @Test
    public void testToStringHelper_omitNullValues() {
        String result = Objects.toStringHelper("Person")
            .add("name", "John")
            .add("age", null)
            .omitNullValues()
            .toString();
        assertEquals("Person{name=John}", result);
    }

    @Test
    public void testFirstNonNull() {
        String result = Objects.firstNonNull(null, "Hello");
        assertEquals("Hello", result);
        
        Integer number = Objects.firstNonNull(null, 5);
        assertEquals(Integer.valueOf(5), number);
    }

}
