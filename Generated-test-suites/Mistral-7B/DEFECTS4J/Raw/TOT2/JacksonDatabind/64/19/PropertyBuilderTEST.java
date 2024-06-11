import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private ObjectMapper objectMapper;

 @BeforeClass
 public static void setupClass() {
 ObjectMapperFactory.setMapperInstance(new ObjectMapper());
 }

 @Before
 public void setup() {
 objectMapper = ObjectMapperFactory.getMapper();
 }

 @Test
 public void testPropertyBuilderConstructor() {
 // Test constructor with valid SerializationConfig and BeanDescription
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 assertNotNull(propertyBuilder);
 }

 @Test(expected = NullPointerException.class)
 public void testPropertyBuilderConstructorNullSerializationConfig() {
 PropertyBuilder propertyBuilder = new PropertyBuilder(null, new BeanDescription());
 }

 @Test(expected = NullPointerException.class)
 public void testPropertyBuilderConstructorNullBeanDescription() {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), null);
 }

 @Test
 public void testGetClassAnnotations() {
 // Test getClassAnnotations method with valid PropertyBuilder instance
 BeanDescription beanDescription = new BeanDescription();
 SerializationConfig serializationConfig = new SerializationConfig();
 PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
 Annotations annotations = propertyBuilder.getClassAnnotations();
 assertNotNull(annotations);
 }

 @Test
 public void testBuildWriterWithValidParameters() throws JsonMappingException {
 // Test buildWriter method with valid parameters
 BeanPropertyDefinition propDef = new BeanPropertyDefinition(new AnnotatedMember(), new BeanDescription(), new JavaType(), null, null, null, null, false, null);
 SerializerProvider serializerProvider = new SerializerProvider();
 TypeSerializer typeSer = null;
 JsonSerializer<?> jsonSerializer = null;
 TypeSerializer contentTypeSer = null;
 AnnotatedMember am = null;
 boolean defaultUseStaticTyping = false;
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, propDef, new JavaType(), jsonSerializer, typeSer, contentTypeSer, am, defaultUseStaticTyping);
 assertNotNull(beanPropertyWriter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildWriterWithNullSerializerProvider() throws JsonMappingException {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
 }

 @Test(expected = NullPointerException.class)
 public void testBuildWriterWithNullBeanPropertyDefinition() throws JsonMappingException {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(new SerializerProvider(), null, null, null, null, null, null, false);
 }

 @Test(expected = NullPointerException.class)
 public void testBuildWriterWithNullJavaType() throws JsonMappingException {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(new SerializerProvider(), new BeanPropertyDefinition(null, null, null, null, null, null, null, false, null), null, null, null, null, null, false);
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriterWithInvalidPropertyDefinition() throws JsonMappingException {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(new SerializerProvider(), new BeanPropertyDefinition(null, null, null, null, null, null, null, false, null), null, null, null, null, null, false);
 }

 @Test
 public void testFindSerializationTypeWithValidAnnotations() throws JsonMappingException {
 // Test findSerializationType method with valid annotations
 Annotated a = new Annotated();
 JavaType declaredType = new JavaType();
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 JavaType serializationType = propertyBuilder.findSerializationType(a, true, declaredType);
 assertEquals(new JavaType(), serializationType);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindSerializationTypeWithEmptyAnnotations() throws JsonMappingException {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 JavaType serializationType = propertyBuilder.findSerializationType(new Annotated(), true, new JavaType());
 fail("Expected JsonMappingException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindSerializationTypeWithInvalidAnnotations() throws JsonMappingException {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 JavaType serializationType = propertyBuilder.findSerializationType(new Annotated(), true, new JavaType());
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testFindSerializationTypeWithNoAnnotations() throws JsonMappingException {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 JavaType serializationType = propertyBuilder.findSerializationType(new Annotated(), false, new JavaType());
 assertNull(serializationType);
 }

 @Test
 public void testGetPropertyDefaultValueWithValidNameAndMember() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 Object defaultBean = propertyBuilder.getDefaultBean();
 String propName = "testProperty";
 AnnotatedMember member = new AnnotatedMember();
 JavaType type = new JavaType();
 Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propName, member, type);
 assertEquals("", propertyDefaultValue);
 }

 @Test
 public void testGetPropertyDefaultValueWithNullName() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 Object defaultBean = propertyBuilder.getDefaultBean();
 String propName = null;
 AnnotatedMember member = new AnnotatedMember();
 JavaType type = new JavaType();
 Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propName, member, type);
 assertNull(propertyDefaultValue);
 }

 @Test
 public void testGetPropertyDefaultValueWithNullMember() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 Object defaultBean = propertyBuilder.getDefaultBean();
 String propName = "testProperty";
 AnnotatedMember member = null;
 JavaType type = new JavaType();
 Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propName, member, type);
 assertNull(propertyDefaultValue);
 }

 @Test
 public void testGetPropertyDefaultValueWithInvalidType() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 Object defaultBean = propertyBuilder.getDefaultBean();
 String propName = "testProperty";
 AnnotatedMember member = new AnnotatedMember();
 JavaType type = null;
 Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propName, member, type);
 assertNull(propertyDefaultValue);
 }

 @Test
 public void testGetPropertyDefaultValueWithNoDefaultBean() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 Object defaultBean = null;
 String propName = "testProperty";
 AnnotatedMember member = new AnnotatedMember();
 JavaType type = new JavaType();
 Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propName, member, type);
 assertNull(propertyDefaultValue);
 }

 @Test
 public void testGetPropertyDefaultValueWithNoGetMethod() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 Object defaultBean = propertyBuilder.getDefaultBean();
 String propName = "testProperty";
 AnnotatedMember member = new AnnotatedMember();
 JavaType type = new JavaType();
 Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propName, member, type);
 assertNull(propertyDefaultValue);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetPropertyDefaultValueWithInvalidGetMethod() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 Object defaultBean = propertyBuilder.getDefaultBean();
 String propName = "testProperty";
 AnnotatedMember member = new AnnotatedMember();
 JavaType type = new JavaType();
 Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propName, member, type);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testgetDefaultBeanWithValidInstance() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 Object defaultBean = propertyBuilder.getDefaultBean();
 assertNotNull(defaultBean);
 }

 @Test
 public void testgetDefaultBeanWithNullInstance() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 Object defaultBean = propertyBuilder.getDefaultBean();
 assertNull(defaultBean);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetDefaultValueWithInvalidType() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 Object propertyDefaultValue = propertyBuilder.getDefaultValue(null);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testGetDefaultValueWithPrimitiveType() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 Object propertyDefaultValue = propertyBuilder.getDefaultValue(new JavaType(int.class));
 assertEquals(0, propertyDefaultValue);
 }

 @Test
 public void testGetDefaultValueWithWrapperType() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 Object propertyDefaultValue = propertyBuilder.getDefaultValue(new JavaType(Integer.class));
 assertEquals(0, propertyDefaultValue);
 }

 @Test
 public void testGetDefaultValueWithStringType() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 Object propertyDefaultValue = propertyBuilder.getDefaultValue(new JavaType(String.class));
 assertEquals("", propertyDefaultValue);
 }

 @Test
 public void testGetDefaultValueWithContainerType() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 Object propertyDefaultValue = propertyBuilder.getDefaultValue(new JavaType(List.class));
 assertEquals(JsonInclude.Include.NON_EMPTY, propertyDefaultValue);
 }

 @Test
 public void testGetDefaultValueWithReferenceType() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 Object propertyDefaultValue = propertyBuilder.getDefaultValue(new JavaType(String[].class));
 assertEquals(JsonInclude.Include.NON_EMPTY, propertyDefaultValue);
 }

 @Test
 public void test_throwWrapped() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
 Exception e = new Exception();
 String propName = "testProperty";
 Object defaultBean = propertyBuilder.getDefaultBean();
 Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propName, new AnnotatedMember(), new JavaType());
 propertyBuilder._throwWrapped(e, propName, defaultBean);
 }

}