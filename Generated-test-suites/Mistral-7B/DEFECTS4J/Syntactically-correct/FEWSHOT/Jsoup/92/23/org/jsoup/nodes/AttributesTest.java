package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testAddAndGet() {
        Attributes attr = new Attributes();
        attr.put("foo", "bar");
        assertEquals("bar", attr.get("foo"));
    }

    @Test
    public void testAddAndGetIgnoreCase() {
        Attributes attr = new Attributes();
        attr.put("FOO", "BAR");
        assertEquals("BAR", attr.getIgnoreCase("foo"));
    }

    @Test
    public void testPutNullValue() {
        Attributes attr = new Attributes();
        attr.put(null, null);
        assertNull(attr.get(null));
    }

    @Test
    public void testPutNullKey() {
        Attributes attr = new Attributes();
        attr.put(null, "foo");
        assertNull(attr.get(null));
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attr = new Attributes();
        attr.put("foo", true);
        assertFalse(attr.get("foo").equals("true"));
        assertTrue(attr.hasKey("foo"));
        assertNull(attr.get("bar"));
    }

    @Test
    public void testPutAndGetMultipleAttributes() {
        Attributes attr = new Attributes();
        attr.put("foo", "bar");
        attr.put("baz", "qux");
        assertEquals("bar", attr.get("foo"));
        assertEquals("qux", attr.get("baz"));
    }

    @Test
    public void testPutAndGetMultipleAttributesIgnoreCase() {
        Attributes attr = new Attributes();
        attr.put("FOO", "BAR");
        attr.put("BAZ", "QUX");
        assertEquals("BAR", attr.getIgnoreCase("foo"));
        assertEquals("QUX", attr.getIgnoreCase("baz"));
    }

    @Test
    public void testPutAndGetMultipleAttributesWithSameKey() {
        Attributes attr = new Attributes();
        attr.put("foo", "bar");
        attr.put("foo", "qux");
        assertEquals("qux", attr.get("foo"));
    }

    @Test
    public void testPutAndGetMultipleAttributesWithSameKeyIgnoreCase() {
        Attributes attr = new Attributes();
        attr.put("FOO", "BAR");
        attr.put("FOO", "QUX");
        assertEquals("QUX", attr.getIgnoreCase("foo"));
    }

    @Test
    public void testPutAndReplaceExistingAttribute() {
        Attributes attr = new Attributes();
        attr.put("foo", "bar");
        attr.put("foo", "qux");
        assertEquals("qux", attr.get("foo"));
    }

    @Test
    public void testPutAndReplaceExistingAttributeIgnoreCase() {
        Attributes attr = new Attributes();
        attr.put("FOO", "BAR");
        attr.put("FOO", "QUX");
        assertEquals("QUX", attr.getIgnoreCase("foo"));
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attr = new Attributes();
        attr.put("foo", "bar");
        attr.remove("foo");
        assertNull(attr.get("foo"));
    }

    @Test
    public void testRemoveAttributeIgnoreCase() {
        Attributes attr = new Attributes();
        attr.put("FOO", "BAR");
        attr.removeIgnoreCase("foo");
        assertNull(attr.getIgnoreCase("foo"));
    }

    @Test
    public void testRemoveNonExistentAttribute() {
        Attributes attr = new Attributes();
        attr.remove("foo");
        assertNull(attr.get("foo"));
    }

    @Test
    public void testRemoveNonExistentAttributeIgnoreCase() {
        Attributes attr = new Attributes();
        attr.removeIgnoreCase("foo");
        assertNull(attr.getIgnoreCase("foo"));
    }

    @Test
    public void testHasKey() {
        Attributes attr = new Attributes();
        attr.put("foo", "bar");
        assertTrue(attr.hasKey("foo"));
        assertFalse(attr.hasKey("bar"));
        assertFalse(attr.hasKeyIgnoreCase("BAR"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attr = new Attributes();
        attr.put("FOO", "BAR");
        assertTrue(attr.hasKeyIgnoreCase("foo"));
        assertTrue(attr.hasKeyIgnoreCase("FOO"));
        assertFalse(attr.hasKeyIgnoreCase("qux"));
    }

    @Test
    public void testSize() {
        Attributes attr = new Attributes();
        attr.put("foo", "bar");
        attr.put("baz", "qux");
        assertEquals(2, attr.size());
        attr.remove("foo");
        assertEquals(1, attr.size());
    }

    @Test
    public void testIsEmpty() {
        Attributes attr = new Attributes();
        assertTrue(attr.isEmpty());
        attr.put("foo", "bar");
        assertFalse(attr.isEmpty());
    }

    @Test
    public void testAddAll() {
        Attributes attr1 = new Attributes();
        attr1.put("foo", "bar");
        Attributes attr2 = new Attributes();
        attr2.put("baz", "qux");

        attr1.addAll(attr2);

        assertEquals(2, attr1.size());
        assertTrue(attr1.hasKey("foo"));
        assertTrue(attr1.hasKey("baz"));
        assertEquals("bar", attr1.get("foo"));
        assertEquals("qux", attr1.get("baz"));
    }

    @Test
    public void testClone() {
        Attributes orig = new Attributes();
        orig.put("foo", "bar");
        orig.put("baz", "qux");

        Attributes clone = orig.clone();
        assertEquals(orig.size(), clone.size());
        assertSame(orig, clone.parent);
    }

    @Test
    public void testEquals() {
        Attributes attr1 = new Attributes();
        attr1.put("foo", "bar");
        attr1.put("baz", "qux");

        Attributes attr2 = new Attributes();
        attr2.put("foo", "bar");
        attr2.put("baz", "qux");

        Attributes attr3 = new Attributes();
        attr3.put("foo", "qux");
        attr3.put("baz", "bar");

        Attributes attr4 = new Attributes();

        Attributes attr5 = (Attributes) attr1.clone();

        assertTrue(attr1.equals(attr1));
        assertTrue(attr1.equals(attr5));
        assertTrue(attr1.equals(attr2));
        assertFalse(attr1.equals(attr3));
        assertFalse(attr1.equals(attr4));
    }

    @Test
    public void testHashCode() {
        Attributes attr1 = new Attributes();
        attr1.put("foo", "bar");
        attr1.put("baz", "qux");

        Attributes attr2 = new Attributes();
        attr2.put("foo", "bar");
        attr2.put("baz", "qux");

        Attributes attr3 = new Attributes();
        attr3.put("foo", "qux");
        attr3.put("baz", "bar");

        Attributes attr4 = new Attributes();

        int h1 = attr1.hashCode();
        int h2 = attr2.hashCode();
        int h3 = attr3.hashCode();
        int h4 = attr4.hashCode();

        assertEquals(h1, attr1.hashCode());
        assertEquals(h1, attr5.hashCode());
        assertEquals(h1, attr2.hashCode());
        assertNotEquals(h1, h2);
        assertNotEquals(h1, h3);
        assertNotEquals(h1, h4);
    }

    @Test
    public void testDatatest() {
        Attributes attr = new Attributes();
        attr.put("data-foo", "bar");
        Map<String, String> dataset = attr.dataset();
        assertEquals("bar", dataset.get("foo"));
        assertEquals(1, dataset.size());
    }
}