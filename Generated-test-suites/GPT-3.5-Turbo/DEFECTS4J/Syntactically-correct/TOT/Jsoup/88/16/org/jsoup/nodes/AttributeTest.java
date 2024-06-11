
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AttributeTest {
    private Attribute attribute;
    private Attributes parent;

    @Before
    public void setUp() {
        attribute = new Attribute("key", "value");
        parent = new Attributes();
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
    public void testSetKeyWithParent() {
        attribute = new Attribute("key", "value", parent);
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        assertEquals("newKey", parent.keySet().iterator().next());
    }

    @Test
    public void testGetValue() {
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testSetValueWithParent() {
        attribute = new Attribute("key", "value", parent);
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertEquals("newValue", parent.get("key"));
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
    public void testEquals() {
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute, attribute2);
    }

    @Test
    public void testHashCode() {
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), attribute2.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "&quot;value&quot;");
        assertEquals("key", attribute.getKey());
        assertEquals("\"value\"", attribute.getValue());
    }

    @Test
    public void testShouldCollapseAttributeWithNullValue() {
        Attribute attribute = new Attribute("name", null);
        Attributes parent = new Attributes();
        parent.put("name", null);
        attribute.parent = parent;
        assertEquals(true, attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testShouldCollapseAttributeWithValueSameAsKey() {
        Attribute attribute = new Attribute("name", "name");
        Attributes parent = new Attributes();
        parent.put("name", "name");
        attribute.parent = parent;
        assertEquals(true, attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testShouldCollapseAttributeWithValueEmpty() {
        Attribute attribute = new Attribute("name", "");
        Attributes parent = new Attributes();
        parent.put("name", "");
        attribute.parent = parent;
        assertEquals(true, attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testShouldCollapseAttributeWithValueNotSameAsKey() {
        Attribute attribute = new Attribute("name", "value");
        Attributes parent = new Attributes();
        parent.put("name", "value");
        attribute.parent = parent;
        assertEquals(false, attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertEquals(true, Attribute.isBooleanAttribute("checked"));
        assertEquals(false, Attribute.isBooleanAttribute("name"));
    }

    @Test
    public void testIsDataAttributeWithValidKey() {
        assertEquals(true, Attribute.isDataAttribute("data-key"));
    }

    @Test
    public void testIsDataAttributeWithInvalidKey() {
        assertEquals(false, Attribute.isDataAttribute("key"));
    }
}
