package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class TypeDeserializerBaseTest {
    
    @Mock
    private BeanProperty mockProperty;
    
    @Mock
    private JavaType mockBaseType;
    
    @Mock
    private TypeIdResolver mockIdResolver;
    
    @Mock
    private JavaType mockImplType;
    
    private TypeDeserializerBase typeDeserializer;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        typeDeserializer = new TypeDeserializerBase(mockBaseType, mockIdResolver, "typeProperty", true, Object.class) {
            
            private static final long serialVersionUID = 1L;

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId)
                    throws IOException {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
    }
    
    @Test
    public void testBaseTypeName() {
        String baseTypeName = typeDeserializer.baseTypeName();
        
        assertEquals("java.lang.Object", baseTypeName);
    }
    
    @Test
    public void testGetPropertyName() {
        String propertyName = typeDeserializer.getPropertyName();
        
        assertEquals("typeProperty", propertyName);
    }
    
    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver typeIdResolver = typeDeserializer.getTypeIdResolver();
        
        assertEquals(mockIdResolver, typeIdResolver);
    }
    
    @Test
    public void testGetDefaultImpl() {
        Class<?> defaultImpl = typeDeserializer.getDefaultImpl();
        
        assertEquals(Object.class, defaultImpl);
    }
    
    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        Object result = typeDeserializer._deserializeWithNativeTypeId(null, null);
        
        assertNull(result);
    }
    
    @Test(expected = IOException.class)
    public void testHandleUnknownTypeId() throws IOException {
        typeDeserializer._handleUnknownTypeId(null, "unknownTypeId", mockIdResolver, mockBaseType);
    }
}