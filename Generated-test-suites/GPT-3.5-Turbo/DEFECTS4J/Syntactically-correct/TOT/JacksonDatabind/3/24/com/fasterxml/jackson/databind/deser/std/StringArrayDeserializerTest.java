import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class StringArrayDeserializerTest {

  private StringArrayDeserializer deserializer;

  @Before
  public void setUp() {
    deserializer = new StringArrayDeserializer();
  }

  @Test
  public void testDeserialize_withValidStrings_returnsArrayOfStrings() throws Exception {
    String input = "[\"hello\", \"world\", \"foo\", \"bar\"]";
    JsonParser jp = createJsonParser(input);
    DeserializationContext ctxt = createDeserializationContext();
    
    String[] result = deserializer.deserialize(jp, ctxt);

    assertNotNull(result);
    assertEquals(4, result.length);
    assertEquals("hello", result[0]);
    assertEquals("world", result[1]);
    assertEquals("foo", result[2]);
    assertEquals("bar", result[3]);
  }

  @Test
  public void testDeserialize_withEmptyArray_returnsEmptyArray() throws Exception {
    String input = "[]";
    JsonParser jp = createJsonParser(input);
    DeserializationContext ctxt = createDeserializationContext();

    String[] result = deserializer.deserialize(jp, ctxt);

    assertNotNull(result);
    assertEquals(0, result.length);
  }

  @Test
  public void testDeserialize_withNullValue_returnsNull() throws Exception {
    String input = "null";
    JsonParser jp = createJsonParser(input);
    DeserializationContext ctxt = createDeserializationContext();

    String[] result = deserializer.deserialize(jp, ctxt);
    
    assertNull(result);
  }

  @Test
  public void testDeserialize_withNonArrayValue_throwsException() throws Exception {
    String input = "\"hello\"";
    JsonParser jp = createJsonParser(input);
    DeserializationContext ctxt = createDeserializationContext();

    try {
      deserializer.deserialize(jp, ctxt);
      fail("Expected IOException to be thrown");
    } catch (IOException e) {
      // Expected
    }
  }

  private JsonParser createJsonParser(String input) throws Exception {
    // Create the JSON parser using a mock implementation
    JsonParser jp = createMockJsonParser();
    // Set up the JSON input
    expect(jp.getText()).andReturn(input).anyTimes();
    expect(jp.nextToken()).andReturn(JsonToken.START_ARRAY).once();
    expect(jp.nextToken()).andReturn(JsonToken.END_ARRAY).once();
    replay(jp);
    return jp;
  }

  private DeserializationContext createDeserializationContext() {
    // Create the deserialization context using a mock implementation
    DeserializationContext ctxt = createMockDeserializationContext();
    // Set up the deserialization configuration
    expect(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).andReturn(true).anyTimes();
    replay(ctxt);
    return ctxt;
  }

  private JsonParser createMockJsonParser() {
    // Create a mock implementation of JsonParser interface
    return createMock(JsonParser.class);
  }

  private DeserializationContext createMockDeserializationContext() {
    // Create a mock implementation of DeserializationContext interface
    return createMock(DeserializationContext.class);
  }
}