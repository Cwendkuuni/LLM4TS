import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {

 private ObjectMapper objectMapper;
 private StdDelegatingSerializer delegatingSerializer;
 private JsonSerializer<String> stringSerializer;
 private Converter<String, Integer> converter;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 stringSerializer = objectMapper.findValueSerializer(String.class);
 converter = new SimpleTypeSerializer.Std(ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Integer.class));
 delegatingSerializer = new StdDelegatingSerializer(converter);
 }

 @Test
 public void testSerializationWithDelegateSerializer() throws JsonGenerationException, IOException {
 ObjectNode input = ObjectMapper.getDefaultInstance().createObjectNode();
 input.put("delegateValue", "Hello World");
 JsonNode output = objectMapper.writeValueAsNode(delegatingSerializer, input);
 assertEquals("Hello World", output.path("delegateValue").asText());
 }

 @Test
 public void testSerializationWithCustomConverter() throws JsonGenerationException, IOException {
 ObjectNode input = ObjectMapper.getDefaultInstance().createObjectNode();
 input.put("inputValue", "123");
 ObjectNode expectedOutput = ObjectMapper.getDefaultInstance().createObjectNode();
 expectedOutput.put("delegateValue", 123);
 JsonNode output = objectMapper.writeValueAsNode(delegatingSerializer, input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testSerializationWithDynamicDelegateSerializer() throws JsonGenerationException, IOException {
 ObjectNode input = ObjectMapper.getDefaultInstance().createObjectNode();
 input.put("inputValue", "456");
 JsonNode output = objectMapper.writeValueAsNode(delegatingSerializer, input);
 JsonSerializer<?> delegateSerializer = objectMapper.findValueSerializer(Integer.class);
 delegatingSerializer = delegatingSerializer.withDelegate(converter, null, delegateSerializer);
 assertEquals(delegateSerializer.getClass().getName(), output.getClass().getName());
 }

 @Test
 public void testSerializationWithValueIsEmpty() throws JsonGenerationException, IOException {
 ObjectNode input = ObjectMapper.getDefaultInstance().createObjectNode();
 ObjectNode output = objectMapper.writeValueAsNode(delegatingSerializer, input);
 assertTrue(output.isNull());
 }

 @Test
 public void testGetSchema() {
 JsonNode schema = delegatingSerializer.getSchema(objectMapper.getSerializerProvider(), null);
 assertNotNull(schema);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetSchemaWithInvalidType() {
 JsonNode schema = delegatingSerializer.getSchema(objectMapper.getSerializerProvider(), new TypeReference<Object>() {});
 fail("Expected JsonMappingException but got " + schema);
 }

}