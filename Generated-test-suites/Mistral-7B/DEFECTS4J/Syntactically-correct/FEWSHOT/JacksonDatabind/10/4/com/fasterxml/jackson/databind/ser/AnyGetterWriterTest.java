package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyFilter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer.StdSerializer;

/**
 * Test cases for {@link AnyGetterWriter} class
 */
public class AnyGetterWriterTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAndSerialize() throws Exception {
        BeanProperty property = new BeanPropertyDefinition(new AnnotatedClass("TestClass"), "mapProperty")
                .build();
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        when(accessor.getName()).thenReturn("mapProperty");

        MapSerializer mapSerializer = new MapSerializer(MAPPER.getTypeFactory().constructMapType(HashMap.class, String.class, Integer.class), StdSerializer.INSTANCE);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);

        Object testBean = new TestClass();

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        anyGetterWriter.getAndSerialize(testBean, gen, provider);

        Map<String, Integer> map = new HashMap<>();
        map.put("key1", 1);
        map.put("key2", 2);

        when(accessor.getValue(testBean)).thenReturn(map);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValueAsString(map);

        when(gen.writeStartObject()).thenReturn(gen);
        when(gen.writeFieldName("key1")).thenReturn(gen);
        when(gen.writeNumberValue(1)).thenReturn(gen);
        when(gen.writeFieldName("key2")).thenReturn(gen);
        when(gen.writeNumberValue(2)).thenReturn(gen);
        when(gen.writeEndObject()).thenReturn(gen);

        anyGetterWriter.getAndSerialize(testBean, gen, provider);

        // Check that the map was serialized correctly
        String serializedMap = MAPPER.writeValueAsString(gen);
        assertEquals(mapper.writeValueAsString(map), serializedMap);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAndSerializeNullMap() throws Exception {
        BeanProperty property = new BeanPropertyDefinition(new AnnotatedClass("TestClass"), "mapProperty")
                .build();
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        when(accessor.getName()).thenReturn("mapProperty");

        MapSerializer mapSerializer = new MapSerializer(MAPPER.getTypeFactory().constructMapType(HashMap.class, String.class, Integer.class), StdSerializer.INSTANCE);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);

        Object testBean = new TestClass();

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        when(accessor.getValue(testBean)).thenReturn(null);

        anyGetterWriter.getAndSerialize(testBean, gen, provider);

        // The method should not write anything to the JsonGenerator
        verifyNoMoreInteractions(gen);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAndSerializeUnexpectedType() throws Exception {
        BeanProperty property = new BeanPropertyDefinition(new AnnotatedClass("TestClass"), "mapProperty")
                .build();
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        when(accessor.getName()).thenReturn("mapProperty");

        MapSerializer mapSerializer = new MapSerializer(MAPPER.getTypeFactory().constructMapType(HashMap.class, String.class, Integer.class), StdSerializer.INSTANCE);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);

        Object testBean = new TestClass();

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        when(accessor.getValue(testBean)).thenReturn(new Integer(42));

        anyGetterWriter.getAndSerialize(testBean, gen, provider);

        assertThrows(JsonMappingException.class, () -> {
            anyGetterWriter.getAndSerialize(testBean, gen, provider);
        });
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAndFilter() throws Exception {
        BeanProperty property = new BeanPropertyDefinition(new AnnotatedClass("TestClass"), "mapProperty")
                .build();
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        when(accessor.getName()).thenReturn("mapProperty");

        MapSerializer mapSerializer = new MapSerializer(MAPPER.getTypeFactory().constructMapType(HashMap.class, String.class, Integer.class), StdSerializer.INSTANCE);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);

        Object testBean = new TestClass();

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        when(accessor.getValue(testBean)).thenReturn(new HashMap<String, Integer>() {{
            put("key1", 1);
            put("key2", 2);
        }});

        when(gen.writeStartObject()).thenReturn(gen);
        when(gen.writeFieldName("key1")).thenReturn(gen);
        when(gen.writeNumberValue(1)).thenReturn(gen);
        when(gen.writeEndObject()).thenReturn(gen);

        AnyGetterWriter.FilteringWriter filteringWriter = mock(AnyGetterWriter.FilteringWriter.class);
        when(mapSerializer.createFilteringWriter(provider, filter, null)).thenReturn(filteringWriter);

        anyGetterWriter.getAndFilter(testBean, gen, provider, filter);

        // Check that the map was filtered correctly
        String serializedMap = MAPPER.writeValueAsString(gen);
        String expectedSerializedMap = "[{\"key\":\"key1\",\"value\":1},{\"key\":\"key2\",\"value\":2}]";
        assertEquals(expectedSerializedMap, serializedMap);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testResolve() throws Exception {
        BeanProperty property = new BeanPropertyDefinition(new AnnotatedClass("TestClass"), "mapProperty")
                .build();
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        when(accessor.getName()).thenReturn("mapProperty");

        MapSerializer mapSerializer = mock(MapSerializer.class);
        when(mapSerializer.getClass()).thenReturn(MapSerializer.class);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);

        SerializerProvider provider = mock(SerializerProvider.class);

        when(provider.handlePrimaryContextualization(mapSerializer, property)).thenReturn(mapSerializer);

        anyGetterWriter.resolve(provider);

        // Check that the mapSerializer was resolved correctly
        assertEquals(mapSerializer, anyGetterWriter._mapSerializer);
    }

    @Test
    public void testGetAndSerializeNullAccesssor() throws Exception {
        BeanProperty property = new BeanPropertyDefinition(new AnnotatedClass("TestClass"), "mapProperty")
                .build();
        MapSerializer mapSerializer = new MapSerializer(MAPPER.getTypeFactory().constructMapType(HashMap.class, String.class, Integer.class), StdSerializer.INSTANCE);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, null, mapSerializer);

        Object testBean = new TestClass();

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        when(accessor.getValue(testBean)).thenReturn(new HashMap<String, Integer>() {{
            put("key1", 1);
            put("key2", 2);
        }});

        anyGetterWriter.getAndSerialize(testBean, gen, provider);

        assertThrows(NullPointerException.class, () -> {
            anyGetterWriter.getAndSerialize(testBean, gen, provider);
        });
    }

    @Test
    public void testGetAndFilterNullAccesssor() throws Exception {
        BeanProperty property = new BeanPropertyDefinition(new AnnotatedClass("TestClass"), "mapProperty")
                .build();
        MapSerializer mapSerializer = new MapSerializer(MAPPER.getTypeFactory().constructMapType(HashMap.class, String.class, Integer.class), StdSerializer.INSTANCE);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, null, mapSerializer);

        Object testBean = new TestClass();

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        when(accessor.getValue(testBean)).thenReturn(new HashMap<String, Integer>() {{
            put("key1", 1);
            put("key2", 2);
        }});

        anyGetterWriter.getAndFilter(testBean, gen, provider, filter);

        assertThrows(NullPointerException.class, () -> {
            anyGetterWriter.getAndFilter(testBean, gen, provider, filter);
        });
    }

    @Test
    public void testGetAndFilterNullFilter() throws Exception {
        BeanProperty property = new BeanPropertyDefinition(new AnnotatedClass("TestClass"), "mapProperty")
                .build();
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        when(accessor.getName()).thenReturn("mapProperty");

        MapSerializer mapSerializer = new MapSerializer(MAPPER.getTypeFactory().constructMapType(HashMap.class, String.class, Integer.class), StdSerializer.INSTANCE);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);

        Object testBean = new TestClass();

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        when(accessor.getValue(testBean)).thenReturn(new HashMap<String, Integer>() {{
            put("key1", 1);
            put("key2", 2);
        }});

        anyGetterWriter.getAndFilter(testBean, gen, provider, null);

        // The method should not throw an exception
    }

    private static class TestClass {
        @JsonSerialize(using = MapSerializer.class)
        public Map<String, Integer> mapProperty;
    }
}