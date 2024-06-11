package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.TestBase;

public class AnyGetterWriterTest extends TestBase {

    @SuppressWarnings("unchecked")
    private AnyGetterWriter getInstanceUnderTest(BeanProperty property, AnnotatedMember accessor) {
        ObjectMapper mapper = new ObjectMapper();
        MapSerializer mapSerializer = mapper.SerializerProvider.findValueSerializer(Map.class, null);
        return new AnyGetterWriter(property, accessor, mapSerializer);
    }

    @Test
    public void testGetAndSerializeNullBean() throws Exception {
        AnyGetterWriter writer = getInstanceUnderTest(null, null);
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        writer.getAndSerialize(bean, gen, provider);
        verifyNoMoreInteractions(gen);
    }

    @Test
    public void testGetAndSerializeEmptyMap() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        when(accessor.getValue(any(Object.class))).thenReturn(new HashMap<>());

        AnyGetterWriter writer = getInstanceUnderTest(property, accessor);
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        writer.getAndSerialize(bean, gen, provider);

        ObjectNode result = mapper.readTree(gen.getOutputBuffer().toString());
        assertEmptyMap(result);
    }

    @Test
    public void testGetAndSerializeMapWithValues() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        when(accessor.getValue(any(Object.class))).thenReturn(new HashMap<String, Object>() {{
            put("key1", "value1");
            put("key2", "value2");
        }});

        AnyGetterWriter writer = getInstanceUnderTest(property, accessor);
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        writer.getAndSerialize(bean, gen, provider);

        ObjectNode result = mapper.readTree(gen.getOutputBuffer().toString());
        assertEquals(2, result.size());
        assertEquals("value1", result.get("key1").asText());
        assertEquals("value2", result.get("key2").asText());
    }

    @Test
    public void testGetAndSerializeNullMap() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        when(accessor.getValue(any(Object.class))).thenReturn(null);

        AnyGetterWriter writer = getInstanceUnderTest(property, accessor);
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        writer.getAndSerialize(bean, gen, provider);
        verifyNoInteractions(gen);
    }

    @Test
    public void testGetAndFilterNullBean() throws Exception {
        AnyGetterWriter writer = getInstanceUnderTest(null, null);
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider
                .class);
        PropertyFilter filter = mock(PropertyFilter.class);

        writer.getAndFilter(bean, gen, provider, filter);
        verifyNoMoreInteractions(gen);
    }

    @Test
    public void testGetAndFilterEmptyMap() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        when(accessor.getValue(any(Object.class))).thenReturn(new HashMap<>());

        AnyGetterWriter writer = getInstanceUnderTest(property, accessor);
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        writer.getAndFilter(bean, gen, provider, filter);
        verify(filter, never()).includeMember("*");
        verify(filter, never()).includeElementValueOnly();
        verifyNoMoreInteractions(gen);
    }

    @Test
    public void testGetAndFilterMapWithValues() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        when(accessor.getValue(any(Object.class))).thenReturn(new HashMap<String, Object>() {{
            put("key1", "value1");
            put("key2", "value2");
        }});

        AnyGetterWriter writer = getInstanceUnderTest(property, accessor);
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        when(filter.includeMember("key1")).thenReturn(true);
        when(filter.includeElementValueOnly()).thenReturn(false);

        writer.getAndFilter(bean, gen, provider, filter);

        ObjectNode result = mapper.readTree(gen.getOutputBuffer().toString());
        assertEquals(1, result.size());
        assertEquals("value1", result.get("key1").asText());
        verify(filter, times(1)).includeMember("key1");
        verifyNoMoreInteractions(filter);
    }

    @Test
    public void testGetAndFilterNullMapValue() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        when(accessor.getValue(any(Object.class))).thenReturn(new HashMap<String, Object>() {{
            put("key1", null);
        }});

        AnyGetterWriter writer = getInstanceUnderTest(property, accessor);
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        writer.getAndFilter(bean, gen, provider, filter);
        verifyNoInteractions(gen);
    }

    @Test
    public void testResolveNullProvider() throws Exception {
        AnyGetterWriter writer = getInstanceUnderTest(null, null);
        SerializerProvider provider = null;

        assertThrows(JsonMappingException.class, () -> writer.resolve(provider));
    }
}