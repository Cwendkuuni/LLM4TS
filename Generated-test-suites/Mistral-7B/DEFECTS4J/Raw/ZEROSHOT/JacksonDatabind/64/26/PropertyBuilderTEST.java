package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 @BeforeClass
 public static void setUpBeforeClass() {
 // Set up any static resources or initialization here
 }

 @AfterClass
 public static void tearDownAfterClass() {
 // Perform any tear down or clean up operations here
 }

 @Before
 public void setUp() {
 // Initialize any test-specific resources or initialization here
 }

 @After
 public void tearDown() {
 // Perform any tear down or cleanup operations here
 }

 @Test
 public void testPropertyBuilder() {
 PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
 assertNull(propertyBuilder._defaultBean);
 assertNull(propertyBuilder._defaultInclusion);
 assertFalse(propertyBuilder._useRealPropertyDefaults);

 SerializationConfig config = new SerializationConfig(new AnnotationIntrospector());
 BeanDescription beanDesc = new BeanDescription(null, null, null, new AnnotatedClass(MyPojo.class));
 propertyBuilder = new PropertyBuilder(config, beanDesc);

 assertEquals(JsonInclude.Value.empty(), propertyBuilder._defaultInclusion);
 assertTrue(propertyBuilder._useRealPropertyDefaults);
 }

 @Test
 public void testBuildWriter() throws JsonMappingException {
 MyPojo pojo = new MyPojo();
 pojo.setStringProperty("test");
 pojo.setIntProperty(1);
 pojo.setListProperty(Arrays.asList(1, 2, 3));
 pojo.setMapProperty(new HashMap<>());
 pojo.setNestedPojo(new NestedPojo());

 SerializationConfig config = new SerializationConfig(new AnnotationIntrospector());
 BeanDescription beanDesc = new BeanDescription(null, null, null, new AnnotatedClass(MyPojo.class));
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 ObjectMapper objectMapper = new ObjectMapper(config);

 BeanSerializerFactory factory = new BeanSerializerFactory(propertyBuilder, beanDesc, null);
 BeanSerializer serializer = factory.createBeanSerializer(MyPojo.class, null);

 ObjectWriter writer = objectMapper.writerForTypeValue(MyPojo.class);
 String json = writer.writeValueAsString(pojo);

 assertEquals("{\"stringProperty\":\"test\",\"intProperty\":1,\"listProperty\":[1,2,3],\"mapProperty\":{},\"nestedPojo\":{\"nestedStringProperty\":\"test\"}}", json);

 SerializerProvider provider = objectMapper.getSerializerProvider();
 AnnotatedMember am = new TestAnnotatedMember("stringProperty", MyPojo.class, null);
 BeanPropertyWriter writer1 = propertyBuilder.buildWriter(provider, null, MyPojo.class, null, null, null, am, false);
 assertTrue(writer1.hasEmptyValueHandler());

 AnnotatedMember am2 = new TestAnnotatedMember("intProperty", MyPojo.class, null);
 BeanPropertyWriter writer2 = propertyBuilder.buildWriter(provider, null, MyPojo.class, null, null, null, am2, false);
 assertFalse(writer2.hasEmptyValueHandler());

 AnnotatedMember am3 = new TestAnnotatedMember("listProperty", MyPojo.class, null);
 BeanPropertyWriter writer3 = propertyBuilder.buildWriter(provider, null, MyPojo.class, null, null, null, am3, false);
 assertFalse(writer3.hasEmptyValueHandler());

 AnnotatedMember am4 = new TestAnnotatedMember("mapProperty", MyPojo.class, null);
 BeanPropertyWriter writer4 = propertyBuilder.buildWriter(provider, null, MyPojo.class, null, null, null, am4, false);
 assertTrue(writer4.hasEmptyValueHandler());

 AnnotatedMember am5 = new TestAnnotatedMember("nestedPojo", MyPojo.class, null);
 BeanPropertyWriter writer5 = propertyBuilder.buildWriter(provider, null, MyPojo.class, null, null, null, am5, false);
 assertNotNull(writer5);
 }

 @Test
 public void testFindSerializationType() throws JsonMappingException {
 MyPojo pojo = new MyPojo();
 pojo.setStringProperty("test");
 pojo.setIntProperty(1);
 pojo.setListProperty(Arrays.asList(1, 2, 3));
 pojo.setMapProperty(new HashMap<>());
 pojo.setNestedPojo(new NestedPojo());

 SerializationConfig config = new SerializationConfig(new AnnotationIntrospector());
 BeanDescription beanDesc = new BeanDescription(null, null, null, new AnnotatedClass(MyPojo.class));
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 ObjectMapper objectMapper = new ObjectMapper(config);

 BeanSerializerFactory factory = new BeanSerializerFactory(propertyBuilder, beanDesc, null);
 BeanSerializer serializer = factory.createBeanSerializer(MyPojo.class, null);
 ObjectWriter writer = objectMapper.writerForTypeValue(MyPojo.class);

 AnnotatedMember am = new TestAnnotatedMember("stringProperty", MyPojo.class, null);
 JavaType javaType = propertyBuilder.findSerializationType(am, false, MyPojo.class);
 assertEquals(String.class, javaType.getRawClass());

 AnnotatedMember am2 = new TestAnnotatedMember("intProperty", MyPojo.class, null);
 JavaType javaType2 = propertyBuilder.findSerializationType(am2, false, MyPojo.class);
 assertEquals(Integer.class, javaType2.getRawClass());

 AnnotatedMember am3 = new TestAnnotatedMember("listProperty", MyPojo.class, null);
 JavaType javaType3 = propertyBuilder.findSerializationType(am3, false, MyPojo.class);
 assertEquals(List.class, javaType3.getRawClass());

 AnnotatedMember am4 = new TestAnnotatedMember("mapProperty", MyPojo.class, null);
 JavaType javaType4 = propertyBuilder.findSerializationType(am4, false, MyPojo.class);
 assertEquals(Map.class, javaType4.getRawClass());

 AnnotatedMember am5 = new TestAnnotatedMember("nestedPojo", MyPojo.class, null);
 JavaType javaType5 = propertyBuilder.findSerializationType(am5, false, MyPojo.class);
 assertEquals(NestedPojo.class, javaType5.getRawClass());

 AnnotatedMember am6 = new TestAnnotatedMember("nonexistentProperty", MyPojo.class, null);
 assertNull(propertyBuilder.findSerializationType(am6, false, MyPojo.class));
 }

 @Test
 public void testGetPropertyDefaultValue() {
 MyPojo pojo = new MyPojo();
 pojo.setStringProperty("test");
 pojo.setIntProperty(1);
 pojo.setListProperty(Arrays.asList(1, 2, 3));
 pojo.setMapProperty(new HashMap<>());
 pojo.setNestedPojo(new NestedPojo());

 PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
 Object defaultBean = propertyBuilder.getDefaultBean();
 assertNull(defaultBean);

 PropertyBuilder propertyBuilder2 = new PropertyBuilder(null, null);
 Object defaultBean2 = propertyBuilder2.getBeanDescription().instantiateBean(true);
 assertNotNull(defaultBean2);

 PropertyBuilder propertyBuilder3 = new PropertyBuilder(null, null);
 Object defaultValue = propertyBuilder3.getPropertyDefaultValue("stringProperty", null, String.class);
 assertEquals("", defaultValue);

 PropertyBuilder propertyBuilder4 = new PropertyBuilder(null, null);
 Object defaultValue2 = propertyBuilder4.getPropertyDefaultValue("intProperty", null, Integer.class);
 assertEquals(0, defaultValue2);

 PropertyBuilder propertyBuilder5 = new PropertyBuilder(null, null);
 Object defaultValue3 = propertyBuilder5.getPropertyDefaultValue("listProperty", null, List.class);
 assertNotNull(defaultValue3);
 assertFalse(defaultValue3 instanceof List);

 PropertyBuilder propertyBuilder6 = new PropertyBuilder(null, null);
 Object defaultValue4 = propertyBuilder6.getPropertyDefaultValue("mapProperty", null, Map.class);
 assertNotNull(defaultValue4);
 assertFalse(defaultValue4 instanceof Map);

 PropertyBuilder propertyBuilder7 = new PropertyBuilder(null, null);
 Object defaultValue5 = propertyBuilder7.getPropertyDefaultValue("nonexistentProperty", null, Object.class);
 assertNull(defaultValue5);
 }

 @Test
 public void testGetDefaultValue() {
 PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
 Object defaultValue = propertyBuilder.getDefaultValue(Object.class.getType());
 assertNull(defaultValue);

 PropertyBuilder propertyBuilder2 = new PropertyBuilder(null, null);
 Object defaultValue2 = propertyBuilder2.getDefaultValue(String.class.getType());
 assertEquals("", defaultValue2);

 PropertyBuilder propertyBuilder3 = new PropertyBuilder(null, null);
 Object defaultValue3 = propertyBuilder3.getDefaultValue(Integer.class.getType());
 assertEquals(0, defaultValue3);

 PropertyBuilder propertyBuilder4 = new PropertyBuilder(null, null);
 Object defaultValue4 = propertyBuilder4.getDefaultValue(List.class.getType());
 assertTrue(defaultValue4 instanceof Object);
 assertFalse(defaultValue4 instanceof List);

 PropertyBuilder propertyBuilder5 = new PropertyBuilder(null, null);
 Object defaultValue5 = propertyBuilder5.getDefaultValue(Map.class.getType());
 assertTrue(defaultValue5 instanceof Object);
 assertFalse(defaultValue5 instanceof Map);
 }

 static class MyPojo {
 private String stringProperty;
 private Integer intProperty;
 private List<Integer> listProperty;
 private Map<String, String> mapProperty;
 private NestedPojo nestedPojo;

 public String getStringProperty() {
 return stringProperty;
 }

 public void setStringProperty(String stringProperty) {
 this.stringProperty = stringProperty;
 }

 public Integer getIntProperty() {
 return intProperty;
 }

 public void setIntProperty(Integer intProperty) {
 this.intProperty = intProperty;
 }

 public List<Integer> getListProperty() {
 return listProperty;
 }

 public void setListProperty(List<Integer> listProperty) {
 this.listProperty = listProperty;
 }

 public Map<String, String> getMapProperty() {
 return mapProperty;
 }

 public void setMapProperty(Map<String, String> mapProperty) {
 this.mapProperty = mapProperty;
 }

 public NestedPojo getNestedPojo() {
 return nestedPojo;
 }

 public void setNestedPojo(NestedPojo nestedPojo) {
 this.nestedPojo = nestedPojo;
 }
 }

 static class NestedPojo {
 private String nestedStringProperty;

 public String getNestedStringProperty() {
 return nestedStringProperty;
 }

 public void setNestedStringProperty(String nestedStringProperty) {
 this.nestedStringProperty = nestedStringProperty;
 }
 }

 static class TestAnnotatedMember {
 private String name;
 private Class<?> clazz;
 private Annotation[] annotations;

 public TestAnnotatedMember(String name, Class<?> clazz, Annotation[] annotations) {
 this.name = name;
 this.clazz = clazz;
 this.annotations = annotations;
 }
 }
}