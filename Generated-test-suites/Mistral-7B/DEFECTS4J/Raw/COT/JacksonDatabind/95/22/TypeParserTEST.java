import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
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
 public void testParseNullCanonical() {
 try {
 typeParser.parse(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseEmptyCanonical() {
 JavaType result = typeParser.parse("");
 assertEquals(JavaType.EMPTY_ARRAY_TYPE, result);
 }

 @Test
 public void testParseSimpleClassName() throws Exception {
 JavaType result = typeParser.parse("String");
 JavaType expected = typeFactory.constructType(String.class);
 assertEquals(expected, result);
 }

 @Test
 public void testParseClassNameWithGenerics() throws Exception {
 JavaType result = typeParser.parse("List<String>");
 JavaType expected = typeFactory.constructType(new TypeReference<List<String>>(){}.getType());
 assertEquals(expected, result);
 }

 @Test
 public void testParseInvalidToken() throws Exception {
 try {
 typeParser.parse("List<String>X");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 String message = e.getMessage();
 assertTrue(message.contains("Unexpected token 'X'"));
 }
 }

 @Test
 public void testParseMissingComma() throws Exception {
 try {
 typeParser.parse("List<String>String");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 String message = e.getMessage();
 assertTrue(message.contains("Unexpected token 'String'"));
 }
 }

 @Test
 public void testParseMissingGreaterThan() throws Exception {
 try {
 typeParser.parse("List<String>String<");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 String message = e.getMessage();
 assertTrue(message.contains("Unexpected end-of-string"));
 }
 }

 @Test
 public void testFindClassNullCanonical() {
 try {
 typeParser.findClass(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFindClassEmptyCanonical() {
 try {
 typeParser.findClass("");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFindClassInvalidClassName() {
 try {
 typeParser.findClass("X");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 String message = e.getMessage();
 assertTrue(message.contains("Can not locate class 'X'"));
 }
 }

 @Test
 public void testFindClassValidClassName() throws Exception {
 Class<?> result = typeParser.findClass("java.util.ArrayList");
 assertEquals(ArrayList.class, result);
 }
}