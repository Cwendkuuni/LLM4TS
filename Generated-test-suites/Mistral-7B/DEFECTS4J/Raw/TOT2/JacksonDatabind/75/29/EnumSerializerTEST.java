import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

 private EnumSerializer serializer;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 ObjectMapper mapper = new ObjectMapper();
 serializer = mapper.findValueSerializer(Enum.class);
 objectMapper = mapper;
 }

 @Test
 public void testSerializeEnumAsString() throws JsonProcessingException {
 EnumSerializer enumSerializer = new EnumSerializer(EnumValues.allValues(MyEnum.class), false);
 MyEnum value = MyEnum.VALUE_1;
 String expected = value.name();
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new StringWriter());
 enumSerializer.serialize(value, jsonGenerator, new SerializerProvider());
 String actual = jsonGenerator.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeEnumAsIndex() throws JsonProcessingException {
 EnumSerializer enumSerializer = new EnumSerializer(EnumValues.allValues(MyEnum.class), true);
 MyEnum value = MyEnum.VALUE_1;
 int expected = value.ordinal();
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new StringWriter());
 enumSerializer.serialize(value, jsonGenerator, new SerializerProvider());
 int actual = Integer.parseInt(jsonGenerator.toString());
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeStringEnum() throws JsonProcessingException {
 String json = ObjectMapperTestUtils.writeValueAsString(MyEnum.VALUE_1);
 MyEnum actual = objectMapper.readValue(json, MyEnum.class);
 MyEnum expected = MyEnum.VALUE_1;
 assertEquals(expected.name(), actual.name());
 }

 @Test
 public void testDeserializeIndexEnum() throws JsonProcessingException {
 String json = ObjectMapperTestUtils.writeValueAsString(0);
 MyEnum actual = objectMapper.readValue(json, MyEnum.class);
 MyEnum expected = MyEnum.VALUE_1;
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidEnum() throws JsonProcessingException {
 String json = "invalidEnumValue";
 MyEnum actual = objectMapper.readValue(json, MyEnum.class);
 fail("Should throw JsonMappingException for invalid enum value");
 }

 @Test
 public void testSerializeEnumWithJsonFormatAnnotations() throws JsonProcessingException {
 MyEnumWithJsonFormat value = MyEnumWithJsonFormat.VALUE_2;
 JsonFormat.Value jsonFormat = new JsonFormat.Value();
 jsonFormat.setShape(JsonFormat.Shape.STRING);
 JsonSerializer<MyEnumWithJsonFormat> serializer = objectMapper.findValueSerializer(MyEnumWithJsonFormat.class);
 serializer = serializer.withResolvedAnnotation(JsonFormat.class).withValue(jsonFormat);
 String expected = "VALUE_2";
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new StringWriter());
 serializer.serialize(value, jsonGenerator, new SerializerProvider());
 String actual = jsonGenerator.toString();
 assertEquals(expected, actual);
 }
}