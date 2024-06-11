import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private SerializationConfig config;
 private BeanDescription beanDesc;
 private PropertyBuilder propertyBuilder;

 @Before
 public void setUp() throws JsonMappingException {
 config = new SerializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 BeanFactory beanFactory = mapper.getFactory();
 SimpleModule module = new SimpleModule();
 module.addSerializer(String.class, new StringSerializer());
 mapper.registerModule(module);
 beanDesc = beanFactory.constructDescription(String[].class);
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 }

 @Test
 public void testBuildWriter_NullSer() {
 assertNull(propertyBuilder.buildWriter(null, null, JavaType.valueOf("[java.lang.String]"), null, null, null, null, false));
 }

 @Test
 public void testBuildWriter_NullDeclaredType() {
 assertNull(propertyBuilder.buildWriter(null, null, null, null, null, null, null, false));
 }

 @Test
 public void testBuildWriter_NullTypeSer() {
 JavaType declaredType = JavaType.valueOf("[java.lang.String]");
 SerializerProvider prov = null;
 BeanPropertyDefinition propDef = null;
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, null, null, null, null, false);
 assertNotNull(bpw);
 }

 @Test
 public void testBuildWriter_NullContentTypeSer() {
 JavaType declaredType = JavaType.valueOf("[java.lang.String]");
 SerializerProvider prov = new SerializerProvider();
 BeanPropertyDefinition propDef = null;
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, null, null, null, null, false);
 assertNotNull(bpw);
 }

 @Test
 public void testBuildWriter_EmptyDeclaredType() {
 JavaType declaredType = JavaType.valueOf("[]");
 SerializerProvider prov = new SerializerProvider();
 BeanPropertyDefinition propDef = null;
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, null, null, null, null, false);
 assertNotNull(bpw);
 }

 @Test
 public void testBuildWriter_NullAmd() {
 JavaType declaredType = JavaType.valueOf("[java.lang.String]");
 SerializerProvider prov = new SerializerProvider();
 BeanPropertyDefinition propDef = null;
 JavaType serializationType = null;
 JsonSerializer<?> ser = null;
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, serializationType, null, null, false);
 assertNull(bpw);
 }

 @Test
 public void testBuildWriter_EmptyNameTransformer() {
 JavaType declaredType = JavaType.valueOf("[java.lang.String]");
 SerializerProvider prov = new SerializerProvider();
 BeanPropertyDefinition propDef = null;
 JavaType serializationType = null;
 JsonSerializer<?> ser = null;
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, serializationType, null, null, false);
 assertNotNull(bpw);
 }

 @Test
 public void testBuildWriter_GenericType() throws JsonProcessingException {
 JavaType declaredType = JavaType.constructType(String[].class);
 SerializerProvider prov = new SerializerProvider();
 BeanPropertyDefinition propDef = new BeanPropertyDefinition(BeanPropertyDefinition.Kind.FIELD, "array", null, null, null, null, null, null);
 JavaType serializationType = JavaType.constructType(String[].class);
 JsonSerializer<?> ser = new StringSerializer();
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, serializationType, null, null, false);
 ObjectWriter writer = prov.getWriter();
 String json = writer.writeValueAsString(new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).writeValueAs(bpw));
 assertEquals("[ ]", json);
 }

 @Test
 public void testBuildWriter_StringType() throws JsonProcessingException {
 JavaType declaredType = JavaType.valueOf(String.class);
 SerializerProvider prov = new SerializerProvider();
 BeanPropertyDefinition propDef = new BeanPropertyDefinition(BeanPropertyDefinition.Kind.FIELD, "stringProp", null, null, null, null, null, null);
 JavaType serializationType = JavaType.valueOf(String.class);
 JsonSerializer<?> ser = new StringSerializer();
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, serializationType, null, null, false);
 ObjectWriter writer = prov.getWriter();
 String json = writer.writeValueAsString(new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).writeValueAs(bpw));
 assertEquals("\"\"", json);
 }

 @Test
 public void testFindSerializationType_NullAmd() throws JsonMappingException {
 assertNull(propertyBuilder.findSerializationType(null, false, null));
 }

 @Test
 public void testFindSerializationType_NullDeclaredType() throws JsonMappingException {
 assertNull(propertyBuilder.findSerializationType(null, false, null));
 }

 @Test
 public void testFindSerializationType_JsonIncludeAnnotation() throws JsonMappingException {
 Class<?> clazz = String[].class;
 AnnotatedMember am = null;
 JavaType declaredType = JavaType.valueOf(clazz);
 JsonInclude jsonIncludeAnnotation = new JsonInclude.Include(JsonInclude.Include.NON_DEFAULT);
 am = new SimpleAnnotatedMember(clazz.getDeclaredField("myField"), new Annotation[] { jsonIncludeAnnotation });
 JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
 assertEquals(String.class, serializationType.getRawClass());
 }

 @Test
 public void testFindSerializationType_JsonSerializeAnnotation() throws JsonMappingException {
 Class<?> clazz = String[].class;
 AnnotatedMember am = null;
 JavaType declaredType = JavaType.valueOf(clazz);
 JsonSerialize jsonSerializeAnnotation = new JsonSerialize.Typing(JsonSerialize.Typing.STATIC);
 am = new SimpleAnnotatedMember(clazz.getDeclaredField("myField"), new Annotation[] { jsonSerializeAnnotation });
 JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
 assertEquals(clazz, serializationType.getRawClass());
 }

 @Test
 public void testFindSerializationType_NoAnnotations() throws JsonMappingException {
 Class<?> clazz = String[].class;
 AnnotatedMember am = null;
 JavaType declaredType = JavaType.valueOf(clazz);
 JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
 assertNull(serializationType);
 }

 @Test
 public void testGetClassAnnotations_NullBeanDesc() {
 assertNull(propertyBuilder.getClassAnnotations());
 }

 @Test
 public void testGetClassAnnotations_EmptyBeanDesc() {
 BeanDescription beanDesc = new BeanDescription();
 assertEquals(0, propertyBuilder.getClassAnnotations(beanDesc).size());
 }

 @Test
 public void testGetClassAnnotations_BeanDescWithAnnotations() {
 BeanDescription beanDesc = new BeanDescription();
 SimpleAnnotationMemberMemberProperty ann1 = new SimpleAnnotationMemberMemberProperty("ann1", JsonInclude.class, new JsonInclude(Include.NON_EMPTY));
 SimpleAnnotationMemberMemberProperty ann2 = new SimpleAnnotationMemberMemberProperty("ann2", JsonSerialize.class, new JsonSerialize. typing(JsonSerialize.Typing.STATIC));
 beanDesc.addAnnotation(ann1);
 beanDesc.addAnnotation(ann2);
 assertEquals(2, propertyBuilder.getClassAnnotations(beanDesc).size());
 }

 @Test
 public void testGetPropertyDefaultValue_NullName() {
 assertNull(propertyBuilder.getPropertyDefaultValue(null, null, JavaType.valueOf("[java.lang.String]")));
 }

 @Test
 public void testGetPropertyDefaultValue_EmptyName() {
 assertNull(propertyBuilder.getPropertyDefaultValue("", null, JavaType.valueOf("[java.lang.String]")));
 }

 @Test
 public void testGetPropertyDefaultValue_NonDefaultBean() throws JsonMappingException {
 Object defaultBean = new ObjectMapper().readValue("[\"foo\", \"bar\"]", String[].class);
 assertEquals("[\"foo\", \"bar\"]", propertyBuilder.getPropertyDefaultValue("", null, JavaType.valueOf("[java.lang.String]")).toString());
 }

 @Test
 public void testGetPropertyDefaultValue_NullMember() {
 assertNull(propertyBuilder.getPropertyDefaultValue(null, null, JavaType.valueOf("[java.lang.String]")));
 }

 @Test
 public void testGetPropertyDefaultValue_StringType() throws JsonMappingException {
 String defaultBean = "defaultValue";
 assertEquals("defaultValue", propertyBuilder.getPropertyDefaultValue("myField", null, JavaType.valueOf(String.class)));
 }

 @Test
 public void testGetPropertyDefaultValue_ContainerType() throws JsonMappingException {
 Object defaultBean = new ObjectMapper().readValue("[\"foo\", \"bar\"]", String[].class);
 assertEquals("[\"foo\", \"bar\"]", propertyBuilder.getPropertyDefaultValue("", null, JavaType.valueOf("[java.lang.String]")).toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetPropertyDefaultValue_NoDefaultValue() throws JsonMappingException {
 Object defaultBean = null;
 assertEquals("", propertyBuilder.getPropertyDefaultValue("myField", null, JavaType.valueOf(String.class)));
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testGetPropertyDefaultValue_NonDefaultType() throws JsonMappingException {
 Object defaultBean = new Object();
 assertNull(propertyBuilder.getPropertyDefaultValue("myField", null, JavaType.valueOf(Object.class)));
 }

 @Test
 public void testGetPropertyDefaultValue_EmptyType() throws JsonMappingException {
 assertNull(propertyBuilder.getPropertyDefaultValue("myField", null, JavaType.valueOf("")));
 }

 @Test
 public void testGetPropertyDefaultValue_NullType() {
 assertNull(propertyBuilder.getPropertyDefaultValue("myField", null, null));
 }

 @Test
 public void testDefaultBean_NullBeanDesc() {
 assertNull(propertyBuilder.getDefaultBean());
 }

 @Test
 public void testDefaultBean_EmptyBeanDesc() {
 assertNull(propertyBuilder.getDefaultBean());
 }

 @Test
 public void testDefaultBean_BeanDescWithDefaultInstance() throws JsonMappingException {
 Object defaultBean = new ObjectMapper().readValue("[\"foo\", \"bar\"]", String[].class);
 assertEquals(defaultBean, propertyBuilder.getDefaultBean());
 }

 @Test
 public void testDefaultBean_BeanDescWithoutDefaultInstance() throws JsonMappingException {
 Object defaultBean = null;
 assertEquals(defaultBean, propertyBuilder.getDefaultBean());
 }

 @Test
 public void testGetDefaultValue_NullType() {
 assertNull(propertyBuilder.getDefaultValue(null));
 }

 @Test
 public void testGetDefaultValue_PrimitiveType() {
 assertEquals(0, propertyBuilder.getDefaultValue(int.class).intValue());
 }

 @Test
 public void testGetDefaultValue_WrapperType() {
 assertEquals(Integer.valueOf(0), propertyBuilder.getDefaultValue(Integer.class));
 }

 @Test
 public void testGetDefaultValue_StringType() {
 assertEquals("", propertyBuilder.getDefaultValue(String.class));
 }

 @Test
 public void testGetDefaultValue_ArrayType() {
 assertEquals(new String[0], propertyBuilder.getDefaultValue(String[].class));
 }

 @Test
 public void testGetDefaultValue_ContainerType() {
 assertEquals(new ArrayList<>(), propertyBuilder.getDefaultValue(ArrayList.class));
 }

 @Test
 public void testGetDefaultValue_ReferenceType() {
 assertNull(propertyBuilder.getDefaultValue(StringBuilder.class));
 }
}