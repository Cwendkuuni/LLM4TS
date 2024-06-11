package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.JsonDeserializer.ValueInstantiator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer.NullValueProvider.None;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer.ValueInstantiator.StdValueInstantiator;
import com.fasterxml.jackson.databind.ser.std.StdSerializer.StdSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class MappingIteratorTest {

    private static final Converter<String, Integer> STRING_TO_INT_CONVERTER = new Converter.Standard(String.class, Integer.class) {
        @Override
        public Integer convert(String text) {
            return Integer.parseInt(text);
        }
    };

    private ObjectMapper objectMapper;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        deserializationContext = objectMapper.getDeserializationContext();
        jsonParser = mock(JsonParser.class);
    }

    @Test
    public void testHasNextAndNextWithEmptyIterator() {
        MappingIterator<String> emptyMappingIterator = MappingIterator.emptyIterator();
        assertFalse(emptyMappingIterator.hasNext());
        assertNull(emptyMappingIterator.next());
    }

    @Test
    public void testHasNextAndNextWithInitializedIterator() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(List.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonDeserializer<List<Integer>> listDeserializer = new StdDeserializer<List<Integer>>(List.class) {
            @Override
            public List<Integer> deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                return Collections.emptyList();
            }
        };
        listDeserializer.setValueInstantiator(new StdValueInstantiator());
        listDeserializer.setNullValueProvider(NullValueProvider.getInstance(None.class));
        MappingIterator<Integer> mappingIterator = new MappingIterator<Integer>(jsonParser, javaType, deserializationContext, listDeserializer, false, null);
        assertTrue(mappingIterator.hasNext());
        Integer next = mappingIterator.next();
        assertNull(next);
        assertFalse(mappingMapper.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        MappingIterator<String> mappingIterator = new MappingIterator<String>(jsonParser, null, deserializationContext, null, false, null);
        mappingIterator.remove();
    }

    @Test
    public void testClose() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.isClosed()).thenReturn(false);
        MappingIterator<String> mappingIterator = new MappingIterator<String>(jsonParser, null, deserializationContext, null, true, null);
        mappingIterator.close();
        // jsonParser should be closed
        when(jsonParser.isClosed()).thenReturn(true);
        mappingIterator.close();
    }

    @Test
    public void testHasNextValueAndNextValueWithNullParser() throws Exception {
        MappingIterator<String> mappingIterator = new MappingIterator<String>(null, null, deserializationContext, null, false, null);
        assertFalse(mappingIterator.hasNextValue());
        assertNull(mappingIterator.nextValue());
    }

    @Test
    public void testHasNextValueAndNextValueWithValidParser() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(List.class);
        JsonDeserializer<List<Integer>> listDeserializer = new StdDeserializer<List<Integer>>(List.class) {
            @Override
            public List<Integer> deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                return new ArrayList<>();
            }
        };
        listDeserializer.setValueInstantiator(new StdValueInstantiator());
        listDeserializer.setNullValueProvider(NullValueProvider.getInstance(None.class));
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        MappingIterator<Integer> mappingIterator = new MappingIterator<Integer>(jsonParser, javaType, deserializationContext, listDeserializer, false, null);
        assertTrue(mappingIterator.hasNextValue());
        List<Integer> nextValue = mappingIterator.nextValue();
        assertFalse(mappingIterator.hasNextValue());
        assertNull(mappingIterator.nextValue());
    }

    @Test
    public void testReadAllWithEmptyIterator() throws Exception {
        MappingIterator<String> emptyMappingIterator = MappingIterator.emptyIterator();
        List<String> list = emptyMappingIterator.readAll();
        assertTrue(list.isEmpty());
    }

    @Test
    public void testReadAllWithValidIterator() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(List.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonDeserializer<List<Integer>> listDeserializer = new StdDeserializer<List<Integer>>(List.class) {
            @Override
            public List<Integer> deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                return new ArrayList<>();
            }
        };
        listDeserializer.setValueInstantiator(new StdValueInstantiator<>());
        listDeserializer.setNullValueProvider(NullValueProvider.getInstance(None.class));
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        MappingIterator<Integer> mappingIterator = new MappingIterator<Integer>(jsonParser, javaType, deserializationContext, listDeserializer, false, null);
        List<Integer> list = mappingIterator.readAll();
        assertTrue(list.isEmpty());
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        when(jsonParser.getValueAsInt()).thenReturn(1);
        mappingIterator.nextValue();
        mappingIterator.nextValue();
        list = mappingIterator.readAll();
        List<Integer> expectedList = new ArrayList<>(2);
        expectedList.add(1);
        expectedList.add(1);
        assertSame(expectedList, list);
    }
}