package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class AttributeTest {

    @Test
    public void testCreateAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.getParent());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        assertEquals("key", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());
        assertNotNull(attrWithParent.getParent());
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
        assertEquals("value", attr.getValue());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        attrWithParent.setKey("newKey");
        assertEquals("newKey", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());
        assertEquals(0, attrWithParent.getParent().size());
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("key", attr.getKey());
        assertEquals("newValue", attr.getValue());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        attrWithParent.setValue("newValue");
        assertEquals("key", attrWithParent.getKey());
        assertEquals("newValue", attrWithParent.getValue());
        assertEquals(0, attrWithParent.getParent().size());
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        assertEquals("key=\"value\"", sb.toString());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        sb = StringUtil.borrowBuilder();
        attrWithParent.html(sb, new Document("").outputSettings());
        assertEquals("key=\"value\"", sb.toString());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isDataAttribute());

        Attribute dataAttr = new Attribute("data-key", "value");
        assertTrue(dataAttr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        Attribute attr = new Attribute("key", "");
        assertTrue(attr.shouldCollapseAttribute(out));

        Attribute attrValueSame = new Attribute("key", "key");
        assertTrue(attrValueSame.shouldCollapseAttribute(out));

        Attribute attrBoolean = new Attribute("boolean", "");
        assertTrue(attrBoolean.shouldCollapseAttribute(out));

        Attribute attrNotBoolean = new Attribute("notBoolean", "");
        assertFalse(attrNotBoolean.shouldCollapseAttribute(out));

        Attribute attrNull = new Attribute(null, null);
        assertFalse(attrNull.shouldCollapseAttribute(out));
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        assertFalse(attr1.equals(attr2));

        Attribute attr3 = new Attribute("key1", "value1");
        assertTrue(attr1.equals(attr3));

        Attribute attr4 = new Attribute("key1", null);
        assertTrue(attr1.equals(attr4));

        Attribute attr5 = new Attribute(null, "value1");
        assertTrue(attr5.equals(attr1));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");
        assertEquals(attr1.hashCode(), attr3.hashCode());
        assertNotEquals(attr1.hashCode(), attr2.hashCode());

        Attribute attr4 = new Attribute("key1", null);
        assertEquals(attr1.hashCode(), attr4.hashCode());

        Attribute attr5 = new Attribute(null, "value1");
        assertEquals(attr5.hashCode(), attr1.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
        assertNull(clonedAttr.getParent());
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneException() {
        Attribute attr = new Attribute("key", "value");
        attr.clone();
    }
}