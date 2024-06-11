import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Before;
import org.junit.Test;

public class StringArrayDeserializerTest {

    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;

    @Before
    public void setUp() {
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
    }

    @Test
    public void testDeserialize_NullJsonToken() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        // Assert that the result is null
        assertEquals(null, result);
    }

    @Test
    public void testDeserialize_ElementDeserializerIsNull() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        // Assert that the result is an empty array
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserialize_WithCustomElementDeserializer() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        
        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer(elementDeserializer);
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        // Assert that the result is an empty array
        assertArrayEquals(new String[0], result);
        verify(elementDeserializer, times(0)).deserialize(any(JsonParser.class), any(DeserializationContext.class));
    }

    @Test
    public void testDeserialize_CustomElementDeserializer() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING).thenReturn(JsonToken.END_ARRAY);
        when(jsonParser.getText()).thenReturn("value");
        
        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
        when(elementDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("custom value");
        
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer(elementDeserializer);
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        // Assert that the result is an array containing the custom value
        assertArrayEquals(new String[]{"custom value"}, result);
        verify(elementDeserializer, times(1)).deserialize(any(JsonParser.class), any(DeserializationContext.class));
    }

    @Test
    public void testDeserialize_WithNullValue() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL).thenReturn(JsonToken.END_ARRAY);
        
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        // Assert that the result is an array containing null
        assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testDeserialize_WithEmptyStringAsNullObjectEnabled() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING).thenReturn(JsonToken.END_ARRAY);
        when(jsonParser.getText()).thenReturn("");
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);
        
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        // Assert that the result is null
        assertArrayEquals(null, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_WithAcceptSingleValueAsArrayDisabled() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);
        
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
    }

    private boolean isDefaultDeserializer(JsonDeserializer<?> deserializer) {
        // implementation of the method
    }
}