import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testFindForCollection_SingleTON_Set() throws Exception {
 JavaType javaTypeSet = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
 DeserializationContext ctxt = objectMapper.getSerializationConfig().getDefaultDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeSet);
 assertNotNull(deserializer);

 Set<Boolean> set = new HashSet<Boolean>() {{
 add(Boolean.TRUE);
 }};
 Object deserializedSet = objectMapper.readValue(objectMapper.writeValueAsString(set), javaTypeSet.withoutRaw());
 assertEquals(deserializedSet, JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeSet).deserialize(null, ctxt, javaTypeSet));
 }

 @Test
 public void testFindForCollection_SingleTON_List() throws Exception {
 JavaType javaTypeList = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
 DeserializationContext ctxt = objectMapper.getSerializationConfig().getDefaultDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeList);
 assertNotNull(deserializer);

 List<Boolean> list = new ArrayList<Boolean>() {{
 add(Boolean.TRUE);
 }};
 Object deserializedList = objectMapper.readValue(objectMapper.writeValueAsString(list), javaTypeList.withoutRaw());
 assertEquals(deserializedList, JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeList).deserialize(null, ctxt, javaTypeList));
 }

 @Test
 public void testFindForCollection_SingleTON_Map() throws Exception {
 JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
 DeserializationContext ctxt = objectMapper.getSerializationConfig().getDefaultDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeMap);
 assertNotNull(deserializer);

 Map<String, String> map = new HashMap<String, String>() {{
 put("a", "b");
 }};
 Object deserializedMap = objectMapper.readValue(objectMapper.writeValueAsString(map), javaTypeMap.withoutRaw());
 assertEquals(deserializedMap, JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeMap).deserialize(null, ctxt, javaTypeMap));
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForCollection_Unsupported_Type() throws Exception {
 JavaType javaTypeUnsupportedType = TypeFactory.defaultInstance().constructType(Integer.class);
 DeserializationContext ctxt = objectMapper.getSerializationConfig().getDefaultDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeUnsupportedType);
 assertNull(deserializer);
 }

 @Test
 public void testFindForMap_SingleTON_Map() throws Exception {
 JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
 DeserializationContext ctxt = objectMapper.getSerializationConfig().getDefaultDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaTypeMap);
 assertNotNull(deserializer);

 Map<String, String> map = new HashMap<String, String>() {{
 put("a", "b");
 }};
 Object deserializedMap = objectMapper.readValue(objectMapper.writeValueAsString(map), javaTypeMap.withoutRaw());
 assertEquals(deserializedMap, JavaUtilCollectionsDeserializers.findForMap(ctxt, javaTypeMap).deserialize(null, ctxt, javaTypeMap));
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForMap_Unsupported_Type() throws Exception {
 JavaType javaTypeUnsupportedType = TypeFactory.defaultInstance().constructType(Integer.class, Integer.class);
 DeserializationContext ctxt = objectMapper.getSerializationConfig().getDefaultDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaTypeUnsupportedType);
 assertNull(deserializer);
 }

 @Test
 public void testConverter_SingleTON_Set() throws Exception {
 JavaType javaTypeSet = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
 DeserializationContext ctxt = objectMapper.getSerializationConfig().getDefaultDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeSet);
 JavaUtilCollectionsConverter converter = (JavaUtilCollectionsConverter) deserializer.getDelegate();
 assertNotNull(converter);

 Set<Boolean> set = new HashSet<Boolean>() {{
 add(Boolean.TRUE);
 }};
 Object deserializedSet = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, javaTypeSet, Set.class).convert(set);
 assertEquals(deserializedSet, JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeSet).deserialize(null, ctxt, javaTypeSet));
 }

 @Test
 public void testConverter_SingleTON_List() throws Exception {
 JavaType javaTypeList = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
 DeserializationContext ctxt = objectMapper.getSerializationConfig().getDefaultDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeList);
 JavaUtilCollectionsConverter converter = (JavaUtilCollectionsConverter) deserializer.getDelegate();
 assertNotNull(converter);

 List<Boolean> list = new ArrayList<Boolean>() {{
 add(Boolean.TRUE);
 }};
 Object deserializedList = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, javaTypeList, List.class).convert(list);
 assertEquals(deserializedList, JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeList).deserialize(null, ctxt, javaTypeList));
 }

 @Test
 public void testConverter_SingleTON_Map() throws Exception {
 JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
 DeserializationContext ctxt = objectMapper.getSerializationConfig().getDefaultDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaTypeMap);
 JavaUtilCollectionsConverter converter = (JavaUtilCollectionsConverter) deserializer.getDelegate();
 assertNotNull(converter);

 Map<String, String> map = new HashMap<String, String>() {{
 put("a", "b");
 }};
 Object deserializedMap = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, javaTypeMap, Map.class).convert(map);
 assertEquals(deserializedMap, JavaUtilCollectionsDeserializers.findForMap(ctxt, javaTypeMap).deserialize(null, ctxt, javaTypeMap));
 }

 @Test(expected = JsonMappingException.class)
 public void testConverter_Unsupported_Type() throws Exception {
 JavaType javaTypeUnsupportedType = TypeFactory.defaultInstance().constructType(Integer.class, Integer.class);
 DeserializationContext ctxt = objectMapper.getSerializationConfig().getDefaultDeserializationContext();
 JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, javaTypeUnsupportedType);
 converter.convert(null);
 fail("Should throw an exception");
 }

 @Test
 public void testUnmodifiableCollection_List() throws Exception {
 JavaType javaTypeList = new TypeReference<List<String>>(){}.getType();
 List<String> list = Arrays.asList("Hello", "World");
 Object deserializedList = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getSerializationConfig().getDefaultDeserializationContext(), javaTypeList).deserialize(null, null, javaTypeList.withoutRaw());
 List<String> unmodifiableList = Collections.unmodifiableList(list);
 assertEquals(unmodifiableList, JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getSerializationConfig().getDefaultDeserializationContext(), javaTypeList).deserialize(null, null, javaTypeList.withoutRaw()));
 }

 @Test
 public void testUnmodifiableCollection_Set() throws Exception {
 JavaType javaTypeSet = new TypeReference<Set<String>>(){}.getType();
 Set<String> set = new HashSet<String>() {{
 add("Hello");
 add("World");
 }};
 Object deserializedSet = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getSerializationConfig().getDefaultDeserializationContext(), javaTypeSet).deserialize(null, null, javaTypeSet.withoutRaw());
 Set<String> unmodifiableSet = Collections.unmodifiableSet(set);
 assertEquals(unmodifiableSet, JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getSerializationConfig().getDefaultDeserializationContext(), javaTypeSet).deserialize(null, null, javaTypeSet.withoutRaw()));
 }

 @Test
 public void testUnmodifiableCollection_Map() throws Exception {
 JavaType javaTypeMap = new TypeReference<Map<String, String>>(){}.getType();
 Map<String, String> map = new HashMap<String, String>() {{
 put("A", "1");
 put("B", "2");
 }};
 Object deserializedMap = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getSerializationConfig().getDefaultDeserializationContext(), javaTypeMap).deserialize(null, null, javaTypeMap.withoutRaw());
 Map<String, String> unmodifiableMap = Collections.unmodifiableMap(map);
 assertEquals(unmodifiableMap, JavaUtilCollectionsDeserializers.findForMap(objectMapper.getSerializationConfig().getDefaultDeserializationContext(), javaTypeMap).deserialize(null, null, javaTypeMap.withoutRaw()));
 }

 @Test
 public void testFindForCollection_AsList() throws Exception {
 JavaType javaTypeList = TypeFactory.defaultInstance().constructType(List.class, Integer.TYPE);
 List<Integer> list = Arrays.asList(1, 2, 3);
 DeserializationContext ctxt = objectMapper.getSerializationConfig().getDefaultDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeList);
 Object deserializedList = deserializer.deserialize(null, ctxt, javaTypeList);
 assertEquals(deserializedList, list);
 }
}