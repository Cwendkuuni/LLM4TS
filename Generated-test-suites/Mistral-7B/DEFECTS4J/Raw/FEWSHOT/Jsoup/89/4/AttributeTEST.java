package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        assertEquals("key", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());
        assertNotNull(attrWithParent.parent);
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyNull() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey(null);
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        String newValue = "newValue";
        attr.setValue(newValue);
        assertEquals(newValue, attr.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueNull() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue(null);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb);
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertTrue(Attribute.shouldCollapseAttribute(null, null, null));
        assertTrue(Attribute.shouldCollapseAttribute("", "", null));
        assertTrue(Attribute.shouldCollapseAttribute("key", "", null));
        assertTrue(Attribute.shouldCollapseAttribute("key", "key", null));
        assertTrue(Attribute.shouldCollapseAttribute("key", "", new Document.OutputSettings(Document.OutputSettings.Syntax.html)));
        assertTrue(Attribute.shouldCollapseAttribute("key", "key", new Document.OutputSettings(Document.OutputSettings.Syntax.html)));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", null));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", new Document.OutputSettings(Document.OutputSettings.Syntax.html)));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value1", new Document.OutputSettings(Document.OutputSettings.Syntax.html)));
    }

    @Test
    public void testIsBooleanAttribute() {
        for (String booleanAttribute : Attribute.booleanAttributes) {
            assertTrue(Attribute.isBooleanAttribute("" + booleanAttribute + ""));
        }

        assertFalse(Attribute.isBooleanAttribute(""));
        assertFalse(Attribute.isBooleanAttribute("invalid"));
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
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();

        assertEquals(attr, clonedAttr);
        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
    }
}