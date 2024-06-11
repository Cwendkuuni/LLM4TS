import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory.DefaultTypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeParserTest {

private static final TypeFactory _factory = new DefaultTypeFactory();

@Test
public void testParse() throws Exception {
String canonical = "java.util.List<com.fasterxml.jackson.databind.JsonNode>";
JavaType expected = _factory.constructType(List.class, JsonNode.class);
JavaType actual = new TypeParser(_factory).parse(canonical);
assertEquals(expected, actual);
}

@Test
public void testParseTypes() throws Exception {
String canonical = "java.util.List<com.fasterxml.jackson.databind.JsonNode>, java.lang.Integer>";
List<JavaType> expected = Arrays.asList(JavaType.construct(List.class, JsonNode.class), Integer.TYPE);
List<JavaType> actual = new TypeParser(_factory).parseTypes(new MyTokenizer(canonical));
assertEquals(expected, actual);
}

@Test
public void testFindClass() throws Exception {
String className = "java.util.List";
Class<?> expected = List.class;
Class<?> actual = new TypeParser(_factory).findClass(className, new MyTokenizer(canonical));
assertEquals(expected, actual);
}

@Test(expected = IllegalArgumentException.class)
public void testParseEmptyString() throws Exception {
String canonical = "";
new TypeParser(_factory).parse(canonical);
fail("Expected IllegalArgumentException");
}

@Test(expected = IllegalArgumentException.class)
public void testParseInvalidCanonicalString() throws Exception {
String canonical = "java.util.List#<com.fasterxml.jackson.databind.JsonNode>>";
new TypeParser(_factory).parse(canonical);
fail("Expected IllegalArgumentException");
}

@Test(expected = IllegalArgumentException.class)
public void testFindNonExistentClass() throws Exception {
String className = "com.fasterxml.jackson.databind.NonExistentClass";
new TypeParser(_factory).findClass(className, new MyTokenizer(canonical));
fail("Expected IllegalArgumentException");
}

@Test(expected = IllegalArgumentException.class)
public void testParseMissingClosingGreaterThan() throws Exception {
String canonical = "java.util.List<com.fasterxml.jackson.databind.JsonNode";
new TypeParser(_factory).parse(canonical);
fail("Expected IllegalArgumentException");
}

@Test(expected = IllegalArgumentException.class)
public void testParseMissingComma() throws Exception {
String canonical = "java.util.List<com.fasterxml.jackson.databind.JsonNode, java.lang.Integer>";
new TypeParser(_factory).parseTypes(new MyTokenizer(canonical));
fail("Expected IllegalArgumentException");
}

@Test
public void testParseRepeatedType() throws Exception {
String canonical = "java.util.List<java.util.List<com.fasterxml.jackson.databind.JsonNode>, com.fasterxml.jackson.databind.JsonNode>";
List<JavaType> expected = Arrays.asList(List.class, List.class, JsonNode.class);
List<JavaType> actual = new TypeParser(_factory).parseTypes(new MyTokenizer(canonical));
assertEquals(expected, actual);
}

@Test
public void testParseCyclicDependency() throws Exception {
String canonical = "java.util.List<java.util.List<java.util.List<com.fasterxml.jackson.databind.JsonNode>>, com.fasterxml.jackson.databind.JsonNode>";
List<JavaType> expected = Arrays.asList(List.class, List.class, List.class, List.class, JsonNode.class);
List<JavaType> actual = new TypeParser(_factory).parseTypes(new MyTokenizer(canonical));
assertEquals(expected, actual);
}
}