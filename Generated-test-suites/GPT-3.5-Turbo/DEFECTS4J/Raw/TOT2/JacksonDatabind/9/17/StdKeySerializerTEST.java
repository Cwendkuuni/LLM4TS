import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeySerializerTest {

   @Test
   public void testSerializeDate() {
       StdKeySerializer serializer = new StdKeySerializer();
       Date date = new Date();

       // Test serializing a Date object
       try {
           serializer.serialize(date, null, null);
       } catch (IOException e) {
           fail("Unexpected IOException thrown");
       }
   }

   @Test
   public void testSerializeString() {
       StdKeySerializer serializer = new StdKeySerializer();
       String testString = "test";

       // Test serializing a String
       try {
           serializer.serialize(testString, null, null);
       } catch (IOException e) {
           fail("Unexpected IOException thrown");
       }
   }

   @Test
   public void testGetSchema() {
       StdKeySerializer serializer = new StdKeySerializer();

       // Test getting schema
       try {
           serializer.getSchema(null, null);
       } catch (JsonMappingException e) {
           fail("Unexpected JsonMappingException thrown");
       }
   }

   @Test
   public void testAcceptJsonFormatVisitor() {
       StdKeySerializer serializer = new StdKeySerializer();

       // Test accepting JsonFormatVisitor
       try {
           serializer.acceptJsonFormatVisitor(null, null);
       } catch (JsonMappingException e) {
           fail("Unexpected JsonMappingException thrown");
       }
   }
}