import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeParserTest {

 private TypeFactory typeFactory;
 private TypeParser typeParser;

 @Before
 public void setup() {
 typeFactory = new TypeFactory();
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testParse_ValidType() throws Exception {
 String canonicalType = "java.util.List<java.lang.String>";
 JavaType parsedType = typeParser.parse(canonicalType);
 Class<?> rawType = parsedType.getRawClass();
 TypeBindings typeBindings = parsedType.getTypeBindings();

 assertTrue(TypeBindings.isContainerType(parsedType));
 assertEquals(List.class, rawType);
 assertEquals(String.class, typeBindings.getParameterTypes().get(0).getRawClass());
 }

 @Test
 public void testParse_EmptyType() throws Exception {
 String canonicalType = "";
 JavaType parsedType = typeParser.parse(canonicalType);
 assertNull(parsedType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_InvalidInput() throws Exception {
 String canonicalType = "InvalidType";
 typeParser.parse(canonicalType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_UnexpectedToken_NonClosingBracket() throws Exception {
 String canonicalType = "java.util.List<java.lang.String<";
 typeParser.parse(canonicalType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_MissingClosingBracket() throws Exception {
 String canonicalType = "java.util.List<java.lang.String";
 typeParser.parse(canonicalType);
 }

 @Test
 public void testParse_ClassWithoutGenerics() throws Exception {
 String canonicalType = "java.lang.String";
 JavaType parsedType = typeParser.parse(canonicalType);
 Class<?> rawType = parsedType.getRawClass();
 assertEquals(String.class, rawType);
 }

 @Test
 public void testParseTypes_ValidTypes() throws Exception {
 String canonicalType = "java.util.List<java.lang.String>, java.util.Map<java.lang.String, java.lang.Integer>";
 List<JavaType> parsedTypes = typeParser.parseTypes(new MyTokenizer(canonicalType));
 JavaType firstType = parsedTypes.get(0);
 JavaType secondType = parsedTypes.get(1);

 assertTrue(firstType instanceof ParameterizedType);
 assertTrue(secondType instanceof ParameterizedType);

 TypeBindings firstTypeBindings = firstType.getTypeBindings();
 assertEquals(List.class, firstTypeBindings.getRawType());
 assertEquals(String.class, firstTypeBindings.getParameterTypes().get(0).getRawClass());

 TypeBindings secondTypeBindings = secondType.getTypeBindings();
 assertEquals(Map.class, secondTypeBindings.getRawType());
 assertEquals(String.class, secondTypeBindings.getParameterTypes().get(0).getRawClass());
 assertEquals(Integer.class, secondTypeBindings.getParameterTypes().get(1).getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseTypes_InvalidInput() throws Exception {
 String canonicalType = "java.util.List<java.lang.String, java.lang.Integer<";
 typeParser.parseTypes(new MyTokenizer(canonicalType));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseTypes_MissingClosingBracket() throws Exception {
 String canonicalType = "java.util.List<java.lang.String, java.lang.Integer";
 typeParser.parseTypes(new MyTokenizer(canonicalType));
 }

 @Test
 public void testFindClass_ValidClassName() throws Exception {
 String className = "java.util.ArrayList";
 Class<?> foundClass = typeParser.findClass(className, null);
 assertEquals(ArrayList.class, foundClass);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindClass_InvalidClassName() throws Exception {
 String className = "InvalidClass";
 typeParser.findClass(className, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindClass_MissingClass() throws Exception {
 String className = "MissingClass";
 typeParser.findClass(className, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void test_Problem() throws Exception {
 MyTokenizer tokens = new MyTokenizer("InvalidInput");
 IllegalArgumentException exception = typeParser._problem(tokens, "ProblemMessage");
 String actualMessage = exception.getMessage();
 String expectedMessage = "Failed to parse type 'InvalidInput' (remaining: ''): ProblemMessage";
 assertEquals(expectedMessage, actualMessage);
 }
}