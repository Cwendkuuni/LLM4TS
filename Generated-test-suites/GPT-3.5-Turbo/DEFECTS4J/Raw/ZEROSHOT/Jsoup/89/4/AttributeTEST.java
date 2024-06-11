import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {
    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("key", "value");
    }

    @Test
    public void testGetKey() {
        assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        assertEquals("value", attribute.setValue("newValue"));
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        String expected = "key=\"value\"";
        assertEquals(expected, attribute.html());
    }

    @Test
    public void testToString() {
        String expected = "key=\"value\"";
        assertEquals(expected, attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute newAttribute = Attribute.createFromEncoded("key", "value");
        assertEquals("key", newAttribute.getKey());
        assertEquals("value", newAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
        assertTrue(Attribute.isDataAttribute("data-key"));
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
        assertTrue(Attribute.isBooleanAttribute("checked"));
    }

    @Test
    public void testEquals() {
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute, attribute2);

        Attribute attribute3 = new Attribute("key", "newValue");
        assertNotEquals(attribute, attribute3);

        Attribute attribute4 = new Attribute("newKey", "value");
        assertNotEquals(attribute, attribute4);
    }

    @Test
    public void testHashCode() {
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), attribute2.hashCode());

        Attribute attribute3 = new Attribute("key", "newValue");
        assertNotEquals(attribute.hashCode(), attribute3.hashCode());

        Attribute attribute4 = new Attribute("newKey", "value");
        assertNotEquals(attribute.hashCode(), attribute4.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
        assertNotSame(attribute, clonedAttribute);
    }
}