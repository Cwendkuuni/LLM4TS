import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private SerializationConfig config;
 private BeanDescription beanDesc;
 private PropertyBuilder propertyBuilder;

 @Before
 public void setup() {
 // Initialize SerializationConfig and BeanDescription for testing
 ObjectMapper objectMapper = new ObjectMapper();
 config = objectMapper.getSerializationConfig();
 beanDesc = new BeanDescription(MyClass.class);
 }

 @Test
 public void testBuilderConstruction() {
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 assertNotNull(propertyBuilder);
 }

 @Test
 public void testGetClassAnnotations() {
 MyAnnotation annotation = new MyAnnotation();
 beanDesc.findProperties().forEach(p -> p.setAnnotation(annotation));
 Annotations actualAnnotations = propertyBuilder.getClassAnnotations();
 Annotations expectedAnnotations = beanDesc.getClassAnnotations();
 assertEquals(expectedAnnotations, actualAnnotations);
 }

 @Test
 public void testBuildWriterDefaultUsage() throws JsonMappingException {
 MyClass myClass = new MyClass();
 MyClassSerializerMyClass ser = new MyClassSerializerMyClass();
 JavaType javaType = ObjectMapper.TypeFactory.defaultInstance().constructType(MyClass.class);
 BeanPropertyWriter expectedWriter = new BeanPropertyWriter(null, null, null, ser, null, null, javaType, false, null);
 BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(new SerializerProvider(), null, javaType, ser, null, null, null, false);
 assertEquals(expectedWriter, actualWriter);
 }

 @Test
 public void testBuildWriterCustomSerializationType() throws JsonMappingException {
 MyClassMyCustomSerializerMyClass ser = new MyClassMyCustomSerializerMyClass();
 JavaType javaType = ObjectMapper.TypeFactory.defaultInstance().constructType(MyClass.class);
 TypeSerializer typeSer = new TypeSerializer();
 BeanPropertyWriter expectedWriter = new BeanPropertyWriter(null, null, null, ser, null, typeSer, javaType, false, null);
 BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(new SerializerProvider(), null, javaType, ser, null, null, null, false, typeSer);
 assertEquals(expectedWriter, actualWriter);
 }

 @Test
 public void testBuildWriterContentTypeSerializer() throws JsonMappingException {
 MyClassMyCustomSerializerMyClass ser = new MyClassMyCustomSerializerMyClass();
 TypeSerializer contentTypeSer = new TypeSerializer();
 JavaType javaType = ObjectMapper.TypeFactory.defaultInstance().constructType(MyClass.class);
 BeanPropertyWriter expectedWriter = new BeanPropertyWriter(null, null, null, ser, null, contentTypeSer, javaType, false, null);
 BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(new SerializerProvider(), null, javaType, ser, null, contentTypeSer, null, false);
 assertEquals(expectedWriter, actualWriter);
 }

 @Test
 public void testBuildWriterUnwrappingNameTransformer() throws JsonMappingException {
 MyClassMyCustomSerializerMyClass ser = new MyClassMyCustomSerializerMyClass();
 NameTransformer unwrapper = new NameTransformer() {
 @Override
 public String transformKey(String key) {
 return "new_key";
 }
 };
 BeanPropertyWriter expectedWriter = new BeanPropertyWriter(null, null, null, ser, null, null, null, false, null)
 .unwrappingWriter(unwrapper);
 BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(new SerializerProvider(), null, null, ser, null, null, null, false, null, unwrapper);
 assertEquals(expectedWriter, actualWriter);
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriterNullSerializer() throws JsonMappingException {
 MyClassMyCustomSerializerMyClass ser = null;
 JavaType javaType = ObjectMapper.TypeFactory.defaultInstance().constructType(MyClass.class);
 BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(new SerializerProvider(), null, javaType, ser, null, null, null, false);
 fail("Expected JsonMappingException but got: " + actualWriter);
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriterNullTypeSerializer() throws JsonMappingException {
 MyClassMyCustomSerializerMyClass ser = new MyClassMyCustomSerializerMyClass();
 TypeSerializer typeSer = null;
 JavaType javaType = ObjectMapper.TypeFactory.defaultInstance().constructType(MyClass.class);
 BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(new SerializerProvider(), null, javaType, ser, null, null, null, false, typeSer);
 fail("Expected JsonMappingException but got: " + actualWriter);
 }

 @Test
 public void testFindSerializationType() {
 MyClassMyCustomSerializerMyClass ser = new MyClassMyCustomSerializerMyClass();
 JavaType javaType = ObjectMapper.TypeFactory.defaultInstance().constructType(MyClass.class);
 try {
 JavaType actualType = propertyBuilder.findSerializationType(new AnnotatedMember(null), false, javaType);
 assertEquals(javaType, actualType);
 } catch (JsonMappingException e) {
 fail("Expected no exception but got: " + e.getMessage());
 }
 try {
 JavaType actualType = propertyBuilder.findSerializationType(new AnnotatedMember(null), false, null);
 fail("Expected JsonMappingException but got null");
 } catch (JsonMappingException e) {
 // Expected
 }
 try {
 MyClassMyCustomSerializerMyClass ser2 = new MyClassMyCustomSerializerMyClass();
 JavaType actualType = propertyBuilder.findSerializationType(new AnnotatedMember(null), false, javaType);
 assertEquals(javaType, actualType);
 } catch (JsonMappingException e) {
 fail("Expected no exception but got: " + e.getMessage());
 }
 try {
 MyClassMyCustomSerializerMyClass ser3 = null;
 JavaType actualType = propertyBuilder.findSerializationType(new AnnotatedMember(null), false, javaType);
 fail("Expected JsonMappingException but got: " + actualType);
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testGetPropertyDefaultValue() throws Exception {
 MyClass myClass = new MyClass();
 MyClass myClassDefault = new MyClass();
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 String name = "testProperty";
 Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, new AnnotatedMember(null), ObjectMapper.TypeFactory.defaultInstance().constructType(MyClass.class));
 assertNull(defaultValue);
 Object actualDefaultValue = myClassDefault.getDefaultValue();
 assertEquals(actualDefaultValue, propertyBuilder.getPropertyDefaultValue(name, new AnnotatedMember(null), ObjectMapper.TypeFactory.defaultInstance().constructType(MyClass.class)));
 }

 @Test
 public void testGetPropertyDefaultValueCustomType() throws Exception {
 MyClassMyCustomDefault myClass = new MyClassMyCustomDefault();
 MyClassMyCustomDefault myClassDefault = new MyClassMyCustomDefault();
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 String name = "testProperty";
 Object actualDefaultValue = myClassDefault.getDefaultValue();
 assertEquals(actualDefaultValue, propertyBuilder.getPropertyDefaultValue(name, new AnnotatedMember(null), ObjectMapper.TypeFactory.defaultInstance().constructType(MyClassMyCustomDefault.class)));
 }

 @Test(expected = JsonMappingException.class)
 public void testGetPropertyDefaultValueNull() throws Exception {
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 String name = "testProperty";
 propertyBuilder.getPropertyDefaultValue(name, new AnnotatedMember(null), null);
 fail("Expected JsonMappingException but got: null");
 }

 @Test
 public void testGetPropertyDefaultValueEmpty() throws Exception {
 MyClassMyEmpty myClass = new MyClassMyEmpty();
 MyClassMyEmpty myClassDefault = new MyClassMyEmpty();
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 String name = "testProperty";
 Object actualDefaultValue = myClassDefault.getDefaultValue();
 assertEquals(actualDefaultValue, propertyBuilder.getPropertyDefaultValue(name, new AnnotatedMember(null), ObjectMapper.TypeFactory.defaultInstance().constructType(MyClassMyEmpty.class)));
 }

 @Test(expected = JsonMappingException.class)
 public void testGetPropertyDefaultValueEmptyNull() throws Exception {
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 String name = "testProperty";
 propertyBuilder.getPropertyDefaultValue(name, new AnnotatedMember(null), null);
 fail("Expected JsonMappingException but got: null");
 }

 @Test
 public void testGetDefaultBean() throws Exception {
 MyClassMyDefault myClass = new MyClassMyDefault();
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 Object actualDefaultBean = propertyBuilder.getDefaultBean();
 assertEquals(myClass, actualDefaultBean);
 }

 @Test
 public void testGetDefaultBeanNull() throws Exception {
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 Object actualDefaultBean = propertyBuilder.getDefaultBean();
 assertNull(actualDefaultBean);
 }
}