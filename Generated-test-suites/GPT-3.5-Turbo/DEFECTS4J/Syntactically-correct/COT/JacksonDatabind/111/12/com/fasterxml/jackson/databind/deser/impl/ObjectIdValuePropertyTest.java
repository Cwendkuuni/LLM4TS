
package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty property;

    @Before
    public void setUp() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null, null, null);
        property = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testWithNullValueDeserializer() {
        assertNull(property.withValueDeserializer(null).getValueDeserializer());
    }

    @Test
    public void testWithNonNullValueDeserializer() {
        JsonDeserializer<?> deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        assertSame(deserializer, property.withValueDeserializer(deserializer).getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        assertNull(property.withNullProvider(null).getNullProvider());
    }

    @Test
    public void testWithNonNullProvider() {
        NullValueProvider provider = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException {
                return null;
            }
        };
        assertSame(provider, property.withNullProvider(provider).getNullProvider());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSetWithNullValue() throws IOException {
        JsonParser p = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null);
        assertNull(property.deserializeSetAndReturn(p, ctxt, null));
    }

    @Test
    public void testDeserializeAndSetWithValue() throws IOException {
        JsonParser p = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null);
        Object instance = new Object();
        assertNull(property.deserializeSetAndReturn(p, ctxt, instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithNullSettableBeanProperty() throws IOException {
        property.setAndReturn(new Object(), new Object());
    }

    @Test
    public void testSetAndReturnWithNonNullSettableBeanProperty() throws IOException {
        SettableBeanProperty idProp = new SettableBeanProperty() {
            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
            }

            @Override
            public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
                return null;
            }

            @Override
            public void set(Object instance, Object value) throws IOException {
            }

            @Override
            public Object setAndReturn(Object instance, Object value) throws IOException {
                return null;
            }

            @Override
            public <A extends Annotation> A getAnnotation(Class<A> acls) {
                return null;
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public <A extends Annotation> A getContextAnnotation(Class<A> acls) {
                return null;
            }

            @Override
            public PropertyMetadata getMetadata() {
                return null;
            }

            @Override
            public PropertyName getFullName() {
                return null;
            }

            @Override
            public boolean isRequired() {
                return false;
            }

            @Override
            public boolean isVirtual() {
                return false;
            }

            @Override
            public NullValueProvider getNullValueProvider() {
                return null;
            }

            @Override
            public ObjectIdInfo getObjectIdInfo() {
                return null;
            }

            @Override
            public JsonDeserializer<Object> getValueDeserializer() {
                return null;
            }

            @Override
            public TypeDeserializer getValueTypeDeserializer() {
                return null;
            }

            @Override
            public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser) {
                return null;
            }

            @Override
            public SettableBeanProperty withNullProvider(NullValueProvider nva) {
                return null;
            }

            @Override
            public SettableBeanProperty withName(PropertyName newName) {
                return null;
            }

            @Override
            public SettableBeanProperty withSimpleName(String simpleName) {
                return null;
            }

            @Override
            public SettableBeanProperty withNullOrIsEmptyProvider(NullValueProvider nva) {
                return null;
            }

            @Override
            public SettableBeanProperty withDefaultNullIgnorable(boolean state) {
                return null;
            }

            @Override
            public SettableBeanProperty withType(TypeDeserializer typeDeser) {
                return null;
            }

            @Override
            public void fixAccess(DeserializationConfig config) {
            }
        };

        property._objectIdReader = new ObjectIdReader(null, idProp, null, null);

        Object instance = new Object();
        assertNull(property.setAndReturn(instance, new Object()));
    }
}

