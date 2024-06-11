import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.SerializationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testConstructor() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
    }

    @Test
    public void testSetKey() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        String newKey = "newKey";
        attribute.setKey(newKey);

        assertEquals(newKey, attribute.getKey());
    }

    @Test
    public void testSetValue() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        String newValue = "newValue";
        String oldValue = attribute.setValue(newValue);

        assertEquals(newValue, attribute.getValue());
        assertEquals(value, oldValue);
    }

    @Test
    public void testHtml() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        try {
            String html = attribute.html();
            assertNotNull(html);
        } catch (SerializationException e) {
            fail("SerializationException should not be thrown here");
        }
    }

    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "testKey";
        String encodedValue = "testValue";
        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);

        assertEquals(unencodedKey, attribute.getKey());
        assertEquals("testValue", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        String key = "data-test";
        Attribute attribute = new Attribute(key, "value");

        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        String key = "async";
        Attribute attribute = new Attribute(key, "true");

        assertTrue(attribute.isBooleanAttribute());
    }
}