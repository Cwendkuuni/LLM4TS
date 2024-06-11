import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
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
 public void testParseEmptyString() {
 try {
 JavaType javaType = typeParser.parse("");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
 }
 }

 @Test
 public void testParseSimpleClass() {
 JavaType javaType = typeParser.parse("int");
 assertEquals(typeFactory.unconstructedType(int.class), javaType);
 }

 @Test
 public void testParseClassWithGenerics() {
 JavaType javaType = typeParser.parse("java.util.List<String>");
 TypeBindings typeBindings = (TypeBindings) javaType.getTypeBindings();
 assertEquals(String.class, typeBindings.getRawClass());
 assertEquals(List.class, typeBindings.getParameterTypes().get(0).getRawClass());
 }

 @Test
 public void testParseClassWithMultipleGenerics() {
 JavaType javaType = typeParser.parse("java.util.Map<Integer, java.util.List<String>>");
 TypeBindings typeBindings = (TypeBindings) javaType.getTypeBindings();
 assertEquals(Integer.class, typeBindings.getRawClass());
 assertEquals(List.class, typeBindings.getParameterTypes().get(0).getRawClass());
 assertEquals(String.class, typeBindings.getParameterTypes().get(1).getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidCanonicalType() {
 typeParser.parse("java.util.List<int>String>");
 fail("Unexpectedly did not throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidToken() {
 typeParser.parse("int<");
 fail("Unexpectedly did not throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingClosingBracket() {
 typeParser.parse("java.util.List<String>");
 typeParser.parse("<");
 fail("Unexpectedly did not throw IllegalArgumentException");
 }
}