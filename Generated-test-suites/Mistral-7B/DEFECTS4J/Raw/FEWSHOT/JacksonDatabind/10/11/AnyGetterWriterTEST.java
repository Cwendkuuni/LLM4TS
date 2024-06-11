package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

/**
 * Test class for {@link AnyGetterWriter}
 */
public class AnyGetterWriterTest {

    @SuppressWarnings("unchecked")
    private AnyGetterWriter createAnyGetterWriter(Object bean, AnnotatedMember accessor, MapSerializer mapSerializer) {
        return new AnyGetterWriter(mock(BeanProperty.class), accessor, mapSerializer) {
            @Override
            public void resolve(SerializerProvider provider) throws JsonMappingException {
                super.resolve(provider);
            }
        };
    }

    @Test
    public void testGetAndSerializeNullValue() throws Exception {
        Object bean = mock(Object.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        ObjectMapper mapper = new ObjectMapper();
        MapSerializer mapSerializer = mapper.getDefaultSerializerProvider().findValueSerializer(Map.class);

        AnyGetterWriter anyGetterWriter = createAnyGetterWriter(bean, accessor, mapSerializer);
        anyGetterWriter.getAndSerialize(bean, mock(JsonGenerator.class), mapper.getSerializerProvider());

        // no exception throws
    }

    @Test
    public void testGetAndSerializeEmptyMap() throws Exception {
        Object bean = mock(Object.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        ObjectMapper mapper = new ObjectMapper();
        MapSerializer mapSerializer = mapper.getDefaultSerializerProvider().findValueSerializer(Map.class);
        Map<String, String> emptyMap = new HashMap<>();

        when(accessor.getValue(bean)).thenReturn(emptyMap);

        AnyGetterWriter anyGetterWriter = createAnyGetterWriter(bean, accessor, mapSerializer);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mapper.getSerializerProvider();

        anyGetterWriter.getAndSerialize(bean, gen, provider);

        // No exception, and the empty map should be serialized properly
        verify(gen).writeStartObject();
        verify(gen).writeEndObject();
    }

    @Test
    public void testGetAndSerializeValidMap() throws Exception {
        Object bean = mock(Object.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        ObjectMapper mapper = new ObjectMapper();
        MapSerializer mapSerializer = mapper.getDefaultSerializerProvider().findValueSerializer(Map.class);
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        when(accessor.getValue(bean)).thenReturn(map);

        AnyGetterWriter anyGetterWriter = createAnyGetterWriter(bean, accessor, mapSerializer);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mapper.getSerializerProvider();

        anyGetterWriter.getAndSerialize(bean, gen, provider);

        // No exception, and the map should be serialized properly
        verify(gen).writeStartObject();
        verify(gen).writeFieldName("key1");
        verify(gen).writeString("value1");
        verify(gen).writeFieldName("key2");
        verify(gen).writeString("value2");
        verify(gen).writeEndObject();
    }

    @Test
    public void testGetAndSerializeThrowsJsonMappingException() throws Exception {
        Object bean = mock(Object.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        ObjectMapper mapper = new ObjectMapper();
        MapSerializer mapSerializer = mapper.getDefaultSerializerProvider().findValueSerializer(Map.class);
        Object invalidValue = new Object() {
            // empty class
        };

        when(accessor.getValue(bean)).thenReturn(invalidValue);

        AnyGetterWriter anyGetterWriter = createAnyGetterWriter(bean, accessor, mapSerializer);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mapper.getSerializerProvider();

        assertThrows(JsonMappingException.class, () -> anyGetterWriter.getAndSerialize(bean, gen, provider));
    }

    @Test
    public void testGetAndFilterNullValue() throws Exception {
        Object bean = mock(Object.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        ObjectMapper mapper = new ObjectMapper();
        MapSerializer mapSerializer = mapper.getDefaultSerializerProvider().findValueSerializer(Map.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        AnyGetterWriter anyGetterWriter = createAnyGetterWriter(bean, accessor, mapSerializer);

        anyGetterWriter.getAndFilter(bean, mock(JsonGenerator.class), mapper.getSerializerProvider(), filter);

        // no exception throws
    }

    @Test
    public void testGetAndFilterEmptyMap() throws Exception {
        Object bean = mock(Object.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        ObjectMapper mapper = new ObjectMapper();
        MapSerializer mapSerializer = mapper.getDefaultSerializerProvider().findValueSerializer(Map.class);
        Map<String, String> emptyMap = new HashMap<>();
        PropertyFilter filter = spy(filter);

        when(accessor.getValue(bean)).thenReturn(emptyMap);

        AnyGetterWriter anyGetterWriter = createAnyGetterWriter(bean, accessor, mapSerializer);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mapper.getSerializerProvider();

        anyGetterWriter.getAndFilter(bean, gen, provider, filter);

        // No exception, and nothing should be written to the generator
        verifyZeroInteractions(gen);
    }

    @Test
    public void testGetAndFilterValidMap() throws Exception {
        Object bean = mock(Object.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        ObjectMapper mapper = new ObjectMapper();
        MapSerializer mapSerializer = mapper.getDefaultSerializerProvider().findValueSerializer(Map.class);
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        PropertyFilter filter = mock(PropertyFilter.class);

        when(accessor.getValue(bean)).thenReturn(map);

        AnyGetterWriter anyGetterWriter = createAnyGetterWriter(bean, accessor, mapSerializer);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mapper.getSerializerProvider();

        anyGetterWriter.getAndFilter(bean, gen, provider, filter);

        // No exception, and the map should be filtered properly
        verify(gen, times(2)).writeFieldName(eq("key1"));
        verify(gen).writeString("value1");
    }
}