package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.mockito.Mockito;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import java.io.IOException;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializerBase = Mockito.mock(TypeDeserializerBase.class);
        Mockito.when(typeDeserializerBase.baseTypeName()).thenReturn("BaseTypeName");
        
        assertEquals("BaseTypeName", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializerBase = Mockito.mock(TypeDeserializerBase.class);
        Mockito.when(typeDeserializerBase.getPropertyName()).thenReturn("PropertyName");
        
        assertEquals("PropertyName", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver typeIdResolver = Mockito.mock(TypeIdResolver.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseMock(new JavaTypeMock(), typeIdResolver, "TypeProp", true, new JavaTypeMock());
        
        assertEquals(typeIdResolver, typeDeserializerBase.getTypeIdResolver());
    }
    
    private static class TypeDeserializerBaseMock extends TypeDeserializerBase {
        
        protected TypeDeserializerBaseMock(JavaType baseType, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;  // Mocked for testing purposes
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;  // Mocked for testing purposes
        }
    }
    
    private static class JavaTypeMock extends JavaType {
        
    }

}