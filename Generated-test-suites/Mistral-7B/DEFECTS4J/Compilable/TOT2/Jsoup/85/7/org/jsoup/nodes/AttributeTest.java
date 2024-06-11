package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("testKey", "testValue");
        String key = attribute.getKey();
        assertEquals("testKey", key);
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setKey("newTestKey");
        String key = attribute.getKey();
        assertEquals("newTestKey", key);
    }
}