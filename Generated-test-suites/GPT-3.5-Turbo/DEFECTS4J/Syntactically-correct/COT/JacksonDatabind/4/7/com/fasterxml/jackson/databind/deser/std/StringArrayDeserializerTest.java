
import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {
  
   @Test
   public void testDeserialize() throws IOException {
       StringArrayDeserializer deserializer = new StringArrayDeserializer();
       String[] expected = {"value1", "value2", "value3"};
       JsonParser jp = new JsonParser();
       DeserializationContext ctxt = new DeserializationContext();
       
       String[] result = deserializer.deserialize(jp, ctxt);
       
       assertArrayEquals(expected, result);
   }
   
   @Test
   public void testDeserializeCustom() throws IOException {
       StringArrayDeserializer deserializer = new StringArrayDeserializer();
       String[] expected = {"value1", "value2", "value3"};
       JsonParser jp = new JsonParser();
       DeserializationContext ctxt = new DeserializationContext();
       JsonDeserializer<String> deser = new JsonDeserializer<String>();
       deserializer._elementDeserializer = deser;
       
       String[] result = deserializer._deserializeCustom(jp, ctxt);
       
       assertArrayEquals(expected, result);
   }
   
   @Test
   public void testDeserializeWithType() throws IOException {
       StringArrayDeserializer deserializer = new StringArrayDeserializer();
       JsonParser jp = new JsonParser();
       DeserializationContext ctxt = new DeserializationContext();
       TypeDeserializer typeDeserializer = new TypeDeserializer();
       
       Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
       
       assertNull(result);
   }
   
   @Test
   public void testHandleNonArray() throws IOException {
       StringArrayDeserializer deserializer = new StringArrayDeserializer();
       JsonParser jp = new JsonParser();
       DeserializationContext ctxt = new DeserializationContext();
       
       String[] result = deserializer.handleNonArray(jp, ctxt);
       
       assertNull(result);
   }
   
   @Test
   public void testCreateContextual() throws JsonMappingException {
       StringArrayDeserializer deserializer = new StringArrayDeserializer();
       DeserializationContext ctxt = new DeserializationContext();
       BeanProperty property = new BeanProperty();
       
       JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
       
       assertNull(result);
   }
}

