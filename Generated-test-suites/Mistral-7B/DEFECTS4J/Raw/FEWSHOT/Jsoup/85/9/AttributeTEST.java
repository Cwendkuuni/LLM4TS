package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testCreateAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        assertNull(attrWithParent.getParent());
        assertEquals("key", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testSetKeyNull() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey(null);
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testSetValueNull() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue(null);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        String actual = StringUtil.releaseBuilder(sb);
        String expected = "key=\"value\"";
        assertEquals(expected, actual);
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        boolean result = Attribute.shouldCollapseAttribute("key", null, out);
        assertFalse(result);

        result = Attribute.shouldCollapseAttribute("key", "", out);
        assertTrue(result);

        result = Attribute.shouldCollapseAttribute("key", "key", out);
        assertTrue(result);

        result = Attribute.shouldCollapseAttribute("key", "value", out);
        assertFalse(result);

        out.syntax(Document.OutputSettings.Syntax.xml);
        result = Attribute.shouldCollapseAttribute("key", null, out);
        assertFalse(result);

        result = Attribute.shouldCollapseAttribute("key", "", out);
        assertFalse(result);

        result = Attribute.shouldCollapseAttribute("key", "key", out);
        assertTrue(result);

        result = Attribute.shouldCollapseAttribute("key", "value", out);
        assertFalse(result);
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertTrue(Attribute.isBooleanAttribute("disabled"));
        assertTrue(Attribute.isBooleanAttribute("required"));
        assertFalse(Attribute.isBooleanAttribute("class"));
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");
        assertEquals(attr1, attr1);
        assertEquals(attr1, attr2);
        assertNotEquals(attr1, attr3);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneException() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}