import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private PropertyBuilder propertyBuilder;
 private Object defaultBean;

 @Before
 public void setup() throws JsonProcessingException {
 Class<MyPojo> pojoClass = MyPojo.class;
 ObjectMapper mapper = new ObjectMapper();
 BeanDescription beanDesc = mapper.getTypeFactory().constructBeanDescription(pojoClass);
 SerializationConfig config = mapper.getSerializationConfig();
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 defaultBean = propertyBuilder.getDefaultBean();
 }

 @Test
 public void testGetClassAnnotations() {
 Annotations annotations = propertyBuilder.getClassAnnotations();
 assertNotNull(annotations);
 }

 @Test
 public void testBuildWriter() throws JsonMappingException {
 JavaType propertyType = mapper.getTypeFactory().constructType(String.class);
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("propertyName");
 BeanPropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(), propDef, propertyType, null, null, null, null, false);
 assertNotNull(writer);
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriterWithInvalidSerializationType() throws JsonMappingException {
 JavaType propertyType = mapper.getTypeFactory().constructType(String[].class);
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("propertyName");
 try {
 propertyBuilder.buildWriter(mapper.getSerializerProvider(), propDef, propertyType, null, null, null, null, false);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testFindSerializationType() {
 AnnotatedMember member = mock(AnnotatedMember.class);
 when(member.getRawType()).thenReturn(String[].class);
 when(member.isAnnotationPresent(JsonSerialize.class)).thenReturn(true);
 when(member.getAnnotation(JsonSerialize.class)).thenReturn(new JsonSerialize());
 when(member.getValue(defaultBean)).thenReturn("defaultValue");

 JavaType expectedType = mapper.getTypeFactory().constructType(String[].class);
 JavaType actualType = propertyBuilder.findSerializationType(member, false, expectedType);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetDefaultBean() {
 assertNull(propertyBuilder.getDefaultBean());
 }

 @Test
 public void testGetPropertyDefaultValue() {
 String propertyName = "propertyName";
 AnnotatedMember member = mock(AnnotatedMember.class);
 when(member.getName()).thenReturn(propertyName);
 when(member.getValue(defaultBean)).thenReturn("defaultValue");

 Object defaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, member, mapper.getTypeFactory().constructType(Integer.class));
 assertEquals("defaultValue", defaultValue);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetPropertyDefaultValueWithNullDefaultBean() {
 String propertyName = "propertyName";
 AnnotatedMember member = mock(AnnotatedMember.class);
 when(member.getName()).thenReturn(propertyName);
 when(member.getValue(defaultBean)).thenThrow(new IllegalStateException("defaultBean is null"));

 try {
 propertyBuilder.getPropertyDefaultValue(propertyName, member, mapper.getTypeFactory().constructType(Integer.class));
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testGetPropertyDefaultValueWithEmptyArrayType() {
 String propertyName = "propertyName";
 AnnotatedMember member = mock(AnnotatedMember.class);
 when(member.getName()).thenReturn(propertyName);
 when(member.getValue(defaultBean)).thenReturn(new Object[0]);

 Object defaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, member, mapper.getTypeFactory().constructType(Object[].class));
 assertArrayEquals(new Object[0], defaultValue);
 }

 @Test
 public void testGetPropertyDefaultValueWithNonEmptyArrayType() {
 String propertyName = "propertyName";
 AnnotatedMember member = mock(AnnotatedMember.class);
 when(member.getName()).thenReturn(propertyName);
 when(member.getValue(defaultBean)).thenReturn(new Object[]{ "value1", "value2" });

Object defaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, member, mapper.getTypeFactory().constructType(Object[].class));
 assertArrayEquals(new Object[]{ "value1", "value2" }, defaultValue);

}

 @Test
 public void testGetPropertyDefaultValueWithListType() {
 String propertyName = "propertyName";
 AnnotatedMember member = mock(AnnotatedMember.class);
 when(member.getName()).thenReturn(propertyName);
 when(member.getValue(defaultBean)).thenReturn(Arrays.asList("value1", "value2"));

 Object defaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, member, mapper.getTypeFactory().constructType(List.class.getTypeName().replaceAll("\\[", ".").replaceAll("]", "").replaceAll("java.util.", "").replaceAll("List", "ArrayList")));
 assertEquals(Arrays.asList("value1", "value2"), defaultValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetPropertyDefaultValueWithInvalidType() {
 String propertyName = "propertyName";
 AnnotatedMember member = mock(AnnotatedMember.class);
 when(member.getName()).thenReturn(propertyName);
 when(member.getValue(defaultBean)).thenThrow(new IllegalStateException("getValue() failed"));

 try {
 propertyBuilder.getPropertyDefaultValue(propertyName, member, mapper.getTypeFactory().constructType(Integer.class));
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetPropertyDefaultValueWithNullValue() {
 String propertyName = "propertyName";
 AnnotatedMember member = mock(AnnotatedMember.class);
 when(member.getName()).thenReturn(propertyName);
 when(member.getValue(defaultBean)).thenReturn(null);

 try {
 propertyBuilder.getPropertyDefaultValue(propertyName, member, mapper.getTypeFactory().constructType(String.class));
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testThrowWrappedException() {
 Throwable thrownException = new Exception("Thrown exception");
 Throwable expectedException = new IllegalArgumentException("Failed to get property 'propertyName' of default MyPojo instance");

 try {
 propertyBuilder._throwWrapped(thrownException, "propertyName", defaultBean);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 assertEquals(expectedException.getMessage(), e.getMessage());
 }
 }
}