
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");
    }

    @Test
    public void testGet() {
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("", attributes.get("key4"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("value1", attributes.getIgnoreCase("Key1"));
        assertEquals("value2", attributes.getIgnoreCase("KEY2"));
        assertEquals("", attributes.getIgnoreCase("key4"));
    }

    @Test
    public void testPut() {
        attributes.put("key4", "value4");
        assertEquals("value4", attributes.get("key4"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("bool1", true);
        assertEquals("bool1", "true", attributes.get("bool1"));
    }

    @Test
    public void testRemove() {
        attributes.remove("key2");
        assertEquals("", attributes.get("key2"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.removeIgnoreCase("Key2");
        assertEquals("", attributes.get("key2"));
    }

    @Test
    public void testHasKey() {
        assertEquals(true, attributes.hasKey("key1"));
        assertEquals(false, attributes.hasKey("key4"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertEquals(true, attributes.hasKeyIgnoreCase("Key1"));
        assertEquals(true, attributes.hasKeyIgnoreCase("KEY2"));
        assertEquals(false, attributes.hasKeyIgnoreCase("key4"));
    }

    @Test
    public void testSize() {
        assertEquals(3, attributes.size());
        attributes.put("key4", "value4");
        assertEquals(4, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key4", "value4");
        otherAttributes.put("key5", "value5");
        attributes.addAll(otherAttributes);
        assertEquals(5, attributes.size());
        assertEquals("value4", attributes.get("key4"));
        assertEquals("value5", attributes.get("key5"));
    }

    @Test
    public void testAsList() {
        List<Attribute> expectedList = new ArrayList<>();
        expectedList.add(new Attribute("key1", "value1"));
        expectedList.add(new Attribute("key2", "value2"));
        expectedList.add(new Attribute("key3", "value3"));

        List<Attribute> actualList = attributes.asList();

        assertEquals(expectedList.size(), actualList.size());
        for (int i = 0; i < expectedList.size(); i++) {
            assertEquals(expectedList.get(i), actualList.get(i));
        }
    }

    @Test
    public void testDataset() {
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("key1", "value1");
        expectedMap.put("key2", "value2");
        expectedMap.put("key3", "value3");

        Map<String, String> actualMap = attributes.dataset();

        assertEquals(expectedMap.size(), actualMap.size());
        for (String key : expectedMap.keySet()) {
            assertEquals(expectedMap.get(key), actualMap.get(key));
        }
    }

    @Test
    public void testHtml() {
        String expectedHtml = " key1=\"value1\" key2=\"value2\" key3=\"value3\"";
        assertEquals(expectedHtml, attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        otherAttributes.put("key2", "value2");
        otherAttributes.put("key3", "value3");

        assertEquals(true, attributes.equals(otherAttributes));
    }

    @Test
    public void testHashCode() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        otherAttributes.put("key2", "value2");
        otherAttributes.put("key3", "value3");

        assertEquals(attributes.hashCode(), otherAttributes.hashCode());
    }

    @Test
    public void testClone() {
        Attributes cloneAttributes = attributes.clone();

        assertEquals(attributes, cloneAttributes);
    }
}

