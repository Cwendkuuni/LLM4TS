package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        assertNotNull(attr);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        assertNotNull(attrWithParent);
        assertEquals("key", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());

        Attribute booleanAttr = new Attribute("checked", null);
        assertTrue(booleanAttr.isBooleanAttribute());
    }

    @Test
    public void testSettingKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingEmptyKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("");
    }

    @Test(expected = NullPointerException.class)
    public void testSettingNullKey() {
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
    public void testSettingNullValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue(null);
    }

    @Test
    public void testCreatingFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute dataAttr = new Attribute("data-test", "value");
        assertTrue(dataAttr.isDataAttribute());

        Attribute nonDataAttr = new Attribute("key", "value");
        assertFalse(nonDataAttr.isDataAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute booleanAttr = new Attribute("checked", null);
        assertTrue(booleanAttr.isBooleanAttribute());

        Attribute nonBooleanAttr = new Attribute("key", "value");
        assertFalse(nonBooleanAttr.isBooleanAttribute());

        Attribute booleanAttrWithValue = new Attribute("disabled", "disabled");
        assertTrue(booleanAttrWithValue.isBooleanAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);

        Attribute booleanAttr = new Attribute("checked", null);
        assertTrue(booleanAttr.shouldCollapseAttribute(out));

        Attribute nonBooleanAttr = new Attribute("key", "value");
        assertFalse(nonBooleanAttr.shouldCollapseAttribute(out));

        Attribute booleanAttrWithValue = new Attribute("disabled", "disabled");
        assertTrue(booleanAttrWithValue.shouldCollapseAttribute(out));

        Attribute booleanAttrEmpty = new Attribute("checked", "");
        assertTrue(booleanAttrEmpty.shouldCollapseAttribute(out));

        Attribute booleanAttrSameName = new Attribute("disabled", "disabled");
        assertTrue(booleanAttrSameName.shouldCollapseAttribute(out));
    }

    @Test
    public void testToString() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key=\"value\"", attr.toString());
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");

        assertFalse(attr1.equals(null));
        assertFalse(attr1.equals(new Object()));
        assertFalse(attr1.equals(attr2));
        assertTrue(attr1.equals(attr3));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");

        int hashcode1 = attr1.hashCode();
        int hashcode2 = attr2.hashCode();
        int hashcode3 = attr3.hashCode();

        assertEquals(hashcode1, hashcode3); // should be equal since equals is true
        assertNotEquals(hashcode1, hashcode2);
    }

    @Test
    public void testCloning() throws CloneNotSupportedException {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();

        assertEquals(attr, clonedAttr);
        assertNotSame(attr, clonedAttr);
        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
    }
}