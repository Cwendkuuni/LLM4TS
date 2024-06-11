package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializer;
    private Map<String, JsonDeserializer<Object>> deserializers;
    private JsonDeserializer<Object> defaultImplDeserializer;
    private BeanProperty property;
    private DeserializationContext deserializationContext;
    private JsonParser jsonParser;
    
    @Before
    public void setUp() {
        typeDeserializer = new TypeDeserializerBase(null, null, "", true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        deserializers = new ConcurrentHashMap<String, JsonDeserializer<Object>>(16, 0.75f, 4);
        defaultImplDeserializer = null;
        property = null;
        deserializationContext = new DeserializationContext(null, null, null);
        jsonParser = null;
    }
    
    @Test
    public void testBaseTypeName() {
        assertEquals("java.lang.Object", typeDeserializer.baseTypeName());
    }
    
    @Test
    public void testGetPropertyName() {
        assertEquals("", typeDeserializer.getPropertyName());
    }
    
    @Test    
    public void testGetTypeIdResolver() {
        assertNull(typeDeserializer.getTypeIdResolver());
    }
    
    @Test
    public void testGetDefaultImpl() {
        assertNull(typeDeserializer.getDefaultImpl());
    }
    
    @Test
    public void testToString() {
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null"
            + "; id-resolver: null]";
        
        assertEquals(expected, typeDeserializer.toString());
    }
    
    @Test
    public void testFindDeserializerDefaults() throws IOException {
        String typeId = "typeId";
        
        JsonDeserializer<Object> result = typeDeserializer._findDeserializer(deserializationContext, typeId);
        
        assertNull(result);
        assertTrue(deserializers.isEmpty());
        assertNull(defaultImplDeserializer);
    }
    
    @Test
    public void testFindDeserializerWithTypeId() throws IOException {
        String typeId = "typeId";
        JavaType type = null;
        
        deserializationContext = new DeserializationContext(null, null, new ObjectMapper());
        
        JsonDeserializer<Object> result = typeDeserializer._findDeserializer(deserializationContext, typeId);
        
        assertNull(result);
        assertTrue(deserializers.isEmpty());
        assertNull(defaultImplDeserializer);
    }
    
    @Test
    public void testHandleUnknownTypeId() throws IOException {
        String typeId = "typeId";
        TypeIdResolver idResolver = null;
        JavaType baseType = null;
        
        IllegalArgumentException result = null;
        try {
            typeDeserializer._handleUnknownTypeId(deserializationContext, typeId, idResolver, baseType);
        } catch (IllegalArgumentException e) {
            result = e;
        }
        
        String expectedMessage = "Unknown type id encountered: typeId, known type ids are not statically known";
        assertEquals(expectedMessage, result.getMessage());
    }
}