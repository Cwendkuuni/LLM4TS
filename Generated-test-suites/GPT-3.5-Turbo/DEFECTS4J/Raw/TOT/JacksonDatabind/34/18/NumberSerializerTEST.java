import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NumberSerializerTest {
  
  private JsonGenerator jsonGenerator;
  private SerializerProvider serializerProvider;
  private NumberSerializer numberSerializer;
  
  @Before
  public void setUp() throws IOException {
    jsonGenerator = new JsonFactory().createGenerator(System.out);
    ObjectMapper mapper = new ObjectMapper();
    serializerProvider = mapper.getSerializerProvider();
    numberSerializer = NumberSerializer.instance;
  }
  
  @Test
  public void testSerialize_integer() throws IOException {
    Number value = 1;
    numberSerializer.serialize(value, jsonGenerator, serializerProvider);
    jsonGenerator.flush();
    // Ensure the JSON output matches the expected output for an integer
    assertEquals("1", outContent.toString());
  }
  
  @Test
  public void testSerialize_long() throws IOException {
    Number value = 100000000000L;
    numberSerializer.serialize(value, jsonGenerator, serializerProvider);
    jsonGenerator.flush();
    // Ensure the JSON output matches the expected output for a long
    assertEquals("100000000000", outContent.toString());
  }
  
  @Test
  public void testSerialize_double() throws IOException {
    Number value = 1.234;
    numberSerializer.serialize(value, jsonGenerator, serializerProvider);
    jsonGenerator.flush();
    // Ensure the JSON output matches the expected output for a double
    assertEquals("1.234", outContent.toString());
  }
  
  @Test
  public void testSerialize_float() throws IOException {
    Number value = 1.23f;
    numberSerializer.serialize(value, jsonGenerator, serializerProvider);
    jsonGenerator.flush();
    // Ensure the JSON output matches the expected output for a float
    assertEquals("1.23", outContent.toString());
  }
  
  @Test
  public void testSerialize_byte() throws IOException {
    Number value = (byte) 8;
    numberSerializer.serialize(value, jsonGenerator, serializerProvider);
    jsonGenerator.flush();
    // Ensure the JSON output matches the expected output for a byte
    assertEquals("8", outContent.toString());
  }
  
  @Test
  public void testSerialize_short() throws IOException {
    Number value = (short) 16;
    numberSerializer.serialize(value, jsonGenerator, serializerProvider);
    jsonGenerator.flush();
    // Ensure the JSON output matches the expected output for a short
    assertEquals("16", outContent.toString());
  }
  
  @Test
  public void testSerialize_bigInteger() throws IOException {
    Number value = new BigInteger("12345678901234567890");
    numberSerializer.serialize(value, jsonGenerator, serializerProvider);
    jsonGenerator.flush();
    // Ensure the JSON output matches the expected output for a BigInteger
    assertEquals("12345678901234567890", outContent.toString());
  }
  
  @Test
  public void testSerialize_bigDecimal() throws IOException {
    Number value = new BigDecimal("3.14159");
    numberSerializer.serialize(value, jsonGenerator, serializerProvider);
    jsonGenerator.flush();
    // Ensure the JSON output matches the expected output for a BigDecimal
    assertEquals("3.14159", outContent.toString());
  }
  
  @Test
  public void testSerialize_otherNumber() throws IOException {
    Number value = (Number) 123;
    numberSerializer.serialize(value, jsonGenerator, serializerProvider);
    jsonGenerator.flush();
    // Ensure the JSON output matches the expected output for other Numbers
    assertEquals("123", outContent.toString());
  }
  
  @Test
  public void testGetSchema_integer() {
    SerializerProvider provider = null;
    Type typeHint = integer.class;
    JsonNode schema = numberSerializer.getSchema(provider, typeHint);
    // Assert that the returned schema is of type integer
    assertEquals("integer", schema.getNodeType().toString());
  }
  
  @Test
  public void testGetSchema_otherType() {
    SerializerProvider provider = null;
    Type typeHint = String.class;
    JsonNode schema = numberSerializer.getSchema(provider, typeHint);
    // Assert that the returned schema is of type number
    assertEquals("number", schema.getNodeType().toString());
  }
  
  @Test
  public void testAcceptJsonFormatVisitor_integer() {
    JsonFormatVisitorWrapper visitor = null;
    JavaType typeHint = null;
    try {
      numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
      // Test should fail if an exception is not thrown
      fail("Expected JsonMappingException was not thrown");
    } catch (JsonMappingException e) {
      // Ensure the exception message is correct
      assertEquals("Cannot expect integer format for a non-integer type", e.getMessage());
    }
  }
  
  @Test
  public void testAcceptJsonFormatVisitor_otherType() {
    JsonFormatVisitorWrapper visitor = null;
    JavaType typeHint = null;
    try {
      numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
      // Test should fail if an exception is not thrown
      fail("Expected JsonMappingException was not thrown");
    } catch (JsonMappingException e) {
      // Ensure the exception message is correct
      assertEquals("Cannot expect number format for an unknown type", e.getMessage());
    }
  }
}