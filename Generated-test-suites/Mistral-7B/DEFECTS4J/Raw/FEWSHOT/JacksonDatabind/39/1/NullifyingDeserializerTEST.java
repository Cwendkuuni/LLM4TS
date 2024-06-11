package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertNull;

public class NullifyingDeserializerTest {

    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        objectMapper = new ObjectMapper();
    }

    @After
    public void tearDown() throws Exception {
        reset(jsonParser, deserializationContext);
    }

    @Test
    public void testDeserializeNull() throws IOException {
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_NULL.getValue());

        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();

        assertNull(nullifyingDeserializer.deserialize(jsonParser, deserializationContext));
    }

    @Test
    public void testDeserializeArray() throws IOException {
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY.getValue());

        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();

        nullifyingDeserializer.deserialize(jsonParser, deserializationContext);

        verify(jsonParser, times(1)).nextToken();
    }

    @Test
    public void testDeserializeObject() throws IOException {
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT.getValue());

        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();

        nullifyingDeserializer.deserialize(jsonParser, deserializationContext);

        verify(jsonParser, times(1)).nextToken();
    }

    @Test
    public void testDeserializeFieldName() throws IOException {
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME.getValue());
        String fieldName = "testField";
        when(jsonParser.getText()).thenReturn(fieldName);

        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();

        assertNull(nullifyingDeserializer.deserialize(jsonParser, deserializationContext));

        verify(jsonParser, times(1)).nextToken();
        verify(jsonParser, times(1)).getText();
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT.getValue());

        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();

        when(typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext)).thenCallRealMethod();

        nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        verify(typeDeserializer, times(1)).deserializeTypedFromAny(eq(jsonParser), eq(deserializationContext));
    }
}