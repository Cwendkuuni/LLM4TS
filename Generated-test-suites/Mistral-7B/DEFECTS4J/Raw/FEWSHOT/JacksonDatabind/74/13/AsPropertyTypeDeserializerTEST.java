package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.core.util.SettableBeanProperty.BeanPropertySetup.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.JSonTypeDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.util.TypeDeserializerBaseTest;

public class AsPropertyTypeDeserializerTest extends TypeDeserializerBaseTest {

    private static final AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
            mapper.getTypeFactory().constructType(String.class), mapper.getTypeIdResolver(), "type", false, null, As.PROPERTY);

    @Override
    protected TypeDeserializer createDeserializer(DeserializationContext ctxt) throws Exception {
        return deserializer;
    }

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"type\":\"String\",\"data\":\"foo\"}");
        JsonToken token = parser.nextToken();
        assertEquals("String", deserializer.getTypeId());
        assertEquals("foo", deserializer.deserializeTypedFromObject(parser, context));

        // test with null input
        parser = mapper.getFactory().createParser("{}");
        token = parser.nextToken();
        assertNull(deserializer.deserializeTypedFromObject(parser, context));
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"@class\":\"java.lang.String\",\"data\":\"foo\"}");
        JsonToken token = parser.nextToken();
        assertEquals("String", deserializer.getTypeId());
        assertEquals("foo", deserializer.deserializeTypedFromObject(parser, context));
    }

    @Test
    public void testDeserializeUsingDefaultImpl() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"data\":[1,2,3]}");
        JsonToken token = parser.nextToken();
        List list = deserializer.deserializeTypedUsingDefaultImpl(parser, context, null);
        assertNotNull(list);
        assertEquals(3, list.size());
        assertInstanceOf(ArrayList.class, list);

        // test with null input
        parser = mapper.getFactory().createParser("{}");
        list = deserializer.deserializeTypedUsingDefaultImpl(parser, context, null);
        assertNull(list);
    }

    @Test
    public void testDeserializeWithPropertyName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.getSerializationConfig().findBeanDescription(String.class);
        PropertyName typePropertyName = beanDesc.getSerializationConfig().findPropertyName("type");

        JsonParser parser = mapper.getFactory().createParser("{\"type\":\"String\",\"data\":\"foo\"}");
        JsonToken token = parser.nextToken();

        // test with null tokenBuffer
        Object result = deserializer.deserializeTypedFromObject(parser, context);
        assertInstanceOf(String.class, result);
        assertEquals("foo", result);

        // test with non-null tokenBuffer
        TokenBuffer tokenBuffer = new TokenBuffer(parser, context);
        deserializer.deserializeTypedForId(parser, context, tokenBuffer);
        Object result2 = parser.readValueAs(String.class);
        assertEquals("foo", result2);
        assertEquals("data", parser.getCurrentName());
        assertEquals("type", tokenBuffer.getFieldNames().nextValue().asText());
        assertEquals("String", result2.getClass().getName());
    }

    @Test(expected = IOException.class)
    public void testMissingProperty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"data\":\"foo\"}");
        deserializer.deserializeTypedFromObject(parser, context);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNonStringType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"type\":1,\"data\":\"foo\"}");
        deserializer.deserializeTypedFromObject(parser, context);
    }
}