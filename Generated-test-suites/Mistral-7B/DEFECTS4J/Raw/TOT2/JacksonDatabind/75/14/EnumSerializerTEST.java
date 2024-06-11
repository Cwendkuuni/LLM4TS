import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

 private EnumSerializer serializer;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 serializer = mapper.findValueSerializer(Enum.class);
 }

 @Test
 public void testSerializeEnum() throws JsonProcessingException {
 // Test serialization with default settings
 Enum testEnum = Enum.VALUES[0];
 String expected = testEnum.name();
 ObjectNode actual = mapper.writeValueAsTree(testEnum, serializer);
 String actualString = actual.toString();
 assertEquals(expected, actualString);

 // Test serialization with index option
 ObjectNode optionsNode = mapper.createObjectNode();
 optionsNode.putBoolean("write_enums_using_index", true);
 JsonSerializer<Object> indexSerializer = mapper.findValueSerializer(Object.class, ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT, optionsNode);
 JsonGenerator gen = mapper.getPolymorphicSerializerProvider().getGenerator();
 gen.setCodecConfig(new JsonCodecConfig(Object.class, indexSerializer));
 ObjectNode serializedAsIndex = mapper.writeValueAsTree(testEnum, indexSerializer);
 int actualIndex = serializedAsIndex.path("@numberInt").asInt();
 assertEquals(testEnum.ordinal(), actualIndex);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeUnknownEnum() throws JsonProcessingException {
 Enum unknownEnum = new Enum("Unknown") {};
 ObjectNode actual = mapper.writeValueAsTree(unknownEnum, serializer);
 fail("Expected JsonMappingException when serializing unknown enum");
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeEnumWithCustomSerialization() throws JsonProcessingException {
 // Assuming there is a custom serializer for this enum
 Enum customEnum = Enum.VALUES[1];
 JsonSerializer<Enum> customSerializer = mapper.findValueSerializer(Enum.class, EnumSerializerCustom.class);
 ObjectNode actual = mapper.writeValueAsTree(customEnum, customSerializer);
 JsonParser parser = mapper.getFactory().createParser(actual.toString());
 JsonToken token = parser.nextToken();
 assertFalse("Expected START_OBJECT but got " + token, token == JsonToken.START_OBJECT);
 fail("Expected JsonMappingException when serializing enum with custom serializer");
 }
}