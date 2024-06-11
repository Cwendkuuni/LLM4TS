import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

 private ObjectMapper mapper;
 private EnumSerializer serializer;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 Class<MyEnum> enumClass = MyEnum.class;
 serializer = EnumSerializer.construct(enumClass, null, null, null);
 }

 @Test
 public void testSerializeEnumAsString() throws JsonProcessingException {
 MyEnum enumValue = MyEnum.VALUE_ONE;
 ObjectNode actual = mapper.writeValueAsTree(enumValue);
 assertEquals("VALUE_ONE", actual.get("value").asText());
 }

 @Test
 public void testSerializeEnumAsIndex() throws JsonProcessingException {
 MyEnum enumValue = MyEnum.VALUE_ONE;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Enum.class).getSerializerProvider().getGenerator();
 serializer = new EnumSerializer(new EnumValues(MyEnum.class), true);
 generator.writeStartObject();
 serializer.serialize(enumValue, generator, null);
 generator.writeEndObject();
 JsonParser parser = mapper.getFactory().createParser(generator.getOutputStreamAsString());
 parser.nextToken(); // NEXT_TOKEN_START_OBJECT
 int index = parser.readValueAs(int.class);
 assertEquals(0, index);
 }

 @Test
 public void testSerializeEnumUsingToString() throws JsonProcessingException {
 ObjectMapper toStringMapper = new ObjectMapper();
 toStringMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
 MyEnum enumValue = MyEnum.VALUE_TWO;
 ObjectNode actual = toStringMapper.writeValueAsTree(enumValue);
 assertEquals("VALUE_TWO", actual.get("value").asText());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeEnumWithInvalidShape() throws JsonProcessingException {
 Class<MyEnumWithInvalidShape> invalidEnumClass = MyEnumWithInvalidShape.class;
 EnumValues enumValues = EnumValues.constructFromName(null, invalidEnumClass);
 EnumSerializer serializer = new EnumSerializer(enumValues, null);
 MyEnumWithInvalidShape invalidEnumValue = MyEnumWithInvalidShape.VALUE_ONE;
 ObjectNode actual = mapper.writeValueAsTree(invalidEnumValue);
 fail("Should have thrown IllegalArgumentException");
 }

}

enum MyEnum {
 @JsonFormat(shape = Shape.STRING)
 VALUE_ONE("ONE"),
 @JsonFormat(shape = Shape.NUMBER)
 VALUE_TWO("TWO"),
 @JsonFormat(shape = Shape.ARRAY)
 VALUE_THREE(new int[] {1, 2, 3}),
 @JsonFormat(shape = Shape.OBJECT)
 VALUE_FOUR(new Object() {});

 String name;

 MyEnum(String name) {
 this.name = name;
 }

 @Override
 public String toString() {
 return name;
 }
}

enum MyEnumWithInvalidShape {
 @JsonFormat(shape = Shape.INVALID)
 VALUE_ONE("ONE");

 String name;

 MyEnumWithInvalidShape(String name) {
 this.name = name;
 }

}