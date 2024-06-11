import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PropertyBuilderTest {

 private ObjectMapper objectMapper;
 private SerializerProvider serializerProvider;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 serializerProvider = objectMapper.getSerializerProvider();
 }

 @Test
 public void testEmptyPropertyBuilder() throws JsonProcessingException {
 PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
 assertNull(propertyBuilder.getClassAnnotations());
 }

 @Test
 public void testPropertyBuilderWithClassAnnotations() throws JsonProcessingException {
 MyClass myClass = new MyClass();
 myClass.setName("Test");
 MyAnnotation myAnnotation = new MyAnnotation();
 MyClassDescription myClassDescription = new MyClassDescription();
 myClassDescription.setClassAnnotations(myAnnotation);
 PropertyBuilder propertyBuilder = new PropertyBuilder(serializerProvider, myClassDescription);
 assertEquals(myAnnotation, propertyBuilder.getClassAnnotations());
 }

 @Test
 public void testBuildWriterWithDefaultValues() throws JsonProcessingException {
 MyClass myClass = new MyClass();
 myClass.setName("Test");
 MyClassDescription myClassDescription = new MyClassDescription();
 myClassDescription.setClassAnnotations(new MyAnnotation());
 JavaType javaType = TypeFactory.defaultInstance().constructType(MyClass.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(serializerProvider, myClassDescription, javaType, null, null, null, null, false);
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, null, javaType, null, null, null, null, false);
 assertEquals("Test", beanPropertyWriter.getName());
 }

 @Test
 public void testBuildWriterWithCustomSerializationType() throws JsonProcessingException {
 MyClass myClass = new MyClass();
 myClass.setName("Test");
 MyClassDescription myClassDescription = new MyClassDescription();
 myClassDescription.setClassAnnotations(new MyAnnotation());
 MyClassCustomType myClassCustomType = new MyClassCustomType();
 JavaType javaType = TypeFactory.defaultInstance().constructType(MyClass.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(serializerProvider, myClassDescription, javaType, null, null, null, null, false);
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, null, javaType, null, myClassCustomType, null, null, false);
 assertEquals("Test", beanPropertyWriter.getName());
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriterWithInvalidClassType() throws JsonProcessingException {
 MyClass myClass = new MyClass();
 MyClassDescription myClassDescription = new MyClassDescription();
 JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(serializerProvider, myClassDescription, javaType, null, null, null, null, false);
 propertyBuilder.buildWriter(serializerProvider, null, javaType, null, null, null, null, false);
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriterWithMissingPropertyDefinition() throws JsonProcessingException {
 MyClass myClass = new MyClass();
 MyClassDescription myClassDescription = new MyClassDescription();
 JavaType javaType = TypeFactory.defaultInstance().constructType(MyClass.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(serializerProvider, myClassDescription, javaType, null, null, null, null, false);
 propertyBuilder.buildWriter(serializerProvider, null, javaType, null, null, null, null, false);
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriterWithMissingAccessors() throws JsonProcessingException {
 MyClass myClass = new MyClass();
 MyClassDescription myClassDescription = new MyClassDescription();
 JavaType javaType = TypeFactory.defaultInstance().constructType(MyClass.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(serializerProvider, myClassDescription, javaType, null, null, null, null, false);
 propertyBuilder.buildWriter(serializerProvider, null, javaType, null, null, null, null, false);
 }

 @Test
 public void testBuildWriterWithNullValues() throws JsonProcessingException {
 MyClass myClass = new MyClass();
 MyClassDescription myClassDescription = new MyClassDescription();
 JavaType javaType = TypeFactory.defaultInstance().constructType(MyClass.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(serializerProvider, myClassDescription, javaType, null, null, null, null, false);
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, null, javaType, null, null, null, null, false);
 assertNull(beanPropertyWriter.getValueToSuppress());
 }

 @Test
 public void testBuildWriterWithEmptyValues() throws JsonProcessingException {
 MyClass myClass = new MyClass();
 MyClassDescription myClassDescription = new MyClassDescription();
 JavaType javaType = TypeFactory.defaultInstance().constructType(MyClass.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(serializerProvider, myClassDescription, javaType, null, null, null, null, false);
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, null, javaType, null, null, null, null, false);
 assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, beanPropertyWriter.getValueToSuppress());
 }

 @Test
 public void testBuildWriterWithStringValues() throws JsonProcessingException {
 MyClass myClass = new MyClass();
 myClass.setName("Test");
 MyClassDescription myClassDescription = new MyClassDescription();
 MyAnnotation myAnnotation = new MyAnnotation();
 myClassDescription.setClassAnnotations(myAnnotation);
 JavaType javaType = TypeFactory.defaultInstance().constructType(MyClass.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(serializerProvider, myClassDescription, javaType, null, null, null, null, false);
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, null, javaType, null, null, null, null, false);
 assertEquals("Test", beanPropertyWriter.getValueToSuppress());
 }

 @Test
 public void testBuildWriterWithCustomValueToSuppress() throws JsonProcessingException {
 MyClass myClass = new MyClass();
 myClass.setName("Test");
 MyClassDescription myClassDescription = new MyClassDescription();
 MyAnnotation myAnnotation = new MyAnnotation();
 myClassDescription.setClassAnnotations(myAnnotation);
 JavaType javaType = TypeFactory.defaultInstance().constructType(MyClass.class);
 BeanPropertyWriter beanPropertyWriter = new BeanPropertyWriter(new BeanPropertyDefinition(null, null, MyClass.class, null), null, null, null, null, null, null, null, null);
 PropertyBuilder propertyBuilder = new PropertyBuilder(serializerProvider, myClassDescription, javaType, null, null, null, null, false);
 Object valueToSuppress = new Object();
 beanPropertyWriter.setValueToSuppress(valueToSuppress);
 BeanPropertyWriter actualBeanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, null, javaType, null, null, null, null, false);
 assertEquals(valueToSuppress, actualBeanPropertyWriter.getValueToSuppress());
 }
}

class MyAnnotation {}
class MyClassDescription {}
class MyClass {
 private String name;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }
}

class MyClassCustomType {}