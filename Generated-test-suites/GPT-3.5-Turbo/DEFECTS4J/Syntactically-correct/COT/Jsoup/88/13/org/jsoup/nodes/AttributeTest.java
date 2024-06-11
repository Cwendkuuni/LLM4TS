import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.SerializationException;
import org.jsoup.internal.StringUtil;
import org.jsoup.helper.Validate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

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
        assertEquals("key=\"value\"", attribute.html());
    }

    @Test(expected = SerializationException.class)
    public void testHtmlIOException() throws IOException {
        attribute.html(new Appendable() {
            public Appendable append(CharSequence csq) throws IOException {
                throw new IOException();
            }
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                throw new IOException();
            }
            public Appendable append(char c) throws IOException {
                throw new IOException();
            }
        }, new Document.OutputSettings());
    }

    @Test
    public void testToString() {
        assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "&amp;");
        assertEquals("key", attr.getKey());
        assertEquals("&", attr.getValue());
    }

    @Test
    public void testIsDataElement() {
        assertFalse(attribute.isDataAttribute());
        assertTrue(Attribute.isDataAttribute("data-key"));
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
        assertTrue(Attribute.shouldCollapseAttribute("key", "key", new Document.OutputSettings()));
    }

    @Test
    public void testEquals() {
        assertTrue(attribute.equals(new Attribute("key", "value")));
        assertFalse(attribute.equals(new Attribute("key", "newValue")));
        assertFalse(attribute.equals(new Attribute("newKey", "value")));
        assertFalse(attribute.equals(null));
        assertFalse(attribute.equals("string"));
    }

    @Test
    public void testHashCode() {
        assertEquals(attribute.hashCode(), new Attribute("key", "value").hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttr = attribute.clone();
        assertNotSame(attribute, clonedAttr);
        assertEquals(attribute.getKey(), clonedAttr.getKey());
        assertEquals(attribute.getValue(), clonedAttr.getValue());
    }

    // Additional test cases for edge cases and exceptions

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyKey() {
        new Attribute("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyWithEmptyKey() {
        attribute.setKey("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyWithNullKey() {
        attribute.setKey(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetValueWithNullValue() {
        attribute.setValue(null);
    }
}