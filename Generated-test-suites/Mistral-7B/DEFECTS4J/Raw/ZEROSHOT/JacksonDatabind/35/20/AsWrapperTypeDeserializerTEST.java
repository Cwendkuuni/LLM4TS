package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseMapTest;

public class AsWrapperTypeDeserializerTest extends BaseMapTest {

 private static final String CLASS_NAME = AsWrapperTypeDeserializer.class.getName();

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedFromObject() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("com.example.Person"), new StdTypeIdResolver(), "type", true, Person.class);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setTypeFactory(mapper.getTypeFactory().decorateWith(new CustomTypeFactory()));
 ObjectNode node = mapper.createObjectNode();
 node.put("type", "com.example.Student");
 node.put("Student", mapper.valueToTree(new Student("Alice", 20)));
 Object readValue = mapper.readValue(node.toString(), deserializer.getType());
 Student student = (Student) readValue;
 assertEquals("Alice", student.name);
 assertEquals(20, student.age);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedFromArray() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("com.example.Person"), new StdTypeIdResolver(), "type", true, Person.class);
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode node = mapper.createObjectNode();
 node.put("type", "com.example.Student");
 node.put("Student", mapper.valueToTree(new Student("Alice", 20)));
 ObjectNode nodeArray = mapper.createObjectNode();
 nodeArray.add(mapper.valueToTree(node));
 Object readValue = mapper.readValue(nodeArray.toString(), deserializer.getType());
 Student student = (Student) readValue;
 assertEquals("Alice", student.name);
 assertEquals(20, student.age);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedFromScalar() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("com.example.Person"), new StdTypeIdResolver(), "type", true, Person.class);
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode node = mapper.createObjectNode();
 node.put("type", "com.example.Student");
 node.put("Student", mapper.valueToTree(new Student("Alice", 20)));
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
 Object readValue = mapper.readValue(json.substring(json.lastIndexOf(":"), json.length()), deserializer.getType());
 Student student = (Student) readValue;
 assertEquals("Alice", student.name);
 assertEquals(20, student.age);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedFromAny() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("com.example.Person"), new StdTypeIdResolver(), "type", true, Person.class);
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode node = mapper.createObjectNode();
 node.put("type", "com.example.Student");
 node.put("Student", mapper.valueToTree(new Student("Alice", 20)));
 Object nodeAny = mapper.valueToTree("{\"type\":\"com.example.Student\", \"Student\":{\"name\":\"Alice\", \"age\":20}}");
 Object readValue = mapper.readValue(nodeAny, deserializer.getType());
 Student student = (Student) readValue;
 assertEquals("Alice", student.name);
 assertEquals(20, student.age);
 }

 @Test
 public void testNullTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("com.example.Person"), new StdTypeIdResolver(), "type", true, Person.class);
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{}");
 parser.nextToken(); //START_OBJECT
 parser.nextToken(); //FIELD_NAME
 parser.nextToken(); //VALUE_NULL
 parser.nextToken(); //END_OBJECT
 Object readValue = deserializer.deserializeTypedFromObject(parser, context());
 assertNull(readValue);
 }

 @Test
 public void testEmptyTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("com.example.Person"), new StdTypeIdResolver(), "type", true, Person.class);
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{ \"type\":\"\" }");
 parser.nextToken(); //START_OBJECT
 parser.nextToken(); //FIELD_NAME
 parser.nextToken(); //VALUE_STRING
 parser.nextToken(); //END_OBJECT
 Object readValue = deserializer.deserializeTypedFromObject(parser, context());
 assertNull(readValue);
 }

 @Test
 public void testWrongTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("com.example.Person"), new StdTypeIdResolver(), "type", true, Person.class);
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{ \"type\":\"com.example.OtherPerson\" }");
 parser.nextToken(); //START_OBJECT
 parser.nextToken(); //FIELD_NAME
 parser.nextToken(); //VALUE_STRING
 parser.nextToken(); //END_OBJECT
 try {
 Object readValue = deserializer.deserializeTypedFromObject(parser, context());
 fail();
 } catch (JsonMappingException e) {
 assertEquals("Not type compatible: com.example.OtherPerson(number=0) is not subtype", e.getMessage());
 }
 }

 @Test
 public void testTypeIdVisible() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("com.example.Person"), new StdTypeIdResolver(), "type", true, Person.class);
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode node = mapper.createObjectNode();
 node.put("type", "com.example.Student");
 node.put("Student", mapper.valueToTree(new Student("Alice", 20)));
 node.put("typeId", "com.example.Teacher");
 Object readValue = mapper.readValue(node.toString(), deserializer.getType());
 Teacher teacher = (Teacher) readValue;
 assertEquals("Alice", teacher.name);
 assertEquals(20, teacher.age);
 }

 @Test
 public void testInvalidJsonStructure() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("com.example.Person"), new StdTypeIdResolver(), "type", true, Person.class);
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{ \"type\":\"com.example.Student\", \"Student\" }");
 try {
 Object readValue = deserializer.deserializeTypedFromObject(parser, context());
 fail();
 } catch (JsonProcessingException e) {
 assertEquals("Unexpected character ('{' (code 123)): expected a valid value (number, string, array, object or token from stream)", e.getMessage());
 }
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("com.example.Person"), new StdTypeIdResolver(), "type", true, Person.class);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setTypeFactory(mapper.getTypeFactory().decorateWith(new CustomTypeFactory()));
 ObjectNode node = mapper.createObjectNode();
 node.put("type", "com.example.Student");
 node.put("Student", mapper.valueToTree(new Student("Alice", 20)));
 String typeIdString = node.get("type").asText();
 Object readValue = mapper.readValue(node.toString(), deserializer.getType(typeIdString));
 Student student = (Student) readValue;
 assertEquals("Alice", student.name);
 assertEquals(20, student.age);
 }

 private static DeserializationContext context() {
 return new TestContext();
 }

}

class CustomTypeFactory extends TypeFactory {
 @Override
 public Type constructType(Class<?> baseType) {
 return super.constructType(baseType);
 }

 @Override
 public Type constructType(Class<?> baseType, Annotation[] annotations) {
 return super.constructType(baseType, annotations);
 }

 @Override
 public Type constructType(Class<?> baseType, Annotation[] annotations, Annotation[] genericTypes) {
 return super.constructType(baseType, annotations, genericTypes);
 }

 @Override
 public Type constructType(String className) throws ClassNotFoundException {
 try {
 return super.constructType(Class.forName(className));
 } catch (ClassNotFoundException e) {
 throw new RuntimeException("Could not initialize class: " + className, e);
 }
 }

 @Override
 public Type constructType(String className, Annotation[] annotations) throws ClassNotFoundException {
 try {
 return super.constructType(Class.forName(className), annotations);
 } catch (ClassNotFoundException e) {
 throw new RuntimeException("Could not initialize class: " + className, e);
 }
 }

 @Override
 public Type constructType(String className, Annotation[] annotations, Annotation[] genericTypes)
 throws ClassNotFoundException {
 try {
 return super.constructType(Class.forName(className), annotations, genericTypes);
 } catch (ClassNotFoundException e) {
 throw new RuntimeException("Could not initialize class: " + className, e);
 }
 }
}