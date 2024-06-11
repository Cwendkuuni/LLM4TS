package org.junit.jupiter.api;
import static org.junit.jupiter.api.Assertions.*;

import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class AttributeTest {

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    public void testNullKey() {
        assertThrows(NullPointerException.class, () -> new Attribute(null, "value"));
    }

    @Test
    public void testEmptyKey() {
        assertThrows(IllegalArgumentException.class, () -> new Attribute("", "value"));
    }

    @Test
    public void testEmptyValue() {
        Attribute attribute = new Attribute("key", "");
        assertEquals("key=\"\"", attribute.html());
    }

    @Test
    public void testInvalidKey() {
        assertThrows(IllegalArgumentException.class, () -> new Attribute("key<invalid", "value"));
    }

    @Test
    public void testKeyAlreadyExists() {
        Attributes attributes = new Attributes();
        attributes.put("key", new Attribute("key", "value1"));
        assertThrows(IllegalArgumentException.class, () -> new Attribute("key", "value2", attributes));
    }

    // Test for ExceptionDuringHtmlCreation here
}