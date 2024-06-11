package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

    @Test
    public void testCreation() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        assertEquals("key", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());
        assertNotNull(attrWithParent.parent);
    }

    @Test
    public void testSettingKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test
    public void testSettingValue() {
        Attribute attr = new Attribute("key", "value");
        String oldValue = attr.getValue();
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
        assertEquals(oldValue, attr.parent.get("key"));
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
        int hashCode2 = attr1.hashCode();
        int hashCode3 = attr2.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr, clonedAttr);
    }

    @Test
    public void testBooleanAttributes() {
        Attribute booleanAttr = new Attribute("checked", "");
        assertTrue(Attribute.isBooleanAttribute("checked"));

        Attribute nonBooleanAttr = new Attribute("key", "value");
        assertFalse(Attribute.isBooleanAttribute("key"));
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertTrue(Attribute.shouldCollapseAttribute("checked", null, outputSettings));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", outputSettings));
    }
}