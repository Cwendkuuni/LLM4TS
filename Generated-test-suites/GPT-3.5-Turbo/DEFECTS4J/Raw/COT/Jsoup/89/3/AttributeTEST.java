
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {
    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("key", "val");
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
        assertEquals("val", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        attribute.setValue("newVal");
        assertEquals("newVal", attribute.getValue());
    }

    @Test
    public void testHtml() {
        assertEquals("key=\"val\"", attribute.html());
    }

    @Test
    public void testToString() {
        assertEquals("key=\"val\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute encodedAttribute = Attribute.createFromEncoded("key", "amp;");
        assertEquals("key", encodedAttribute.getKey());
        assertEquals("&", encodedAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
        assertTrue(Attribute.isDataAttribute("data-key"));
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
        assertFalse(Attribute.shouldCollapseAttribute("key", "val", new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
        assertTrue(Attribute.isBooleanAttribute("checked"));
    }

    @Test
    public void testEquals() {
        Attribute equalAttribute = new Attribute("key", "val");
        assertEquals(attribute, equalAttribute);
    }

    @Test
    public void testHashCode() {
        Attribute equalAttribute = new Attribute("key", "val");
        assertEquals(attribute.hashCode(), equalAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }
}
