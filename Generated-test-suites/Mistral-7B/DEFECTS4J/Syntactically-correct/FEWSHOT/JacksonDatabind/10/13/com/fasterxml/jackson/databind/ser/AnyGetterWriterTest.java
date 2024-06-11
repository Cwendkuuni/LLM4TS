package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer.StandardSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class AnyGetterWriterTest {

    @SuppressWarnings("unchecked")
    private static final AnnotatedMember accessor = mock(AnnotatedMember.class);
    @SuppressWarnings("unchecked")
    private static final BeanProperty property = mock(BeanProperty.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testConstructor() {
        AnnotatedMember accessorMock = mock(AnnotatedMember.class);
        BeanProperty propertyMock = mock(BeanProperty.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(propertyMock, accessorMock, mapSerializer);

        assertEquals(propertyMock, anyGetterWriter._property);
        assertEquals(accessorMock, anyGetterWriter._accessor);
        assertEquals(mapSerializer, anyGetterWriter._mapSerializer);
    }

    @Test
    public void testGetAndSerializeNullBean() throws IOException, JsonProcessingException {
        Object bean = null;

        SerializedString serialize = new SerializedString();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        when(provider.getSerializerProvider()).thenReturn(provider);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, _mapSerializer);

        anyGetterWriter.getAndSerialize(bean, gen, provider);

        verify(gen, never()).writeStartObject();
        verify(gen, never()).writeEnd();
    }

    @Test
    public void testGetAndSerializeEmptyMap() throws IOException, JsonProcessingException {
        Object bean = new Object();
        Map<String, Object> emptyMap = new HashMap<>();

        SerializedString serialize = new SerializedString();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        when(provider.getSerializerProvider()).thenReturn(provider);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, _mapSerializer);

        anyGetterWriter.getAndSerialize(bean, gen, provider);

        verify(gen).writeStartObject();
        verify(gen).writeEnd();
    }

    @Test
    public void testGetAndSerializeMapWithValues() throws IOException, JsonProcessingException {
        Object bean = new Object();
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        SerializedString serializeValue1 = new SerializedString("value1");
        SerializedString serializeValue2 = new SerializedString("value2");
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        when(provider.getSerializerProvider()).thenReturn(provider);

        AnnotatedMember accessorMock = mock(AnnotatedMember.class);
        when(accessorMock.getValue(bean)).thenReturn(map);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessorMock, _mapSerializer);

        anyGetterWriter.getAndSerialize(bean, gen, provider);

        verify(gen).writeStartObject();
        verify(gen).writeFieldName("key1");
        verify(gen).writeString(serializeValue1.getValue());
        verify(gen).writeFieldName("key2");
        verify(gen).writeString(serializeValue2.getValue());
        verify(gen).writeEnd();
    }

    @Test
    public void testGetAndSerializeNonMapValue() throws IOException, JsonProcessingException {
        Object bean = new Object();
        int value = 1;

        SerializedString serialize = new SerializedString();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        when(provider.getSerializerProvider()).thenReturn(provider);

        AnnotatedMember accessorMock = mock(AnnotatedMember.class);
        when(accessorMock.getValue(bean)).thenReturn(value);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessorMock, _mapSerializer);

        assertThrows(JsonMappingException.class, () -> {
            anyGetterWriter.getAndSerialize(bean, gen, provider);
        });
    }

    @Test
    public void testGetAndFilterNullBean() throws IOException, JsonProcessingException {
        Object bean = null;

        PropertyFilter filter = mock(PropertyFilter.class);

        SerializedString serialize = new SerializedString();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        when(provider.getSerializerProvider()).thenReturn(provider);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, null);

        anyGetterWriter.getAndFilter(bean, gen, provider, filter);

        verify(gen, never()).writeStartObject();
        verify(gen, never()).writeEnd();
    }

    @Test
    public void testGetAndFilterEmptyMap() throws IOException, JsonProcessingException {
        Object bean = new Object();
        Map<String, Object> emptyMap = new HashMap<>();

        PropertyFilter filter = mock(PropertyFilter.class);

        SerializedString serialize = new SerializedString();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        when(provider.getSerializerProvider()).thenReturn(provider);

        AnnotatedMember accessorMock = mock(AnnotatedMember.class);
        when(accessorMock.getValue(bean)).thenReturn(emptyMap);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessorMock, null);

        anyGetterWriter.getAndFilter(bean, gen, provider, filter);

        verify(gen, never()).writeStartObject();
        verify(gen, never()).writeEnd();
    }

    @Test
    public void testGetAndFilterMapWithValues() throws IOException, JsonProcessingException {
        Object bean = new Object();
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        PropertyFilter filter = mock(PropertyFilter.class);
        when(filter.include("key1")).thenReturn(true);
        when(filter.include("key2")).thenReturn(false);

        SerializedString serializeValue1 = new SerializedString("value1");

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        when(provider.getSerializerProvider()).thenReturn(provider);

        AnnotatedMember accessorMock = mock(AnnotatedMember.class);
        when(accessorMock.getValue(bean)).thenReturn(map);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessorMock, null);

        anyGetterWriter.getAndFilter(bean, gen, provider, filter);

        verify(gen).writeStartObject();
        verify(gen).writeFieldName("key1");
        verify(gen).writeString(serializeValue1.getValue());
        verify(gen, never()).writeFieldName("key2");
        verify(gen).writeEnd();
    }

    @Test
    public void testResolveNullProvider() throws JsonMappingException {
        SerializerProvider provider = null;

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, _mapSerializer);

        assertThrows(JsonMappingException.class, () -> {
            anyGetterWriter.resolve(provider);
        });
    }

    @Test
    public void testResolveWithMapSerializer() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);
        when(provider.handlePrimaryContextualization(mapSerializer, property)).thenReturn(mapSerializer);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, _mapSerializer);

        anyGetterWriter.resolve(provider);

        assertEquals(mapSerializer, anyGetterWriter._mapSerializer);
    }

    private static class SerializedString {
        private final String value;

        SerializedString(String value) {
            this.value = value;
        }

        String getValue() {
            return value;
        }
    }

    private static final MapSerializer _mapSerializer = new StandardSerializer<>(Map.class).withContext(ClassUtil.constructContext(MapSerializer.class));
}