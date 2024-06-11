import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

 @Test
 public void testEnumSerialization() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(MyEnum.class), null);
 ObjectMapper mapper = new ObjectMapper();
 MyEnum enumValue = MyEnum.VALUE_1;

 // Test serialization to JSON
 JsonNode jsonNode = mapper.writeValueAsTree(enumValue, new TypeReference<ObjectNode>() {});
 ObjectNode jsonObject = (ObjectNode) jsonNode;
 String serializedName = jsonObject.get("VALUE_1").asText();
 assertEquals("VALUE_1", serializedName);

 // Test serialization as index
 JsonGenerator gen = mapper.getFactory().createGenerator();
 serializer.serialize(enumValue, gen, null);
 assertEquals("0", gen.getOutputBuffer().toString());
 gen.close();

 // Test serialization to empty String when TO_STRING feature is disabled
 mapper.disable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
 jsonNode = mapper.writeValueAsTree(enumValue);
 assertEquals("", jsonNode.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeWithInvalidShape() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(MyEnum.class), null);
 JsonFormat format = new JsonFormat(Shape.INVALID, null);
 JsonSerializer<MyEnum> serializedSerializer = mapper.findValueSerializer(MyEnum.class, null, null, format);
 assertNotNull(serializedSerializer);
 }
}

enum MyEnum {
 @JsonValue("VALUE_0")
 VALUE_0,
 @JsonValue("VALUE_1")
 VALUE_1,
 @JsonValue("VALUE_2")
 VALUE_2;
}