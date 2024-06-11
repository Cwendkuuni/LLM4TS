import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        //Arrange
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        //Act
        String[] result = deserializer.deserialize(jp, ctxt);

        //Assert
        assertNull(result);
    }

    @Test
    public void testDeserialize_CustomDeserializer() throws IOException {
        //Arrange
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer._elementDeserializer = mock(JsonDeserializer.class);

        //Act
        String[] result = deserializer.deserialize(jp, ctxt);

        //Assert
        assertNull(result);
    }

    @Test
    public void testDeserialize_CustomDeserializer_WithValues() throws IOException {
        //Arrange
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer._elementDeserializer = mock(JsonDeserializer.class);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);

        //Act
        String[] result = deserializer.deserialize(jp, ctxt);

        //Assert
        assertNotNull(result);
        assertEquals(1, result.length);
        assertNull(result[0]);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        //Arrange
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        //Act
        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);

        //Assert
        assertNull(result);
    }

    @Test
    public void testHandleNonArray_DisabledAcceptSingleValueAsArray() throws IOException {
        //Arrange
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);

        //Act & Assert
        try {
            deserializer.handleNonArray(jp, ctxt);
            fail("Expected MappingException");
        } catch (JsonMappingException ex) {
            //Expected exception
        }
    }

    @Test
    public void testHandleNonArray_AcceptSingleValueAsArray_NullToken() throws IOException {
        //Arrange
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        //Act
        String[] result = deserializer.handleNonArray(jp, ctxt);

        //Assert
        assertNotNull(result);
        assertEquals(1, result.length);
        assertNull(result[0]);
    }

    @Test
    public void testHandleNonArray_AcceptSingleValueAsArray_StringToken_EmptyStringAllowed() throws IOException {
        //Arrange
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("");

        //Act
        String[] result = deserializer.handleNonArray(jp, ctxt);

        //Assert
        assertNull(result);
    }

    @Test
    public void testHandleNonArray_AcceptSingleValueAsArray_StringToken_EmptyStringNotAllowed() throws IOException {
        //Arrange
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("");

        //Act & Assert
        try {
            deserializer.handleNonArray(jp, ctxt);
            fail("Expected MappingException");
        } catch (JsonMappingException ex) {
            //Expected exception
        }
    }

    @Test
    public void testHandleNonArray_AcceptSingleValueAsArray_StringToken_ValidString() throws IOException {
        //Arrange
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("TestString");

        //Act
        String[] result = deserializer.handleNonArray(jp, ctxt);

        //Assert
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("TestString", result[0]);
    }

    @Test
    public void testCreateContextual_WithContentConverter() throws IOException {
        //Arrange
        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        when(ctxt.findContextualValueDeserializer(ctxt.constructType(String.class), property)).thenReturn(deser);
        when(ctxt.handleSecondaryContextualization(deser, property)).thenReturn(deser);

        //Act
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);

        //Assert
        assertEquals(deser, result);
    }

    @Test
    public void testCreateContextual_WithDefaultDeserializer() throws IOException {
        //Arrange
        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        when(ctxt.findContextualValueDeserializer(ctxt.constructType(String.class), property)).thenReturn(deserializer);

        //Act
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);

        //Assert
        assertEquals(deserializer, result);
    }

    @Test
    public void testCreateContextual_DefaultDeserializer() throws IOException {
        //Arrange
        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        when(ctxt.findContextualValueDeserializer(ctxt.constructType(String.class), property)).thenReturn(deser);
        when(deserializer.isDefaultDeserializer(deser)).thenReturn(true);

        //Act
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);

        //Assert
        assertNull(result);
    }

}