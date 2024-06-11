package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.core.*;
import org.junit.*;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class MappingIteratorTest {

    @Test
    public void testHasNextAndNext() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[[1, 2], [3, 4], [5, 6]]");
        MappingIterator<JsonNode> iterator = mapper.valueIterator(node.elements());

        // Test hasNext and next methods with valid data
        assertTrue(iterator.hasNext());
        JsonNode nextNode = iterator.next();
        assertEquals(JsonNodeType.ARRAY, nextNode.getNodeType());
        assertTrue(iterator.hasNext());
        JsonNode nextNode2 = iterator.next();
        assertEquals(JsonNodeType.ARRAY, nextNode2.getNodeType());
        assertTrue(iterator.hasNext());
        JsonNode nextNode3 = iterator.next();
        assertEquals(JsonNodeType.ARRAY, nextNode3.getNodeType());
        assertFalse(iterator.hasNext());

        // Test hasNext and next methods with empty iterator
        iterator = mapper.valueIterator(JsonNodeFactory.instance.emptyArray());
        assertFalse(iterator.hasNext());

        // Test hasNext and next methods with null iterator
        iterator = null;
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testCloseParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[[1, 2], [3, 4], [5, 6]]");
        MappingIterator<JsonNode> iterator = mapper.valueIterator(node.elements());

        // Test closing parser when managed
        assertTrue(iterator.hasNext());
        iterator.next();
        iterator.close();
        assertTrue(iterator.hasNext()); // Should still have next value, parser is closed but not managed by iterator

        // Test closing parser when not managed
        iterator = mapper.valueIterator(node.elements(), false, null);
        assertTrue(iterator.hasNext());
        iterator.next();
        iterator.close();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testReadAll() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[[1, 2], [3, 4], [5, 6]]");
        List<JsonNode> resultList = new ArrayList<>();
        MappingIterator<JsonNode> iterator = mapper.valueIterator(node.elements());

        iterator.readAll(resultList);

        assertEquals(3, resultList.size());
        for (JsonNode jsonNode : resultList) {
            assertArrayEquals(new int[]{JsonNodeType.ARRAY, JsonNodeType.ARRAY, JsonNodeType.ARRAY}, jsonNode.getNodeTypes());
        }
    }

    @Test
    public void testReadAllCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[[1, 2], [3, 4], [5, 6]]");
        ArrayList<JsonNode> resultList = new ArrayList<>();
        MappingIterator<JsonNode> iterator = mapper.valueIterator(node.elements());

        iterator.readAll(resultList);

        assertEquals(3, resultList.size());
        for (JsonNode jsonNode : resultList) {
            assertArrayEquals(new int[]{JsonNodeType.ARRAY, JsonNodeType.ARRAY, JsonNodeType.ARRAY}, jsonNode.getNodeTypes());
        }

        HashSet<JsonNode> resultSet = new HashSet<>();
        iterator.readAll(resultSet);

        assertEquals(3, resultSet.size());
        for (JsonNode jsonNode : resultSet) {
            assertArrayEquals(new int[]{JsonNodeType.ARRAY, JsonNodeType.ARRAY, JsonNodeType.ARRAY}, jsonNode.getNodeTypes());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[[1, 2], [3, 4], [5, 6]]");
        MappingIterator<JsonNode> iterator = mapper.valueIterator(node.elements());

        iterator.next();
        iterator.remove();
    }
}