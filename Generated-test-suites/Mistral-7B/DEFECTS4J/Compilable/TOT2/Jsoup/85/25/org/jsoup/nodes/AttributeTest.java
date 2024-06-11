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
}