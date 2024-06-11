package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.jsoup.helper.StringUtil.join;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

    @Test
    public void testConstructorWithKeyAndValue() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testConstructorWithKeyValueAndParent() {
        Attributes parent = new Attributes();
        Attribute attr = new Attribute("key", "value", parent);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertEquals(attr, parent.get("key"));
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
    }

    @Test
    public void testHtml() throws Exception {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        String expected = "key=\"value\"";
        assertEquals(expected, sb.toString());

        attr = new Attribute("key1", "value1");
        attr.html(sb, new Document("").outputSettings());
        expected = "key1=\"value1\"";
        assertEquals(expected, sb.toString());

        attr = new Attribute("key2", null);
        attr.html(sb, new Document("").outputSettings());
        expected = "key2";
        assertEquals(expected, sb.toString());

        attr = new Attribute("key3", "");
        attr.html(sb, new Document("").outputSettings());
        expected = "key3";
        assertEquals(expected, sb.toString());

        attr = new Attribute("key4", "value4");
        attr.html(sb, new Document("").outputSettings());
        attr.setValue("newValue4");
        attr.html(sb, new Document("").outputSettings());
        expected = "key4=\"newValue4\"";
        assertEquals(expected, sb.toString());

        attr = new Attribute("key5", "value5");
        sb.setLength(0);
        attr.html(sb, new Document("").outputSettings());
        attr.setValue("newValue5");
        attr.html(sb, new Document("").outputSettings());
        expected = "key5=\"newValue5\"";
        assertEquals(expected, sb.toString());

        attr = new Attribute("key6", "value6");
        attr.html(sb, new Document("").outputSettings());
        attr.setValue(null);
        attr.html(sb, new Document("").outputSettings());
        expected = "key6";
        assertEquals(expected, sb.toString());

        attr = new Attribute("key7", "value7");
        attr.html(sb, new Document("").outputSettings());
        attr.setValue("newValue7");
        attr.html(sb, new Document("").outputSettings());
        attr.setValue(null);
        attr.html(sb, new Document("").outputSettings());
        expected = "key7";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testToString() {
        Attribute attr = new Attribute("key", "value");
        assertEquals(attr.html(), attr.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());

        attr = Attribute.createFromEncoded("key1", "key1=\"value1\"");
        assertEquals("key1", attr.getKey());
        assertEquals("value1", attr.getValue());

        attr = Attribute.createFromEncoded("key2", "");
        assertEquals("key2", attr.getKey());
        assertNull(attr.getValue());

        attr = Attribute.createFromEncoded("key3", null);
        assertNull(attr);
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isDataAttribute());

        attr = new Attribute("data-key", "value");
        assertTrue(attr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertTrue(Attribute.shouldCollapseAttribute("key", null, new Document.OutputSettings()));
        assertTrue(Attribute.shouldCollapseAttribute("key", "", new Document.OutputSettings()));
        assertTrue(Attribute.shouldCollapseAttribute("key", "key", new Document.OutputSettings()));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", new Document.OutputSettings()));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value1", new Document.OutputSettings()));

        assertTrue(Attribute.shouldCollapseAttribute("key", null, new Document.OutputSettings(Document.OutputSettings.Syntax.xml)));
        assertTrue(Attribute.shouldCollapseAttribute("key", "", new Document.OutputSettings(Document.OutputSettings.Syntax.xml)));
        assertTrue(Attribute.shouldCollapseAttribute("key", "key", new Document.OutputSettings(Document.OutputSettings.Syntax.xml)));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", new Document.OutputSettings(Document.OutputSettings.Syntax.xml)));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value1", new Document.OutputSettings(Document.OutputSettings.Syntax.xml)));

        assertTrue(Attribute.shouldCollapseAttribute("checked", null, new Document.OutputSettings()));
        assertTrue(Attribute.shouldCollapseAttribute("checked", "", new Document.OutputSettings()));
        assertTrue(Attribute.shouldCollapseAttribute("checked", "checked", new Document.OutputSettings()));
        assertFalse(Attribute.shouldCollapseAttribute("checked", "true", new Document.OutputSettings()));
        assertFalse(Attribute.shouldCollapseAttribute("checked", "false", new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isBooleanAttribute());

        attr = new Attribute("checked", null);
        assertTrue(attr.isBooleanAttribute());

        attr = new Attribute("checked", "");
        assertTrue(attr.isBooleanAttribute());

        attr = new Attribute("checked", "checked");
        assertTrue(attr.isBooleanAttribute());

        attr = new Attribute("checked", "true");
        assertFalse(attr.isBooleanAttribute());

        attr = new Attribute("checked", "false");
        assertFalse(attr.isBooleanAttribute());
    }

    @Test
    public void testIsDataAttributeWithPrefix() {
        Attribute attr = new Attribute("data-key", "value");
        assertTrue(attr.isDataAttribute());

        attr = new Attribute("data-key1", "value1");
        assertTrue(attr.isDataAttribute());

        attr = new Attribute("data-key2", null);
        assertTrue(attr.isDataAttribute());

        attr = new Attribute("data-key3", "");
        assertTrue(attr.isDataAttribute());

        attr = new Attribute("data-key4", "value4");
        attr.setValue("newValue4");
        assertTrue(attr.isDataAttribute());

        attr = new Attribute("data-key5", "value5");
        attr.setValue(null);
        assertTrue(attr.isDataAttribute());

        attr = new Attribute("data-key6", "value6");
        attr.setValue("newValue6");
        attr.setValue(null);
        assertFalse(attr.isDataAttribute());
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute cloned = attr.clone();
        assertEquals("key", cloned.getKey());
        assertEquals("value", cloned.getValue());
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value1");
        Attribute attr4 = new Attribute("key1", "value2");
        Attribute attr5 = new Attribute("key2", "value1");
        Attribute attr6 = new Attribute("key1", null);
        Attribute attr7 = new Attribute("key2", null);

        assertTrue(attr1.equals(attr2));
        assertFalse(attr1.equals(attr3));
        assertFalse(attr1.equals(attr4));
        assertFalse(attr1.equals(attr5));
        assertFalse(attr1.equals(attr6));
        assertFalse(attr1.equals(attr7));
        assertFalse(attr6.equals(attr7));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value1");
        Attribute attr4 = new Attribute("key1", "value2");
        Attribute attr5 = new Attribute("key2", "value1");
        Attribute attr6 = new Attribute("key1", null);
        Attribute attr7 = new Attribute("key2", null);

        int hashCode1 = attr1.hashCode();
        int hashCode2 = attr2.hashCode();
        int hashCode3 = attr3.hashCode();
        int hashCode4 = attr4.hashCode();
        int hashCode5 = attr5.hashCode();
        int hashCode6 = attr6.hashCode();
        int hashCode7 = attr7.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode1, hashCode5);
        assertNotEquals(hashCode1, hashCode6);
        assertNotEquals(hashCode1, hashCode7);
        assertNotEquals(hashCode6, hashCode7);
    }

    @Test
    public void testMapInterface() {
        Attributes attributes = new Attributes();
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value3");

        attributes.put(attr1);
        attributes.put(attr2);

        Map<String, Attribute> map = new LinkedHashMap<>(attributes);

        assertEquals(2, map.size());
        assertTrue(map.containsKey(attr1.getKey()));
        assertTrue(map.containsKey(attr2.getKey()));
        assertEquals(attr1, map.get(attr1.getKey()));
        assertEquals(attr2, map.get(attr2.getKey()));

        map.put(attr3);
        assertEquals(3, map.size());
        assertTrue(map.containsKey(attr1.getKey()));
        assertTrue(map.containsKey(attr2.getKey()));
        assertTrue(map.containsKey(attr3.getKey()));
        assertEquals(attr1, map.get(attr1.getKey()));
        assertEquals(attr2, map.get(attr2.getKey()));
        assertEquals(attr3, map.get(attr3.getKey()));
    }

    @Test
    public void testPutAndGetFromParent() {
        Attributes parent = new Attributes();
        Attribute attr = new Attribute("key", "value");

        parent.put(attr);
        assertEquals("value", parent.get("key").getValue());

        attr.setKey("newKey");
        assertEquals("newKey", parent.get("newKey").getKey());
        assertEquals("value", parent.get("key").getValue());

        attr.setValue("newValue");
        assertEquals("newValue", parent.get("newKey").getValue());
        assertEquals("value", parent.get("key").getValue());
    }
}