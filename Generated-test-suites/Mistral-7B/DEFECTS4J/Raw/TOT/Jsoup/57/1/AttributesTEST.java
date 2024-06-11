package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class AttributesTest {

    @Test
    public void testGetAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        String actual = attributes.get("key1");
        String expected = "value1";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        String actual = attributes.getIgnoreCase("key1");
        String expected = "value1";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAttributeWhenNull() {
        Attributes attributes = new Attributes();
        String actual = attributes.get("key1");
        assertEquals("", actual);
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        String expected = "value1";
        attributes.put("key1", expected);
        String actual = attributes.get("key1");
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullAttributeKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullAttributeValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testPutIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        String expected = "Value1";
        attributes.putIgnoreCase("key1", expected);
        String actual = attributes.get("key1");
        assertEquals(expected, actual);
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
        attributes.put("key1", false);
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testPutSameBooleanAttributeTwice() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        assertTrue(attributes.hasKey("key1"));
        attributes.put("key1", true);
        assertTrue(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        attributes.removeIgnoreCase("Key1");
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key1"));
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        attributes.put("key1", "value1");
        assertEquals(1, attributes.size());
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        attributes2.put("key2", "value3");
        attributes2.put("key3", "value3");
        attributes1.addAll(attributes2);
        List<Attribute> expectedList = Arrays.asList(
                new Attribute("key1", "value1"),
                new Attribute("key2", "value3"),
                new Attribute("key3", "value3")
        );
        List<Attribute> actualList = attributes1.asList();
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "data-value1");
        attributes.put("key2", "value2");
        Map<String, String> dataset = attributes.dataset();
        assertEquals("data-value1", dataset.get("data-key1"));
        assertNull(dataset.get("key2"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        StringBuilder expected = new StringBuilder();
        expected.append(" key1=\"value1\"");
        String actual = attributes.html();
        assertEquals(expected.toString(), actual);
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        String expected = "Attributes{attributes=" +
                "LinkedHashMap{" +
                "Attribute{key='key1', value='value1'}" +
                "}}";
        String actual = attributes.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        Attributes attributes4 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key1", "value2");
        attributes4.put("key2", "value2");
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
        assertFalse(attributes1.equals(attributes4));
        assertFalse(attributes1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        Attributes attributes4 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key1", "value2");
        attributes4.put("key2", "value2");
        int attributes1Hashcode = attributes1.hashCode();
        int attributes2Hashcode = attributes2.hashCode();
        int attributes3Hashcode = attributes3.hashCode();
        int attributes4Hashcode = attributes4.hashCode();
        assertEquals(attributes1Hashcode, attributes2Hashcode);
        assertNotSame(attributes1Hashcode, attributes3Hashcode);
        assertNotSame(attributes1Hashcode, attributes4Hashcode);
    }
}