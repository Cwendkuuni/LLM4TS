import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;

public class TypeParserTest {

 private TypeFactory typeFactory = new TypeFactory();
 private TypeParser typeParser = new TypeParser(typeFactory);

 @Test
 public void testParseSimpleType() {
 String canonicalType = "int";
 JavaType actualType = typeParser.parse(canonicalType);
 JavaType expectedType = typeFactory.constructType(int.class);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testParseClassType() {
 String canonicalType = "java.util.List";
 JavaType actualType = typeParser.parse(canonicalType);
 JavaType expectedType = typeFactory.constructType(new TypeReference<List<?>>() {}.getType());
 assertEquals(expectedType, actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() {
 String canonicalType = "invalidType";
 typeParser.parse(canonicalType);
 }

 @Test
 public void testParseGenericType() {
 String canonicalType = "java.util.List<java.lang.String>";
 JavaType actualType = typeParser.parse(canonicalType);
 JavaType expectedType = typeFactory.constructType(new TypeReference<List<String>>() {}.getType());
 assertEquals(expectedType, actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingCloseBracket() {
 String canonicalType = "java.util.List<java.lang.String<";
 typeParser.parse(canonicalType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidTokenInGenericType() {
 String canonicalType = "java.util.List<java.lang.String#>";
 typeParser.parse(canonicalType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidClass() {
 String canonicalType = "java.util.ListInvalidClass";
 typeParser.parse(canonicalType);
 }
}