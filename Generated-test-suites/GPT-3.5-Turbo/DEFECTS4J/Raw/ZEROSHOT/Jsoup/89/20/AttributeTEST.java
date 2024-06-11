import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testAttributeConstructorAndGetters() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testAttributeSetters() {
        Attribute attribute = new Attribute("key", "value");

        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());

        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testAttributeHtml() {
        Attribute attribute = new Attribute("key", "value");
        String html = attribute.html();
        assertEquals("key=\"value\"", html);
    }

    @Test
    public void testAttributeToString() {
        Attribute attribute = new Attribute("key", "value");
        String str = attribute.toString();
        assertEquals("key=\"value\"", str);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "&lt;value&gt;");
        assertEquals("key", attribute.getKey());
        assertEquals("<value>", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());

        attribute.setKey("not-data-key");
        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "value");

        Document.OutputSettings outHtml = new Document.OutputSettings();
        outHtml.syntax(Document.OutputSettings.Syntax.html);

        assertTrue(attribute.shouldCollapseAttribute(outHtml));

        attribute.setKey("boolKey");
        assertTrue(attribute.shouldCollapseAttribute(outHtml));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute1 = new Attribute("allowfullscreen", null);
        assertTrue(attribute1.isBooleanAttribute());

        Attribute attribute2 = new Attribute("customAttr", null);
        assertFalse(attribute2.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute1, attribute2);

        Attribute attribute3 = new Attribute("key", "differentValue");
        assertNotEquals(attribute1, attribute3);

        Attribute attribute4 = new Attribute("differentKey", "value");
        assertNotEquals(attribute1, attribute4);
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute1.hashCode(), attribute2.hashCode());

        Attribute attribute3 = new Attribute("key", "differentValue");
        assertNotEquals(attribute1.hashCode(), attribute3.hashCode());

        Attribute attribute4 = new Attribute("differentKey", "value");
        assertNotEquals(attribute1.hashCode(), attribute4.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = attribute1.clone();
        assertEquals(attribute1, attribute2);
        assertNotSame(attribute1, attribute2);
    }
}