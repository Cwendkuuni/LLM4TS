import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {
    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");
        attributes.put("height", "180");
    }

    @Test
    public void testGet() {
        assertEquals("John", attributes.get("name"));
        assertEquals("25", attributes.get("age"));
        assertEquals("180", attributes.get("height"));
        assertEquals("", attributes.get("weight"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("John", attributes.getIgnoreCase("Name"));
        assertEquals("25", attributes.getIgnoreCase("Age"));
        assertEquals("180", attributes.getIgnoreCase("Height"));
        assertEquals("", attributes.getIgnoreCase("Weight"));
    }

    @Test
    public void testPut() {
        attributes.put("weight", "70");
        assertEquals("70", attributes.get("weight"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("married", true);
        assertEquals("true", attributes.get("married"));

        attributes.put("employed", false);
        assertFalse(attributes.hasKey("employed"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attr = new Attribute("weight", "70");
        attributes.put(attr);
        assertEquals("70", attributes.get("weight"));
    }

    @Test
    public void testRemove() {
        attributes.remove("age");
        assertFalse(attributes.hasKey("age"));
        assertEquals("", attributes.get("age"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.removeIgnoreCase("Name");
        assertFalse(attributes.hasKey("name"));
        assertEquals("", attributes.get("name"));
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("name"));
        assertTrue(attributes.hasKey("age"));
        assertTrue(attributes.hasKey("height"));
        assertFalse(attributes.hasKey("weight"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("Name"));
        assertTrue(attributes.hasKeyIgnoreCase("Age"));
        assertTrue(attributes.hasKeyIgnoreCase("Height"));
        assertFalse(attributes.hasKeyIgnoreCase("Weight"));
    }

    @Test
    public void testSize() {
        assertEquals(3, attributes.size());

        attributes.put("weight", "70");
        assertEquals(4, attributes.size());

        attributes.remove("age");
        assertEquals(3, attributes.size());

        attributes.removeIgnoreCase("Name");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("weight", "70");
        newAttributes.put("gender", "male");

        attributes.addAll(newAttributes);

        assertTrue(attributes.hasKey("weight"));
        assertTrue(attributes.hasKey("gender"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (Attribute attr : attributes) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testAsList() {
        int count = 0;
        for (Attribute attr : attributes.asList()) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    
    public void testDataset() {
        attributes.put("data-id", "123");
        attributes.put("data-name", "John");
        
        Map<String, String> dataset = attributes.dataset();
        assertEquals("123", dataset.get("id"));
        assertEquals("John", dataset.get("name"));
    }
    
    @Test
    public void testHtml() {
        attributes.put("class", "container");
        attributes.put("style", "background: blue;");
        
        String expectedHtml = " class=\"container\" style=\"background: blue;\"";
        assertEquals(expectedHtml, attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes sameAttributes = new Attributes();
        sameAttributes.put("name", "John");
        sameAttributes.put("age", "25");
        sameAttributes.put("height", "180");

        Attributes differentAttributes = new Attributes();
        differentAttributes.put("name", "John");
        differentAttributes.put("age", "30");
        differentAttributes.put("height", "180");

        assertTrue(attributes.equals(sameAttributes));
        assertFalse(attributes.equals(differentAttributes));
    }

    @Test
    public void testHashCode() {
        Attributes sameAttributes = new Attributes();
        sameAttributes.put("name", "John");
        sameAttributes.put("age", "25");
        sameAttributes.put("height", "180");

        assertEquals(attributes.hashCode(), sameAttributes.hashCode());
    }

    @Test
    public void testClone() {
        Attributes cloned = attributes.clone();

        assertEquals(attributes, cloned);
        assertNotSame(attributes, cloned);

        attributes.put("weight", "70");
        assertFalse(attributes.equals(cloned));
    }

    @Test
    public void testDatasetPut() {
        attributes.dataset().put("id", "123");
        attributes.dataset().put("name", "John");

        assertEquals("123", attributes.get("data-id"));
        assertEquals("John", attributes.get("data-name"));
    }

    @Test
    public void testDatasetIterator() {
        attributes.put("data-id", "123");
        attributes.put("data-name", "John");

        int count = 0;
        for (Map.Entry<String, String> entry : attributes.dataset().entrySet()) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testDatasetRemove() {
        attributes.put("data-id", "123");
        attributes.put("data-name", "John");

        attributes.dataset().remove("id");
        assertFalse(attributes.hasKey("data-id"));
    }

    @Test
    public void testDataKey() {
        assertEquals("data-key", Attributes.dataKey("key"));
    }
}