package com.fasterxml.jackson.databind.jsontype.impl; // adjust the package if necessary

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TypeDeserializerBaseTest {

    private static final String TEST_CLASS_NAME = "com.example.test.TestType";
    private static final String TEST_TYPE_ID = "testTypeId";
    private static final String TEST_TYPE_PROPERTY_NAME = "testTypePropertyName";

    private TypeDeserializerBase typeDeserializerBase;
    private TypeIdResolver typeIdResolverMock;
    private DeserializationContext deserializationContextMock;
    private JsonParser jsonParserMock;

    @Before
    public void setup() {
        typeIdResolverMock = mock(TypeIdResolver.class);
        deserializationContextMock = mock(DeserializationContext.class);
        jsonParserMock = mock(JsonParser.class);

        JavaType baseTypeMock = mock(JavaType.class);
        when(baseTypeMock.getRawClass()).thenReturn(Object.class);

        JavaType javaTypeMock = mock(JavaType.class);
        when(javaTypeMock.getRawClass()).thenReturn(String.class);

        typeDeserializerBase = new TypeDeserializerBase(baseTypeMock, typeIdResolverMock, TEST_TYPE_PROPERTY_NAME, true, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return NullifyingDeserializer.instance;
            }
        };
    }

    @Test
    public void testForProperty() {
        BeanProperty beanPropertyMock = mock(BeanProperty.class);
        TypeDeserializer typeDeserializerForProperty = typeDeserializerBase.forProperty(beanPropertyMock);
        assertEquals(TypeDeserializer.class, typeDeserializerForProperty.getClass());
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.WRAP_EXISTING_ARRAY_TYPES, typeInclusion);
    }

    @Test
    public void testBaseTypeName() {
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals("java.lang.Object", baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        String propertyName = typeDeserializerBase.getPropertyName();
        assertEquals(TEST_TYPE_PROPERTY_NAME, propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();
        assertEquals(typeIdResolverMock, typeIdResolver);
    }

    @Test
    public void testGetStringDeserializer() throws IOException {
        String typeId = TEST_TYPE_ID;
        JsonDeserializer<Object> stringDeserializer = typeDeserializerBase._findDeserializer(deserializationContextMock, typeId);
        assertEquals(String.class, stringDeserializer.getClass());

        verify(typeIdResolverMock).typeFromId(eq(deserializationContextMock), eq(typeId));
    }

    @Test(expected = IOException.class)
    public void testGetNullDeserializer() throws IOException {
        typeDeserializerBase._findDeserializer(deserializationContextMock, null);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JsonDeserializer<Object> defaultImplDeserializer = typeDeserializerBase._findDefaultImplDeserializer(deserializationContextMock);
        assertEquals(NullifyingDeserializer.instance, defaultImplDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerWithCustomClass() throws IOException {
        Class<StringBuilder> customClass = StringBuilder.class;

        JavaType javaTypeMockWithCustomClass = mock(JavaType.class);
        when(javaTypeMockWithCustomClass.getRawClass()).thenReturn(customClass);

        TypeDeserializerBase typeDeserializerBaseWithCustomClass = new TypeDeserializerBase(baseTypeMock, typeIdResolverMock, TEST_TYPE_PROPERTY_NAME, true, javaTypeMockWithCustomClass) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return ctxt.findContextualValueDeserializer(javaTypeMockWithCustomClass, null);
            }
        };

        JsonDeserializer<Object> defaultImplDeserializerWithCustomClass = typeDeserializerBaseWithCustomClass._findDefaultImplDeserializer(deserializationContextMock);
        assertEquals(customClass, defaultImplDeserializerWithCustomClass.getValueClass());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParserMock, deserializationContextMock, TEST_TYPE_ID);
        verify(jsonParserMock).nextToken();
        verifyNoMoreInteractions(jsonParserMock);
        verifyNoInteractions(deserializationContextMock);
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeWithNativeTypeIdWhenNoTypeId() throws IOException {
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParserMock, deserializationContextMock, null);
        verify(jsonParserMock).nextToken();
        verifyNoMoreInteractions(jsonParserMock);
        verifyNoInteractions(deserializationContextMock);
        assertEquals(null, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdWhenUnknownTypeId() throws IOException {
        when(typeIdResolverMock.typeFromId(eq(deserializationContextMock), anyString())).thenReturn(null);

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParserMock, deserializationContextMock, TEST_TYPE_ID);
    }
}