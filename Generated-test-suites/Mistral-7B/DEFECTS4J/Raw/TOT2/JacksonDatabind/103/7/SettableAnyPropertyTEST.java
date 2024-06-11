import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

 private SettableAnyProperty settableAnyProperty;
 private Object sampleInstance;

 @Before
 public void setup() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JavaType javaType = mapper.getTypeFactory().constructMapType(Map.class, Object.class, Object.class);
 settableAnyProperty = new SettableAnyProperty(
 new BeanProperty.Standardizer().standardize(new AnnotatedField(Map.class.getDeclaredField("entrySet"))),
 new AnnotatedField(Map.class, "entrySet"), javaType, null, null, null);
 sampleInstance = new HashMap<>();
 }

 @Test
 public void testBasicUseCase() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonNode node = mapper.readTree("{\"key\":\"value\"}");
 SettableAnyProperty.deserializeAndSet(settableAnyProperty, node, null, sampleInstance, "root");
 Map<Object, Object> map = (Map<Object, Object>) sampleInstance;
 assertEquals("value", map.get("key"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullValue() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonNode node = mapper.readTree("null");
 SettableAnyProperty.deserializeAndSet(settableAnyProperty, node, null, sampleInstance, "root");
 fail("Expected IllegalArgumentException but got none");
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidType() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonNode node = mapper.readTree("{\"key\":1}");
 SettableAnyProperty.deserializeAndSet(settableAnyProperty, node, null, sampleInstance, "root");
 fail("Expected JsonMappingException but got none");
 }

 @Test(expected = IOException.class)
 public void testIOException() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonNode node = mapper.readTree("{\"key\":\"value\"}");
 ObjectWriter writer = mapper.writer().withContext(new JsonMappingContext.Builder(mapper.getFactory()).build());
 JsonStreamParser parser = mapper.parser().readValue(writer.writeValueAsString(node), JsonParser.class);
 parser.nextToken(); // consume START_OBJECT
 parser.close(); // close parser and throw IOException
 SettableAnyProperty.deserializeAndSet(settableAnyProperty, null, null, sampleInstance, "root");
 fail("Expected IOException but got none");
 }

 @Test
 public void testSetterIsField() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JavaType javaType = mapper.getTypeFactory().constructMapType(Map.class, String.class, String.class);
 SettableAnyProperty settableAnyPropertyField = new SettableAnyProperty(
 new BeanProperty.Standardizer().standardize(new AnnotatedField(Map.class.getDeclaredField("entrySet"))),
 new AnnotatedField(Map.class, "entrySet"), javaType, null, null, null);
 sampleInstance = new HashMap<>();
 SettableAnyProperty.deserializeAndSet(settableAnyPropertyField, mapper.readTree("{\"key\":\"value\"}"), null, sampleInstance, "root");
 Map<String, String> map = (Map<String, String>) sampleInstance;
 assertEquals("value", map.get("key"));
 }

 @Test
 public void testValueDeserializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonDeserializer<String> stringDeserializer = new StringDeserializer();
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
 new BeanProperty.Standardizer().standardize(new AnnotatedField(Map.class.getDeclaredField("entrySet"))),
 new AnnotatedField(Map.class, "entrySet"), null, null, null, null);
 settableAnyProperty = settableAnyProperty.withValueDeserializer(stringDeserializer);
 sampleInstance = new HashMap<>();
 JsonNode node = mapper.readTree("{\"key\":\"hello\"}");
 SettableAnyProperty.deserializeAndSet(settableAnyProperty, node, null, sampleInstance, "root");
 Map<Object, Object> map = (Map<Object, Object>) sampleInstance;
 assertEquals("hello", map.get("key"));
 }

 @Test
 public void testKeyDeserializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 KeyDeserializer stringKeyDeserializer = new StringKeyDeserializer();
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
 new BeanProperty.Standardizer().standardize(new AnnotatedField(Map.class.getDeclaredField("entrySet"))),
 new AnnotatedField(Map.class, "entrySet"), null, stringKeyDeserializer, null, null);
 sampleInstance = new HashMap<>();
 SettableAnyProperty.deserializeAndSet(settableAnyProperty, mapper.readTree("{\"key\":\"hello\", \"value\":\"world\"}"), null, sampleInstance, "root");
 Map<String, Object> map = (Map<String, Object>) sampleInstance;
 assertEquals("hello", map.keySet().iterator().next());
 assertEquals("world", map.get("hello"));
 }

}