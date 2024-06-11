import org.junit.*;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        assertEquals("value", attributes.getIgnoreCase("KEY"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        assertTrue(attributes.hasKey("key"));
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutBooleanTrue() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);

        assertTrue(attributes.hasKey("key"));
        assertEquals("true", attributes.get("key"));
    }

    @Test
    public void testPutBooleanFalse() {
        Attributes attributes = new Attributes();
        attributes.put("key", false);

        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attr = new Attribute("key", "value");
        attributes.put(attr);

        assertTrue(attributes.hasKey("key"));
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");

        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.removeIgnoreCase("KeY");

        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        assertTrue(attributes.hasKeyIgnoreCase("KEY"));
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
        Attributes attributes = new Attributes();
        Attributes incoming = new Attributes();
        incoming.put("key", "value");
        attributes.addAll(incoming);

        assertTrue(attributes.hasKey("key"));
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Iterator<Attribute> iterator = attributes.iterator();

        assertTrue(iterator.hasNext());
        Attribute attr1 = iterator.next();
        assertEquals("key1", attr1.getKey());
        assertEquals("value1", attr1.getValue());
        assertTrue(iterator.hasNext());
        Attribute attr2 = iterator.next();
        assertEquals("key2", attr2.getKey());
        assertEquals("value2", attr2.getValue());
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> attributeList = attributes.asList();

        assertEquals(2, attributeList.size());
        Attribute attr1 = attributeList.get(0);
        assertEquals("key1", attr1.getKey());
        assertEquals("value1", attr1.getValue());
        Attribute attr2 = attributeList.get(1);
        assertEquals("key2", attr2.getKey());
        assertEquals("value2", attr2.getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("key1"));
        assertEquals("value2", dataset.get("key2"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String html = attributes.html();

        assertEquals(" key1=\"value1\" key2=\"value2\"", html);
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String str = attributes.toString();

        assertEquals(" key1=\"value1\" key2=\"value2\"", str);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        assertTrue(attributes1.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int hashCode = attributes.hashCode();

        assertEquals(2408779, hashCode);
    }

    @Test
    public void testClone() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = attributes1.clone();

        assertTrue(attributes1 != attributes2);
        assertTrue(attributes1.equals(attributes2));
    }

    @Test
    public void testDataKey() {
        String dataKey = Attributes.dataKey("key");

        assertEquals("data-key", dataKey);
    }
}