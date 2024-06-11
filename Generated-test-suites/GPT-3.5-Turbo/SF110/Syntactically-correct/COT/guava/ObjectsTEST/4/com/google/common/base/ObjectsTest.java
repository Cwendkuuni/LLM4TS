
import org.junit.*;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal(null, "test"));
        assertFalse(Objects.equal("test", null));
        assertTrue(Objects.equal("test", "test"));
        assertFalse(Objects.equal("test", "TEST"));
        assertFalse(Objects.equal("test", "test "));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(1, Objects.hashCode(null));
        assertEquals(31, Objects.hashCode("test"));
        assertEquals(-142713832, Objects.hashCode("test", "TEST"));
    }

    @Test
    public void testToStringHelper() {
        assertNotNull(Objects.toStringHelper(new Object()));
        assertNotNull(Objects.toStringHelper(Object.class));
        assertNotNull(Objects.toStringHelper("className"));
    }

    @Test
    public void testFirstNonNull() {
        assertNull(Objects.firstNonNull(null, null));
        assertNull(Objects.firstNonNull(null, "test"));
        assertEquals("test", Objects.firstNonNull("test", null));
    }

    @Test
    public void testToStringHelperAdd() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("className");
        helper.add("name1", "value1");
        assertEquals("className{name1=value1}", helper.toString());
        helper.add("name2", 2);
        assertEquals("className{name1=value1, name2=2}", helper.toString());
    }

    @Test
    public void testToStringHelperAddValue() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("className");
        helper.addValue("value1");
        assertEquals("className{value1}", helper.toString());
        helper.addValue(2);
        assertEquals("className{value1, 2}", helper.toString());
    }

    @Test
    public void testToStringHelperOmitNullValues() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("className");
        helper.omitNullValues();
        assertEquals("className{}", helper.toString());
        helper.addValue("value1");
        helper.add("name2", null);
        assertEquals("className{value1}", helper.toString());
    }

}
