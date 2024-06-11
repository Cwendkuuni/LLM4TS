
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testCreateAttribute() {
        Attribute attribute = new Attribute("name", "John Doe");
        assertEquals("name", attribute.getKey());
        assertEquals("John Doe", attribute.getValue());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("name", "John Doe");
        attribute.setKey("email");
        assertEquals("email", attribute.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("name", "John Doe");
        attribute.setValue("john@doe.com");
        assertEquals("john@doe.com", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("name", "John Doe");
        String html = attribute.html();
        assertEquals("name=\"John Doe\"", html);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlSerializationException() {
        Attribute attribute = new Attribute("name", "John Doe");
        attribute.setParent(null); // Set parent to null to simulate an exception
        String html = attribute.html();
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("name", "John&#32;Doe");
        assertEquals("John Doe", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-name", "John Doe");
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute1 = new Attribute("name", "");
        Attribute attribute2 = new Attribute("name", "name");
        Attributes attributes = new Attributes();
        attributes.put("name", "John Doe");
        Attribute attribute3 = attributes.get("name");
        Attributes.OutputSettings outputSettings = new Attributes.OutputSettings();
        assertTrue(attribute1.shouldCollapseAttribute(outputSettings));
        assertTrue(attribute2.shouldCollapseAttribute(outputSettings));
        assertFalse(attribute3.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute1 = new Attribute("checked", "");
        Attribute attribute2 = new Attribute("name", "John Doe");
        assertTrue(attribute1.isBooleanAttribute());
        assertFalse(attribute2.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("name", "John Doe");
        Attribute attribute2 = new Attribute("name", "John Doe");
        Attribute attribute3 = new Attribute("email", "john@doe.com");
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("name", "John Doe");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}

