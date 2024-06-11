package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {
    private ObjectMapper objectMapper;
    private DeserializationContext deserializationContext;
    private TypeDeserializer typeDeserializer;
    private JsonParser jsonParser;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        deserializationContext = mock(DeserializationContext.class);
        typeDeserializer = mock(TypeDeserializer.class);
        jsonParser = mock(JsonParser.class);
    }

    @Test
    public void testDeserialize_NullInput_ReturnsNull() throws Exception {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.NULL);

        assertNull(NullifyingDeserializer.instance.deserialize(jsonParser, deserializationContext));
        verify(jsonParser).nextToken();
    }

    @Test
    public void testDeserialize_JsonArray_ReturnsNull() throws Exception {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);

        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        nullifyingDeserializer.deserialize(jsonParser, deserializationContext);

        verify(jsonParser).skipChildren();
        verify(jsonParser).nextToken();
        assertNull(NullifyingDeserializer.instance.deserialize(jsonParser, deserializationContext));
    }

    @Test
    public void testDeserialize_JsonObject_ReturnsNull() throws Exception {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);

        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        nullifyingDeserializer.deserialize(jsonParser, deserializationContext);

        verify(jsonParser).skipChildren();
        verify(jsonParser).nextToken();
        assertNull(NullifyingDeserializer.instance.deserialize(jsonParser, deserializationContext));
    }

    @Test
    public void testDeserialize_StringValue_ReturnsNull() throws Exception {
        when(jsonParser.getText()).thenReturn("foo");
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        nullifyingDeserializer.deserialize(jsonParser, deserializationContext);

        verify(jsonParser).nextToken();
        assertNull(NullifyingDeserializer.instance.deserialize(jsonParser, deserializationContext));
    }

    @Test
    public void testDeserialize_NumberIntegerValue_ReturnsNull() throws Exception {
        when(jsonParser.getValueAsInt()).thenReturn(42);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);

        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        nullifyingDeserializer.deserialize(jsonParser, deserializationContext);

        verify(jsonParser).nextToken();
        assertNull(NullifyingDeserializer.instance.deserialize(jsonParser, deserializationContext));
    }

    @Test
    public void testDeserialize_NumberLongValue_ReturnsNull() throws Exception {
        when(jsonParser.getValueAsLong()).thenReturn(42L);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_LONG);

        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        nullifyingDeserializer.deserialize(jsonParser, deserializationContext);

        verify(jsonParser).nextToken();
        assertNull(NullifyingDeserializer.instance.deserialize(jsonParser, deserializationContext));
    }

    @Test
    public void testDeserialize_NumberFloatValue_ReturnsNull() throws Exception {
        when(jsonParser.getValueAsFloat()).thenReturn(3.14f);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_FLOAT);

        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        nullifyingDeserializer.deserialize(jsonParser, deserializationContext);

        verify(jsonParser).nextToken();
        assertNull(NullifyingDeserializer.instance.deserialize(jsonParser, deserializationContext));
    }

    @Test
    public void testDeserialize_NumberDoubleValue_ReturnsNull() throws Exception {
        when(jsonParser.getValueAsDouble()).thenReturn(3.14);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_FLOAT);

        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        nullifyingDeserializer.deserialize(jsonParser, deserializationContext);

        verify(jsonParser).nextToken();
        assertNull(NullifyingDeserializer.instance.deserialize(jsonParser, deserializationContext));
    }

    @Test
    public void testDeserialize_BooleanTrueValue_ReturnsNull() throws Exception {
        when(jsonParser.getValueAsBoolean()).thenReturn(true);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_BOOLEAN_TRUE);

        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        nullifyingDeserializer.deserialize(jsonParser, deserializationContext);

        verify(jsonParser).nextToken();
        assertNull(NullifyingDeserializer.instance.deserialize(jsonParser, deserializationContext));
    }

    @Test
    public void testDeserialize_BooleanFalseValue_ReturnsNull() throws Exception {
        when(jsonParser.getValueAsBoolean()).thenReturn(false);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_BOOLEAN_FALSE);

        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        nullifyingDeserializer.deserialize(jsonParser, deserializationContext);

        verify(jsonParser).nextToken();
        assertNull(NullifyingDeserializer.instance.deserialize(jsonParser, deserializationContext));
    }

    @Test
    public void testDeserialize_NullDeserializer_ReturnsNull() throws Exception {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, null);

        verify(jsonParser).nextToken();
        assertNull(NullifyingDeserializer.instance.deserializeWithType(jsonParser, deserializationContext, null));
    }

    @Test
    public void testDeserializeWithType_JsonArray_ReturnsNull() throws Exception {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);

        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        verify(jsonParser).skipChildren();
        verify(jsonParser).nextToken();
        assertNull(NullifyingDeserializer.instance.deserializeWithType(jsonParser, deserializationContext, typeDeserializer));
    }

    @Test
    public void testDeserializeWithType_JsonObject_ReturnsNull() throws Exception {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);

        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        verify(jsonParser).skipChildren();
        verify(jsonParser).nextToken();
        assertNull(NullifyingDeserializer.instance.deserializeWithType(jsonParser, deserializationContext, typeDeserializer));
    }

    @Test
    public void testDeserializeWithType_StringValue_ReturnsNull() throws Exception {
        when(jsonParser.getText()).thenReturn("foo");
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME);

        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        verify(typeDeserializer).deserializeTypedFromAny(jsonParser, deserializationContext);
        verify(jsonParser).nextToken();
        assertNull(NullifyingDeserializer.instance.deserializeWithType(jsonParser, deserializationContext, typeDeserializer));
    }

    @Test
    public void testDeserializeWithType_NumberValue_ReturnsNull() throws Exception {
        when(jsonParser.getValueAsInt()).thenReturn(42);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);

        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        verify(typeDeserializer).deserializeTypedFromAny(jsonParser, deserializationContext);
        verify(jsonParser).nextToken();
        assertNull(NullifyingDeserializer.instance.deserializeWithType(jsonParser, deserializationContext, typeDeserializer));
    }

    @Test
    public void testDeserializeWithType_BooleanValue_ReturnsNull() throws Exception {
        when(jsonParser.getValueAsBoolean()).thenReturn(true);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_BOOLEAN_TRUE);

        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        verify(typeDeserializer).deserializeTypedFromAny(jsonParser, deserializationContext);
        verify(jsonParser).nextToken();
        assertNull(NullifyingDeserializer.instance.deserializeWithType(jsonParser, deserializationContext, typeDeserializer));
    }
}