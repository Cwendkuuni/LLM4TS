import com.fasterxml.jackson.core.SerializationConfig;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private SerializationConfig config;
 private BeanDescription beanDesc;

 @Before
 public void setup() {
 config = new SerializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 try {
 beanDesc = mapper.getTypeFactory().constructType(Bean.class);
 } catch (JsonProcessingException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testBuilder() {
 PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
 assertNotNull(builder);
 }

 @Test
 public void testGetClassAnnotations() {
 Annotations annotations = builder.getClassAnnotations();
 assertNotNull(annotations);
 }

 @Test
 public void testBuildWriterNull() {
 assertNull(builder.buildWriter(null, null, null, null, null, null, null, false));
 }

 @Test
 public void testBuildWriterNoPropertyDef() {
 BeanPropertyDefinition propDef = null;
 assertNull(builder.buildWriter(null, propDef, null, null, null, null, null, false));
 }

 @Test
 public void testBuildWriterDefault() throws JsonMappingException {
 SerializerProvider prov = new StandardSerializerProvider();
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("name");
 BeanPropertyWriter writer = builder.buildWriter(prov, propDef, null, null, null, null, null, false);
 assertNotNull(writer);
 }

 @Test
 public void testBuildWriterCustomType() throws JsonMappingException {
 JavaType declaredType = TypeFactory.defaultInstance().constructType(String[].class);
 SerializerProvider prov = new StandardSerializerProvider();
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("name");
 TypeSerializer typeSer = null;
 BeanPropertyWriter writer = builder.buildWriter(prov, propDef, declaredType, null, null, typeSer, null, false);
 assertNotNull(writer);
 }

 @Test
 public void testBuildWriterWithNullContentTypeSerializer() throws JsonMappingException {
 TypeSerializer contentTypeSer = null;
 BeanPropertyWriter writer = builder.buildWriter(null, null, null, null, null, contentTypeSer, null, false);
 assertNull(writer);
 }

 @Test
 public void testBuildWriterWithContentTypeSerializer() throws JsonMappingException {
 String serializationTypeName = "java.util.ArrayList";
 TypeSerializer contentTypeSer = new StdTypeSerializer(TypeSerializationConfig.getDefaultInstance(), serializationTypeName);
 BeanPropertyWriter writer = builder.buildWriter(null, null, null, null, null, contentTypeSer, null, false);
 assertNotNull(writer);
 }

 @Test
 public void testGetClassAnnotationsForEmptyClass() {
 BeanDescription emptyBeanDesc = mapper.getTypeFactory().constructType(Object.class);
 PropertyBuilder emptyBuilder = new PropertyBuilder(config, emptyBeanDesc);
 Annotations annotations = emptyBuilder.getClassAnnotations();
 assertTrue(annotations.isEmpty());
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriterWithNonExistentProperty() throws JsonMappingException {
 BeanPropertyDefinition nonExistentPropDef = beanDesc.findPropertyDefinition("nonExistentProperty");
 BeanPropertyWriter writer = builder.buildWriter(null, nonExistentPropDef, null, null, null, null, null, false);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testBuildWriterWithNullSer() {
 Serializer nullSer = null;
 BeanPropertyWriter writer = builder.buildWriter(null, null, null, null, null, null, null, false);
 assertNull(writer);
 }

 @Test
 public void testBuildWriterWithNullSerializerProvider() {
 SerializerProvider nullProv = null;
 BeanPropertyWriter writer = builder.buildWriter(nullProv, null, null, null, null, null, null, false);
 assertNull(writer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildWriterWithDeclaredTypeNotAContainer() throws JsonMappingException {
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("name");
 BeanPropertyWriter writer = builder.buildWriter(null, propDef, null, null, null, null, null, false);
 assertNotNull(writer);

 BeanPropertyWriter writer2 = builder.buildWriter(null, propDef, TypeFactory.defaultInstance().constructType(int.class), null, null, null, null, false);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildWriterWithNullSerializationType() throws JsonMappingException {
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("name");
 BeanPropertyWriter writer = builder.buildWriter(null, propDef, null, null, null, null, null, false);
 assertNotNull(writer);

 BeanPropertyWriter writer2 = builder.buildWriter(null, propDef, null, null, null, null, null, true);
 fail("Expected IllegalStateException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildWriterWithDeclaredTypeNotMatchingSerializationType() throws JsonMappingException {
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("name");
 TypeSerializer typeSer = new StdTypeSerializer(TypeSerializationConfig.getDefaultInstance(), String[].class.getName());
 BeanPropertyWriter writer = builder.buildWriter(null, propDef, null, null, null, typeSer, null, false);
 assertNotNull(writer);

 BeanPropertyWriter writer2 = builder.buildWriter(null, propDef, TypeFactory.defaultInstance().constructType(int.class), null, null, typeSer, null, false);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testBuildWriterWithSuppressNullsTrue() throws JsonMappingException {
 SerializerPropertyWriterNullser = new JsonSerializer<Object>() {};
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("name");
 BeanPropertyWriter writer = builder.buildWriter(null, propDef, null, null, null, null, null, true);
 writer.assignNullSerializer(nullSer);
 assertNotNull(writer);
 }

 @Test
 public void testBuildWriterWithNullSerializerAssigned() throws JsonMappingException {
 SerializerPropertyWriterNullser = new JsonSerializer<Object>() {};
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("name");
 BeanPropertyWriter writer = builder.buildWriter(null, propDef, null, null, null, null, null, true);
 writer.assignNullSerializer(nullSer);
 writer.assignNullSerializer(nullSer);
 assertNotNull(writer);
 }

 @Test
 public void testBuildWriterWithUnwrappingNameTransformer() throws JsonMappingException {
 PropertyNameTransformer unwrappingTransformer = new PropertyNameTransformerDefaultImpl();
 BeanPropertyWriter writer = builder.buildWriter(null, null, null, null, null, null, null, false);
 writer = writer.unwrappingWriter(unwrappingTransformer);
 assertNotNull(writer);
 }

}