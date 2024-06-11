import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private PropertyBuilder propertyBuilder;
 private ObjectNode node;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 propertyBuilder = new PropertyBuilder(mapper.getSerializationConfig(), mapper.getDefaultSerializerProvider().findBeanDescription(MyClass.class));
 objectMapper = mapper;
 }

 @Test
 public void testBuildWriter_Default() throws JsonProcessingException {
 BeanPropertyWriter writer = propertyBuilder.buildWriter(objectMapper.getSerializationConfig(), null, null, null, null, null, null, false);
 assertNotNull(writer);
 }

 @Test
 public void testBuildWriter_WithSerializationType() throws JsonProcessingException {
 BeanPropertyDefinition propDef = new BeanPropertyDefinition(MyClass.class, "name");
 JavaType declaredType = ObjectTypeFactory.defaultInstance().constructType(String.class);
 JsonSerializer<String> ser = new JsonSerializer<String>() {
 @Override
 public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeString(value);
 }
 };
 TypeSerializer typeSer = TypeSerializer.empty();
 BeanPropertyWriter writer = propertyBuilder.buildWriter(objectMapper.getSerializationConfig(), propDef, declaredType, ser, typeSer, null, null, false);
 assertNotNull(writer);
 }

 @Test
 public void testBuildWriter_WithContentTypeSerializer() throws JsonProcessingException {
 BeanPropertyDefinition propDef = new BeanPropertyDefinition(MyClass.class, "list");
 JavaType declaredType = TypeFactory.type(ArrayList.class, Integer.class);
 JsonSerializer<Integer> ser = new JsonSerializer<Integer>() {
 @Override
 public void serialize(Integer value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeNumber(value.intValue());
 }
 };
 TypeSerializer contentTypeSer = new StandardSerializer<>(ArrayList.class);
 BeanPropertyWriter writer = propertyBuilder.buildWriter(objectMapper.getSerializationConfig(), propDef, declaredType, ser, null, contentTypeSer, null, false);
 assertNotNull(writer);
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriter_WithInvalidSerializationType() throws JsonProcessingException {
 BeanPropertyDefinition propDef = new BeanPropertyDefinition(MyClass.class, "name");
 JsonMappingException expectedException = new JsonMappingException("Illegal concrete-type annotation for method 'getName': class [B not a super-type of (declared) class class java.lang.String");
 try {
 propertyBuilder.buildWriter(objectMapper.getSerializationConfig(), propDef, null, null, null, TypeFactory.type(byte[].class), null, false);
 } catch (JsonMappingException e) {
 assertEquals(expectedException.getMessage(), e.getMessage());
 }
 }

 @Test
 public void testGetClassAnnotations() {
 Annotations annotations = propertyBuilder.getClassAnnotations();
 assertNotNull(annotations);
 }

 @Test
 public void testGetSerializationType_NoAnnotation() throws JsonMappingException {
 JavaType serializationType = propertyBuilder.findSerializationType(null, false, null);
 assertNull(serializationType);
 }

 @Test
 public void testGetSerializationType_WithAnnotation() throws JsonMappingException {
 MyClass myClass = new MyClass();
 AnnotatedMember am = new AnnotatedMember(MyClass.class, "name");
 am.setAnnotations(new JsonSerializeAnnotation(MyClass.class, "name", String.class));
 JavaType serializationType = propertyBuilder.findSerializationType(am, false, null);
 assertEquals(String.class, serializationType.getRawClass());
 }

 @Test
 public void testGetDefaultBean() {
 Object defaultBean = propertyBuilder.getDefaultBean();
 assertNotNull(defaultBean);
 }

 @Test
 public void testgetPropertyDefaultValue() throws JsonMappingException {
 MyClass myClass = new MyClass();
 Object name = myClass.getName();
 Object nameDefault = propertyBuilder.getPropertyDefaultValue("name", null, MyClass.class);
 assertNull(nameDefault);
 assertEquals(name, propertyBuilder.getPropertyDefaultValue("name", newAnnotatedMember(MyClass.class, "name"), MyClass.class));
 }

 @Test
 public void testgetDefaultValue_String() {
 Object defaultValue = propertyBuilder.getDefaultValue(ObjectTypeFactory.constructType(String.class));
 assertEquals("", defaultValue);
 }

 @Test
 public void testgetDefaultValue_List() {
 Object defaultValue = propertyBuilder.getDefaultValue(TypeFactory.type(ArrayList.class, Integer.class));
 assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
 }

 @Test
 public void testgetDefaultValue_ClassWithDefaultConstructor() throws JsonMappingException {
 MyDefaultClass myDefaultClass = new MyDefaultClass();
 Object defaultBean = propertyBuilder.getDefaultBean();
 assertNotNull(defaultBean);
 assertEquals(myDefaultClass, defaultBean);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testgetDefaultValue_Null() throws JsonMappingException {
 Object defaultBean = propertyBuilder.getDefaultBean();
 assertNull(defaultBean);
 MyClass myClass = null;
 Object nameDefault = propertyBuilder.getPropertyDefaultValue("name", null, MyClass.class);
 assertEquals(MyClass.class.getName(), nameDefault.toString());
 }

 @Test
 public void testgetDefaultValue_NoDefaultConstructor() throws JsonMappingException {
 MyClassWithoutDefaultConstructor myClassWithoutDefaultConstructor = new MyClassWithoutDefaultConstructor();
 try {
 propertyBuilder.getDefaultBean();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}