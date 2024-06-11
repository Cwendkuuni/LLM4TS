import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        // test for getting an attribute value by key
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testGetIgnoreCase() {
        // test for getting an attribute's value by case-insensitive key
        attributes.put("Key1", "Value1");
        attributes.put("kEy2", "Value2");
        assertEquals("Value1", attributes.getIgnoreCase("key1"));
        assertEquals("Value2", attributes.getIgnoreCase("KEY2"));
    }

    @Test
    public void testPut() {
        // test for setting a new attribute or replacing an existing one by key
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));

        // test for setting a new boolean attribute
        attributes.put("key3", true);
        assertTrue(attributes.hasKey("key3"));
        assertEquals("", attributes.get("key3"));

        // test for removing an attribute if value is false
        attributes.put("key3", false);
        assertFalse(attributes.hasKey("key3"));
    }

    @Test
    public void testPutAttribute() {
        // test for set a new attribute or replace an existing one
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        attributes.put(attribute1);
        attributes.put(attribute2);
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testRemove() {
        // test for removing an attribute by key
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertEquals("", attributes.get("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        // test for removing an attribute by key ignoring case sensitivity
        attributes.put("Key1", "Value1");
        attributes.put("Key2", "Value2");
        attributes.removeIgnoreCase("key1");
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
        assertEquals("", attributes.getIgnoreCase("key1"));
    }

    @Test
    public void testHasKey() {
        // test for checking if attributes contain an attribute with the given key (case-sensitive)
        assertFalse(attributes.hasKey("key1"));
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        // test for checking if attributes contain an attribute with the given key (case-insensitive)
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
        attributes.put("Key1", "Value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testSize() {
        // test for getting the number of attributes in the set
        assertEquals(0, attributes.size());
        attributes.put("key1", "value1");
        assertEquals(1, attributes.size());
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        // test for adding all attributes from another set to this set
        Attributes newAttributes = new Attributes();
        newAttributes.put("newKey1", "newValue1");
        newAttributes.put("newKey2", "newValue2");
        attributes.addAll(newAttributes);
        assertTrue(attributes.hasKey("newKey1"));
        assertTrue(attributes.hasKey("newKey2"));
    }

    @Test
    public void testIterator() {
        // test for iterating through the attributes set
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Iterator<Attribute> iterator = attributes.iterator();
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAsList() {
        // test for getting the attributes as a List
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> attributeList = attributes.asList();
        assertNotNull(attributeList);
        assertEquals(2, attributeList.size());
        assertEquals("key1", attributeList.get(0).getKey());
        assertEquals("value1", attributeList.get(0).getValue());
        assertEquals("key2", attributeList.get(1).getKey());
        assertEquals("value2", attributeList.get(1).getValue());
        assertThrows(UnsupportedOperationException.class, () -> attributeList.add(new Attribute("key3", "value3")));
    }

    @Test
    public void testDataset() {
        // test for retrieving a filtered view of attributes that are HTML5 custom data attributes
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        assertNotNull(dataset);
        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("key1"));
        assertEquals("value2", dataset.get("key2"));
    }

    @Test
    public void testHtml() {
        // test for getting the HTML representation of the attributes
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String expected = " key1=\"value1\" key2=\"value2\"";
        String html = attributes.html();
        assertEquals(expected, html);
    }

    @Test
    public void testEquals() {
        // test for checking if the attributes are equal to another set of attributes
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        otherAttributes.put("key2", "value2");
        assertTrue(attributes.equals(attributes));
        assertFalse(attributes.equals(otherAttributes));
    }
    
    @Test
    public void testHashCode() {
        // test for calculating the hashcode of the attributes
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        otherAttributes.put("key2", "value2");
        assertEquals(attributes.hashCode(), attributes.hashCode());
        assertNotEquals(attributes.hashCode(), otherAttributes.hashCode());
    }

    @Test
    public void testClone() {
        // test for cloning the attributes
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clone = attributes.clone();
        assertNotNull(clone);
        assertEquals(attributes.size(), clone.size());
        assertEquals(attributes.get("key1"), clone.get("key1"));
        assertEquals(attributes.get("key2"), clone.get("key2"));
    }

    @Test
    public void testToString() {
        // test for getting the string representation of the attributes (same as html())
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String expected = " key1=\"value1\" key2=\"value2\"";
        String toString = attributes.toString();
        assertEquals(expected, toString);
    }
}