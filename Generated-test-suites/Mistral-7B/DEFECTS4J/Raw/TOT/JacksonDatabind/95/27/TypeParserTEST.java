import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeDescriptor;
import com.fasterxml.jackson.databind.TypeFunction;

public class TypeParserTest {

 private TypeFactory typeFactory;
 private TypeParser typeParser;

 @Before
 public void setup() {
 typeFactory = new TypeFactory();
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testParseSimpleClass() {
 String canonical = "int";
 JavaType javaType = typeParser.parse(canonical);
 TypeDescriptor typeDescriptor = typeFactory.constructType(javaType.getTypeId());
 assertEquals(TypeDescriptor.VALUE_TYPE, typeDescriptor.getBasicType());
 }

 @Test
 public void testParseClassWithGenerics() {
 String canonical = "java.util.List<java.util.Map<String, Integer>>";
 JavaType javaType = typeParser.parse(canonical);
 TypeDescriptor typeDescriptor = typeFactory.constructType(javaType.getTypeId());
 TypeFunction typeFunction = (TypeFunction) typeDescriptor.getRawClass().getGenericSuperclass();
 assertEquals(TypeFunction.CONTAINER_TYPE, typeFunction.getStaticType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnexpectedToken() {
 String canonical = "int<";
 typeParser.parse(canonical);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnexpectedEndOfString() {
 String canonical = "java.util.List<java.util.Map<String, Integer>";
 typeParser.parse(canonical);
 }
}