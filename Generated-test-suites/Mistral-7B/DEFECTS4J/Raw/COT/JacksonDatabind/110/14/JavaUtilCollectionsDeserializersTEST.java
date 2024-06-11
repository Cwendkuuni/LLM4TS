import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.*;

public class JavaUtilCollectionsDeserializersTest {

 private ObjectMapper objectMapper = new JsonMapper();

 @Test
 public void testFindForCollection_SingletonList() throws Exception {
 JavaType javaType = new TypeReference<List<Boolean>>(){}.getType();
 List<Boolean> inputList = Arrays.asList(Boolean.TRUE, Boolean.TRUE);
 DeserializationContext context = objectMapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, javaType);
 List<Boolean> outputList = objectMapper.readValue(objectMapper.writeValueAsString(inputList), deserializer.getType());
 assertEquals(Collections.singletonList(Boolean.TRUE), outputList);
 }

 @Test
 public void testFindForCollection_SingletonSet() throws Exception {
 JavaType javaType = new TypeReference<Set<Boolean>>(){}.getType();
 Set<Boolean> inputSet = Collections.singleton(Boolean.TRUE);
 DeserializationContext context = objectMapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, javaType);
 Set<Boolean> outputSet = objectMapper.readValue(objectMapper.writeValueAsString(inputSet), deserializer.getType());
 assertEquals(Collections.singleton(Boolean.TRUE), outputSet);
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws Exception {
 JavaType javaType = new TypeReference<List<Boolean>>(){}.getType();
 List<Boolean> inputList = Arrays.asList(Boolean.TRUE, Boolean.TRUE);
 List<Boolean> unmodifiableList = Collections.unmodifiableList(inputList);
 DeserializationContext context = objectMapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, javaType);
 List<Boolean> outputList = objectMapper.readValue(objectMapper.writeValueAsString(unmodifiableList), deserializer.getType());
 assertEquals(unmodifiableList, outputList);
 }

 @Test
 public void testFindForCollection_UnmodifiableSet() throws Exception {
 JavaType javaType = new TypeReference<Set<Boolean>>(){}.getType();
 Set<Boolean> inputSet = Collections.unmodifiableSet(Arrays.asList(Boolean.TRUE, Boolean.TRUE).stream().collect(Collectors.toSet()));
 DeserializationContext context = objectMapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, javaType);
 Set<Boolean> outputSet = objectMapper.readValue(objectMapper.writeValueAsString(inputSet), deserializer.getType());
 assertEquals(inputSet, outputSet);
 }

 @Test
 public void testFindForMap_SingletonMap() throws Exception {
 JavaType javaType = new TypeReference<Map<String, Integer>>(){}.getType();
 Map<String, Integer> inputMap = new HashMap<String, Integer>() {{
 put("key", 1);
 }} ;
 DeserializationContext context = objectMapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, javaType);
 Map<String, Integer> outputMap = objectMapper.readValue(objectMapper.writeValueAsString(inputMap), deserializer.getType());
 assertEquals(inputMap, outputMap);
 }

 @Test
 public void testFindForMap_UnmodifiableMap() throws Exception {
 JavaType javaType = new TypeReference<Map<String, Integer>>(){}.getType();
 Map<String, Integer> inputMap = new HashMap<String, Integer>() {{
 put("key", 1);
 }};
 Map<String, Integer> unmodifiableMap = Collections.unmodifiableMap(inputMap);
 DeserializationContext context = objectMapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, javaType);
 Map<String, Integer> outputMap = objectMapper.readValue(objectMapper.writeValueAsString(unmodifiableMap), deserializer.getType());
 assertEquals(unmodifiableMap, outputMap);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForInvalidType() throws Exception {
 JavaType javaType = new TypeReference<Integer>(){}.getType();
 DeserializationContext context = objectMapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, javaType);
 }

}