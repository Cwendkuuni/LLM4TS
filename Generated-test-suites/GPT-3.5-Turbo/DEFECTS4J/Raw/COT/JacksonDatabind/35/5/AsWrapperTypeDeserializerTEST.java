import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;

public class AsWrapperTypeDeserializerTest {
  
  @Test
  public void testDeserializeTypedFromObject() throws IOException {
    // Arrange
    AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
    JsonParser jp = new JsonFactory().createParser("{}");
    DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    
    // Act
    Object result = deserializer.deserializeTypedFromObject(jp, ctxt);
    
    // Assert
    assertNull(result);
  }
    
  @Test
  public void testDeserializeTypedFromArray() throws IOException {
    // Arrange
    AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
    JsonParser jp = new JsonFactory().createParser("[]");
    DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    
    // Act
    Object result = deserializer.deserializeTypedFromArray(jp, ctxt);
    
    // Assert
    assertNull(result);
  }
    
  @Test
  public void testDeserializeTypedFromScalar() throws IOException {
    // Arrange
    AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
    JsonParser jp = new JsonFactory().createParser("null");
    DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    
    // Act
    Object result = deserializer.deserializeTypedFromScalar(jp, ctxt);
    
    // Assert
    assertNull(result);
  }
    
  @Test
  public void testDeserializeTypedFromAny() throws IOException {
    // Arrange
    AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
    JsonParser jp = new JsonFactory().createParser("null");
    DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    
    // Act
    Object result = deserializer.deserializeTypedFromAny(jp, ctxt);
    
    // Assert
    assertNull(result);
  }
    
  @Test(expected = JsonParseException.class)
  public void testDeserializeTypedFromObject_InvalidInputValue() throws IOException {
    // Arrange
    AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
    JsonParser jp = new JsonFactory().createParser("2");
    DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    
    // Act
    deserializer.deserializeTypedFromObject(jp, ctxt);
  }
    
  @Test(expected = JsonParseException.class)
  public void testDeserializeTypedFromArray_InvalidInputValue() throws IOException {
    // Arrange
    AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
    JsonParser jp = new JsonFactory().createParser("\"test\"");
    DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    
    // Act
    deserializer.deserializeTypedFromArray(jp, ctxt);
  }
    
  @Test(expected = JsonParseException.class)
  public void testDeserializeTypedFromScalar_InvalidInputValue() throws IOException {
    // Arrange
    AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
    JsonParser jp = new JsonFactory().createParser("{test}");
    DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    
    // Act
    deserializer.deserializeTypedFromScalar(jp, ctxt);
  }
    
  @Test(expected = JsonParseException.class)
  public void testDeserializeTypedFromAny_InvalidInputValue() throws IOException {
    // Arrange
    AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
    JsonParser jp = new JsonFactory().createParser("[test]");
    DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    
    // Act
    deserializer.deserializeTypedFromAny(jp, ctxt);
  }
  
  @Test
  public void testDeserializeTypedFromObject_TypeIdVisible() throws IOException {
    // Arrange
    AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, "type", true, null);
    JsonParser p = JsonParserSequence.createParser("{}", null);
    DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    
    // Act
    Object result = deserializer.deserializeTypedFromObject(p, ctxt);
    
    // Assert
    assertNull(result);
  }
  
  @Test(expected = JsonParseException.class)
  public void testDeserializeTypedFromObject_TypeIdVisible_InvalidInput() throws IOException {
    // Arrange
    AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, "type", true, null);
    JsonParser p = JsonParserSequence.createParser("{", null);
    DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    
    // Act
    deserializer.deserializeTypedFromObject(p, ctxt);
  }
  
  @Test
  public void testDeserializeTypedFromObject_TypeIdRead() throws IOException {
    // Arrange
    AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
    JsonParser p = JsonParserSequence.createParser("{\"type\":\"test\"}", null);
    DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    
    // Act
    Object result = deserializer.deserializeTypedFromObject(p, ctxt);
    
    // Assert
    assertNull(result);
  }
  
  @Test(expected = JsonParseException.class)
  public void testDeserializeTypedFromObject_TypeIdRead_InvalidInput() throws IOException {
    // Arrange
    AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
    JsonParser p = JsonParserSequence.createParser("{\"type\":}", null);
    DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    
    // Act
    deserializer.deserializeTypedFromObject(p, ctxt);
  }
  
  @Test(expected = JsonParseException.class)
  public void testDeserializeTypedFromObject_MissingClosingObject() throws IOException {
    // Arrange
    AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
    JsonParser p = JsonParserSequence.createParser("{\"type\":\"test\"", null);
    DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    
    // Act
    deserializer.deserializeTypedFromObject(p, ctxt);
  }
  
  @Test(expected = JsonParseException.class)
  public void testDeserializeTypedFromObject_MissingClosingObject_InvalidInput() throws IOException {
    // Arrange
    AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
    JsonParser p = JsonParserSequence.createParser("{\"type\":\"test\",\"value\":", null);
    DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    
    // Act
    deserializer.deserializeTypedFromObject(p, ctxt);
  }
  
  @Test(expected = JsonParseException.class)
  public void testDeserializeTypedFromObject_MissingClosingObject_InvalidInput2() throws IOException {
    // Arrange
    AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
    JsonParser p = JsonParserSequence.createParser("{\"type\":\"test\",\"value\":\"test\"", null);
    DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    
    // Act
    deserializer.deserializeTypedFromObject(p, ctxt);
  }
  
  @Test
  public void testDeserializeTypedFromObject_MergeTypeId() throws IOException {
    // Arrange
    AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, "type", true, null);
    JsonParser p = JsonParserSequence.createParser("{\"value\":\"test\"}", null);
    DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    
    // Act
    Object result = deserializer.deserializeTypedFromObject(p, ctxt);
    
    // Assert
    assertNull(result);
  }
  
  @Test(expected = JsonParseException.class)
  public void testDeserializeTypedFromObject_MergeTypeId_InvalidInput() throws IOException {
    // Arrange
    AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, "type", true, null);
    JsonParser p = JsonParserSequence.createParser("{\"value\":", null);
    DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
    
    // Act
    deserializer.deserializeTypedFromObject(p, ctxt);
  }
}