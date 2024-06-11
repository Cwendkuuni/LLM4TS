import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testSerializeEnumAsString() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.valuesOf(MyEnum.class));
 ObjectWriter writer = MAPPER.writerWithView(MyEnum.class);
 MyEnum enumValue = MyEnum.VALUE_1;
 String expectedJson = "\"VALUE_1\"";
 String actualJson = writer.writeValueAsString(enumValue);
 assertTrue("Expected string representation of enum value, but got: " + actualJson, actualJson.equals(expectedJson));
 }

 @Test
 public void testSerializeEnumAsIndex() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.valuesOf(MyEnum.class), true);
 ObjectWriter writer = MAPPER.writerWithView(MyEnum.class);
 MyEnum enumValue = MyEnum.VALUE_1;
 int expectedJson = 0;
 String actualJson = writer.writeValueAsString(enumValue);
 // Jackson will serialize enum as index when configured to do so
 // and the enum values are known statically (i.e., not via reflection)
 // In this test, we manually set the serializer to serialize as index and assert the result
 assertNotNull("Expected int representation of enum value, but got null", Integer.parseInt(actualJson));
 assertEquals("Expected index representation of enum value to be: " + expectedJson, expectedJson, Integer.parseInt(actualJson));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeUnknownEnum() throws JsonProcessingException {
 EnumSerializer serializer = new EnumSerializer(EnumValues.valuesOf(MyEnum.class));
 ObjectWriter writer = MAPPER.writerWithView(MyEnum.class);
 MyUnknownEnum unknownEnum = MyUnknownEnum.UNKNOWN;
 String actualJson = writer.writeValueAsString(unknownEnum);
 fail("Expected IllegalArgumentException, but got: " + actualJson);
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

enum MyUnknownEnum {
 UNKNOWN;
}