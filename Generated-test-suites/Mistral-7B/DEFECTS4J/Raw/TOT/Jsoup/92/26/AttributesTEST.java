package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGetAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);

        attributes.put("Key", "VALUE"); // test case sensitive
        actual = attributes.get("Key");
        assertEquals(expected, actual);

        attributes.normalize(); // test normalized keys
        actual = attributes.get("key");
        assertEquals(expected, actual);

        attributes.put("", "empty"); // test empty key
        actual = attributes.get("");
        assertEquals(expected, actual);

        attributes.put("nullKey", null); // test null value
        actual = attributes.get("nullKey");
        assertNull(actual);
    }

    @Test
    public void testGetAttributeIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.getIgnoreCase("key");
        String expected = "value";
        assertEquals(expected, actual);

        attributes.put("Key", "VALUE"); // test case insensitive
        actual = attributes.getIgnoreCase("key");
        assertEquals(expected, actual);

        attributes.put("kEy", "VALUE"); // test key with mixed case
        actual = attributes.getIgnoreCase("key");
        assertEquals(expected, actual);

        attributes.put("", "empty"); // test empty key
        actual = attributes.getIgnoreCase("");
        assertEquals(expected, actual);

        attributes.put("nullKey", null); // test null value
        actual = attributes.getIgnoreCase("nullKey");
        assertNull(actual);
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        String key = "key";
        String value = "value";

        attributes.put(key, value);
        int sizeBeforePut = attributes.size();
        attributes.put(key, value); // test putting the same key-value pair twice
        int sizeAfterPut = attributes.size();

        assertEquals(sizeBeforePut, sizeAfterPut); // test that the size remains the same

        String actual = attributes.get(key);
        assertEquals(value, actual);

        attributes.putIgnoreCase(key, "newValue"); // test putting with case insensitive key
        actual = attributes.get(key);
        assertEquals("newValue", actual);

        attributes.putIgnoreCase("Key", "NEWVALUE"); // test putting with case insensitive key
        actual = attributes.get(key);
        assertEquals("newValue", actual);
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        String key = "key";
        boolean value = true;

        attributes.put(key, value);
        int sizeBeforePut = attributes.size();
        attributes.put(key, value); // test putting the same key-boolean value pair twice
        int sizeAfterPut = attributes.size();

        assertEquals(sizeBeforePut, sizeAfterPut); // test that the size remains the same

        boolean actual = attributes.getBoolean(key);
        assertTrue(actual);

        attributes.putIgnoreCase(key, false); // test putting with case insensitive key
        actual = attributes.getBoolean(key);
        assertFalse(actual);

        attributes.remove(key); // test removing the key
        actual = attributes.getBoolean(key);
        assertFalse(actual);
    }

    @Test
    public void testPutAttributeWithNullValue() {
        Attributes attributes = new Attributes();
        String key = "key";

        attributes.put(key, null);
        int sizeBeforePut = attributes.size();
        attributes.put(key, null); // test putting the same key-null value pair twice
        int sizeAfterPut = attributes.size();

        assertEquals(sizeBeforePut, sizeAfterPut); // test that the size remains the same

        String actual = attributes.get(key);
        assertNull(actual);
    }

    @Test
    public void testPutAttributeWithEmptyStringValue() {
        Attributes attributes = new Attributes();
        String key = "key";

        attributes.put(key, "");
        int sizeBeforePut = attributes.size();
        attributes.put(key, ""); // test putting the same key-empty string value pair twice
        int sizeAfterPut = attributes.size();

        assertEquals(sizeBeforePut, sizeAfterPut); // test that the size remains the same

        String actual = attributes.get(key);
        assertEquals("", actual);
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        String key = "key";
        String value = "value";

        attributes.put(key, value);
        int sizeBeforeRemove = attributes.size();
        attributes.remove(key);
        int sizeAfterRemove = attributes.size();

        assertEquals(sizeBeforeRemove - 1, sizeAfterRemove); // test that the size decreases by one

        String actual = attributes.get(key);
        assertNull(actual);
    }

    @Test
    public void testRemoveAttributeIgnoreCase() {
        Attributes attributes = new Attributes();
        String key = "key";
        String value = "value";

        attributes.put(key, value);
        int sizeBeforeRemove = attributes.size();
        attributes.removeIgnoreCase("key");
        int sizeAfterRemove = attributes.size();

        assertEquals(sizeBeforeRemove - 1, sizeAfterRemove); // test that the size decreases by one

        String actual = attributes.get(key);
        assertNull(actual);
    }

    @Test
    public void testHasAttribute() {
        Attributes attributes = new Attributes();
        String key = "key";
        String value = "value";

        attributes.put(key, value);
        boolean actual = attributes.hasKey(key);
        assertTrue(actual);

        attributes.remove(key);
        actual = attributes.hasKey(key);
        assertFalse(actual);

        attributes.putIgnoreCase("KEY", value);
        actual = attributes.hasKey(key);
        assertTrue(actual);

        attributes.removeIgnoreCase("KEY");
        actual = attributes.hasKey(key);
        assertFalse(actual);
    }

    @Test
    public void testHasAttributeIgnoreCaseWithEmptyStringValue() {
        Attributes attributes = new Attributes();
        String key = "key";
        String value = "";

        attributes.put(key, value);
        boolean actual = attributes.hasKeyIgnoreCase(key);
        assertTrue(actual);

        attributes.remove(key);
        actual = attributes.hasKeyIgnoreCase(key);
        assertFalse(actual);

        attributes.putIgnoreCase("KEY", value);
        actual = attributes.hasKeyIgnoreCase(key);
        assertTrue(actual);

        attributes.removeIgnoreCase("KEY");
        actual = attributes.hasKeyIgnoreCase(key);
        assertFalse(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullAttributeKey() {
        Attributes attributes = new Attributes();
        String nullKey = null;
        String value = "value";

        attributes.put(nullKey, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullAttributeValue() {
        Attributes attributes = new Attributes();
        String key = "key";
        String nullValue = null;

        attributes.put(key, nullValue);
    }
}