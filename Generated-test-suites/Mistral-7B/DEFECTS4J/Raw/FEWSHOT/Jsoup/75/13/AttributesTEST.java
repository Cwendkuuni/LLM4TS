package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class AttributesTest {

    private final Attributes attributes;

    public AttributesTest(String... keysAndValues) {
        attributes = new Attributes();
        for (String keyValue : keysAndValues) {
            String[] keyVal = keyValue.split(":");
            if (keyVal.length == 1) {
                attributes.put(keyVal[0]);
            } else {
                attributes.put(keyVal[0], keyVal[1]);
            }
        }
    }

    @Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            new Object[]{"foo"},
            new Object[]{"foo:bar"},
            new Object[]{"foo:bar", "baz"},
            new Object[]{"foo:bar", "baz:qux"},
            new Object[]{"foo:bar", "boo:quux", "quux:quux"},
            new Object[]{"foo:bar", "boo:quux", "quux:quux", "quux:quux:quux"}
        });
    }

    @Test
    public void testSize() {
        assertEquals(attributes.size(), keysAndValues.length / 2);
    }

    @Test
    public void testGet() {
        for (int i = 0; i < keysAndValues.length; i += 2) {
            String key = keysAndValues[i];
            String value = keysAndValues[i + 1];
            assertEquals(value, attributes.get(key));
        }
    }

    @Test
    public void testGetIgnoreCase() {
        for (int i = 0; i < keysAndValues.length; i += 2) {
            String key = keysAndValues[i];
            String value = keysAndValues[i + 1];
            assertEquals(value, attributes.getIgnoreCase(key));
        }
    }

    @Test
    public void testHasKey() {
        for (int i = 0; i < keysAndValues.length; i += 2) {
            String key = keysAndValues[i];
            assertTrue(attributes.hasKey(key));
            assertTrue(attributes.hasKeyIgnoreCase(key));
        }
    }

    @Test
    public void testHasKeyIgnoreCaseFalse() {
        for (String key : Arrays.asList("baz", "quux", "quux:quux")) {
            assertFalse(attributes.hasKeyIgnoreCase(key));
        }
    }

    @Test
    public void testRemove() {
        String keyToRemove = keysAndValues[0];
        attributes.remove(keyToRemove);
        assertFalse(attributes.hasKey(keyToRemove));
        assertFalse(attributes.hasKeyIgnoreCase(keyToRemove));
    }

    @Test
    public void testRemoveIgnoreCase() {
        String keyToRemove = keysAndValues[0];
        attributes.removeIgnoreCase(keyToRemove);
        assertFalse(attributes.hasKey(keyToRemove));
        assertFalse(attributes.hasKeyIgnoreCase(keyToRemove));
    }

    @Test
    public void testPut() {
        String newKey = "quux";
        String newValue = "quux";
        attributes.put(newKey, newValue);
        assertTrue(attributes.hasKey(newKey));
        assertTrue(attributes.hasKeyIgnoreCase(newKey));
        assertEquals(newValue, attributes.get(newKey));
        assertEquals(newValue, attributes.getIgnoreCase(newKey));
    }

    @Test
    public void testPutIgnoreCase() {
        String newKey = "quux";
        String newValue = "quux";
        attributes.putIgnoreCase(newKey, newValue);
        assertTrue(attributes.hasKey(newKey));
        assertTrue(attributes.hasKeyIgnoreCase(newKey));
        assertEquals(newValue, attributes.get(newKey));
        assertEquals(newValue, attributes.getIgnoreCase(newKey));
    }

    @Test
    public void testPutOverwrite() {
        String oldValue = "bar";
        String newValue = "baz";
        attributes.put("foo", oldValue);
        attributes.put("foo", newValue);
        assertFalse(attributes.hasKey(oldValue));
        assertTrue(attributes.hasKey(newValue));
        assertEquals(newValue, attributes.get(
                "foo"));
        assertEquals(newValue, attributes.getIgnoreCase("foo"));
        assertEquals(newValue, attributes.getIgnoreCase("FOO"));
    }

    @Test
    public void testPutOverwriteIgnoreCase() {
        String oldValue = "BAR";
        String newValue = "BAZ";
        attributes.put("Foo", oldValue);
        attributes.put("Foo", newValue);
        assertFalse(attributes.hasKey(oldValue));
        assertTrue(attributes.hasKey(newValue));
        assertEquals(newValue, attributes.get(
                "Foo"));
        assertEquals(newValue, attributes.getIgnoreCase("foo"));
        assertEquals(newValue, attributes.getIgnoreCase("FOO"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("foo", false);
        assertFalse(attributes.hasKey("foo"));
        assertNull(attributes.get("foo"));
        assertFalse(attributes.hasKeyIgnoreCase("foo"));
        assertNull(attributes.getIgnoreCase("foo"));
        assertTrue(attributes.hasKey("foo:false"));
        assertNull(attributes.get("foo:false"));
        assertFalse(attributes.hasKeyIgnoreCase("foo:false"));
        assertNull(attributes.getIgnoreCase("foo:false"));
        assertFalse(attributes.hasKey("foo:true"));
        assertNull(attributes.get("foo:true"));
        assertFalse(attributes.hasKeyIgnoreCase("foo:true"));
        assertNull(attributes.getIgnoreCase("foo:true"));
        assertFalse(attributes.hasKey("foo"));
        assertNull(attributes.get("foo"));
        assertFalse(attributes.hasKeyIgnoreCase("foo"));
        assertNull(attributes.getIgnoreCase("foo"));
    }

    @Test
    public void testPutBooleanIgnoreCase() {
        attributes.put("foo", false);
        assertFalse(attributes.hasKey("foo:false"));
        assertNull(attributes.get("foo:false"));
        assertFalse(attributes.hasKeyIgnoreCase("foo:false"));
        assertNull(attributes.getIgnoreCase("foo:false"));
        assertFalse(attributes.hasKey("foo:true"));
        assertNull(attributes.get("foo:true"));
        assertFalse(attributes.hasKeyIgnoreCase("foo:true"));
        assertNull(attributes.getIgnoreCase("foo:true"));
        attributes.putIgnoreCase("foo:false", null);
        assertTrue(attributes.hasKey("foo:false"));
        assertNull(attributes.get("foo:false"));
        assertTrue(attributes.hasKeyIgnoreCase("foo:false"));
        assertNull(attributes.getIgnoreCase("foo:false"));
        assertFalse(attributes.hasKey("foo:true"));
        assertNull(attributes.get("foo:true"));
        assertFalse(attributes.hasKeyIgnoreCase("foo:true"));
        assertNull(attributes.getIgnoreCase("foo:true"));
        assertFalse(attributes.hasKey("foo"));
        assertNull(attributes.get("foo"));
        assertFalse(attributes.hasKeyIgnoreCase("foo"));
        assertNull(attributes.getIgnoreCase("foo"));
    }

    @Test
    public void testToList() {
        List<Map.Entry<String, String>> list = attributes.asList();
        assertEquals(size(), list.size());
        for (int i = 0; i < size(); i++) {
            Map.Entry<String, String> entry = list.get(i);
            String key = entry.getKey();
            String value = entry.getValue();
            assertEquals(key, attributes.get(key));
            assertEquals(value, attributes.get(key));
            assertEquals(key, attributes.getIgnoreCase(key));
            assertEquals(value, attributes.getIgnoreCase(key));
        }
    }

    @Test
    public void testClone() {
        Attributes clonedAttributes = attributes.clone();
        assertEquals(size(), clonedAttributes.size());
        for (int i = 0; i < size(); i++) {
            String key = keysAndValues[i * 2];
            String value = keysAndValues[i * 2 + 1];
            assertEquals(value, clonedAttributes.get(key));
        }
    }

    @Test
    public void testEquals() {
        Attributes otherAttributes = new Attributes();
        for (String keyValue : keysAndValues) {
            String[] keyVal = keyValue.split(":");
            if (keyVal.length == 1) {
                otherAttributes.put(keyVal[0]);
            } else {
                otherAttributes.put(keyVal[0], keyVal[1]);
            }
        }
        assertTrue(attributes.equals(otherAttributes));
        assertSame(attributes, otherAttributes);
    }

    @Test
    public void testToString() {
        assertEquals("Attributes{" + keysAndValues[0] + "=" + keysAndValues[1] + "}", attributes.toString());
    }

    @Test
    public void testDataset() {
        Map<String, String> dataset = attributes.dataset();
        assertEquals(size() / 2, dataset.size());
        for (Map.Entry<String, String> entry : dataset.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            assertTrue(key.startsWith(Attributes.dataPrefix));
            assertEquals(value, attributes.get(key));
        }
    }

    @Test
    public void testHtml() throws IOException {
        StringBuilder accum = new StringBuilder();
        attributes.html(accum, null);
        String html = accum.toString();
        for (int i = 0; i < size(); i++) {
            String key = keysAndValues[i * 2];
            String value = keysAndValues[i * 2 + 1];
            String attributeString = " " + key + "=\"" + value + "\"";
            assertTrue(html.contains(attributeString));
        }
    }
}