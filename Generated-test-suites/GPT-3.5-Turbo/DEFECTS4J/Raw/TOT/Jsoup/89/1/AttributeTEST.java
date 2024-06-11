import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    Attribute attribute;

    // Test cases for constructor
    @Test
    public void testConstructor() {
        attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    // Test cases for getKey()
    @Test
    public void testGetKey() {
        attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
    }

    // Test cases for setKey()
    @Test
    public void testSetKey() {
        attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyNull() {
        attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyEmpty() {
        attribute = new Attribute("key", "value");
        attribute.setKey("");
    }

    // Test cases for getValue()
    @Test
    public void testGetValue() {
        attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
    }

    // Test cases for setValue()
    @Test
    public void testSetValue() {
        attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testSetValueParentNotNull() {
        Attribute parent = new Attribute("parentKey", "parentValue");
        attribute = new Attribute("key", "value", parent);
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertEquals("newValue", parent.get("key"));
    }

    // Test cases for html()
    @Test
    public void testHtml() {
        attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.html());
    }

    // Test cases for toString()
    @Test
    public void testToString() {
        attribute = new Attribute("key", "value");
        assertEquals(attribute.html(), attribute.toString());
    }

    // Test cases for createFromEncoded()
    @Test
    public void testCreateFromEncoded() {
        attribute = Attribute.createFromEncoded("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    // Test cases for isDataAttribute()
    @Test
    public void testIsDataAttribute() {
        attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testIsDataAttributeFalse() {
        attribute = new Attribute("key", "value");
        assertFalse(attribute.isDataAttribute());
    }

    // Test cases for shouldCollapseAttribute()
    @Test
    public void testShouldCollapseAttribute() {
        attribute = new Attribute("key", "value");
        Document.OutputSettings out = new Document.OutputSettings();
        assertTrue(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testShouldCollapseAttributeFalse() {
        attribute = new Attribute("key", "value");
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.xml);

        assertFalse(attribute.shouldCollapseAttribute(out));
    }

    // Test cases for isBooleanAttribute()
    @Test
    public void testIsBooleanAttribute() {
        attribute = new Attribute("allowfullscreen", "value");
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeNullValue() {
        attribute = new Attribute("key", null);
        assertTrue(attribute.isBooleanAttribute());
    }

    // Test cases for equals()
    @Test
    public void testEquals() {
        attribute = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute.equals(attribute2));
    }

    // Test cases for hashCode()
    @Test
    public void testHashCode() {
        attribute = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), attribute2.hashCode());
    }

    // Test cases for clone()
    @Test
    public void testClone() {
        attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }

}