import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.SerializationException;
import org.jsoup.internal.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    // List of public methods and their signatures:
    // public String getKey()
    // public void setKey(String key)
    // public String getValue()
    // public String setValue(String val)
    // public void html(Appendable accum, Document.OutputSettings out) throws IOException
    // public String toString()
    // public static Attribute createFromEncoded(String unencodedKey, String encodedValue)
    // protected boolean isDataAttribute()
    // protected static boolean isDataAttribute(String key)
    // protected final boolean shouldCollapseAttribute(Document.OutputSettings out)
    // protected static boolean shouldCollapseAttribute(final String key, final String val, final Document.OutputSettings out)
    // protected boolean isBooleanAttribute()
    // protected static boolean isBooleanAttribute(final String key)
    // public boolean equals(Object o)
    // public int hashCode()
    // public Attribute clone()

    // Basic test cases for each method

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("value", attribute.setValue("newValue"));
        assertEquals("newValue", attribute.getValue());
    }
    
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        
        try {
            attribute.html(sb, out);
        } catch(IOException exception) {
            fail("IOException thrown");
        }
        
        assertEquals("key=\"value\"", sb.toString());
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "&quot;value&quot;");
        assertEquals("key", attribute.getKey());
        assertEquals("\"value\"", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute1 = new Attribute("key", "value");
        assertFalse(attribute1.isDataAttribute());

        Attribute attribute2 = new Attribute("data-key", "value");
        assertTrue(attribute2.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute1 = new Attribute("key", "key");
        Document.OutputSettings out1 = new Document.OutputSettings();
        assertFalse(attribute1.shouldCollapseAttribute(out1));

        Attribute attribute2 = new Attribute("key", "");
        Document.OutputSettings out2 = new Document.OutputSettings();
        assertTrue(attribute2.shouldCollapseAttribute(out2));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute1 = new Attribute("key", "value");
        assertFalse(attribute1.isBooleanAttribute());

        Attribute attribute2 = new Attribute("autofocus", null);
        assertTrue(attribute2.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key", "differentValue");
        Attribute attribute4 = new Attribute("differentKey", "value");

        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
        assertFalse(attribute1.equals(attribute4));
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key", "differentValue");
        Attribute attribute4 = new Attribute("differentKey", "value");

        assertEquals(attribute1.hashCode(), attribute2.hashCode());
        assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
        assertNotEquals(attribute1.hashCode(), attribute4.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = attribute1.clone();

        assertEquals(attribute1.getKey(), attribute2.getKey());
        assertEquals(attribute1.getValue(), attribute2.getValue());
    }

    // Test cases for potential edge cases and exception handling scenarios

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyKey() {
        new Attribute("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullKey() {
        new Attribute(null, "value");
    }

    @Test
    public void testSetKeyWithParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        assertEquals("newKey", parent.get(0));
    }

    @Test
    public void testSetValueWithParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        attribute.setValue("newValue");
        assertEquals("value", attribute.setValue("newValue"));
        assertEquals("newValue", attribute.getValue());
        assertEquals("newValue", parent.get(0));
    }

    @Test
    public void testHtmlWithException() {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        try {
            attribute.html(sb, out);
            fail("IOException not thrown");
        } catch(IOException exception) {
            assertTrue(exception instanceof SerializationException);
        }
    }
}

