package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeDeserializerBaseTest {
    
    private TypeDeserializerBase typeDeserializer;
    private ObjectMapper objectMapper;
    private DeserializationContext deserializationContext;
    private TypeIdResolver typeIdResolver;
    private BeanProperty beanProperty;
    
    @Before
    public void setup() {
        JavaType baseType = mock(JavaType.class);
        typeIdResolver = mock(TypeIdResolver.class);
        beanProperty = mock(BeanProperty.class);
        
        typeDeserializer = new TypeDeserializerBase(baseType, typeIdResolver, "type", true, baseType) {
            private static final long serialVersionUID = 1L;
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }
        };
        
        objectMapper = new ObjectMapper();
        deserializationContext = mock(DeserializationContext.class);
        when(deserializationContext.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(true);
    }
    
    @Test
    public void testBaseTypeName() {
        String baseTypeName = "com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase";
        assertEquals(baseTypeName, typeDeserializer.baseTypeName());
    }
    
    @Test
    public void testGetPropertyName() {
        assertEquals("type", typeDeserializer.getPropertyName());
    }
    
    @Test
    public void testGetTypeIdResolver() {
        assertEquals(typeIdResolver, typeDeserializer.getTypeIdResolver());
    }
    
    @Test
    public void testGetDefaultImpl() {
        assertNull(typeDeserializer.getDefaultImpl());
    }
    
    @Test
    public void testBaseType() {
        JavaType baseType = mock(JavaType.class);
        assertEquals(baseType, typeDeserializer.baseType());
    }
    
    @Test
    public void testToString() {
        String expectedToStringResult = "[" + typeDeserializer.getClass().getName()
                + "; base-type:" + typeDeserializer.baseType()
                + "; id-resolver: " + typeIdResolver
                + "]";
        assertEquals(expectedToStringResult, typeDeserializer.toString());
    }
    
    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        Object result = new Object();
        Object typeId = "typeId";
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        
        when(jp.getTypeId()).thenReturn(typeId);
        when(deserializationContext.findContextualValueDeserializer(null, beanProperty)).thenReturn(deser);
        when(deser.deserialize(jp, deserializationContext)).thenReturn(result);
        
        Object deserializedObject = typeDeserializer._deserializeWithNativeTypeId(jp, deserializationContext);
        
        assertEquals(result, deserializedObject);
    }
    
    @Test
    public void testDeserializeWithNativeTypeIdWithNullTypeId() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        
        when(jp.getTypeId()).thenReturn(null);
        when(deserializationContext.findContextualValueDeserializer(null, beanProperty)).thenReturn(deser);
        when(deserializer.deserialize(jp, deserializationContext)).thenReturn(result);
        
        Object result = ClassUtil.rawClass(typeDeserializer.getTypeId());
        Object deserializedObject = typeDeserializer._deserializeWithNativeTypeId(jp, deserializationContext, typeId);
        
        assertEquals(result, deserializedObject);
    }

    @Test
    public void testDeserializeWithNativeTypeIdWithNonNullTypeId() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        Object result = new Object();

        Object typeId = "typeId";
        
        when(jp.getTypeId()).thenReturn(typeId);
        when(deserializationContext.findContextualValueDeserializer(null, beanProperty)).thenReturn(deser);
        when(deser.deserialize(jp, deserializationContext)).thenReturn(result);
        
        Object deserializedObject = typeDeserializer._deserializeWithNativeTypeId(jp, deserializationContext, typeId);
        
        assertEquals(result, deserializedObject);
    }
    
    @Test
    public void testHandleUnknownTypeId() throws IOException {
        String typeId = "typeId";
        String extraDesc = null;
        
        JavaType actual = typeDeserializer._handleUnknownTypeId(deserializationContext, typeId);
        
        assertNull(actual);
    }
    
    @Test
    public void testHandleMissingTypeId() throws IOException {
        String extraDesc = null;
        
        JavaType actual = typeDeserializer._handleMissingTypeId(deserializationContext, extraDesc);
        
        assertNull(actual);
    }
}