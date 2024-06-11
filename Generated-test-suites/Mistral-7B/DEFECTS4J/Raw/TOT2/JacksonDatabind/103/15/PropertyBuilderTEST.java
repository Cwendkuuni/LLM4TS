import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private ObjectMapper mapper;

 @BeforeClass
 public static void setup() {
 ObjectMapper mapper = new ObjectMapper();
 }

 /**
 * Testing constructor of PropertyBuilder with valid arguments
 */
 @Test
 public void testPropertyBuilderConstruction() {
 SerializationConfig config = new SerializationConfig();
 BeanDescription beanDesc = new BeanDescription(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 assertNotNull(propertyBuilder);
 }

 /**
 * Testing getClassAnnotations() method
 */
 @Test
 public void testGetClassAnnotations() throws JsonProcessingException {
 SerializationConfig config = new SerializationConfig();
 BeanDescription beanDesc = new BeanDescription(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 Annotations annotations = propertyBuilder.getClassAnnotations();
 assertTrue(annotations.annotations().length > 0);
 }

 /**
 * Testing buildWriter() method with valid arguments
 */
 @Test
 public void testBuildWriter() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProp", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 JavaType declaredType = mapper.getTypeFactory().constructType(String.class);
 JsonSerializer<?> ser = new StringSerializer();
 TypeSerializer typeSer = null;
 TypeSerializer contentTypeSer = null;
 AnnotatedMember am = new SimpleAnnotatedMember("getName", String.class, null, null);
 boolean defaultUseStaticTyping = false;
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
 BeanPropertyWriter writer = propertyBuilder.buildWriter(new Provider(), propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
 assertNotNull(writer);
 }

 /**
 * Testing buildWriter() method with null arguments
 */
 @Test(expected = JsonMappingException.class)
 public void testBuildWriterWithNullArguments() {
 ObjectMapper mapper = new ObjectMapper();
 BeanPropertyDefinition propDef = null;
 JavaType declaredType = null;
 JsonSerializer<?> ser = null;
 TypeSerializer typeSer = null;
 TypeSerializer contentTypeSer = null;
 AnnotatedMember am = null;
 boolean defaultUseStaticTyping = true;
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
 try {
 propertyBuilder.buildWriter(new Provider(), propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 /**
 * Testing findSerializationType() method with valid arguments
 */
 @Test
 public void testFindSerializationType() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 Annotated a = new SimpleAnnotatedMember("testAnnotated", String.class, null, null);
 JavaType declaredType = mapper.getTypeFactory().constructType(String.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), null);
 JavaType serializationType = propertyBuilder.findSerializationType(a, false, declaredType);
 assertEquals(declaredType, serializationType);
 }

 /**
 * Testing findSerializationType() method with invalid arguments
 */
 @Test(expected = JsonMappingException.class)
 public void testFindSerializationTypeWithInvalidArguments() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 Annotated a = null;
 JavaType declaredType = null;
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), null);
 try {
 propertyBuilder.findSerializationType(a, false, declaredType);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 /**
 * Testing getDefaultBean() method
 */
 @Test
 public void testGetDefaultBean() throws JsonMappingException {
 SerializationConfig config = new SerializationConfig();
 BeanDescription beanDesc = new BeanDescription(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 Object defaultBean = propertyBuilder.getDefaultBean();
 assertNull(defaultBean);
 }

 /**
 * Testing getPropertyDefaultValue() method with valid arguments
 */
 @Test
 public void testGetPropertyDefaultValue() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 BeanDescription beanDesc = new BeanDescription(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), beanDesc);
 String defaultValue = propertyBuilder.getPropertyDefaultValue("testProp", new SimpleAnnotatedMember("getName", String.class, null, null), mapper.getTypeFactory().constructType(String.class));
 assertEquals("", defaultValue);
 }

 /**
 * Testing getPropertyDefaultValue() method with null arguments
 */
 @Test(expected = NullPointerException.class)
 public void testGetPropertyDefaultValueWithNullArguments() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), null);
 try {
 propertyBuilder.getPropertyDefaultValue(null, null, null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 /**
 * Testing getPropertyDefaultValue() method with invalid property name
 */
 @Test(expected = IllegalArgumentException.class)
 public void testGetPropertyDefaultValueWithInvalidPropertyName() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), null);
 try {
 propertyBuilder.getPropertyDefaultValue("invalidPropertyName", new SimpleAnnotatedMember("getName", String.class, null, null), mapper.getTypeFactory().constructType(String.class));
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 /**
 * Testing getPropertyDefaultValue() method with null member
 */
 @Test(expected = NullPointerException.class)
 public void testGetPropertyDefaultValueWithNullMember() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), null);
 try {
 propertyBuilder.getPropertyDefaultValue("testProp", null, mapper.getTypeFactory().constructType(String.class));
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 /**
 * Testing getPropertyDefaultValue() method with null type
 */
 @Test(expected = IllegalArgumentException.class)
 public void testGetPropertyDefaultValueWithNullType() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), null);
 try {
 propertyBuilder.getPropertyDefaultValue("testProp", new SimpleAnnotatedMember("getName", null, null, null), null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 /**
 * Testing _throwWrapped() method with null exception
 */
 @Test(expected = NullPointerException.class)
 public void testThrowWrappedWithNullException() {
 ObjectMapper mapper = new ObjectMapper();
 try {
 propertyBuilder._throwWrapped(null, "testProp", null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 /**
 * Testing _throwWrapped() method with IllegalArgumentException exception
 */
 @Test
 public void testThrowWrappedWithIllegalArgumentException() {
 ObjectMapper mapper = new ObjectMapper();
 IllegalArgumentException e = new IllegalArgumentException("testException");
 try {
 propertyBuilder._throwWrapped(e, "testProp", null);
 fail("Expected no exception");
 } catch (IllegalArgumentException ex) {
 assertEquals("testException", ex.getMessage());
 }
 }
}