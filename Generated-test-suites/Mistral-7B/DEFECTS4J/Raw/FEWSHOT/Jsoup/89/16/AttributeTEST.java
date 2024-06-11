package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

public class AttributeTest {

    @Test
    public void testBooleanAttributes() {
        for (String booleanAttribute : Attribute.booleanAttributes) {
            Attribute attribute = new Attribute(booleanAttribute, "");
            assertTrue(attribute.isBooleanAttribute());
        }

        String nonBooleanAttribute = "data-custom-attribute";
        Attribute nonBoolean = new Attribute(nonBooleanAttribute, "");
        assertFalse(nonBoolean.isBooleanAttribute());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute encoded = Attribute.createFromEncoded("class", "foo bar baz");
        assertEquals("class", encoded.getKey());
        assertEquals("foo bar baz", encoded.getValue());
    }

    @Test
    public void testCollapsibleAttribute() throws Exception {
        Map<String, String> attributes = new Attributes();
        attributes.put(new Attribute("id", "test"));
        Attribute attr = new Attribute("id", null, attributes);
        assertTrue(attr.shouldCollapseAttribute(null));
        assertTrue(attr.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));

        attributes.put(new Attribute("class", "test"));
        attr = new Attribute("class", null, attributes);
        assertFalse(attr.shouldCollapseAttribute(null));
        assertFalse(attr.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));

        attributes.put(new Attribute("class", ""));
        attr = new Attribute("class", null, attributes);
        assertTrue(attr.shouldCollapseAttribute(null));
        assertTrue(attr.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));

        attributes.put(new Attribute("checked", ""));
        attr = new Attribute("checked", null, attributes);
        assertTrue(attr.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));
    }

    @Test
    public void testShouldCollapseAttribute() throws Exception {
        Map<String, String> attributes = new Attributes();
        attributes.put(new Attribute("id", "test"));
        Attribute attr = new Attribute("id", null, attributes);
        assertTrue(attr.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));

        attributes.put(new Attribute("class", "test"));
        attr = new Attribute("class", null, attributes);
        assertFalse(attr.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));

        Document doc = new Document();
        attributes = doc.head().attributes();
        attributes.put(new Attribute("id", "test"));
        attr = new Attribute("id", null, attributes);
        assertTrue(attr.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-test", "test");
        assertTrue(attribute.isDataAttribute());

        attribute = new Attribute("test", "test");
        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testEqualsAndHashcode() {
        String key1 = "test1";
        String key2 = "test2";
        String val1 = "test1";
        String val2 = "test2";

        Attribute attr1 = new Attribute(key1, val1);
        Attribute attr2 = new Attribute(key2, val2);
        Attribute attr3 = new Attribute(key1, val1);

        assertEquals(attr1, attr3);
        assertEquals(attr1.hashCode(), attr3.hashCode());

        assertNotEquals(attr1, attr2);
        assertNotEquals(attr1.hashCode(), attr2.hashCode());

        Attribute clone = attr1.clone();
        assertEquals(attr1, clone);
        assertEquals(attr1.hashCode(), clone.hashCode());
    }

    @Test
    public void testClone() {
        String key = "test";
        String val = "test";

        Attribute attribute = new Attribute(key, val);
        Attribute cloned = attribute.clone();

        assertEquals(attribute, cloned);
        assertSame(attribute.getKey(), cloned.getKey());
        assertSame(attribute.getValue(), cloned.getValue());
    }
}