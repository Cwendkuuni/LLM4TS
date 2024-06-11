package com.fasterxml.jackson.databind;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MappingIteratorTest {

    private static class TestObject {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private MappingIterator<TestObject> iterator;

    @Before
    public void setUp() {
        // We will create a custom JavaType for our TestObject
        // but for the purpose of this test, we can ignore the details
        JavaType type = new SimpleType(TestObject.class);
        // Create the ObjectMapper and JsonParser for testing
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("[{\"id\":1, \"name\":\"Alice\"}, {\"id\":2, \"name\":\"Bob\"}]");
        // Create the MappingIterator using the constructor under test
        iterator = new MappingIterator<>(type, jsonParser, objectMapper.getDeserializationContext(),
                objectMapper.deserializerFor(TestObject.class), true, null);
    }

    @Test
    public void testHasNext() {
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext() {
        TestObject firstObject = iterator.next();
        assertEquals(1, firstObject.getId());
        assertEquals("Alice", firstObject.getName());

        TestObject secondObject = iterator.next();
        assertEquals(2, secondObject.getId());
        assertEquals("Bob", secondObject.getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        iterator.remove();
    }

    @Test
    public void testClose() {
        // Close the iterator should close the underlying JsonParser
        // Here we just test that no exception is thrown
        try {
            iterator.close();
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testReadAll() {
        // Read all TestObjects from the iterator into a list
        List<TestObject> result = iterator.readAll();
        // Test that the list size is as expected
        assertEquals(2, result.size());
        // Test the values of the TestObject in the list
        assertEquals(1, result.get(0).getId());
        assertEquals("Alice", result.get(0).getName());
        assertEquals(2, result.get(1).getId());
        assertEquals("Bob", result.get(1).getName());
    }

    @Test
    public void testGetParser() {
        JsonParser parser = iterator.getParser();
        assertNotNull(parser);
    }

    @Test
    public void testGetParserSchema() {
        FormatSchema schema = iterator.getParserSchema();
        assertNull(schema);
    }

    @Test
    public void testGetCurrentLocation() {
        JsonLocation location = iterator.getCurrentLocation();
        assertNotNull(location);
        assertEquals(1, location.getLineNr());
        assertEquals(0, location.getColumnNr());
    }
}