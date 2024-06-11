package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

public class AttributesTest {

    @Test
    public void testAttributesBasicFunctionality() throws Exception {
        Attributes attributes = new Attributes();

        // Test method: get(String key)
        attributes.put("key1", "value1");
        String value1 = attributes.get("key1");
        Assert.assertEquals("value1", value1);

        // Test method: getIgnoreCase(String key)
        attributes.put("key2", "value2");
        String value2 = attributes.getIgnoreCase("key2");
        Assert.assertEquals("value2", value2);

        // Test method: put(String key, String value)
        String newValue = "newValue";
        attributes.put("key3", newValue);
        String value3 = attributes.get("key3");
        Assert.assertEquals(newValue, value3);

        // Test method: put(String key, boolean value)
        attributes.put("key4", true);
        boolean boolValue = attributes.get("key4").equals("true");
        Assert.assertTrue(boolValue);

        // Test method: iterator()
        List<Map.Entry<String, String>> expectedList = new ArrayList<>();
        expectedList.add(new AbstractMap.SimpleEntry<>("key1", "value1"));
        expectedList.add(new AbstractMap.SimpleEntry<>("key2", "value2"));
        expectedList.add(new AbstractMap.SimpleEntry<>("key3", newValue));
        expectedList.add(new AbstractMap.SimpleEntry<>("key4", "true"));

        Iterator<Map.Entry<String, String>> iterator = attributes.iterator();
        List<Map.Entry<String, String>> actualList = new ArrayList<>();
        while (iterator.hasNext()) {
            actualList.add((Map.Entry<String, String>) iterator.next());
        }
        assertThat(actualList, is(instanceOf(List.class)));
        assertThat(actualList, is(Collections.unmodifiableList(Collections.nCopies(4, new HashMap.SimpleEntry<>("", "")))));
        assertThat(actualList, is(Collections.singletonList(expectedList.get(0))));

        // Test method: addAll(Attributes incoming)
        Attributes incomingAttributes = new Attributes();
        incomingAttributes.put("key5", "value5");
        attributes.addAll(incomingAttributes);
        String value5 = attributes.get("key5");
        Assert.assertEquals("value5", value5);

        // Test method: size()
        int size = attributes.size();
        Assert.assertEquals(5, size);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributesWithNullKey() throws Exception {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributesWithNullValue() throws Exception {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testAttributesWithEmptyStringKey() throws Exception {
        Attributes attributes = new Attributes();
        attributes.put("", "value");
        String value = attributes.get("");
        Assert.assertEquals("value", value);
    }

    @Test
    public void testAttributesWithEmptyStringValue() throws Exception {
        Attributes attributes = new Attributes();
        attributes.put("key", "");
        String value = attributes.get("key");
        Assert.assertEquals("", value);

        attributes.put("key", null);
        Assert.assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testAttributesWithBooleanValue() throws Exception {
        Attributes attributes = new Attributes();
        attributes.put("key", false);
        Assert.assertFalse(attributes.get("key").equals("true"));

        attributes.put("key", true);
        Assert.assertTrue(attributes.get("key").equals("true"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAttributesWithOutOfBoundsIndex() throws Exception {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove(10);
    }

    @Test
    public void testAttributesWithDuplicateKey() throws Exception {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        String value1 = attributes.get("key1");
        String value2 = attributes.getIgnoreCase("key1");
        Assert.assertEquals("value2", value1);
        Assert.assertEquals("value2", value2);
    }

    @Test
    public void testAttributesWithEmptyAttributes() throws Exception {
        Attributes emptyAttributes = new Attributes();
        Assert.assertEquals(0, emptyAttributes.size());
        Assert.assertFalse(emptyAttributes.hasKey("key"));
        Assert.assertFalse(emptyAttributes.hasKeyIgnoreCase("key"));
        Assert.assertFalse(emptyAttributes.hasKeyIgnoreCase(""));
        Assert.assertFalse(emptyAttributes.hasKey(""));
    }

    @Test
    public void testAttributesWithDataAttributes() throws Exception {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("data-key1", "data-value1");

        Map<String, String> dataset = attributes.dataset();
        Map<String, String> expectedDataset = new HashMap<>();
        expectedDataset.put("data-key1", "data-value1");

        assertThat(dataset, is(instanceOf(Map.class)));
        assertThat(dataset, is(expectedDataset));
    }
}

// 
// 