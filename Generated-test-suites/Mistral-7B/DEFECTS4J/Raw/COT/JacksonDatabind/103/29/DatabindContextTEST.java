package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.type.*;
import com.fasterxml.jackson.databind.util.*;

import org.junit.*;

/**
 * Test class for DatabindContext.
 */
public class DatabindContextTest {

 private DatabindContext context;

 @Before
 public void setup() {
 MapperConfig<?> config = new MapperFactory().getMapper().getConfig();
 context = config.getRootContext();
 }

 @Test
 public void testGetConfig() {
 MapperConfig<?> expected = new MapperFactory().getMapper().getConfig();
 MapperConfig<?> actual = context.getConfig();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetAnnotationIntrospector() {
 AnnotationIntrospector expected = context.getConfig().getAnnotationIntrospector();
 AnnotationIntrospector actual = context.getAnnotationIntrospector();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsEnabled() {
 assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_TYPES));
 assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
 }

 @Test
 public void testCanOverrideAccessModifiers() {
 assertTrue(context.canOverrideAccessModifiers());
 }

 @Test
 public void testGetActiveView() {
 assertNull(context.getActiveView());
 }

 @Test
 public void testGetLocale() {
 assertNull(context.getLocale());
 }

 @Test
 public void testGetTimeZone() {
 assertNull(context.getTimeZone());
 }

 @Test
 public void testgetDefaultPropertyFormat() {
 assertNull(context.getDefaultPropertyFormat(Object.class));
 }

 @Test
 public void testGetAttribute() {
 Object attr = context.getAttribute("testKey");
 assertNull(attr);
 }

 @Test
 public void testSetAttribute() {
 context.setAttribute("testKey", "testValue");
 Object attr = context.getAttribute("testKey");
 assertEquals("testValue", attr);
 }

 @Test
 public void testConstructType() {
 Type type = Object.class;
 JavaType expected = context.getTypeFactory().constructType(type);
 JavaType actual = context.constructType(type);
 assertEquals(expected, actual);
 }

 @Test
 public void testConstructSpecializedType() {
 JavaType baseType = context.getTypeFactory().constructType(String.class);
 Class<?> subclass = StringBuffer.class;
 JavaType expected = context.getTypeFactory().constructSpecializedType(baseType, subclass);
 JavaType actual = context.constructSpecializedType(baseType, subclass);
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testResolveSubTypeWithInvalidTypeId() throws JsonMappingException {
 JavaType baseType = context.getTypeFactory().constructType(String.class);
 String invalidSubClass = "InvalidTypeId";
 JavaType actual = context.resolveSubType(baseType, invalidSubClass);
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidTypeIdException() throws JsonMappingException {
 JavaType baseType = context.getTypeFactory().constructType(String.class);
 String typeId = "InvalidTypeId";
 context.invalidTypeIdException(baseType, typeId, "InvalidTypeIdExceptionMessage");
 fail("Expected JsonMappingException");
 }

 @Test
 public void testGetTypeFactory() {
 TypeFactory expected = context.getTypeFactory();
 TypeFactory actual = context.getTypeFactory();
 assertEquals(expected, actual);
 }

 @Test
 public void testObjectIdGeneratorInstance() throws JsonMappingException {
 Annotated annotated = Mockito.mock(Annotated.class);
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerator.class, null, null);
 ObjectIdGenerator<?> expected = new ObjectIdGeneratorForInt() {
 @Override
 public long generateId(ObjectIdInfo info) {
 return 1L;
 }
 };
 ObjectIdGenerator<?> actual = context.objectIdGeneratorInstance(annotated, objectIdInfo);
 assertEquals(expected, actual);
 }

 @Test
 public void testObjectIdResolverInstance() throws JsonMappingException {
 Annotated annotated = Mockito.mock(Annotated.class);
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdResolver.class, null, null);
 ObjectIdResolver actual = context.objectIdResolverInstance(annotated, objectIdInfo);
 assertNotNull(actual);
 }

 @Test
 public void testConverterInstance() throws JsonMappingException {
 Annotated annotated = Mockito.mock(Annotated.class);
 Object converterDef = Converter.stringConverter(null);
 Converter<Object, Object> expected = new StringConverter();
 Converter<Object, Object> actual = context.converterInstance(annotated, converterDef);
 assertEquals(expected, actual);
 }

 @Test
 public void testReportBadDefinition() {
 JavaType type = TypeFactory.defaultInstance().constructType(String.class);
 String msg = "Bad definition for type %s";
 Object result = context.reportBadDefinition(type, msg);
 assertNull(result);
 }

 @Test
 public void testReportBadDefinitionWithClass() throws JsonMappingException {
 Class<?> clazz = String.class;
 String msg = "Bad definition for type %s";
 Object result = context.reportBadDefinition(clazz, msg);
 assertNull(result);
 }
}