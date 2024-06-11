import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider.TypeSerializer;

class NumberSerializersTest {

 @BeforeAll
 static void setUp() {
 ObjectMapper objectMapper = new ObjectMapper();
 // Initialize ObjectMapper with the NumberSerializers
 ObjectNode serializers = objectMapper.createObjectNode();
 NumberSerializers.addAll(serializers);
 objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 objectMapper.setDefaultTyping(DefaultTyping.NON_FINAL);
 objectMapper.setSerializationConfig(objectMapper.getSerializerConfig().withSerializerInitializer(new SerializerInitializer() {
 @Override
 public JsonSerializer<?> findSerializer(Class<?> aClass, JsonSerializerProvider jsonSerializerProvider) {
 return serializers.get(aClass.getName());
 }
 }));
 }

 @AfterEach
 void tearDown() {
 // Clear the ObjectMapper after each test case
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.setSerializationConfig(objectMapper.getSerializerConfig().withResolver(new StandardSerializerFactory(null)));
 }

 @Test
 void testIntegerSerializer_NormalCase() throws JsonProcessingException {
 Integer integer = 1;
 JsonNode result = mapper.writeValueAsTree(integer);
 assertEquals("1", result.asText());
 }

 @Test
 void testIntegerSerializer_NullValue() {
 Integer integer = null;
 assertThrows(JsonMappingException.class, () -> mapper.writeValueAsString(integer));
 }

 @Test
 void testIntegerSerializer_NegativeValue() throws JsonProcessingException {
 Integer integer = -1;
 JsonNode result = mapper.writeValueAsTree(integer);
 assertEquals("-1", result.asText());
 }

 @Test
 void testIntegerSerializer_LargeValue() throws JsonProcessingException {
 Integer integer = Integer.MAX_VALUE;
 JsonNode result = mapper.writeValueAsTree(integer);
 assertEquals(Integer.toString(Integer.MAX_VALUE), result.asText());
 }

 @Test
 void testIntegerSerializer_SerializationWithType() throws JsonProcessingException {
 ObjectNode rootNode = mapper.createObjectNode();
 rootNode.put("value", new Integer(1));
 JsonNode serializedNode = mapper.writeValueAsTree(rootNode);
 ObjectNode expectedNode = mapper.createObjectNode();
 expectedNode.put("value", mapper.valueToTree(1));
 assertEquals(expectedNode, serializedNode);
 }

 @Test
 void testIntegerSerializer_CustomJsonFormat() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addSerializer(Integer.class, new CustomIntegerSerializer());
 objectMapper.registerModule(module);
 JsonNode result = objectMapper.writeValueAsTree(1);
 assertEquals("CUSTOM_INTEGER", result.asText());
 }

 // Add test cases for other methods in the NumberSerializers class similarly

}

class CustomIntegerSerializer extends StdSerializer<Integer> {
 CustomIntegerSerializer() {
 this(null);
 }

 CustomIntegerSerializer(Class<Integer> t) {
 super(t);
 }

 @Override
 public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers)
 throws IOException {
 gen.writeString("CUSTOM_INTEGER");
 }
}