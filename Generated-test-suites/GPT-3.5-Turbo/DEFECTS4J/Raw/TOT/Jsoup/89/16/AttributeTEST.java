import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AttributeTest {

    private Attribute attribute;

    @Before
    public void setup() {
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

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyWithNull() {
        attribute.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyWithEmpty() {
        attribute.setKey("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueWithNull() {
        attribute.setValue(null);
    }

    @Test
    public void testHtml() {
        assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    public void testToString() {
        assertEquals(attribute.html(), attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute encodedAttribute = Attribute.createFromEncoded("key", "&quot;value&quot;");
        assertEquals("key", encodedAttribute.getKey());
        assertEquals("\"value\"", encodedAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute equalAttribute = new Attribute("key", "value");

        assertEquals(attribute, equalAttribute);
        assertEquals(attribute.hashCode(), equalAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
        assertTrue(attribute != clonedAttribute);
    }
}