package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class TypeDeserializerBaseTest {
    
    private TypeDeserializerBase typeDeserializer;
    private DeserializationContext deserializationContext;
    private JsonParser jsonParser;
    private BeanProperty beanProperty;
    private TypeIdResolver typeIdResolver;
    private JavaType baseType;
    private JsonDeserializer<Object> deserializer;

    @Before
    public void setup() {
        typeIdResolver = mock(TypeIdResolver.class);
        baseType = mock(JavaType.class);
        beanProperty = mock(BeanProperty.class);
        deserializer = mock(JsonDeserializer.class);
        deserializationContext = mock(DeserializationContext.class);
        jsonParser = mock(JsonParser.class);
        typeDeserializer = new TypeDeserializerBaseStub(baseType, typeIdResolver, "", true, null);
    }
    
    @Test
    public void testForProperty() {
        TypeDeserializerBase actual = typeDeserializer.forProperty(beanProperty);
        
        assertNotNull(actual);
        assertEquals(TypeDeserializerBaseStub.class, actual.getClass());
    }
    
    @Test
    public void testGetTypeInclusion() {
        assertEquals(TypeDeserializer.As.EXTERNAL_PROPERTY, typeDeserializer.getTypeInclusion());
    }
    
    @Test
    public void testBaseTypeName() {
        when(baseType.getRawClass()).thenReturn(String.class);
        
        assertEquals("java.lang.String", typeDeserializer.baseTypeName());
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
    public void testToString() {
        when(baseType.getRawClass()).thenReturn(String.class);
        
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseStub; base-type:java.lang.String; id-resolver: null]", typeDeserializer.toString());
    }
    
    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        when(jsonParser.getTypeId()).thenReturn("testTypeId");
        when(typeIdResolver.typeFromId(deserializationContext, "testTypeId")).thenReturn(baseType);
        when(deserializationContext.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        when(deserializationContext.findContextualValueDeserializer(baseType, beanProperty)).thenReturn(deserializer);
        when(deserializer.deserialize(jsonParser, deserializationContext)).thenReturn("testValue");
        
        Object actual = typeDeserializer._deserializeWithNativeTypeId(jsonParser, deserializationContext);
        
        assertNotNull(actual);
        assertEquals("testValue", actual);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNativeTypeIdWithException() throws Exception {
        when(jsonParser.getTypeId()).thenReturn("testTypeId");
        when(typeIdResolver.typeFromId(deserializationContext, "testTypeId")).thenReturn(null);
        when(deserializationContext.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(true);
        
        typeDeserializer._deserializeWithNativeTypeId(jsonParser, deserializationContext);
    }
    
    @Test
    public void testHandleUnknownTypeId() throws Exception {
        when(typeIdResolver.getDescForKnownTypeIds()).thenReturn(null);
        
        JavaType actual = typeDeserializer._handleUnknownTypeId(deserializationContext, "testTypeId", typeIdResolver, baseType);
        
        assertNull(actual);
    }
    
    @Test
    public void testHandleUnknownTypeIdWithDescription() throws Exception {
        when(typeIdResolver.getDescForKnownTypeIds()).thenReturn("k1, k2");
        when(deserializationContext.handleUnknownTypeId(baseType, "testTypeId", typeIdResolver, "known type ids = k1, k2")).thenReturn(baseType);
        
        JavaType actual = typeDeserializer._handleUnknownTypeId(deserializationContext, "testTypeId", typeIdResolver, baseType);
        
        assertNotNull(actual);
        assertEquals(baseType, actual);
    }
    
    public class TypeDeserializerBaseStub extends TypeDeserializerBase {
        
        public TypeDeserializerBaseStub(JavaType baseType, TypeIdResolver idRes, String typePropertyName,
                boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }
        
        public TypeDeserializerBaseStub(TypeDeserializerBase src, BeanProperty property) {
            super(src, property);
        }
        
        @Override
        public TypeDeserializerBase forProperty(BeanProperty prop) {
            return new TypeDeserializerBaseStub(this, prop);
        }
        
        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }
    }

}