package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;

import java.util.Arrays;

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

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        attrWithParent.setKey("newKey");
        assertEquals("newKey", attrWithParent.getKey());
        assertEquals(0, attrWithParent.getParent().size()); // new key should make old entry obsolete
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        attrWithParent.setValue("newValue");
        assertEquals("newValue", attrWithParent.getValue());
        assertEquals(1, attrWithParent.getParent().size());
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb);
        String expected = String.format("%s=\"%s\"", attr.getKey(), attr.getValue());
        assertEquals(expected, StringUtil.releaseBuilder(sb));

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        StringBuilder sbParent = StringUtil.borrowBuilder();
        attrWithParent.html(sbParent, new Document("").outputSettings());
        String htmlParent = StringUtil.releaseBuilder(sbParent);
        assertEquals("<attribute name=\"key\" " + htmlParent + "/>", attrWithParent.html());
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertFalse(Attribute.isBooleanAttribute("href"));
        assertFalse(new Attribute("key", "").isBooleanAttribute());
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(new Attribute("data-key", "").isDataAttribute());
        assertFalse(new Attribute("key", "").isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        assertTrue(Attribute.shouldCollapseAttribute("checked", null, out));
        assertTrue(Attribute.shouldCollapseAttribute("checked", "", out));
        assertTrue(Attribute.shouldCollapseAttribute("checked", "checked", out));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", out));
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());

        Attribute attrWithParent = new Attribute("key", null, new Attributes());
        attrWithParent.setValue("value");
        Attribute attrFromEncoded = Attribute.createFromEncoded("key", "key=\"" + attrWithParent.html() + '\"');
        assertEquals(attr, attrFromEncoded);
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");

        assertFalse(attr1.equals(attr2));
        assertTrue(attr1.equals(attr3));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");

        assertEquals(attr1.hashCode(), attr3.hashCode());
        assertNotEquals(attr1.hashCode(), attr2.hashCode());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attribute attr = new Attribute("key", "value");
        Attribute cloned = attr.clone();
        assertEquals(attr, cloned);
    }

    @Test
    public void testCreateFromEncodedWithParent() {
        Attributes parent = new Attributes();
        parent.put("key1", new Attribute("key1", "value1", parent));

        Attribute attr = Attribute.createFromEncoded("key1", "key1=\"value1\"");
        assertEquals(parent.get("key1"), attr);
    }
}