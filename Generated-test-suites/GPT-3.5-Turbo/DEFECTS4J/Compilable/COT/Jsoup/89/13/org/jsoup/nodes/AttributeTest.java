package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");

        String key = attribute.getKey();

        assertEquals("key", key);
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");

        attribute.setKey("newKey");

        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");

        String value = attribute.getValue();

        assertEquals("value", value);
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");

        String oldValue = attribute.setValue("newValue");

        assertEquals("value", oldValue);
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");

        String html = attribute.html();

        assertEquals("key=\"value\"", html);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "value&amp;");

        assertEquals("key", attribute.getKey());
        assertEquals("value&", attribute.getValue());
    }
}
