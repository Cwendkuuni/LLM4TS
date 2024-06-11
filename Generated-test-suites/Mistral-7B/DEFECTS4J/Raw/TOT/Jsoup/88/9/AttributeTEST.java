import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

public class AttributeTest {

    @Test
    public void testAttributeConstruction() {
        Attribute attr = new Attribute("key", "value");
        Attribute attrWithParent = new Attribute("key", "value", new Attributes());

        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertEquals("key", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());
        assertNull(attr.getParent());
        assertNotNull(attrWithParent.getParent());
    }

    @Test
    public void testAttributeSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");

        assertEquals("newKey", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeSetNullKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey(null);
    }

    @Test
    public void testAttributeSetValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");

        assertEquals("key", attr.getKey());
        assertEquals("newValue", attr.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeSetNullValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue(null);
    }

    @Test
    public void testAttributeHtmlEncoded() {
        Attribute attr = new Attribute("key", "&amp;value");
        assertEquals("key=\"&amp;value\"", attr.html());
    }

    @Test
    public void testAttributeHtml() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key=\"value\"", attr.html());
    }

    @Test
    public void testAttributeToString() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key=\"value\"", attr.toString());
    }

    @Test
    public void testAttributeCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "&amp;value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testAttributeIsDataAttribute() {
        Attribute attr = new Attribute("data-key", null);
        assertTrue(attr.isDataAttribute());

        attr = new Attribute("key", null);
        assertFalse(attr.isDataAttribute());
    }

    @Test
    public void testAttributeShouldCollapseAttribute() {
        Attribute attr = new Attribute("key", "");
        assertTrue(attr.shouldCollapseAttribute(null));

        attr = new Attribute("checked", null);
        assertTrue(attr.shouldCollapseAttribute(null));

        attr = new Attribute("key", "value");
        assertFalse(attr.shouldCollapseAttribute(null));
    }

    @Test
    public void testAttributeShouldCollapseAttributeWithOutputSettings() {
        Attribute attr = new Attribute("key", "");
        assertTrue(attr.shouldCollapseAttribute(Document.OutputSettings.html()));

        attr = new Attribute("checked", null);
        assertTrue(attr.shouldCollapseAttribute(Document.OutputSettings.html()));

        attr = new Attribute("key", "value");
        assertFalse(attr.shouldCollapseAttribute(Document.OutputSettings.html()));
    }

    @Test
    public void testAttributeClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();

        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
        assertEquals(attr.hashCode(), clonedAttr.hashCode());
    }

    @Test
    public void testAttributeEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");

        assertEquals(attr1, attr1);
        assertEquals(attr1, attr2);
        assertNotEquals(attr1, attr3);
    }
}