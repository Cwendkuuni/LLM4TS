package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {

    @Test
    public void testNullifyingDeserializer() throws Exception {
        JsonParser jsonParser = createParser();
        DeserializationContext ctxt = createContext();

        Object result = NullifyingDeserializer.instance.deserialize(jsonParser, ctxt);
        assertNull(result);
    }

    @Test
    public void testNullifyingDeserializerWithType() throws Exception {
        JsonParser jsonParser = createParser();
        DeserializationContext ctxt = createContext();
        TypeDeserializer typeDeserializer = createTypeDeserializer();

        Object result = NullifyingDeserializer.instance.deserializeWithType(jsonParser, ctxt, typeDeserializer);
        assertNull(result);
    }

    @Test
    public void testNullifyingDeserializerWithStartArray() throws Exception {
        JsonParser jsonParser = createParser();
        jsonParser.nextTokenId(JsonTokenId.ID_START_ARRAY.getId());
        DeserializationContext ctxt = createContext();
        TypeDeserializer typeDeserializer = createTypeDeserializer();

        Object result = NullifyingDeserializer.instance.deserializeWithType(jsonParser, ctxt, typeDeserializer);
        assertNull(result);
    }

    @Test
    public void testNullifyingDeserializerWithStartObject() throws Exception {
        JsonParser jsonParser = createParser();
        jsonParser.nextTokenId(JsonTokenId.ID_START_OBJECT.getId());
        DeserializationContext ctxt = createContext();
        TypeDeserializer typeDeserializer = createTypeDeserializer();

        Object result = NullifyingDeserializer.instance.deserializeWithType(jsonParser, ctxt, typeDeserializer);
        assertNull(result);
    }

    @Test
    public void testNullifyingDeserializerWithFieldName() throws Exception {
        JsonParser jsonParser = createParser();
        jsonParser.nextValue("fieldName");
        DeserializationContext ctxt = createContext();
        TypeDeserializer typeDeserializer = createTypeDeserializer();

        JsonDeserializer<Object> deserializer = JsonDeserializer.instance(NullifyingDeserializer.class, Object.class);
        Object result = deserializer.deserialize(jsonParser, ctxt);
        assertNull(result);

        result = NullifyingDeserializer.instance.deserializeWithType(jsonParser, ctxt, typeDeserializer);
        assertNull(result);
    }

    @Test
    public void testNullifyingDeserializerWithOtherTokens() throws Exception {
        JsonParser jsonParser = createParser();

        // Valid JSON tokens
        jsonParser.nextTokenId(JsonTokenId.ID_NULL.getId());
        jsonParser.nextTokenId(JsonTokenId.ID_BOOL.getId());
        jsonParser.nextTokenId(JsonTokenId.ID_NUMBER_INT.getId());
        jsonParser.nextTokenId(JsonTokenId.ID_NUMBER_FLOAT.getId());
        jsonParser.nextTokenId(JsonTokenId.ID_STRING.getId());

        DeserializationContext ctxt = createContext();
        TypeDeserializer typeDeserializer = createTypeDeserializer();

        Object result = NullifyingDeserializer.instance.deserializeWithType(jsonParser, ctxt, typeDeserializer);
        assertNull(result);
    }

    private JsonParser createParser() throws IOException {
        return new TestJsonParser();
    }

    private DeserializationContext createContext() {
        return new TestDeserializationContext();
    }

    private TypeDeserializer createTypeDeserializer() {
        return new TestTypeDeserializer();
    }
}