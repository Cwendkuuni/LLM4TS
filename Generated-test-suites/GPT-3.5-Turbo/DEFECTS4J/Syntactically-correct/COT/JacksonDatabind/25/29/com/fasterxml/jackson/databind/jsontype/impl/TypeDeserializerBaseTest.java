
package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.JavaType;

import org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class TypeDeserializerBaseTest {

    @Mock
    private TypeIdResolver idResolver;
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() {
        idResolver = mock(TypeIdResolver.class);
        JavaType baseType = mock(JavaType.class);
        BeanProperty property = mock(BeanProperty.class);
        Class<?> defaultImpl = Object.class;
        typeDeserializerBase = new TypeDeserializerBaseTestStub(baseType,idResolver,"",true,defaultImpl);
    }

    @Test
    public void TestForProperty() {
        assertFalse(typeDeserializerBase.forProperty(null) != null);
    }

    @Test
    public void TestGetTypeInclusion() {
        assertNull(typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void TestBaseTypeName() {
        assertNull(typeDeserializerBase.baseTypeName());
    }

    @Test
    public void TestGetPropertyName() {
        assertNull(typeDeserializerBase.getPropertyName());
    }

    @Test
    public void TestGetTypeIdResolver() {
        assertNull(typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void TestJavaUtilMap() {
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void TestToString() {
        assertNull(typeDeserializerBase.toString());
    }
    
    @Test(expected = Exception.class)
    public void TestThrowUnknownTypeId() throws Exception {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        mock(JsonDeserializer.class);
        String typeId = "";
        typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, null);
    }
    
    class TypeDeserializerBaseTestStub extends TypeDeserializerBase {
        protected TypeDeserializerBaseTestStub(JavaType baseType, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }
    }
}
