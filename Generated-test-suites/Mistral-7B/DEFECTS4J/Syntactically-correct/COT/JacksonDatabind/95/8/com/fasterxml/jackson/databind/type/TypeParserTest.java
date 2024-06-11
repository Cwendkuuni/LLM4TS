import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;
import com.fasterxml.jackson.databind.type.MyTokenizer;

public class TypeParserTest {

 private TypeFactory typeFactory;
 private TypeParser typeParser;

 @Before
 public void setup() {
 typeFactory = new TypeFactory();
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testParseNullCanonical() {
 try {
 typeParser.parse(null);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseEmptyString() {
 try {
 JavaType javaType = typeParser.parse("");
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseSimpleType() {
 String canonical = "int";
 JavaType javaType = typeParser.parse(canonical);
 assertEquals("java.lang.Integer", javaType.getClassName());
 }

 @Test
 public void testParseClassType() {
 String canonical = "java.util.ArrayList";
 JavaType javaType = typeParser.parse(canonical);
 assertEquals("java.util.ArrayList", javaType.getClassName());
 }

 @Test
 public void testParseGenericType() {
 String canonical = "java.util.ArrayList<java.lang.String>";
 JavaType javaType = typeParser.parse(canonical);
 assertEquals("java.util.ArrayList<java.lang.String>", javaType.getCanonicalName());
 }

 @Test
 public void testParseMultipleTypes() {
 String canonical = "java.util.ArrayList<java.lang.String>, java.util.HashMap<java.lang.String, java.lang.Integer>";
 List<JavaType> javaTypes = typeParser.parseTypes(new MyTokenizer(canonical));
 assertEquals(2, javaTypes.size());
 assertEquals("java.util.ArrayList<java.lang.String>", javaTypes.get(0).getCanonicalName());
 assertEquals("java.util.HashMap<java.lang.String, java.lang.Integer>", javaTypes.get(1).getCanonicalName());
 }

 @Test
 public void testParseInvalidToken() {
 String canonical = "java.util.ArrayList<java.lang.String>*";
 try {
 typeParser.parseTypes(new MyTokenizer(canonical));
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 String message = e.getMessage();
 assertTrue(message.contains("Unexpected token '*'"));
 }
 }

 @Test
 public void testFindClass() {
 try {
 Class<?> clazz = typeParser.findClass("java.util.ArrayList", new MyTokenizer(""));
 assertEquals("java.util.ArrayList", clazz.getName());
 } catch (Exception e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testFindClassWithProblem() {
 String canonical = "java.util.ArrayList<java.lang.String>";
 try {
 Class<?> clazz = typeParser.findClass("java.nx.ArrayList<java.lang.String>", new MyTokenizer(canonical));
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 String message = e.getMessage();
 assertTrue(message.contains("Can not locate class 'java.nx.ArrayList'"));
 }
 }
}