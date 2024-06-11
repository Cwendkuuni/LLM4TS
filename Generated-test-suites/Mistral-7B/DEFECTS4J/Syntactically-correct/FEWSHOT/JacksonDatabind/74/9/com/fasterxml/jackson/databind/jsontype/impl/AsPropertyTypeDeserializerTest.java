package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;

public class AsPropertyTypeDeserializerTest {
    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        TokenBuffer tb = new TokenBuffer(factory.createParser("{}"), null);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                null,
                "type",
                true,
                null,
                As.PROPERTY
        );

        JsonParser jp = factory.createParser("{\"type\":\"String\"}");
        Object result = deserializer.deserializeTypedFromObject(jp, new TestContext());
        assertEquals("String", result.getClass().getName());

        jp = factory.createParser("{\"type\":null}");
        assertNull(deserializer.deserializeTypedFromObject(jp, new TestContext()));

        jp = factory.createParser("{\"type\":\"Integer\"}");
        assertInstanceOf(Integer.class, deserializer.deserializeTypedFromObject(jp, new TestContext()));

        jp = factory.createParser("{\"type\":\"Boolean\"}");
        assertInstanceOf(Boolean.class, deserializer.deserializeTypedFromObject(jp, new TestContext()));

        jp = factory.createParser("{\"type\":\"null\"}");
        assertNull(deserializer.deserializeTypedFromObject(jp, new TestContext()));

        jp = factory.createParser("{\"type\":\"[\"String\"]\",\"data\":[\"foo\",\"bar\"]}");
        assertInstanceOf(List.class, deserializer.deserializeTypedFromObject(jp, new TestContext()));

        jp = factory.createParser("{\"data\":\"foo\"}"); // no type information
        assertNull(deserializer.deserializeTypedFromObject(jp, new TestContext()));
    }

    @Test
    public void testDeserializeTypedForId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                null,
                "type",
                true,
                null,
                As.PROPERTY
        );

        JsonParser jp = factory.createParser("{\"type\":\"String\",\"data\":\"foo\"}");
        assertEquals("foo", deserializer._deserializeTypedForId(jp, new TestContext(), null).toString());

        jp = factory.createParser("{\"type\":null,\"data\":\"foo\"}"); // no type information
        assertNull(deserializer._deserializeTypedForId(jp, new TestContext(), null));
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                null,
                "type",
                true,
                null,
                As.PROPERTY
        );

        JsonParser jp = factory.createParser("{\"data\":\"foo\"}");
        assertNull(deserializer._deserializeTypedUsingDefaultImpl(jp, new TestContext(), null));
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(List.class),
                null,
                "type",
                true,
                null,
                As.PROPERTY
        );

        JsonParser jp = factory.createParser("[{\"type\":\"String\",\"data\":\"foo\"},{\"type\":\"String\",\"data\":\"bar\"}]");
        assertEquals(List.class, deserializer.getClass());
        assertEquals(2, ((List<?>) deserializer.deserializeTypedFromArray(jp, new TestContext())).size());
    }

    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(Integer.class),
                null,
                "type",
                true,
                null,
                As.PROPERTY
        );

        JsonParser jp = factory.createParser("42");
        assertEquals(42, deserializer.deserializeTypedFromScalar(jp, new TestContext()).intValue());
    }

    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                null,
                "type",
                true,
                null,
                As.PROPERTY
        );

        JsonParser jp = factory.createParser("[42]");
        assertInstanceOf(Integer.class, deserializer.deserializeTypedFromAny(jp, new TestContext()));
    }
}

class TestContext extends DeserializationContext {
    @Override
    public JsonDeserializer<?> findValueDeserializer(Class<?> paramType, JsonDeserializer<?> valueDeserializer) {
        return null;
    }

    @Override
    public JsonDeserializer<?> findBeanDeserializer(BeanDescription beanDesc, TypeDeserializer valueDeserializer) {
        return null;
    }

    @Override
    public Object findValueInstantiator(Class<?> beanType) {
        return null;
    }

    @Override
    public Object findBeanInstance(BeanDescription beanDesc) {
        return null;
    }

    @Override
    public TypeIdResolver getTypeIdResolver() {
        return null;
    }

    @Override
    public Object findValueSerializer(Class<?> valueClass, JsonSerializer<?> valueSerializer) {
        return null;
    }

    @Override
    public Object findBeanSerializer(BeanDescription beanDesc, JsonSerializer<?> valueSerializer) {
        return null;
    }

    @Override
    public void reportError(MessageBase message) throws JsonMappingException {
        // do nothing
    }

    @Override
    public void reportWarning(MessageBase message) {
        // do nothing
    }

    @Override
    public void reportDataBindingException(Throwable ex) throws JsonMappingException {
        // do nothing
    }
}