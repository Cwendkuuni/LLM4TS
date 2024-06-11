package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

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
    public void testSettingKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");

        assertEquals("newKey", attr.getKey());

        try {
            attr.setKey(null);
            fail("Should not allow null key");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testSettingValue() {
        Attribute attr = new Attribute("key", "value");
        String newValue = "newValue";
        attr.setValue(newValue);

        assertEquals("newValue", attr.getValue());

        try {
            attr.setValue(null);
            fail("Should not allow null value");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");

        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());

        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));

        attr.setValue(null);
        sb.setLength(0);
        attr.html(sb, new Document("").outputSettings());

        assertEquals("key", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testShouldCollapseAttribute() {
        boolean collapsible = new Attribute("key", "").shouldCollapseAttribute(new Document.OutputSettings());

        assertTrue(collapsible);

        collapsible = new Attribute("booleanKey", "").shouldCollapseAttribute(new Document.OutputSettings());

        assertFalse(collapsible);

        collapsible = new Attribute("booleanKey", "true").shouldCollapseAttribute(new Document.OutputSettings());

        assertTrue(collapsible);
    }

    @Test
    public void testIsBooleanAttribute() {
        boolean booleanAttr = new Attribute("booleanKey", null).isBooleanAttribute();

        assertTrue(booleanAttr);

        boolean nonBooleanAttr = new Attribute("key", "value").isBooleanAttribute();

        assertFalse(nonBooleanAttr);
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");

        assertTrue(attr1.equals(attr2));
        assertFalse(attr1.equals(attr3));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();

        assertEquals(attr, clonedAttr);
    }
}