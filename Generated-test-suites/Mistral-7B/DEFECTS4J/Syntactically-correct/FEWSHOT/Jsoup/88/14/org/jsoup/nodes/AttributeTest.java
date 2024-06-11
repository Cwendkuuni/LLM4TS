package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes.OutputSettings;
import org.jsoup.nodes.Attributes.Syntax;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        Attribute attrWithParent = new Attribute("key", "value", new Attributes());

        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.getParent());

        assertEquals("key", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());
        assertNotNull(attrWithParent.getParent());
    }

    @Test
    public void testAttributeCreationWithParent() {
        Attributes parent = new Attributes();
        Attribute attr = new Attribute("key", "value", parent);

        parent.put(attr);
        assertEquals("key", parent.keyAt(0));
        assertEquals("value", parent.valueAt(0));

        attr.setKey("newKey");
        assertEquals("newKey", parent.keyAt(0));
        assertEquals("value", parent.valueAt(0));

        attr.setValue("newValue");
        assertEquals("newKey", parent.keyAt(0));
        assertEquals("newValue", parent.valueAt(0));
    }

    @Test
    public void testGetStringKey() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
    }

    @Test
    public void testSetStringKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test
    public void testGetStringValue() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testSetStringValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
    }

    @Test
    public void testHtml() throws IOException {
        Document doc = new Document();
        Attribute attr = new Attribute("key", "value");

        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, doc.outputSettings());

        String expected = "key=\"value\"";
        assertEquals(expected, StringUtil.releaseBuilder(sb).toString());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);

        String key = "checked";
        String val = null;
        assertTrue(Attribute.shouldCollapseAttribute(out));

        val = "";
        assertTrue(Attribute.shouldCollapseAttribute(key, val, out));

        val = "checked";
        assertTrue(Attribute.shouldCollapseAttribute(key, val, out));

        key = "class";
        val = "foo";
        assertFalse(Attribute.shouldCollapseAttribute(key, val, out));

        val = "class='foo'";
        assertFalse(Attribute.shouldCollapseAttribute(key, val, out));

        val = "class='foo bar'";
        assertFalse(Attribute.shouldCollapseAttribute(key, val, out));

        key = "data-foo";
        val = "bar";
        assertFalse(Attribute.shouldCollapseAttribute(key, val, out));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertTrue(Attribute.isBooleanAttribute("disabled"));
        assertFalse(Attribute.isBooleanAttribute("class"));
        assertFalse(Attribute.isBooleanAttribute("style"));
    }

    @Test
    public void testCreateFromEncoded() {
        String encodedKey = "data-foo";
        String encodedValue = "bar=1";

        Attribute attr = Attribute.createFromEncoded(encodedKey.substring(5), encodedValue);

        assertEquals("foo", attr.getKey());
        assertEquals("bar=1", attr.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(Attribute.isDataAttribute("data-foo"));
        assertFalse(Attribute.isDataAttribute("class"));
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");

        assertTrue(attr1.equals(attr1));
        assertTrue(attr1.equals(attr2));
        assertFalse(attr1.equals(attr3));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");

        assertEquals(attr1.hashCode(), attr1.hashCode());
        assertEquals(attr1.hashCode(), attr2.hashCode());
        assertNotEquals(attr1.hashCode(), attr3.hashCode());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();

        assertEquals(attr, clonedAttr);
        assertNotSame(attr, clonedAttr);
        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
    }
}