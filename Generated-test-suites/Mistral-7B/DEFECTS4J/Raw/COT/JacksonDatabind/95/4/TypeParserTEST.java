package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.ClassIdResolver;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializerBase;

/**
 * Test class for TypeParser
 */
public class TypeParserTest {

 private TypeFactory _factory;
 private TypeDeserializer _td;
 private TypeSerializer _ts;
 private ClassIdResolver _classIdResolver;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 _factory = new TypeFactoryBuilder().build();
 _td = new TypeDeserializer() {
 @Override
 public JavaType deserializeTyped(String tn, DeserializationContext ctxt) {
 return ctxt.constructSpecializedType(TypeParser.class.getTypeName(), tn);
 }
 };
 _ts = new TypeSerializerBase() {
 @Override
 public JavaType getType(SerializeConfig config, TypeSerializerProvider provider) {
 return TypeParser.class;
 }
 };
 _classIdResolver = new ClassIdResolver() {
 @Override
 public Class<?> resolveClassId(int id) {
 return null;
 }
 };
 }

 @Test
 public void testParse_validType() throws Exception {
 String canonical = "java.lang.String";
 JavaType expected = _factory.constructType(canonical);
 JavaType actual = new TypeParser(_factory).parse(canonical);
 assertEquals(expected, actual);
 }

 @Test
 public void testParse_validGenericType() throws Exception {
 String canonical = "java.util.List<java.lang.String>";
 JavaType expected = TypeBindings.constructParametrizedType(List.class, String.class);
 JavaType actual = new TypeParser(_factory).parse(canonical);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_invalidType() throws Exception {
 String canonical = "java.lang.String<java.util.List";
 new TypeParser(_factory).parse(canonical);
 }

 @Test
 public void testParseTypes_validTypeList() throws Exception {
 String canonical = "java.lang.String, java.util.List<java.lang.String>, java.util.List<java.lang.Integer>";
 List<JavaType> expected = new ArrayList<JavaType>() {{
 add(TypeParser.constructType(String.class));
 add(TypeParser.constructType(new ParameterizedType() {{
 setRawType(List.class);
 setTypeArguments(new Type[] {String.class});
 }}));
 add(TypeParser.constructType(new ParameterizedType() {{
 setRawType(List.class);
 setTypeArguments(new Type[] {Integer.class});
 }}));
 }};
 List<JavaType> actual = new TypeParser(_factory).parseTypes(new MyTokenizer("[" + canonical + "]"));
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseTypes_invalidTypeList() throws Exception {
 new TypeParser(_factory).parseTypes(new MyTokenizer("[" + "java.lang.String<java.util.List" + "]"));
 }

 @Test
 public void testFindClass_validClassName() throws Exception {
 String className = "java.lang.String";
 Class<?> expected = _factory.findClass(className);
 Class<?> actual = new TypeParser(_factory).findClass(className, new MyTokenizer(canonical));
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindClass_invalidClassName() throws Exception {
 String className = "java.lang.String<java.util.List";
 new TypeParser(_factory).findClass(className, new MyTokenizer(""));
 }

 @Test
 public void testProblem() throws Exception {
 MyTokenizer tokens = new MyTokenizer("");
 IllegalArgumentException exception = new TypeParser(_factory)._problem(tokens, "Expected error message");
 String expectedMessage = "Failed to parse type '' (remaining: ''):"
 + " Expected error message";
 String actualMessage = exception.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }
}