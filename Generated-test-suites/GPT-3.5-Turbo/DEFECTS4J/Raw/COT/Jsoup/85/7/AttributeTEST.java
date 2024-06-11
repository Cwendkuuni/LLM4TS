
import org.jsoup.nodes.Attribute;
import org.jsoup.helper.Validate;
import org.jsoup.internal.StringUtil;
import org.jsoup.SerializationException;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
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
        assertEquals("newValue", attribute.getValue());
        assertEquals("value", oldValue);
    }

    @Test
    public void testHtml() {
        String html = attribute.html();
        assertEquals("key=\"value\"", html);
    }

    @Test
    public void testHtmlWithException() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        try {
            attribute.html(null, out);
            fail("Expected SerializationException to be thrown");
        } catch (IOException e) {
            fail("Expected SerializationException to be thrown");
        } catch (SerializationException e) {
            // Expected exception
        }
    }

    @Test
    public void testToString() {
        String string = attribute.toString();
        assertEquals("key=\"value\"", string);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testIsDataAttributeWithPrefix() {
        Attribute attr = new Attribute("data-key", "value");
        assertTrue(attr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        assertTrue(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");

        assertTrue(attr1.equals(attr1));
        assertTrue(attr1.equals(attr2));
        assertFalse(attr1.equals(attr3));
        assertFalse(attr1.equals(null));
        assertFalse(attr1.equals("key1=value1"));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");

        assertTrue(attr1.hashCode() == attr2.hashCode());
        assertFalse(attr1.hashCode() == attr3.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clone = attribute.clone();

        assertEquals(clone.getKey(), attribute.getKey());
        assertEquals(clone.getValue(), attribute.getValue());
        assertNotSame(clone, attribute);
    }
}
