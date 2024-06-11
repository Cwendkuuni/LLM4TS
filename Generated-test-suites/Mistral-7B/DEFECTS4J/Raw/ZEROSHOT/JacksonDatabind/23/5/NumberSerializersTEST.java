import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer input = 123;
 String output = "123";

 ObjectWriter writer = objectMapper.writerWithTypeSerializer(Integer.class);
 String serialized = writer.writeValueAsString(input);

 JsonParser parser = objectMapper.getFactory().createParser(serialized);
 Object readValue = parser.readValueAs(Integer.class);

 assertEquals(input, readValue);
 assertEquals(output, serialized);
 }

 @Test
 public void testIntegerSerializerWithJsonFormat() throws JsonProcessingException {
 Integer input = 123;
 String format = "customFormat";

 JsonFormat jsonFormat = new JsonFormat(JsonFormat.Feature.WRITE_NUMBERS_AS_STRINGS, format);
 ObjectWriter writer = objectMapper.writer(JsonFormatCompatibility.Standard.FULL_COMPATIBILITY)
 .withSerializerFactory(new BeanSerializerFactory(objectMapper) {
 @Override
 public JsonSerializer<?> findSerializer(Class<?> type, BeanProperty property, JavaType javaType) {
 if (Integer.class.isAssignableFrom(type)) {
 return new IntegerSerializerWithFormat(jsonFormat);
 }
 return super.findSerializer(type, property, javaType);
 }
 });
 String serialized = writer.writeValueAsString(input);

 JsonParser parser = objectMapper.getFactory().createParser(serialized);
 Object readValue = parser.readValueAs(Integer.class);

 assertEquals(input, readValue);
 assertEquals(output, serialized);
 }

 @Test
 public void testIntegerSerializerContextual() throws JsonProcessingException {
 Integer input = 123;
 ObjectWriter writer = objectMapper.writer().withRootValue(input);
 String serialized = writer.writeValueAsString("");

 JsonParser parser = objectMapper.getFactory().createParser(serialized);
 Object readValue = parser.readValueAs(String.class);
 Object deserializedValue = parser.readValueAs(Integer.class);

 assertEquals(readValue, serialized);
 assertEquals(input, deserializedValue);
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long input = 123L;
 String output = "123";

 ObjectWriter writer = objectMapper.writerWithTypeSerializer(Long.class);
 String serialized = writer.writeValueAsString(input);

 JsonParser parser = objectMapper.getFactory().createParser(serialized);
 Object readValue = parser.readValueAs(Long.class);

 assertEquals(input, readValue);
 assertEquals(output, serialized);
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float input = 123.45f;
 String output = "123.45";

 ObjectWriter writer = objectMapper.writerWithTypeSerializer(Float.class);
 String serialized = writer.writeValueAsString(input);

 JsonParser parser = objectMapper.getFactory().createParser(serialized);
 Object readValue = parser.readValueAs(Float.class);

 assertEquals(input, readValue);
 assertEquals(output, serialized);
 }

 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 Double input = 123.45;
 String output = "123.45";

 ObjectWriter writer = objectMapper.writerWithTypeSerializer(Double.class);
 String serialized = writer.writeValueAsString(input);

 JsonParser parser = objectMapper.getFactory().createParser(serialized);
 Object readValue = parser.readValueAs(Double.class);

 assertEquals(input, readValue);
 assertEquals(output, serialized);
 }
}

class IntegerSerializerWithFormat extends IntegerSerializer {
 IntegerSerializerWithFormat(JsonFormat jsonFormat) {
 super(Integer.class, JsonParser.NumberType.INT, "integer");
 this.addMixInAnnotations(new JsonFormatMixIn(jsonFormat));
 }
}

class JsonFormatMixIn {
 @JsonFormat(shape = JsonFormat.Shape.STRING, serializableAs = Integer.class)
 public static class JsonFormatMixInInt {
 }
}