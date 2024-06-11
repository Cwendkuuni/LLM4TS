package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest {

    @Test
    public void testGetAndSerializeNullBean() throws Exception {
        AnyGetterWriter writer = new AnyGetterWriter(mock(BeanProperty.class), mock(AnnotatedMember.class), mock(MapSerializer.class));
        Object bean = null;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        writer.getAndSerialize(bean, gen, provider);
        // No exception expected
    }

    @Test
    public void testGetAndSerializeEmptyMap() throws Exception {
        AnyGetterWriter writer = new AnyGetterWriter(mock(BeanProperty.class), mock(AnnotatedMember.class), mock(MapSerializer.class));
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Map<String, Object> emptyMap = new HashMap<>();

        when(_accessor.getValue(bean)).thenReturn(emptyMap);

        writer.getAndSerialize(bean, gen, provider);

        // No output expected
    }

    @Test
    public void testGetAndSerializeMap() throws Exception {
        AnyGetterWriter writer = new AnyGetterWriter(mock(BeanProperty.class), mock(AnnotatedMember.class), mock(MapSerializer.class));
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Object value = new Object();
        Map<String, Object> map = new HashMap<>();
        map.put("key1", value);

        when(_accessor.getValue(bean)).thenReturn(map);
        when(_mapSerializer.serializeFields(map, gen, provider)).thenCallRealMethod();

        writer.getAndSerialize(bean, gen, provider);

        // Map should be serialized
    }

    @Test
    public void testGetAndSerializeWithNullMapSerializer() throws Exception {
        AnyGetterWriter writer = new AnyGetterWriter(mock(BeanProperty.class), mock(AnnotatedMember.class), null);
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Object value = new Object();
        Map<String, Object> map = new HashMap<>();
        map.put("key1", value);

        when(_accessor.getValue(bean)).thenReturn(map);

        assertThrows(JsonMappingException.class, () -> writer.getAndSerialize(bean, gen, provider));
    }

    @Test
    public void testGetAndFilterNullBean() throws Exception {
        AnyGetterWriter writer = new AnyGetterWriter(mock(BeanProperty.class), mock(AnnotatedMember.class), mock(MapSerializer.class));
        Object bean = null;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        writer.getAndFilter(bean, gen, provider, filter);
        // No exception expected
    }

    @Test
    public void testGetAndFilterEmptyMap() throws Exception {
        AnyGetterWriter writer = new AnyGetterWriter(mock(BeanProperty.class), mock(AnnotatedMember.class), mock(MapSerializer.class));
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        Map<String, Object> emptyMap = new HashMap<>();

        when(_accessor.getValue(bean)).thenReturn(emptyMap);

        writer.getAndFilter(bean, gen, provider, filter);
        // No output expected
    }

    @Test
    public void testGetAndFilterMap() throws Exception {
        AnyGetterWriter writer = new AnyGetterWriter(mock(BeanProperty.class), mock(AnnotatedMember.class), mock(MapSerializer.class));
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        Object value = new Object();
        Map<String, Object> map = new HashMap<>();
        map.put("key1", value);

        when(_accessor.getValue(bean)).thenReturn(map);

        writer.getAndFilter(bean, gen, provider, filter);

        // Map should not be serialized if it doesn't meet the filter
    }

    @Test
    public void testResolve() throws Exception {
        AnyGetterWriter writer = new AnyGetterWriter(mock(BeanProperty.class), mock(AnnotatedMember.class), mock(MapSerializer.class));
        SerializerProvider provider = mock(SerializerProvider.class);

        writer.resolve(provider);

        // MapSerializer should be resolved
    }
}