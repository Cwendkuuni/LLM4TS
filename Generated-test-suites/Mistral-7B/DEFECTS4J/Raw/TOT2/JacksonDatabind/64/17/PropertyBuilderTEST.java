import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private PropertyBuilder propertyBuilder;
 private Object defaultBean;

 @Before
 public void setUp() throws JsonProcessingException {
 JsonFactory jsonFactory = new JsonFactory();
 ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
 BeanDescription beanDesc = objectMapper.findBeanDescription(SimplePOJO.class, null);
 propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), beanDesc);
 defaultBean = propertyBuilder.getDefaultBean();
 }

 @Test
 public void testBuildWriter_BasicUsage() throws JsonMappingException {
 SerializerProvider prov = new StandardSerializerProvider();
 BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition("name");
 java.lang.reflect.Type type = propDef.getRawType();
 JsonSerializer<Object> ser = new stdSerializer<Object>(Object.class);
 TypeSerializer typeSer = null;
 JavaType declaredType = null;
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, type, ser, typeSer, null, null, false);
 assertNotNull(bpw);
 }

 @Test
 public void testBuildWriter_SerializationType() throws JsonMappingException {
 SerializerProvider prov = new StandardSerializerProvider();
 BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition("list");
 JavaType declaredType = propertyBuilder.findSerializationType(null, false, propDef.getType());
 assertEquals(java.util.ArrayList.class, declaredType.getRawClass());
 }

 @Test
 public void testBuildWriter_SuppressNulls() throws JsonMappingException {
 SerializerProvider prov = new StandardSerializerProvider();
 BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition("nullableInt");
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, propDef.getType(), null, null, null, null, false);
 assertNotNull(bpw);
 bpw.setNullValueSerializer(null);
 assertNull(bpw.getNullValueSerializer());
 BeanPropertyWriter bpw2 = propertyBuilder.buildWriter(prov, propDef, propDef.getType(), null, null, null, null, true);
 assertNotNull(bpw2);
 bpw2.setNullValueSerializer(null);
 assertNull(bpw2.getNullValueSerializer());
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriter_InvalidSerializationType() throws JsonMappingException {
 SerializerProvider prov = new StandardSerializerProvider();
 BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition("invalidType");
 JavaType declaredType = propertyBuilder.findSerializationType(null, false, propDef.getType());
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, null, null, null, null, false);
 fail("Expected JsonMappingException but got "+bpw);
 }

 @Test
 public void testBuildWriter_PropertyAnnotations() throws JsonMappingException {
 SerializerProvider prov = new StandardSerializerProvider();
 BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition("customAnnotation");
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, propDef.getType(), null, null, null, null, false);
 assertNotNull(bpw);
 JsonSerializer<Object> ser = bpw.getNullValueSerializer();
 assertTrue(ser instanceof CustomJsonSerializer);
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriter_MissingProperty() throws JsonMappingException {
 SerializerProvider prov = new StandardSerializerProvider();
 BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition("missingProperty");
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, propDef.getType(), null, null, null, null, false);
 fail("Expected JsonMappingException but got "+bpw);
 }

 @Test
 public void testUseRealPropertyDefaults() throws JsonMappingException {
 SerializerProvider prov = new StandardSerializerProvider();
 BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition("useRealPropertyDefaults");
 Object valueToSuppress = propertyBuilder.getPropertyDefaultValue("useRealPropertyDefaults", null, propDef.getType());
 assertNotNull(valueToSuppress);
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, propDef.getType(), null, null, null, null, true);
 assertNull(bpw.getValueToSuppress());
 }

 @Test
 public void testDefaultValue() {
 Object defaultValue = propertyBuilder.getDefaultValue(Object.class.getType());
 assertEquals(null, defaultValue);
 }

 @Test
 public void testDefaultValue_Array() {
 Object defaultValue = propertyBuilder.getDefaultValue(new TypeReference<Object[]>() {});
 assertArrayEquals(new Object[0], defaultValue);
 }

 @Test
 public void testDefaultValue_Map() {
 Object defaultValue = propertyBuilder.getDefaultValue(new TypeReference<Object>() {});
 assertTrue(defaultValue instanceof Map<?,?>);
 assertEquals(0, ((Map<?,?>)defaultValue).size());
 }

 @Test
 public void testDefaultValue_String() {
 Object defaultValue = propertyBuilder.getDefaultValue(String.class);
 assertEquals("", defaultValue);
 }

 @Test
 public void testDefaultValue_Integer() {
 Object defaultValue = propertyBuilder.getDefaultValue(Integer.TYPE);
 assertEquals(0, defaultValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildWriter_NullPropertyWriter() throws JsonMappingException {
 SerializerProvider prov = new StandardSerializerProvider();
 BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition("name");
 BeanPropertyWriter bpw = null;
 BeanPropertyWriter bpw2 = propertyBuilder.buildWriter(prov, propDef, propDef.getType(), null, null, null, null, false);
 fail("Expected IllegalArgumentException but got "+bpw2);
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriter_NullSerializerProvider() throws JsonMappingException {
 BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition("name");
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(null, propDef, propDef.getType(), null, null, null, null, false);
 fail("Expected JsonMappingException but got "+bpw);
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriter_NullPropertyDefinition() throws JsonMappingException {
 SerializerProvider prov = new StandardSerializerProvider();
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, null, null, null, null, null, null, false);
 fail("Expected JsonMappingException but got "+bpw);
 }

 @Test
 public void testBuildWriter_NullTypeSerializer() throws JsonMappingException {
 SerializerProvider prov = new StandardSerializerProvider();
 JavaType declaredType = new TypeReference<List<Object>>() {}.getType();
 BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition("list");
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, null, null, null, null, false);
 assertNotNull(bpw);
 }

 @Test
 public void testBuildWriter_NullType() throws JsonMappingException {
 SerializerProvider prov = new StandardSerializerProvider();
 BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition("name");
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, null, null, null, null, null, false);
 assertNotNull(bpw);
 }

 @Test
 public void testBuildWriter_NullSerializer() throws JsonMappingException {
 SerializerProvider prov = new StandardSerializerProvider();
 BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition("name");
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, propDef.getType(), null, null, null, null, false);
 assertNotNull(bpw);
 JsonSerializer<Object> ser = bpw.getNullValueSerializer();
 assertNull(ser);
 BeanPropertyWriter bpw2 = propertyBuilder.buildWriter(prov, propDef, propDef.getType(), new stdSerializer<Object>(Object.class), null, null, null, false);
 assertNotNull(bpw2);
 JsonSerializer<Object> ser2 = bpw2.getNullValueSerializer();
 assertNotNull(ser2);
 }

 @Test
 public void testBuildWriter_NullAnnotatedMember() throws JsonMappingException {
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
 assertNull(bpw);
 }

 @Test
 public void testBuildWriter_NullDefaultUseStaticTyping() throws JsonMappingException {
 SerializerProvider prov = new StandardSerializerProvider();
 BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition("name");
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, propDef.getType(), null, null, null, null, false, false);
 assertNotNull(bpw);
 }
}