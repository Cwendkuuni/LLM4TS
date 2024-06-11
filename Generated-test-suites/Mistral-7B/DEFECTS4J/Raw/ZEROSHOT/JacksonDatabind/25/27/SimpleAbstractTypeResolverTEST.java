import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @Before
 public void setUp() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);
 }

 @Test
 public void testFindTypeMapping() {
 DeserializationConfig config = new DeserializationConfig();
 config.setAnnotationIntrospector(new StandardAnnotationIntrospector());
 ObjectMapper mapper = new ObjectMapper();
 mapper.setConfig(config);
 mapper.setTypeResolver(resolver);

 JavaType javaTypeCollection = mapper.getTypeFactory().constructType(Collection.class);
 JavaType javaTypeLinkedList = mapper.getTypeFactory().constructType(LinkedList.class);
 JavaType expected = javaTypeLinkedList;
 JavaType actual = resolver.findTypeMapping(config, javaTypeCollection);
 assertEquals(expected.getRawClass(), actual.getRawClass());

 JavaType javaTypeList = mapper.getTypeFactory().constructType(List.class);
 JavaType javaTypeLinkedList2 = mapper.getTypeFactory().constructType(LinkedList.class);
 JavaType expected2 = javaTypeLinkedList2;
 JavaType actual2 = resolver.findTypeMapping(config, javaTypeList);
 assertEquals(expected2.getRawClass(), actual2.getRawClass());

 JavaType javaTypeString = mapper.getTypeFactory().constructType(String.class);
 JavaType expected3 = javaTypeString;
 JavaType actual3 = resolver.findTypeMapping(config, javaTypeString);
 assertEquals(expected3.getRawClass(), actual3.getRawClass());

 JavaType javaTypeInteger = mapper.getTypeFactory().constructType(Integer.class);
 JavaType expected4 = javaTypeInteger;
 JavaType actual4 = resolver.findTypeMapping(config, javaTypeInteger);
 assertEquals(expected4.getRawClass(), actual4.getRawClass());

 JavaType javaTypeAbstractList = mapper.getTypeFactory().constructType(AbstractList.class);
 JavaType expected5 = javaTypeLinkedList2;
 JavaType actual5 = resolver.findTypeMapping(config, javaTypeAbstractList);
 assertEquals(expected5.getRawClass(), actual5.getRawClass());

 JavaType javaTypeArrayList = mapper.getTypeFactory().constructType(ArrayList.class);
 JavaType expected6 = javaTypeArrayList;
 JavaType actual6 = resolver.findTypeMapping(config, javaTypeArrayList);
 assertEquals(expected6.getRawClass(), actual6.getRawClass());

 JavaType javaTypeHashMap = mapper.getTypeFactory().constructType(HashMap.class);
 JavaType expected7 = javaTypeHashMap;
 JavaType actual7 = resolver.findTypeMapping(config, javaTypeHashMap);
 assertEquals(expected7.getRawClass(), actual7.getRawClass());

 JavaType javaTypeHashtable = mapper.getTypeFactory().constructType(Hashtable.class);
 JavaType expected8 = javaTypeHashtable;
 JavaType actual8 = resolver.findTypeMapping(config, javaTypeHashtable);
 assertEquals(expected8.getRawClass(), actual8.getRawClass());

 JavaType javaTypeArray = mapper.getTypeFactory().constructType(Object[].class);
 JavaType expected9 = javaTypeArray;
 JavaType actual9 = resolver.findTypeMapping(config, javaTypeArray);
 assertEquals(expected9.getRawClass(), actual9.getRawClass());

 JavaType javaTypeClassId = mapper.getTypeFactory().constructType(ClassIdType.class);
 JavaType expected10 = javaTypeClassId;
 JavaType actual10 = resolver.findTypeMapping(config, javaTypeClassId);
 assertEquals(expected10.getRawClass(), actual10.getRawClass());
 }

 @Test
 public void testResolveAbstractType() {
 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper();
 mapper.setConfig(config);
 mapper.setTypeResolver(resolver);

 JavaType javaTypeCollection = mapper.getTypeFactory().constructType(Collection.class);
 assertNull(resolver.resolveAbstractType(config, javaTypeCollection));

 JavaType javaTypeList = mapper.getTypeFactory().constructType(List.class);
 assertNull(resolver.resolveAbstractType(config, javaTypeList));

 JavaType javaTypeAbstractList = mapper.getTypeFactory().constructType(AbstractList.class);
 assertNull(resolver.resolveAbstractType(config, javaTypeAbstractList));

 JavaType javaTypeArrayList = mapper.getTypeFactory().constructType(ArrayList.class);
 assertNull(resolver.resolveAbstractType(config, javaTypeArrayList));

 JavaType javaTypeLinkedList = mapper.getTypeFactory().constructType(LinkedList.class);
 JavaType javaTypeList2 = mapper.getTypeFactory().constructType(List.class);
 assertEquals(javaTypeList2.getRawClass(), resolver.resolveAbstractType(config, javaTypeLinkedList).getRawClass());

 JavaType javaTypeMap = mapper.getTypeFactory().constructType(Map.class);
 JavaType javaTypeHashMap = mapper.getTypeFactory().constructType(HashMap.class);
 JavaType javaTypeHashtable = mapper.getTypeFactory().constructType(Hashtable.class);
 assertEquals(javaTypeHashtable.getRawClass(), resolver.resolveAbstractType(config, javaTypeHashMap).getRawClass());
 }
}