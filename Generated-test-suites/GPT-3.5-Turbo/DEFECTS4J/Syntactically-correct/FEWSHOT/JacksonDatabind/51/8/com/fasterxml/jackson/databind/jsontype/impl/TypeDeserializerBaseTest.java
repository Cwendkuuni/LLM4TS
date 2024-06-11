package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TypeDeserializerBaseTest {

    @Mock
    private JsonParser jsonParserMock;

    @Mock
    private DeserializationContext deserializationContextMock;

    @Mock
	private JsonDeserializer<Object> jsonDeserializerMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(jsonParserMock.getTypeId()).thenReturn("1");
    }

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null) {};
        assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());
    }
    
    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, "type", false, null) {};
        assertEquals("type", typeDeserializerBase.getPropertyName());
    }
    
    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, typeIdResolver, null, false, null) {};
        assertEquals(typeIdResolver, typeDeserializerBase.getTypeIdResolver());
    }
    
    @Test
    public void testGetDefaultImpl() {
        JavaType defaultImpl = mock(JavaType.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, defaultImpl) {};
        assertEquals(defaultImpl.getRawClass(), typeDeserializerBase.getDefaultImpl());
    }
    
    @Test
    public void testToString() {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, typeIdResolver, "typePropertyName", true, null) {};
        assertEquals("["+typeDeserializerBase.getClass().getName()+"; base-type:"+baseType+"; id-resolver: "+typeIdResolver+"]", typeDeserializerBase.toString());
    }
    
    @Test
    public void testDeserializeWithNativeTypeId_NullTypeId() throws IOException {
        String typeId = null;
        
        TypeDeserializerBase typeDeserializerBase = spy(TypeDeserializerBase.class);
        when(typeDeserializerBase._findDefaultImplDeserializer(eq(deserializationContextMock))).thenReturn(jsonDeserializerMock);
        
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParserMock, deserializationContextMock, typeId);
        
        assertNull(result);
        verify(typeDeserializerBase, times(1))._findDefaultImplDeserializer(eq(deserializationContextMock));
        verify(jsonDeserializerMock, never()).deserialize(eq(jsonParserMock), eq(deserializationContextMock));
    }
    
    @Test
    public void testDeserializeWithNativeTypeId_ValidTypeId() throws IOException {
        String typeId = "1";
        
        TypeDeserializerBase typeDeserializerBase = spy(TypeDeserializerBase.class);
        when(typeDeserializerBase._findDeserializer(eq(deserializationContextMock), eq(typeId))).thenReturn(jsonDeserializerMock);
        when(jsonDeserializerMock.deserialize(eq(jsonParserMock), eq(deserializationContextMock))).thenReturn("result");
        
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParserMock, deserializationContextMock, typeId);
        
        assertEquals("result", result);
        verify(typeDeserializerBase, never())._findDefaultImplDeserializer(eq(deserializationContextMock));
        verify(typeDeserializerBase, times(1))._findDeserializer(eq(deserializationContextMock), eq(typeId));
        verify(jsonDeserializerMock, times(1)).deserialize(eq(jsonParserMock), eq(deserializationContextMock));
    }
    
    @Test
    public void testHandleUnknownTypeId() throws IOException {
        String typeId = "1";
        String extraDesc = "extra description";
        
        DeserializationContext ctxt = mock(DeserializationContext.class, RETURNS_DEEP_STUBS);
        JavaType actualType = mock(JavaType.class);
        when(ctxt.handleUnknownTypeId(any(JavaType.class), anyString(), any(TypeIdResolver.class), anyString())).thenReturn(actualType);
        
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(actualType, null, null, false, null) {};
        
        JavaType result = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, null, actualType);
        
        assertEquals(actualType, result);
        verify(ctxt, times(1)).handleUnknownTypeId(any(JavaType.class), anyString(), any(TypeIdResolver.class), anyString());
    }
}