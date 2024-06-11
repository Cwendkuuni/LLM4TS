package org.jsoup.nodes;
import org.jsoup.nodes.Attribute;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AttributeTest {

    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("testKey", "testValue");
    }

    @Test
    public void getKey() {
        assertEquals("testKey", attribute.getKey());
    }

    @Test
    public void setKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void getValue() {
        assertEquals("testValue", attribute.getValue());
    }

    @Test
    public void setValue() {
        assertEquals("testValue", attribute.setValue("newValue"));
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void html() {
        assertEquals("testKey=\"testValue\"", attribute.html());
    }

}