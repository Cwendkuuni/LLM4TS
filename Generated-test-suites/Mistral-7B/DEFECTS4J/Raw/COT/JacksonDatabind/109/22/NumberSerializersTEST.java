import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testAddAll() throws Exception {
 Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
 NumberSerializers.addAll(allDeserializers);

 assertEquals(6, allDeserializers.size());
 }

 @Test
 public void testIntegerSerializer() throws Exception {
 IntegerSerializer integerSerializer = new NumberSerializers().new IntegerSerializer(Integer.class);

 JsonNode node = mapper.readTree("{\"num\": 42}");
 Integer value = mapper.treeToValue(node.path("num"), integerSerializer.getClass());
 assertEquals(42, value.intValue());
 }

 @Test
 public void testIntLikeSerializer() throws Exception {
 IntLikeSerializer intLikeSerializer = new NumberSerializers().new IntLikeSerializer();

 JsonNode node = mapper.readTree("{\"num\": 42}");
 Number value = mapper.treeToValue(node.path("num"), intLikeSerializer.getClass());
 assertEquals(42, value.intValue());
 }

 @Test
 public void testShortSerializer() throws Exception {
 ShortSerializer shortSerializer = new NumberSerializers().new ShortSerializer();

 JsonNode node = mapper.readTree("{\"num\": -128}");
 Short value = mapper.treeToValue(node.path("num"), shortSerializer.getClass());
 assertEquals(-128, value.shortValue());
 }

 @Test
 public void testLongSerializer() throws Exception {
 LongSerializer longSerializer = new NumberSerializers().new LongSerializer(Long.class);

 JsonNode node = mapper.readTree("{\"num\": 9223372036854775807L}");
 Long value = mapper.treeToValue(node.path("num"), longSerializer.getClass());
 assertEquals(9223372036854775807L, value.longValue());
 }

 @Test
 public void testFloatSerializer() throws Exception {
 FloatSerializer floatSerializer = new NumberSerializers().new FloatSerializer();

 JsonNode node = mapper.readTree("{\"num\": 3.14F}");
 Float value = mapper.treeToValue(node.path("num"), floatSerializer.getClass());
 assertEquals(3.14F, value, 0.001F);
 }

 @Test
 public void testDoubleSerializer() throws Exception {
 DoubleSerializer doubleSerializer = new NumberSerializers().new DoubleSerializer(Double.class);

 JsonNode node = mapper.readTree("{\"num\": 6.02214076E23}");
 Double value = mapper.treeToValue(node.path("num"), doubleSerializer.getClass());
 assertEquals(6.02214076E23, value, 0.00000001);
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidJsonFormat() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"num\": [1, 2, 3]}";
 mapper.readValue(json, NumberSerializers.ShortSerializer.class);
 }

 @Test
 public void testJsonFormatAnnotation() throws Exception {
 JsonFormat jsonFormat = NumberSerializers.ShortSerializer.class.getAnnotation(JsonFormat.class);
 assertNotNull(jsonFormat);
 assertArrayEquals(new String[]{"number"}, jsonFormat.shape());
 }
}