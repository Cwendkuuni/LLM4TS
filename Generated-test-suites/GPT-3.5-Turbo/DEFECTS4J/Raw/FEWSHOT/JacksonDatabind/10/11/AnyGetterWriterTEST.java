package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;


public class AnyGetterWriterTest {

    private AnyGetterWriter anyGetterWriter;
    private BeanPropertyAccessor accessor;
    private BeanProperty beanProperty;
    private MapSerializer mapSerializer;

    @Before
    public void setUp() {
        accessor = Mockito.mock(BeanPropertyAccessor.class);
        beanProperty = Mockito.mock(BeanProperty.class);
        mapSerializer = Mockito.mock(MapSerializer.class);
        anyGetterWriter = new AnyGetterWriter(beanProperty, accessor, mapSerializer);
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        Mockito.when(accessor.getValue(Mockito.any())).thenReturn(map);

        anyGetterWriter.getAndSerialize(new Object(), generator, provider);

        Mockito.verify(accessor, Mockito.times(1)).getValue(Mockito.any());
        Mockito.verify(mapSerializer, Mockito.times(1)).serializeFields(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void testGetAndSerializeWhenValueIsNull() throws Exception {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        Mockito.when(accessor.getValue(Mockito.any())).thenReturn(null);

        anyGetterWriter.getAndSerialize(new Object(), generator, provider);

        Mockito.verify(accessor, Mockito.times(1)).getValue(Mockito.any());
        Mockito.verify(mapSerializer, Mockito.never()).serializeFields(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void testGetAndSerializeWhenValueIsNotMap() throws Exception {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        Mockito.when(accessor.getValue(Mockito.any())).thenReturn("not a map");

        try {
            anyGetterWriter.getAndSerialize(new Object(), generator, provider);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertTrue(e.getMessage().contains("Value returned by 'any-getter'"));
        }

        Mockito.verify(accessor, Mockito.times(1)).getValue(Mockito.any());
        Mockito.verify(mapSerializer, Mockito.never()).serializeFields(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void testGetAndFilter() throws Exception {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        PropertyFilter filter = Mockito.mock(PropertyFilter.class);

        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        Mockito.when(accessor.getValue(Mockito.any())).thenReturn(map);

        anyGetterWriter.getAndFilter(new Object(), generator, provider, filter);

        Mockito.verify(accessor, Mockito.times(1)).getValue(Mockito.any());
        Mockito.verify(mapSerializer, Mockito.times(1)).serializeFilteredFields(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.isNull());
    }

    @Test
    public void testGetAndFilterWhenValueIsNull() throws Exception {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        PropertyFilter filter = Mockito.mock(PropertyFilter.class);

        Mockito.when(accessor.getValue(Mockito.any())).thenReturn(null);

        anyGetterWriter.getAndFilter(new Object(), generator, provider, filter);

        Mockito.verify(accessor, Mockito.times(1)).getValue(Mockito.any());
        Mockito.verify(mapSerializer, Mockito.never()).serializeFilteredFields(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.isNull());
    }

    @Test
    public void testGetAndFilterWhenValueIsNotMap() throws Exception {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        PropertyFilter filter = Mockito.mock(PropertyFilter.class);

        Mockito.when(accessor.getValue(Mockito.any())).thenReturn("not a map");

        try {
            anyGetterWriter.getAndFilter(new Object(), generator, provider, filter);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertTrue(e.getMessage().contains("Value returned by 'any-getter'"));
        }

        Mockito.verify(accessor, Mockito.times(1)).getValue(Mockito.any());
        Mockito.verify(mapSerializer, Mockito.never()).serializeFilteredFields(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.isNull());
    }

    @Test
    public void testResolve() throws JsonMappingException {
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        anyGetterWriter.resolve(provider);

        Mockito.verify(provider, Mockito.times(1)).handlePrimaryContextualization(Mockito.any(), Mockito.any());
    }

    @Test
    public void testResolveWhenSerializeIsNull() throws JsonMappingException {
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        anyGetterWriter._mapSerializer = null;

        anyGetterWriter.resolve(provider);

        Mockito.verify(provider, Mockito.never()).handlePrimaryContextualization(Mockito.any(), Mockito.any());
    }
}