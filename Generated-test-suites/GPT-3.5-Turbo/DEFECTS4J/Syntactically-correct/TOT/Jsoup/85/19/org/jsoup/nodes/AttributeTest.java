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
        assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    public void testToString() {
        assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("encodedKey", "&lt;value&gt;");
        assertEquals("encodedKey", attr.getKey());
        assertEquals("<value>", attr.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
        Attribute dataAttribute = new Attribute("data-key", "value");
        assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
        Document.OutputSettings out = new Document.OutputSettings();
        assertTrue(attribute.shouldCollapseAttribute(out));

        attribute.setKey("collapsedKey");
        attribute.setValue("collapsedKey");
        assertTrue(attribute.shouldCollapseAttribute(out));

        attribute.setValue("");
        assertTrue(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
        Attribute booleanAttribute = new Attribute("boolean", null);
        assertTrue(booleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute.equals(attribute2));
        assertFalse(attribute.equals(null));
        assertFalse(attribute.equals("string"));
        assertFalse(attribute.equals(new Attribute("newKey", "value")));
        assertFalse(attribute.equals(new Attribute("key", "newValue")));
    }

    @Test
    public void testHashCode() {
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), attribute2.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clone = attribute.clone();
        assertEquals(attribute.getKey(), clone.getKey());
        assertEquals(attribute.getValue(), clone.getValue());
        assertNotSame(attribute, clone);
    }
}