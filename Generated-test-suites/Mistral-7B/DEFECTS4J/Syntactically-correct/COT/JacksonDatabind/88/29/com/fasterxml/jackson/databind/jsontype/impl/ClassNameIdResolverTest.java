import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.DatabindContext;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

 private ClassNameIdResolver resolver;
 private TypeFactory typeFactory;
 private DatabindContext context;

 @Before
 public void setup() throws IOException {
 resolver = new ClassNameIdResolver(null, null);
 typeFactory = ObjectMapper.getDefaultInstance().getTypeFactory();
 context = ObjectMapper.getDefaultInstance().getDeserializationContext();
 }

 @Test
 public void testGetMechanism() {
 JsonTypeInfo.Id id = resolver.getMechanism();
 assertEquals(JsonTypeInfo.Id.CLASS, id);
 }

 @Test
 public void testIdFromValue() {
 Object value = new Object();
 String id = resolver.idFromValue(value);
 String expected = value.getClass().getName();
 assertEquals(expected, id);
 }

 @Test
 public void testIdFromValueAndType() {
 Object value = new Object();
 Class<?> type = value.getClass();
 String id = resolver.idFromValueAndType(value, type);
 String expected = type.getName();
 assertEquals(expected, id);
 }

 @Test
 public void testTypeFromId() throws IOException {
 String id = "java.lang.String";
 JavaType javaType = resolver.typeFromId(context, id);
 Class<?> expected = java.lang.String.class;
 assertEquals(expected, javaType.getRawClass());
 }

 @Test(expected = NullPointerException.class)
 public void testTypeFromIdNullContext() throws IOException {
 String id = "java.lang.String";
 resolver.typeFromId(null, id);
 }

 @Test(expected = IOException.class)
 public void testTypeFromIdIOException() throws IOException {
 String id = "com.nonexistent.class";
 resolver.typeFromId(context, id);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTypeFromIdInvalidId() throws IOException {
 String id = "InvalidTypeId";
 resolver.typeFromId(context, id);
 }

 @Test
 public void testIdFromValueEnum() {
 Object value = MyEnum.SOME_VALUE;
 String id = resolver.idFromValue(value);
 String expected = MyEnum.class.getName();
 assertEquals(expected, id);
 }

 @Test
 public void testIdFromValueAndTypeEnum() {
 Object value = MyEnum.SOME_VALUE;
 Class<?> type = MyEnum.class;
 String id = resolver.idFromValueAndType(value, type);
 String expected = type.getName();
 assertEquals(expected, id);
 }

 @Test
 public void test_typeFromIdWithGenericType() throws IOException {
 String id = "java.util.List<java.lang.Integer>";
 JavaType javaType = resolver.typeFromId(context, id);
 Class<?> expected = java.util.List.class;
 assertEquals(expected, javaType.getRawClass());
 }

 @Test
 public void test_idFromValueWithArray() {
 Object value = new int[10];
 String id = resolver.idFromValue(value);
 String expected = "int[]";
 assertEquals(expected, id);
 }

 @Test
 public void test_idFromValueWithCustomClass() {
 Object value = new MyCustomClass();
 String id = resolver.idFromValue(value);
 String expected = MyCustomClass.class.getName();
 assertEquals(expected, id);
 }

 @Test
 public void test_idFromValueWithInnerClass() {
 Object value = new OuterClass().new InnerClass();
 String id = resolver.idFromValue(value);
 String expected = "outerClass$InnerClass";
 assertEquals(expected, id);
 }

}