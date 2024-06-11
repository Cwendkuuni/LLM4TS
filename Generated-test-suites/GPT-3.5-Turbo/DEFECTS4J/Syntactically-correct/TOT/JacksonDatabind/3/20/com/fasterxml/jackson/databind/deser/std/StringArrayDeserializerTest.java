import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {
    
    private StringArrayDeserializer stringArrayDeserializer;
    
    @Mock
    private JsonParser jsonParser;
    
    @Mock
    private DeserializationContext deserializationContext;
    
    @Mock
    private JsonDeserializer<String> elementDeserializer;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        stringArrayDeserializer = new StringArrayDeserializer();
    }
    
    @Test
    public void testDeserialize_WithNoElementDeserializerAndEmptyArray_ShouldReturnEmptyArray() throws IOException {
        // Arrange
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        
        // Act
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.length);
        verify(jsonParser, times(1)).isExpectedStartArrayToken();
        verify(jsonParser, times(1)).nextToken();
    }
    
    @Test
    public void testDeserialize_WithNoElementDeserializerAndArrayOfStrings_ShouldReturnArrayOfStrings() throws IOException {
        // Arrange
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jsonParser.getText()).thenReturn("one", "two");
        
        // Act
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("one", result[0]);
        assertEquals("two", result[1]);
        verify(jsonParser, times(1)).isExpectedStartArrayToken();
        verify(jsonParser, times(3)).nextToken();
        verify(jsonParser, times(2)).getText();
    }
    
    @Test
    public void testDeserialize_WithNoElementDeserializer_AndArrayOfNulls_ShouldReturnArrayOfNulls() throws IOException {
        // Arrange
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        
        // Act
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.length);
        assertNull(result[0]);
        verify(jsonParser, times(1)).isExpectedStartArrayToken();
        verify(jsonParser, times(2)).nextToken();
    }
    
    @Test
    public void testDeserialize_WithElementDeserializerAndArrayOfStrings_ShouldReturnArrayOfStrings() throws IOException {
        // Arrange
        stringArrayDeserializer = new StringArrayDeserializer(elementDeserializer);
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(elementDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("one", "two");
        
        // Act
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("one", result[0]);
        assertEquals("two", result[1]);
        verify(jsonParser, times(1)).isExpectedStartArrayToken();
        verify(jsonParser, times(3)).nextToken();
        verify(elementDeserializer, times(2)).deserialize(jsonParser, deserializationContext);
    }
    
    @Test
    public void testDeserialize_WithElementDeserializerAndArrayOfNulls_ShouldReturnArrayOfNulls() throws IOException {
        // Arrange
        stringArrayDeserializer = new StringArrayDeserializer(elementDeserializer);
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        when(elementDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(null);
        
        // Act
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.length);
        assertNull(result[0]);
        verify(jsonParser, times(1)).isExpectedStartArrayToken();
        verify(jsonParser, times(2)).nextToken();
        verify(elementDeserializer, times(1)).deserialize(jsonParser, deserializationContext);
    }
    
    @Test(expected = IOException.class)
    public void testDeserialize_WithNoElementDeserializer_AndInvalidJsonArray_ShouldThrowIOException() throws IOException {
        // Arrange
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        
        // Act
        stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        // Assert (Exception expected)
    }
    
    @Test
    public void testDeserialize_WithNoElementDeserializerAndEmptyStringArrayAsNull_ShouldReturnNull() throws IOException {
        // Arrange
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("");
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        
        // Act
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        // Assert
        assertNull(result);
        verify(jsonParser, times(1)).isExpectedStartArrayToken();
        verify(jsonParser, times(1)).getCurrentToken();
        verify(jsonParser, times(1)).getText();
        verify(deserializationContext, times(1)).isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        verify(deserializationContext, times(1)).isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        verify(jsonParser, times(1)).nextToken();
    }
    
    @Test(expected = IOException.class)
    public void testDeserializeWithType_ShouldThrowIOException() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        
        // Act
        stringArrayDeserializer.deserializeWithType(jsonParser, deserializationContext, null);
        
        // Assert (Exception expected)
    }
    
    @Test
    public void testCreateContextual_WithNoElementDeserializer_ShouldReturnNewInstanceWithElementDeserializer() throws IOException {
        // Arrange
        when(deserializationContext.constructType(String.class)).thenReturn(null);
        when(deserializationContext.findContextualValueDeserializer(null, null)).thenReturn(elementDeserializer);
        when(stringArrayDeserializer.isDefaultDeserializer(elementDeserializer)).thenReturn(false);
        JsonDeserializer<?> mockDeserializer = mock(JsonDeserializer.class);
        when(deserializationContext.handleSecondaryContextualization(elementDeserializer, null)).thenReturn(mockDeserializer);
        
        // Act
        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(deserializationContext, null);
        
        // Assert
        assertNotNull(result);
        assertNotEquals(stringArrayDeserializer, result);
        assertTrue(result instanceof StringArrayDeserializer);
        verify(deserializationContext, times(1)).constructType(String.class);
        verify(deserializationContext, times(1)).findContextualValueDeserializer(null, null);
        verify(deserializationContext, times(1)).handleSecondaryContextualization(elementDeserializer, null);
    }
    
    @Test
    public void testCreateContextual_WithNoElementDeserializer_ShouldReturnNull() throws IOException {
        // Arrange
        when(deserializationContext.constructType(String.class)).thenReturn(null);
        when(deserializationContext.findContextualValueDeserializer(null, null)).thenReturn(elementDeserializer);
        when(stringArrayDeserializer.isDefaultDeserializer(elementDeserializer)).thenReturn(true);
        
        // Act
        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(deserializationContext, null);
        
        // Assert
        assertNull(result);
        verify(deserializationContext, times(1)).constructType(String.class);
        verify(deserializationContext, times(1)).findContextualValueDeserializer(null, null);
    }
    
    @Test
    public void testCreateContextual_WithElementDeserializer_ShouldReturnSameInstance() throws IOException {
        // Arrange
        when(deserializationContext.constructType(String.class)).thenReturn(null);
        when(deserializationContext.findContextualValueDeserializer(null, null)).thenReturn(elementDeserializer);
        when(stringArrayDeserializer.isDefaultDeserializer(elementDeserializer)).thenReturn(false);
        when(stringArrayDeserializer.getElementDeserializer()).thenReturn(elementDeserializer);
        
        // Act
        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(deserializationContext, null);
        
        // Assert
        assertNotNull(result);
        assertEquals(stringArrayDeserializer, result);
        verify(deserializationContext, times(1)).constructType(String.class);
        verify(deserializationContext, times(1)).findContextualValueDeserializer(null, null);
        verify(stringArrayDeserializer, times(1)).getElementDeserializer();
    }
}