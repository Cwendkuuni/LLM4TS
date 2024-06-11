package com.fasterxml.jackson.databind.deser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonToken;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;

public class SettableAnyPropertyTest {

    public class AnySetterReferring extends Referring {
        private final SettableAnyProperty _parent;
        private final Object _pojo;
        private final String _propName;

        public AnySetterReferring(SettableAnyProperty parent, UnresolvedForwardReference reference, Class<?> type, Object instance, String propName) {
            super(reference, type);
            _parent = parent;
            _pojo = instance;
            _propName = propName;
        }

        @Override
        public void handleResolvedForwardReference(Object id, Object value) throws IOException {
            if (!hasId(id)) {
                throw new IllegalArgumentException("Trying to resolve a forward reference with id [" + id.toString() + "] that wasn't previously registered.");
            }
            _parent.set(_pojo, _propName, value);
        }
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.createParser("{}");
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        settableAnyProperty.deserializeAndSet(jsonParser, deserializationContext, new Object(), "propName");
    }

    @Test
    public void testDeserialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.createParser("\"value\"");
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        Object result = settableAnyProperty.deserialize(jsonParser, deserializationContext);
        assertEquals("value", result);
    }

    @Test
    public void testSet() throws IOException {
        Map<Object, Object> instance = new HashMap<>();
        AnnotatedMember setter = new AnnotatedField(null, null, null, null);
        JavaType type = null;
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = null;
        TypeDeserializer typeDeserializer = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, setter, type, keyDeserializer, valueDeserializer, typeDeserializer);
        settableAnyProperty.set(instance, "propName", "value");
        assertEquals("value", instance.get("propName"));
    }

    @Test
    public void testValueDeserializer() {
        JsonDeserializer<Object> valueDeserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "value";
            }
        };

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, valueDeserializer, null);
        assertTrue(settableAnyProperty.hasValueDeserializer());
    }

    @Test
    public void testGetType() {
        JavaType type = new JavaType(null, null, null, null, null, null, false, null, null);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, type, null, null, null);
        assertEquals(type, settableAnyProperty.getType());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = new DeserializationConfig(null, null, null);
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(null, null, null, null);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, annotatedMethod, null, null, null, null);
        settableAnyProperty.fixAccess(config);
    }

    @Test
    public void testReadResolve() {
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        Object result = settableAnyProperty.readResolve();
        assertEquals(settableAnyProperty, result);
    }

    @Test
    public void testGetProperty() {
        BeanProperty property = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, null, null, null, null, null);
        assertEquals(property, settableAnyProperty.getProperty());
    }

    @Test
    public void testHasValueDeserializer() {
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        assertFalse(settableAnyProperty.hasValueDeserializer());
    }

    @Test
    public void testGetterAndSetter() {
        AnnotatedMember setter = new AnnotatedField(null, null, null, null);
        JavaType type = null;
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = null;
        TypeDeserializer typeDeserializer = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, setter, type, keyDeserializer, valueDeserializer, typeDeserializer);

        BeanProperty property = null;
        settableAnyProperty.setProperty(property);
        assertEquals(property, settableAnyProperty.getProperty());

        settableAnyProperty.fixAccess(null);

        ObjectMapper objectMapper = new ObjectMapper();
        settableAnyProperty.withValueDeserializer(objectMapper.getDeserializationContext().findContextualValueDeserializer(type, property));
    }
}