
import org.junit.*;
import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;

public class AttributesTest {
    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        // test when attributes is null
        assertEquals("", attributes.get("key"));

        // test when attribute key not found
        attributes.put("name", "John");
        assertEquals("", attributes.get("age"));

        // test when attribute key found
        assertEquals("John", attributes.get("name"));
    }

    @Test
    public void testGetIgnoreCase() {
        // test when attributes is null
        assertEquals("", attributes.getIgnoreCase("key"));

        // test when attribute key not found
        attributes.put("name", "John");
        assertEquals("", attributes.getIgnoreCase("age"));

        // test when attribute key found
        assertEquals("John", attributes.getIgnoreCase("NAME"));
    }

    @Test
    public void testPut() {
        // test putting a new attribute
        attributes.put("name", "John");
        assertEquals("John", attributes.get("name"));

        // test replacing an existing attribute
        attributes.put("name", "Jane");
        assertEquals("Jane", attributes.get("name"));
    }

    @Test
    public void testPutBoolean() {
        // test putting a true boolean attribute
        attributes.put("isSingle", true);
        assertEquals("true", attributes.get("isSingle"));

        // test putting a false boolean attribute
        attributes.put("isSingle", false);
        assertEquals("", attributes.get("isSingle"));
    }

    @Test
    public void testRemove() {
        // test removing an existing attribute
        attributes.put("name", "John");
        attributes.remove("name");
        assertEquals("", attributes.get("name"));

        // test removing a non-existing attribute
        attributes.remove("age");
        assertEquals("", attributes.get("age"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        // test removing an existing attribute
        attributes.put("name", "John");
        attributes.removeIgnoreCase("Name");
        assertEquals("", attributes.get("name"));

        // test removing a non-existing attribute
        attributes.removeIgnoreCase("Age");
        assertEquals("", attributes.get("age"));
    }

    @Test
    public void testHasKey() {
        // test when attributes is null
        assertFalse(attributes.hasKey("name"));

        // test when attribute key not found
        attributes.put("name", "John");
        assertFalse(attributes.hasKey("age"));

        // test when attribute key found
        assertTrue(attributes.hasKey("name"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        // test when attributes is null
        assertFalse(attributes.hasKeyIgnoreCase("name"));

        // test when attribute key not found
        attributes.put("name", "John");
        assertFalse(attributes.hasKeyIgnoreCase("age"));

        // test when attribute key found
        assertTrue(attributes.hasKeyIgnoreCase("NAME"));
    }

    @Test
    public void testSize() {
        // test when attributes is null
        assertEquals(0, attributes.size());

        // test when attributes is not null
        attributes.put("name", "John");
        attributes.put("age", "25");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        // test adding attributes to an empty set
        Attributes incoming = new Attributes();
        incoming.put("name", "John");
        incoming.put("age", "25");
        attributes.addAll(incoming);
        assertEquals("John", attributes.get("name"));
        assertEquals("25", attributes.get("age"));

        // test adding attributes to a non-empty set
        incoming = new Attributes();
        incoming.put("gender", "Male");
        attributes.addAll(incoming);
        assertEquals("John", attributes.get("name"));
        assertEquals("25", attributes.get("age"));
        assertEquals("Male", attributes.get("gender"));
    }

    @Test
    public void testIterator() {
        // test when attributes is null
        assertFalse(attributes.iterator().hasNext());

        // test when attributes is empty
        attributes.put("name", "John");
        assertFalse(attributes.iterator().hasNext());

        // test when attributes has values
        attributes.put("age", "25");
        assertTrue(attributes.iterator().hasNext());
        assertEquals("John", attributes.iterator().next().getKey());
    }

    @Test
    public void testAsList() {
        // test when attributes is null
        assertEquals(0, attributes.asList().size());

        // test when attributes is empty
        attributes.put("name", "John");
        assertEquals(1, attributes.asList().size());
        assertEquals("name", attributes.asList().get(0).getKey());

        // test when attributes has values
        attributes.put("age", "25");
        assertEquals(2, attributes.asList().size());
        assertEquals("name", attributes.asList().get(0).getKey());
        assertEquals("age", attributes.asList().get(1).getKey());
    }

    @Test
    public void testDataset() {
        attributes.put("data-name", "John");
        attributes.put("data-age", "25");

        // test getting all custom data attributes
        assertEquals(2, attributes.dataset().size());
        assertEquals("John", attributes.dataset().get("name"));
        assertEquals("25", attributes.dataset().get("age"));

        // test putting a new custom data attribute
        attributes.dataset().put("city", "New York");
        assertEquals(3, attributes.dataset().size());
        assertEquals("New York", attributes.get("data-city"));

        // test putting a custom data attribute with existing key
        String oldValue = attributes.dataset().put("city", "Los Angeles");
        assertEquals("New York", oldValue);
        assertEquals("Los Angeles", attributes.dataset().get("city"));

        // test removing a custom data attribute
        oldValue = attributes.dataset().remove("city");
        assertEquals("Los Angeles", oldValue);
        assertFalse(attributes.hasKey("data-city"));
    }

    @Test
    public void testHtml() {
        // test when attributes is null
        assertEquals("", attributes.html());

        // test when attributes is empty
        attributes.put("name", "John");
        assertEquals(" name=\"John\"", attributes.html());

        // test when attributes has values
        attributes.put("age", "25");
        assertEquals(" name=\"John\" age=\"25\"", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes otherAttributes = new Attributes();

        // test when both attributes are null
        assertTrue(attributes.equals(otherAttributes));

        // test when one attribute is null and the other is not
        attributes.put("name", "John");
        assertFalse(attributes.equals(otherAttributes));

        // test when both attributes are not null
        otherAttributes.put("name", "John");
        assertTrue(attributes.equals(otherAttributes));

        // test when attributes have different values
        otherAttributes.put("name", "Jane");
        assertFalse(attributes.equals(otherAttributes));
    }

    @Test
    public void testHashCode() {
        Attributes otherAttributes = new Attributes();

        // test when both attributes are null
        assertEquals(attributes.hashCode(), otherAttributes.hashCode());

        // test when one attribute is null and the other is not
        attributes.put("name", "John");
        assertNotEquals(attributes.hashCode(), otherAttributes.hashCode());

        // test when both attributes are not null
        otherAttributes.put("name", "John");
        assertEquals(attributes.hashCode(), otherAttributes.hashCode());

        // test when attributes have different values
        otherAttributes.put("name", "Jane");
        assertNotEquals(attributes.hashCode(), otherAttributes.hashCode());
    }

    @Test
    public void testClone() {
        // test cloning an empty attributes
        Attributes clone = attributes.clone();
        assertEquals(attributes, clone);
        assertNotSame(attributes, clone);

        // test cloning attributes with values
        attributes.put("name", "John");
        clone = attributes.clone();
        assertEquals(attributes, clone);
        assertNotSame(attributes, clone);

        // test modifying the cloned attributes
        clone.put("age", "25");
        assertNotEquals(attributes, clone);
        assertFalse(attributes.hasKey("age"));
        assertTrue(clone.hasKey("age"));
    }
}
