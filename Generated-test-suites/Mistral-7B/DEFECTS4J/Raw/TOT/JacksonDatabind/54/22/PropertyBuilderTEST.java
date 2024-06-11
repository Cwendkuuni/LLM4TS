import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private SerializationConfig config;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 config = new SerializationConfig();
 mapper = new ObjectMapper(config);
 }

 @Test
 public void testBuilderWithDefaultInclusion() throws JsonProcessingException {
 BeanDescription beanDesc = new BeanDescription(MyPojo.class);
 PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

 Annotations classAnnotations = builder.getClassAnnotations();
 assertNotNull(classAnnotations);

 BeanPropertyWriter writer = builder.buildWriter(new TestSerializerProvider(), null, null, null, null, null, null, false);
 assertNotNull(writer);
 }

 @Test
 public void testBuilderWithCustomInclusion() throws JsonProcessingException {
 MyPojo pojo = new MyPojo();
 pojo.setName("test");
 pojo.setAge(25);
 BeanDescription beanDesc = new BeanDescription(MyPojo.class);
 PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

 Annotations classAnnotations = builder.getClassAnnotations();
 assertNotNull(classAnnotations);

 JsonInclude.Value defaultInclusion = builder.getDefaultInclusion();
 assertEquals(JsonInclude.Include.NON_DEFAULT, defaultInclusion.getValueInclusion());

 BeanPropertyWriter writer = builder.buildWriter(new TestSerializerProvider(), null, null, null, null, null, null, false);
 ObjectNode jsonNode = mapper.writeValueAsTree(writer);
 Object Node = jsonNode.get("name");
 assertNotNull(Node);
 assertEquals("test", Node.asText());

 Node = jsonNode.get("age");
 assertNotNull(Node);
 assertEquals(25, Node.asInt());
 }

 @Test(expected = JsonMappingException.class)
 public void testBuilderWithInvalidSerializationType() throws JsonProcessingException {
 MyPojo pojo = new MyPojo();
 pojo.setName("test");
 BeanDescription beanDesc = new BeanDescription(MyPojo.class);
 PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

 BeanPropertyWriter writer = builder.buildWriter(new TestSerializerProvider(), null, null, null, null, null, null, false);
 writer.assignNullSerializer(null);
 writer.build();
 fail("No JsonMappingException thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuilderWithInvalidPropertyName() throws JsonProcessingException {
 MyPojo pojo = new MyPojo();
 pojo.setName("test");
 BeanDescription beanDesc = new BeanDescription(MyPojo.class);
 PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

 try {
 builder.buildWriter(new TestSerializerProvider(), null, null, null, null, null, null, false);
 fail("IllegalArgumentException not thrown");
 } catch (JsonMappingException e) {
 // expected
 }

 try {
 builder.getPropertyDefaultValue("invalid", null, null);
 fail("IllegalArgumentException not thrown");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testBuilderWithNullDefaultBean() throws JsonProcessingException {
 MyPojo pojo = new MyPojo();
 pojo.setName("test");
 BeanDescription beanDesc = new BeanDescription(MyPojo.class);
 PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

 Object defaultBean = builder.getDefaultBean();
 assertNull(defaultBean);
 }

 @Test
 public void testBuilderWithDefaultBeanValue() throws JsonProcessingException {
 MyPojo pojo = new MyPojo();
 BeanDescription beanDesc = new BeanDescription(MyPojo.class);
 PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
 Object defaultBean = builder.getDefaultBean();
 MyPojo defaultValuePojo = (MyPojo) defaultBean;
 assertNotNull(defaultValuePojo);
 assertNull(defaultValuePojo.getName());
 assertNull(defaultValuePojo.getAge());
 }

 @Test
 public void testBuilderWithEmptyBeanValue() throws JsonProcessingException {
 MyPojo pojo = new MyPojo();
 BeanDescription beanDesc = new BeanDescription(MyPojo.class);
 PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
 Object emptyBean = builder.getDefaultBean();
 assertNull(emptyBean);
 }

 @Test
 public void testBuilderWithDefaultValueForPrimitiveType() throws JsonProcessingException {
 MyPojo pojo = new MyPojo();
 BeanDescription beanDesc = new BeanDescription(MyPojo.class);
 PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
 Object intDefaultValue = builder.getDefaultValue(IntType.instance());
 assertEquals(0, intDefaultValue.intValue());
 }

 @Test
 public void testBuilderWithDefaultValueForWrapperType() throws JsonProcessingException {
 MyPojo pojo = new MyPojo();
 BeanDescription beanDesc = new BeanDescription(MyPojo.class);
 PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
 Object stringDefaultValue = builder.getDefaultValue(StringType.instance());
 assertEquals("", stringDefaultValue.toString());
 }

 @Test
 public void testBuilderWithDefaultValueForCollectionType() throws JsonProcessingException {
 MyPojo pojo = new MyPojo();
 BeanDescription beanDesc = new BeanDescription(MyPojo.class);
 PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
 JavaType javaType = TypeFactory.collectionType(ArrayList.class, IntType.instance());
 Object collectionDefaultValue = builder.getDefaultValue(javaType);
 assertEquals(JsonInclude.Include.NON_EMPTY, collectionDefaultValue);
 }

 @Test
 public void testBuilderWithDefaultValueForMapType() throws JsonProcessingException {
 MyPojo pojo = new MyPojo();
 BeanDescription beanDesc = new BeanDescription(MyPojo.class);
 PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
 JavaType javaType = TypeFactory.mapType(HashMap.class, StringType.instance(), IntType.instance());
 Object mapDefaultValue = builder.getDefaultValue(javaType);
 assertEquals(JsonInclude.Include.NON_EMPTY, mapDefaultValue);
 }

 @Test
 public void testBuilderWithDefaultValueForReferenceType() throws JsonProcessingException {
 MyPojo pojo = new MyPojo();
 BeanDescription beanDesc = new BeanDescription(MyPojo.class);
 PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
 JavaType javaType = TypeFactory.valueType(MyAnotherPojo.class);
 Object referenceDefaultValue = builder.getDefaultValue(javaType);
 assertEquals(JsonInclude.Include.NON_DEFAULT, referenceDefaultValue);
 }
}

class MyPojo {
 @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
 private String name;
 @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
 private int age;

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
}

class MyAnotherPojo {
 // A simple POJO class for testing
}

class IntType extends JavaType {
 static final IntType instance = new IntType();

 IntType() {
 super(int.class);
 }
}

class StringType extends JavaType {
 static final StringType instance = new StringType();

 StringType() {
 super(String.class);
 }
}

class TestSerializerProvider extends StdSerializerProvider {}