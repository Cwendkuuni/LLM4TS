package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

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

        // Test the change propagates to the parent Attributes if it exists
        Attributes parent = new Attributes();
        Attribute attrParent = new Attribute("key", "value", parent);
        attrParent.setKey("newKey");
        assertEquals("newKey", parent.get(0).getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());

        // Test the change propagates to the parent Attributes if it exists
        Attributes parent = new Attributes();
        Attribute attrParent = new Attribute("key", "value", parent);
        attrParent.setValue("newValue");
        assertEquals("newValue", parent.get(0).getValue());
    }

    @Test
    public void testHtml() throws IOException {
        String expected = "key=\"value\"";
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        assertEquals(expected, StringUtil.releaseBuilder(sb));

        String expectedEmpty = "key";
        Attribute attrEmpty = new Attribute("key", "");
        StringBuilder sbEmpty = StringUtil.borrowBuilder();
        attrEmpty.html(sbEmpty, new Document("").outputSettings());
        assertEquals(expectedEmpty, StringUtil.releaseBuilder(sbEmpty));
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(new Attribute("data-test", "test").isDataAttribute());
        assertFalse(new Attribute("test", "test").isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertTrue(new Attribute("checked", null).shouldCollapseAttribute(null));
        assertTrue(new Attribute("checked", "").shouldCollapseAttribute(null));
        assertFalse(new Attribute("checked", "checked").shouldCollapseAttribute(null));

        assertTrue(new Attribute("autofocus", null).shouldCollapseAttribute(null, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));
        assertTrue(new Attribute("autofocus", "").shouldCollapseAttribute(null, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));
        assertFalse(new Attribute("autofocus", "autofocus").shouldCollapseAttribute(null, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(new Attribute("checked", null).isBooleanAttribute());
        assertTrue(new Attribute("checked", "").isBooleanAttribute());
        assertFalse(new Attribute("test", null).isBooleanAttribute());
        assertFalse(new Attribute("test", "").isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeWithPrefix() {
        assertTrue(new Attribute("data-test-checked", null).isBooleanAttribute());
        assertFalse(new Attribute("data-test-test", null).isBooleanAttribute());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionShouldBeThrownForNullParent() {
        new Attribute("key", "value", null);
    }

    @Test
    public void testEquals() {
        Attribute a1 = new Attribute("key1", "value1");
        Attribute a2 = new Attribute("key1", "value1");
        Attribute a3 = new Attribute("key2", "value1");

        assertTrue(a1.equals(a1));
        assertTrue(a1.equals(a2));
        assertFalse(a1.equals(a3));
    }

    @Test
    public void testClone() {
        Attribute a = new Attribute("key", "value");
        Attribute cloned = a.clone();
        assertEquals(a.getKey(), cloned.getKey());
        assertEquals(a.getValue(), cloned.getValue());
        assertNotSame(a, cloned);
    }
}