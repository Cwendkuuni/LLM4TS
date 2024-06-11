import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private SerializationConfig config;
 private ObjectNode node;
 private ObjectMapper mapper;
 private PropertyBuilder propertyBuilder;

 @Before
 public void setup() throws JsonProcessingException {
 config = new SerializationConfig();
 ObjectMapper objectMapper = new ObjectMapper(config);
 mapper = new ObjectMapper(objectMapper.getSerializerProvider().findValueSerializer(String.class));
 propertyBuilder = new PropertyBuilder(config, new BeanDescription(String.class));
 }

 @Test
 public void testBuildWriter_StringType() throws JsonMappingException {
 String propertyName = "testProperty";
 AnnotatedMember am = new AnnotatedMember(String.class, "testProperty", null);
 JavaType declaredType = new TypeReference<>("java.lang.String") {}.getType();
 JsonSerializer<?> serializer = mapper.findValueSerializer(String.class);
 TypeSerializer contentTypeSerializer = null;
 TypeSerializer typeSerializer = null;
 BeanPropertyDefinition propDef = new BeanPropertyDefinition(propertyName, propertyBuilder.getClassAnnotations(), declaredType);
 BeanPropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(), propDef, declaredType, serializer, typeSerializer, contentTypeSerializer, am, false);
 assertTrue(writer != null);
 }

 @Test
 public void testBuildWriter_ContainerType() throws JsonMappingException {
 String propertyName = "testList";
 AnnotatedMember am = new AnnotatedMember(ArrayList.class, "testList", null);
 JavaType declaredType = new TypeReference<>("java.util.ArrayList<java.lang.String>") {}.getType();
 JsonSerializer<?> serializer = mapper.findValueSerializer(String.class);
 TypeSerializer contentTypeSerializer = mapper.getSerializerProvider().getSerializerProvider().getSerializerProvider().findValueSerializer(ArrayList.class);
 TypeSerializer typeSerializer = mapper.getSerializerProvider().getSerializerProvider().getSerializerProvider().findValueSerializer(String[].class);
 BeanPropertyDefinition propDef = new BeanPropertyDefinition(propertyName, propertyBuilder.getClassAnnotations(), declaredType);
 BeanPropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(), propDef, declaredType, serializer, typeSerializer, contentTypeSerializer, am, false);
 assertTrue(writer != null);
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriter_Failure_InvalidType() throws JsonMappingException {
 String propertyName = "testObject";
 AnnotatedMember am = new AnnotatedMember(Object.class, "testObject", null);
 JavaType declaredType = new TypeReference<>("java.lang.Object") {}.getType();
 JsonSerializer<?> serializer = mapper.findValueSerializer(String.class);
 TypeSerializer contentTypeSerializer = null;
 TypeSerializer typeSerializer = null;
 BeanPropertyDefinition propDef = new BeanPropertyDefinition(propertyName, propertyBuilder.getClassAnnotations(), declaredType);
 BeanPropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(), propDef, declaredType, serializer, typeSerializer, contentTypeSerializer, am, false);
 fail("Expected JsonMappingException but got null");
 }

 @Test
 public void testFindSerializationType_Annotation_Valid() throws JsonMappingException {
 String propertyName = "testProperty";
 Annotated a = new AnnotatedMember(String.class, propertyName, null);
 JavaType declaredType = new TypeReference<>("java.lang.String") {}.getType();
 BeanPropertyDefinition propDef = new BeanPropertyDefinition(propertyName, propertyBuilder.getClassAnnotations(), declaredType);
 JavaType result = propertyBuilder.findSerializationType(a, false, declaredType);
 assertTrue(result != null);
 assertTrue(result.equals(declaredType));
 }

 @Test
 public void testFindSerializationType_Annotation_Invalid() throws JsonMappingException {
 String propertyName = "testProperty";
 Annotated a = new AnnotatedMember(String[].class, propertyName, null);
 JavaType declaredType = new TypeReference<>("java.lang.String[]") {}.getType();
 BeanPropertyDefinition propDef = new BeanPropertyDefinition(propertyName, propertyBuilder.getClassAnnotations(), declaredType);
 try {
 propertyBuilder.findSerializationType(a, false, declaredType);
 fail("Expected JsonMappingException but got null");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testGetDefaultBean() {
 Object defaultBean = propertyBuilder.getDefaultBean();
 assertNull(defaultBean);
 }

 @Test
 public void testGetPropertyDefaultValue_PrimitiveType() {
 Object defaultValue = propertyBuilder.getPropertyDefaultValue("testProperty", new AnnotatedMember(Integer.TYPE, "testProperty", null), new TypeReference<>("int") {}.getType());
 assertEquals(0, defaultValue.intValue());
 }

 @Test
 public void testGetPropertyDefaultValue_StringType() {
 Object defaultValue = propertyBuilder.getPropertyDefaultValue("testProperty", new AnnotatedMember(String.class, "testProperty", null), new TypeReference<>("java.lang.String") {}.getType());
 assertEquals("", defaultValue.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetPropertyDefaultValue_Failure() {
 Object defaultValue = propertyBuilder.getPropertyDefaultValue("testProperty", new AnnotatedMember(String[].class, "testProperty", null), new TypeReference<>("java.lang.String") {}.getType());
 fail("Expected IllegalArgumentException but got null");
 }

 @Test
 public void testGetPropertyDefaultValue_ReferenceType() {
 Object defaultValue = propertyBuilder.getPropertyDefaultValue("testProperty", new AnnotatedMember(String.class, "testProperty", null), new TypeReference<>("java.lang.String") {}.getType());
 assertNull(defaultValue);
 }

 @Test
 public void testGetPropertyDefaultValue_ContainerType() {
 Object defaultValue = propertyBuilder.getPropertyDefaultValue("testProperty", new AnnotatedMember(ArrayList.class, "testProperty", null), new TypeReference<>("java.util.ArrayList<java.lang.String>") {}.getType());
 assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
 }

 @Test
 public void testGetPropertyDefaultValue_NullType() {
 Object defaultValue = propertyBuilder.getPropertyDefaultValue("testProperty", new AnnotatedMember(Object.class, "testProperty", null), new TypeReference<>("java.lang.Object") {}.getType());
 assertNull(defaultValue);
 }

 @Test
 public void testGetPropertyDefaultValue_EmptyType() {
 Object defaultValue = propertyBuilder.
 getPropertyDefaultValue("testProperty", new AnnotatedMember(String[].class, "testProperty", null), new TypeReference<>("java.lang.String[]") {}.getType());
 assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
 }

 @Test
 public void testFindNullSerializer() {
 JsonSerializer<?> nullSerializer = propertyBuilder.findNullSerializer(new AnnotatedMember(String.class, "testProperty", null));
 assertNull(nullSerializer);
 }

 @Test
 public void testFindNullSerializer_present() throws JsonMappingException {
 JsonSerializer<?> nullSerializer = propertyBuilder.findNullSerializer(new AnnotatedMember(String.class, "testProperty", null));
 JsonSerializer<?> serializer = mapper.findValueSerializer(String.class);
 nullSerializer = new JsonSerializer<Object>() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeNull();
 }
 };
 Annotated ref = new AnnotatedMember(String.class, "testProperty", null);
 ref.setAnnotation(new NullSerializerAnnotation());
 assertEquals(serializer, propertyBuilder.findNullSerializer(ref));
 }

 @Test
 public void testFindUnwrappingNameTransformer() {
 NameTransformer nameTransformer = propertyBuilder.findUnwrappingNameTransformer(new AnnotatedMember(String.class, "testProperty", null));
 assertNull(nameTransformer);
 }

 @Test
 public void testFindUnwrappingNameTransformer_present() throws JsonMappingException {
 NameTransformer nameTransformer = propertyBuilder.findUnwrappingNameTransformer(new AnnotatedMember(String.class, "testProperty", null));
 NameTransformer annotatedNameTransformer = new NameTransformer() {
 @Override
 public String transform(String name, JsonSerializer<?> serializer) {
 return "newName";
 }
 };
 Annotated ref = new AnnotatedMember(String.class, "testProperty", null);
 ref.setAnnotation(new NameTransformerAnnotation(annotatedNameTransformer));
 assertEquals(annotatedNameTransformer, propertyBuilder.findUnwrappingNameTransformer(ref));
 }
}