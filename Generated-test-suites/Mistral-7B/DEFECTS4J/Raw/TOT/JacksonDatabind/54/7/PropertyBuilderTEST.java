import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private SerializationConfig config;
 private ObjectNode node;
 private ObjectMapper mapper;
 private PropertyBuilder builder;

 @Before
 public void setup() throws JsonProcessingException {
 config = new SerializationConfig();
 mapper = new ObjectMapper(config);
 BeanDescription beanDesc = new BeanDescription(TestBean.class);
 builder = new PropertyBuilder(config, beanDesc);
 }

 @Test
 public void testBuildWriter_DefaultInclusion() throws JsonProcessingException {
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("name");
 SerializerProvider prov = mapper.getSerializerProvider();
 JavaType declaredType = mapper.getTypeFactory().constructType(TestBean.class);
 JsonSerializer<?> ser = mapper.findValueSerializer(TestBean.class, "name");
 TypeSerializer typeSer = null;
 TypeSerializer contentTypeSer = null;
 AnnotatedMember am = null;
 boolean defaultUseStaticTyping = false;
 BeanPropertyWriter bpw = builder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
 assertEquals("name", bpw.getMemberName());
 }

 @Test
 public void testBuildWriter_IncludeNonDefault() throws JsonProcessingException {
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("nonDefault");
 SerializerProvider prov = mapper.getSerializerProvider();
 JavaType declaredType = mapper.getTypeFactory().constructType(TestBean.class);
 JsonSerializer<?> ser = mapper.findValueSerializer(TestBean.class, "nonDefault");
 TypeSerializer typeSer = null;
 TypeSerializer contentTypeSer = null;
 AnnotatedMember am = null;
 boolean defaultUseStaticTyping = false;
 Object defaultBean = new TestBean();
 Object defaultValue = "defaultValue";
 builder._defaultBean = defaultBean;
 BeanPropertyWriter bpw = builder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
 assertEquals("nonDefault", bpw.getMemberName());
 assertNull(bpw.getValueHandler());

 TestBean testBean = new TestBean();
 testBean.setNonDefault("expectedValue");
 node = mapper.readValue(mapper.writeValueAsString(testBean), ObjectNode.class);
 Object actualValue = node.path("nonDefault").textValue();
 assertEquals("expectedValue", actualValue);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildWriter_EmptyContentType() throws JsonProcessingException {
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("array");
 SerializerProvider prov = mapper.getSerializerProvider();
 JavaType declaredType = mapper.getTypeFactory().constructType(TestBean[].class);
 JsonSerializer<?> ser = mapper.findValueSerializer(TestBean[].class, "array");
 TypeSerializer typeSer = null;
 TypeSerializer contentTypeSer = TypeSerializer.emptyArraySerializer(mapper.getTypeFactory().constructType(TestBean.class));
 AnnotatedMember am = null;
 boolean defaultUseStaticTyping = false;
 BeanPropertyWriter bpw = builder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
 fail("Should have thrown IllegalStateException");
 }

 @Test
 public void testBuildWriter_NullSerializer() throws JsonProcessingException {
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("nullSerializer");
 SerializerProvider prov = mapper.getSerializerProvider();
 JavaType declaredType = mapper.getTypeFactory().constructType(TestBean.class);
 JsonSerializer<?> ser = null;
 TypeSerializer typeSer = null;
 TypeSerializer contentTypeSer = null;
 AnnotatedMember am = null;
 boolean defaultUseStaticTyping = false;
 BeanPropertyWriter bpw = builder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
 assertNull(bpw.getNullSerializer());
 }

 @Test
 public void testBuildWriter_Unwrapping() throws JsonProcessingException {
 BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("unwrapping");
 SerializerProvider prov = mapper.getSerializerProvider();
 JavaType declaredType = mapper.getTypeFactory().constructType(TestBean.class);
 JsonSerializer<?> ser = mapper.findValueSerializer(TestBean.class, "unwrapping");
 TypeSerializer typeSer = null;
 TypeSerializer contentTypeSer = null;
 AnnotatedMember am = null;
 boolean defaultUseStaticTyping = false;
 NameTransformer unwrapper = new NameTransformer() {
 @Override public String transform(String name, JsonSerializer.SerializerProvider provider) {
 return "transformedName";
 }};
 BeanPropertyWriter bpw = builder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
 assertEquals("unwrapping", bpw.getMemberName());
 assertEquals("transformedName", bpw.getUnwrappedName());
 }

 @Test
 public void testFindSerializationType_StaticTyping() throws JsonProcessingException {
 JavaType declaredType = mapper.getTypeFactory().constructType(TestBean.class);
 Annotated am = null;
 boolean useStaticTyping = true;
 JavaType serializationType = builder.findSerializationType(am, useStaticTyping, declaredType);
 assertNotNull(serializationType);
 assertEquals(TestBean.class, serializationType.getRawClass());
 }

 @Test
 public void testFindSerializationType_Default() throws JsonProcessingException {
 JavaType declaredType = mapper.getTypeFactory().constructType(TestBean.class);
 Annotated am = null;
 boolean useStaticTyping = false;
 JavaType serializationType = builder.findSerializationType(am, useStaticTyping, declaredType);
 assertNull(serializationType);
 }

 @Test
 public void testGetClassAnnotations() throws JsonProcessingException {
 assertEquals(1, builder.getClassAnnotations().size());
 }

 @Test
 public void testGetPropertyDefaultValue() throws JsonProcessingException {
 assertEquals("", builder.getPropertyDefaultValue("classAnnotations", null, mapper.getTypeFactory().constructType(TestBean.class)));
 }

 @Test
 public void testGetPropertyDefaultValue_ClassWithDefaultConstructor() throws JsonProcessingException {
 TestBeanWithDefaultConstructor testBean = new TestBeanWithDefaultConstructor();
 builder._defaultBean = testBean;
 assertEquals("defaultValue", builder.getPropertyDefaultValue("classAnnotationsWithDefaultConstructor", null, mapper.getTypeFactory().constructType(TestBeanWithDefaultConstructor.class)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetPropertyDefaultValue_NoDefaultConstructor() throws JsonProcessingException {
 TestBeanWithoutDefaultConstructor testBean = new TestBeanWithoutDefaultConstructor();
 builder._defaultBean = testBean;
 builder.getPropertyDefaultValue("classAnnotationsWithoutDefaultConstructor", null, mapper.getTypeFactory().constructType(TestBeanWithoutDefaultConstructor.class));
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testGetPropertyDefaultValue_PrimitiveTypes() throws JsonProcessingException {
 assertEquals(0, builder.getPropertyDefaultValue("intProperty", null, mapper.getTypeFactory().constructType(int.class)));
 }

 @Test
 public void testGetPropertyDefaultValue_String() throws JsonProcessingException {
 assertEquals("", builder.getPropertyDefaultValue("stringProperty", null, mapper.getTypeFactory().constructType(String.class)));
 }

 @Test
 public void testGetPropertyDefaultValue_Array() throws JsonProcessingException {
 JavaType arrayType = mapper.getTypeFactory().constructType(int[].class);
 assertEquals(new int[0], builder.getPropertyDefaultValue("intArrayProperty", null, arrayType));
 }

 @Test
 public void testGetPropertyDefaultValue_Collection() throws JsonProcessingException {
 JavaType collectionType = mapper.getTypeFactory().constructType(ArrayList.class);
 assertEquals(Collections.emptyList(), builder.getPropertyDefaultValue("listProperty", null, collectionType));
 }

 @Test
 public void testGetPropertyDefaultValue_ReferenceType() throws JsonProcessingException {
 JavaType referenceType = mapper.getTypeFactory().constructType(TestReferenceType.class);
 assertNull(builder.getPropertyDefaultValue("referenceProperty", null, referenceType));
 }

}