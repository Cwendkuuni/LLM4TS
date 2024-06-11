package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testCreation() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());

        Attribute attrParent = new Attributes();
        attr = new Attribute("key", "value", attrParent);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertEquals(attr, attrParent.get("key"));
    }

    @Test
    public void testKeySetter() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());

        Attribute attrParent = new Attributes();
        attr = new Attribute("key", "value", attrParent);
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
        assertNull(attrParent.get("key"));
        assertEquals(attr, attrParent.get("newKey"));
    }

    @Test
    public void testValueSetter() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());

        Attribute attrParent = new Attributes();
        attr = new Attribute("key", "value", attrParent);
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
        assertEquals("value", attrParent.get("key").getValue());
        assertEquals(attr, attrParent.get("key"));
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");

        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        String html = StringUtil.releaseBuilder(sb);
        assertEquals("key=\"value\"", html);

        attr = Attribute.createFromEncoded("key", "key=\"value\"");
        html = attr.html();
        assertEquals("key=\"value\"", html);
    }

    @Test
    public void testBooleanAttributes() {
        Attribute attr = new Attribute("checked", null);
        assertTrue(Attribute.isBooleanAttribute("checked"));

        attr = new Attribute("", "checked");
        assertTrue(attr.isDataAttribute());
        assertTrue(attr.isBooleanAttribute());

        attr = new Attribute("checked", "");
        assertTrue(attr.isDataAttribute());
        assertTrue(attr.isBooleanAttribute());

        attr = new Attribute("checked", "true");
        assertTrue(attr.isDataAttribute());
        assertTrue(attr.isBooleanAttribute());
        assertEquals("checked=\"true\"", attr.html());

        attr = new Attribute("checked", "false");
        assertTrue(attr.isDataAttribute());
        assertTrue(attr.isBooleanAttribute());
        assertEquals("checked=\"false\"", attr.html());

        attr = new Attribute("checked", null);
        assertFalse(attr.shouldCollapseAttribute(null));
        assertFalse(attr.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));

        attr = new Attribute("checked", "");
        assertTrue(attr.shouldCollapseAttribute(null));
        assertTrue(attr.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));

        attr = new Attribute("checked", "true");
        assertFalse(attr.shouldCollapseAttribute(null));
        assertFalse(attr.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));

        attr = new Attribute("checked", "false");
        assertFalse(attr.shouldCollapseAttribute(null));
        assertFalse(attr.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));

        attr = new Attribute("checked", null);
        attr.setValue("true");
        assertFalse(attr.shouldCollapseAttribute(null));
        assertFalse(attr.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));

        attr = new Attribute("checked", null);
        attr.setValue("false");
        assertFalse(attr.shouldCollapseAttribute(null));
        assertFalse(attr.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));

        attr = new Attribute("checked", "true");
        assertFalse(attr.shouldCollapseAttribute(null));
        assertFalse(attr.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));

        attr = new Attribute("checked", "false");
        assertFalse(attr.shouldCollapseAttribute(null));
        assertFalse(attr.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));

        attr = new Attribute("checked", null);
        attr.setKey("foo");
        assertFalse(attr.shouldCollapseAttribute(null));
        assertFalse(attr.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));

        attr = new Attribute("checked", "true");
        attr.setKey("foo");
        assertFalse(attr.shouldCollapseAttribute(null));
        assertFalse(attr.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));

        attr = new Attribute("checked", "false");
        attr.setKey("foo");
        assertFalse(attr.shouldCollapseAttribute(null));
        assertFalse(attr.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "val1");
        Attribute attr2 = new Attribute("key1", "val1");
        Attribute attr3 = new Attribute("key2", "val1");
        Attribute attr4 = new Attribute("key1", null);
        Attribute attr5 = new Attribute("key1", null);

        assertEquals(attr1, attr1);
        assertEquals(attr1, attr2);
        assertNotEquals(attr1, attr3);
        assertNotEquals(attr1, attr4);
        assertNotEquals(attr1, attr5);
        assertNotEquals(attr2, attr3);
        assertNotEquals(attr2, attr4);
        assertNotEquals(attr2, attr5);
        assertNotEquals(attr3, attr4);
        assertNotEquals(attr3, attr5);
        assertNotEquals(attr4, attr5);
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "val1");
        Attribute attr2 = new Attribute("key1", "val1");
        Attribute attr3 = new Attribute("key2", "val1");
        Attribute attr4 = new Attribute("key1", null);
        Attribute attr5 = new Attribute("key1", null);

        int hashCode1 = attr1.hashCode();
        int hashCode2 = attr2.hashCode();
        int hashCode3 = attr3.hashCode();
        int hashCode4 = attr4.hashCode();
        int hashCode5 = attr5.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode1, hashCode5);
        assertNotEquals(hashCode2, hashCode3);
        assertNotEquals(hashCode2, hashCode4);
        assertNotEquals(hashCode2, hashCode5);
        assertNotEquals(hashCode3, hashCode4);
        assertNotEquals(hashCode3, hashCode5);
        assertNotEquals(hashCode4, hashCode5);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr, clonedAttr);
    }
}