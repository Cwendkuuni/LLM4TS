import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("KEY", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("", attributes.getIgnoreCase("NON_EXISTENT_KEY"));
    }

    @Test
    public void testPut() {
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));

        attributes.put("key2", "value3");
        assertEquals("value3", attributes.get("key2"));

        attributes.put("key3", "value4");
        assertEquals("value4", attributes.get("key3"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key5", true);
        assertTrue(attributes.hasKey("key5"));
        assertNull(attributes.get("key5"));

        attributes.remove("key5");
        assertFalse(attributes.hasKey("key5"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key6", "value6");
        attributes.put(attribute);
        assertEquals("value6", attributes.get("key6").getValue());

        Attribute newAttribute = new Attribute("key6", "newValue6");
        attributes.put(newAttribute);
        assertEquals("newValue6", attributes.get("key6").getValue());
    }

    @Test
    public void testRemove() {
        attributes.put("key7", "value7");
        assertTrue(attributes.hasKey("key7"));
        attributes.remove("key7");
        assertFalse(attributes.hasKey("key7"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("KEY8", "value8");
        assertTrue(attributes.hasKeyIgnoreCase("key8"));
        attributes.removeIgnoreCase("KEY8");
        assertFalse(attributes.hasKeyIgnoreCase("key8"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key9", "value9");
        assertTrue(attributes.hasKey("key9"));
        assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("KEY10", "value10");
        assertTrue(attributes.hasKeyIgnoreCase("key10"));
        assertFalse(attributes.hasKeyIgnoreCase("NON_EXISTENT_KEY"));
    }

    @Test
    public void testSize() {
        attributes.put("key11", "value11");
        assertEquals(1, attributes.size());

        attributes.put("key12", "value12");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes2 = new Attributes();
        attributes2.put("key13", "value13");
        attributes2.put("key14", "value14");

        attributes.addAll(attributes2);
        assertEquals(4, attributes.size());
        assertTrue(attributes.hasKey("key13"));
        assertTrue(attributes.hasKey("key14"));
    }

    @Test
    public void testDataset() {
        attributes.put("data-key15", "value15");
        attributes.put("data-key16", "value16");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertTrue(dataset.containsKey("data-key15"));
        assertTrue(dataset.containsKey("data-key16"));
    }

    @Test
    public void testHtml() {
        attributes.put("key17", "value17");
        assertEquals("<key17>value17</key17>", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("key18", "value18");
        attributes2.put("key18", "value18");
        attributes3.put("key19", "value19");

        assertEquals(attributes1, attributes1);
        assertEquals(attributes1, attributes2);
        assertNotEquals(attributes1, attributes3);
    }

    @Test
    public void testClone() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key20", "value20");
        Attributes attributes2 = attributes1.clone();
        assertEquals(attributes1, attributes2);
    }
}