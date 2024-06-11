package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class DatabindContextTest {

 private DatabindContext context;
 private MapperConfig<?> config;

 @Before
 public void setUp() {
 config = new MapperFactory().getMapper().getConfig();
 context = config.getInstance(DatabindContext.class);
 }

 @Test
 public void testGetConfig() {
 MapperConfig<?> expected = config;
 MapperConfig<?> actual = context.getConfig();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsEnabled() {
 assertTrue(context.isEnabled(MapperFeature.DEFAULT_PROPERTY_OVERRIDES));
 assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
 }

 @Test
 public void testGetAnnotationIntrospector() {
 AnnotationIntrospector expected = config.getAnnotationIntrospector();
 AnnotationIntrospector actual = context.getAnnotationIntrospector();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetActiveView() {
 assertNull(context.getActiveView());
 }

 @Test
 public void testGetLocale() {
 assertEquals(Locale.getDefault(), context.getLocale());
 }

 @Test
 public void testGetTimeZone() {
 assertEquals(TimeZone.getDefault(), context.getTimeZone());
 }

 @Test
 public void testGetDefaultPropertyFormat() {
 JavaType javaType = context.getTypeFactory().constructType(String.class);
 ObjectNode defaultPropertyFormat = context.getDefaultPropertyFormat(javaType).getDefaultPropertyFormat();
 assertTrue(defaultPropertyFormat.isContainerNode());
 assertTrue(defaultPropertyFormat.hasNonNull("parseDatePatterns"));
 assertTrue(defaultPropertyFormat.hasNonNull("serialDateFormat"));
 }

 @Test
 public void testConstructType() {
 Type type = Integer.TYPE;
 JavaType javaType = context.constructType(type);
 assertEquals(Integer.class, javaType.getRawClass());
 }

 @Test
 public void testConstructSpecializedType() {
 JavaType baseType = context.getTypeFactory().constructType(Number.class);
 JavaType subclassType = context.constructSpecializedType(baseType, Integer.class);
 assertEquals(Integer.class, subclassType.getRawClass());
 }

 @Test
 public void testResolveSubType() throws JsonMappingException {
 JavaType baseType = context.getTypeFactory().constructType(String.class);
 String subClass = "java.util.Locale";
 JavaType javaType = context.resolveSubType(baseType, subClass);
 assertEquals(Locale.class, javaType.getRawClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidTypeIdException() throws JsonMappingException {
 JavaType baseType = context.getTypeFactory().constructType(String.class);
 String typeId = "InvalidType";
 context.invalidTypeIdException(baseType, typeId, "");
 }

 @Test
 public void testGetTypeFactory() {
 TypeFactory expected = config.getTypeFactory();
 TypeFactory actual = context.getTypeFactory();
 assertEquals(expected, actual);
 }

 @Test
 public void testObjectIdGeneratorInstance() throws JsonMappingException {
 Annotated annotated = new NodeNode();
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Id.Class, Integer.class);
 ObjectIdGenerator<?> expected = new SimpleObjectIdGenerator();
 ObjectIdGenerator<?> actual = context.objectIdGeneratorInstance(annotated, objectIdInfo);
 assertEquals(expected.getClass(), actual.getClass());
 }

 @Test
 public void testObjectIdResolverInstance() {
 Annotated annotated = new NodeNode();
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(Id.Property, "id");
 ObjectIdResolver expected = new SimpleObjectIdResolver();
 ObjectIdResolver actual = context.objectIdResolverInstance(annotated, objectIdInfo);
 assertEquals(expected.getClass(), actual.getClass());
 }

 @Test
 public void testConverterInstance() throws JsonMappingException {
 Annotated annotated = new NodeNode();
 String converterClassName = String.class.getName();
 Converter<Object, Object> expected = new StringConverter();
 Converter<Object, Object> actual = context.converterInstance(annotated, converterClassName);
 assertEquals(expected.getClass(), actual.getClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testReportBadDefinition() throws JsonMappingException {
 JavaType javaType = context.getTypeFactory().constructType(String.class);
 context.reportBadDefinition(javaType, "Test Error Message");
 }
}