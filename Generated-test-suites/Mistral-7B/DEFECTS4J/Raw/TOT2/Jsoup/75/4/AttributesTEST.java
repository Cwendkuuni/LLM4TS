import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);

        attributes.normalize(); // testing case sensitivity
        String actualIgnoreCase = attributes.getIgnoreCase("key");
        assertEquals(expected, actualIgnoreCase);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String actual1 = attributes.get("key1");
        String expected1 = "value1";
        assertEquals(expected1, actual1);

        String actual2 = attributes.get("key2");
        String expected2 = "value2";
        assertEquals(expected2, actual2);

        attributes.normalize(); // testing case sensitivity
        String actual1IgnoreCase = attributes.getIgnoreCase("key1");
        assertEquals(expected1, actual1IgnoreCase);

        attributes.putIgnoreCase("Key1", "newValue1"); // test case insensitive put
        String actual1AfterPutIgnoreCase = attributes.get("key1");
        assertEquals(expected1, actual1AfterPutIgnoreCase);

        String actual1AfterPutIgnoreCaseIgnoreCase = attributes.getIgnoreCase("Key1");
        assertEquals("newValue1", actual1AfterPutIgnoreCaseIgnoreCase);
    }

    @Test
    public void testPutMethodWithBooleanValue() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKey("booleanKey"));
        assertFalse(attributes.hasKey("booleanKey") && attributes.get("booleanKey").equals("false"));
    }

    @Test(expected = NullPointerException.class)
    public void testPutMethodWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testPutMethodWithNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");
        String actual1 = attributes.get("key1");
        assertEquals(EmptyString, actual1);

        String actual2 = attributes.get("key2");
        String expected2 = "value2";
        assertEquals(expected2, actual2);

        attributes.remove("key2");
        String actual3 = attributes.get("key2");
        assertEquals(EmptyString, actual3);

        attributes.normalize(); // testing case sensitivity
        attributes.removeIgnoreCase("Key2");
        String actual3IgnoreCase = attributes.get("key2");
        assertEquals(EmptyString, actual3IgnoreCase);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveMethodWithNonExistentKey() {
        Attributes attributes = new Attributes();
        attributes.remove("nonExistentKey");
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        assertTrue(attributes.hasKey("key2"));
        assertTrue(attributes.hasKeyIgnoreCase("key2"));
        assertFalse(attributes.hasKey("nonExistentKey"));
        assertFalse(attributes.hasKeyIgnoreCase("NonExistentKey"));
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAllMethod() {
        Attributes baseAttributes = new Attributes();
        Attributes additionalAttributes = new Attributes();
        additionalAttributes.put("key3", "value3");
        additionalAttributes.put("key4", "value4");

        baseAttributes.addAll(additionalAttributes);

        String actual1 = baseAttributes.get("key1");
        String expected1 = "value1";
        assertEquals(expected1, actual1);

        String actual2 = baseAttributes.get("key2");
        String expected2 = "value2";
        assertEquals(expected2, actual2);

        String actual3 = baseAttributes.get("key3");
        String expected3 = "value3";
        assertEquals(expected3, actual3);

        String actual4 = baseAttributes.get("key4");
        String expected4 = "value4";
        assertEquals(expected4, actual4);

        assertEquals(4, baseAttributes.size());
    }

    @Test(expected = NullPointerException.class)
    public void testAddAllMethodWithNullAttributes() {
        Attributes baseAttributes = new Attributes();
        Attributes nullAttributes = null;
        baseAttributes.addAll(nullAttributes);
    }

    @Test
    public void testIteratorMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Iterator<Attribute> iterator = attributes.iterator();

        Attribute attribute = iterator.next();
        String key1 = attribute.getKey();
        String value1 = attribute.getValue();
        assertEquals("key1", key1);
        assertEquals("value1", value1);

        attribute = iterator.next();
        String key2 = attribute.getKey();
        String value2 = attribute.getValue();
        assertEquals("key2", key2);
        assertEquals("value2", value2);

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAsListMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> attributesList = attributes.asList();

        Attribute attribute1 = attributesList.get(0);
        String key1 = attribute1.getKey();
        String value1 = attribute1.getValue();
        assertEquals("key1", key1);
        assertEquals("value1", value1);

        Attribute attribute2 = attributesList.get(1);
        String key2 = attribute2.getKey();
        String value2 = attribute2.getValue();
        assertEquals("key2", key2);
        assertEquals("value2", value2);

        assertEquals(2, attributesList.size());
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("dataKey1", "dataValue1");
        attributes.put("dataKey2", "dataValue2");

        Map<String, String> dataset = attributes.dataset();

        String actual1 = dataset.get("dataKey1");
        String expected1 = "dataValue1";
        assertEquals(expected1, actual1);

        String actual2 = dataset.get("dataKey2");
        String expected2 = "dataValue2";
        assertEquals(expected2, actual2);

        assertEquals(2, dataset.size());
    }

    @Test
    public void testHtmlMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key1=\"value1\" key2=\"value2\"");

        String actualHtml = attributes.html();
        assertEquals(expectedHtml.toString(), actualHtml);
    }

    @Test
    public void testEqualsAndHashcodeMethods() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        assertTrue(attributes1.equals(attributes2));
        assertTrue(attributes1.hashCode() == attributes2.hashCode());

        attributes1.put("key3", "value3");
        assertFalse(attributes1.equals(attributes2));
        assertFalse(attributes1.hashCode() == attributes2.hashCode());
    }

    @Test
    public void testCloneMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clonedAttributes = attributes.clone();

        String actual1 = clonedAttributes.get("key1");
        String expected1 = "value1";
        assertEquals(expected1, actual1);

        String actual2 = clonedAttributes.get("key2");
        String expected2 = "value2";
        assertEquals(expected2, actual2);

        assertEquals(2, clonedAttributes.size());

        attributes.put("key3", "value3");
        String actual3 = clonedAttributes.get("key3");
        assertEquals(EmptyString, actual3);
    }
}