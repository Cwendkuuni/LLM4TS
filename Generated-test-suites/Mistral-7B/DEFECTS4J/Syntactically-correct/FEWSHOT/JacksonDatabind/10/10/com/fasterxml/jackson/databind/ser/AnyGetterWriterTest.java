package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyFilter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class AnyGetterWriterTest {

    @SuppressWarnings("unchecked")
    private AnyGetterWriter createAnyGetterWriter(ObjectMapper mapper, BeanProperty property, AnnotatedMember accessor, MapSerializer mapSerializer) {
        return new AnyGetterWriter(property, accessor, mapSerializer);
    }

    @Test
    public void testGetAndSerializeNullBean() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        AnyGetterWriter writer = createAnyGetterWriter(mapper, property, accessor, mock(MapSerializer.class));

        Object bean = null;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        writer.getAndSerialize(bean, gen, provider);

        assertEquals(0, gen.getOutputSize());
    }

    @Test
    public void testGetAndSerializeEmptyMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);
        AnyGetterWriter writer = createAnyGetterWriter(mapper, property, accessor, mapSerializer);

        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        when(accessor.getValue(bean)).thenReturn(new HashMap<>());

        writer.getAndSerialize(bean, gen, provider);

        Map<String, Object> map = mapper.readValue(gen.getOutputBuffer(), Map.class);
        assertEquals(0, map.size());
    }

    @Test
    public void testGetAndSerializeMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        when(accessor.getValue(bean)).thenReturn(map);

        AnyGetterWriter writer = createAnyGetterWriter(mapper, property, accessor, mapSerializer);

        writer.getAndSerialize(bean, gen, provider);

        Map<String, Object> serializedMap = mapper.readValue(gen.getOutputBuffer(), Map.class);
        assertEquals(map.size(), serializedMap.size());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            assertEquals(key, serializedMap.get(key));
            assertEquals(value.getClass(), serializedMap.get(key).getClass());
        }
    }

    @Test
    public void testGetAndSerializeMapSerializerNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);

        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        when(accessor.getValue(bean)).thenReturn(new HashMap<>());

        AnyGetterWriter writer = createAnyGetterWriter(mapper, property, accessor, null);

        assertThrows(JsonMappingException.class, () -> writer.getAndSerialize(bean, gen, provider));
    }

    @Test
    public void testGetAndFilterNullBean() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        Object bean = null;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        writer.getAndFilter(bean, gen, provider, filter);

        assertEquals(0, gen.getOutputSize());
    }

    @Test
    public void testGetAndFilterEmptyMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        when(accessor.getValue(bean)).thenReturn(new HashMap<>());

        AnyGetterWriter writer = createAnyGetterWriter(mapper, property, accessor, mapSerializer);

        writer.getAndFilter(bean, gen, provider, filter);

        assertEquals(0, gen.getOutputSize());
    }

    @Test
    public void testGetAndFilterMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = spy(new MapSerializer(mapper.getFactory()));

        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        when(accessor.getValue(bean)).thenReturn(map);
        when(mapSerializer.serializeFilteredFields(map, gen, provider, filter, null)).thenReturn(null);

        AnyGetterWriter writer = createAnyGetterWriter(mapper, property, accessor, mapSerializer);

        writer.getAndFilter(bean, gen, provider, filter);

        assertEquals(0, gen.getOutputSize());
    }

    @Test
    public void testGetAndFilterMapSerializerNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);

        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        when(accessor.getValue(bean)).thenReturn(new HashMap<>());

        AnyGetterWriter writer = createAnyGetterWriter(mapper, property, accessor, null);

        assertThrows(JsonMappingException.class, () -> writer.getAndFilter(bean, gen, provider, filter));
    }

    @Test
    public void testResolve() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        AnyGetterWriter writer = createAnyGetterWriter(mapper, property, accessor, mapSerializer);
        SerializerProvider provider = mock(SerializerProvider.class);

        writer.resolve(provider);

        JsonSerializer<Map<String, Object>> serialized = provider.getSerializers().findValueSerializer(Map.class);
        assertEquals(mapSerializer, serialized);
    }
}