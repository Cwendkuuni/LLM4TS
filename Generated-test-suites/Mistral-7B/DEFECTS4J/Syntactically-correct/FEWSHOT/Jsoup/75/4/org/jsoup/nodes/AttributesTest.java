package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testSizeAndCapacity() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("key2", "value2");
        attrs.put("key3", "value3");

        assertEquals(3, attrs.size());
        attrs.addAll(new Attributes().put("key4", "value4").put("key5", "value5"));
        assertEquals(5, attrs.size());

        attrs.checkCapacity(10);

        attrs.put("key6", "value6");
        attrs.put("key7", "value7");
        attrs.put("key8", "value8");
        attrs.put("key9", "value9");
        attrs.put("key10", "value10");

        assertEquals(10, attrs.size());
    }

    @Test
    public void testGetAndPut() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("key2", "value2");

        assertEquals("value1", attrs.get("key1"));
        assertEquals("value2", attrs.get("key2"));
        assertEquals("", attrs.get("key3"));

        attrs.put("key1", "newValue1");
        attrs.put("key2", "newValue2");

        assertEquals("newValue1", attrs.get("key1"));
        assertEquals("newValue2", attrs.get("key2"));
    }

    @Test
    public void testPutAndRemove() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("key2", "value2");
        attrs.put("key3", "value3");

        assertEquals(3, attrs.size());

        attrs.remove("key1");
        assertEquals(2, attrs.size());
        assertNull(attrs.get("key1"));

        attrs.remove("key2");
        assertEquals(1, attrs.size());
        assertNull(attrs.get("key2"));

        attrs.remove("key3");
        assertEquals(0, attrs.size());
        assertNull(attrs.get("key3"));
    }

    @Test
    public void testHasKey() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("key2", "value2");

        assertTrue(attrs.hasKey("key1"));
        assertTrue(attrs.hasKey("key2"));
        assertFalse(attrs.hasKey("key3"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attrs = new Attributes();
        attrs.put("KEY1", "value1");
        attrs.put("KEY2", "value2");

        assertTrue(attrs.hasKeyIgnoreCase("key1"));
        assertTrue(attrs.hasKeyIgnoreCase("KEY1"));
        assertTrue(attrs.hasKeyIgnoreCase("key2"));
        assertTrue(attrs.hasKeyIgnoreCase("KEY2"));
        assertFalse(attrs.hasKeyIgnoreCase("key3"));
    }

    @Test
    public void testPutAndPutIgnoreCase() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("key2", "value2");

        attrs.putIgnoreCase("key1", "newValue1");
        assertEquals("newValue1", attrs.get("key1"));

        attrs.putIgnoreCase("key3", "value3");
        attrs.putIgnoreCase("key3", "newValue3");
        assertEquals("newValue3", attrs.get("key3"));
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attrs = new Attributes();
        attrs.put("key1", true);

        assertTrue(attrs.hasKey("key1"));
        assertNull(attrs.get("key1"));
    }

    @Test
    public void testPutIgnoreCaseBooleanAttribute() {
        Attributes attrs = new Attributes();
        attrs.putIgnoreCase("key1", true);

        assertTrue(attrs.hasKey("key1"));
        assertNull(attrs.get("key1"));
    }

    @Test
    public void testPutAndPutIgnoreCaseBooleanAttribute() {
        Attributes attrs = new Attributes();
        attrs.put("key1", true);
        attrs.putIgnoreCase("key1", false);

        assertFalse(attrs.hasKey("key1"));
    }

    @Test
    public void testPutAndAddAll() {
        Attributes attrs1 = new Attributes().put("key1", "value1");
        Attributes attrs2 = new Attributes().put("key2", "value2");
        Attributes attrs3 = new Attributes();

        attrs3.put("key3", "value3");
        attrs3.addAll(attrs1);
        attrs3.addAll(attrs2);

        assertEquals(4, attrs3.size());
        assertEquals("value1", attrs3.get("key1"));
        assertEquals("value2", attrs3.get("key2"));
        assertNull(attrs3.get("key3"));
    }

    @Test
    public void testPutAndAddAllIgnoreCase() {
        Attributes attrs1 = new Attributes().put("key1", "value1");
        Attributes attrs2 = new Attributes().put("key2", "value2");
        Attributes attrs3 = new Attributes();

        attrs3.putIgnoreCase("key1", "newValue1");
        attrs3.addAll(attrs1);
        attrs3.addAll(attrs2);

        assertEquals(4, attrs3.size());
        assertEquals("newValue1", attrs3.get("key1"));
        assertEquals("value2", attrs3.get("key2"));
        assertNull(attrs3.get("key3"));
    }

    @Test
    public void testPutAndAddAllWithDuplicates() {
        Attributes attrs1 = new Attributes().put("key1", "value1");
        Attributes attrs2 = new Attributes().put("key1", "value2");
        Attributes attrs3 = new Attributes();

        attrs3.put("key3", "value3");
        attrs3.addAll(attrs1);
        attrs3.addAll(attrs2);

        assertEquals(3, attrs3.size());
        assertEquals("value2", attrs3.get("key1"));
        assertNull(attrs3.get("key3"));
    }

    @Test
    public void testPutAndAddAllWithDuplicatesIgnoreCase() {
        Attributes attrs1 = new Attributes().put("key1", "value1");
        Attributes attrs2 = new Attributes().put("key1", "value2");
        Attributes attrs3 = new Attributes();

        attrs3.putIgnoreCase("key1", "newValue1");
        attrs3.addAll(attrs1);
        attrs3.addAll(attrs2);

        assertEquals(3, attrs3.size());
        assertEquals("newValue1", attrs3.get("key1"));
        assertNull(attrs3.get("key3"));
    }

    @Test
    public void testPutAndPutIgnoreCaseBooleanAttributeWithDuplicates() {
        Attributes attrs1 = new Attributes().put("key1", true);
        Attributes attrs2 = new Attributes().put("key1", false);
        Attributes attrs3 = new Attributes();

        attrs3.putIgnoreCase("key1", true);
        attrs3.addAll(attrs1);
        attrs3.addAll(attrs2);

        assertEquals(2, attrs3.size());
        assertFalse(attrs3.hasKey("key1"));
    }

    @Test
    public void testPutAndPutIgnoreCaseBooleanAttributeWithDuplicates2() {
        Attributes attrs1 = new Attributes().put("key1", true);
        Attributes attrs2 = new Attributes().put("key1", true);
        Attributes attrs3 = new Attributes();

        attrs3.putIgnoreCase("key1", false);
        attrs3.addAll(attrs1);
        attrs3.addAll(attrs2);

        assertEquals(2, attrs3.size());
        assertFalse(attrs3.hasKey("key1"));
    }

    @Test
    public void testPutAndPutIgnoreCaseBooleanAttributeWithDuplicates3() {
        Attributes attrs1 = new Attributes().put("key1", true);
        Attributes attrs2 = new Attributes().put("key1", false);
        Attributes attrs3 = new Attributes();

        attrs3.put("key1", true);
        attrs3.addAll(attrs1);
        attrs3.addAll(attrs2);

        assertEquals(3, attrs3.size());
        assertTrue(attrs3.hasKey("key1"));
    }

    @Test
    public void testRemove() {
        Attributes attrs = new Attributes().put("key1", "value1").put("key2", "value2").put("key3", "value3");

        attrs.remove("key1");
        attrs.remove("key2");
        attrs.remove("key3");

        assertEquals(0, attrs.size());
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attrs = new Attributes().put("key1", "value1").put("key2", "value2").put("key3", "value3");

        attrs.removeIgnoreCase("key1");
        attrs.removeIgnoreCase("key2");
        attrs.removeIgnoreCase("key3");

        assertEquals(0, attrs.size());
    }

    @Test
    public void testRemoveWithDuplicates() {
        Attributes attrs = new Attributes().put("key1", "value1").put("key1", "value2").put("key2", "value2").put("key2", "value3");

        attrs.remove("key1");
        assertEquals(2, attrs.size());
        assertTrue(attrs.hasKey("key2"));

        attrs.removeIgnoreCase("key1");
        assertEquals(2, attrs.size());
        assertFalse(attrs.hasKey("key1"));
    }

    @Test
    public void testRemoveWithDuplicates2() {
        Attributes attrs = new Attributes().put("key1", "value1").put("key1", "value2").put("key2", "value2").put("key2", "value3");

        attrs.removeIgnoreCase("key1");
        assertEquals(2, attrs.size());
        assertFalse(attrs.hasKey("key1"));

        attrs.remove("key2");
        assertEquals(1, attrs.size());
        assertFalse(attrs.hasKey("key2"));
    }

    @Test
    public void testRemoveWithDuplicates3() {
        Attributes attrs = new Attributes().put("key1", "value1").put("key1", "value2").put("key2", "value2").put("key2", "value3");

        attrs.removeIgnoreCase("key1");
        assertEquals(3, attrs.size());
        assertTrue(attrs.hasKey("key2"));

        attrs.remove("key2");
        assertEquals(2, attrs.size());
        assertFalse(attrs.hasKey("key2"));
    }

    @Test
    public void testRemoveWithDuplicates4() {
        Attributes attrs = new Attributes().put("key1", "value1").put("key1", "value2").put("key2", "value2").put("key2", "value3");

        attrs.removeIgnoreCase("key1");
        assertEquals(3, attrs.size());
        assertFalse(attrs.hasKey("key1"));

        attrs.removeIgnoreCase("key2");
        assertEquals(2, attrs.size());
        assertFalse(attrs.hasKey("key2"));
    }

    @Test
    public void testDataset() {
        Attributes attrs = new Attributes().put("data-foo", "bar").put("data-baz", "qux");
        Map<String, String> dataset = attrs.dataset();

        assertEquals(2, dataset.size());
        assertEquals("bar", dataset.get("foo"));
        assertEquals("qux", dataset.get("baz"));
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attrs = new Attributes().put("key1", "value1").put("key2", "value2");

        StringBuilder expected = new StringBuilder();
        expected.append(" key1=\"value1\" key2=\"value2\"");

        assertEquals(expected.toString(), attrs.html());
    }

    @Test
    public void testToString() {
        Attributes attrs = new Attributes().put("key1", "value1").put("key2", "value2");

        StringBuilder expected = new StringBuilder();
        expected.append(" key1=\"value1\" key2=\"value2\"");

        assertEquals(expected.toString(), attrs.toString());
    }

    @Test
    public void testEquals() {
        Attributes attrs1 = new Attributes().put("key1", "value1").put("key2", "value2");
        Attributes attrs2 = new Attributes().put("key1", "value1").put("key2", "value2");
        Attributes attrs3 = new Attributes().put("key1", "value1").put("key3", "value3");

        assertTrue(attrs1.equals(attrs2));
        assertFalse(attrs1.equals(attrs3));
    }

    @Test
    public void testHashCode() {
        Attributes attrs1 = new Attributes().put("key1", "value1").put("key2", "value2");
        Attributes attrs2 = new Attributes().put("key1", "value1").put("key2", "value2");
        Attributes attrs3 = new Attributes().put("key1", "value1").put("key3", "value3");

        assertEquals(attrs1.hashCode(), attrs2.hashCode());
        assertNotEquals(attrs1.hashCode(), attrs3.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attrs = new Attributes().put("key1", "value1").put("key2", "value2");

        Attributes cloned = attrs.clone();

        assertEquals(attrs.size(), cloned.size());
        assertEquals(attrs.get("key1"), cloned.get("key1"));
        assertEquals(attrs.get("key2"), cloned.get("key2"));
    }

    @Test
    public void testNormalize() {
        Attributes attrs = new Attributes().put("KEY1", "value1").put("KEY2", "value2");

        attrs.normalize();

        assertEquals(2, attrs.size());
        assertEquals("key1", attrs.get("KEY1"));
        assertEquals("key2", attrs.get("KEY2"));
    }
}