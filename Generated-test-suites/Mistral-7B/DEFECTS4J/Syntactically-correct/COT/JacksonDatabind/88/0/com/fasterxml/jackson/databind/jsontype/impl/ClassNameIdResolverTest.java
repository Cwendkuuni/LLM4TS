import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

@RunWith(MockitoJUnitRunner.class)
public class ClassNameIdResolverTest {

 private JavaType baseType;
 private TypeFactory typeFactory;
 private DatabindContext context;
 private ClassNameIdResolver resolver;

 @Before
 public void setup() throws IOException {
 baseType = Mockito.mock(JavaType.class);
 typeFactory = Mockito.spy(TypeFactory.class);
 context = Mockito.mock(DatabindContext.class);
 resolver = new ClassNameIdResolver(baseType, typeFactory);
 }

 @Test
 public void testGetMechanism() {
 assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
 }

 @Test
 public void testRegisterSubtype() {
 // This method is not used with class name - based resolvers
 }

 @Test (expected = UnsupportedOperationException.class)
 public void testIdFromValueWhenValueIsNull() {
 resolver.idFromValue(null);
 }

 @Test
 public void testIdFromValue() throws Exception {
 String id = resolver.idFromValue(new Object());
 assertEquals(classNameFromObject(new Object()), id);
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
 String id = resolver.idFromValueAndType(new Object(), Object.class);
 assertEquals(classNameFromObject(new Object()), id);
 }

 @Test
 public void testTypeFromIdWhenValidIdIsProvided() throws IOException {
 JavaType javaType = resolver.typeFromId(context, classNameFromObject(String.class));
 assertEquals(String.class, javaType.getRawClass());
 }

 @Test (expected = NullPointerException.class)
 public void testTypeFromIdWhenNullContextIsProvided() {
 resolver.typeFromId(null, classNameFromObject(String.class));
 }

 @Test (expected = IllegalArgumentException.class)
 public void testTypeFromIdWhenInvalidIdIsProvided() throws IOException {
 resolver.typeFromId(context, "invalidId");
 }

 @Test
 public void testTypeFromIdWhenClassNotFoundExceptionIsThrown() throws IOException {
 when(typeFactory.findClass(eq("com.faker.package.TestClass"))).thenThrow(new ClassNotFoundException());
 JavaType javaType = resolver.typeFromId(context, "com.faker.package.TestClass");
 assertEquals(baseType, javaType);
 }

 @Test
 public void testTypeFromIdWhenDeserializationContextIsProvidedAndUnknownTypeIsFound() throws IOException {
 when(context.handleUnknownTypeId(eq(baseType), eq("com.faker.package.TestClass"), eq(resolver), anyString()))
 .thenReturn(String.class);
 JavaType javaType = resolver.typeFromId(context, "com.faker.package.TestClass");
 assertEquals(String.class, javaType.getRawClass());
 }

 @Test
 public void testIdFromValueAndTypeWhenEnumIsProvided() throws IOException {
 Enum testEnum = Enum.valueOf(String.class, "TEST");
 String id = resolver.idFromValueAndType(testEnum, Enum.class);
 assertEquals(classNameFromEnum(testEnum), id);
 }

 @Test
 public void testIdFromValueAndTypeWhenEnumSubclassIsProvided() throws IOException {
 Enum testEnumSubclass = Enum.valueOf(String.class, "TEST_SUBCLASS");
 EnumSuperclass superclass = Enum.valueOf(String.class, "TEST_SUPERCLASS");
 String id = resolver.idFromValueAndType(testEnumSubclass, Enum.class);
 assertEquals(classNameFromEnum(superclass), id);
 }

 @Test
 public void testTypeFromIdWhenGenericTypeIsProvided() throws IOException {
 JavaType javaType = resolver.typeFromId(context, "java.util.List<java.lang.String>");
 assertEquals(List.class, javaType.getRawClass());
 }

 @Test
 public void testTypeFromIdWhenEnumSetIsProvided() throws IOException {
 Enum testEnum = Enum.valueOf(String.class, "TEST");
 EnumSet<Enum> enumSet = EnumSet.of(testEnum);
 String id = resolver.idFromValue(enumSet);
 assertEquals("java.util.EnumSet<"+ classNameFromEnum(testEnum) +">", id);
 JavaType javaType = resolver.typeFromId(context, id);
 assertEquals(EnumSet.class, javaType.getRawClass());
 }

 @Test
 public void testTypeFromIdWhenEnumMapIsProvided() throws IOException {
 Enum testEnum = Enum.valueOf(String.class, "TEST");
 EnumMap<Enum, Object> enumMap = new EnumMap<>(testEnum.getDeclaringClass(), testEnum);
 String id = resolver.idFromValue(enumMap);
 assertEquals("java.util.EnumMap<"+ classNameFromEnum(testEnum) +", java.lang.Object>", id);
 JavaType javaType = resolver.typeFromId(context, id);
 assertEquals(EnumMap.class, javaType.getRawClass());
 }

 @Test
 public void testIdFromValueWhenJavaUtilArraysListIsProvided() throws IOException {
 Object obj = new Object[1];
 String id = resolver.idFromValue(obj);
 assertEquals("java.util.ArrayList", id);
 }

 private String classNameFromObject(Object obj) {
 return resolver._idFrom(obj, obj.getClass(), typeFactory);
 }

 private String classNameFromEnum(Enum enumObject) {
 return ClassUtil.getClassName(enumObject.getDeclaringClass());
 }
}