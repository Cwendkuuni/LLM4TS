package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AttributesTest {
    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String expectedValue = "value";
        String actualValue = attributes.get("key");
        assertEquals(expectedValue, actualValue);
    }
}