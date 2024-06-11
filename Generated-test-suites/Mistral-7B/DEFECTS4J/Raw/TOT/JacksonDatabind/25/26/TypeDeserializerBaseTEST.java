package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

    private ObjectMapper mapper;
    private JsonParser parser;
    private TypeDeserializerBase typeDeserializer;

    @Before
    public void setup() throws JsonProcessingException {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        parser = mapper.getFactory().createParser("[]");
        typeDeserializer = new TypeDeserializerBase(TypeDeserializer.NullType.class, null, null, false, null);
    }

    @Test
    public void testNullTypeDeserializer() throws JsonProcessingException {
        assertNull(typeDeserializer.deserialize(parser, null));
        assertNull(typeDeserializer.forProperty(null));
    }

    @Test
    public void testEmptyTypeIdResolver() throws JsonProcessingException {
        TypeDeserializerBase emptyTypeDeserializer = new TypeDeserializerBase(TypeDeserializer.NullType.class, null, null, false, null) {
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };

        assertNull(emptyTypeDeserializer.deserialize(parser, null));
        assertNull(emptyTypeDeserializer.forProperty(null));
    }

    @Test
    public void testTypeIdResolverDeserializer() throws JsonProcessingException {
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(TypeDeserializer.NullType.class, idResolver, null, false, null) {
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return ctxt.findContextualValueDeserializer(getTypeIdResolver().typeFromId(ctxt, typeId), null);
            }
        };

        parser.nextValue(); // consume [
        parser.nextToken(); // read String typeId
        parser.getValueAsString("StringType");

        assertEquals("String", typeDeserializer.deserialize(parser, null).getClass().getSimpleName());
    }

    @Test(expected = JsonMappingException.class)
    public void testUnknownTypeId() throws JsonProcessingException {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(TypeDeserializer.NullType.class, null, null, false, null) {
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }

            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
                throw new JsonMappingException("Unknown type id: " + typeId);
            }
        };

        parser.nextValue(); // consume [
        parser.nextToken(); // read String typeId
        parser.getValueAsString("UnknownTypeId");

        typeDeserializer.deserialize(parser, null);
    }
}