import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionDeserializerTest {

  private CollectionDeserializer deserializer;

  @Before
  public void setup() {
    deserializer = new CollectionDeserializer();
  }
  
  @Test
  public void testDeserialize_EmptyArray_CorrectOutput() {
    JsonParser jsonParser = new JsonParser("[ ]");
    DeserializationContext context = new DeserializationContext();
    Collection<Object> result = deserializer.deserialize(jsonParser, context);
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
  
  @Test
  public void testDeserialize_SingleValue_CorrectOutput() {
    JsonParser jsonParser = new JsonParser("[ 123 ]");
    DeserializationContext context = new DeserializationContext();
    Collection<Object> result = deserializer.deserialize(jsonParser, context);
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(123, result.get(0));
  }
  
  @Test
  public void testDeserialize_NullValue_CorrectOutput() {
    JsonParser jsonParser = new JsonParser("[ null ]");
    DeserializationContext context = new DeserializationContext();
    Collection<Object> result = deserializer.deserialize(jsonParser, context);
    assertNotNull(result);
    assertEquals(1, result.size());
    assertNull(result.get(0));
  }
  
  @Test
  public void testDeserialize_MultipleValues_CorrectOutput() {
    JsonParser jsonParser = new JsonParser("[ 1, 2, 3, 4, 5 ]");
    DeserializationContext context = new DeserializationContext();
    Collection<Object> result = deserializer.deserialize(jsonParser, context);
    assertNotNull(result);
    assertEquals(5, result.size());
    assertTrue(result.contains(1));
    assertTrue(result.contains(2));
    assertTrue(result.contains(3));
    assertTrue(result.contains(4));
    assertTrue(result.contains(5));
  }
  
  @Test
  public void testDeserialize_InvalidInput_ThrowsException() {
    JsonParser jsonParser = new JsonParser("{ }");
    DeserializationContext context = new DeserializationContext();
    try{
      Collection<Object> result = deserializer.deserialize(jsonParser, context);
      fail("Expected exception not thrown");
    } catch(JsonMappingException ex) {
      // Exception expected
    }
  }
  
  @Test
  public void testDeserialize_EmptyString_CorrectOutput() {
    JsonParser jsonParser = new JsonParser("[ \"\" ]");
    DeserializationContext context = new DeserializationContext();
    Collection<Object> result = deserializer.deserialize(jsonParser, context);
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("", result.get(0));
  }

}