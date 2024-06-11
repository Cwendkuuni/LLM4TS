package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Before;
import org.junit.Test;

public class MappingIteratorTest {

    private JsonParser mockParser;
    private DeserializationContext mockContext;
    private ObjectMapper mockObjectMapper;
    private JsonDeserializer<Object> mockDeserializer;
    private MappingIterator<Object> iterator;

    @Before
    public void setup() {
        mockParser = mock(JsonParser.class);
        mockContext = mock(DeserializationContext.class);
        mockObjectMapper = mock(ObjectMapper.class);
        mockDeserializer = mock(JsonDeserializer.class);

        when(mockParser.isExpectedStartArrayToken()).thenReturn(false);
        when(mockParser.nextToken()).thenCallRealMethod();
        when(mockParser.getCurrentToken()).thenCallRealMethod();
        when(mockParser.clearCurrentToken()).thenCallRealMethod();
        when(mockParser.close()).thenCallRealMethod();

        when(mockObjectMapper.getFactory()).thenReturn(new ObjectMapper.DefaultSerializerProvider());
        when(mockObjectMapper.findValueDeserializer(Object.class)).thenReturn(mockDeserializer);
    }

    @Test
    public void testEmptyIterator() {
        iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNextValue_NoValue() throws IOException {
        iterator = new MappingIterator<>(JsonTypeFactory.instance.constructType(Object.class), mockParser, mockContext, mockDeserializer, false, null);
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testHasNextValue_ValueAvailable() throws IOException {
        Object value = "value";
        when(mockDeserializer.deserialize(mockParser, mockContext)).thenReturn(value);

        iterator = new MappingIterator<>(JsonTypeFactory.instance.constructType(Object.class), mockParser, mockContext, mockDeserializer, false, null);
        assertTrue(iterator.hasNextValue());
        assertEquals(value, iterator.nextValue());
        verify(mockParser).clearCurrentToken();
    }

    @Test
    public void testHasNextValue_ValueAvailableWithUpdatedValue() throws IOException {
        Object value = "value";
        Object updatedValue = new Object();
        when(mockDeserializer.deserialize(mockParser, mockContext, updatedValue)).thenReturn(value);

        iterator = new MappingIterator<>(JsonTypeFactory.instance.constructType(Object.class), mockParser, mockContext, mockDeserializer, false, updatedValue);
        assertTrue(iterator.hasNextValue());
        assertEquals(value, iterator.nextValue());
        verify(mockParser).clearCurrentToken();
        verify(updatedValue).equals(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws IOException {
        iterator = new MappingIterator<>(JsonTypeFactory.instance.constructType(Object.class), mockParser, mockContext, mockDeserializer, false, null);
        iterator.remove();
    }

    @Test
    public void testClose() throws IOException {
        iterator = new MappingIterator<>(JsonTypeFactory.instance.constructType(Object.class), mockParser, mockContext, mockDeserializer, false, null);
        iterator.close();
        verify(mockParser).close();
    }

    @Test
    public void testReadAll() throws IOException {
        List<Object> resultList = new ArrayList<>();
        Object value1 = "value1";
        Object value2 = "value2";

        when(mockDeserializer.deserialize(mockParser, mockContext)).thenReturn(value1);
        when(mockDeserializer.deserialize(mockParser, mockContext)).thenReturn(value2);

        iterator = new MappingIterator<>(JsonTypeFactory.instance.constructType(Object.class), mockParser, mockContext, mockDeserializer, false, null);
        List<Object> list = iterator.readAll();
        assertEquals(2, list.size());
        assertEquals(value1, list.get(0));
        assertEquals(value2, list.get(1));

        verify(mockParser).clearCurrentToken();
        verify(mockParser, times(3)).nextToken();
    }

    @Test
    public void testReadAll_WrappedArray() throws IOException {
        List<Object> resultList = new ArrayList<>();
        Object value1 = new Object();
        Object value2 = new Object();
        Node node = new ArrayNode();
        node.add(value1);
        node.add(value2);

        when(mockParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_OBJECT);
        when(mockParser.nextToken()).thenReturn(value1);
        when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_OBJECT);
        when(mockParser.nextToken()).thenReturn(value2);
        when(mockParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        iterator = new MappingIterator<>(JsonTypeFactory.instance.constructTypeCollectionType(List.class, Object.class), mockParser, mockContext, mockDeserializer, true, null);
        List<Object> list = iterator.readAll();
        assertEquals(2, list.size());
        assertEquals(value1, list.get(0));
        assertEquals(value2, list.get(1));

        verify(mockParser, times(5)).nextToken();
        verify(mockParser).clearCurrentToken();
    }

    @Test
    public void testReadAll_UnwrappedArrays() throws IOException {
        List<Object> resultList = new ArrayList<>();
        Object value1 = new Object();
        Object value2 = new Object();

        when(mockParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockParser.nextToken()).thenReturn(JsonToken.START_ARRAY);

        when(mockDeserializer.deserialize(mockParser, mockContext)).thenReturn(value1);
        when(mockDeserializer.deserialize(mockParser, mockContext)).thenReturn(value2);

        iterator = new MappingIterator<>(JsonTypeFactory.instance.constructTypeCollectionType(List.class, Object.class), mockParser, mockContext, mockDeserializer, false, null);
        List<Object> list = iterator.readAll();
        assertEquals(2, list.size());
        assertEquals(value1, list.get(0));
        assertEquals(value2, list.get(1));

        verify(mockParser, times(5)).nextToken();
        verify(mockParser).clearCurrentToken();
    }
}