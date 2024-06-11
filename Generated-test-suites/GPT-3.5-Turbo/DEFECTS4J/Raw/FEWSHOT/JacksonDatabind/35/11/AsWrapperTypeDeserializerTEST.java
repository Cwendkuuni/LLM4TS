package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {
    private AsWrapperTypeDeserializer deserializer;
    private JavaType baseType;
    private TypeIdResolver idResolver;
    private String typePropertyName;
    private boolean typeIdVisible;
    private Class<?> defaultImpl;
    
    @Before
    public void setup() {
        baseType = TypeFactory.defaultInstance().constructType(String.class);
        idResolver = new TypeIdResolver() {

            @Override
            public void init(JavaType baseType) {
            }

            @Override
            public String idFromValue(Object value) {
                return null;
            }

            @Override
            public String idFromValueAndType(Object value, Class<?> suggestedType) {
                return null;
            }

            @Override
            public JavaType typeFromId(String id) {
                return null;
            }

            @Override
            public JsonTypeInfo.Id getMechanism() {
                return null;
            }
            
        };
        typePropertyName = "type";
        typeIdVisible = true;
        defaultImpl = Object.class;
        deserializer = new AsWrapperTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(baseType, deserializer.baseType());
        assertEquals(typePropertyName, deserializer.getTypePropertyName());
        assertEquals(typeIdVisible, deserializer.isTypeIdVisible());
    }
    
    @Test
    public void testWithProperty() {
        BeanProperty prop = null;
        TypeDeserializer newDeserializer = deserializer.forProperty(prop);
        assertTrue(newDeserializer instanceof AsWrapperTypeDeserializer);
    }
    
    @Test
    public void testGetTypeInclusion() {
        assertEquals(As.WRAPPER_OBJECT, deserializer.getTypeInclusion());
    }
    
    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        JsonParser jp = new ObjectMapper().getFactory().createParser("{}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Object result = deserializer.deserializeTypedFromObject(jp, ctxt);
        assertNull(result);
    }
    
    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        JsonParser jp = new ObjectMapper().getFactory().createParser("[]");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Object result = deserializer.deserializeTypedFromArray(jp, ctxt);
        assertNull(result);
    }
    
    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        JsonParser jp = new ObjectMapper().getFactory().createParser("'test'");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Object result = deserializer.deserializeTypedFromScalar(jp, ctxt);
        assertNull(result);
    }
    
    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        JsonParser jp = new ObjectMapper().getFactory().createParser("{}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Object result = deserializer.deserializeTypedFromAny(jp, ctxt);
        assertNull(result);
    }
    
    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = new ObjectMapper().getFactory().createParser("{\"type\": \"test\", \"value\": \"testValue\"}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Object value = deserializer._deserialize(p, ctxt);
        assertNotNull(value);
        assertTrue(value instanceof String);
        assertEquals("testValue", value);
    }

}