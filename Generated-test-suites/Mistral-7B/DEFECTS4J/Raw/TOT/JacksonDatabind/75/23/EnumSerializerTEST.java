import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeEnum() throws JsonProcessingException {
 EnumSerializer serializer = mapper.findValueSerializer(Enum.class);
 Enum testEnum = MyEnum.FIRST;
 ObjectNode node = mapper.createObjectNode();
 JsonGenerator gen = mapper.getFactory().createGenerator(node);
 serializer.serialize(testEnum, gen, null);
 gen.close();
 assertEquals("FIRST", node.get(0).asText());
 }

 @Test
 public void testSerializeEnumAsIndex() throws JsonProcessingException {
 EnumSerializer serializer = mapper.findValueSerializer(Enum.class);
 ObjectNode node = mapper.createObjectNode();
 JsonGenerator gen = mapper.getFactory().createGenerator(node);
 serializer.serialize(MyEnum.FIRST, gen, null);
 gen.close();
 assertEquals("0", node.get(0).asText());
 }

 @Test
 public void testSerializeEnumWithCustomValues() throws JsonProcessingException {
 EnumSerializer serializer = mapper.findValueSerializer(MyEnum.class);
 ObjectNode node = mapper.createObjectNode();
 JsonGenerator gen = mapper.getFactory().createGenerator(node);
 serializer.serialize(MyEnum.SECOND, gen, null);
 gen.close();
 assertEquals("CUSTOM_VALUE_2", node.get(0).asText());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeEnumWithInvalidFormat() throws JsonProcessingException {
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode node = mapper.createObjectNode();
 EnumSerializer serializer = mapper.findValueSerializer(MyEnum.class);
 serializer.createContextual(mapper.getSerializerProvider(), null);
 serializer.serialize(MyEnum.FIRST, mapper.getFactory().createGenerator(node), mapper.getSerializerProvider());
 fail("Expected JsonMappingException");
 }

 @Test(expected = IllegalStateException.class)
 public void testSerializeEnumWithNullValue() {
 EnumSerializer serializer = mapper.findValueSerializer(Enum.class);
 try {
 serializer.serialize(null, null, null);
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // expected
 }
 }

}

enum MyEnum {
 @JsonValue("FIRST")
 FIRST,
 @JsonValue("SECOND")
 @JsonValue("CUSTOM_VALUE_2")
 SECOND;
}