package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.Converter.StdConverter;

public class TypeDeserializerBaseTest {

    private static final String TEST_TYPE_ID = "testTypeId";

    private ObjectMapper objectMapper;
    private TypeDeserializerBase testee;
    private DeserializationContext context;
    private JsonParser parser;
    private JsonDeserializer<Object> deserializerMock;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        context = mock(DeserializationContext.class);
        parser = mock(JsonParser.class);
        deserializerMock = mock(JsonDeserializer.class);
        testee = new TestTypeDeserializerBase(JavaType.constructType(Object.class), mock(TypeIdResolver.class),
                "testPropertyName", false, JavaType.constructType(String.class));
    }

    @Test
    public void testForPropertyWithProperty() throws IOException {
        BeanProperty beanProperty = mock(BeanProperty.class);
        when(beanProperty.getName()).thenReturn("testPropertyName");
        testee = new TestTypeDeserializerBase(testee, beanProperty);
        assertEquals(testee.getClass(), TypeDeserializerBase.class);
        assertEquals("testPropertyName", testee.getPropertyName());
    }

    @Test
    public void testGetBaseType() throws IOException {
        assertEquals(Object.class, testee.baseType());
    }

    @Test
    public void testGetTypeIdResolver() throws IOException {
        TypeIdResolver idResolverMock = mock(TypeIdResolver.class);
        testee = new TestTypeDeserializerBase(JavaType.constructType(Object.class), idResolverMock,
                "testPropertyName", false, JavaType.constructType(String.class));
        assertEquals(idResolverMock, testee.getTypeIdResolver());
    }

    @Test
    public void testGetTypePropertyName() throws IOException {
        assertEquals("testPropertyName", testee.getPropertyName());
    }

    @Test
    public void testGetDefaultImpl() throws IOException {
        assertEquals(String.class, testee.getDefaultImpl());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        when(context.findContextualValueDeserializer(eq(String.class), anyObject())).thenReturn(deserializerMock);
        assertEquals(deserializerMock, testee._findDeserializer(context, TEST_TYPE_ID));
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JsonDeserializer<Object> defaultImplDeserializer = mock(JsonDeserializer.class);
        when(context.findContextualValueDeserializer(eq(String.class), anyObject())).thenReturn(defaultImplDeserializer);
        when(defaultImplDeserializer.getNullValue()).thenReturn(null);

        synchronized (testee._defaultImpl) {
            when(testee._defaultImpl._defaultImplDeserializer).thenReturn(null);
            testee._defaultImpl._defaultImplDeserializer = defaultImplDeserializer;
        }

        assertEquals(defaultImplDeserializer, testee._findDefaultImplDeserializer(context));
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        when(context.handleMissingTypeId(eq(Object.class), anyObject())).thenThrow(new IOException("Test exception"));

        when(context.handleUnknownTypeId(eq(Object.class), eq(TEST_TYPE_ID), anyObject(), anyString())).thenThrow(new IOException("Test exception"));

        JavaType expectedType = JavaType.constructType(Integer.class);
        when(context.handleUnknownTypeId(eq(Object.class), eq(TEST_TYPE_ID), anyObject(), anyString()))
                .thenReturn(expectedType);

        TypeIdResolver typeIdResolverMock = mock(TypeIdResolver.class);
        when(typeIdResolverMock.typeFromId(context, TEST_TYPE_ID)).thenReturn(expectedType);
        testee = new TestTypeDeserializerBase(JavaType.constructType(Object.class), typeIdResolverMock,
                "testPropertyName", false, JavaType.constructType(String.class));

        Object result = testee._handleUnknownTypeId(context, TEST_TYPE_ID);
        assertEquals(expectedType, result);
    }

    @Test
    public void testHandleMissingTypeId() throws IOException {
        when(context.handleMissingTypeId(eq(Object.class), anyObject())).thenThrow(new IOException("Test exception"));

        TypeIdResolver typeIdResolverMock = mock(TypeIdResolver.class);
        when(typeIdResolverMock.typeFromId(context, TEST_TYPE_ID)).thenReturn(JavaType.constructType(Integer.class));
        testee = new TestTypeDeserializerBase(JavaType.constructType(Object.class), typeIdResolverMock,
                "testPropertyName", false, JavaType.constructType(String.class));

        Object result = testee._handleMissingTypeId(context, "Error message");
        assertEquals(JavaType.constructType(Integer.class), result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        when(parser.getTypeId()).thenReturn(TEST_TYPE_ID);
        when(context.findContextualValueDeserializer(eq(String.class), anyObject())).thenReturn(deserializerMock);

        Object result = testee._deserializeWithNativeTypeId(parser, context);
        assertEquals(deserializerMock, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithInvalidTypeId() throws IOException {
        when(parser.getTypeId()).thenReturn("invalidTypeId");
        when(context.handleUnknownTypeId(eq(Object.class), anyObject())).thenThrow(new IOException("Test exception"));

        testee._deserializeWithNativeTypeId(parser, context);
    }

    @Test
    public void testDeserializeWithoutTypeId() throws IOException {
        when(parser.getTypeId()).thenReturn(null);
        when(context.findContextualValueDeserializer(eq(null), anyObject())).thenReturn(deserializerMock);

        Object result = testee._deserializeWithNativeTypeId(parser, context);
        assertEquals(deserializerMock, result);
    }

    @Test
    public void testToString() throws IOException {
        assertEquals("[com.example.test.TypeDeserializerBase; base-type:class java.lang.Object; id-resolver:com.example.test.TypeIdResolverImpl@7f81a1a2;]", testee.toString());
    }

    static class TestTypeDeserializerBase extends TypeDeserializerBase {

        public TestTypeDeserializerBase(JavaType baseType, TypeIdResolver idResolver, String typePropertyName,
                boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        }

        public TestTypeDeserializerBase(TypeDeserializerBase src, BeanProperty property) {
            super(src, property);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

        @Override
        public String toString() {
            return "TestTypeDeserializerBase";
        }
    }

    static class TestTypeIdResolver implements TypeIdResolver {

        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
            if (TEST_TYPE_ID.equals(typeId)) {
                return JavaType.constructType(Integer.class);
            }
            return null;
        }
    }

    static class TestConverter implements Converter<String, Integer> {

        @Override
        public Integer convert(String value, StdConverter<String, Integer> converter) throws IOException {
            return Integer.parseInt(value);
        }
    }
}

class StdConverter<T, U> implements Converter<T, U> {

}