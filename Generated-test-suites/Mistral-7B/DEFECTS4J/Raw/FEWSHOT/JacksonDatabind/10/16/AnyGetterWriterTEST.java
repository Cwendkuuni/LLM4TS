package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.fasterxml.jackson.databind.ser.TestBase;

/**
 * Test cases for {@link AnyGetterWriter} class
 */
public class AnyGetterWriterTest extends TestBase {

    @SuppressWarnings("unchecked")
    private AnyGetterWriter createAnyGetterWriter(String propertyName, Class<?> mapValueType) {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mapper.findProperty("test", TestBean.class, propertyName);
        AnnotatedMember accessor = mapper.findMember(TestBean.class, propertyName);
        MapSerializer mapSerializer = mapper.findValueSerializer(Object.class, Map.class);
        return new AnyGetterWriter(property, accessor, mapSerializer);
    }

    @Test
    public void testGetAndSerializeNullBean() throws Exception {
        Object bean = null;
        AnyGetterWriter writer = createAnyGetterWriter("anyGetter", String.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        writer.getAndSerialize(bean, gen, provider);
        // no exception expected
    }

    @Test
    public void testGetAndSerializeNullValue() throws Exception {
        Object bean = new TestBean();
        AnyGetterWriter writer = createAnyGetterWriter("anyGetter", Map.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        when(writer._accessor.getValue(bean)).thenReturn(null);
        writer.getAndSerialize(bean, gen, provider);
        // no output expected
    }

    @Test
    public void testGetAndSerializeEmptyMap() throws Exception {
        Object bean = new TestBean();
        AnyGetterWriter writer = createAnyGetterWriter("anyGetter", Map.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Map<String, String> map = new HashMap<>();
        when(writer._accessor.getValue(bean)).thenReturn(map);
        writer.getAndSerialize(bean, gen, provider);
        verify(gen).writeStartObject();
        verify(gen).writeEndObject();
    }

    @Test
    public void testGetAndSerializeMap() throws Exception {
        Object bean = new TestBean();
        AnyGetterWriter writer = createAnyGetterWriter("anyGetter", Map.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        when(writer._accessor.getValue(bean)).thenReturn(map);
        writer.getAndSerialize(bean, gen, provider);
        verify(gen).writeStartObject();
        verify(gen).writeFieldName("key1");
        verify(gen).writeStringValue("value1");
        verify(gen).writeFieldName("key2");
        verify(gen).writeStringValue("value2");
        verify(gen).writeEndObject();
    }

    @Test
    public void testGetAndFilterNullBean() throws Exception {
        Object bean = null;
        AnyGetterWriter writer = createAnyGetterWriter("anyGetter", String.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        writer.getAndFilter(bean, gen, provider, filter);
        // no exception expected
    }

    @Test
    public void testGetAndFilterNullValue() throws Exception {
        Object bean = new TestBean();
        AnyGetterWriter writer = createAnyGetterWriter("anyGetter", Map.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        when(writer._accessor.getValue(bean)).thenReturn(null);
        writer.getAndFilter(bean, gen, provider, filter);
        // no output expected
    }

    @Test
    public void testGetAndFilterEmptyMap() throws Exception {
        Object bean = new TestBean();
        AnyGetterWriter writer = createAnyGetterWriter("anyGetter", Map.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        Map<String, String> map = new HashMap<>();
        when(writer._accessor.getValue(bean)).thenReturn(map);
        writer.getAndFilter(bean, gen, provider, filter);
        // no output expected
    }

    @Test
    public void testGetAndFilterMapWithFilteredFields() throws Exception {
        Object bean = new TestBean();
        AnyGetterWriter writer = createAnyGetterWriter("anyGetter", Map.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        when(writer._accessor.getValue(bean)).thenReturn(map);
        when(filter.includeProperty("key1")).thenReturn(true);
        writer.getAndFilter(bean, gen, provider, filter);
        verify(gen).writeStartObject();
        verify(gen).writeFieldName("key1");
        verify(gen).writeStringValue("value1");
        verify(gen).writeEndObject();
    }

    @Test
    public void testResolveNullProvider() throws Exception {
        SerializerProvider provider = null;
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        writer.resolve(provider);
        // no exception expected
    }

    @Test
    public void testResolveNullProperty() throws Exception {
        SerializerProvider provider = mock(SerializerProvider.class);
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        writer.resolve(provider);
        // no exception expected
    }

    @Test
    public void testGetAndSerializeWithNullSerializer() throws Exception {
        Object bean = new TestBean();
        AnyGetterWriter writer = createAnyGetterWriter("anyGetter", Map.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        writer._mapSerializer = null;
        writer.getAndSerialize(bean, gen, provider);
        assertThrows(JsonMappingException.class, () -> writer.getAndSerialize(bean, gen, provider));
    }

    @Test
    public void testGetAndFilterWithNullSerializer() throws Exception {
        Object bean = new TestBean();
        AnyGetterWriter writer = createAnyGetterWriter("anyGetter", Map.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        writer._mapSerializer = null;
        writer.getAndFilter(bean, gen, provider, filter);
        assertThrows(JsonMappingException.class, () -> writer.getAndFilter(bean, gen, provider, filter));
    }

    @Test
    public void testGetAndSerializeWithInvalidValueType() throws Exception {
        Object bean = new TestBean();
        AnyGetterWriter writer = createAnyGetterWriter("anyGetter", Integer.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        writer.getAndSerialize(bean, gen, provider);
        assertThrows(JsonMappingException.class, () -> writer.getAndSerialize(bean, gen, provider));
    }

    public static class TestBean {
        // no relevant code here
    }
}