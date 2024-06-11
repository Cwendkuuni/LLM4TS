package org.jsoup.nodes;
import org.junit.Test;
import org.jsoup.nodes.Attribute;

import static org.junit.Assert.assertEquals;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("testKey", "testVal");
        assertEquals("testKey", attribute.getKey());
    }
}