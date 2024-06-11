package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;
import java.util.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

    private static class TestProperty extends SettableBeanProperty {

        TestProperty(String name) {
            super(new com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition() {
                @Override
                public String getName() {
                    return name;
                }

                @Override
                public com.fasterxml.jackson.databind.AnnotationIntrospector.AnnotatedMember getAccessor() {
                    return null;
                }

                @Override
                public com.fasterxml.jackson.databind.AnnotationIntrospector.AnnotatedMember getMutator() {
                    return null;
                }

                @Override
                public com.fasterxml.jackson.databind.util.Annotations getAnnotations() {
                    return null;
                }

                @Override
                public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> aClass) {
                    return null;
                }

                @Override
                public <A extends java.lang.annotation.Annotation> A getContextAnnotation(Class<A> aClass) {
                    return null;
                }
            }, null, null, null, null, true, null, false);
        }

        @Override
        protected SettableBeanProperty withName(String s) {
            return null;
        }

        @Override
        protected SettableBeanProperty withValueDeserializer(JsonDeserializer<?> jsonDeserializer) {
            return null;
        }

        @Override
        protected SettableBeanProperty withNullProvider(NullValueProvider nullValueProvider) {
            return null;
        }

        @Override
        protected SettableBeanProperty withValue(Object o) {
            return null;
        }

        @Override
        protected SettableBeanProperty withViews(Class<?>... classes) {
            return null;
        }

        @Override
        public void deserializeAndSet(JsonParser jsonParser, DeserializationContext deserializationContext, Object o) throws IOException {

        }

        @Override
        public void set(Object o, Object o1) throws IOException {

        }
    }

    private ExternalTypeHandler externalTypeHandler;

    @Before
    public void setUp() {
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<String, Integer>();
        TestProperty property = new TestProperty("name");
        properties[0] = new ExtTypedProperty(property, null);
        nameToPropertyIndex.put("name", 0);

        externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, null, null);
    }

    @Test
    public void testStart() {
        ExternalTypeHandler newExternalTypeHandler = externalTypeHandler.start();
        assertNotNull(newExternalTypeHandler);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        String propName = "name";
        Object bean = new Object();
        boolean result = externalTypeHandler.handleTypePropertyValue(jsonParser, deserializationContext, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        String propName = "name";
        Object bean = new Object();
        boolean result = externalTypeHandler.handlePropertyValue(jsonParser, deserializationContext, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testComplete() throws IOException {
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        Object bean = new Object();
        Object result = externalTypeHandler.complete(jsonParser, deserializationContext, bean);
        assertEquals(bean, result);
    }

    @Test
    public void testCompleteWithBufferAndCreator() throws IOException {
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        PropertyValueBuffer buffer = new PropertyValueBuffer(null);
        PropertyBasedCreator creator = new PropertyBasedCreator(null, null, null);
        Object result = externalTypeHandler.complete(jsonParser, deserializationContext, buffer, creator);
        assertNotNull(result);
    }

}