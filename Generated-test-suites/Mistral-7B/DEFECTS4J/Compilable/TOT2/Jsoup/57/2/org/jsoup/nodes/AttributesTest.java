package org.jsoup.nodes;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Attributes;

public class AttributesTest {
    @org.junit.Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");

        String expected = "testValue";
        String actual = attributes.get("testKey");

        assertEquals(expected, actual);
    }
}