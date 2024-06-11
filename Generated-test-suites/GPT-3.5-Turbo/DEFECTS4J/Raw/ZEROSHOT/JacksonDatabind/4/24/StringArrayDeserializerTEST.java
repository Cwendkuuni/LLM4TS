import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

   @Test
   public void testDeserialize() {
      JsonParser jp = null;
      DeserializationContext ctxt = null;
      StringArrayDeserializer instance = new StringArrayDeserializer();

      try {
         String[] result = instance.deserialize(jp, ctxt);
         assertArrayEquals(null, result);
      }
      catch(IOException e){
         fail("IOException thrown");
      }
   }

   @Test
   public void testDeserializeWithType() {
      JsonParser jp = null;
      DeserializationContext ctxt = null;
      TypeDeserializer typeDeserializer = null;
      StringArrayDeserializer instance = new StringArrayDeserializer();

      try {
         Object result = instance.deserializeWithType(jp, ctxt, typeDeserializer);
         assertNull(result);
      }
      catch(IOException e){
         fail("IOException thrown");
      }
   }
}