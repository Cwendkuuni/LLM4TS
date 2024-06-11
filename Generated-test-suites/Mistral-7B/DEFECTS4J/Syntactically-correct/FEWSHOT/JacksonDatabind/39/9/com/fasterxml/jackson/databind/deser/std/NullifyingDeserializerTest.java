package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.TypeDeserializer;
import org.junit.Test;
import static org.junit.Assert.assertNull;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();

        assertNull(NullifyingDeserializer.instance.deserialize(parser, context));
    }

    @Test
    public void testDeserializeWithNullContext() throws IOException {
        JsonParser parser = createMockParser();
        assertNull(NullifyingDeserializer.instance.deserialize(parser, null));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithTypeDeserializerNull() throws IOException {
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        TypeDeserializer typeDeserializer = null;

        NullifyingDeserializer.instance.deserializeWithType(parser, context, typeDeserializer);
    }

    @Test
    public void testDeserializeWithTypeWhenArray() throws IOException {
        JsonParser parser = createMockParser(JsonTokenId.ID_START_ARRAY.asInt());
        DeserializationContext context = createMockContext();
        TypeDeserializer typeDeserializer = createMockTypeDeserializer();

        assertNull(NullifyingDeserializer.instance.deserializeWithType(parser, context, typeDeserializer));
    }

    @Test
    public void testDeserializeWithTypeWhenObject() throws IOException {
        JsonParser parser = createMockParser(JsonTokenId.ID_START_OBJECT.asInt());
        DeserializationContext context = createMockContext();
        TypeDeserializer typeDeserializer = createMockTypeDeserializer();

        assertNull(NullifyingDeserializer.instance.deserializeWithType(parser, context, typeDeserializer));
    }

    @Test
    public void testDeserializeWithTypeWhenFieldName() throws IOException {
        JsonParser parser = createMockParser(JsonTokenId.ID_FIELD_NAME.asInt());
        DeserializationContext context = createMockContext();
        TypeDeserializer typeDeserializer = createMockTypeDeserializer();

        assertNull(NullifyingDeserializer.instance.deserializeWithType(parser, context, typeDeserializer));
    }

    @Test
    public void testDeserializeWithTypeWhenOtherToken() throws IOException {
        JsonParser parser = createMockParser(JsonTokenId.ID_VALUE_STRING.asInt());
        DeserializationContext context = createMockContext();
        TypeDeserializer typeDeserializer = createMockTypeDeserializer();

        assertNull(NullifyingDeserializer.instance.deserializeWithType(parser, context, typeDeserializer));
    }

    private JsonParser createMockParser() {
        return createMockParser(-1);
    }

    private JsonParser createMockParser(int tokenId) {
        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentTokenId()).thenReturn(tokenId);
        return parser;
    }

    private DeserializationContext createMockContext() {
        return mock(DeserializationContext.class);
    }

    private TypeDeserializer createMockTypeDeserializer() {
        return mock(TypeDeserializer.class);
    }
}