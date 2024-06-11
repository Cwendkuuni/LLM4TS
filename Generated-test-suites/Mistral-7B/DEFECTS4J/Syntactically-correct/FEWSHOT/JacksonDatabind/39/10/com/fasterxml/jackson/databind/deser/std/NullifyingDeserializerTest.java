package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_VALUE_STRING.getId());

        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        Object result = nullifyingDeserializer.deserialize(jsonParser, deserializationContext);
        assertNull(result);

        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT.getId());
        result = nullifyingDeserializer.deserialize(jsonParser, deserializationContext);
        assertNull(result);

        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY.getId());
        result = nullifyingDeserializer.deserialize(jsonParser, deserializationContext);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_VALUE_STRING.getId());

        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertNull(result);

        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY.getId());
        result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertNull(result);

        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT.getId());
        result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertNull(result);

        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME.getId());
        result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertNull(result);

        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_NULL.getId());
        result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertNull(result);

        when(typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext)).thenReturn("test");
        result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertNull(result);
    }
}