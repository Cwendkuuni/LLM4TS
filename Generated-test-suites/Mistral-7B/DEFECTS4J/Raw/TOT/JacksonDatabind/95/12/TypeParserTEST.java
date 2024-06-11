import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {

 private TypeFactory typeFactory = new TypeFactory();
 private TypeParser typeParser;

 @Before
 public void setUp() {
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testParseSimpleType() {
 String canonicalType = "int";
 JavaType parsedType = typeParser.parse(canonicalType);
 assertEquals(JavaType.getType(int.class), parsedType);
 }

 @Test
 public void testParseClassType() {
 String canonicalType = "java.lang.String";
 JavaType parsedType = typeParser.parse(canonicalType);
 assertEquals(JavaType.getType(String.class), parsedType);
 }

 @Test
 public void testParseArrayType() {
 String canonicalType = "int[]";
 JavaType parsedType = typeParser.parse(canonicalType);
 JavaType expectedType = typeFactory.constructArrayType(JavaType.getType(int.class));
 assertEquals(expectedType, parsedType);
 }

 @Test
 public void testParseGenericType() {
 String canonicalType = "java.util.List<java.lang.String>";
 JavaType parsedType = typeParser.parse(canonicalType);
 JavaType expectedType = new TypeReference<List<String>>(){}.getType();
 assertEquals(expectedType, parsedType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() {
 String canonicalType = "java.util.List<java.lang.Integer>abc";
 typeParser.parse(canonicalType);
 fail("Expected IllegalArgumentException not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingType() {
 String canonicalType = "java.util.List>";
 typeParser.parse(canonicalType);
 fail("Expected IllegalArgumentException not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnclosedType() {
 String canonicalType = "java.util.List<java.lang.String<";
 typeParser.parse(canonicalType);
 fail("Expected IllegalArgumentException not thrown");
 }
}