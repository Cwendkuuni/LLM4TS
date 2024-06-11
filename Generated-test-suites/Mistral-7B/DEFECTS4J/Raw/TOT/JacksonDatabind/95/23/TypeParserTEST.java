import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {

 private TypeFactory typeFactory = new TypeFactory();
 private TypeParser typeParser = new TypeParser(typeFactory);

 @Test
 public void testParseSimpleClass() {
 String canonical = "int";
 JavaType result = typeParser.parse(canonical);
 assertEquals(JavaType.getTypeFactory().constructType(int.class), result);
 }

 @Test
 public void testParseClassWithGenerics() {
 String canonical = "java.util.List<java.lang.String>";
 JavaType result = typeParser.parse(canonical);
 TypeFactory tf = typeFactory;
 JavaType listType = tf.constructCollectionType(List.class, String.class);
 TypeBindings bindings = new TypeBindings(String.class, listType);
 JavaType expected = tf.constructType(List.class, String.class);
 JavaType actual = typeParser.withFactory(tf).parse(canonical);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidCanonical() {
 String canonical = "java.util.<List<String> string";
 typeParser.parse(canonical);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnexpectedToken() {
 String canonical = "java.util.List<java.lang.String> # ";
 typeParser.parse(canonical);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingEndToken() {
 String canonical = "java.util.List<java.lang.String>";
 typeParser.parse(canonical);
 typeParser.parse(canonicial); // missing ">" at the end
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnknownClass() {
 String canonical = "com.unknown.MyUnknownClass";
 typeParser.parse(canonical);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseClassNotFound() {
 String canonical = "java.lang.XNotAFoundClass";
 typeParser.parse(canonical);
 }

 @Test
 public void testParseClassWithArray() {
 String canonical = "[Ljava.lang.String;";
 JavaType result = typeParser.parse(canonical);
 TypeFactory tf = typeFactory;
 JavaType javaStringType = tf.constructType(String.class);
 JavaType javaStringArrayType = tf.constructArrayType(javaStringType, null);
 JavaType expected = tf.constructType(String[].class);
 JavaType actual = typeParser.parse(canonical);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseClassWithArrayAndGenerics() {
 String canonical = "[Ljava.util.List<java.lang.String>;";
 JavaType result = typeParser.parse(canonical);
 TypeFactory tf = typeFactory;
 JavaType javaStringType = tf.constructType(String.class);
 JavaType javaListType = tf.constructCollectionType(List.class, String.class);
 JavaType javaListArrayType = tf.constructArrayType(javaListType, null);
 JavaType expected = tf.constructType(List[].class);
 JavaType actual = typeParser.parse(canonical);
 assertEquals(expected, actual);
 }
}