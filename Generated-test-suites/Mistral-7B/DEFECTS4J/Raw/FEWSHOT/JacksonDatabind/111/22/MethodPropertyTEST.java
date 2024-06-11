package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class MethodPropertyTest {

    private static class TestBean {
        private String property;

        public void setProperty(String value) {
            this.property = value;
        }
    }

    @Test
    public void testMethodProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = new TestBean();

        MethodProperty methodProperty = mapper.findProperty("TestBean", "property");
        assertNotNull(methodProperty);
        assertEquals("property", methodProperty.getName());

        // Test deserializeAndSet
        String input = "\"foo\"";
        JsonParser parser = mapper.getFactory().createParser(input);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        methodProperty.deserializeAndSet(parser, ctxt, bean);
        assertEquals("foo", bean.getProperty());

        // Test deserializeSetAndReturn
        Object result = methodProperty.deserializeSetAndReturn(parser, ctxt, bean);
        assertSame(bean, result);
        assertEquals("foo", bean.getProperty());

        // Test set
        methodProperty.set(bean, "bar");
        assertEquals("bar", bean.getProperty());

        // Test setAndReturn
        result = methodProperty.setAndReturn(bean, "baz");
        assertSame(bean, result);
        assertEquals("baz", bean.getProperty());

        // Test withValueDeserializer
        JsonDeserializer<String> deserializer = mapper.findValueDeserializer(String.class);
        MethodProperty methodPropertyWithDeserializer = new MethodProperty(methodProperty, deserializer, mapper.findNullValueProvider());
        assertEquals(deserializer, methodPropertyWithDeserializer.getValueDeserializer());

        // Test withNullValueProvider
        NullValueProvider nullValueProvider = mapper.findNullValueProvider();
        MethodProperty methodPropertyWithNullValueProvider = new MethodProperty(methodProperty, null, nullValueProvider);
        assertSame(nullValueProvider, methodPropertyWithNullValueProvider.getNullProvider());

        // Test fixAccess
        AnnotatedMember annotatedMember = Annotations.findAnnotation(methodProperty, JsonAutoDetect.Class.VISIBLE_ANNOTATIONS_CLASS);
        methodPropertyWithNullValueProvider.fixAccess(mapper.getDeserializationConfig());
        assertSame(JsonAutoDetect.Visibility.ANY, annotatedMember.getVisibility());

        // Test deserialize
        Object value = mapper.readerForUpdating(bean).readValue(parser);
        assertEquals("foo", value);
        assertEquals("foo", bean.getProperty());

        // Test invoke
        Method setterMethod = TestBean.class.getMethod("setProperty", String.class);
        Object invokeResult = setterMethod.invoke(bean, "qux");
        assertEquals(null, invokeResult);
        assertEquals("qux", bean.getProperty());

        // Test invoke with exception
        try {
            setterMethod.invoke(bean, null);
            fail();
        } catch (UndeclaredThrowableException e) {
            assertEquals(NullPointerException.class, e.getCause().getClass());
        }
    }

    @Test
    public void testMethodPropertySerialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = new TestBean();
        bean.setProperty("initial");

        String input = mapper.writeValueAsString(bean);
        JsonParser parser = mapper.getFactory().createParser(input);
        Object result = mapper.readValue(parser, TestBean.class);
        assertEquals("initial", result.getProperty());
    }

    @Test
    public void testMethodPropertyWithAnnotation() throws IOException {
        @JsonDeserialize(using = JsonDeserializerWithStaticMapper.class)
        static class TestBeanWithAnnotation {
            private String property;

            @JsonProperty("name")
            public void setProperty(String value) {
                this.property = value;
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        TestBeanWithAnnotation bean = new TestBeanWithAnnotation();
        bean.setProperty("initial");

        MethodProperty methodProperty = mapper.findProperty("TestBeanWithAnnotation", "property");
        assertNotNull(methodProperty);
        assertEquals("property", methodProperty.getName());

        // Test deserializeAndSet with annotation present
        String input = "{\"name\":\"foo\"}";
        JsonParser parser = mapper.getFactory().createParser(input);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        methodProperty.deserializeAndSet(parser, ctxt, bean);
        assertEquals("foo", bean.getProperty());

        // Test deserializeSetAndReturn with annotation present
        Object result = methodProperty.deserializeSetAndReturn(parser, ctxt, bean);
        assertSame(bean, result);
        assertEquals("foo", bean.getProperty());

        // Test set with annotation present
        methodProperty.set(bean, "bar");
        assertEquals("bar", bean.getProperty());

        // Test setAndReturn with annotation present
        result = methodProperty.setAndReturn(bean, "baz");
        assertSame(bean, result);
        assertEquals("baz", bean.getProperty());
    }
}