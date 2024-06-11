import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeParserTest {

 private TypeFactory typeFactory;
 private TypeParser typeParser;

 @Before
 public void setup() {
 typeFactory = new TypeFactory();
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testParse_ValidType() {
 String canonical = "java.lang.String";
 JavaType expectedType = typeFactory.constructType(canonical);
 JavaType actualType = typeParser.parse(canonical);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testParse_ClassWithGenericType() {
 String canonical = "java.util.List<java.lang.Integer>";
 JavaType expectedType = TypeBindings.construct(Integer.class, List.class);
 JavaType actualType = typeParser.parse(canonical);
 assertEquals(expectedType, actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_InvalidType() {
 String canonical = "invalidType";
 try {
 typeParser.parse(canonical);
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Failed to parse type '"+canonical+"' (remaining: ''): Unexpected end-of-string";
 assertEquals(expectedMessage, e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_MissingToken() {
 String canonical = "java.lang.String<";
 try {
 typeParser.parse(canonical);
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Failed to parse type 'java.lang.String<'" +
                          " (remaining: ''): Unexpected end-of-string";
 assertEquals(expectedMessage, e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_InvalidToken() {
 String canonical = "java.lang.String%";
 try {
 typeParser.parse(canonical);
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Failed to parse type 'java.lang.String%' (remaining: ''): " +
                          "Unexpected token '%'";
 assertEquals(expectedMessage, e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_MissingClass() {
 String canonical = "java.lang.<String>";
 try {
 typeParser.parse(canonical);
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Can not locate class 'java.lang.'";
 assertEquals(expectedMessage, e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_MissingClass2() {
 String canonical = "java.lang.String.<>";
 try {
 typeParser.parse(canonical);
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Can not locate class 'java.lang.String'";
 assertEquals(expectedMessage, e.getMessage());
 }
 }
}