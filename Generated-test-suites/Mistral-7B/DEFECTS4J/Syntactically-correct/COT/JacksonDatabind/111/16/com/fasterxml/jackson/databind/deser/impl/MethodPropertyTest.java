package com.example.methodpropertytest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.MethodProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Test;

public class MethodPropertyTest {

    @Test
    public void testMethodPropertyConstructor() throws Exception {
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        MethodProperty methodProperty = new MethodProperty(null, type, typeDeser, contextAnnotations, method);
        assertNotNull(methodProperty);
    }

    @Test
    public void testMethodPropertyWithName() throws Exception {
        MethodProperty methodProperty = new MethodProperty(null, null);
        PropertyName newName = mock(PropertyName.class);
        MethodProperty newMethodProperty = methodProperty.withName(newName);
        assertEquals(newName, newMethodProperty.getName());
    }

    @Test
    public void testMethodPropertyWithValueDeserializer() throws Exception {
        MethodProperty methodProperty = new MethodProperty(null, null, null);
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        MethodProperty newMethodProperty = methodProperty.withValueDeserializer(deserializer);
        assertEquals(deserializer, newMethodProperty.getValueDeserializer());
    }

    @Test
    public void testMethodPropertyWithNullProvider() throws Exception {
        MethodProperty methodProperty = new MethodProperty(null, null, null);
        NullValueProvider nva = mock(NullValueProvider.class);
        MethodProperty newMethodProperty = methodProperty.withNullProvider(nva);
        assertEquals(nva, newMethodProperty.getNullProvider());
    }

    @Test
    public void testFixAccess() throws Exception {
        MethodProperty methodProperty = new MethodProperty(null, null, null);
        methodProperty.fixAccess(null);
        // Cannot directly assert on private methods, so this test case is incomplete
    }

    @Test
    public void testGetAnnotation() throws Exception {
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, mock(AnnotatedMethod.class)) {
            @Override
            public <A extends Annotation> A getAnnotation(Class<A> acls) {
                return mock(A.class);
            }
        };
        assertEquals(mock(Annotation.class), methodProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() throws Exception {
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, mock(AnnotatedMethod.class)) {
            @Override
            public AnnotatedMember getMember() {
                return mock(AnnotatedMember.class);
            }
        };
        assertEquals(mock(AnnotatedMember.class), methodProperty.getMember());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetWithNullInstance() throws Exception {
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, mock(AnnotatedMethod.class));
        Object instance = null;
        methodProperty.deserializeAndSet(mock(JsonParser.class), null, instance);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        Object value = new Object();
        JsonParser parser = mock(JsonParser.class);
        when(parser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getValueAsString()).thenReturn("\"test\"");
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, mock(AnnotatedMethod.class)) {
            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
                assertEquals("test", value);
            }
        };
        Object instance = new Object();
        methodProperty.deserializeAndSet(parser, null, instance);
        assertEquals(value, instance);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithNullValue() throws Exception {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, mock(AnnotatedMethod.class)) {
            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
                fail("Should not reach this point");
            }
        };
        methodProperty.deserializeAndSet(mock(JsonParser.class), null, instance);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithNullNullValueProvider() throws Exception {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, mock(AnnotatedMethod.class)) {
            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
                fail("Should not reach this point");
            }
        };
        methodProperty.deserializeAndSet(mock(JsonParser.class), null, instance, null);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithIoException() throws Exception {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, mock(AnnotatedMethod.class)) {
            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
                throw new IOException();
            }
        };
        methodProperty.deserializeAndSet(mock(JsonParser.class), null, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        Object value = new Object();
        JsonParser parser = mock(JsonParser.class);
        when(parser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getValueAsString()).thenReturn("\"test\"");
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, mock(AnnotatedMethod.class)) {
            @Override
            public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
                assertEquals("test", value);
                return value;
            }
        };
        Object instance = new Object();
        Object result = methodProperty.deserializeSetAndReturn(parser, null, instance);
        assertEquals(value, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnWithNullValue() throws Exception {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, mock(AnnotatedMethod.class)) {
            @Override
            public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
                fail("Should not reach this point");
            }
        };
        methodProperty.deserializeSetAndReturn(mock(JsonParser.class), null, instance);
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnWithNullNullValueProvider() throws Exception {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, mock(AnnotatedMethod.class)) {
            @Override
            public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
                fail("Should not reach this point");
            }
        };
        methodProperty.deserializeSetAndReturn(mock(JsonParser.class), null, instance, null);
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnWithIoException() throws Exception {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, mock(AnnotatedMethod.class)) {
            @Override
            public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
                throw new IOException();
            }
        };
        methodProperty.deserializeSetAndReturn(mock(JsonParser.class), null, instance);
    }

    @Test
    public void testSet() throws Exception {
        Object value = new Object();
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, mock(AnnotatedMethod.class)) {
            @Override
            public void set(Object instance, Object value) throws IOException {
                assertEquals(value, instance);
            }
        };
        methodProperty.set(instance, value);
        assertEquals(value, instance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithNullInstance() throws Exception {
        Object value = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, mock(AnnotatedMethod.class));
        methodProperty.set(null, value);
    }

    @Test
    public void testSetWithNullValue() throws Exception {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, mock(AnnotatedMethod.class));
        methodProperty.set(instance, null);
        assertNull(instance);
    }

    @Test(expected = IOException.class)
    public void testSetWithIoException() throws Exception {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, mock(AnnotatedMethod.class)) {
            @Override
            public void set(Object instance, Object value) throws IOException {
                throw new IOException();
            }
        };
        methodProperty.set(instance, new Object());
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object value = new Object();
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, mock(AnnotatedMethod.class)) {
            @Override
            public Object setAndReturn(Object instance, Object value) throws IOException {
                assertEquals(value, instance);
                return value;
            }
        };
        Object result = methodProperty.setAndReturn(instance, value);
        assertEquals(value, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAndReturnWithNullInstance() throws Exception {
        Object value = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, mock(AnnotatedMethod.class));
        methodProperty.setAndReturn(null, value);
    }

    @Test(expected = IOException.class)
    public void testSetAndReturnWithIoException() throws Exception {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, mock(AnnotatedMethod.class)) {
            @Override
            public Object setAndReturn(Object instance, Object value) throws IOException {
                throw new IOException();
            }
        };
        methodProperty.setAndReturn(instance, new Object());
    }
}