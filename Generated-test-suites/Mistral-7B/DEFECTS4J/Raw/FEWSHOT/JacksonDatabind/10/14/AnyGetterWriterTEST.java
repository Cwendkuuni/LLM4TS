package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.Serializers;
import com.fasterxml.jackson.databind.util.BeanUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AnyGetterWriterTest {

    private ObjectMapper mapper;
    private SerializerProvider provider;
    private BeanProperty property;
    private AnnotatedMember accessor;
    private MapSerializer mapSerializer;
    private AnyGetterWriter writer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mapper = new ObjectMapper();
        provider = mapper.getSerializerProvider();
        property = mock(BeanProperty.class);
        accessor = mock(AnnotatedMember.class);
        mapSerializer = mock(MapSerializer.class);
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        when(accessor.getValue(any(Object.class))).thenReturn(new HashMap<>());
        writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = mock(Object.class);
        JsonGenerator gen = mock(JsonGenerator.class);

        writer.getAndSerialize(bean, gen, provider);

        verify(mapSerializer, times(1)).serializeFields((Map<?,?>) any(), eq(gen), eq(provider));
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeMapIsNull() throws Exception {
        when(accessor.getValue(any(Object.class))).thenReturn(null);
        writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = mock(Object.class);
        JsonGenerator gen = mock(JsonGenerator.class);

        writer.getAndSerialize(bean, gen, provider);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeNonMapType() throws Exception {
        when(accessor.getValue(any(Object.class))).thenReturn(new Object());
        writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = mock(Object.class);
        JsonGenerator gen = mock(JsonGenerator.class);

        writer.getAndSerialize(bean, gen, provider);
    }

    @Test
    public void testResolve() throws Exception {
        when(mapper.findValueSerializer(property)).thenReturn(mapSerializer);
        writer = new AnyGetterWriter(property, accessor, null);

        writer.resolve(provider);

        verify(provider, times(1)).handlePrimaryContextualization(eq(mapSerializer), eq(property));
    }

    @Test
    public void testGetAndFilter() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        when(accessor.getValue(any(Object.class))).thenReturn(map);
        writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = mock(Object.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        writer.getAndFilter(bean, gen, provider, filter);

        // We cannot mock _mapSerializer.serializeFilteredFields, so we just check if it was called
        verify(mapSerializer, times(1)).serializeFilteredFields((Map<?,?>) any(), eq(gen), eq(provider), eq(filter), any());
    }

    @Test
    public void testGetAndFilterNullMap() throws Exception {
        when(accessor.getValue(any(Object.class))).thenReturn(null);
        writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = mock(Object.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        writer.getAndFilter(bean, gen, provider, filter);

        // Nothing to verify since _mapSerializer.serializeFilteredFields was not called
    }

    @Test
    public void testGetAndFilterNullFilter() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        when(accessor.getValue(any(Object.class))).thenReturn(map);
        writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = mock(Object.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        PropertyFilter filter = null;

        writer.getAndFilter(bean, gen, provider, filter);

        // Nothing to verify since _mapSerializer.serializeFilteredFields was not called
    }
}