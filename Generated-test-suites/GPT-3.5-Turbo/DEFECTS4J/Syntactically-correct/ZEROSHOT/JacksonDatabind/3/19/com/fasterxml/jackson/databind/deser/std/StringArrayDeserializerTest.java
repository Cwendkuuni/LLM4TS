import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.mockito.Mockito;

public class StringArrayDeserializerTest {
  
  private StringArrayDeserializer stringArrayDeserializer;
  
  @Before
  public void setUp() {
    stringArrayDeserializer = new StringArrayDeserializer();
  }
  
  @Test
  public void testDeserialize_CustomElementDeserializer_ReturnsStringArray() throws IOException {
    // Arrange
    JsonParser jsonParser = Mockito.mock(JsonParser.class);
    DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
    JsonDeserializer<String> elementDeserializer = Mockito.mock(JsonDeserializer.class);
    stringArrayDeserializer._elementDeserializer = elementDeserializer;
    // Act
    String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
    // Assert
    assertNotNull(result);
    assertEquals(String[].class, result.getClass());
  }
  
  @Test
  public void testDeserialize_NullParser_ReturnsNull() throws IOException {
    // Arrange
    JsonParser jsonParser = Mockito.mock(JsonParser.class);
    DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
    Mockito.when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
    // Act
    String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
    // Assert
    assertNull(result);
  }
  
  @Test
  public void testDeserialize_NullValueDeserializer_ReturnsStringArray() throws IOException {
    // Arrange
    JsonParser jsonParser = Mockito.mock(JsonParser.class);
    DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
    JsonToken[] tokens = new JsonToken[]{JsonToken.VALUE_STRING, JsonToken.VALUE_NULL, JsonToken.END_ARRAY};
    Mockito.when(jsonParser.nextToken()).thenReturn(tokens[0], tokens[1], tokens[2]);
    Mockito.when(jsonParser.getText()).thenReturn("test");
    // Act
    String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
    // Assert
    assertNotNull(result);
    assertEquals(String[].class, result.getClass());
  }
  
  @Test
  public void testDeserialize_CustomValue_ReturnsStringArray() throws IOException {
    // Arrange
    JsonParser jsonParser = Mockito.mock(JsonParser.class);
    DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
    JsonToken[] tokens = new JsonToken[]{JsonToken.VALUE_STRING, JsonToken.VALUE_NULL, JsonToken.END_ARRAY};
    Mockito.when(jsonParser.nextToken()).thenReturn(tokens[0], tokens[1], tokens[2]);
    Mockito.when(jsonParser.getText()).thenReturn("test");
    Mockito.when(stringArrayDeserializer._parseString(jsonParser, deserializationContext)).thenReturn("test");
    // Act
    String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
    // Assert
    assertNotNull(result);
    assertEquals(String[].class, result.getClass());
  }
  
  @Test
  public void testDeserialize_WithContext_ReturnsStringArray() throws IOException {
    // Arrange
    JsonParser jsonParser = Mockito.mock(JsonParser.class);
    DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
    JsonDeserializer<String> contextDeserializer = Mockito.mock(JsonDeserializer.class);
    BeanProperty property = Mockito.mock(BeanProperty.class);
    Mockito.when(stringArrayDeserializer.isDefaultDeserializer(contextDeserializer)).thenReturn(true);
    Mockito.when(stringArrayDeserializer.findConvertingContentDeserializer(deserializationContext, property, contextDeserializer)).thenReturn(null);
    Mockito.when(deserializationContext.findContextualValueDeserializer(Mockito.any(), Mockito.eq(property))).thenReturn(contextDeserializer);
    // Act
    JsonDeserializer<?> result = stringArrayDeserializer.createContextual(deserializationContext, property);
    // Assert
    assertNotNull(result);
    assertEquals(StringArrayDeserializer.class, result.getClass());
  }
  
  @Test
  public void testDeserialize_WithoutContext_ReturnsStringArray() throws IOException {
    // Arrange
    JsonParser jsonParser = Mockito.mock(JsonParser.class);
    DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
    JsonDeserializer<String> contextDeserializer = Mockito.mock(JsonDeserializer.class);
    BeanProperty property = Mockito.mock(BeanProperty.class);
    Mockito.when(stringArrayDeserializer.isDefaultDeserializer(contextDeserializer)).thenReturn(false);
    Mockito.when(stringArrayDeserializer.findConvertingContentDeserializer(deserializationContext, property, contextDeserializer)).thenReturn(contextDeserializer);
    Mockito.when(deserializationContext.handleSecondaryContextualization(contextDeserializer, property)).thenReturn(contextDeserializer);
    // Act
    JsonDeserializer<?> result = stringArrayDeserializer.createContextual(deserializationContext, property);
    // Assert
    assertNotNull(result);
    assertEquals(StringArrayDeserializer.class, result.getClass());
  }
  
  @Test
  public void testDeserializeWithType_ReturnsObject() throws IOException {
    // Arrange
    JsonParser jsonParser = Mockito.mock(JsonParser.class);
    DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
    TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
    // Act
    Object result = stringArrayDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
    // Assert
    assertNotNull(result);
    assertEquals(Object.class, result.getClass());
  }
  
  @Test
  public void testHandleNonArray_WithAcceptSingleValueAsArray_ReturnsStringArray() throws IOException {
    // Arrange
    JsonParser jsonParser = Mockito.mock(JsonParser.class);
    DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
    Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
    // Act
    String[] result = stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext);
    // Assert
    assertNotNull(result);
    assertEquals(String[].class, result.getClass());
  }
  
  @Test(expected = IOException.class)
  public void testHandleNonArray_WithoutAcceptSingleValueAsArray_ThrowsIOException() throws IOException {
    // Arrange
    JsonParser jsonParser = Mockito.mock(JsonParser.class);
    DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
    Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
    Mockito.when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);
    // Act
    String[] result = stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext);
  }
}