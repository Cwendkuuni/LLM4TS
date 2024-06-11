package org.apache.commons.collections4.keyvalue;

import org.apache.commons.collections4.MultiKey;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Test cases for MultiKey class.
 */
public class MultiKeyTest {

    @Test
    public void testMultiKeyTwoKeys() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        // When - Put the MultiKey in a Map
        Map<MultiKey<K>, String> map = new HashMap<>();
        map.put(multiKey, "localizedText");

        // Then - Retrieve the value using the MultiKey
        MultiKey<K> fetchMultiKey = new MultiKey<>(key1, key2);
        String localizedText = map.get(fetchMultiKey);
        Assert.assertEquals("localizedText", localizedText);
    }

    @Test
    public void testMultiKeyThreeKeys() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);

        // When - Put the MultiKey in a Map
        Map<MultiKey<K>, String> map = new HashMap<>();
        map.put(multiKey, "localizedText");

        // Then - Retrieve the value using the MultiKey
        MultiKey<K> fetchMultiKey = new MultiKey<>(key1, key2, key3);
        String localizedText = map.get(fetchMultiKey);
        Assert.assertEquals("localizedText", localizedText);
    }

    @Test
    public void testMultiKeyFourKeys() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);

        // When - Put the MultiKey in a Map
        Map<MultiKey<K>, String> map = new HashMap<>();
        map.put(multiKey, "localizedText");

        // Then - Retrieve the value using the MultiKey
        MultiKey<K> fetchMultiKey = new MultiKey<>(key1, key2, key3, key4);
        String localizedText = map.get(fetchMultiKey);
        Assert.assertEquals("localizedText", localizedText);
    }

    @Test
    public void testMultiKeyFiveKeys() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);

        // When - Put the MultiKey in a Map
        Map<MultiKey<K>, String> map = new HashMap<>();
        map.put(multiKey, "localizedText");

        // Then - Retrieve the value using the MultiKey
        MultiKey<K> fetchMultiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        String localizedText = map.get(fetchMultiKey);
        Assert.assertEquals("localizedText", localizedText);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeysArray() {
        // Given
        K[] keys = null;

        // When - Trying to create a MultiKey instance with the null keys array
        MultiKey<String> multiKey = new MultiKey<>(keys);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeyInMultiKey() {
        // Given
        K key1 = "key1";
        K key2 = null;
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testModifyingKeysAfterCreation() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        // When - Modifying the keys after creating the MultiKey
        key1 = "modifiedKey1";

        // Then - The equals method returns false for the original MultiKey
        Assert.assertFalse(multiKey.equals(new MultiKey<>(key1, key2)));
    }
}