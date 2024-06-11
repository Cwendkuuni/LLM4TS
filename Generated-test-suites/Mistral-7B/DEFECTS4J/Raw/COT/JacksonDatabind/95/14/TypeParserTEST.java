import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeParserTest {

 private TypeFactory _factory;
 private TypeParser _parser;

 @Before
 public void setup() {
 _factory = new TypeFactory();
 _parser = new TypeParser(_factory);
 }

 @Test
 public void testParseValidType() {
 String canonicalType = "java.util.List<com.fasterxml.jackson.databind.JsonNode>";
 JavaType parsedType = _parser.parse(canonicalType);
 TypeReference typeRef = new TypeReference<List<JsonNode>>() {};
 assertEquals(typeRef.getType(), parsedType);
 }

 @Test
 public void testParseClassType() {
 String canonicalType = "java.util.String";
 JavaType parsedType = _parser.parse(canonicalType);
 JavaType expectedType = _factory._fromClass(null, String.class, null);
 assertEquals(expectedType, parsedType);
 }

 @Test
 public void testParseGenericType() {
 String canonicalType = "java.util.List<com.fasterxml.jackson.databind.JsonNode>";
 JavaType parsedType = _parser.parse(canonicalType);
 TypeReference typeRef = new TypeReference<List<JsonNode>>() {};
 JavaType expectedType = TypeBindings.constructParametrizedType(List.class, typeRef);
 assertEquals(expectedType, parsedType);
 }

 @Test
 public void testParseEmptyTypeList() {
 String canonicalType = "java.util.List<>";
 JavaType parsedType = _parser.parse(canonicalType);
 assertNull(parsedType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() {
 String canonicalType = "java.util.List<com.fasterxml.jackson.databind.JsonNode<";
 _parser.parse(canonicalType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnexpectedToken() {
 String canonicalType = "java.util.List<com.fasterxml.jackson.databind.JsonNode<,>";
 _parser.parse(canonicalType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingClosingBracket() {
 String canonicalType = "java.util.List<com.fasterxml.jackson.databind.JsonNode<";
 _parser.parse(canonicalType);
 }
}