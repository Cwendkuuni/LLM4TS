import static org.junit.Assert.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class StringCollectionDeserializerTest {
  
  private StringCollectionDeserializer deserializer;
  private JsonDeserializer<Object> valueDeserializer;
  private DeserializationContext deserializationContext;
  private JsonParser jsonParser;
  
  @Before
  public void setup() {
    deserializer = new StringCollectionDeserializer();
    valueDeserializer = new JsonDeserializer<Object>();
    deserializationContext = new DeserializationContext(jsonParser);
    jsonParser = new JsonParser();
  }

  @Test
  public void testDeserialize() throws IOException {
    Collection<String> expected = Arrays.asList("one", "two", "three");
    ArrayList<String> actual = new ArrayList<String>();
    jsonParser.setExpectedStartArrayToken(true);
    
    deserializer.deserialize(jsonParser, deserializationContext, actual);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testDeserializeWithNullValue() throws IOException {
    Collection<String> expected = Arrays.asList("one", null, "three");
    ArrayList<String> actual = new ArrayList<String>();
    jsonParser.setExpectedStartArrayToken(true);
    
    deserializer.deserialize(jsonParser, deserializationContext, actual);
    
    assertEquals(expected, actual);
  }
  
  @Test(expected = JsonMappingException.class)
  public void testDeserializeInvalidJson() throws IOException {
    ArrayList<String> actual = new ArrayList<String>();
    jsonParser.setExpectedStartArrayToken(false);
    
    deserializer.deserialize(jsonParser, deserializationContext, actual);
  }
  
  @Test
  public void testDeserializeEmptyArray() throws IOException {
    ArrayList<String> actual = new ArrayList<String>();
    jsonParser.setExpectedStartArrayToken(true);
    jsonParser.setCurrentToken(JsonToken.END_ARRAY);
    
    deserializer.deserialize(jsonParser, deserializationContext, actual);
    
    assertEquals(0, actual.size());
  }
}