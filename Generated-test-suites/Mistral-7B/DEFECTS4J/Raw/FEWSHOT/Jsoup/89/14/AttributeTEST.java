package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testCreateAttribute() {
        Attribute attr = new Attribute("class", "foo");
        assertEquals("class", attr.getKey());
        assertEquals("foo", attr.getValue());
        assertNull(attr.getParent());

        Attribute attrWithParent = new Attribute("class", "foo", new Attributes());
        assertEquals("class", attrWithParent.getKey());
        assertEquals("foo", attrWithParent.getValue());
        assertNotNull(attrWithParent.getParent());
    }

    @Test
    public void testBooleanAttributes() {
        Attribute attr = new Attribute("checked", null);
        assertTrue(attr.isBooleanAttribute());
        assertFalse(attr.isDataAttribute());

        attr.setValue("checked");
        assertTrue(attr.isBooleanAttribute());
        assertFalse(attr.isDataAttribute());

        attr.setValue("false");
        assertFalse(attr.isBooleanAttribute());
        assertFalse(attr.isDataAttribute());

        attr.setValue("");
        assertTrue(attr.isBooleanAttribute());

        attr.setKey("disabled");
        assertTrue(attr.isBooleanAttribute());
        assertFalse(attr.isDataAttribute());

        attr.setValue("disabled");
        assertTrue(attr.isBooleanAttribute());
        assertFalse(attr.isDataAttribute());

        attr.setValue("false");
        assertFalse(attr.isBooleanAttribute());
        assertFalse(attr.isDataAttribute());

        attr.setValue("");
        assertTrue(attr.isBooleanAttribute());
    }

    @Test
    public void testDataAttributes() {
        Attribute attr = new Attribute("data-test", "testValue");
        assertTrue(attr.isDataAttribute());
        assertFalse(attr.isBooleanAttribute());

        attr.setValue(null);
        assertFalse(attr.isDataAttribute());
        assertFalse(attr.isBooleanAttribute());

        attr.setValue("");
        assertFalse(attr.isDataAttribute());
        assertFalse(attr.isBooleanAttribute());

        attr.setValue("testValue2");
        assertTrue(attr.isDataAttribute());
        assertFalse(attr.isBooleanAttribute());

        attr.setKey("data-other-test");
        assertTrue(attr.isDataAttribute());
        assertFalse(attr.isBooleanAttribute());
    }

    @Test
    public void testCollapsibleAttributes() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><div class=\"foo\"></div></body></html>";
        Document doc = Jsoup.parse(html);

        Attributes attributes = doc.select("div").first().attributes();
        Attribute attr1 = attributes.get("class");
        Attribute attr2 = new Attribute("class", "", doc.outputSettings());
        assertEquals(attr1, attr2);

        attr1.setValue("foo bar");
        assertNotEquals(attr1, attr2);

        attr1.setValue("");
        assertEquals(attr1, attr2);

        attr1.setValue("foo");
        assertNotEquals(attr1, attr2);

        attr1.setValue("false");
        assertNotEquals(attr1, attr2);

        attr1.setValue("checked");
        assertTrue(attr1.shouldCollapseAttribute(doc.outputSettings()));
        assertEquals(attr1, attr2);

        attr1.setValue("checked foo");
        assertFalse(attr1.shouldCollapseAttribute(doc.outputSettings()));
        assertNotEquals(attr1, attr2);
    }

    @Test
    public void testEqualsAndHashCode() {
        Attribute attr1 = new Attribute("class", "foo");
        Attribute attr2 = new Attribute("class", "foo");
        Attribute attr3 = new Attribute("class", "bar");
        Attribute attr4 = attr1;
        Attribute attr5 = new Attribute("class", "foo");

        assertEquals(attr1, attr4);
        assertEquals(attr1, attr5);
        assertEquals(attr1.hashCode(), attr4.hashCode());
        assertEquals(attr1.hashCode(), attr5.hashCode());

        assertNotEquals(attr1, attr3);
        assertNotEquals(attr1.hashCode(), attr3.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("class", "foo");
        Attribute clonedAttr = attr.clone();

        assertEquals(attr, clonedAttr);
        assertNotEquals(attr, clonedAttr);
        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
        assertNull(clonedAttr.getParent());
    }
}