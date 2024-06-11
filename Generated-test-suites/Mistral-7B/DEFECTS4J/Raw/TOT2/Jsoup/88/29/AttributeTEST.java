import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {
    @Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    // Test cases for setKey(), setValue(), html(), isDataAttribute(), shouldCollapseAttribute(), equals(), hashcode(), and clone()

    // Edge cases and exception handling test cases

    @Test(expected = NullPointerException.class)
    public void testCreateAttributeWithNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testCreateAttributeWithNullValue() {
        new Attribute("key", null);
    }

    @Test
    public void testCreateAttributeWithLongKey() {
        String longKey = "A" + new String(new char[1024]).replace("\0", "a");
        Attribute attribute = new Attribute(longKey, "value");
        assertEquals(longKey, attribute.getKey());
    }

    @Test
    public void testCreateAttributeWithSpecialCharactersKey() {
        Attribute attribute = new Attribute("key%&/=!$@#?^", "value");
        assertEquals("key%&/=!$@#?^", attribute.getKey());
    }

    @Test
    public void testSettingKeyToEmptyString() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("");
        assertEquals("", attribute.getKey());
    }

    @Test
    public void testSettingKeyToNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
        assertNull(attribute.getKey());
    }

    @Test
    public void testSettingValueToEmptyString() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("");
        assertEquals("", attribute.getValue());
    }

    @Test
    public void testSettingValueToNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
        assertNull(attribute.getValue());
    }

    @Test
    public void testCallingMethodsAfterParentRemoved() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        parent.clear();
        // Test methods here
        assertNull(attribute.getParent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCallingMethodsWithNullOutputSettings() {
        Attribute attribute = new Attribute("key", "value");
        attribute.html(null, new Document.OutputSettings());
    }
}