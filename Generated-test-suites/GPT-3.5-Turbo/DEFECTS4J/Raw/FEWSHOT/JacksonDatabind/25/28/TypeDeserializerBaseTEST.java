package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class TypeDeserializerBaseTest {
    private static TypeDeserializerBase deserializer;
    private static JavaType baseType;
    private static TypeIdResolver idResolver;
    private static String typePropertyName;
    private static boolean typeIdVisible;
    private static Class<?> defaultImpl;
    private static BeanProperty beanProperty;
    
    @BeforeClass
    public static void setup() {
        baseType = mock(JavaType.class);
        idResolver = mock(TypeIdResolver.class);
        typePropertyName = "typeName";
        typeIdVisible = true;
        defaultImpl = null;
        beanProperty = mock(BeanProperty.class);
        
        deserializer = new TypeDeserializerBaseMock(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testForProperty() {
        TypeDeserializerBase result = deserializer.forProperty(beanProperty);
        
        // Verify that the result is a new instance of TypeDeserializerBase with the same properties except for the beanProperty
        assertEquals(TypeDeserializerBaseMock.class, result.getClass());
        assertEquals(baseType, ((TypeDeserializerBaseMock) result)._baseType);
        assertEquals(idResolver, ((TypeDeserializerBaseMock) result)._idResolver);
        assertEquals(typePropertyName, ((TypeDeserializerBaseMock) result)._typePropertyName);
        assertEquals(typeIdVisible, ((TypeDeserializerBaseMock) result)._typeIdVisible);
        assertEquals(defaultImpl, ((TypeDeserializerBaseMock) result)._defaultImpl);
        assertNull(((TypeDeserializerBaseMock) result)._property);
    }
    
    @Test
    public void testGetTypeInclusion() {
        // The default implementation should return null
        assertNull(deserializer.getTypeInclusion());
    }
    
    @Test
    public void testBaseTypeName() {
        // Mock the getRawClass() method of the JavaType to return a specific class name
        when(baseType.getRawClass()).thenReturn(String.class);
        
        assertEquals("java.lang.String", deserializer.baseTypeName());
    }
    
    @Test
    public void testGetPropertyName() {
        assertEquals("typeName", deserializer.getPropertyName());
    }
    
    @Test
    public void testGetTypeIdResolver() {
        assertEquals(idResolver, deserializer.getTypeIdResolver());
    }
    
    @Test
    public void testGetDefaultImpl() {
        assertNull(deserializer.getDefaultImpl());
    }
    
    @Test
    public void testToString() {
        // Mock the getRawClass() method of the JavaType to return a specific class name
        when(baseType.getRawClass()).thenReturn(String.class);
        
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseMock; base-type:java.lang.String; id-resolver: " + idResolver + "]";
        
        assertEquals(expected, deserializer.toString());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testDeserializeWithNativeTypeId() throws Exception {
        deserializer._deserializeWithNativeTypeId(mock(JsonParser.class), mock(DeserializationContext.class));
    }

    private static class TypeDeserializerBaseMock extends TypeDeserializerBase {
        private static final long serialVersionUID = 1L;

        protected TypeDeserializerBaseMock(JavaType baseType, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible,
                Class<?> defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        protected TypeDeserializerBaseMock(TypeDeserializerBaseMock src, BeanProperty property) {
            super(src, property);
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new TypeDeserializerBaseMock(this, prop);
        }        
    }
}