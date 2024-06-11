import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class ObjectsTest {
    @Test
    public void testEqual() {
        assertEquals(true, Objects.equal(null, null));
        assertEquals(true, Objects.equal(1, 1));
        assertEquals(false, Objects.equal(1, null));
        assertEquals(false, Objects.equal(null, 1));
        assertEquals(true, Objects.equal("foo", "foo"));
        assertEquals(false, Objects.equal("foo", "bar"));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(41682695, Objects.hashCode("foo"));
        assertEquals(978, Objects.hashCode(1, 2, 3));
        assertEquals(481, Objects.hashCode(null));
        assertEquals(74997, Objects.hashCode("foo", "bar", "baz"));
    }
    
    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
        assertEquals("ClassName{}", helper.toString());
        
        helper = Objects.toStringHelper("ClassName")
            .add("name", "John")
            .add("age", 30);
        assertEquals("ClassName{name=John, age=30}", helper.toString());
        
        helper = Objects.toStringHelper("ClassName")
            .omitNullValues()
            .add("name", "John")
            .add("age", null);
        assertEquals("ClassName{name=John}", helper.toString());
        
        helper = Objects.toStringHelper("ClassName")
            .add("name", "John")
            .addValue(30);
        assertEquals("ClassName{name=John, 30}", helper.toString());
    }
    
    @Test
    public void testFirstNonNull() {
        assertEquals("foo", Objects.firstNonNull("foo", "bar"));
        assertEquals("bar", Objects.firstNonNull(null, "bar"));
        assertNull(Objects.firstNonNull(null, null));
    }
}