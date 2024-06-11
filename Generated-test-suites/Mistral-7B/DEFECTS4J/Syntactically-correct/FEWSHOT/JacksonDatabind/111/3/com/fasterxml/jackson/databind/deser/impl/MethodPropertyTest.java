package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.test.BaseMapTest;

public class MethodPropertyTest extends BaseMapTest {

    @SuppressWarnings("unchecked")
    private static final MethodProperty<Object, Object> createTestMethodProperty(
            Object obj, String propertyName, Class<?> propertyType, Method method) {
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(propertyType);
        AnnotatedMember member = mock(AnnotatedMethod.class);
        when(member.getAnnotations()).thenReturn(new Annotation[0]);
        when(member.getName()).thenReturn(propertyName);
        when(member.getAnnotated()).thenReturn(member);
        when(member.getMember()).thenReturn(member);
        Annotations contextAnnotations = mock(Annotations.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        return new MethodProperty<>(
                new BeanPropertyDefinition(propertyName, propertyType, null, null),
                type, typeDeser, contextAnnotations, (AnnotatedMethod) member);
    }

    @Test
    public void testCreate() {
        Object obj = new Object() {
            public void setFoo(String value) {
                // setter method
            }
        };
        Method method = obj.getClass().getMethod("setFoo", String.class);
        MethodProperty<Object, String> property = new MethodProperty<>(
                null, "foo", String.class, method);
        assertSame(property.getPropertyName(), "foo");
        assertSame(property._setter, method);
        assertNull(property._nullProvider);
        assertNull(property._valueDeserializer);
        assertNull(property._valueTypeDeserializer);
        assertNull(property._annotated);
    }

    @Test
    public void testCreateWithValueDeserializer() {
        Object obj = new Object() {
            public void setFoo(String value) {
                // setter method
            }
        };
        Method method = obj.getClass().getMethod("setFoo", String.class);
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        MethodProperty<Object, String> property = new MethodProperty<>(
                null, "foo", String.class, method, deserializer, null);
        assertSame(property.getPropertyName(), "foo");
        assertSame(property._setter, method);
        assertSame(property._valueDeserializer, deserializer);
        assertNull(property._nullProvider);
        assertNull(property._valueTypeDeserializer);
        assertNull(property._annotated);
    }

    @Test
    public void testCreateWithNullProvider() {
        Object obj = new Object() {
            public void setFoo(String value) {
                // setter method
            }
        };
        Method method = obj.getClass().getMethod("setFoo", String.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        MethodProperty<Object, String> property = new MethodProperty<>(
                null, "foo", String.class, method, null, nva);
        assertSame(property.getPropertyName(), "foo");
        assertSame(property._setter, method);
        assertNull(property._valueDeserializer);
        assertNull(property._valueTypeDeserializer);
        assertSame(property._nullProvider, nva);
        assertNull(property._annotated);
    }

    @Test
    public void testCreateWithTypeDeserializer() {
        Object obj = new Object() {
            public void setFoo(String value) {
                // setter method
            }
        };
        Method method = obj.getClass().getMethod("setFoo", String.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        MethodProperty<Object, String> property = new MethodProperty<>(
                null, "foo", String.class, method, null, null, typeDeserializer);
        assertSame(property.getPropertyName(), "foo");
        assertSame(property._setter, method);
        assertNull(property._valueDeserializer);
        assertSame(property._valueTypeDeserializer, typeDeserializer);
        assertNull(property._nullProvider);
        assertNull(property._annotated);
    }

    @Test
    public void testCreateWithAnnotations() {
        Object obj = new Object() {
            @JsonSetter(value = "bar")
            public void setFoo(String value) {
                // setter method with @JsonSetter annotation
            }
        };
        Method method = obj.getClass().getMethod("setFoo", String.class);
        MethodProperty<Object, String> property = new MethodProperty<Object, String>(
                null, "foo", String.class, method);
        assertSame(property.getPropertyName(), "foo");
        assertSame(property._setter, method);
        assertNull(property._nullProvider);
        assertNull(property._valueDeserializer);
        assertNull(property._valueTypeDeserializer);
        AnnotatedMethod annotated = property._annotated;
        assertSame(annotated.getMember(), property);
        JsonSetter annotation = annotated.getAnnotation(JsonSetter.class);
        assertSame(annotation.value(), "bar");
    }

    @Test
    public void testSetValue() throws IOException {
        Object obj = new Object() {
            private String foo;
            public void setFoo(String value) {
                this.foo = value;
            }
        };
        Method method = obj.getClass().getMethod("setFoo", String.class);
        MethodProperty<Object, String> property = createTestMethodProperty(
                obj, "foo", String.class, method);
        String value = "value";
        property.setValue(obj, value);
        assertEquals(value, obj.foo);
    }

    @Test
    public void testSetValueWithNull() throws IOException {
        Object obj = new Object() {
            private String foo;
            public void setFoo(String value) {
                this.foo = value;
            }
        };
        Method method = obj.getClass().getMethod("setFoo", String.class);
        MethodProperty<Object, String> property = createTestMethodProperty(
                obj, "foo", String.class, method);
        NullValueProvider nva = mock(NullValueProvider.class);
        when(nva.getNullValue(null)).thenReturn(null);
        property._nullProvider = nva;
        property.setValue(obj, null);
        assertNull(obj.foo);
    }

    @Test(expected = IllegalAccessException.class)
    public void testSetValueWithIllegalAccess() throws IOException {
        Object obj = new Object() {
            private String foo;
            public void setFoo(String value) {
                this.foo = value;
            }
        };
        Method method = obj.getClass().getMethod("setFoo", String.class);
        MethodProperty<Object, String> property = createTestMethodProperty(
                obj, "foo", String.class, method);
        when(property._setter.isAccessible()).thenReturn(false);
        String value = "value";
        property.setValue(obj, value);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object obj = new Object() {
            private String foo;
            public void setFoo(String value) {
                this.foo = value;
            }
        };
        Method method = obj.getClass().getMethod("setFoo", String.class);
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        MethodProperty<Object, String> property = createTestMethodProperty(
                obj, "foo", String.class, method, deserializer, null);
        String value = "value";
        JsonParser parser = mock(JsonParser.class);
        when(parser.getValueAsString()).thenReturn(value);
        when(parser.getCurrentName()).thenReturn("foo");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Object instance = new Object();
        when(parser.getCodec()).thenReturn(ObjectMapper.getDefaultInstance());
        property.deserializeAndSet(parser, null, instance);
        assertEquals(value, instance.foo);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object obj = new Object() {
            private String foo;
            public String getFoo() {
                return foo;
            }
        };
        Method method = obj.getClass().getMethod("setFoo", String.class);
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        MethodProperty<Object, String> property = createTestMethodProperty(
                obj, "foo", String.class, method, deserializer, null);
        String value = "value";
        JsonParser parser = mock(JsonParser.class);
        when(parser.getValueAsString()).thenReturn(value);
        when(parser.getCurrentName()).thenReturn("foo");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Object instance = new Object();
        when(parser.getCodec()).thenReturn(ObjectMapper.getDefaultInstance());
        Object result = property.deserializeSetAndReturn(parser, null, instance);
        assertEquals(value, result);
    }

    @Test
    public void testSet() throws IOException {
        Object obj = new Object() {
            private String foo;
            public void setFoo(String value) {
                this.foo = value;
            }
        };
        Method method = obj.getClass().getMethod("setFoo", String.class);
        MethodProperty<Object, String> property = createTestMethodProperty(
                obj, "foo", String.class, method);
        String value = "value";
        property.set(obj, value);
        assertEquals(value, obj.foo);
    }

    @Test
    public void testSetWithNull() throws IOException {
        Object obj = new Object() {
            private String foo;
            public void setFoo(String value) {
                this.foo = value;
            }
        };
        Method method = obj.getClass().getMethod("setFoo", String.class);
        MethodProperty<Object, String> property = createTestMethodProperty(
                obj, "foo", String.class, method);
        NullValueProvider nva = mock(NullValueProvider.class);
        when(nva.getNullValue(null)).thenReturn(null);
        property._nullProvider = nva;
        property.set(obj, null);
        assertNull(obj.foo);
    }

    @Test(expected = IllegalAccessException.class)
    public void testSetWithIllegalAccess() throws IOException {
        Object obj = new Object() {
            private String foo;
            public void setFoo(String value) {
                this.foo = value;
            }
        };
        Method method = obj.getClass().getMethod("setFoo", String.class);
        MethodProperty<Object, String> property = createTestMethodProperty(
                obj, "foo", String.class, method);
        when(property._setter.isAccessible()).thenReturn(false);
        String value = "value";
        property.set(obj, value);
    }
}