import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("class", "test");
    }

    @Test
    public void testGetKey() {
        assertEquals("class", attribute.getKey());
    }

    @Test
    public void testGetKeyWithNull() {
        assertNotNull(attribute.getKey());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("id");
        assertEquals("id", attribute.getKey());
    }

    @Test
    public void testSetKeyWithNull() {
        attribute.setKey(null);
        assertNotNull(attribute.getKey());
    }

    @Test
    public void testGetValue() {
        assertEquals("test", attribute.getValue());
    }

    @Test
    public void testGetValueWithNull() {
        assertNotNull(attribute.getValue());
    }

    @Test
    public void testSetValue() {
        attribute.setValue("value");
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValueWithNull() {
        attribute.setValue(null);
        assertNotNull(attribute.getValue());
    }

    @Test
    public void testHtml() {
        String expected = "class=\"test\"";
        assertEquals(expected, attribute.html());
    }

    @Test
    public void testToString() {
        String expected = "class=\"test\"";
        assertEquals(expected, attribute.toString());
    }

    @Test
    public void testClone() {
        Attribute clone = attribute.clone();
        assertNotSame(attribute, clone);
        assertEquals(attribute.getKey(), clone.getKey());
        assertEquals(attribute.getValue(), clone.getValue());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute newAttribute = Attribute.createFromEncoded("data", "&#64;encode");
        assertEquals("data", newAttribute.getKey());
        assertEquals("@encode", newAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute("class"));
        assertTrue(attribute.isBooleanAttribute("hidden"));
    }

    @Test
    public void testEquals() {
        Attribute equalAttribute = new Attribute("class", "test");
        assertTrue(attribute.equals(equalAttribute));
        assertFalse(attribute.equals(new Attribute("id", "test")));
        assertFalse(attribute.equals(new Attribute("class", "other")));
    }

    @Test
    public void testHashCode() {
        Attribute equalAttribute = new Attribute("class", "test");
        assertEquals(attribute.hashCode(), equalAttribute.hashCode());
    }
}