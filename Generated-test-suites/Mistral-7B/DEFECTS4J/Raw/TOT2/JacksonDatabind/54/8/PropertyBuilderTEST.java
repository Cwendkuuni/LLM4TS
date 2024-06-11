import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PropertyBuilder Test")
public class PropertyBuilderTest {

 @BeforeAll
 static void setup() {
 ObjectMapper mapper = new ObjectMapper();
 // Setup for testing
 }

 @Test
 @DisplayName("Test PropertyBuilder constructor")
 void testPropertyBuilderConstructor() {
 SerializationConfig config = mapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(null, null, null, null, null);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 assertNotNull(propertyBuilder);
 }

 @Test
 @DisplayName("Test getClassAnnotations method")
 void testGetClassAnnotations() throws Exception {
 SerializationConfig config = mapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(null, null, null, null, null);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 Annotations annotations = propertyBuilder.getClassAnnotations();
 assertNotNull(annotations);
 }

 @Test
 @DisplayName("Test buildWriter method with default useStaticTyping = true")
 void testBuildWriterDefaultUseStaticTypingTrue() throws JsonMappingException {
 SerializationConfig config = mapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(null, null, null, null, null);
 ObjectType declaredType = ObjectTypeFactory.defaultInstance().constructSimpleType(String.class);
 JsonSerializer<String> ser = mock(JsonSerializer.class);
 TypeSerializer typeSer = mock(TypeSerializer.class);
 TypeSerializer contentTypeSer = null;
 AnnotatedMember am = mock(AnnotatedMember.class);
 boolean defaultUseStaticTyping = true;
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 BeanPropertyWriter result = propertyBuilder.buildWriter(mock(SerializerProvider.class), mock(BeanPropertyDefinition.class), declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
 assertNotNull(result);
 }

 @Test
 @DisplayName("Test buildWriter method with default useStaticTyping = false")
 void testBuildWriterDefaultUseStaticTypingFalse() throws JsonMappingException {
 SerializationConfig config = mapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(null, null, null, null, null);
 ObjectType declaredType = ObjectTypeFactory.defaultInstance().constructSimpleType(String.class);
 JsonSerializer<String> ser = mock(JsonSerializer.class);
 TypeSerializer typeSer = mock(TypeSerializer.class);
 TypeSerializer contentTypeSer = null;
 AnnotatedMember am = mock(AnnotatedMember.class);
 boolean defaultUseStaticTyping = false;
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 BeanPropertyWriter result = propertyBuilder.buildWriter(mock(SerializerProvider.class), mock(BeanPropertyDefinition.class), declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
 assertNotNull(result);
 }

 @Test
 @DisplayName("Test findSerializationType method with annotation")
 void testFindSerializationTypeWithAnnotation() throws JsonMappingException {
 SerializationConfig config = mapper.getSerializationConfig();
 AnnotatedMember am = mock(AnnotatedMember.class);
 JavaType declaredType = ObjectTypeFactory.defaultInstance().constructSimpleType(String.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
 JavaType result = propertyBuilder.findSerializationType(am, true, declaredType);
 assertEquals(String.class, result.getRawClass());
 }

 @Test
 @DisplayName("Test findSerializationType method without annotation")
 void testFindSerializationTypeWithoutAnnotation() throws JsonProcessingException {
 SerializationConfig config = mapper.getSerializationConfig();
 ObjectType declaredType = ObjectTypeFactory.defaultInstance().constructSimpleType(String.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
 JavaType result = propertyBuilder.findSerializationType(null, false, declaredType);
 assertNull(result);
 }

 @Test
 @DisplayName("Test getPropertyDefaultValue method for String property")
 void testGetPropertyDefaultValueForStringProperty() throws Exception {
 SerializationConfig config = mapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(null, null, null, null, null);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 Object defaultBean = propertyBuilder.getDefaultBean();
 String name = "name";
 AnnotatedMember member = mock(AnnotatedMember.class);
 JavaType type = ObjectTypeFactory.defaultInstance().constructSimpleType(String.class);
 Object result = propertyBuilder.getPropertyDefaultValue(name, member, type);
 assertEquals("", result);
 }

 @Test
 @DisplayName("Test getPropertyDefaultValue method for Integer property")
 void testGetPropertyDefaultValueForIntegerProperty() throws Exception {
 SerializationConfig config = mapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(null, null, null, null, null);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 Object defaultBean = propertyBuilder.getDefaultBean();
 String name = "name";
 AnnotatedMember member = mock(AnnotatedMember.class);
 JavaType type = ObjectTypeFactory.defaultInstance().constructSimpleType(int.class);
 Object result = propertyBuilder.getPropertyDefaultValue(name, member, type);
 assertEquals(0, result);
 }

 @Test
 @DisplayName("Test getDefaultValue method for String type")
 void testGetDefaultValueForStringType() {
 JavaType type = ObjectTypeFactory.defaultInstance().constructSimpleType(String.class);
 Object result = PropertyBuilder.this.getDefaultValue(type);
 assertEquals("", result);
 }

 @Test
 @DisplayName("Test getDefaultValue method for Integer type")
 void testGetDefaultValueForIntegerType() {
 JavaType type = ObjectTypeFactory.defaultInstance().constructSimpleType(int.class);
 Object result = PropertyBuilder.this.getDefaultValue(type);
 assertEquals(0, result);
 }

 @Test
 @DisplayName("Test getDefaultValue method for CustomType")
 void testGetDefaultValueForCustomType() {
 JavaType type = ObjectTypeFactory.defaultInstance().constructType(CustomType.class);
 Object result = PropertyBuilder.this.getDefaultValue(type);
 assertNull(result);
 }

 @Test
 @DisplayName("Test _throwWrapped method")
 void testThrowWrappedMethod() {
 SerializationConfig config = mapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(null, null, null, null, null);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 String propName = "name";
 Object defaultBean = new Object();
 Exception e = new Exception("Test Exception");
 Object result = propertyBuilder._throwWrapped(e, propName, defaultBean);
 fail("_throwWrapped method should have thrown an IllegalArgumentException");
 }
}