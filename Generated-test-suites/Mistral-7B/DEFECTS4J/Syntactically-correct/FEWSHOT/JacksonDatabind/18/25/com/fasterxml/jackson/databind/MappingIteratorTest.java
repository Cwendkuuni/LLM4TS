package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class MappingIteratorTest<T> {

    @SuppressWarnings("unchecked")
    @Test
    public void testMappingIterator() {
        JavaType javaType = new SimpleType(Object.class);
        MappingIterator<Object> iterator = new MappingIterator<>(javaType, null, null, null, false, null);
        assertFalse(iterator.hasNext());

        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        JsonDeserializer<Object> deserializer = createMockDeserializer();

        MappingIterator<Object> iterator2 = new MappingIterator<>(javaType, parser, context, deserializer, true, null);

        assertTrue(iterator2.hasNext());
        assertTrue(iterator2.next() instanceof Object);
        assertTrue(iterator2.hasNext());

        // Test hasNextValue()
        assertTrue(iterator2.hasNextValue());
        assertFalse(iterator2.hasNextValue());
        parser.nextToken();
        assertTrue(iterator2.hasNextValue());

        // Test nextValue()
        Object nextValue = iterator2.nextValue();
        assertNotNull(nextValue);
        assertFalse(iterator2.hasNextValue());
        assertEquals(nextValue, iterator2.nextValue());

        // Test readAll()
        List<Object> list = iterator2.readAll();
        assertEquals(2, list.size());
        assertEquals(nextValue, list.get(0));
        assertNull(iterator2.nextValue());

        // Test readAll(ArrayList)
        ArrayList<Object> arrayList = new ArrayList<>();
        iterator2.readAll(arrayList);
        assertEquals(2, arrayList.size());
        assertEquals(nextValue, arrayList.get(0));
        assertNull(iterator2.nextValue());

        // Test readAll(LinkedList)
        LinkedList<Object> linkedList = new LinkedList<>();
        iterator2.readAll(linkedList);
        assertEquals(2, linkedList.size());
        assertEquals(nextValue, linkedList.getFirst());
        assertNull(iterator2.nextValue());

        // Test readAll(HashSet)
        HashSet<Object> hashSet = new HashSet<>();
        iterator2.readAll(hashSet);
        assertEquals(2, hashSet.size());
        assertEquals(nextValue, hashSet.iterator().next());
        assertNull(iterator2.nextValue());

        // Test close()
        iterator2.close();
        assertFalse(iterator2.hasNextValue());
        assertFalse(iterator2.hasNext());
        assertNull(iterator2.nextValue());

        // Test emptyIterator()
        MappingIterator<?> emptyIterator = MappingIterator.emptyIterator();
        assertFalse(emptyIterator.hasNextValue());
        assertFalse(emptyIterator.hasNext());
        assertNull(emptyIterator.nextValue());
    }

    private JsonParser createMockParser() {
        JsonParser parser = mock(JsonParser.class);
        when(parser.isExpectedStartArrayToken()).thenReturn(false);
        when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
        return parser;
    }

    private DeserializationContext createMockContext() {
        DeserializationContext context = mock(DeserializationContext.class);
        return context;
    }

    private JsonDeserializer<Object> createMockDeserializer() {
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        return deserializer;
    }
}