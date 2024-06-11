package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonFilter;
import com.fasterxml.jackson.databind.json.SerializableString;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.Converter;

/**
 * Test cases for {@link AnyGetterWriter} class
 */
public class AnyGetterWriterTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @SuppressWarnings("unchecked")
    @Test
    public void testAnyGetterWriter() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = new LinkedHashMap<>();
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonGenerator gen = mock(JsonGenerator.class);

        when(provider.getSerializationConfig()).thenReturn(new SerializationConfig());
        when(provider.findValueSerializer(Map.class)).thenReturn(mapSerializer);

        writer.resolve(provider);

        ObjectWriter objectWriter = MAPPER.writerWithRootValue(bean);
        ObjectWriter anyWriter = objectWriter.withSerializerFactory(new CustomSerializerFactory());

        anyWriter.writeValue(System.out, bean);
    }

    @Test
    public void testGetAndSerializeNullValue() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = new Object();
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonGenerator gen = mock(JsonGenerator.class);

        when(provider.getSerializationConfig()).thenReturn(new SerializationConfig());

        writer.getAndSerialize(bean, gen, provider);

        assertNull(gen.getOutputBuffer());
    }

    @Test
    public void testGetAndSerializeEmptyMap() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = new LinkedHashMap<>();
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonGenerator gen = mock(JsonGenerator.class);

        when(provider.getSerializationConfig()).thenReturn(new SerializationConfig());
        when(mapSerializer.serializeFields((Map<?, ?>) bean, gen, provider)).thenCallRealMethod();

        writer.getAndSerialize(bean, gen, provider);

        Map<String, Object> expectedMap = new LinkedHashMap<>();
        expectedMap.put("", SerializableString.class.getName());
        expectedMap.put("", SerializableString.class.getName());

        verify(mapSerializer).serializeFields(eq(expectedMap), eq(gen), eq(provider));
    }

    @Test
    public void testGetAndSerializeNonMapValue() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = new Object();
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonGenerator gen = mock(JsonGenerator.class);

        when(provider.getSerializationConfig()).thenReturn(new SerializationConfig());

        Object value = new Object();
        when(_accessor.getValue(bean)).thenReturn(value);

        assertThrows(JsonMappingException.class, () -> writer.getAndSerialize(bean, gen, provider));
    }

    @Test
    public void testGetAndFilterNullValue() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = new Object();
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        when(provider.getSerializationConfig()).thenReturn(new SerializationConfig());

        writer.getAndFilter(bean, gen, provider, filter);

        assertNull(gen.getOutputBuffer());
    }

    @Test
    public void testGetAndFilterEmptyMap() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = new LinkedHashMap<>();
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        when(provider.getSerializationConfig()).thenReturn(new SerializationConfig());
        when(mapSerializer.serializeFilteredFields((Map<?, ?>) bean, gen, provider, filter, null))
                .thenCallRealMethod();

        writer.getAndFilter(bean, gen, provider, filter);

        Map<String, Object> expectedMap = new LinkedHashMap<>();
        expectedMap.put("", SerializableString.class.getName());
        expectedMap.put("", SerializableString.class.getName());

        verify(mapSerializer).serializeFilteredFields(eq(expectedMap), eq(gen), eq(provider), eq(filter), null);
    }

    @Test
    public void testGetAndFilterNonMapValue() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = new Object();
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        when(provider.getSerializationConfig()).thenReturn(new SerializationConfig());

        Object value = new Object();
        when(_accessor.getValue(bean)).thenReturn(value);

        assertThrows(JsonMappingException.class, () -> writer.getAndFilter(bean, gen, provider, filter));
    }

    @Test
    public void testResolveNullSerializers() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        SerializerProvider provider = mock(SerializerProvider.class);

        assertThrows(JsonMappingException.class, () -> writer.resolve(provider));
    }

    @Test
    public void testResolveWithNullProperty() throws Exception {
        BeanProperty property = null;
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        SerializerProvider provider = mock(SerializerProvider.class);

        writer.resolve(provider);

        assertNull(writer._mapSerializer);
    }

    @Test
    public void testResolveWithSerializers() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        SerializerProvider provider = mock(SerializerProvider.class);
        MapSerializer expectedSerializer = mock(MapSerializer.class);

        when(provider.handlePrimaryContextualization(eq(mapSerializer), any(BeanProperty.class)))
                .thenReturn(expectedSerializer);

        writer.resolve(provider);

        assertEquals(expectedSerializer, writer._mapSerializer);
    }

    private static class CustomSerializerFactory extends SimpleSerializerProvider.BaseSerializerFactory {
        @Override
        public JsonSerializer<Map<String, Object>> findValueSerializer(Class<?> type) {
            if (Map.class.isAssignableFrom(type)) {
                return new MapSerializer(new ObjectMapper().getFactory());
            }
            return super.findValueSerializer(type);
        }

        @Override
        public JsonSerializer<Map<String, Object>> findValueSerializer(Class<?> type,
                JsonSerializer<Object> serializer) {
            if (Map.class.isAssignableFrom(type)) {
                return new MapSerializer(new ObjectMapper().getFactory());
            }
            return super.findValueSerializer(type, serializer);
        }

        @Override
        public JsonSerializer<Map<String, Object>> findValueSerializer(Class<?> type,
                JsonSerializer<Map<String, Object>> serializer) {
            if (Map.class.isAssignableFrom(type)) {
                return serializer;
            }
            return super.findValueSerializer(type, serializer);
        }

        @Override
        public JsonSerializer<Map<String, Object>> findValueSerializer(Class<?> type,
                JsonSerializer<Object> serializer, Converter<Object, Map<String, Object>> converter) {
            if (Map.class.isAssignableFrom(type)) {
                return new MapSerializer(new ObjectMapper().getFactory());
            }
            return super.findValueSerializer(type, serializer, converter);
        }
    }
}