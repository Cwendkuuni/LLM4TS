import static org.junit.Assert.*;

import org.junit.Test;

public class JsonNodeDeserializerTest {

   @Test
   public void testGetNullValue() {
       JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
       assertEquals(NullNode.getInstance(), deserializer.getNullValue());
   }

   @Test
   public void testGetDeserializer() {
       JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
       assertTrue(deserializer instanceof ObjectDeserializer);

       deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
       assertTrue(deserializer instanceof ArrayDeserializer);

       deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
       assertEquals(JsonNodeDeserializer.instance, deserializer);
   }

   @Test
   public void testDeserialize() {
       JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
       ObjectMapper mapper = new ObjectMapper();
       JsonFactory factory = new JsonFactory();
       try {
           JsonParser parser = factory.createParser("{\"key\": \"value\"}");
           DeserializationContext ctxt = mapper.getDeserializationContext();
           JsonNode node = deserializer.deserialize(parser, ctxt);
           assertTrue(node instanceof ObjectNode);
       } catch (Exception e) {
           fail("Exception thrown during deserialization");
       }
   }

   @Test
   public void testObjectDeserializerDeserialize() {
       JsonNodeDeserializer.ObjectDeserializer deserializer = new JsonNodeDeserializer.ObjectDeserializer();
       ObjectMapper mapper = new ObjectMapper();
       JsonFactory factory = new JsonFactory();
       try {
           JsonParser parser = factory.createParser("{\"key\": \"value\"}");
           DeserializationContext ctxt = mapper.getDeserializationContext();
           JsonNode node = deserializer.deserialize(parser, ctxt);
           assertTrue(node instanceof ObjectNode);
       } catch (Exception e) {
           fail("Exception thrown during deserialization");
       }
   }

   @Test
   public void testArrayDeserializerDeserialize() {
       JsonNodeDeserializer.ArrayDeserializer deserializer = new JsonNodeDeserializer.ArrayDeserializer();
       ObjectMapper mapper = new ObjectMapper();
       JsonFactory factory = new JsonFactory();
       try {
           JsonParser parser = factory.createParser("[1, 2, 3]");
           DeserializationContext ctxt = mapper.getDeserializationContext();
           JsonNode node = deserializer.deserialize(parser, ctxt);
           assertTrue(node instanceof ArrayNode);
       } catch (Exception e) {
           fail("Exception thrown during deserialization");
       }
   }
}