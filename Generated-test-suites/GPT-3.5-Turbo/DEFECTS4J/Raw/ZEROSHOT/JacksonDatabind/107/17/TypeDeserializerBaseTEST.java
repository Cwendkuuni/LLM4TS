package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.*;

public class TypeDeserializerBaseTest extends TestCase {
    
    private TypeDeserializerBase typeDeserializer;
    private JavaType baseType;
    private TypeIdResolver typeIdResolver;
    private String typePropertyName;
    private boolean typeIdVisible;
    private JavaType defaultImpl;
    private BeanProperty property;
    private JsonDeserializer<Object> deser;
    private DeserializationContext context;
    
    private String typeId = "testTypeId";
    
    @Before
    public void setUp() {
        baseType = Mockito.mock(JavaType.class);
        typeIdResolver = Mockito.mock(TypeIdResolver.class);
        typePropertyName = "testType";
        typeIdVisible = true;
        defaultImpl = Mockito.mock(JavaType.class);
        property = Mockito.mock(BeanProperty.class);
        deser = Mockito.mock(JsonDeserializer.class);
        context = Mockito.mock(DeserializationContext.class);
        
        typeDeserializer = new TypeDeserializerBase(baseType, typeIdResolver, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
    }
    
    @Test
    public void testBaseTypeName() {
        when(baseType.getRawClass()).thenReturn(Object.class);
        assertEquals("java.lang.Object", typeDeserializer.baseTypeName());
    }
    
    @Test
    public void testGetPropertyName() {
        assertEquals("testType", typeDeserializer.getPropertyName());
    }
    
    @Test
    public void testGetTypeIdResolver() {
        assertEquals(typeIdResolver, typeDeserializer.getTypeIdResolver());
    }
    
    @Test
    public void testGetDefaultImpl() {
        when(defaultImpl.getRawClass()).thenReturn(Object.class);
        assertEquals(Object.class, typeDeserializer.getDefaultImpl());
    }
    
    @Test
    public void testBaseType() {
        assertEquals(baseType, typeDeserializer.baseType());
    }
    
    @Test
    public void testToString() {
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]", typeDeserializer.toString());
    }
    
    @Test
    public void test_findDeserializer_DeserializerNotCached() throws IOException {
        Map<String,JsonDeserializer<Object>> deserializers = mock(ConcurrentHashMap.class);
        JsonParser jp = Mockito.mock(JsonParser.class);
        
        typeDeserializer._deserializers = deserializers;
        
        when(jp.getTypeId()).thenReturn(typeId);
        when(deserializers.get(typeId)).thenReturn(null);
        when(typeIdResolver.typeFromId(any(), any())).thenReturn(baseType);
        when(context.findContextualValueDeserializer(any(), any())).thenReturn(deser);
        
        JsonDeserializer<Object> result = typeDeserializer._findDeserializer(context, typeId);
        
        verify(deserializers).put(typeId, deser);
        assertEquals(deser, result);
    }
    
    @Test
    public void test_findDeserializer_DeserializerCached() throws IOException {
        Map<String,JsonDeserializer<Object>> deserializers = mock(ConcurrentHashMap.class);
        
        typeDeserializer._deserializers = deserializers;
        
        when(deserializers.get(typeId)).thenReturn(deser);
        
        JsonDeserializer<Object> result = typeDeserializer._findDeserializer(context, typeId);
        
        verify(deserializers, never()).put(typeId, deser);
        assertEquals(deser, result);
    }
    
    @Test
    public void test_findDefaultImplDeserializer_DefaultImplNull() throws IOException {
        when(defaultImpl.getRawClass()).thenReturn(null);
        JsonDeserializer<Object> result = typeDeserializer._findDefaultImplDeserializer(context);
        assertEquals(NullifyingDeserializer.instance, result);
    }
    
    @Test
    public void test_findDefaultImplDeserializer_DefaultImplBogus() throws IOException {
        when(defaultImpl.getRawClass()).thenReturn(Void.class);
        JsonDeserializer<Object> result = typeDeserializer._findDefaultImplDeserializer(context);
        assertEquals(NullifyingDeserializer.instance, result);
    }
    
    @Test
    public void test_findDefaultImplDeserializer_DeserializerCached() throws IOException {
        typeDeserializer._defaultImplDeserializer = deser;
        JsonDeserializer<Object> result = typeDeserializer._findDefaultImplDeserializer(context);
        assertEquals(deser, result);
    }
    
    @Test
    public void test_findDefaultImplDeserializer_DeserializerNotCached() throws IOException {
        Class<?> raw = Mockito.mock(Class.class);
        typeDeserializer._defaultImpl = defaultImpl;
        when(defaultImpl.getRawClass()).thenReturn(raw);
        when(raw.isSynthetic()).thenReturn(false);
        when(raw.isInterface()).thenReturn(false);
        when(context.findContextualValueDeserializer(any(), any())).thenReturn(deser);
        JsonDeserializer<Object> result = typeDeserializer._findDefaultImplDeserializer(context);
        assertEquals(deser, result);
    }
    
    @Test
    public void test_deserializeWithNativeTypeId_WithTypeNull() throws IOException {
        when(_findDefaultImplDeserializer(context)).thenReturn(deser);
        Object result = typeDeserializer._deserializeWithNativeTypeId(mock(JsonParser.class), context);
        verify(context).reportInputMismatch(baseType(), "No (native) type id found when one was expected for polymorphic type handling");
        assertNull(result);
    }
    
    @Test
    public void test_deserializeWithNativeTypeId_WithTypeAvailable() throws IOException {
        JsonObject object = Mockito.mock(JsonObject.class);
        when(object.getTypeId()).thenReturn(typeId);
        when(object.getString("testType")).thenReturn(typeId);
        when(_findDeserializer(context, typeId)).thenReturn(deser);
        Object result = typeDeserializer._deserializeWithNativeTypeId(object, context, typeId);
        verify(deser).deserialize(object, context);
        assertNotNull(result);
    }
    
    @Test
    public void test_handleUnknownTypeId() throws IOException {
        JsonObject object = Mockito.mock(JsonObject.class);
        when(object.getTypeId()).thenReturn(typeId);
        when(object.getString("testType")).thenReturn(typeId);
        when(_idResolver.getDescForKnownTypeIds()).thenReturn(null);
        when(context.handleUnknownTypeId(eq(baseType), eq(typeId), eq(typeIdResolver), any())).thenReturn(null);
        assertNull(typeDeserializer._handleUnknownTypeId(context, typeId));
    }
    
    @Test
    public void test_handleMissingTypeId() throws IOException {
        JsonObject object = Mockito.mock(JsonObject.class);
        when(object.getTypeId()).thenReturn(typeId);
        when(object.getString("testType")).thenReturn(typeId);
        when(_idResolver.getDescForKnownTypeIds()).thenReturn(null);
        when(context.handleMissingTypeId(eq(baseType), eq(typeIdResolver), any())).thenReturn(null);
        assertNull(typeDeserializer._handleMissingTypeId(context, null));
    }

}