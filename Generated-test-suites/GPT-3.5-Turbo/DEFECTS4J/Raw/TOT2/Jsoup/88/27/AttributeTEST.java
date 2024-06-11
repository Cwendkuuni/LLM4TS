import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test
    public void testGetValue() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("value", attr.setValue("newValue"));
        assertEquals("newValue", attr.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attr = new Attribute("html", "<body>");
        assertEquals("html=\"&lt;body&gt;\"", attr.html());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("url", "http://example.com");
        assertEquals("url", attr.getKey());
        assertEquals("http://example.com", attr.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("data-id", "123");
        assertTrue(attr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attr = new Attribute("div", "div");
        assertTrue(attr.shouldCollapseAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("required", null);
        assertTrue(attr.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key", "value");
        Attribute attr2 = new Attribute("key", "value");
        assertTrue(attr1.equals(attr2));
    }

    @Test
    public void testHashCode() {
        Attribute attr = new Attribute("key", "value");
        assertEquals(3503921, attr.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
    }

}