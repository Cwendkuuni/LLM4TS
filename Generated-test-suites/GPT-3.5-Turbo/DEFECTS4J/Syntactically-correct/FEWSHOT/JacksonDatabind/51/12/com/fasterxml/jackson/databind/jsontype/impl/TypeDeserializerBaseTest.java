package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    private TypeIdResolver typeIdResolverMock;
    private JavaType baseTypeMock;
    private Object typeIdMock;
    private DeserializationContext deserializationContextMock;
    private JsonDeserializer<Object> defaultImplDeserializerMock;
    private BeanProperty beanPropertyMock;
    private JsonParser jsonParserMock;

    @Before
    public void setup() {
        typeIdResolverMock = mock(TypeIdResolver.class);
        baseTypeMock = mock(JavaType.class);
        typeIdMock = new Object();
        deserializationContextMock = mock(DeserializationContext.class);
        defaultImplDeserializerMock = mock(JsonDeserializer.class);
        beanPropertyMock = mock(BeanProperty.class);
        jsonParserMock = mock(JsonParser.class);
    }

    @Test
    public void test_forProperty() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseStub(baseTypeMock, typeIdResolverMock,
                "typePropertyName", true, baseTypeMock);
        TypeDeserializerBase result = typeDeserializerBase.forProperty(beanPropertyMock);
        
        assertEquals(beanPropertyMock, result._property);
    }

    @Test
    public void test_getPropertyName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseStub(baseTypeMock, typeIdResolverMock,
                "typePropertyName", true, baseTypeMock);

        String result = typeDeserializerBase.getPropertyName();

        assertEquals("typePropertyName", result);
    }

    @Test
    public void test_getTypeName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseStub(baseTypeMock, typeIdResolverMock,
                "typePropertyName", true, baseTypeMock);
        when(baseTypeMock.getRawClass()).thenReturn(String.class);

        String result = typeDeserializerBase.baseTypeName();

        assertEquals("java.lang.String", result);
    }

    @Test
    public void test_getTypeIdResolver() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseStub(baseTypeMock, typeIdResolverMock,
                "typePropertyName", true, baseTypeMock);

        TypeIdResolver result = typeDeserializerBase.getTypeIdResolver();

        assertEquals(typeIdResolverMock, result);
    }

    @Test
    public void test_getDefaultImpl() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseStub(baseTypeMock, typeIdResolverMock,
                "typePropertyName", true, null);

        assertNull(typeDeserializerBase.getDefaultImpl());

        typeDeserializerBase = new TypeDeserializerBaseStub(baseTypeMock, typeIdResolverMock, "typePropertyName",
                true, baseTypeMock);

        assertEquals(baseTypeMock.getRawClass(), typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseStub(baseTypeMock, typeIdResolverMock,
                "typePropertyName", true, baseTypeMock);

        String result = typeDeserializerBase.toString();

        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseStub; base-type:"
                + baseTypeMock + "; id-resolver: " + typeIdResolverMock + "]", result);
    }

    @Test
    public void test_handleUnknownTypeId() throws Exception {
        TypeDeserializerBaseStub typeDeserializerBase = new TypeDeserializerBaseStub(baseTypeMock,
                typeIdResolverMock, "typePropertyName", true, baseTypeMock);
        Class<?> rawClass = mock(Class.class);
        when(baseTypeMock.getRawClass()).thenReturn(rawClass);
        when(rawClass.getName()).thenReturn("name");
        when(typeIdResolverMock.getDescForKnownTypeIds()).thenReturn("extraDesc");
        when(deserializationContextMock.handleUnknownTypeId(baseTypeMock, "typeId", typeIdResolverMock, "extraDesc"))
                .thenReturn(baseTypeMock);
        
        JavaType result = typeDeserializerBase._handleUnknownTypeId(deserializationContextMock, "typeId", typeIdResolverMock, baseTypeMock);
        
        assertEquals(baseTypeMock, result);
    }
}