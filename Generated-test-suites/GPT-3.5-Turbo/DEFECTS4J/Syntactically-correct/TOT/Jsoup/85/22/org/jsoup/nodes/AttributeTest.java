package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.HashMap;

public class AttributeTest {

    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("key", "value");
    }

    @Test
    public void testGetKey() {
        String key = attribute.getKey();
        assert(key.equals("key"));
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        String key = attribute.getKey();
        assert(key.equals("newKey"));
    }

    @Test
    public void testGetValue() {
        String value = attribute.getValue();
        assert(value.equals("value"));
    }

    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        String value = attribute.getValue();
        assert(value.equals("newValue"));
    }

    @Test
    public void testHtml() {
        String html = attribute.html();
        assert(html.equals("key=\"value\""));
    }

    @Test
    public void testToString() {
        String str = attribute.toString();
        assert(str.equals("key=\"value\""));
    }

    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "newKey";
        String encodedValue = "new%Value";
        Attribute newAttribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
        assert(newAttribute.getKey().equals("newKey"));
        assert(newAttribute.getValue().equals("newValue"));
    }

    @Test
    public void testIsDataAttribute() {
        boolean isData = attribute.isDataAttribute();
        assert(!isData);
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        boolean shouldCollapse = attribute.shouldCollapseAttribute(out);
        assert(!shouldCollapse);
    }

    @Test
    public void testIsBooleanAttribute() {
        boolean isBoolean = attribute.isBooleanAttribute();
        assert(!isBoolean);
    }

    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("key", "value");
        assert(attribute.equals(sameAttribute));

        Attribute differentAttribute = new Attribute("key", "differentValue");
        assert(!attribute.equals(differentAttribute));
    }

    @Test
    public void testHashCode() {
        int hashCode = attribute.hashCode();
        assert(hashCode != 0);
    }
}