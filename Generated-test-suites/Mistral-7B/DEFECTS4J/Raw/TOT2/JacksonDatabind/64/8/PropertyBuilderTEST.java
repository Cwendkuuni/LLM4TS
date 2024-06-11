import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private PropertyBuilder propertyBuilder;

 @Before
 public void setUp() {
 ObjectMapper objectMapper = new ObjectMapper();
 BeanDescription beanDescription = new BeanDescription(new MyPojo());
 SerializationConfig serializationConfig = objectMapper.getSerializationConfig();
 propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
 }

 @Test
 public void testGetClassAnnotations() {
 Class<?> clazz = MyPojo.class;
 Annotations expectedAnnotations = MyPojo.class.getAnnotations();
 Annotations actualAnnotations = propertyBuilder.getClassAnnotations();
 assertEquals(expectedAnnotations, actualAnnotations);
 }

 @Test
 public void testBuildWriter_NullSer() {
 try {
 propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testBuildWriter_NullPropDef() {
 try {
 propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testBuildWriter_NullDeclaredType() {
 try {
 propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testBuildWriter_NullSerializedValue() {
 JsonSerializer<?> jsonSerializer = mock(JsonSerializer.class);
 try {
 propertyBuilder.buildWriter(null, null, null, jsonSerializer, null, null, null, false);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testBuildWriter_NullTypeSerializer() {
 JsonSerializer<?> jsonSerializer = mock(JsonSerializer.class);
 TypeSerializer typeSerializer = mock(TypeSerializer.class);
 try {
 propertyBuilder.buildWriter(null, null, null, jsonSerializer, null, null, null, false);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testBuildWriter_ValidArgs() {
 JsonSerializer<?> jsonSerializer = mock(JsonSerializer.class);
 TypeSerializer typeSerializer = mock(TypeSerializer.class);
 try {
 BeanPropertyWriter actualBeanPropertyWriter = propertyBuilder.buildWriter(null, null, null, jsonSerializer, typeSerializer, null, null, false);
 assertNotNull(actualBeanPropertyWriter);
 } catch (JsonMappingException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testFindSerializationType_NoAnnotations_DeclaredType() {
 JavaType javaType = TypeFactory.type(String[].class, Object.class);
 JavaType expectedJavaType = TypeFactory.type(String[].class, Object.class);
 JavaType actualJavaType = propertyBuilder.findSerializationType(null, false, javaType);
 assertEquals(expectedJavaType, actualJavaType);
 }

 @Test
 public void testFindSerializationType_Annotations_DeclaredType() {
 MyPojoPojoSerializer myPojoPojoSerializer = new MyPojoPojoSerializer();
 JavaType javaType = TypeFactory.type(String.class, MyPojoPojoSerializer.class);
 JavaType expectedJavaType = TypeFactory.type(String.class, MyPojoPojoSerializer.class);
 JavaType actualJavaType = propertyBuilder.findSerializationType(null, false, javaType);
 assertEquals(expectedJavaType, actualJavaType);
 }

 @Test
 public void testFindSerializationType_NoAnnotations_NullDeclaredType() {
 try {
 propertyBuilder.findSerializationType(null, false, null);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testFindSerializationType_Annotations_NullDeclaredType() {
 try {
 propertyBuilder.findSerializationType(null, false, null);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testGetDefaultBean() {
 MyPojo defaultBean = propertyBuilder.getDefaultBean();
 assertNotNull(defaultBean);
 }

 @Test
 public void testGetPropertyDefaultValue_ValidName_ValidMember() {
 String name = "name";
 AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
 JavaType javaType = TypeFactory.type(String.class);
 Object defaultValue = getDefaultValue(javaType);
 when(annotatedMember.getValue(anyObject())).thenReturn(defaultValue);
 Object actualDefaultValue = propertyBuilder.getPropertyDefaultValue(name, annotatedMember, javaType);
 assertEquals(defaultValue, actualDefaultValue);
 }

 @Test
 public void testGetPropertyDefaultValue_ValidName_NullMember() {
 String name = "name";
 Object defaultValue = getDefaultValue(String.class);
 Object actualDefaultValue = propertyBuilder.getPropertyDefaultValue(name, null, String.class);
 assertEquals(defaultValue, actualDefaultValue);
 }

 @Test
 public void testGetPropertyDefaultValue_InvalidName() {
 String name = "invalidName";
 AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
 JavaType javaType = TypeFactory.type(String.class);
 Object defaultValue = getDefaultValue(javaType);
 when(annotatedMember.getValue(anyObject())).thenReturn(defaultValue);
 try {
 propertyBuilder.getPropertyDefaultValue(name, annotatedMember, javaType);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetPropertyDefaultValue_NullName() {
 try {
 propertyBuilder.getPropertyDefaultValue(null, null, String.class);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetPropertyDefaultValue_NullMember() {
 try {
 propertyBuilder.getPropertyDefaultValue("name", null, String.class);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 private Object getDefaultValue(JavaType javaType) {
 Object result;
 if (javaType == null) {
 result = null;
 } else if (javaType.isPrimitiveType()) {
 result = ClassUtil.defaultValue(javaType.getRawClass());
 } else if (javaType.isContainerType() || javaType.isReferenceType()) {
 result = JsonInclude.Include.NON_EMPTY;
 } else if (javaType.getRawClass() == String.class) {
 result = "";
 } else {
 result = null;
 }
 return result;
 }
}