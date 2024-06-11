package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.hamcrest.core.IsInstanceOf;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class AttributeTest {
    @Test
    public void testNewAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.getParent());
    }

    @Test
    public void testNewAttributeWithParent() {
        Attributes parent = new Attributes();
        Attribute attr = new Attribute("key", "value", parent);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertSame(parent, attr.getParent());
    }

    @Test
    public void testSettingKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test
    public void testSettingNullKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey(null);
        assertNull(attr.getKey());
    }

    @Test
    public void testSettingValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
    }

    @Test
    public void testSettingNullValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue(null);
        assertNull(attr.getValue());
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));

        StringBuilder sb2 = StringUtil.borrowBuilder();
        attr.html(sb2, new Document("").outputSettings()); // no need to call encode again
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb2));

        StringBuilder sb3 = StringUtil.borrowBuilder();
        attr.html(sb3, new org.jsoup.nodes.Document.OutputSettings().syntax(Document.OutputSettings.Syntax.xml));
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb3));
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertTrue(attr.shouldCollapseAttribute(new Document.OutputSettings()));
        assertTrue(attr.shouldCollapseAttribute(new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html)));
        assertTrue(attr.shouldCollapseAttribute(new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.xml)));
        assertFalse(attr.shouldCollapseAttribute(new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html).putBoolean("html5", true)));
        assertEquals("key=\"value\"", attr.html());
    }

    @Test
    public void testBooleanAttribute() {
        Attribute attr = new Attribute("checked", null);
        assertTrue(attr.isBooleanAttribute());
        attr.setValue("true");
        assertTrue(attr.isBooleanAttribute());
        attr.setValue("false");
        assertFalse(attr.isBooleanAttribute());
        attr.setValue(null);
        assertTrue(attr.isBooleanAttribute());

        Attribute attr2 = new Attribute("notBoolean", null);
        assertFalse(attr2.isBooleanAttribute());
    }
}