import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {

 private TypeFactory typeFactory;
 private TypeParser typeParser;

 @Before
 public void setup() {
 typeFactory = new TypeFactory();
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testParse_validTypeName() throws Exception {
 String canonicalType = "java.util.List<com.fasterxml.jackson.databind.JsonNode>";
 JavaType parsedType = typeParser.parse(canonicalType);
 assertEquals(JavaType.constructParametrizedType(List.class, JsonNode.class), parsedType);
 }

 @Test
 public void testParse_validClassType() throws Exception {
 String canonicalType = "java.util.List";
 JavaType parsedType = typeParser.parse(canonicalType);
 assertEquals(JavaType.constructRawType(List.class), parsedType);
 }

 @Test
 public void testParse_emptyInput() throws Exception {
 assertThrows(IllegalArgumentException.class, () -> typeParser.parse(""));
 }

 @Test
 public void testParse_invalidInput() throws Exception {
 assertThrows(IllegalArgumentException.class, () -> typeParser.parse("invalidTypeName"));
 }

 @Test
 public void testParseType_validTypeNameAndGenericTypes() throws Exception {
 MyTokenizer tokens = new MyTokenizer("java.util.List<com.fasterxml.jackson.databind.JsonNode<Integer>>");
 JavaType parsedType = typeParser.parseType(tokens);
 List<JavaType> expectedTypes = Arrays.asList(List.class, JsonNode.class, Integer.class);
 assertEquals(TypeBindings.create(List.class, expectedTypes.toArray(new JavaType[0])), parsedType.getTypeBindings());
 }

 @Test
 public void testParseType_validClassType() throws Exception {
 MyTokenizer tokens = new MyTokenizer("java.util.List");
 JavaType parsedType = typeParser.parseType(tokens);
 assertEquals(JavaType.constructRawType(List.class), parsedType);
 }

 @Test
 public void testParseTypes_validTypes() throws Exception {
 MyTokenizer tokens = new MyTokenizer("java.util.List<com.fasterxml.jackson.databind.JsonNode<Integer>>,java.util.Map<String,Integer>");
 List<JavaType> parsedTypes = typeParser.parseTypes(tokens);
 List<JavaType> expectedTypes = Arrays.asList(JavaType.constructParametrizedType(List.class, JsonNode.class), JavaType.constructParametrizedType(Map.class, String.class, Integer.class));
 assertEquals(expectedTypes, parsedTypes);
 }

 @Test
 public void testParseTypes_incompleteInput() throws Exception {
 MyTokenizer tokens = new MyTokenizer("java.util.List<com.fasterxml.jackson.databind.JsonNode<Integer>>,");
 assertThrows(IllegalArgumentException.class, () -> typeParser.parseTypes(tokens));
 }

 @Test
 public void testParseTypes_missingClosingBracket() throws Exception {
 MyTokenizer tokens = new MyTokenizer("java.util.List<com.fasterxml.jackson.databind.JsonNode<Integer>");
 assertThrows(IllegalArgumentException.class, () -> typeParser.parseTypes(tokens));
 }

 @Test
 public void testFindClass_validClassname() throws Exception {
 String className = "java.util.List";
 JavaType javaType = typeParser.findClass(className, new MyTokenizer(className));
 assertEquals(JavaType.constructRawType(List.class), javaType);
 }

 @Test
 public void testFindClass_invalidClassname() throws Exception {
 String className = "InvalidClassName";
 assertThrows(IllegalArgumentException.class, () -> typeParser.findClass(className, new MyTokenizer(className)));
 }
}