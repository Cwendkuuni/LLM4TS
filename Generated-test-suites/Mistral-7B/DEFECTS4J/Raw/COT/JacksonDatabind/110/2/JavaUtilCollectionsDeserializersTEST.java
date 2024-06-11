import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Map;

public class JavaUtilCollectionsDeserializersTest {

 private ObjectMapper objectMapper = new JsonMapper();

 @Test
 public void testFindForCollection_SingletonList() throws Exception {
 JavaType javaType = new TypeReference<List<Boolean>>() {}.getType();
 Set<Boolean> input = Collections.singleton(Boolean.TRUE);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
 List<Boolean> output = objectMapper.readValue(objectMapper.writeValueAsString(input), deserializer.getType());
 Assert.assertEquals(input, output);
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws Exception {
 JavaType javaType = new TypeReference<List<Boolean>>() {}.getType();
 List<Boolean> input = Collections.unmodifiableList(Collections.singletonList(Boolean.TRUE));
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
 List<Boolean> output = objectMapper.readValue(objectMapper.writeValueAsString(input), deserializer.getType());
 Assert.assertEquals(input, output);
 }

 @Test
 public void testFindForCollection_SingletonSet() throws Exception {
 JavaType javaType = new TypeReference<Set<Boolean>>() {}.getType();
 Set<Boolean> input = Collections.singleton(Boolean.TRUE);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
 Set<Boolean> output = objectMapper.readValue(objectMapper.writeValueAsString(input), deserializer.getType());
 Assert.assertEquals(input, output);
 }

 @Test
 public void testFindForCollection_UnmodifiableSet() throws Exception {
 JavaType javaType = new TypeReference<Set<Boolean>>() {}.getType();
 Set<Boolean> input = Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE));
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
 Set<Boolean> output = objectMapper.readValue(objectMapper.writeValueAsString(input), deserializer.getType());
 Assert.assertEquals(input, output);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForCollection_InvalidType() throws Exception {
 JavaType javaType = new TypeReference<Object[]>() {}.getType();
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
 }

 @Test
 public void testFindForMap_SingletonMap() throws Exception {
 JavaType javaType = new TypeReference<Map<String, String>>() {}.getType();
 Map<String, String> input = Collections.singletonMap("key", "value");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaType);
 Map<String, String> output = objectMapper.readValue(objectMapper.writeValueAsString(input), deserializer.getType());
 Assert.assertEquals(input, output);
 }

 @Test
 public void testFindForMap_UnmodifiableMap() throws Exception {
 JavaType javaType = new TypeReference<Map<String, String>>() {}.getType();
 Map<String, String> input = Collections.unmodifiableMap(Collections.singletonMap("key", "value"));
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaType);
 Map<String, String> output = objectMapper.readValue(objectMapper.writeValueAsString(input), deserializer.getType());
 Assert.assertEquals(input, output);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForMap_InvalidType() throws Exception {
 JavaType javaType = new TypeReference<Object[]>() {}.getType();
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaType);
 }
}