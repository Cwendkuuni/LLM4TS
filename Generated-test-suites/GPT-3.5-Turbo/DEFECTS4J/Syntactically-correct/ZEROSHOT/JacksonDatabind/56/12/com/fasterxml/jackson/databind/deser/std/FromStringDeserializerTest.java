import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;

public class FromStringDeserializerTest {
  
  private FromStringDeserializer testDeserializer;

  @Before
  public void setup() {
    testDeserializer = new FromStringDeserializer<TestModel>(TestModel.class) {
      protected TestModel _deserialize(String value, DeserializationContext ctxt) {
        TestModel testModel = new TestModel();
        testModel.setValue(value);
        return testModel;
      }
    };
  }

  @Test
  public void testDeserialize_success() throws Exception {
    JsonParser p = new JsonParser();
    DeserializationContext ctxt = new DeserializationContext();
    p.setCurrentToken(JsonToken.VALUE_STRING);
    p.setValueAsString("testValue");
    TestModel result = testDeserializer.deserialize(p, ctxt);
    assertNotNull(result);
    assertEquals("testValue", result.getValue());
  }

  @Test(expected = JsonMappingException.class)
  public void testDeserialize_invalidValue() throws Exception {
    JsonParser p = new JsonParser();
    DeserializationContext ctxt = new DeserializationContext();
    p.setCurrentToken(JsonToken.VALUE_STRING);
    p.setValueAsString("invalid");
    testDeserializer.deserialize(p, ctxt);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeserialize_emptyString() throws Exception {
    JsonParser p = new JsonParser();
    DeserializationContext ctxt = new DeserializationContext();
    p.setCurrentToken(JsonToken.VALUE_STRING);
    p.setValueAsString("");
    testDeserializer.deserialize(p, ctxt);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeserialize_nullValue() throws Exception {
    JsonParser p = new JsonParser();
    DeserializationContext ctxt = new DeserializationContext();
    p.setCurrentToken(JsonToken.VALUE_NULL);
    testDeserializer.deserialize(p, ctxt);
  }

  @Test(expected = JsonMappingException.class)
  public void testDeserialize_nonStringValue() throws Exception {
    JsonParser p = new JsonParser();
    DeserializationContext ctxt = new DeserializationContext();
    p.setCurrentToken(JsonToken.VALUE_NUMBER_INT);
    p.setValueAsString("123");
    testDeserializer.deserialize(p, ctxt);
  }
}