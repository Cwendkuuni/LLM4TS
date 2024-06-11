import org.junit.Before;
import org.junit.Test;
import org.jsoup.SerializationException;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

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
        String oldValue = attribute.setValue("newValue");
        assertEquals("value", oldValue);
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        String html = attribute.html();
        assertEquals("key=\"value\"", html);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlWithIOException() {
        attribute = new Attribute("key", "value") {
            @Override
            protected void html(Appendable accum, Document.OutputSettings out) throws IOException {
                throw new IOException();
            }
        };
        attribute.html();
    }

    @Test
    public void testToString() {
        String html = attribute.html();
        assertEquals(html, attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute newAttribute = Attribute.createFromEncoded("unencodedKey", "&quot;encodedValue&quot;");
        assertEquals("unencodedKey", newAttribute.getKey());
        assertEquals("\"encodedValue\"", newAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
        attribute.setKey("data-key");
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(Document.OutputSettings.htmlDefault()));
        attribute.setKey("checked");
        assertFalse(attribute.shouldCollapseAttribute(Document.OutputSettings.htmlDefault()));

        attribute.setKey("selected");
        assertTrue(attribute.shouldCollapseAttribute(Document.OutputSettings.htmlDefault()));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
        attribute.setKey("disabled");
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute newAttribute = new Attribute("key", "value");
        assertTrue(attribute.equals(newAttribute));
        assertTrue(newAttribute.equals(attribute));
    }

    @Test
    public void testHashCode() {
        Attribute newAttribute = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), newAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertNotSame(attribute, clonedAttribute);
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}