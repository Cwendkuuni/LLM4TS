package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.StringAttribute;

public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");

        String expected = "value";
        String result = attributes.get("test");
        assertEquals(expected, result);
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "Value");

        String expected = "Value";
        String result = attributes.getIgnoreCase("test");
        assertEquals(expected, result);
    }

    @Test
    public void testGetMethodWithNullAttributes() {
        Attributes attributes = new Attributes();

        String result = attributes.get("test");
        assertEquals("", result);
    }

    @Test
    public void testPutMethodWithStringValue() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");

        String expected = "value";
        String result = attributes.get("test");
        assertEquals(expected, result);
    }

    @Test
    public void testPutMethodWithBooleanValue() {
        Attributes attributes = new Attributes();
        attributes.put("test", true);

        Attribute attribute = attributes.get("test");
        assertTrue(attribute instanceof BooleanAttribute);
    }

    @Test
    public void testPutMethodWithAttributeObject() {
        Attributes attributes = new Attributes();
        String key = "test";
        Attribute attribute = new StringAttribute(key, "value");
        attributes.put(attribute);

        String expected = "value";
        String result = attributes.get(key);
        assertEquals(expected, result);
    }

    @Test
    public void testRemoveMethodWithStringKey() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        attributes.remove("test");

        String result = attributes.get("test");
        assertNull(result);
    }

    @Test
    public void testRemoveMethodWithIgnoreCaseStringKey() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "Value");
        attributes.removeIgnoreCase("test");

        String result = attributes.get("Test");
        assertNull(result);
    }

    @Test
    public void testHasKeyMethodWithStringKey() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");

        boolean expected = true;
        boolean result = attributes.hasKey("test");
        assertEquals(expected, result);
    }

    @Test
    public void testHasKeyMethodWithIgnoreCaseStringKey() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "Value");

        boolean expected = true;
        boolean result = attributes.hasKeyIgnoreCase("test");
        assertEquals(expected, result);
    }

    @Test
    public void testHasKeyMethodWithNullAttributes() {
        Attributes attributes = new Attributes();

        boolean result = attributes.hasKey("test");
        assertFalse(result);
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");

        int expected = 2;
        int result = attributes.size();
        assertEquals(expected, result);
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("test2", "value2");
        attributes2.put("test3", "value3");

        Attributes expected = new Attributes();
        expected.put("test1", "value1");
        expected.put("test2", "value2");
        expected.put("test3", "value3");

        Attributes attributes = new Attributes();
        attributes.addAll(attributes1);
        attributes.addAll(attributes2);

        Iterator<Attribute> iterator = attributes.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            String key = attribute.getKey();
            String value = attribute.getValue();
            Attribute expectedAttribute = expected.get(key);
            assertEquals(expectedAttribute.getValue(), value);
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testIteratorMethod() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");

        List<Attribute> expected = Arrays.asList(attributes.get("test1"), attributes.get("test2"));
        List<Attribute> result = new ArrayList<>(attributes.asList());

        assertEquals(expected, result);
    }

    @Test
    public void testAsListMethod() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");

        List<Attribute> expected = Arrays.asList(attributes.get("test1"), attributes.get("test2"));
        List<Attribute> result = attributes.asList();

        assertEquals(expected, result);
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-test1", "value1");
        attributes.put("data-test2", "value2");

        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("test1", "value1");
        expected.put("test2", "value2");

        Map<String, String> result = attributes.dataset();
        assertEquals(expected, result);
    }

    @Test
    public void testHtmlMethodWithEmptyAttributes() {
        Attributes attributes = new Attributes();

        String expected = "";
        String result = attributes.html();
        assertEquals(expected, result);
    }

    @Test
    public void testHtmlMethodWithAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");

        String expected = " test1=\"value1\" test2=\"value2\"";
        String result = attributes.html();
        assertEquals(expected, result);
    }

    @Test
    public void testEqualsMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "value1");
        attributes1.put("test2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("test1", "value1");
        attributes2.put("test2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.put("test1", "value1");
        attributes3.put("test3", "value3");

        Attributes attributes4 = new Attributes();
        attributes4.put("test2", "value2");
        attributes4.put("test3", "value3");

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
        assertFalse(attributes1.equals(attributes4));
        assertFalse(attributes1.equals(null));
        assertFalse(attributes1.equals(""));
    }

    @Test
    public void testHashCodeMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "value1");
        attributes1.put("test2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("test1", "value1");
        attributes2.put("test2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.put("test1", "value1");
        attributes3.put("test3", "value3");

        int expected1 = attributes1.hashCode();
        int expected2 = attributes2.hashCode();
        int expected3 = attributes3.hashCode();

        int result1 = attributes1.hashCode();
        int result2 = attributes2.hashCode();
        int result3 = attributes3.hashCode();

        assertEquals(expected1, result1);
        assertEquals(expected2, result2);
        assertNotEquals(expected1, result3);
    }

    @Test
    public void testCloneMethod() throws CloneNotSupportedException {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");

        Attributes clonedAttributes = attributes.clone();

        Iterator<Attribute> iterator = attributes.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            Attribute clonedAttribute = clonedAttributes.get(attribute.getKey());
            assertEquals(attribute.getValue(), clonedAttribute.getValue());
            count++;
        }
        assertEquals(2, count);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlMethodWithIOException() throws IOException {
        Attributes attributes = new Attributes();
        Document document = new Document();
        attributes.html(document.outputSettings().asWriter(), document);
    }

    @Test
    public void testGetMethodWithNullKey() {
        Attributes attributes = new Attributes();

        String result = attributes.get(null);
        assertEquals("", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutMethodWithNullKey() {
        Attributes attributes = new Attributes();

        attributes.put(null, "value");
        fail("IllegalArgumentException expected but not thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutMethodWithNullValue() {
        Attributes attributes = new Attributes();

        attributes.put("key", null);
        fail("IllegalArgumentException expected but not thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutMethodWithDuplicateKey() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value1");

        attributes.put("test", "value2");
        fail("IllegalArgumentException expected but not thrown");
    }

    @Test
    public void testRemoveMethodWithNonExistentKey() {
        Attributes attributes = new Attributes();

        boolean result = attributes.remove("test");
        assertFalse(result);
    }

    @Test
    public void testRemoveMethodWithEmptyAttributes() {
        Attributes attributes = new Attributes();

        boolean result = attributes.remove("test");
        assertFalse(result);
    }

    @Test
    public void testHasKeyMethodWithNonExistentKey() {
        Attributes attributes = new Attributes();

        boolean result = attributes.hasKey("test");
        assertFalse(result);
    }

    @Test
    public void testHasKeyMethodWithEmptyAttributes() {
        Attributes attributes = new Attributes();

        boolean result = attributes.hasKey("test");
        assertFalse(result);
    }

    @Test
    public void testSizeMethodWithEmptyAttributes() {
        Attributes attributes = new Attributes();

        int result = attributes.size();
        assertEquals(0, result);
    }

    @Test
    public void testAddAllMethodWithEmptyAttributes() {
        Attributes attributes1 = new Attributes();

        Attributes attributes2 = new Attributes();
        attributes2.put("test", "value");

        attributes1.addAll(attributes2);

        boolean result = attributes1.hasKey("test");
        assertTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testIteratorMethodWithNullAttributes() {
        Attributes attributes = null;

        attributes.iterator();
        fail("NullPointerException expected but not thrown");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorMethodWithModifiableList() {
        Attributes attributes = new Attributes();
        List<Attribute> list = attributes.asList();

        list.remove(0);
        fail("UnsupportedOperationException expected but not thrown");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveMethodWithModifiableMap() {
        Attributes attributes = new Attributes();
        Map<String, Attribute> map = attributes.attributes;

        map.remove("test");
        fail("UnsupportedOperationException expected but not thrown");
    }

    @Test(expected = ClassCastException.class)
    public void testRemoveMethodWithNonAttributeObject() {
        Attributes attributes = new Attributes();
        attributes.put("test", new Object());

        try {
            attributes.remove("test");
            fail("ClassCastException expected but not thrown");
        } catch (ClassCastException e) {
            // expected
        }
    }

    @Test(expected = ClassCastException.class)
    public void testGetMethodWithNonAttributeObject() {
        Attributes attributes = new Attributes();
        attributes.put("test", new Object());

        try {
            attributes.get("test");
            fail("ClassCastException expected but not thrown");
        } catch (ClassCastException e) {
            // expected
        }
    }

    @Test(expected = ClassCastException.class)
    public void testPutMethodWithNonStringKey() {
        Attributes attributes = new Attributes();
        Integer key = 1;

        try {
            attributes.put(key, "value");
            fail("ClassCastException expected but not thrown");
        } catch (ClassCastException e) {
            // expected
        }
    }

    @Test(expected = ClassCastException.class)
    public void testPutMethodWithNonStringValue() {
        Attributes attributes = new Attributes();
        Integer value = 1;

        try {
            attributes.put("test", value);
            fail("ClassCastException expected but not thrown");
        } catch (ClassCastException e) {
            // expected
        }
    }

    @Test(expected = ClassCastException.class)
    public void testPutMethodWithNonAttributeObjectValue() {
        Attributes attributes = new Attributes();
        attributes.put("test", new Object());

        try {
            attributes.put("test", "value");
            fail("ClassCastException expected but not thrown");
        } catch (ClassCastException e) {
            // expected
        }
    }

    @Test(expected = ClassCastException.class)
    public void testDatasetMethodWithNonLinkedHashMap() {
        Attributes attributes = new Attributes();
        Map<String, String> dataset = new HashMap<>();

        try {
            attributes.dataset() = dataset;
            fail("ClassCastException expected but not thrown");
        } catch (ClassCastException e) {
            // expected
        }
    }

    @Test
    public void testDatasetMethodWithEmptyAttributes() {
        Attributes attributes = new Attributes();

        Map<String, String> dataset = attributes.dataset();
        assertTrue(dataset.isEmpty());
    }

    @Test
    public void testDatasetMethodWithAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("data-test1", "value1");
        attributes.put("data-test2", "value2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertTrue(dataset.get("test1").equals("value1"));
        assertTrue(dataset.get("test2").equals("value2"));
    }
}