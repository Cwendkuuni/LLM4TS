package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"typePropertyName\": \"MyType\", \"typeId\": \"myTypeId\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class),
                ctxt.getTypeIdResolver(), "typePropertyName", true, mapper.getTypeFactory().constructType(DefaultImplType.class), As.PROPERTY);

        assertEquals(MyType.class, deserializer.deserializeTypedFromObject(parser, ctxt).getClass());
    }

    @Test
    public void testDeserializeTypedFromObjectWithNullTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"typePropertyName\": null}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class),
                ctxt.getTypeIdResolver(), "typePropertyName", true, mapper.getTypeFactory().constructType(DefaultImplType.class), As.PROPERTY);

        assertNull(deserializer.deserializeTypedFromObject(parser, ctxt));
    }

    @Test
    public void testDeserializeTypedFromObjectWithoutTypePropertyName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"typeId\": \"myTypeId\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class),
                ctxt.getTypeIdResolver(), null, true, mapper.getTypeFactory().constructType(DefaultImplType.class), As.PROPERTY);

        assertNull(deserializer.deserializeTypedFromObject(parser, ctxt));
    }

    @Test
    public void testDeserializeTypedFromObjectWithoutTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"typePropertyName\": \"MyType\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class),
                ctxt.getTypeIdResolver(), "typePropertyName", true, mapper.getTypeFactory().constructType(DefaultImplType.class), As.PROPERTY);

        assertNull(deserializer.deserializeTypedFromObject(parser, ctxt));
    }

    @Test
    public void testDeserializeTypedFromObjectWithNativeTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"@type\": \"MyType\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class),
                ctxt.getTypeIdResolver(), null, true, mapper.getTypeFactory().constructType(DefaultImplType.class), As.PROPERTY);

        assertEquals(MyType.class, deserializer.deserializeTypedFromObject(parser, ctxt).getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeTypedFromObjectWithInvalidTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"typePropertyName\": \"MyType\", \"typeId\": \"InvalidTypeId\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class),
                ctxt.getTypeIdResolver(), "typePropertyName", true, mapper.getTypeFactory().constructType(DefaultImplType.class), As.PROPERTY);

        deserializer.deserializeTypedFromObject(parser, ctxt);
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromObjectWithUnexpectedToken() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"typePropertyName\": \"MyType\"}42");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class),
                ctxt.getTypeIdResolver(), "typePropertyName", true, mapper.getTypeFactory().constructType(DefaultImplType.class), As.PROPERTY);

        deserializer.deserializeTypedFromObject(parser, ctxt);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, ctxt.getTypeIdResolver(), null, false, null, As.PROPERTY);

        assertEquals(DefaultImplType[].class, deserializer.deserializeTypedUsingDefaultImpl(parser, ctxt).getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeTypedUsingDefaultImplWhenNotArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"typePropertyName\": \"MyType\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, ctxt.getTypeIdResolver(), "typePropertyName", false, null, As.PROPERTY);

        deserializer.deserializeTypedUsingDefaultImpl(parser, ctxt);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImplWithTokenBuffer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"typePropertyName\": \"MyType\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, ctxt.getTypeIdResolver(), "typePropertyName", false, null, As.PROPERTY);

        TokenBuffer tokenBuffer = new TokenBuffer(parser, ctxt);
        parser = tokenBuffer.asParser(parser);
        parser.nextToken(); // skip typePropertyName

        Object result = deserializer.deserializeTypedUsingDefaultImpl(parser, ctxt);

        assertInstanceOf(DefaultImplType.class, result);
        assertEquals(TokenToken.END_OBJECT.getId(), parser.getCurrentToken().getId());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(MyType.class, new MyTypeSerializer());
        mapper.registerModule(module);

        JsonParser parser = mapper.getFactory().createParser("{\"typePropertyName\": \"MyType\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class),
                ctxt.getTypeIdResolver(), "typePropertyName", true, mapper.getTypeFactory().constructType(DefaultImplType.class), As.PROPERTY);

        JsonDeserializer<MyType> deserializerForMyType = deserializer.findDeserializer(ctxt, "MyType");

        assertInstanceOf(MyTypeDeserializer.class, deserializerForMyType);
    }

    @Test
    public void testFindDeserializerWhenNotFound() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"typePropertyName\": \"UnknownType\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class),
                ctxt.getTypeIdResolver(), "typePropertyName", true, mapper.getTypeFactory().constructType(DefaultImplType.class), As.PROPERTY);

        assertNull(deserializer.findDeserializer(ctxt, "UnknownType"));
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, ctxt.getTypeIdResolver(), null, false, null, As.PROPERTY);

        JsonDeserializer<DefaultImplType[]> deserializerForDefaultImplType = deserializer.findDefaultImplDeserializer(ctxt);

        assertInstanceOf(DefaultImplTypeDeserializer.class, deserializerForDefaultImplType);
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[\"MyType1\", \"MyType2\"]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType[].class),
                ctxt.getTypeIdResolver(), null, true, mapper.getTypeFactory().constructType(DefaultImplType.class), As.PROPERTY);

        Object result = deserializer.deserializeTypedFromArray(parser, ctxt);
        assertInstanceOf(MyType[].class, result.getClass());

        MyType[] myTypes = (MyType[]) result;
        assertEquals(MyType1.class, myTypes[0].getClass());
        assertEquals(MyType2.class, myTypes[1].getClass());
    }

    @Test
    public void testDeserializeTypedFromArrayWithNullTypeIdResolver() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[\"MyType1\", \"MyType2\"]");
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType[].class), null, null, true, null, As.PROPERTY);

        Object result = deserializer.deserializeTypedFromArray(parser, null);
        assertInstanceOf(MyType[].class, result.getClass());

        MyType[] myTypes = (MyType[]) result;
        assertEquals(MyType1.class, myTypes[0].getClass());
        assertEquals(MyType2.class, myTypes[1].getClass());
    }

    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("42");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class),
                ctxt.getTypeIdResolver(), null, true, mapper.getTypeFactory().constructType(DefaultImplType.class), As.PROPERTY);

        assertEquals(42, deserializer.deserializeTypedFromScalar(parser, ctxt).longValue());
    }

    static class DefaultImplType {}
    static class MyTypeDeserializer extends JsonDeserializer<MyType> {
        @Override
        public MyType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return new MyType();
        }
    }
    static class MyTypeSerializer extends JsonSerializer<MyType> {
        @Override
        public void serialize(MyType myType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString("MyType");
        }
    }
    static class MyType {
        // empty class
    }
    static class MyType1 extends MyType {
        // empty class
    }
    static class MyType2 extends MyType {
        // empty class
    }
}