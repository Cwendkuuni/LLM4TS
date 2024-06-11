import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EnumSerializerTest {

 private EnumSerializer serializer;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 this.objectMapper = mapper;
 EnumSerializer enumSerializer = new EnumSerializer(EnumValues.valueOf(MyEnum.class), null);
 this.serializer = enumSerializer;
 }

 @Test
 public void testSerializeEnum() throws JsonGenerationException {
 MyEnum enumValue = MyEnum.VALUE_ONE;
 ObjectNode result = this.objectMapper.writeValueAsObject(enumValue, new ObjectNode());
 assertEquals("VALUE_ONE", result.get("value").asText());
 }

 @Test
 public void testSerializeEnumWithCustomValues() throws JsonGenerationException {
 MyEnumCustom enumValue = MyEnumCustom.CUSTOM_VALUE_ONE;
 ObjectNode result = this.objectMapper.writeValueAsObject(enumValue, new ObjectNode());
 assertEquals("CUSTOM_VALUE_ONE", result.get("value").asText());
 }

 @Test
 public void testSerializeEnumWithToString() throws JsonGenerationException {
 MyEnumToString enumValue = MyEnumToString.VALUE_ONE;
 ObjectMapper mapperWithToString = new ObjectMapper();
 mapperWithToString.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
 ObjectNode result = mapperWithToString.writeValueAsObject(enumValue, new ObjectNode());
 assertEquals("getValueOne", result.get("value").asText());
 }

 @Test(expected = JsonGenerationException.class)
 public void testSerializeEnumWithNull() throws JsonGenerationException {
 MyEnum nullEnum = null;
 ObjectNode result = this.objectMapper.writeValueAsObject(nullEnum, new ObjectNode());
 assertNull(result);
 }

 @Test(expected = JsonGenerationException.class)
 public void testSerializeEnumWithUnknownValue() throws JsonGenerationException {
 MyEnumUnknown unknownEnum = new MyEnumUnknown("UnknownValue");
 ObjectNode result = this.objectMapper.writeValueAsObject(unknownEnum, new ObjectNode());
 assertNull(result);
 }

 @Test
 public void testDeserializeEnum() throws IOException {
 String json = "{\"value\": \"VALUE_ONE\"}";
 MyEnum enumValue = this.objectMapper.readValue(json, MyEnum.class);
 assertEquals(MyEnum.VALUE_ONE, enumValue);
 }

 @Test
 public void testDeserializeEnumWithCustomValues() throws IOException {
 String json = "{\"value\": \"CUSTOM_VALUE_ONE\"}";
 MyEnumCustom enumValue = this.objectMapper.readValue(json, MyEnumCustom.class);
 assertEquals(MyEnumCustom.CUSTOM_VALUE_ONE, enumValue);
 }

 @Test(expected = IOException.class)
 public void testDeserializeEnumWithNull() throws IOException {
 String json = "{\"value\": null}";
 this.objectMapper.readValue(json, MyEnum.class);
 }

 @Test(expected = IOException.class)
 public void testDeserializeEnumWithUnknownValue() throws IOException {
 String json = "{\"value\": \"UnknownValue\"}";
 this.objectMapper.readValue(json, MyEnum.class);
 }
}

enum MyEnum {
 @JsonFormat(shape = JsonFormat.Shape.STRING, value = "getValueOne")
 VALUE_ONE
}

enum MyEnumCustom {
 @JsonFormat(shape = JsonFormat.Shape.STRING, value = "CUSTOM_VALUE_ONE")
 CUSTOM_VALUE_ONE
}

enum MyEnumToString {
 VALUE_ONE
}

enum MyEnumUnknown {
 UNKNOWN_VALUE
}