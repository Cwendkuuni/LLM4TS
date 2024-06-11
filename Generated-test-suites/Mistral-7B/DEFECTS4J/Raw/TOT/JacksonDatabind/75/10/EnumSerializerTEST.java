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
 Enum testEnum = Enum.values()[0];
 ObjectNode actual = mapper.treeToObject(serializer.serialize(testEnum, new JsonGenerator(new ByteArrayOutputStream()), new SerializerProvider()));
 ObjectNode expected = mapper.createObjectNode();
 expected.put("value", testEnum.name());
 assertEquals("{\"value\":\"ENUM_VALUE_0\"}", actual.toString());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testSerializeEnumWithCustomToString() throws JsonProcessingException {
 EnumSerializer serializer = mapper.findValueSerializer(MyCustomEnum.class);
 MyCustomEnum testEnum = MyCustomEnum.CUSTOM_VALUE;
 ObjectNode actual = mapper.treeToObject(serializer.serialize(testEnum, new JsonGenerator(new ByteArrayOutputStream()), new SerializerProvider()));
 ObjectNode expected = mapper.createObjectNode();
 expected.put("value", "CUSTOM_VALUE");
 assertEquals("{\"value\":\"CUSTOM_VALUE\"}", actual.toString());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testSerializeEnumAsIndex() throws JsonProcessingException {
 EnumSerializer serializer = mapper.findValueSerializer(Enum.class, new ObjectMapper.SerializerProvider().withFeature(SerializationFeature.WRITE_ENUMS_USING_INDEX, true));
 Enum testEnum = Enum.values()[0];
 ObjectNode actual = mapper.treeToObject(serializer.serialize(testEnum, new JsonGenerator(new ByteArrayOutputStream()), new SerializerProvider()));
 ObjectNode expected = mapper.createObjectNode();
 expected.put("0", testEnum.name());
 assertEquals("{\"0\":\"ENUM_VALUE_0\"}", actual.toString());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeEnum_Null() throws JsonProcessingException {
 EnumSerializer serializer = mapper.findValueSerializer(Enum.class);
 Enum testEnum = null;
 ObjectNode actual = mapper.treeToObject(serializer.serialize(testEnum, new JsonGenerator(new ByteArrayOutputStream()), new SerializerProvider()));
 fail("Expected JsonMappingException");
 }
}