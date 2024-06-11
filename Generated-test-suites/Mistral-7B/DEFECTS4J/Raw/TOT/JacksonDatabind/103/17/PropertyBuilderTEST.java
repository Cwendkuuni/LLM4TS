package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.databind.node.*;
import org.testng.Assert;
import org.testng.annotations.*;

public class PropertyBuilderTest {

 @BeforeClass
 public static void setupClass() {
 ObjectMapper mapper = new ObjectMapper();
 mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
 }

 /**
 * Test for basic functionality of PropertyBuilder when property has no default value.
 */
 @Test
 public void testBasicNoDefault() throws Exception {
 BeanDescription beanDesc = mapper.getTypeFactory().constructBeanDescription(Person.class);
 PropertyBuilder builder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);
 BeanPropertyWriter writer = builder.buildWriter(mapper.getSerializationConfig().getDefaultSerializerProvider(),
 null, Person.class.getDeclaredField("name"), null, null, null, null, false);

 Assert.assertNull(writer.getValue(null));
 }

 /**
 * Test for basic functionality of PropertyBuilder when property has default value, but it is null.
 */
 @Test
 public void testBasicDefaultNull() throws Exception {
 BeanDescription beanDesc = mapper.getTypeFactory().constructBeanDescription(Person.class);
 PropertyBuilder builder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);
 BeanPropertyWriter writer = builder.buildWriter(mapper.getSerializationConfig().getDefaultSerializerProvider(),
 null, Person.class.getDeclaredField("age"), null, null, null, null, false);

 Person person = new Person();
 Assert.assertEquals(writer.getValue(person), Integer.valueOf(0));
 }

 /**
 * Test for basic functionality of PropertyBuilder when property has default value that is not null.
 */
 @Test
 public void testBasicDefaultValue() throws Exception {
 BeanDescription beanDesc = mapper.getTypeFactory().constructBeanDescription(Person.class);
 PropertyBuilder builder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);
 BeanPropertyWriter writer = builder.buildWriter(mapper.getSerializationConfig().getDefaultSerializerProvider(),
 null, Person.class.getDeclaredField("gender"), null, null, null, null, false);

 Person person = new Person();
 Assert.assertEquals(writer.getValue(person), "MALE");
 }

 /**
 * Test for functionality of PropertyBuilder when property has annotation that forces type to use declared type.
 */
 @Test
 public void testCustomType() throws Exception {
 BeanDescription beanDesc = mapper.getTypeFactory().constructBeanDescription(Person.class);
 PropertyBuilder builder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);

 JsonInclude.Value inclPerType = JsonInclude.Value.merge(
 beanDesc.findPropertyInclusion(JsonInclude.Value.empty()),
 mapper.getDefaultPropertyInclusion(Person.class, JsonInclude.Value.empty()));

 AnnotatedMember am = new AnnotatedClassMemberBuilder()
 .setAnnotated(beanDesc.findPropertyDefinition("age"))
 .setName("age")
 .setDeclaredAnnotations(new JsonSerialize(using = CustomSerializer.class))
 .build();

 JavaType serializationType = builder.findSerializationType(am, true, null);
 BeanPropertyWriter writer = builder.buildWriter(mapper.getSerializationConfig().getDefaultSerializerProvider(),
 null, null, null, null, null, am, false);

 Person person = new Person();
 Assert.assertEquals(writer.getValue(person), 1);
 }

 /**
 * Test for functionality of PropertyBuilder when property has per-property default exclusion.
 */
 @Test
 public void testPropertyDefaultExclusion() throws Exception {
 BeanDescription beanDesc = mapper.getTypeFactory().constructBeanDescription(Person.class);
 PropertyBuilder builder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);
 BeanPropertyWriter writer = builder.buildWriter(mapper.getSerializationConfig().getDefaultSerializerProvider(),
 null, Person.class.getDeclaredField("age"), null, null, null, null, false);

 Person person = new Person();
 person.age = 1;
 ObjectWriter ow = mapper.writerWithView(CustomSerializer.class);
 String json = ow.writeValueAsString(person);
 JsonNode node = mapper.readTree(json);

 Assert.assertNull(writer.getValue(null), "Default should be excluded");
 Assert.assertNotNull(node.get("age"), "Value should be serialized");
 }

 /**
 * Test for functionality of PropertyBuilder when property has per-type default inclusion.
 */
 @Test
 public void testTypeDefaultInclusion() throws Exception {
 BeanDescription beanDesc = mapper.getTypeFactory().constructBeanDescription(Person.class);
 PropertyBuilder builder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);

 JsonInclude.Value inclPerType = JsonInclude.Value.merge(
 beanDesc.findPropertyInclusion(JsonInclude.Value.empty()),
 mapper.getDefaultPropertyInclusion(Person.class, JsonInclude.Value.empty()));

 JsonInclude.Value incl = JsonInclude.Value.merge(
 mapper.getDefaultPropertyInclusion(), inclPerType);

 builder.setDefaultInclusion(incl);
 BeanPropertyWriter writer = builder.buildWriter(mapper.getSerializationConfig().getDefaultSerializerProvider(),
 null, Person.class.getDeclaredField("name"), null, null, null, null, false);

 Person person = new Person();
 ObjectWriter ow = mapper.writerWithView(CustomSerializer.class);
 String json = ow.writeValueAsString(person);
 JsonNode node = mapper.readTree(json);

 Assert.assertEquals(writer.getValue(null), "John Doe", "Default value should be included");
 Assert.assertEquals(node.get("name").asText(), "John Doe", "Value should be deserialized correctly");
 }

 /**
 * Test for functionality of PropertyBuilder when property has property specific default exclusion.
 */
 @Test
 public void testPropertyDefaultExclusionAnnotation() throws Exception {
 BeanDescription beanDesc = mapper.getTypeFactory().constructBeanDescription(Person.class);
 PropertyBuilder builder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);
 BeanPropertyWriter writer = builder.buildWriter(mapper.getSerializationConfig().getDefaultSerializerProvider(),
 null, Person.class.getDeclaredField("age"), null, null, null, null, false);

 Person person = new Person();
 person.age = 1;
 ObjectWriter ow = mapper.writerWithView(CustomSerializer.class);
 String json = ow.writeValueAsString(person);
 JsonNode node = mapper.readTree(json);

 BeanDescription personDesc = mapper.getTypeFactory().constructBeanDescription(Person.class);
 PropertyBuilder builder2 = new PropertyBuilder(mapper.getSerializationConfig(), personDesc);
 BeanPropertyWriter writer2 = builder2.buildWriter(mapper.getSerializationConfig().getDefaultSerializerProvider(),
 null, personDesc.findPropertyDefinition("age"), null, null, null, null, false);

 Assert.assertNull(writer.getValue(null), "Default should be excluded");
 Assert.assertNotNull(node.get("age"), "Value should be serialized");
 Assert.assertNull(writer2.getValue(null), "Property-specific default should be excluded");
 }

 /**
 * Test for functionality of PropertyBuilder when property has custom inclusion filter.
 */
 @Test
 public void testCustomInclusionFilter() throws Exception {
 BeanDescription beanDesc = mapper.getTypeFactory().constructBeanDescription(Person.class);
 PropertyBuilder builder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);
 BeanPropertyWriter writer = builder.buildWriter(mapper.getSerializationConfig().getDefaultSerializerProvider(),
 null, Person.class.getDeclaredField("age"), null, null, null, null, false);

 Person person = new Person();
 person.age = 1;
 ObjectWriter ow = mapper.writerWithView(CustomSerializer.class);
 String json = ow.writeValueAsString(person);
 JsonNode node = mapper.readTree(json);

 ObjectWriter ow2 = mapper.writerWithView(CustomInclusionFilter.class);
 JsonNode node2 = ow2.writeValueAsString(person);
 JsonNode node3 = mapper.readTree(node2.getText());

 BeanPropertyWriter writer2 = builder.buildWriter(mapper.getSerializationConfig().getDefaultSerializerProvider(),
 null, Person.class.getDeclaredField("age"), null, null, null, null, false);

 Assert.assertNull(writer.getValue(null), "Default should be excluded");
 Assert.assertNotNull(node.get("age"), "Value should be serialized");
 Assert.assertNull(writer2.getValue(null), "Custom inclusion filter should exclude default value");
 Assert.assertNotNull(node3.get("age"), "Custom inclusion filter should include value");
 }
}

class Person {
 @JsonSerialize(using = CustomSerializer.class)
 @JsonInclude(JsonInclude.Include.NON_DEFAULT)
 private String name;
 @JsonInclude(JsonInclude.Include.NON_EMPTY)
 private int age;
 private String gender;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public int getAge() {
 return age;
 }

 public void setAge(int age) {
 this.age = age;
 }

 public String getGender() {
 return gender;
 }

 public void setGender(String gender) {
 this.gender = gender;
 }
}

class CustomSerializer extends StdSerializer<Object> {
 protected CustomSerializer() {
 this(null);
 }

 public CustomSerializer(Class<Object> t) {
 super(t);
 }

 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return null;
 }

 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 if (value == null) {
 gen.writeNull();
 } else {
 gen.writeValue(value);
 }
 }
}

class CustomInclusionFilter extends JsonFilter {
 @Override
 public void serializeValue(SerializerProvider p, JsonGenerator g, Object value, TypeSerializer s) throws IOException {
 if (value instanceof Number && ((Number) value).intValue() > 0) {
 super.serializeValue(p, g, value, s);
 } else {
 g.writeNull();
 }
 }
}