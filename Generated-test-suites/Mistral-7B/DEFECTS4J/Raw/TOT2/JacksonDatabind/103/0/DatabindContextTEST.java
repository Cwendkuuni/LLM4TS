import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DatabindContextTest {

 private DatabindContext context;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 MapperConfig<?> config = new MapperFactoryModifier().configure(MapperFactory.StandardFinder.instance(), new AnnotationIntrospector());
 context = config.getDefaultContext();
 mapper = new ObjectMapper(config);
 }

 @Test
 public void testGetConfig() {
 MapperConfig<?> config = context.getConfig();
 assertEquals(config.getClass(), MapperConfig.StandardConfig.class);
 }

 @Test
 public void testIsEnabled_MapperFeature() {
 assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_GETTERS));
 assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_FIELDS));
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
 Class<String> stringClass = String.class;
 assertEquals(JsonFormat.Value.stdSerialDateFormat, context.getDefaultPropertyFormat(stringClass));
 }

 @Test
 public void testGetAttribute() {
 context.setAttribute("test", "test");
 assertEquals("test", context.getAttribute("test"));
 }

 @Test
 public void testConstructType() {
 JavaType javaType = context.getTypeFactory().constructType(int.class);
 assertNotNull(javaType);
 assertEquals(int.class, javaType.getRawClass());
 }

 @Test
 public void testResolveSubType() throws JsonMappingException {
 JavaType baseType = context.getTypeFactory().constructType(String.class);
 JavaType subType = context.resolveSubType(baseType, "java.util.ArrayList");
 assertNotNull(subType);
 assertEquals(java.util.ArrayList.class, subType.getRawClass());
 }

 @Test
 public void testInvalidTypeIdException() {
 JavaType baseType = context.getTypeFactory().constructType(String.class);
 assertThrows(JsonMappingException.class, () -> context.invalidTypeIdException(baseType, "ArrayList"));
 }

 @Test
 public void testTypeFactory() {
 TypeFactory typeFactory = context.getTypeFactory();
 JavaType javaType = typeFactory.constructType(int[].class);
 assertEquals(int[].class, javaType.getRawClass());
 }

 @Test
 public void testObjectIdGeneratorInstance() throws JsonMappingException {
 Annotated annotated = mock(Annotated.class);
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null, JsonAutoDetect.None.class, null);
 ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
 assertNotNull(objectIdGenerator);
 }

 @Test
 public void testObjectIdResolverInstance() {
 Annotated annotated = mock(Annotated.class);
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null, JsonAutoDetect.None.class, null);
 ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
 assertNotNull(objectIdResolver);
 }

 @Test
 public void testConverterInstance() throws JsonMappingException {
 Annotated annotated = mock(Annotated.class);
 Object converterDef = JsonStringSerializer.class;
 Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
 assertNotNull(converter);
 }

 @Test
 public void testReportBadDefinition() throws JsonMappingException {
 JavaType type = context.getTypeFactory().constructType(String.class);
 String msg = "Invalid definition for type %s".formatted(type.getRawClassName());
 assertThrows(JsonMappingException.class, () -> context.reportBadDefinition(type, msg));
 }

 @Test
 public void testReportBadDefinition_Class() throws JsonMappingException {
 Class<String> type = String.class;
 String msg = "Invalid definition for class %s".formatted(type.getName());
 assertThrows(JsonMappingException.class, () -> context.reportBadDefinition(type, msg));
 }

 @Test(expected = JsonMappingException.class)
 public void testReportBadDefinition_Null() throws JsonMappingException {
 JavaType type = null;
 context.reportBadDefinition(type, "Invalid definition for type %s".formatted(type.getRawClassName()));
 }

 @Test
 public void testFormat() {
 String msg = "[Message: %s] [Type: %s] [Location: %s]";
 String errorMessage = context._format(msg, "Test Message", "com.test.TestClass", "Test File:123");
 assertEquals("Message: Test Message\nType: com.test.TestClass\nLocation: Test File:123", errorMessage);
 }

 @Test
 public void testTruncate() {
 String desc = "This is a very long error message that needs to be truncated";
 String truncatedDesc = context._truncate(desc);
 assertTrue(truncatedDesc.startsWith("This is a very long error message that needs to be truncated]..."));
 }

 @Test
 public void testQuotedString() {
 String desc = "This is a string that needs to be quoted";
 String quotedDesc = context._quotedString(desc);
 assertTrue(quotedDesc.startsWith("\"This is a string that needs to be quoted\""));
 }

 @Test
 public void testColonConcat() {
 String msgBase = "Message: [%s]";
 String extra = "Type: java.util.ArrayList, Location: TestFile:123";
 String combinedMsg = context._colonConcat(msgBase, extra);
 assertEquals("Message: [java.util.ArrayList]\nType: java.util.ArrayList\nLocation: TestFile:123", combinedMsg);
 }

 @Test
 public void testDesc() {
 String desc = "This is a test description";
 String quotedDesc = context._desc(desc);
 assertTrue(quotedDesc.startsWith("\"This is a test description\""));
 }
}