import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.SerializationException;
import org.jsoup.internal.StringUtil;
import org.jsoup.helper.Validate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.*;

public class AttributeTest {

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
        String oldValue = attribute.setValue("newValue");
        assertEquals("value", oldValue);
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.html());
    }

    @Test(expected = SerializationException.class)
    public void testHtmlIOException() {
        Attribute attribute = new Attribute("key", "value") {
            @Override
            protected void html(Appendable accum, Document.OutputSettings out) throws IOException {
                throw new IOException();
            }
        };
        attribute.html();
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testIsNotDataAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "key", null) {
            @Override
            protected final boolean shouldCollapseAttribute(Document.OutputSettings out) {
                return true;
            }
        };
        assertTrue(attribute.shouldCollapseAttribute(null));
    }

    @Test
    public void testShouldNotCollapseAttribute() {
        Attribute attribute = new Attribute("key", "value", null) {
            @Override
            protected final boolean shouldCollapseAttribute(Document.OutputSettings out) {
                return false;
            }
        };
        assertFalse(attribute.shouldCollapseAttribute(null));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value", null) {
            @Override
            protected boolean isBooleanAttribute() {
                return true;
            }
        };
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testIsNotBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value", null) {
            @Override
            protected boolean isBooleanAttribute() {
                return false;
            }
        };
        assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute1.equals(attribute2));
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals(attribute.getKey().hashCode(), attribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clone = attribute.clone();
        assertNotSame(attribute, clone);
        assertEquals(attribute.getKey(), clone.getKey());
        assertEquals(attribute.getValue(), clone.getValue());
    }

}