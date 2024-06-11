package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.TestBase;

public class AnyGetterWriterTest extends TestBase {

    @SuppressWarnings("unchecked")
    private AnyGetterWriter createTestee(String propertyName, Class<? extends Map<?,?>> mapType) {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mapper.findBeanProperty("root", propertyName);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        when(accessor.getName()).thenReturn(propertyName);
        when(accessor.getValue(any(Object.class))).thenReturn(new HashMap<>());
        MapSerializer mapSerializer = new MapSerializer(mapType, null, null, null, null, ObjectMapper.getDefaultInstance());
        return new AnyGetterWriter(property, accessor, mapSerializer);
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        AnyGetterWriter testee = createTestee("anyProperty", HashMap.class);
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = new SerializerProvider();

        testee.getAndSerialize(bean, gen, provider);

        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeEndObject();
    }

    @Test
    public void testGetAndSerialize_ValueIsNull() throws Exception {
        AnyGetterWriter testee = createTestee("anyProperty", HashMap.class);
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = new SerializerProvider();

        testee.getAndSerialize(bean, gen, provider);

        verify(gen, never()).writeStartObject();
        verify(gen, never()).writeEndObject();
    }

    @Test
    public void testGetAndSerialize_ValueIsNotMap() throws Exception {
        AnyGetterWriter testee = createTestee("anyProperty", HashMap.class);
        Object bean = new Object();
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = new SerializerProvider();

        when(testee._accessor.getValue(bean)).thenReturn(value);

        assertThrows(JsonMappingException.class, () -> testee.getAndSerialize(bean, gen, provider));

        verify(gen, never()).writeStartObject();
        verify(gen, never()).writeEndObject();
    }

    @Test
    public void testGetAndFilter() throws Exception {
        AnyGetterWriter testee = createTestee("anyProperty", HashMap.class);
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = new SerializerProvider();
        PropertyFilter filter = mock(PropertyFilter.class);

        testee.getAndFilter(bean, gen, provider, filter);

        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeEndObject();
    }

    @Test
    public void testGetAndFilter_ValueIsNull() throws Exception {
        AnyGetterWriter testee = createTestee("anyProperty", HashMap.class);
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = new SerializerProvider();
        PropertyFilter filter = mock(PropertyFilter.class);

        testee.getAndFilter(bean, gen, provider, filter);

        verify(gen, never()).writeStartObject();
        verify(gen, never()).writeEndObject();
    }

    @Test
    public void testGetAndFilter_ValueIsNotMap() throws Exception {
        AnyGetterWriter testee = createTestee("anyProperty", HashMap.class);
        Object bean = new Object();
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = new SerializerProvider();
        PropertyFilter filter = mock(PropertyFilter.class);

        when(testee._accessor.getValue(bean)).thenReturn(value);

        assertThrows(JsonMappingException.class, () -> testee.getAndFilter(bean, gen, provider, filter));

        verify(gen, never()).writeStartObject();
        verify(gen, never()).writeEndObject();
    }

    @Test
    public void testResolve() throws JsonMappingException {
        AnyGetterWriter testee = new AnyGetterWriter(null, null, null);
        SerializerProvider provider = new SerializerProvider();

        testee.resolve(provider);

        assertTrue(testee._mapSerializer != null);
    }
}