package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * Test cases for {@link TypeDeserializerBase} class.
 */
public class TypeDeserializerBaseTest extends TestBase {

    @Test
    public void testFindDeserializerWithNullTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(Object.class, null, null, null, null);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getConfig().getContextInstance();
        JsonParser jp = mapper.getFactory().createParser(new ByteArrayInputStream(new byte[0]));

        assertNull(typeDeserializerBase._findDeserializer(ctxt, null));
    }

    @Test
    public void testFindDeserializerWithEmptyTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(Object.class, null, null, null, null);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getConfig().getContextInstance();
        JsonParser jp = mapper.getFactory().createParser(new ByteArrayInputStream(new byte[0]));

        assertNull(typeDeserializerBase._findDeserializer(ctxt, ""));
    }

    @Test
    public void testFindDeserializerWithValidTypeId() throws IOException {
        Class<?> mockType = mock(Class.class);
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(Object.class, null, null, null, null);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getConfig().getContextInstance();
        JsonParser jp = mapper.getFactory().createParser(new ByteArrayInputStream(new byte[0]));

        when(mockType.getName()).thenReturn("com.example.MyType");

        String typeId = "com.example.MyType";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        typeDeserializerBase._deserializers.put(typeId, deserializer);

        Object result = typeDeserializerBase._findDeserializer(ctxt, typeId);
        assertEquals(deserializer, result);
    }

    @Test
    public void testFindDeserializerWithUnknownTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(Object.class, null, null, null, null);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getConfig().getContextInstance();
        JsonParser jp = mapper.getFactory().createParser(new ByteArrayInputStream(new byte[0]));

        String typeId = "com.unknown.MyType";

        assertNull(typeDeserializerBase._findDeserializer(ctxt, typeId));
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(Object.class, null, null, null, null);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getConfig().getContextInstance();
        JsonParser jp = mapper.getFactory().createParser(new ByteArrayInputStream(new byte[0]));

        assertNull(typeDeserializerBase._findDefaultImplDeserializer(ctxt));

        JavaType defaultImpl = mapper.getTypeFactory().constructType(String.class);
        JsonDeserializer<String> defaultImplDeserializer = new StringDeserializer();
        typeDeserializerBase._defaultImplDeserializer = defaultImplDeserializer;

        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertEquals(defaultImplDeserializer, result);
    }

    @Test
    public void testDeserializeWithNativeTypeIdNull() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(Object.class, null, null, null, null);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getConfig().getContextInstance();
        JsonParser jp = mapper.getFactory().createParser(new ByteArrayInputStream(new byte[0]));

        assertNull(typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt));
    }

    @Test
    public void testDeserializeWithNativeTypeIdEmptyString() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(Object.class, null, null, null, null);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getConfig().getContextInstance();
        JsonParser jp = mapper.getFactory().createParser(new ByteArrayInputStream(new byte[0]));

        String typeId = "";
        assertNull(typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId));
    }

    @Test
    public void testDeserializeWithNativeTypeIdInvalidTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(Object.class, null, null, null, null);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getConfig().getContextInstance();
        JsonParser jp = mapper.getFactory().createParser(new ByteArrayInputStream(new byte[0]));

        String typeId = "InvalidTypeId";
        assertNull(typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId));
    }

    @Test
    public void testDeserializeWithNativeTypeIdValidTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(Object.class, null, null, null, null);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getConfig().getContextInstance();
        JsonParser jp = mapper.getFactory().createParser(new ByteArrayInputStream(new byte[0]));

        String typeId = "java.lang.String";
        String expectedResult = "test";
        String result = (String) typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(Object.class, null, null, null, null);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getConfig().getContextInstance();
        JsonParser jp = mapper.getFactory().createParser(new ByteArrayInputStream(new byte[0]));

        String typeId = "com.unknown.MyType";
        JavaType actual = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, null, null);
        assertNull(actual);

        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.typeFromId(ctxt, typeId)).thenReturn(null);

        actual = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, null);
        assertNull(actual);

        when(idResolver.typeFromId(ctxt, typeId)).thenReturn(mockType(String.class));

        actual = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, null);
        assertEquals(mockType(String.class), actual);
    }

    private static Class<?> mockType(Class<?> clazz) {
        return mock(Class.class);
    }

    static class BaseTypeDeserializer extends TypeDeserializerBase {
        public BaseTypeDeserializer(JavaType baseType, TypeIdResolver idResolver, String typePropertyName,
                boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            throw new UnsupportedOperationException();
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            throw new UnsupportedOperationException();
        }
    }

    static class StringDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return "test";
        }
    }
}