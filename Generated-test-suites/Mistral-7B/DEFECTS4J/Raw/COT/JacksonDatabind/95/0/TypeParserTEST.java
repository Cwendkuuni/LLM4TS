import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeParserTest {

 private TypeFactory typeFactory;
 private TypeParser typeParser;

 @Before
 public void setUp() {
 typeFactory = new TypeFactory();
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testParse_ValidType() {
 String canonical = "java.util.List<java.lang.Integer>";
 JavaType javaType = typeParser.parse(canonical);
 Class<?> javaClass = javaType.getRawClass();
 assertEquals("java.util.List", javaClass.getName());
 assertTrue(List.class.isAssignableFrom(javaClass));
 assertEquals(Integer.class, javaType.getContentType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_InvalidType() {
 String canonical = "java.util.List<java.lang.Integer>asd";
 try {
 typeParser.parse(canonical);
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Failed to parse type 'asd' (remaining: 'asd'): Unexpected token 'asd', expected ',' or '>'";
 String actualMessage = e.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_EmptyType() {
 String canonical = "";
 try {
 typeParser.parse(canonical);
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Unexpected end-of-string";
 String actualMessage = e.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }
 }

 @Test
 public void testParseType_ValidClass() {
 MyTokenizer tokens = new MyTokenizer("java.lang.String");
 JavaType javaType = typeParser.parseType(tokens);
 Class<?> javaClass = javaType.getRawClass();
 assertEquals("java.lang.String", javaClass.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseType_InvalidToken() {
 MyTokenizer tokens = new MyTokenizer("java.lang.Stringasd");
 try {
 typeParser.parseType(tokens);
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Unexpected token 'asd' in class name";
 String actualMessage = e.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }
 }

 @Test
 public void testParseTypes_ValidTypes() {
 MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.Integer>,java.util.Map<java.lang.String,java.lang.String>");
 List<JavaType> javaTypes = typeParser.parseTypes(tokens);
 assertEquals(2, javaTypes.size());
 JavaType javaType1 = javaTypes.get(0);
 Class<?> javaClass1 = javaType1.getRawClass();
 assertEquals("java.util.List", javaClass1.getName());
 assertTrue(List.class.isAssignableFrom(javaClass1));
 assertEquals(Integer.class, javaType1.getContentType());
 JavaType javaType2 = javaTypes.get(1);
 Class<?> javaClass2 = javaType2.getRawClass();
 assertEquals("java.util.Map", javaClass2.getName());
 assertTrue(Map.class.isAssignableFrom(javaClass2));
 assertEquals(String.class, javaType2.getKeyType());
 assertEquals(String.class, javaType2.getValueType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseTypes_InvalidToken() {
 MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.Integer>,java.util.Map<java.lang.String,java.lang.String>asd");
 try {
 typeParser.parseTypes(tokens);
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Unexpected token 'asd'";
 String actualMessage = e.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }
 }

 @Test
 public void testFindClass_ValidClass() {
 String className = "java.lang.String";
 Class<?> javaClass = typeParser.findClass(className, null);
 assertEquals("java.lang.String", javaClass.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindClass_InvalidClassName() {
 String className = "java.lang.asdString";
 try {
 typeParser.findClass(className, null);
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Can not locate class 'java.lang.asdString'";
 String actualMessage = e.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }
 }

 @Test
 public void testProblem_ValidMessage() {
 MyTokenizer tokens = new MyTokenizer("");
 IllegalArgumentException expectedException = new IllegalArgumentException("Unexpected end-of-string");
 IllegalArgumentException actualException = typeParser._problem(tokens, "Unexpected end-of-string");
 assertEquals(expectedException.getMessage(), actualException.getMessage());
 }

}