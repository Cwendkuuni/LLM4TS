import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

 private ObjectMapper mapper;
 private TypeIdResolver resolver;

 @Before
 public void setUp() throws Exception {
 mapper = new ObjectMapper();
 resolver = mapper.getTypeFactory().constructValueType(TypeIdResolver.class);
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testIdFromValue() throws Exception {
 Object value = new Object();
 Class<?> cls = value.getClass();
 String id = resolver.idFromValue(value);
 JavaType type = mapper.getTypeFactory().constructType(id);
 assertTrue(type.isAssignableFrom(cls));
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
 Object value = new Object();
 Class<?> cls = String.class;
 String id = resolver.idFromValueAndType(value, cls);
 JavaType type = mapper.getTypeFactory().constructType(id);
 assertTrue(type.isAssignableFrom(cls));
 }

 @Test
 public void testTypeFromId() throws Exception {
 String id = "java.lang.String";
 JavaType type = resolver.typeFromId(null, id);
 assertEquals(String.class, type.getRawClass());
 }

 @Test
 public void testTypeFromIdWithGenericType() throws Exception {
 TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {};
 String id = typeRef.getTypeId();
 JavaType type = resolver.typeFromId(null, id);
 assertEquals(List.class, type.getRawClass());
 assertEquals(String.class, type.getType().getComponentType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTypeId() throws Exception {
 String id = "InvalidTypeId";
 JavaType type = resolver.typeFromId(null, id);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnknownClass() throws Exception {
 String id = "com.unknown.UnknownClass";
 JavaType type = resolver.typeFromId(null, id);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testIdFromValueForEnum() throws Exception {
 Object value = Color.RED;
 String id = resolver.idFromValue(value);
 JavaType type = mapper.getTypeFactory().constructType(id);
 assertTrue(type.isInstance(value));
 }

 @Test
 public void testIdFromValueForAnonymousClass() throws Exception {
 Object value = new Object() {
 public void foo() {}
 };
 String id = resolver.idFromValue(value);
 JavaType type = mapper.getTypeFactory().constructType(id);
 assertTrue(type.isInstance(value));
 }

 @Test
 public void testIdFromValueForInnerClass() throws Exception {
 Object outerValue = new Object();
 Class<?> outerType = outerValue.getClass();
 Object innerValue = new Object() {
 };
 Class<?> innerType = innerValue.getClass();
 String id = resolver.idFromValueAndType(innerValue, outerType);
 JavaType type = mapper.getTypeFactory().constructType(id);
 assertTrue(type.isInstance(innerValue));
 }

 @Test
 public void testIdFromValueForArray() throws Exception {
 Object value = new Object[10];
 String id = resolver.idFromValue(value);
 JavaType type = mapper.getTypeFactory().constructType(id);
 assertEquals(Object[].class, type.getRawClass());
 }

 @Test
 public void testIdFromValueForMap() throws Exception {
 Object value = new HashMap<String, Integer>() {{
 put("one", 1);
 put("two", 2);
 }};
 String id = resolver.idFromValue(value);
 JavaType type = mapper.getTypeFactory().constructType(id);
 assertTrue(type.isInstance(value));
 }

 @Test
 public void testIdFromValueForCollection() throws Exception {
 Object value = new ArrayList<String>() {{
 add("one");
 add("two");
 }};
 String id = resolver.idFromValue(value);
 JavaType type = mapper.getTypeFactory().constructType(id);
 assertTrue(type.isInstance(value));
 }

 @Test
 public void testIdFromValueForList() throws Exception {
 Object value = Arrays.asList("one", "two");
 String id = resolver.idFromValue(value);
 JavaType type = mapper.getTypeFactory().constructType(id);
 assertTrue(type.isInstance(value));
 assertEquals(List.class, type.getRawClass());
 assertEquals(String.class, type.getType().getComponentType());
 }
}