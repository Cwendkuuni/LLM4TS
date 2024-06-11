package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

@RunWith(MockitoJUnitRunner.class)
public class TypeDeserializerBaseTest {
    
    private TypeDeserializerBase typeDeserializerBase;
    
    @Mock
    private JavaType baseType;
    
    @Mock
    private TypeIdResolver idResolver;
    
    @Mock
    private BeanProperty property;
    
    @Mock
    private DeserializationContext ctxt;
    
    @Before
    public void setup() {
        typeDeserializerBase = new TypeDeserializerBaseMock(baseType, idResolver, "test", true, TypeDeserializerBaseMock.class) {};
    }
    
    @Test
    public void testForProperty() {
        TypeDeserializerBase deserializer = typeDeserializerBase.forProperty(property);
        assertNotNull(deserializer);
        assertEquals(TypeDeserializerBaseMock.class, deserializer.getClass());
    }
    
    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.PROPERTY, typeInclusion);
    }
    
    @Test
    public void testBaseTypeName() {
        when(baseType.getRawClass()).thenReturn(TestBean.class);
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseTest.TestBean", baseTypeName);
    }
    
    @Test
    public void testGetPropertyName() {
        String propertyName = typeDeserializerBase.getPropertyName();
        assertEquals("test", propertyName);
    }
    
    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();
        assertNotNull(typeIdResolver);
    }
    
    @Test
    public void testGetDefaultImpl() {
        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
        assertEquals(TypeDeserializerBaseMock.class, defaultImpl);
    }
    
    @Test
    public void testToString() {
        String toString = typeDeserializerBase.toString();
        assertEquals("[class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseMock; base-type:null; id-resolver: null]", toString);
    }
    
    @Test
    public void testDeserialize() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(jp.getTypeId()).thenReturn("1");
        when(idResolver.typeFromId(ctxt, "1")).thenReturn(baseType);
        when(ctxt.findContextualValueDeserializer(baseType, property)).thenReturn(deserializer);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, "1");
        assertEquals(deserializer, result);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithUnknownTypeId() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        when(jp.getTypeId()).thenReturn("1");
        when(idResolver.typeFromId(ctxt, "1")).thenReturn(null);
        typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, "1");
    }
    
    @Test
    public void testDeserializeWithNullTypeId() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        when(jp.getTypeId()).thenReturn(null);
        JsonDeserializer<Object> result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, null);
        assertEquals(NullifyingDeserializer.instance, result);
    }
    
    @Test
    public void testDeserializeWithDefaultImpl() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(jp.getTypeId()).thenReturn("1");
        when(idResolver.typeFromId(ctxt, "1")).thenReturn(null);
        when(typeDeserializerBase._findDefaultImplDeserializer(ctxt)).thenReturn(deser);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, "1");
        assertEquals(deser, result);
    }
    
    @Test
    public void testHandleUnknownTypeId() throws IOException {
        String typeId = "1";
        String extraDesc = "known type ids = test";
        when(idResolver instanceof TypeIdResolverBase).thenReturn(true);
        when(((TypeIdResolverBase) idResolver).getDescForKnownTypeIds()).thenReturn("test");
        try {
            typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
        } catch(JsonMappingException e) {
            assertEquals("No (native) type id found when one was expected for polymorphic type handling", e.getMessage());
        }
    }
    
    private static class TypeDeserializerBaseMock extends TypeDeserializerBase {

        private static final long serialVersionUID = 1L;

        public TypeDeserializerBaseMock(JavaType baseType, TypeIdResolver idRes, String typePropertyName,
                boolean typeIdVisible, Class<?> defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.PROPERTY;
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new TypeDeserializerBaseMock(_baseType, _idResolver, _typePropertyName, _typeIdVisible, _defaultImpl.getRawClass());
        }
        
    }
    
    private static class TestBean {
        
    }

}