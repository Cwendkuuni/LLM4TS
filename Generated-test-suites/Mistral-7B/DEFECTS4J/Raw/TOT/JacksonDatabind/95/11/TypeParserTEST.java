import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {

 private TypeFactory typeFactory = new TypeFactoryBuilder().build();
 private TypeParser typeParser;

 @Test
 public void testParseSimpleClass() {
 typeParser = new TypeParser(typeFactory);
 JavaType javaType = typeParser.parse("int");
 assertEquals(JavaType.getTypeFactory().constructType(int.class), javaType);
 }

 @Test
 public void testParseClassWithGenerics() {
 typeParser = new TypeParser(typeFactory);
 JavaType javaType = typeParser.parse("java.util.List<String>");
 TypeReference<List<String>> expectedType = new TypeReference<List<String>>() {};
 assertEquals(expectedType.getType(), javaType);
 }

 @Test
 public void testParseClassWithInnerClasses() {
 typeParser = new TypeParser(typeFactory);
 JavaType javaType = typeParser.parse("java.lang.String[][]");
 TypeReference<String[][]> expectedType = new TypeReference<String[][]>() {};
 assertEquals(expectedType.getType(), javaType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidClassWithGenerics() {
 typeParser = new TypeParser(typeFactory);
 JavaType javaType = typeParser.parse("java.util.List<String<String>>");
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidClass() {
 typeParser = new TypeParser(typeFactory);
 JavaType javaType = typeParser.parse("java.util.List<String>");
 typeParser = typeParser.withFactory(null);
 JavaType javaType2 = typeParser.parse("java.util.List<String>");
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidStringWithUnexpectedToken() {
 typeParser = new TypeParser(typeFactory);
 MyTokenizer tokens = new MyTokenizer("int<");
 JavaType javaType = typeParser.parseType(tokens);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidStringWithUnexpectedEndOfString() {
 typeParser = new TypeParser(typeFactory);
 MyTokenizer tokens = new MyTokenizer("int>");
 JavaType javaType = typeParser.parseType(tokens);
 tokens.nextToken(); // consume '>'
 fail("Should throw IllegalArgumentException");
 }
}