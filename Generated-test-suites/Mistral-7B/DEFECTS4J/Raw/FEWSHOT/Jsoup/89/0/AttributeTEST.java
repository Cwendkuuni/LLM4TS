package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.helper.Validate;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());

        Attribute attributeWithParent = new Attribute(key, value, new Attributes());
        assertEquals(key, attributeWithParent.getKey());
        assertEquals(value, attributeWithParent.getValue());
        assertNotNull(attributeWithParent.parent);
    }

    @Test
    public void testSettingKey() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        String newKey = "newKey";
        attribute.setKey(newKey);
        assertEquals(newKey, attribute.getKey());
        assertEquals(value, attribute.getValue());

        // test if key change updates parent if present
        Attributes parent = new Attributes();
        attribute.setParent(parent);
        parent.put(newKey, attribute);
        assertEquals(newKey, parent.keyAt(0));
    }

    @Test
    public void testSettingValue() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        String newValue = "newValue";
        attribute.setValue(newValue);
        assertEquals(key, attribute.getKey());
        assertEquals(newValue, attribute.getValue());

        // test if value change updates parent if present
        Attributes parent = new Attributes();
        parent.put(key, attribute);
        attribute.setValue("newValue2");
        assertEquals("newValue2", parent.get(key));
    }

    @Test
    public void testHtml() throws IOException {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        assertEquals("testkey=\"testValue\"", StringUtil.releaseBuilder(sb));

        // test shouldCollapseAttribute
        Document.OutputSettings out = new Document.OutputSettings();
        attribute.html(sb, out);
        assertTrue(StringUtil.releaseBuilder(sb).startsWith("testkey"));
        assertTrue(StringUtil.releaseBuilder(sb).endsWith("\"") || value.isEmpty());

        out.syntax(Document.OutputSettings.Syntax.xml);
        attribute.html(sb, out);
        assertEquals("testKey=\"testValue\"", StringUtil.releaseBuilder(sb));

        out.syntax(Document.OutputSettings.Syntax.html);
        attribute.setValue(null);
        attribute.html(sb, out);
        assertTrue(StringUtil.releaseBuilder(sb).isEmpty());

        attribute.setValue("");
        attribute.html(sb, out);
        assertTrue(StringUtil.releaseBuilder(sb).isEmpty());

        String booleanKey = "allowfullscreen";
        attribute.setKey(booleanKey);
        attribute.setValue("");
        attribute.html(sb, out);
        assertTrue(StringUtil.releaseBuilder(sb).isEmpty());

        out.syntax(Document.OutputSettings.Syntax.html);
        attribute.setValue("test");
        attribute.html(sb, out);
        assertEquals("allowfullscreen=\"test\"", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "data-test-key";
        String encodedValue = "data-test-key=\"testValue\"";
        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
        assertEquals(unencodedKey, attribute.getKey());
        assertEquals("testValue", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        String key = "testKey";
        assertTrue(Attribute.isDataAttribute(key));
        String dataPrefix = Attributes.dataPrefix;
        String dataKey = dataPrefix + "testKey";
        assertTrue(Attribute.isDataAttribute(dataKey));
        String nonDataKey = "nonDataKey";
        assertFalse(Attribute.isDataAttribute(nonDataKey));
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        assertTrue(attribute.shouldCollapseAttribute(out));
        out.syntax(Document.OutputSettings.Syntax.xml);
        assertFalse(attribute.shouldCollapseAttribute(out));
        out.syntax(Document.OutputSettings.Syntax.html);
        assertTrue(attribute.shouldCollapseAttribute(out));
        attribute.setValue("");
        assertTrue(attribute.shouldCollapseAttribute(out));
        attribute.setValue(value);
        assertFalse(attribute.shouldCollapseAttribute(out));
    }

    @Test(expected = SerializationException.class)
    public void testExceptionWhenInvalidOutputSettings() throws IOException {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.html(StringUtil.borrowBuilder(), out);
    }

    @Test
    public void testEqualsAndHashcode() {
        String key1 = "testKey1";
        String value1 = "testValue1";
        Attribute attribute1 = new Attribute(key1, value1);

        String key2 = "testKey2";
        String value2 = "testValue2";
        Attribute attribute2 = new Attribute(key2, value2);

        Attribute clone = attribute1.clone();

        assertEquals(attribute1, attribute1);
        assertEquals(attribute1, clone);
        assertEquals(attribute1.hashCode(), attribute1.hashCode());
        assertEquals(attribute1.hashCode(), clone.hashCode());

        assertNotEquals(attribute1, attribute2);
        assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
    }
}