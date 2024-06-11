import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

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
 public void testParse_ValidType_ReturnsExpectedType() {
 String canonicalType = "java.lang.String";
 JavaType actualType = typeParser.parse(canonicalType);
 JavaType expectedType = typeFactory.constructType(canonicalType);
 assertEquals(expectedType.getRawClass(), actualType.getRawClass());
 }

 @Test
 public void testParse_EmptyString_ThrowsIllegalArgumentException() {
 assertThrows(IllegalArgumentException.class, () -> typeParser.parse(""));
 }

 @Test
 public void testParse_InvalidType_ThrowsIllegalArgumentException() {
 assertThrows(IllegalArgumentException.class, () -> typeParser.parse("invalidType"));
 }

 @Test
 public void testParseType_ValidTypeWithoutGenerics_ReturnsExpectedType() {
 MyTokenizer tokens = new MyTokenizer("java.lang.String");
 JavaType actualType = typeParser.parseType(tokens);
 JavaType expectedType = typeFactory._fromClass(null, JavaType.getType(String.class), null);
 assertEquals(expectedType.getRawClass(), actualType.getRawClass());
 }

 @Test
 public void testParseType_ValidTypeWithGenerics_ReturnsExpectedType() {
 MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.Integer>");
 List<JavaType> parameterTypes = Arrays.asList(JavaType.getType(Integer.class));
 TypeBindings bindings = TypeBindings.create(JavaType.getType(List.class), parameterTypes);
 JavaType actualType = typeParser.parseType(tokens);
 JavaType expectedType = typeFactory._fromClass(null, JavaType.getType(List.class), bindings);
 assertEquals(expectedType.getRawClass(), actualType.getRawClass());
 }

 @Test
 public void testParseTypes_ValidTypes_ReturnsExpectedTypes() {
 MyTokenizer tokens = new MyTokenizer("java.lang.String, java.util.List<java.lang.Integer>");
 JavaType stringType = typeParser.parseType(tokens);
 JavaType listType = typeParser.parseType(tokens);
 assertEquals(JavaType.getType(String.class), stringType);
 assertEquals(typeFactory._fromClass(null, JavaType.getType(List.class), null), listType);
 }

 @Test
 public void testParseTypes_MissingClosingBracket_ThrowsIllegalArgumentException() {
 MyTokenizer tokens = new MyTokenizer("java.lang.String, java.util.List<java.lang.Integer");
 assertThrows(IllegalArgumentException.class, () -> typeParser.parseTypes(tokens));
 }

 @Test
 public void testFindClass_ValidClass_ReturnsExpectedClass() {
 String className = "java.lang.String";
 Class<?> actualClass = typeParser.findClass(className, new MyTokenizer(className));
 assertNotNull(actualClass);
 assertEquals(String.class, actualClass);
 }

 @Test
 public void testFindClass_InvalidClass_ThrowsIllegalArgumentException() {
 String className = "InvalidClass";
 assertThrows(IllegalArgumentException.class, () -> typeParser.findClass(className, new MyTokenizer(className)));
 }
}