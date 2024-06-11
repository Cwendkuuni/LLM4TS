import com.fasterxml.jackson.core.DatabindException;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClassNameIdResolverTest {

 private final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testIdFromValue() throws DatabindException {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
 Object value = new Object();
 String id = resolver.idFromValue(value);
 JavaType javaType = resolver.typeFromId(objectMapper.getDatabindContext(), id);
 assertEquals(Object.class, javaType.getRawClass());
 }

 @Test
 public void testIdFromValueAndType() throws DatabindException {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
 Object value = new Object();
 Class<?> type = String.class;
 String id = resolver.idFromValueAndType(value, type);
 JavaType javaType = resolver.typeFromId(objectMapper.getDatabindContext(), id);
 assertEquals(String.class, javaType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTypeId() throws DatabindException {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
 DatabindContext context = objectMapper.getDatabindContext();
 String id = "InvalidTypeId";
 JavaType javaType = resolver.typeFromId(context, id); // should throw IllegalArgumentException
 }

 @Test
 public void testClassNotFoundException() throws DatabindException {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
 DatabindContext context = objectMapper.getDatabindContext();
 String id = "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver";
 JavaType javaType = resolver.typeFromId(context, id); // should return the ClassNameIdResolver class
 }

 @Test
 public void testEnum() throws DatabindException {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
 Enum value = Enum.valueOf(String.class, "TEST");
 String id = resolver.idFromValue(value);
 JavaType javaType = resolver.typeFromId(objectMapper.getDatabindContext(), id);
 assertEquals(String.class, javaType.getRawClass());
 }

 @Test
 public void testCollectionType() throws DatabindException {
 ClassNameIdResolver resolver = new ClassNameIdResolver(List.class.getTypeName(), objectMapper.getTypeFactory());
 List<String> value = Arrays.asList("test1", "test2");
 String id = resolver.idFromValueAndType(value, Object[].class);
 JavaType javaType = resolver.typeFromId(objectMapper.getDatabindContext(), id);
 assertEquals(List.class, javaType.getRawClass());
 assertEquals(ArrayList.class, javaType.getRawType().getComponentType());
 }

 @Test
 public void testMapType() throws DatabindException {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Map.class.getTypeName(), objectMapper.getTypeFactory());
 Map<String, String> value = new HashMap<>();
 value.put("key1", "value1");
 String id = resolver.idFromValueAndType(value, Object[].class);
 JavaType javaType = resolver.typeFromId(objectMapper.getDatabindContext(), id);
 assertEquals(Map.class, javaType.getRawClass());
 assertEquals(HashMap.class, javaType.getRawType().getKeyType().getRawClass());
 assertEquals(String.class, javaType.getRawType().getValueType().getRawClass());
 }

 @Test
 public void testArrayType() throws DatabindException {
 ClassNameIdResolver resolver = new ClassNameIdResolver(int[].class.getTypeName(), objectMapper.getTypeFactory());
 int[] value = {1, 2, 3};
 String id = resolver.idFromValueAndType(value, int[].class);
 JavaType javaType = resolver.typeFromId(objectMapper.getDatabindContext(), id);
 assertEquals(int[].class, javaType.getRawClass());
 }
}