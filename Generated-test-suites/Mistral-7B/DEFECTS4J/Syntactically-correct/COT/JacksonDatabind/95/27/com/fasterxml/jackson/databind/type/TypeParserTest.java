import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeParser;
import com.fasterxml.jackson.databind.util.MyTokenizer;

public class TypeParserTest {

 private TypeFactory typeFactory;
 private TypeParser typeParser;

 @Before
 public void setUp() {
 typeFactory = new TypeFactory();
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testParse_SimpleClass() {
 String canonical = "java.lang.String";
 JavaType javaType = typeParser.parse(canonical);
 Class<?> clazz = javaType.getRawClass();
 assertEquals("java.lang.String", clazz.getName());
 }

 @Test
 public void testParse_ClassWithGenerics() {
 String canonical = "java.util.ArrayList<java.lang.Integer>";
 JavaType javaType = typeParser.parse(canonical);
 assertNotNull(javaType.getParameters());
 assertEquals(1, javaType.getParameters().size());
 JavaType listElementType = javaType.getParameters().get(0);
 assertEquals("java.lang.Integer", listElementType.getRawClass().getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_InvalidInput() {
 String canonical = "java.lang.String<InvalidType>";
 try {
 typeParser.parse(canonical);
 } catch (IllegalArgumentException e) {
 String message = e.getMessage();
 assertTrue(message.contains("Failed to parse type '"));
 assertTrue(message.contains("problem: "));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_UnexpectedEndOfString() {
 String canonical = "java.lang.String";
 MyTokenizer tokens = new MyTokenizer(canonical.trim());
 typeParser.parseType(tokens);
 tokens.pushBack(">");
 try {
 typeParser.parse(canonical);
 } catch (IllegalArgumentException e) {
 String message = e.getMessage();
 assertEquals("Unexpected end-of-string", message);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_MissingClass() {
 String canonical = "com.non.existent.package.NonExistentClass";
 try {
 typeParser.parse(canonical);
 } catch (IllegalArgumentException e) {
 String message = e.getMessage();
 assertTrue(message.contains("Can not locate class '"));
 assertTrue(message.contains("problem: "));
 }
 }

 @Test
 public void testParse_EmptyString() {
 JavaType javaType = typeParser.parse("");
 assertNull(javaType);
 }
}