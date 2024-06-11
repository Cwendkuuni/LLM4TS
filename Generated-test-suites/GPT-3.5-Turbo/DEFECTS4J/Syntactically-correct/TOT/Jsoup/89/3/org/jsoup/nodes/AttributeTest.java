import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.io.IOException;

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
    public void testAttributeConstructor() {
        assertNotNull(attribute);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
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
        Attribute newAttribute = Attribute.createFromEncoded("encodedKey", "encodedValue");
        assertEquals("encodedKey", newAttribute.getKey());
        assertEquals("encodedValue", newAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
        Attribute dataAttribute = new Attribute("data-key", "value");
        assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
        Attribute collapsibleAttribute = new Attribute("value", "value");
        assertFalse(collapsibleAttribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attributeCopy = new Attribute("key", "value");
        assertEquals(attribute, attributeCopy);
        Attribute attributeDifferent = new Attribute("differentKey", "value");
        assertNotEquals(attribute, attributeDifferent);
    }

    @Test
    public void testHashCode() {
        Attribute attributeCopy = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), attributeCopy.hashCode());
    }

    @Test
    public void testClone() {
        Attribute cloneAttribute = attribute.clone();
        assertEquals(attribute, cloneAttribute);
    }
}