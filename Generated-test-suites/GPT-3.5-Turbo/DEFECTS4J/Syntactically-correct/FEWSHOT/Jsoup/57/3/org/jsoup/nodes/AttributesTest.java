package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setup() {
        attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "30");
        attributes.put("gender", "Male");
    }

    @Test
    public void testGet() {
        assertEquals("John", attributes.get("name"));
        assertEquals("30", attributes.get("age"));
        assertEquals("Male", attributes.get("gender"));
        assertEquals("", attributes.get("address"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("John", attributes.getIgnoreCase("name"));
        assertEquals("30", attributes.getIgnoreCase("age"));
        assertEquals("Male", attributes.getIgnoreCase("gender"));
        assertEquals("", attributes.getIgnoreCase("address"));
        assertEquals("John", attributes.getIgnoreCase("NAME"));
        assertEquals("30", attributes.getIgnoreCase("AGE"));
        assertEquals("Male", attributes.getIgnoreCase("GENDER"));
        assertEquals("", attributes.getIgnoreCase("ADDRESS"));
    }

    @Test
    public void testPut() {
        attributes.put("address", "123 Street");
        assertEquals("123 Street", attributes.get("address"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("isVerified", true);
        assertTrue(attributes.hasKey("isVerified"));
        assertEquals("", attributes.get("isVerified"));
        
        attributes.put("isVerified", false);
        assertFalse(attributes.hasKey("isVerified"));
        assertEquals("", attributes.get("isVerified"));
    }

    @Test
    public void testRemove() {
        assertTrue(attributes.hasKey("name"));
        attributes.remove("name");
        assertFalse(attributes.hasKey("name"));
        assertEquals("", attributes.get("name"));

        assertFalse(attributes.hasKey("address"));
        attributes.remove("address");
        assertFalse(attributes.hasKey("address"));
        assertEquals("", attributes.get("address"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        assertTrue(attributes.hasKey("name"));
        assertTrue(attributes.hasKey("NAME"));
        attributes.removeIgnoreCase("name");
        assertFalse(attributes.hasKey("name"));
        assertFalse(attributes.hasKey("NAME"));
        assertEquals("", attributes.get("name"));

        assertFalse(attributes.hasKey("address"));
        assertFalse(attributes.hasKey("ADDRESS"));
        attributes.removeIgnoreCase("address");
        assertFalse(attributes.hasKey("address"));
        assertFalse(attributes.hasKey("ADDRESS"));
        assertEquals("", attributes.get("address"));
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("name"));
        assertTrue(attributes.hasKey("age"));
        assertTrue(attributes.hasKey("gender"));
        assertFalse(attributes.hasKey("address"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKey("name"));
        assertTrue(attributes.hasKey("NAME"));
        assertTrue(attributes.hasKey("age"));
        assertTrue(attributes.hasKey("AGE"));
        assertTrue(attributes.hasKey("gender"));
        assertTrue(attributes.hasKey("GENDER"));
        assertFalse(attributes.hasKey("address"));
        assertFalse(attributes.hasKey("ADDRESS"));
    }

    @Test
    public void testSize() {
        assertEquals(3, attributes.size());
        attributes.put("address", "123 Street");
        assertEquals(4, attributes.size());
        attributes.remove("age");
        assertEquals(3, attributes.size());
        attributes.remove("address");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("address", "123 Street");
        newAttributes.put("phoneNumber", "1234567890");

        attributes.addAll(newAttributes);

        assertEquals(5, attributes.size());
        assertEquals("123 Street", attributes.get("address"));
        assertEquals("1234567890", attributes.get("phoneNumber"));
    }

    @Test
    public void testAsList() {
        attributes.put("address", "123 Street");
        attributes.put("phoneNumber", "1234567890");
        attributes.put("email", "test@test.com");

        assertEquals(6, attributes.asList().size());
        assertEquals("name=John", attributes.asList().get(0).toString());
        assertEquals("age=30", attributes.asList().get(1).toString());
        assertEquals("gender=Male", attributes.asList().get(2).toString());
        assertEquals("address=123 Street", attributes.asList().get(3).toString());
        assertEquals("phoneNumber=1234567890", attributes.asList().get(4).toString());
        assertEquals("email=test@test.com", attributes.asList().get(5).toString());
    }

    @Test
    public void testDataset() {
        attributes.put("data-id", "1");
        attributes.put("data-name", "John");
        attributes.put("data-age", "30");

        assertEquals(3, attributes.dataset().size());
        assertEquals("1", attributes.dataset().get("id"));
        assertEquals("John", attributes.dataset().get("name"));
        assertEquals("30", attributes.dataset().get("age"));
    }

    @Test
    public void testHtml() {
        attributes.put("class", "person");
        attributes.put("id", "123");
        
        assertEquals(" name=\"John\"", attributes.html());
        assertEquals(" name=\"John\" age=\"30\"", attributes.html());
        assertEquals(" name=\"John\" age=\"30\" gender=\"Male\"", attributes.html());
        assertEquals(" name=\"John\" age=\"30\" gender=\"Male\" class=\"person\"", attributes.html());
        assertEquals(" name=\"John\" age=\"30\" gender=\"Male\" class=\"person\" id=\"123\"", attributes.html());
    }

    @Test
    public void testToString() {
        attributes.put("class", "person");
        attributes.put("id", "123");
        
        assertEquals(" name=\"John\"", attributes.toString());
        assertEquals(" name=\"John\" age=\"30\"", attributes.toString());
        assertEquals(" name=\"John\" age=\"30\" gender=\"Male\"", attributes.toString());
        assertEquals(" name=\"John\" age=\"30\" gender=\"Male\" class=\"person\"", attributes.toString());
        assertEquals(" name=\"John\" age=\"30\" gender=\"Male\" class=\"person\" id=\"123\"", attributes.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes2 = new Attributes();
        attributes2.put("name", "John");
        attributes2.put("age", "30");
        attributes2.put("gender", "Male");

        assertTrue(attributes.equals(attributes2));

        attributes2.put("address", "123 Street");
        assertFalse(attributes.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        Attributes attributes2 = new Attributes();
        attributes2.put("name", "John");
        attributes2.put("age", "30");
        attributes2.put("gender", "Male");

        assertEquals(attributes.hashCode(), attributes2.hashCode());

        attributes2.put("address", "123 Street");
        assertNotEquals(attributes.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        attributes.put("class", "person");
        attributes.put("id", "123");

        Attributes clonedAttributes = attributes.clone();

        assertEquals(attributes, clonedAttributes);
        assertFalse(attributes == clonedAttributes);
    }
}