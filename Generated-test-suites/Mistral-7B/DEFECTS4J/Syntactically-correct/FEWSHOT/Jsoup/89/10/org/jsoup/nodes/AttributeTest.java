package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.jsoup.nodes.Attributes.AttributesNotFoundException;
import org.jsoup.nodes.Attributes.NotFound;
import org.jsoup.nodes.Attributes.OutputSettings;
import org.jsoup.nodes.Attributes.dataPrefix;
import org.junit.Test;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.getParent());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        assertEquals("key", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());
        assertSame(attrWithParent.getParent(), attrWithParent.getParent());
    }

    @Test(expected = NullPointerException.class)
    public void testNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        new Attribute("key", null);
    }

    @Test
    public void testSettingKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testNullNewKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey(null);
    }

    @Test
    public void testSettingValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testNullNewValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue(null);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();

        attr.html(sb, new OutputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));

        StringBuilder sb2 = StringUtil.borrowBuilder();
        attr.html(sb2, new OutputSettings().syntax(OutputSettings.Syntax.xml));
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb2));

        StringBuilder sb3 = StringUtil.borrowBuilder();
        attr.html(sb3, new OutputSettings().syntax(OutputSettings.Syntax.html).encodeForXML(true));
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb3));

        StringBuilder sb4 = StringUtil.borrowBuilder();
        attr.html(sb4, new OutputSettings().syntax(OutputSettings.Syntax.html).quoteAttributeValues(true));
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb4));
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attr = new Attribute("key", "value");

        assertTrue(attr.shouldCollapseAttribute(new OutputSettings().syntax(OutputSettings.Syntax.html)));
        assertTrue(attr.shouldCollapseAttribute(new OutputSettings().syntax(OutputSettings.Syntax.html).quoteAttributeValues(true)));
        assertFalse(attr.shouldCollapseAttribute(new OutputSettings().syntax(OutputSettings.Syntax.xml)));
        assertFalse(attr.shouldCollapseAttribute(new OutputSettings().syntax(OutputSettings.Syntax.xml).quoteAttributeValues(true)));
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("key", "value");

        assertFalse(attr.isDataAttribute());
        assertTrue(attr.isDataAttribute("data-key"));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("key", "value");

        assertFalse(attr.isBooleanAttribute());
        assertTrue(Arrays.binarySearch(Attribute.booleanAttributes, "checked") >= 0);
        assertTrue(attr.isBooleanAttribute("checked"));
    }

    @Test(expected = AttributesNotFoundException.class)
    public void testGetParentNoParent() {
        Attribute attr = new Attribute("key", "value");
        attr.getParent();
    }

    @Test
    public void testGetParent() {
        Attributes attrs = new Attributes();
        attrs.put(new Attribute("key", "value"));

        Attribute attr = attrs.get("key");
        assertEquals(attrs, attr.getParent());
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

        int hashCode1 = attr1.hashCode();
        int hashCode2 = attr2.hashCode();
        int hashCode3 = attr3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();

        assertEquals("key", clonedAttr.getKey());
        assertEquals("value", clonedAttr.getValue());
        assertNull(clonedAttr.getParent());
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneThrowsException() throws CloneNotSupportedException {
        Attribute attr = new Attribute("key", "value");
        attr.clone();
    }
}