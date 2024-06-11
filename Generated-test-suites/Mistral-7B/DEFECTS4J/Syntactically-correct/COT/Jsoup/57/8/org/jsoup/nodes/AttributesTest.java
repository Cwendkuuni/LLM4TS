import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.helper.Validate;

public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        String actual = attributes.getIgnoreCase("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    @Test
    public void testPutBooleanMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        boolean actual = attributes.hasKey("key");
        assertTrue(actual);
        attributes.put("key", false);
        actual = attributes.hasKey("key");
        assertFalse(actual);
    }

    @Test
    public void testPutAttributeMethod() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullValueMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        String actual = attributes.get("key");
        assertEquals("", actual);
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNullKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.remove(null);
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean actual = attributes.hasKey("key");
        assertTrue(actual);
        attributes.remove("key");
        actual = attributes.hasKey("key");
        assertFalse(actual);
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        boolean actual = attributes.hasKeyIgnoreCase("key");
        assertTrue(actual);
        attributes.remove("Key");
        actual = attributes.hasKeyIgnoreCase("key");
        assertFalse(actual);
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int actual = attributes.size();
        int expected = 2;
        assertEquals(expected, actual);
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        String actual1 = attributes1.get("key1");
        String expected1 = "value1";
        assertEquals(expected1, actual1);
        String actual2 = attributes1.get("key2");
        String expected2 = "value2";
        assertEquals(expected2, actual2);
    }

    @Test
    public void testRemoveIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        attributes.removeIgnoreCase("key");
        String actual = attributes.get("Key");
        assertEquals("", actual);
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveIgnoreCaseNullKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.removeIgnoreCase(null);
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "value");
        Map<String, String> actual = attributes.dataset();
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("data-key", "value");
        assertEquals(expected, actual);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlMethodEmptyAttributes() {
        Attributes attributes = new Attributes();
        StringBuilder accum = new StringBuilder();
        try {
            attributes.html(accum, new Document.OutputSettings());
            fail("Expected SerializationException");
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Test(expected = SerializationException.class)
    public void testHtmlMethodEmptyStringBuilder() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        StringBuilder accum = new StringBuilder();
        try {
            attributes.html(accum, new Document.OutputSettings());
            fail("Expected SerializationException");
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Test
    public void testHtmlMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        StringBuilder accum = new StringBuilder();
        try {
            attributes.html(accum, new Document.OutputSettings());
            String expected = " key1=\"value1\" key2=\"value2\"";
            String actual = accum.toString();
            assertEquals(expected, actual);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testEqualsMethod() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key2", "value2");
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
        assertFalse(attributes1.equals(null));
    }

    @Test
    public void testHashCodeMethod() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key2", "value2");
        int actual1 = attributes1.hashCode();
        int actual2 = attributes2.hashCode();
        int actual3 = attributes3.hashCode();
        assertEquals(actual1, actual2);
        assertNotEquals(actual1, actual3);
    }

    @Test
    public void testCloneMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Attributes clonedAttributes = attributes.clone();
        String actual = clonedAttributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }
}