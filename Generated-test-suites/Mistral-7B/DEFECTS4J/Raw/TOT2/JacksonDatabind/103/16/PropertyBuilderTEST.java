import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private PropertyBuilder propertyBuilder;
 private Object defaultBean;

 @Before
 public void setUp() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 BeanDescription beanDesc = mapper.getTypeFactory().constructBeanDescription(PropertyBuilder.class);
 SerializationConfig config = mapper.getSerializationConfig();
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 defaultBean = propertyBuilder.getDefaultBean();
 }

 @Test
 public void testDefaultBeanIsNotNull() {
 assertNotNull(defaultBean);
 }

 @Test
 public void testGetClassAnnotations() throws Exception {
 Annotations actual = propertyBuilder.getClassAnnotations();
 assertNotNull(actual);
 Annotations expected = mapper.getAnnotationIntrospector().findClassAnnotations(PropertyBuilder.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testBuildWriter_Empty() throws Exception {
 SerializerProvider prov = mapper.getSerializerProvider();
 BeanPropertyDefinition propDef = null;
 JavaType declaredType = null;
 JsonSerializer<?> ser = null;
 TypeSerializer typeSer = null;
 TypeSerializer contentTypeSer = null;
 AnnotatedMember am = null;
 boolean defaultUseStaticTyping = false;
 BeanPropertyWriter result = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
 assertNull(result);
 }

 @Test
 public void testBuildWriter_NullSer() throws Exception {
 SerializerProvider prov = mapper.getSerializerProvider();
 BeanPropertyDefinition propDef = null;
 JavaType declaredType = null;
 JsonSerializer<?> ser = null;
 TypeSerializer typeSer = null;
 TypeSerializer contentTypeSer = null;
 AnnotatedMember am = null;
 boolean defaultUseStaticTyping = false;
 BeanPropertyWriter result = propertyBuilder.buildWriter(prov, propDef, declaredType, null, typeSer, contentTypeSer, am, defaultUseStaticTyping);
 assertNull(result);
 }

 @Test
 public void testBuildWriter_NullPropDef() throws Exception {
 SerializerProvider prov = mapper.getSerializerProvider();
 BeanPropertyDefinition propDef = null;
 JavaType declaredType = null;
 JsonSerializer<?> ser = null;
 TypeSerializer typeSer = null;
 TypeSerializer contentTypeSer = null;
 AnnotatedMember am = null;
 boolean defaultUseStaticTyping = false;
 BeanPropertyWriter result = propertyBuilder.buildWriter(prov, null, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
 assertNull(result);
 }

 @Test
 public void testBuildWriter_Valid() throws Exception {
 SerializerProvider prov = mapper.getSerializerProvider();
 BeanPropertyDefinition propDef = mapper.findBeanPropertyDefinition("propertyBuilder", PropertyBuilder.class, "name");
 JavaType declaredType = mapper.getTypeFactory().constructType(String.class);
 JsonSerializer<String> ser = mapper.findValueSerializer(String.class);
 TypeSerializer typeSer = mapper.getTypeSerializer();
 TypeSerializer contentTypeSer = mapper.getTypeSerializer();
 AnnotatedMember am = BeanDescription.findProperty(PropertyBuilder.class, "name");
 boolean defaultUseStaticTyping = false;
 BeanPropertyWriter result = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
 assertNotNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriter_InvalidType() throws Exception {
 SerializerProvider prov = mapper.getSerializerProvider();
 BeanPropertyDefinition propDef = mapper.findBeanPropertyDefinition("propertyBuilder", PropertyBuilder.class, "name");
 JavaType declaredType = mapper.getTypeFactory().constructType(String[].class);
 JsonSerializer<String> ser = mapper.findValueSerializer(String.class);
 TypeSerializer typeSer = mapper.getTypeSerializer();
 TypeSerializer contentTypeSer = mapper.getTypeSerializer();
 AnnotatedMember am = BeanDescription.findProperty(PropertyBuilder.class, "name");
 boolean defaultUseStaticTyping = false;
 BeanPropertyWriter result = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
 fail("Should have thrown JsonMappingException");
 }

 @Test
 public void testFindSerializationType_Empty() throws Exception {
 Annotated a = null;
 boolean useStaticTyping = false;
 JavaType declaredType = null;
 JavaType result = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
 assertNull(result);
 }

 @Test
 public void testFindSerializationType_Valid() throws Exception {
 Annotated a = mapper.findPropertyAnnotation("propertyBuilder", PropertyBuilder.class, "name");
 JavaType declaredType = mapper.getTypeFactory().constructType(String.class);
 JavaType result = propertyBuilder.findSerializationType(a, false, declaredType);
 assertEquals(declaredType, result);
 }

 @Test
 public void testGetPropertyDefaultValue_NullBean() throws Exception {
 String propName = "name";
 AnnotatedMember member = null;
 JavaType type = mapper.getTypeFactory().constructType(String.class);
 Object result = propertyBuilder.getPropertyDefaultValue(propName, member, type);
 assertNull(result);
 }

 @Test
 public void testGetPropertyDefaultValue_Valid() throws Exception {
 String propName = "name";
 AnnotatedMember member = BeanDescription.findProperty(PropertyBuilder.class, propName);
 JavaType type = mapper.getTypeFactory().constructType(String.class);
 Object result = propertyBuilder.getPropertyDefaultValue(propName, member, type);
 assertEquals("", result);
 }

 @Test
 public void testGetPropertyDefaultValue_NoDefaultBean() throws Exception {
 String propName = "name";
 AnnotatedMember member = BeanDescription.findProperty(PropertyBuilder.class, propName);
 JavaType type = mapper.getTypeFactory().constructType(String.class);
 Object result = propertyBuilder.getPropertyDefaultValue(propName, member, type);
 assertNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetPropertyDefaultValue_InvalidPropertyName() throws Exception {
 String propName = "nonexistentProperty";
 AnnotatedMember member = BeanDescription.findProperty(PropertyBuilder.class, propName);
 JavaType type = mapper.getTypeFactory().constructType(String.class);
 Object result = propertyBuilder.getPropertyDefaultValue(propName, member, type);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test(expected = JsonMappingException.class)
 public void testGetPropertyDefaultValue_InvalidMember() throws Exception {
 String propName = "name";
 AnnotatedMember member = null;
 JavaType type = mapper.getTypeFactory().constructType(String.class);
 Object result = propertyBuilder.getPropertyDefaultValue(propName, member, type);
 fail("Should have thrown JsonMappingException");
 }

 @Test
 public void testGetPropertyDefaultValue_NoDefaultValue() throws Exception {
 String propName = "version";
 AnnotatedMember member = BeanDescription.findProperty(PropertyBuilder.class, propName);
 JavaType type = mapper.getTypeFactory().constructType(String.class);
 Object result = propertyBuilder.getPropertyDefaultValue(propName, member, type);
 assertNull(result);
 }
}