import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private SerializationConfig config;
 private ObjectNode rootNode;
 private ObjectMapper mapper;

 @Before
 public void setup() throws JsonProcessingException {
 config = new SerializationConfig();
 mapper = new ObjectMapper(config);
 }

 @Test
 public void testBuildWriterWithDefaultProperties() throws JsonProcessingException {
 BeanDescription beanDesc = new BeanDescription(SomeClass.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 BeanPropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(), null, SomeClass.class.getType(), null, null, null, null, false);
 assertEquals(SomeClass.class, writer.getDeclaredType());
 }

 @Test
 public void testBuildWriterWithAnnotationSerializationType() throws JsonProcessingException {
 SomeClass someClass = new SomeClass();
 someClass.setName("test");
 AnnotatedMember member = ReflectionTestUtils.findField(SomeClass.class, "name");
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(SomeClass.class));
 JavaType serializedType = new TypeReference<>() {}.getType();
 BeanPropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(), null, SomeClass.class.getType(), null, null, null, member, true);
 assertEquals(serializedType, writer.getSerializationType());
 }

 @Test
 public void testBuildWriterWithContentTypeSerializer() throws JsonProcessingException {
 SomeClass someClass = new SomeClass();
 someClass.setList(new ArrayList<>());
 AnnotatedMember member = ReflectionTestUtils.findField(SomeClass.class, "list");
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(SomeClass.class));
 BeanPropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(), null, SomeClass.class.getType(), null, null, new TypeSerializer(), member, false);
 assertTrue(writer.getSerializationType().getContentType() != null);
 }

 @Test
 public void testBuildWriterWithNullSerializer() throws JsonProcessingException {
 SomeClass someClass = new SomeClass();
 AnnotatedMember member = ReflectionTestUtils.findField(SomeClass.class, "name");
 JsonSerializer<?> nullSerializer = null;
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(SomeClass.class));
 BeanPropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(), null, SomeClass.class.getType(), null, null, null, member, false);
 assertNull(writer.getNullSerializer());
 }

 @Test
 public void testBuildWriterWithUnwrappingNameTransformer() throws JsonProcessingException {
 SomeClass someClass = new SomeClass();
 someClass.setNestedObject(new NestedObject());
 AnnotatedMember member = ReflectionTestUtils.findField(SomeClass.class, "nestedObject");
 NameTransformer nameTransformer = new NameTransformer() {
 @Override
 public String transformDescription(BeanPropertyDescription description) {
 return description.getName().toUpperCase();
 }
 };
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(SomeClass.class));
 BeanPropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(), null, SomeClass.class.getType(), null, null, null, member, false).unwrappingWriter(nameTransformer);
 assertEquals(NameTransformer.class, writer.getClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriterWithInvalidType() throws JsonProcessingException {
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(SomeClass.class));
 BeanPropertyWriter writer = propertyBuilder.buildWriter(mapper.getSerializerProvider(), null, new TypeReference<>() {}.getType(), null, null, null, null, false);
 fail("Expected JsonMappingException to be thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindSerializationTypeWithIncompatibleType() throws JsonProcessingException {
 SomeClass someClass = new SomeClass();
 AnnotatedMember member = ReflectionTestUtils.findField(SomeClass.class, "name");
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(SomeClass.class));
 try {
 propertyBuilder.findSerializationType(member, false, SomeClass.class.getType());
 fail("Expected IllegalArgumentException to be thrown");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testGetClassAnnotations() throws JsonProcessingException {
 BeanDescription beanDescription = new BeanDescription(SomeClass.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);
 Annotations annotations = propertyBuilder.getClassAnnotations();
 assertNotNull(annotations);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetClassAnnotationsWithInvalidBeanDescription() throws JsonProcessingException {
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
 try {
 propertyBuilder.getClassAnnotations();
 fail("Expected JsonMappingException to be thrown");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testGetPropertyDefaultValue() throws JsonProcessingException {
 BeanDescription beanDescription = new BeanDescription(SomeClass.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);
 Object name = propertyBuilder.getPropertyDefaultValue("name", null, SomeClass.class);
 assertNull(name);
 }

 @Test
 public void testGetPropertyDefaultValueWithDefaultBean() throws JsonProcessingException {
 SomeClass defaultBean = new SomeClass();
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(SomeClass.class));
 propertyBuilder._defaultBean = defaultBean;
 Object name = propertyBuilder.getPropertyDefaultValue("name", null, SomeClass.class);
 assertEquals("", name);
 }

 @Test
 public void testGetPropertyDefaultValueWithEmptyList() throws JsonProcessingException {
 SomeClass defaultBean = new SomeClass();
 defaultBean.setList(new ArrayList<>());
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(SomeClass.class));
 propertyBuilder._defaultBean = defaultBean;
 Object list = propertyBuilder.getPropertyDefaultValue("list", null, SomeClass.class);
 assertNotNull(list);
 assertTrue(list instanceof ObjectNode);
 assertEquals(0, ((ObjectNode) list).size());
 }

 @Test(expected = JsonMappingException.class)
 public void testGetPropertyDefaultValueWithInvalidBeanDescription() throws JsonProcessingException {
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
 try {
 propertyBuilder.getPropertyDefaultValue("name", null, SomeClass.class);
 fail("Expected JsonMappingException to be thrown");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testGetPropertyDefaultValueWithNonStaticAccess() throws JsonProcessingException {
 SomeClass defaultBean = new SomeClass();
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(SomeClass.class));
 try {
 propertyBuilder.getPropertyDefaultValue("name", null, SomeClass.class);
 fail("Expected IllegalStateException to be thrown");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testGetDefaultBean() throws JsonProcessingException {
 BeanDescription beanDescription = new BeanDescription(SomeClass.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);
 Object defaultBean = propertyBuilder.getDefaultBean();
 assertNotNull(defaultBean);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetDefaultBeanWithInvalidBeanDescription() throws JsonProcessingException {
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
 try {
 propertyBuilder.getDefaultBean();
 fail("Expected JsonMappingException to be thrown");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testgetDefaultInclusion() throws JsonProcessingException {
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(SomeClass.class));
 JsonInclude.Value defaultInclusion = propertyBuilder.getDefaultInclusion();
 assertEquals(JsonInclude.Include.NON_DEFAULT, defaultInclusion.getSerializationInclusion());
 }

 @Test
 public void testUseRealPropertyDefaults() throws JsonProcessingException {
 SomeClass someClass = new SomeClass();
 BeanDescription beanDescription = new BeanDescription(SomeClass.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);
 propertyBuilder._useRealPropertyDefaults = true;
 Object name = propertyBuilder.getPropertyDefaultValue("name", null, SomeClass.class);
 assertEquals("test", name);
 }

 @Test
 public void testUseRealPropertyDefaultsWithDefaultBean() throws JsonProcessingException {
 SomeClass defaultBean = new SomeClass();
 BeanDescription beanDescription = new BeanDescription(SomeClass.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);
 propertyBuilder._useRealPropertyDefaults = true;
 propertyBuilder._defaultBean = defaultBean;
 Object name = propertyBuilder.getPropertyDefaultValue("name", null, SomeClass.class);
 assertEquals("", name);
 }

 @Test
 public void testUseRealPropertyDefaultsWithEmptyList() throws JsonProcessingException {
 SomeClass defaultBean = new SomeClass();
 defaultBean.setList(new ArrayList<>());
 BeanDescription beanDescription = new BeanDescription(SomeClass.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);
 propertyBuilder._useRealPropertyDefaults = true;
 propertyBuilder._defaultBean = defaultBean;
 Object list = propertyBuilder.getPropertyDefaultValue("list", null, SomeClass.class);
 assertEquals(0, ((ObjectNode) list).size());
 }

 @Test(expected = JsonMappingException.class)
 public void testUseRealPropertyDefaultsWithInvalidBeanDescription() throws JsonProcessingException {
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
 try {
 propertyBuilder.getPropertyDefaultValue("name", null, SomeClass.class);
 fail("Expected JsonMappingException to be thrown");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testSerialize() throws JsonProcessingException {
 SomeClass someClass = new SomeClass();
 someClass.setName("test");
 ObjectWriter writer = mapper.writerFor(SomeClass.class).withDefaultPrettyPrinter();
 rootNode = writer.writeValueAsTree(someClass);
 assertTrue(rootNode.has("name"));
 assertEquals("test", rootNode.get("name").asText());
 }

 @Test
 public void testSerializeWithAnnotationSerializationType() throws JsonProcessingException {
 SomeClass someClass = new SomeClass();
 someClass.setName("test");
 ObjectWriter writer = mapper.writerFor(SomeClass.class.getType());
 rootNode = writer.writeValueAsTree(someClass);
 assertTrue(rootNode.has("name"));
 assertEquals("test", rootNode.get("name").asText());
 }

 @Test
 public void testSerializeWithContentTypeSerializer() throws JsonProcessingException {
 SomeClass someClass = new SomeClass();
 someClass.setList(new ArrayList<>());
 ObjectWriter writer = mapper.writerFor(SomeClass.class);
 rootNode = writer.writeValueAsTree(someClass);
 assertTrue(rootNode.has("list"));
 assertTrue(rootNode.get("list").isArray());
 }

 @Test
 public void testSerializeWithNullSerializer() throws JsonProcessingException {
 SomeClass someClass = new SomeClass();
 someClass.setName(null);
 ObjectWriter writer = mapper.writerFor(SomeClass.class);
 rootNode = writer.writeValueAsTree(someClass);
 assertTrue(rootNode.has("name"));
 assertNull(rootNode.get("name"));
 }

 @Test
 public void testSerializeWithUnwrappingNameTransformer() throws JsonProcessingException {
 SomeClass someClass = new SomeClass();
 someClass.setNestedObject(new NestedObject());
 ObjectWriter writer = mapper.writerFor(SomeClass.class).withDefaultPrettyPrinter();
 rootNode = writer.writeValueAsTree(someClass);
 assertEquals("NAMED_OBJECT", rootNode.get("nestedObject").getName());
 }

 @Test
 public void testSerializeWithEmptyList() throws JsonProcessingException {
 SomeClass someClass = new SomeClass();
 someClass.setList(new ArrayList<>());
 ObjectWriter writer = mapper.writerFor(SomeClass.class).withDefaultPrettyPrinter();
 rootNode = writer.writeValueAsTree(someClass);
 assertTrue(rootNode.has("list"));
 assertTrue(rootNode.get("list").isArray());
 assertTrue(rootNode.get("list").isEmpty());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNullValue() throws JsonProcessingException {
 SomeClass someClass = new SomeClass();
 ObjectWriter writer = mapper.writerFor(SomeClass.class);
 rootNode = writer.writeValueAsTree(someClass);
 fail("Expected JsonMappingException to be thrown");
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeWithInvalidType() throws JsonProcessingException {
 ObjectWriter writer = mapper.writerFor(new TypeReference<>() {}.getType());
 rootNode = writer.writeValueAsTree(new Object());
 fail("Expected JsonMappingException to be thrown");
 }

 @Test
 public void testDeserialize() throws JsonProcessingException {
 rootNode = mapper.readTree("{\"name\":\"test\"}");
 SomeClass someClass = mapper.readValue(rootNode, SomeClass.class);
 assertEquals("test", someClass.getName());
 }

 @Test
 public void testDeserializeWithAnnotationSerializationType() throws JsonProcessingException {
 rootNode = mapper.readTree("{\"name\":\"test\"}");
 SomeClass someClass = mapper.readValue(rootNode, SomeClass.class.getType());
 assertEquals("test", someClass.getName());
 }

 @Test
 public void testDeserializeWithContentTypeSerializer() throws JsonProcessingException {
 List list = new ArrayList<>();
 ObjectNode rootNode = mapper.readTree("{\"list\":[]}");
 SomeClass someClass = mapper.readValue(rootNode, SomeClass.class);
 assertEquals(list, someClass.getList());
 }

 @Test
 public void testDeserializeWithNullSerializer() throws JsonProcessingException {
 ObjectNode rootNode = mapper.readTree("{\"name\":null}");
 SomeClass someClass = mapper.readValue(rootNode, SomeClass.class);
 assertNull(someClass.getName());
 }

 @Test
 public void testDeserializeWithUnwrappingNameTransformer() throws JsonProcessingException {
 ObjectNode rootNode = mapper.readTree("{\"NAMED_OBJECT\":{}}");
 SomeClass someClass = mapper.readValue(rootNode, SomeClass.class);
 assertNotNull(someClass.getNestedObject());
 }

 @Test
 public void testDeserializeWithEmptyList() throws JsonProcessingException {
 ObjectNode rootNode = mapper.readTree("{\"list\":[]}");
 SomeClass someClass = mapper.readValue(rootNode, SomeClass.class);
 assertTrue(someClass.getList().isEmpty());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNullValue() throws JsonProcessingException {
 ObjectNode rootNode = mapper.readTree("{null}");
 SomeClass someClass = mapper.readValue(rootNode, SomeClass.class);
 fail("Expected JsonMappingException to be thrown");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithInvalidType() throws JsonProcessingException {
 SomeClass someClass = mapper.readValue(mapper.readTree("{\"name\":1}"), SomeClass.class);
 fail("Expected JsonMappingException to be thrown");
 }

 @Test
 public void testDeserializeWithEmptyName() throws JsonProcessingException {
 ObjectNode rootNode = mapper.readTree("{}");
 SomeClass someClass = mapper.readValue(rootNode, SomeClass.class);
 assertNull(someClass.getName());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithMissingName() throws JsonProcessingException {
 ObjectNode rootNode = mapper.readTree("{\"list\":[]}");
 SomeClass someClass = mapper.readValue(rootNode, SomeClass.class);
 fail("Expected JsonMappingException to be thrown");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithMissingList() throws JsonProcessingException {
 ObjectNode rootNode = mapper.readTree("{\"name\":\"test\"}");
 SomeClass someClass = mapper.readValue(rootNode, SomeClass.class);
 fail("Expected JsonMappingException to be thrown");
 }

}