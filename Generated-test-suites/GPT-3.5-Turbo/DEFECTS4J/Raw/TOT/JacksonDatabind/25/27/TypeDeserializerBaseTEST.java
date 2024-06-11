package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.SimpleType;

import static org.mockito.Mockito.*;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;

    private DeserializationContext deserializationContext;
    private JsonParser jsonParser;
    private JavaType javaType;
    private BeanProperty beanProperty;
    private TypeIdResolver typeIdResolver;
    private String typePropertyName;
    private boolean typeIdVisible;
    private Class<?> defaultImpl;

    @Before
    public void setUp() {
        javaType = SimpleType.constructUnsafe(Object.class);
        beanProperty = Mockito.mock(BeanProperty.class);
        typeIdResolver = Mockito.mock(TypeIdResolver.class);

        deserializationContext = Mockito.mock(DeserializationContext.class);
        jsonParser = Mockito.mock(JsonParser.class);

        typePropertyName = "type";
        typeIdVisible = true;
        defaultImpl = Object.class;

        typeDeserializerBase = new TestTypeDeserializerBase(javaType, typeIdResolver, typePropertyName,
                typeIdVisible, defaultImpl);
    }

    @Test
    public void testForProperty() {
        BeanProperty newBeanProperty = Mockito.mock(BeanProperty.class);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(newBeanProperty);

        assertEqual(typeDeserializerBase, typeDeserializerBase, typeDeserializer);
    }

    @Test
    public void testGetTypeInclusion() {
        TypeDeserializer.As typeInclusion = typeDeserializerBase.getTypeInclusion();

        assertEqual(TypeDeserializer.As.WRAPPER_PROPERTY, typeInclusion);
    }

    @Test
    public void testBaseTypeName() {
        String baseTypeName = typeDeserializerBase.baseTypeName();

        assertEqual(Object.class.getName(), baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        String propertyName = typeDeserializerBase.getPropertyName();

        assertEqual(typePropertyName, propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver returnedTypeIdResolver = typeDeserializerBase.getTypeIdResolver();

        assertEqual(typeIdResolver, returnedTypeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> returnedDefaultImpl = typeDeserializerBase.getDefaultImpl();

        assertEqual(defaultImpl, returnedDefaultImpl);
    }

    @Test
    public void testToString() {
        String stringRepresentation = typeDeserializerBase.toString();

        assertEqual("[com.fasterxml.jackson.databind.jsontype.impl.TestTypeDeserializerBase; base-type:Object; " +
                "id-resolver: " + typeIdResolver + "]", stringRepresentation);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        TestTypeDeserializerBase typeDeserializerBaseSpy = Mockito.spy(typeDeserializerBase);
        Object typeId = "MyTestId";

        typeDeserializerBaseSpy._deserializeWithNativeTypeId(jsonParser, deserializationContext, typeId);

        verify(typeDeserializerBaseSpy).deserialize(jsonParser, deserializationContext);
    }

    @Test(expected = com.fasterxml.jackson.databind.JsonMappingException.class)
    public void testDeserializeWithNativeTypeId_WithNullTypeId_ThrowsJsonMappingException() throws Exception {
        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext, null);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        try {
            typeDeserializerBase._handleUnknownTypeId(deserializationContext, "UnknownType",
                    typeIdResolver, javaType);
        } catch (com.fasterxml.jackson.databind.JsonMappingException e) {
            assertEqual("UnknownType", e.getTypeId());
        }
    }

    private static void assertEqual(Object expected, Object actual) {
        assertEqual(null, expected, actual);
    }

    private static void assertEqual(String message, Object expected, Object actual) {
        org.junit.Assert.assertEquals(message, expected, actual);
    }

    private static class TestTypeDeserializerBase extends TypeDeserializerBase {

        protected TestTypeDeserializerBase(JavaType baseType, TypeIdResolver idRes,
                                           String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return this;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

        @Override
        protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws java.io.IOException {
            return null;
        }

        @Override
        protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                                                                TypeIdResolver idResolver, JavaType baseType) throws java.io.IOException {
            throw new com.fasterxml.jackson.databind.JsonMappingException(baseType, typeId, null);
        }

        @Override
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws java.io.IOException {
            return null;
        }
    }
}