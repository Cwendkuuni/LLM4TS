import com.fasterxml.jackson.core.base.DatabindContext;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

 private TypeFactory typeFactory;
 private DatabindContext context;
 private JavaType baseType;
 private ClassNameIdResolver idResolver;

 @Before
 public void setup() {
 typeFactory = new TypeFactoryBuilder()
 .base(Object.class)
 .build();
 baseType = typeFactory.constructType(Object.class);
 context = new ObjectMapper().getDeserializationContext();
 idResolver = new ClassNameIdResolver(baseType, typeFactory);
 }

 @Test
 public void testGetMechanism() {
 assertEquals(JsonTypeInfo.Id.CLASS, idResolver.getMechanism());
 }

 @Test
 public void testIdFromValue() {
 Object value = new Object();
 String actual = idResolver.idFromValue(value);
 String expected = value.getClass().getName();
 assertEquals(expected, actual);
 }

 @Test
 public void testIdFromValueAndType() {
 Object value = new Object();
 Class<?> type = String.class;
 String actual = idResolver.idFromValueAndType(value, type);
 String expected = type.getName();
 assertEquals(expected, actual);
 }

 @Test
 public void testTypeFromIdHappyPath() throws IOException {
 String id = "java.lang.String";
 JavaType javaType = idResolver.typeFromId(context, id);
 assertEquals(String.class, javaType.getRawClass());
 }

 @Test(expected = IOException.class)
 public void testTypeFromIdUnknownClass() throws IOException {
 String id = "com.nonExistent.UnknownClass";
 idResolver.typeFromId(context, id);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTypeFromIdInvalidId() throws IOException {
 String id = "InvalidTypeId";
 idResolver.typeFromId(context, id);
 }

 @Test
 public void testIdFromValueEnumSet() throws IOException {
 EnumSet<Color> enumSet = EnumSet.allOf(Color.class);
 String actual = idResolver.idFromValue(enumSet);
 String expected = typeFactory.constructCollectionType(EnumSet.class, Color.class).toCanonical();
 assertEquals(expected, actual);
 }

 @Test
 public void testIdFromValueEnumMap() throws IOException {
 EnumMap<Color, String> enumMap = new EnumMap<>(Color.class);
 enumMap.put(Color.RED, "red");
 enumMap.put(Color.GREEN, "green");
 String actual = idResolver.idFromValue(enumMap);
 String expected = typeFactory.constructMapType(EnumMap.class, Color.class, String.class).toCanonical();
 assertEquals(expected, actual);
 }

 @Test
 public void testIdFromValueInnerClass() throws IOException {
 Object outer = new OuterClass();
 String actual = idResolver.idFromValue(outer.new InnerClass());
 String expected = "com.fasterxml.jackson.databind.jsontype.impl.OuterClass$InnerClass";
 assertEquals(expected, actual);
 }
}

 class OuterClass {
 static class InnerClass {
 }
 }