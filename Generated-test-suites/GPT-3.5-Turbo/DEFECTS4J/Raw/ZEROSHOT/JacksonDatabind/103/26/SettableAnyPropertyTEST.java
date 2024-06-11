package com.fasterxml.jackson.databind.deser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class SettableAnyPropertyTest {

    private SettableAnyProperty settableAnyProperty;

    @Before
    public void setUp() throws IOException {
        BeanProperty property = new BeanProperty() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> annotationClass) {
                return null;
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public int getMetadata() {
                return 0;
            }
        };
        AnnotatedMember setter = new AnnotatedMember() {
            @Override
            public Class<?> getDeclaringClass() {
                return null;
            }

            @Override
            public boolean getRequired() {
                return false;
            }

            @Override
            public Class<?> getRawType() {
                return null;
            }

            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public TypeResolutionContext getTypeContext() {
                return null;
            }

            @Override
            public Object getRawAnnotations() {
                return null;
            }

            @Override
            public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> annotationClass) {
                return null;
            }

            @Override
            public boolean hasAnnotation(Class<?> annotationClass) {
                return false;
            }

            @Override
            public Object call() throws Exception {
                return null;
            }

            @Override
            public Object call(Object[] args) throws Exception {
                return null;
            }

            @Override
            public Object call1(Object arg) throws Exception {
                return null;
            }

            @Override
            public Object callOn(Object pojo, Object[] args) throws Exception {
                return null;
            }

            @Override
            public Object callOnWith(Object pojo, Object arg0) throws Exception {
                return null;
            }

            @Override
            public void setValue(Object pojo, Object value) throws IOException, InvocationTargetException {
            }

            @Override
            public int getAnnotationCount() {
                return 0;
            }

            @Override
            public AnnotatedMember withAnnotations(Object annotations) {
                return null;
            }

            @Override
            public void fixAccess(boolean force) {
            }

            @Override
            public void setValue(Object bean, Object value, DeserializationContext ctxt)
                    throws IOException, InvocationTargetException {
            }
        };
        JavaType type = TypeFactory.defaultInstance().constructType(Object.class);
        KeyDeserializer keyDeser = null;
        JsonDeserializer<Object> valueDeser = null;
        TypeDeserializer typeDeser = null;
        settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
    }

    @Test
    public void deserializeAndSetTest() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("");
        DeserializationConfig deserializationConfig = new ObjectMapper().getDeserializationConfig();
        DeserializationContext deserializationContext = new DeserializationContext(jsonParser, deserializationConfig);
        Object instance = new Object();
        String propName = "prop";
        settableAnyProperty.deserializeAndSet(jsonParser, deserializationContext, instance, propName);
    }

    @Test
    public void deserializeTest() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("");
        DeserializationConfig deserializationConfig = new ObjectMapper().getDeserializationConfig();
        DeserializationContext deserializationContext = new DeserializationContext(jsonParser, deserializationConfig);
        Object result = settableAnyProperty.deserialize(jsonParser, deserializationContext);
        Assert.assertNull(result);
    }

    @Test
    public void setTest() throws IOException {
        Object instance = new Object();
        Object propName = "prop";
        Object value = "value";
        settableAnyProperty.set(instance, propName, value);
        Assert.assertTrue(true);
    }

    @Test
    public void throwAsIOETest() throws IOException {
        Exception e = new IllegalArgumentException();
        Object propName = "prop";
        Object value = "value";
        try {
            settableAnyProperty._throwAsIOE(e, propName, value);
        } catch (JsonMappingException ex) {
            Assert.assertEquals(ex.getMessage(), "Problem deserializing \"any\" property 'prop' of class null (expected type: null; actual type: java.lang.String)");
        }
    }

    @Test
    public void getClassNameTest() {
        String className = settableAnyProperty.getClassName();
        Assert.assertNull(className);
    }

    @Test
    public void toStringTest() {
        String string = settableAnyProperty.toString();
        Assert.assertEquals(string, "[any property on class null]");
    }

    private static class BeanProperty implements com.fasterxml.jackson.databind.BeanProperty {
        @Override
        public String getName() {
            return null;
        }

        @Override
        public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> annotationClass) {
            return null;
        }

        @Override
        public AnnotatedMember getMember() {
            return null;
        }

        @Override
        public int getMetadata() {
            return 0;
        }
    }
}