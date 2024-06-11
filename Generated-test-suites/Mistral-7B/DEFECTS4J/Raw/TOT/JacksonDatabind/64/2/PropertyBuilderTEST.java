import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private ObjectMapper objectMapper;
 private PropertyBuilder propertyBuilder;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testBuilderWithDefaultProperties() throws JsonMappingException {
 // Given a PropertyBuilder instance with default properties
 SerializationConfig config = objectMapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(BeanDescription.StandardBuilder.stanдарtBuilder().forClass(TestClass.class));
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

 // When creating a BeanPropertyWriter
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);

 // Then the BeanPropertyWriter is not null
 assertNotNull(beanPropertyWriter);
 }

 @Test
 public void testBuilderWithNonDefaultProperties() throws JsonMappingException {
 // Given a PropertyBuilder instance with non-default properties
 SerializationConfig config = objectMapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(BeanDescription.StandardBuilder.stanдарtBuilder().forClass(TestClass.class));
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 propertyBuilder._defaultInclusion = JsonInclude.Value.include(JsonInclude.Include.NON_DEFAULT);

 // When creating a BeanPropertyWriter
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);

 // Then the BeanPropertyWriter is not null, and has non-default values set
 assertNotNull(beanPropertyWriter);
 assertEquals(JsonInclude.Include.NON_DEFAULT, propertyBuilder._defaultInclusion.getValueInclusion());
 }

 @Test
 public void testBuilderWithCustomSerializationType() throws JsonMappingException {
 // Given a PropertyBuilder instance with a custom serialization type
 SerializationConfig config = objectMapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(BeanDescription.StandardBuilder.stanдарtBuilder().forClass(TestClass.class));
 AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
 JavaType declaredType = TypeFactory.type(int[].class, String.class);
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 propertyBuilder._annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);
 JavaType serializationType = Mockito.mock(JavaType.class);
 when(propertyBuilder._annotationIntrospector.findSerializationType(annotatedMember, false, declaredType)).thenReturn(serializationType);

 // When creating a BeanPropertyWriter
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, declaredType, null, null, null, annotatedMember, false);

 // Then the BeanPropertyWriter has the custom serialization type set
 assertEquals(serializationType, beanPropertyWriter.getSerializationType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuilderWithInvalidSerializationType() throws JsonMappingException {
 // Given a PropertyBuilder instance
 SerializationConfig config = objectMapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(BeanDescription.StandardBuilder.stanдарtBuilder().forClass(TestClass.class));
 propertyBuilder = new PropertyBuilder(config, beanDesc);

 // When creating a BeanPropertyWriter with an invalid serialization type
 JavaType declaredType = TypeFactory.type(int.class, String.class);
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, declaredType, null, null, null, null, false);

 // Then an IllegalArgumentException is thrown
 fail("Expected an IllegalArgumentException to be thrown");
 }

 @Test(expected = JsonMappingException.class)
 public void testBuilderWithNullPropertyDefinition() throws JsonMappingException {
 // Given a PropertyBuilder instance
 SerializationConfig config = objectMapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(BeanDescription.StandardBuilder.stanдарtBuilder().forClass(TestClass.class));
 propertyBuilder = new PropertyBuilder(config, beanDesc);

 // When creating a BeanPropertyWriter with a null property definition
 BeanPropertyDefinition propDef = null;
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, propDef, null, null, null, null, null, false);
 // Then a JsonMappingException is thrown
 fail("Expected a JsonMappingException to be thrown");
 }

 @Test
 public void testBuilderWithSuppressedNulls() throws JsonMappingException {
 // Given a PropertyBuilder instance with suppressed nulls
 SerializationConfig config = objectMapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(BeanDescription.StandardBuilder.stanдарtBuilder().forClass(TestClass.class));
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 propertyBuilder._useRealPropertyDefaults = true;

 // When creating a BeanPropertyWriter
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);

 // Then null values are suppressed
 Object valueToSuppress = null;
 assertTrue(beanPropertyWriter.isSuppressNulls());
 assertTrue(beanPropertyWriter.hasValueToSuppress(valueToSuppress));
 }

 @Test
 public void testBuilderWithUnwrappingNameTransformer() throws JsonMappingException {
 // Given a PropertyBuilder instance with an unwrapping name transformer
 SerializationConfig config = objectMapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(BeanDescription.StandardBuilder.stanдарtBuilder().forClass(TestClass.class));
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 NameTransformer nameTransformer = Mockito.mock(NameTransformer.class);
 when(propertyBuilder._annotationIntrospector.findUnwrappingNameTransformer(Mockito.any())).thenReturn(nameTransformer);

 // When creating a BeanPropertyWriter with an unwrapping name transformer
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);

 // Then the BeanPropertyWriter has the unwrapping name transformer set
 assertEquals(nameTransformer, beanPropertyWriter.getUnwrappingNameTransformer());
 }

 @Test(expected = JsonMappingException.class)
 public void testBuilderWithInvalidAnnotationIntrospector() throws JsonMappingException {
 // Given a PropertyBuilder instance with an invalid annotation introspector
 SerializationConfig config = objectMapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(BeanDescription.StandardBuilder.stanдарtBuilder().forClass(TestClass.class));
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 propertyBuilder._annotationIntrospector = null;

 // When creating a BeanPropertyWriter
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);

 // Then a JsonMappingException is thrown
 fail("Expected a JsonMappingException to be thrown");
 }

 @Test
 public void testBuilderWithNullSerializerDefinition() throws JsonMappingException {
 // Given a PropertyBuilder instance with a null serializer definition
 SerializationConfig config = objectMapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(BeanDescription.StandardBuilder.stanдарtBuilder().forClass(TestClass.class));
 propertyBuilder = new PropertyBuilder(config, beanDesc);

 // When creating a BeanPropertyWriter with a null serializer definition
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);

 // Then the BeanPropertyWriter has no serializer definition set
 assertNull(beanPropertyWriter.getSerializer());
 }

 @Test
 public void testBuilderWithSerializerDefinition() throws JsonMappingException {
 // Given a PropertyBuilder instance with a serializer definition
 SerializationConfig config = objectMapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(BeanDescription.StandardBuilder.stanдарtBuilder().forClass(TestClass.class));
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 JsonSerializer<?> jsonSerializer = Mockito.mock(JsonSerializer.class);
 when(config.findValueSerializer(Mockito.any(BeanPropertyDefinition.class), Mockito.any(JavaType.class)))
 .thenReturn(jsonSerializer);

 // When creating a BeanPropertyWriter with a serializer definition
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, null, jsonSerializer, null, null, null, false);

 // Then the BeanPropertyWriter has the serializer definition set
 assertEquals(jsonSerializer, beanPropertyWriter.getSerializer());
 }

 @Test(expected = JsonMappingException.class)
 public void testBuilderWithNullContentTypeSerializer() throws JsonMappingException {
 // Given a PropertyBuilder instance
 SerializationConfig config = objectMapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(BeanDescription.StandardBuilder.stanдарtBuilder().forClass(TestClass.class));
 propertyBuilder = new PropertyBuilder(config, beanDesc);

 // When creating a BeanPropertyWriter with a null content type serializer
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);

 // Then a JsonMappingException is thrown
 fail("Expected a JsonMappingException to be thrown");
 }

 @Test
 public void testBuilderWithContentTypeSerializer() throws JsonMappingException {
 // Given a PropertyBuilder instance with a content type serializer
 SerializationConfig config = objectMapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(BeanDescription.StandardBuilder.stanдарtBuilder().forClass(TestClass.class));
 TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);
 propertyBuilder = new PropertyBuilder(config, beanDesc);

 // When creating a BeanPropertyWriter with a content type serializer
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, null, null, typeSerializer, null, null, false);

 // Then the BeanPropertyWriter has the content type serializer set
 assertEquals(typeSerializer, beanPropertyWriter.getContentTypeSerializer());
 }
}