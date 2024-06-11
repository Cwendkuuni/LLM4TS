import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

 private ObjectMapper mapper;
 private EnumSerializer serializer;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 serializer = mapper.findValueSerializer(Enum.class);
 }

 @Test
 public void testSerializesEnumByName() throws Exception {
 Enum testEnum = Enum.valueOf(MyEnum.class, "ENUM_VALUE_1");
 ObjectNode result = mapper.writeValueAsObject(testEnum);
 String serializedName = result.get("name").asText();
 assertEquals("ENUM_VALUE_1", serializedName);
 }

 @Test
 public void testSerializesEnumByNameWithJsonFormat() throws Exception {
 MyEnumWithJsonFormat testEnum = MyEnumWithJsonFormat.VALUE_1;
 ObjectNode result = mapper.writeValueAsObject(testEnum);
 String serializedName = result.get("name").asText();
 assertEquals("VALUE_1", serializedName);
 }

 @Test
 public void testSerializesEnumByOrdinal() throws Exception {
 Enum testEnum = Enum.valueOf(MyEnum.class, "ENUM_VALUE_2");
 ObjectNode result = mapper.writeValueAsObject(testEnum);
 int serializedOrdinal = result.get("index").asInt();
 assertEquals(1, serializedOrdinal); // assuming Enum order is 0, 1
 }

 @Test
 public void testSerializesEnumByOrdinalWithJsonFormat() throws Exception {
 MyEnumWithJsonFormat testEnum = MyEnumWithJsonFormat.VALUE_2;
 ObjectNode result = mapper.writeValueAsObject(testEnum);
 int serializedOrdinal = result.get("index").asInt();
 assertEquals(1, serializedOrdinal); // assuming Enum order is 0, 1
 }

 @Test(expected = JsonMappingException.class)
 public void testThrowsJsonMappingExceptionWhenUnsupportedSerializationShape() throws Exception {
 ObjectNode node = mapper.createObjectNode();
 node.put("shape", "ARRAY_WITH_STRING_KEYS");
 ObjectNode root = mapper.readTree(node.toString());
 MyEnumWithJsonFormat testEnum = MyEnumWithJsonFormat.VALUE_1;
 mapper.writeValueAsJson(System.out, serializer, testEnum, new StandardSerializerProvider());
 fail("Expected JsonMappingException when trying to serialize Enum using unsupported 'ARRAY_WITH_STRING_KEYS' shape");
 }
}