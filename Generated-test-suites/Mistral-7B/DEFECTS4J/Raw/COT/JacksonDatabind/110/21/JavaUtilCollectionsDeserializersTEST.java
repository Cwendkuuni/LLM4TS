import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.util.Converter;
import java.util.*;

public class JavaUtilCollectionsDeserializersTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testFindForCollection_SingleTON_List() throws JsonMappingException {
 JavaType type = new TypeReference<List<Boolean>>() {}.getType();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);
 assertEquals(List.class, deserializer.getTargetValueType(null));
 }

 @Test
 public void testFindForCollection_SingleTON_Set() throws JsonMappingException {
 JavaType type = new TypeReference<Set<Boolean>>() {}.getType();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);
 assertEquals(Set.class, deserializer.getTargetValueType(null));
 }

 @Test
 public void testFindForCollection_UNMODIFIABLE_List() throws JsonMappingException {
 JavaType type = new TypeReference<List<Boolean>>() {}.getType();
 type = mapper.getTypeFactory().constructType(type.getRawClass(), Collections.unmodifiableList(Arrays.asList(true, true)).getClass());
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);
 assertEquals(List.class, deserializer.getTargetValueType(null));
 }

 @Test
 public void testFindForCollection_UNMODIFIABLE_Set() throws JsonMappingException {
 JavaType type = new TypeReference<Set<Boolean>>() {}.getType();
 type = mapper.getTypeFactory().constructType(type.getRawClass(), Collections.unmodifiableSet(Arrays.asList(true, true)).getClass());
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);
 assertEquals(Set.class, deserializer.getTargetValueType(null));
 }

 @Test
 public void testFindForMap_SingleTON_Map() throws JsonMappingException {
 JavaType type = new TypeReference<Map<String, Boolean>>() {}.getType();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), type);
 assertEquals(Map.class, deserializer.getTargetValueType(null));
 }

 @Test
 public void testFindForMap_UNMODIFIABLE_Map() throws JsonMappingException {
 JavaType type = new TypeReference<Map<String, Boolean>>() {}.getType();
 type = mapper.getTypeFactory().constructType(type.getRawClass(), Collections.unmodifiableMap(new HashMap<String, Boolean>() {{ put("k", true); }}).getClass());
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), type);
 assertEquals(Map.class, deserializer.getTargetValueType(null));
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForUnknownType() throws JsonMappingException {
 JavaType type = new TypeReference<JavaUtilCollectionsDeserializers>() {}.getType();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);
 fail("Should throw JsonMappingException");
 }

}