package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TypeDeserializerBaseTest;
import com.fasterxml.jackson.databind.util.TypeDeserializerBaseTest.TestTypeDeserializer;

public class TypeDeserializerBaseTest extends TypeDeserializerBaseTest {

    private static final String TEST_TYPE_ID = "com.test:TestType";
    private DeserializationContext context;
    private ObjectReader reader;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = mock(DeserializationContext.class);
        reader = mock(ObjectReader.class);
    }

    @Test
    public void testBaseConstructor() throws Exception {
        TypeDeserializerBase deserializer = new TestTypeDeserializer(
                objectMapper.getTypeFactory().constructType(Object.class),
                mock(TypeIdResolver.class),
                null,
                false,
                null
        );
        assertEquals(Object.class, deserializer.getBaseType());
        assertEquals(null, deserializer.getPropertyName());
        assertEquals(null, deserializer.getDefaultImpl());
        assertNull(deserializer.getTypeIdResolver());
        assertNull(deserializer.getDefaultImplDeserializer());
    }

    @Test
    public void testPropertyConstructor() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializerBase deserializer = new TestTypeDeserializer(
                objectMapper.getTypeFactory().constructType(Object.class),
                mock(TypeIdResolver.class),
                null,
                false,
                null,
                property
        );
        assertEquals(Object.class, deserializer.getBaseType());
        assertEquals(property.getName(), deserializer.getPropertyName());
        assertNull(deserializer.getDefaultImpl());
        assertNull(deserializer.getTypeIdResolver());
        assertNull(deserializer.getDefaultImplDeserializer());
    }

    @Test
    public void testForProperty() throws Exception {
        TypeDeserializerBase deserializer = new TestTypeDeserializer(
                objectMapper.getTypeFactory().constructType(Object.class),
                mock(TypeIdResolver.class),
                "testProperty",
                false,
                null
        );
        TypeDeserializerBase forProperty = deserializer.forProperty(mock(BeanProperty.class));
        assertEquals(deserializer.getClass(), forProperty.getClass());
        assertSame(deserializer.getPropertyName(), forProperty.getPropertyName());
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        TypeDeserializerBase deserializer = new TestTypeDeserializer(
                objectMapper.getTypeFactory().constructType(Object.class),
                mock(TypeIdResolver.class),
                null,
                false,
                null
        );
        assertEquals(JsonTypeInfo.As.WRAP_IN_ARRAY, deserializer.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws Exception {
        TypeDeserializerBase deserializer = new TestTypeDeserializer(
                objectMapper.getTypeFactory().constructType(String.class),
                mock(TypeIdResolver.class),
                null,
                false,
                null
        );
        assertEquals(String.class.getName(), deserializer.baseTypeName());
    }

    @Test
    public void testToString() throws Exception {
        TypeDeserializerBase deserializer = new TestTypeDeserializer(
                objectMapper.getTypeFactory().constructType(String.class),
                mock(TypeIdResolver.class),
                null,
                false,
                null
        );
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TestTypeDeserializer;"
                + " base-type:class java.lang.String;"
                + " id-resolver:<mock>]";
        assertEquals(expected, deserializer.toString());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        JsonDeserializer<String> stringDeserializer = new FromStringDeserializer();
        TypeDeserializerBase deserializer = new TestTypeDeserializer(
                objectMapper.getTypeFactory().constructType(String.class),
                mock(TypeIdResolver.class),
                null,
                false,
                null
        );
        when(deserializer.getTypeIdResolver().typeFromId(context, TEST_TYPE_ID)).thenReturn(String.class);
        when(context.findContextualValueDeserializer(String.class, any())).thenReturn(stringDeserializer);

        JsonDeserializer<Object> result = deserializer._findDeserializer(context, TEST_TYPE_ID);
        assertEquals(String.class, result.getClass());
        verify(deserializer.getTypeIdResolver()).typeFromId(context, TEST_TYPE_ID);
        verify(context).findContextualValueDeserializer(eq(String.class), any());
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        JsonDeserializer<Object> defaultImplDeserializer = new NullifyingDeserializer();
        TypeDeserializerBase deserializer = new TestTypeDeserializer(
                objectMapper.getTypeFactory().constructType(String.class),
                mock(TypeIdResolver.class),
                null,
                false,
                Object.class
        );
        when(context.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        when(context.findContextualValueDeserializer(Object.class, any())).thenReturn(defaultImplDeserializer);

        JsonDeserializer<Object> result = deserializer._findDefaultImplDeserializer(context);
        assertEquals(defaultImplDeserializer.getClass(), result.getClass());
        verify(context).findContextualValueDeserializer(eq(Object.class), any());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);

        JsonParser parser = mapper.getFactory().createParser(new StringReader("{\"typeId\":\"com.test:TestType\", \"value\":\"Test Value\"}"));
        TestTypeDeserializer deserializer = new TestTypeDeserializer(
                mapper.getTypeFactory().constructType(Object.class),
                mock(TypeIdResolver.class),
                null,
                false,
                null
        );

        when(deserializer.getTypeIdResolver().typeFromId(parser, null)).thenReturn(TEST_TYPE_ID);
        when(deserializer._findDeserializer(context, TEST_TYPE_ID)).thenReturn(mock(JsonDeserializer.class));

        Object result = deserializer._deserializeWithNativeTypeId(parser, context);
        assertNotNull(result);
        verify(deserializer)._findDeserializer(context, TEST_TYPE_ID);

        parser = mapper.getFactory().createParser(new StringReader("{\"value\":\"Test Value\"}"));
        deserializer = new TestTypeDeserializer(
                mapper.getTypeFactory().constructType(Object.class),
                mock(TypeIdResolver.class),
                null,
                false,
                null
        );

        Object result2 = deserializer._deserializeWithNativeTypeId(parser, context);
        assertNull(result2);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeId() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        TypeDeserializerBase deserializer = new TestTypeDeserializer(
                objectMapper.getTypeFactory().constructType(Object.class),
                mock(TypeIdResolver.class),
                null,
                false,
                null
        );
        deserializer._handleUnknownTypeId(context, "unknownTypeId", mock(TypeIdResolver.class), null);
    }

    private static class TestTypeDeserializer extends TypeDeserializerBase {
        TestTypeDeserializer(JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl) {
            super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        }

        TestTypeDeserializer(JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl, BeanProperty property) {
            super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl, property);
        }

        @Override
        public abstract TypeDeserializer forProperty(BeanProperty prop);

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.WRAP_IN_ARRAY;
        }
    }
}