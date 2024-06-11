import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private SerializationConfig config;
 private BeanDescription beanDesc;
 private PropertyBuilder propertyBuilder;

 @Before
 public void setup() throws JsonMappingException {
 Class<?> sampleClass = MySampleClass.class;
 config = new SerializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 BeanFactory beanFactory = mapper.getTypeFactory();
 beanDesc = mapper.introspect(sampleClass);
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 }

 @Test
 public void testGetClassAnnotations() throws JsonMappingException {
 Annotations annotations = propertyBuilder.getClassAnnotations();
 assertNotNull(annotations);
 // add test cases for specific annotations present
 }

 @Test
 public void testBuildWriter() throws JsonMappingException {
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("sampleProperty");
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, propDef, propDef.getBeanType(), null, null, null, null, false);
 assertNotNull(writer);
 // add test cases for specific behavior of BeanPropertyWriter
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriterInvalidType() throws JsonMappingException {
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("sampleProperty");
 JavaType invalidType = TypeFactory.unknownType();
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, propDef, invalidType, null, null, null, null, false);
 fail("Should have thrown JsonMappingException");
 }

 @Test
 public void testFindSerializationType() throws JsonMappingException {
 AnnotatedMember member = beanDesc.findProperty("sampleProperty").getMember();
 JavaType declaredType = member.getType();
 JavaType serializationType = propertyBuilder.findSerializationType(member, false, declaredType);
 assertEquals(declaredType, serializationType);
 // add test cases for annotated type overrides
 }

 @Test
 public void testGetDefaultBean() {
 Object defaultBean = propertyBuilder.getDefaultBean();
 assertNotNull(defaultBean);
 // add test cases for default bean instantiation
 }

 @Test
 public void testGetPropertyDefaultValue() throws JsonMappingException {
 AnnotatedMember member = beanDesc.findProperty("sampleProperty").getMember();
 JavaType propertyType = member.getType();
 Object defaultValue = propertyBuilder.getPropertyDefaultValue("sampleProperty", member, propertyType);
 assertNull(defaultValue); // default value is not present for this test case
 // add test cases for default value presence and type
 }

 @Test
 public void testGetDefaultValue() {
 JavaType intType = TypeFactory.intType();
 Object defaultValue = propertyBuilder.getDefaultValue(intType);
 assertEquals(0, defaultValue);
 // add test cases for different types
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetPropertyDefaultValueNull() throws JsonMappingException {
 String propertyName = "sampleProperty";
 AnnotatedMember member = null;
 JavaType type = null;
 Object defaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, member, type);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test(expected = JsonMappingException.class)
 public void testGetPropertyDefaultValueInvalidType() throws JsonMappingException {
 String propertyName = "sampleProperty";
 AnnotatedMember member = beanDesc.findProperty("sampleProperty").getMember();
 JavaType invalidType = TypeFactory.unknownType();
 Object defaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, member, invalidType);
 fail("Should have thrown JsonMappingException");
 }
}