import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import static org.jsoup.internal.StringUtil.lowerCase;

public class AttributesTest {

    

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        assertEquals("value", result);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");
        String result = attributes.getIgnoreCase("key");
        assertEquals("VALUE", result);
    }

    @Test
    public void testGetWhenKeyNotFound() {
        Attributes attributes = new Attributes();
        String result = attributes.get("key");
        assertEquals("", result);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        assertEquals("value", result);
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "VALUE");
        String result = attributes.getIgnoreCase("key");
        assertEquals("VALUE", result);
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.get("key").equals("true"));
        Assert.assertNull(attributes.get("key"));
        Attributes clone = attributes.clone();
        Assert.assertFalse(clone.hasKey("key"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value, attributes);
        attributes.put(attribute);
        String result = attributes.get(key);
        assertEquals(value, result);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        String result = attributes.get("key");
        assertEquals("", result);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "VALUE");
        attributes.removeIgnoreCase("key");
        String result = attributes.getIgnoreCase("key");
        assertEquals("", result);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("other"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "VALUE");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKeyIgnoreCase("other"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes2.put("key3", "value3");

        Attributes result = new Attributes();
        result.addAll(attributes1);
        result.addAll(attributes2);

        assertEquals(3, result.size());
        assertTrue(result.hasKey("key1"));
        assertTrue(result.hasKey("key2"));
        assertTrue(result.hasKey("key3"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> expectedList = Arrays.asList(
                new Attribute("key1", "value1", attributes),
                new Attribute("key2", "value2", attributes)
        );

        List<Attribute> actualList = new ArrayList<>();
        for (Attribute attribute : attributes) {
            actualList.add(attribute);
        }

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> expectedList = Arrays.asList(
                new Attribute("key1", "value1", attributes),
                new Attribute("key2", "value2", attributes)
        );

        List<Attribute> actualList = attributes.asList();

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "data-value1");
        attributes.put("data-key2", "data-value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(2, dataset.size());
        assertEquals("data-value1", dataset.get("data-key1"));
        assertEquals("data-value2", dataset.get("data-key2"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key1=\"value1\"");

        StringBuilder actualHtml = new StringBuilder();
        try {
            attributes.html(actualHtml, new Document.OutputSettings());
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(expectedHtml.toString(), actualHtml.toString());
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        StringBuilder expectedToString = new StringBuilder();
        expectedToString.append("key1=\"value1\"");

        StringBuilder actualToString = new StringBuilder();
        actualToString.append(attributes.toString());

        assertEquals(expectedToString.toString(), actualToString.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");

        Attributes attributes3 = new Attributes();
        attributes3.put("key2", "value2");

        Attributes attributes4 = new Attributes();
        attributes4.put("key1", "value1");
        attributes4.put("key2", "value2");

        Attributes attributes5 = attributes1;

        assertEquals(attributes1, attributes1);
        assertEquals(attributes1, attributes4);
        assertEquals(attributes1, attributes5);
        assertNotEquals(attributes1, attributes2);
        assertNotEquals(attributes1, attributes3);
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");

        Attributes attributes3 = new Attributes();
        attributes3.put("key2", "value2");

        Attributes attributes4 = new Attributes();
        attributes4.put("key1", "value1");
        attributes4.put("key2", "value2");

        Attributes attributes5 = attributes1;

        int hashCode1 = attributes1.hashCode();
        int hashCode2 = attributes2.hashCode();
        int hashCode3 = attributes3.hashCode();
        int hashCode4 = attributes4.hashCode();
        int hashCode5 = attributes5.hashCode();

        assertEquals(hashCode1, hashCode1);
        assertEquals(hashCode1, hashCode4);
        assertEquals(hashCode1, hashCode5);
        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        Attributes clonedAttributes = attributes.clone();

        assertEquals(attributes, clonedAttributes);
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("KEY2", "VALUE2");

        attributes.normalize();

        String key1 = attributes.get("key1");
        String key2 = attributes.get("key2");

        assertEquals("key1", key1);
        assertEquals("KEY2", key2);
    }

    
}