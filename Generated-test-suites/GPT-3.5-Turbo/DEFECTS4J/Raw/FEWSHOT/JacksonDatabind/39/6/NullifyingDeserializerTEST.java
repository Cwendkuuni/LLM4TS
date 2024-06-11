package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.mock;

public class NullifyingDeserializerTest {

    private NullifyingDeserializer nullifyingDeserializer;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private TypeDeserializer typeDeserializer;

    @Before
    public void setup() {
        nullifyingDeserializer = NullifyingDeserializer.instance;
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        typeDeserializer = mock(TypeDeserializer.class);
    }

    @Test
    public void testDeserialize() throws IOException {
        Object result = nullifyingDeserializer.deserialize(jsonParser, deserializationContext);
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeStartArray() throws IOException {
        Mockito.when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);

        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeStartObject() throws IOException {
        Mockito.when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);

        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeFieldName() throws IOException {
        Mockito.when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME);

        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        Assert.assertNull(result);
    }
}