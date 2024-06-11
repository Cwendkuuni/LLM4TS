import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private SerializationConfig config;
 private BeanDescription beanDesc;
 private PropertyBuilder propertyBuilder;

 @Before
 public void setup() throws JsonMappingException {
 JsonFactory jsonFactory = new JsonFactory();
 ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
 config = objectMapper.getSerializationConfig();
 beanDesc = new BeanDescription(JsonPojo.class);
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 }

 @Test
 public void testClassAnnotations() {
 Annotations annotations = propertyBuilder.getClassAnnotations();
 assertEquals(JsonInclude.Include.NON_DEFAULT, annotations.findValue(JsonInclude.class));
 }

 @Test
 public void testBuildWriter_SimpleType() throws JsonMappingException {
 ObjectWriter objectWriter = config.builder()
 .forType(SimpleType.class)
 .build();
 BeanPropertyWriter builder = propertyBuilder.buildWriter(
 new SerializerProvider(),
 null,
 JavaType.getType(SimpleType.class),
 null,
 null,
 null,
 null,
 false
 );
 assertEquals(builder.getSerializer(), objectWriter.getSerializerProvider().getSerializerInstance(SimpleType.class));
 }

 @Test
 public void testBuildWriter_ContainerType() throws JsonMappingException {
 ObjectWriter objectWriter = config.builder()
 .forType(List.class)
 .build();
 BeanPropertyWriter builder = propertyBuilder.buildWriter(
 new SerializerProvider(),
 null,
 JavaType.getType(List.class),
 null,
 null,
 objectWriter,
 null,
 false
 );
 assertEquals(objectWriter.getSerializerProvider().getSerializerInstance(List.class), builder.getSerializer());
 }

 @Test
 public void testFindSerializationType_SimpleType() throws JsonMappingException {
 JavaType javaType = propertyBuilder.findSerializationType(new Annotated(), false, JavaType.getType(SimpleType.class));
 assertEquals(JavaType.getType(SimpleType.class), javaType);
 }

 @Test
 public void testFindSerializationType_ContainerType() throws JsonMappingException {
 JavaType javaType = propertyBuilder.findSerializationType(new Annotated(), false, JavaType.getType(List.class));
 assertEquals(JavaType.typeFromValue(List.class, null), javaType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindSerializationType_InvalidType() throws JsonMappingException {
 propertyBuilder.findSerializationType(new Annotated(), false, JavaType.getType(String.class));
 }

 @Test
 public void testGetClassAnnotations_EmptyAnnotations() {
 Annotations annotations = propertyBuilder.getClassAnnotations();
 assertNull(annotations);
 }

 @Test
 public void testGetDefaultBean_NullDefaultBean() {
 Object defaultBean = propertyBuilder.getDefaultBean();
 assertNull(defaultBean);
 }

 @Test
 public void testGetDefaultBean_DefaultBeanInstance() {
 Object defaultBean = propertyBuilder.getDefaultBean();
 assertNotNull(defaultBean);
 }

 @Test
 public void testGetPropertyDefaultValue_PrimitiveType() {
 int defaultValue = propertyBuilder.getDefaultValue(JavaType.getType(int.class));
 assertEquals(0, defaultValue);
 }

 @Test
 public void testGetPropertyDefaultValue_StringType() {
 String defaultValue = propertyBuilder.getDefaultValue(JavaType.getType(String.class));
 assertEquals("", defaultValue);
 }

 @Test
 public void testGetPropertyDefaultValue_ContainerType() {
 Object defaultValue = propertyBuilder.getDefaultValue(JavaType.getType(List.class));
 assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
 }

 @Test
 public void testGetPropertyDefaultValue_ReferenceType() {
 Object defaultValue = propertyBuilder.getDefaultValue(JavaType.getType(StringBuffer.class));
 assertEquals(JsonInclude.Include.NON_DEFAULT, defaultValue);
 }

 @Test(expected = NullPointerException.class)
 public void testGetPropertyDefaultValue_NullType() {
 propertyBuilder.getPropertyDefaultValue(null, null, null);
 }
}