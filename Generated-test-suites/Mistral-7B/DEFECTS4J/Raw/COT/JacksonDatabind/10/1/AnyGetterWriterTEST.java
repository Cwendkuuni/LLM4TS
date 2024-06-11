package com.example.junit.tests;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest {

    @Test
    public void testConstructor() {
        BeanProperty property = new BeanProperty(new AnnotatedClass(AnyGetterWriterTest.class), "testProperty", null);
        AnnotatedMember accessor = new AnnotatedField(AnyGetterWriterTest.class, "testProperty");
        MapSerializer serializer = new MapSerializer(new AnnotationIntrospector(), ObjectMapper.getDefaultInstance().getSerializerProvider(), null);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        assertNotNull(anyGetterWriter);
    }

    @Test
    public void testGetAndSerialize_NullValue() {
        Object bean = new Object() {};
        Object result = new AnyGetterWriter(new BeanProperty(null, null, null), new AnnotatedField(AnyGetterWriterTest.class, "testProperty"), null)
                .getAndSerialize(bean, mock(JsonGenerator.class), mock(SerializerProvider.class));
        assertNull(result);
    }

    @Test
    public void testGetAndSerialize_NonMapValue() {
        Object bean = new Object() {
            Object testProperty = new Object() {};
        };
        Object result = new AnyGetterWriter(new BeanProperty(null, null, null), new AnnotatedField(AnyGetterWriterTest.class, "testProperty"), null)
                .getAndSerialize(bean, mock(JsonGenerator.class), mock(SerializerProvider.class));
        assertNull(result);

        try {
            new AnyGetterWriter(new BeanProperty(null, null, null), new AnnotatedField(AnyGetterWriterTest.class, "testProperty"), null)
                    .getAndSerialize(bean, mock(JsonGenerator.class), mock(SerializerProvider.class));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (testProperty()) not java.util.Map but " + Object.class.getName(), e.getMessage());
        }
    }

    @Test
    public void testGetAndSerialize_NullMapper() {
        Object bean = new HashMap<>();
        Object result = new AnyGetterWriter(new BeanProperty(null, null, null), new AnnotatedField(AnyGetterWriterTest.class, "testProperty"), null)
                .getAndSerialize(bean, mock(JsonGenerator.class), mock(SerializerProvider.class));
        assertNull(result);

        try {
            new AnyGetterWriter(new BeanProperty(null, null, null), new AnnotatedField(AnyGetterWriterTest.class, "testProperty"), null)
                    .getAndSerialize(bean, mock(JsonGenerator.class), mock(SerializerProvider.class));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (testProperty()) not java.util.Map but MapSerializer=null", e.getMessage());
        }
    }

    @Test
    public void testGetAndSerialize_EmptyMap() {
        Object bean = new HashMap<>();
        Object result = new AnyGetterWriter(new BeanProperty(null, null, null), new AnnotatedField(AnyGetterWriterTest.class, "testProperty"), new MapSerializer(null, null, null))
                .getAndSerialize(bean, mock(JsonGenerator.class), mock(SerializerProvider.class));
        assertArrayEquals(new byte[0], result);
    }

    @Test
    public void testGetAndSerialize() {
        Object bean = new HashMap<String, Object>() {{
            put("key1", "value1");
            put("key2", "value2");
        }};

        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.writeStartObject()).thenReturn(gen);
        when(gen.writeFieldName("key1")).thenReturn(gen);
        when(gen.writeValue("value1")).thenReturn(gen);
        when(gen.writeFieldName("key2")).thenReturn(gen);
        when(gen.writeValue("value2")).thenReturn(gen);
        when(gen.writeEndObject()).thenReturn(gen);

        SerializerProvider provider = mock(SerializerProvider.class);
        when(provider.getSerializerProvider()).thenReturn(provider);

        Object result = new AnyGetterWriter(new BeanProperty(null, null, null), new AnnotatedField(AnyGetterWriterTest.class, "testProperty"), new MapSerializer(null, null, null))
                .getAndSerialize(bean, gen, provider);

        assertEquals(bean, result);

        verify(gen).writeStartObject();
        verify(gen).writeFieldName("key1");
        verify(gen).writeValue("value1");
        verify(gen).writeFieldName("key2");
        verify(gen).writeValue("value2");
        verify(gen).writeEndObject();
    }

    @Test
    public void testGetAndFilter_NullValue() {
        Object bean = new Object() {};
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty(null, null, null), new AnnotatedField(AnyGetterWriterTest.class, "testProperty"), null);
        anyGetterWriter.getAndFilter(bean, mock(JsonGenerator.class), mock(SerializerProvider.class), null);
    }

    @Test
    public void testGetAndFilter_NonMapValue() {
        Object bean = new Object() {
            Object testProperty = new Object() {};
        };
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty(null, null, null), new AnnotatedField(AnyGetterWriterTest.class, "testProperty"), null);

        try {
            anyGetterWriter.getAndFilter(bean, mock(JsonGenerator.class), mock(SerializerProvider.class), null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (testProperty()) not java.util.Map but " + Object.class.getName(), e.getMessage());
        }
    }

    @Test
    public void testGetAndFilter_NullMapper() {
        Object bean = new HashMap<String, Object>() {{
            put("key1", "value1");
            put("key2", "value2");
        }};
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty(null, null, null), new AnnotatedField(AnyGetterWriterTest.class, "testProperty"), null);

        try {
            anyGetterWriter.getAndFilter(bean, mock(JsonGenerator.class), mock(SerializerProvider.class), null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (testProperty()) not java.util.Map but MapSerializer=null", e.getMessage());
        }
    }

    @Test
    public void testResolve() {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        Object result = new AnyGetterWriter(new BeanProperty(null, null, null), new AnnotatedField(AnyGetterWriterTest.class, "testProperty"), null)
                .resolve(mapper.getSerializerProvider());
        assertNotNull(result);
        assertTrue(result instanceof MapSerializer);
    }
}