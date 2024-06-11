package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;

public class AttributeTest {

    @Test
    public void testCreateAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        assertEquals("key", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());
        assertNull(attrWithParent.parent);
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("oldKey", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyEmpty() {
        Attribute attr = new Attribute("oldKey", "value");
        attr.setKey("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyNull() {
        Attribute attr = new Attribute("oldKey", "value");
        attr.setKey(null);
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "oldValue");
        String newValue = "newValue";
        String oldValue = attr.setValue(newValue);
        assertEquals("newValue", attr.getValue());
        assertEquals("oldValue", oldValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueNull() {
        Attribute attr = new Attribute("key", "oldValue");
        attr.setValue(null);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        String html = StringUtil.releaseBuilder(sb);
        assertEquals("key=\"value\"", html);

        StringBuilder sb2 = StringUtil.borrowBuilder();
        attr.html(sb2, new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.xml));
        String xml = StringUtil.releaseBuilder(sb2);
        assertEquals("key=\"value\"", xml);
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("data-key", "value");
        assertTrue(attr.isDataAttribute());

        Attribute attrWithoutPrefix = new Attribute("key", "value");
        assertFalse(attrWithoutPrefix.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        boolean shouldCollapse = Attribute.shouldCollapseAttribute("html", null, new Document.OutputSettings());
        assertFalse(shouldCollapse);

        shouldCollapse = Attribute.shouldCollapseAttribute("html", "", new Document.OutputSettings());
        assertFalse(shouldCollapse);

        shouldCollapse = Attribute.shouldCollapseAttribute("html", "html", new Document.OutputSettings());
        assertTrue(shouldCollapse);

        shouldCollapse = Attribute.shouldCollapseAttribute("html", "false", new Document.OutputSettings());
        assertFalse(shouldCollapse);

        shouldCollapse = Attribute.shouldCollapseAttribute("html", "true", new Document.OutputSettings());
        assertTrue(shouldCollapse);

        shouldCollapse = Attribute.shouldCollapseAttribute("html", "html", new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.xml));
        assertFalse(shouldCollapse);
    }

    @Test
    public void testIsBooleanAttribute() {
        assertFalse(Attribute.isBooleanAttribute("key"));

        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.isBooleanAttribute());
    }
}