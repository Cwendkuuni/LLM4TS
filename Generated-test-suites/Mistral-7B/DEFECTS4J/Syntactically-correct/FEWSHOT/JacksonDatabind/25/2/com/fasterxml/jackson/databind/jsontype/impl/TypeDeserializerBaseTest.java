package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testFindDeserializerWithNullTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }
        };

        DeserializationContext context = new TestContext();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, null);
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerWithEmptyTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }
        };

        DeserializationContext context = new TestContext();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, "");
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerWithValidTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, String.class) {
            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }

            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            protected Object _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType)
                    throws IOException {
                throw new AssertionError();
            }
        };

        DeserializationContext context = new TestContext();
        context.setActiveTypeId("com.example.StringType");
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, "com.example.StringType");
        assertNotNull(deserializer);
    }

    @Test
    public void testFindDeserializerWithDefaultImplDeserializer() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }

            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return NullifyingDeserializer.instance;
            }

            @Override
            protected Object _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType)
                    throws IOException {
                throw new AssertionError();
            }
        };

        DeserializationContext context = new TestContext();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, null);
        assertEquals(NullifyingDeserializer.instance, deserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException, JsonProcessingException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }
        };

        JsonParser parser = new TestParser();
        parser.setCodec(new TestObjectMapper());
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(parser, new TestContext(), null);
        assertNull(result);

        parser.setValue("\"com.example.IntegerType\"");
        result = typeDeserializerBase._deserializeWithNativeTypeId(parser, new TestContext(), null);
        assertEquals(Integer.class, result.getClass());
    }

    @Test
    public void testDeserializeWithUnknownTypeId() throws IOException, JsonProcessingException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }

            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return NullifyingDeserializer.instance;
            }

            @Override
            protected Object _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType)
                    throws IOException {
                throw new AssertionError();
            }
        };

        JsonParser parser = new TestParser();
        parser.setCodec(new TestObjectMapper());
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(parser, new TestContext(), "unknownTypeId");
        throw new AssertionError();
    }
}

class TestContext extends DeserializationContext {
    @Override
    public JavaType getRootValueType() {
        return null;
    }

    @Override
    public JavaType getTypeFactory() {
        return null;
    }

    @Override
    public Object findValueDeserializer(JavaType type) {
        return null;
    }

    @Override
    public Object findBeanDeserializer(JavaType type) {
        return null;
    }

    @Override
    public Object findContextualValueDeserializer(JavaType type, BeanProperty property) {
        return null;
    }

    @Override
    public Object findContextualDeserializer(DeserializerProvider provider, BeanDescription beanDesc) {
        return null;
    }

    @Override
    public Object findContextualDeserializer(DeserializerProvider provider, String propertyName) {
        return null;
    }

    @Override
    public Object findValueDeserializer(Class<?> type) {
        return null;
    }

    @Override
    public Object findBeanDeserializer(Class<?> type) {
        return null;
    }

    @Override
    public Object findContextualValueDeserializer(Class<?> type, BeanProperty property) {
        return null;
    }

    @Override
    public Object findContextualDeserializer(DeserializerProvider provider, BeanDescription beanDesc) {
        return null;
    }

    @Override
    public Object findContextualDeserializer(DeserializerProvider provider, String propertyName) {
        return null;
    }

    @Override
    public Object findValueDeserializer(TypeDeserializer deserializer) {
        return null;
    }

    @Override
    public Object findBeanDeserializer(TypeDeserializer deserializer) {
        return null;
    }

    @Override
    public Object findContextualValueDeserializer(TypeDeserializer deserializer, BeanProperty property) {
        return null;
    }

    @Override
    public Object findContextualDeserializer(DeserializerProvider provider, TypeDeserializer deserializer) {
        return null;
    }

    @Override
    public Object findContextualDeserializer(DeserializerProvider provider, String propertyName, TypeDeserializer deserializer) {
        return null;
    }
}

class TestObjectMapper extends ObjectMapper {
    @Override
    public void findAndRegisterModules() {
    }
}

class TestParser extends JsonParser {
    @Override
    public void close() throws IOException {
    }
}