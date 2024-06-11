
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AttributeTest {

    private Attribute attribute;
    private Attributes attributes;

    @Before
    public void setup() {
        attributes = new Attributes();
        attribute = new Attribute("key", "value", attributes);
    }

    @Test
    public void testGetKey() {
        assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        assertEquals("value", attributes.get("newKey"));
    }

    @Test
    public void testGetValue() {
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertEquals("newValue", attributes.get("key"));
    }

    @Test
    public void testHtml() {
        assertEquals("key=\"value\"", attribute.html());
    }

    @Test(expected = SerializationException.class)
    public void testHtmlWithException() {
        attribute = new Attribute("key", "value", null);
        attribute.html();
    }

    @Test
    public void testFromString() {
        Attribute newAttribute = Attribute.createFromEncoded("newKey", "new&amp;Value");
        assertEquals("newKey", newAttribute.getKey());
        assertEquals("new&Value", newAttribute.getValue());
        assertNull(newAttribute.parent);
    }

    @Test
    public void testIsDataAttribute() {
        attribute = new Attribute("data-key", "value", null);
        assertEquals(true, attribute.isDataAttribute());
        attribute = new Attribute("key", "value", null);
        assertEquals(false, attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertEquals(false, attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        attribute = new Attribute("async", "value", null);
        assertEquals(true, attribute.isBooleanAttribute());
        attribute = new Attribute("key", "value", null);
        assertEquals(false, attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        attribute = new Attribute("key", "value", null);
        Attribute sameAttribute = new Attribute("key", "value", null);
        assertEquals(true, attribute.equals(sameAttribute));
        Attribute differentAttribute = new Attribute("anotherKey", "value", null);
        assertEquals(false, attribute.equals(differentAttribute));
        assertEquals(false, attribute.equals(null));
        assertEquals(false, attribute.equals("key"));
    }

    @Test
    public void testHashCode() {
        attribute = new Attribute("key", "value", null);
        Attribute sameAttribute = new Attribute("key", "value", null);
        assertEquals(attribute.hashCode(), sameAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute cloneAttribute = attribute.clone();
        assertEquals(attribute.getKey(), cloneAttribute.getKey());
        assertEquals(attribute.getValue(), cloneAttribute.getValue());
        assertNull(cloneAttribute.parent);
    }
}
