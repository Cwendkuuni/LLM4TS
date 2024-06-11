import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.MapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DatabindContextTest {

 private DatabindContext context;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 MapperFactory mapperFactory = new MapperFactory();
 objectMapper = mapperFactory.getObjectMapper();
 context = objectMapper.getSerializationConfig().getDefaultSerializationContext();
 }

 @Test
 public void testGetConfig() {
 MapperConfig<?> config = context.getConfig();
 assertEquals(MapperConfig.StandardConfig.class, config.getClass());
 }

 @Test
 public void testGetAnnotationIntrospector() {
 AnnotationIntrospector annotationIntrospector = context.getAnnotationIntrospector();
 assertNotNull(annotationIntrospector);
 }

 @Test
 public void testIsEnabled() {
 assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
 assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS.name() + "_ABC")); // invalid feature name
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
 assertEquals(Locale.getDefault(), context.getLocale());
 }

 @Test
 public void testGetTimeZone() {
 assertEquals(TimeZone.getDefault(), context.getTimeZone());
 }

 @Test
 public void testgetDefaultPropertyFormat() {
 Class<?> baseType = String.class;
 JsonFormat.Value format = context.getDefaultPropertyFormat(baseType);
 assertNotNull(format);
 }

 @Test
 public void testgetAttribute() {
 context.setAttribute("testKey", "testValue");
 Object value = context.getAttribute("testKey");
 assertEquals("testValue", value);
 }

 @Test
 public void testSetAttribute() {
 context.setAttribute("testKey", null);
 context.setAttribute("testKey", "testValue");
 Object value = context.getAttribute("testKey");
 assertEquals("testValue", value);
 }

 @Test
 public void testConstructType() throws JsonMappingException {
 JavaType javaType = context.constructType(String.class);
 assertEquals(String.class, javaType.getRawClass());
 }

 @Test
 public void testResolveSubType() throws JsonMappingException {
 JavaType baseType = context.getTypeFactory().constructType(String.class);
 JavaType subType = context.resolveSubType(baseType, "java.util.List");
 assertNotNull(subType);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidTypeIdException() throws JsonMappingException {
 JavaType baseType = context.getTypeFactory().constructType(String.class);
 JavaType subType = context.resolveSubType(baseType, "java.util.Map");
 context.invalidTypeIdException(baseType, "java.util.Map", "Unexpected type: java.util.Map");
 }

 @Test
 public void testTypeFactory() {
 TypeFactory typeFactory = context.getTypeFactory();
 assertEquals(TypeFactory.Default.class, typeFactory.getClass());
 }

 @Test
 public void testObjectIdGeneratorInstance() throws JsonMappingException {
 Annotated annotated = null;
 ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, null);
 assertNull(objectIdGenerator);
 }

 @Test
 public void testObjectIdResolverInstance() {
 Annotated annotated = null;
 ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, null);
 assertNull(objectIdResolver);
 }

 @Test
 public void testConverterInstance() throws JsonMappingException {
 Annotated annotated = null;
 Object converterDef = String.class; // a class
 Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
 assertNull(converter);
 }

 @Test
 public void testReportBadDefinition() throws JsonProcessingException {
 JavaType type = context.getTypeFactory().constructType(String.class);
 String msg = "Bad definition for type " + _desc(type.getClassName());
 // test with null method
 context.reportBadDefinition(type, null);
 // test with valid msg
 context.reportBadDefinition(type, msg);
 }

 @Test
 public void test_format() {
 String msg = "Test message: %s";
 String formatMsg = context._format(msg, "test value");
 assertEquals("Test message: test value", formatMsg);
 }

 @Test
 public void test_truncate() {
 String desc = "This is a very long test description for testing the _truncate method";
 String truncatedDesc = context._truncate(desc);
 assertTrue(truncatedDesc.length() < desc.length());
 }

 @Test
 public void test_quotedString() {
 String desc = "This is a test desc with \"quoted string\"";
 String quotedDesc = context._quotedString(desc);
 assertTrue(quotedDesc.length() > desc.length());
 }

 @Test
 public void test_colonConcat() {
 String msgBase = "Base msg: ";
 String extra = "extra test";
 String concatMsg = context._colonConcat(msgBase, extra);
 assertEquals("Base msg: extra test", concatMsg);
 }
}