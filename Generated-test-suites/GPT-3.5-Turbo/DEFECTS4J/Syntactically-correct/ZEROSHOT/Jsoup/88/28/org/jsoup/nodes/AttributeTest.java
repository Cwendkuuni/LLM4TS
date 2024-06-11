import org.jsoup.SerializationException;
import org.jsoup.internal.StringUtil;
import org.jsoup.helper.Validate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AttributeTest {
    private Attribute attribute;

    @BeforeEach
    public void setUp() {
        attribute = new Attribute("key", "value");
    }

    @Test
    public void testConstructorWithKeyAndValue() {
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
    }

    @Test
    public void testConstructorWithKeyAndValueAndParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(parent, attribute.parent);
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        assertNull(attribute.parent);
    }

    @Test
    public void testSetValue() {
        String oldVal = attribute.setValue("newValue");
        assertEquals("value", oldVal);
        assertEquals("newValue", attribute.getValue());
        assertNull(attribute.parent);
    }

    @Test
    public void testHtml() {
        String expected = "key=\"value\"";
        assertEquals(expected, attribute.html());
    }

    @Test
    public void testHtmlWithIOException() {
        class DocumentMock extends org.jsoup.nodes.Document {
            public DocumentMock(String baseUri) {
                super(baseUri);
            }

            public org.jsoup.nodes.Document.OutputSettings outputSettings() {
                throw new IOException();
            }
        }

        assertThrows(SerializationException.class, () -> attribute.html(new StringBuilder(), new DocumentMock("")));
    }

    @Test
    public void testToString() {
        String expected = "key=\"value\"";
        assertEquals(expected, attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("unencodedKey", "encodedValue");
        assertEquals("unencodedKey", attribute.getKey());
        assertEquals("encodedValue", attribute.getValue());
        assertNull(attribute.parent);
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
        assertTrue(Attribute.isDataAttribute("data-key"));
    }

    @Test
    public void testShouldCollapseAttribute() {
        org.jsoup.nodes.Document.OutputSettings out = new org.jsoup.nodes.Document.OutputSettings();
        assertTrue(attribute.shouldCollapseAttribute(out));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", out));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(attribute.isBooleanAttribute());
        assertFalse(Attribute.isBooleanAttribute("key"));
    }

    @Test
    public void testEquals() {
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key", "newValue");
        assertEquals(attribute, attribute);
        assertEquals(attribute, attribute2);
        assertNotEquals(attribute, attribute3);
        assertNotEquals(attribute, null);
        assertNotEquals(attribute, "key");
    }

    @Test
    public void testHashCode() {
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), attribute2.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clone = attribute.clone();
        assertNotSame(attribute, clone);
        assertEquals(attribute, clone);
    }
}