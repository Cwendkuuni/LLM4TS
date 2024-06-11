import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DatabindContextTest {

 private DatabindContext context;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 context = objectMapper.getDeserializationContext();
 }

 /**
 * Test method for getConfig()
 */
 @Test
 public void testGetConfig() {
 MapperConfig<?> config = context.getConfig();
 assertEquals(MapperConfig.class, config.getClass());
 }

 /**
 * Test method for getAnnotationIntrospector()
 */
 @Test
 public void testGetAnnotationIntrospector() {
 AnnotationIntrospector annotationIntrospector = context.getAnnotationIntrospector();
 assertEquals(AnnotationIntrospector.class, annotationIntrospector.getClass());
 }

 /**
 * Test method for isEnabled()
 */
 @Test
 public void testIsEnabled() {
 assertTrue(context.isEnabled(MapperFeature.DEFAULT_LOCALE_HANDLING));
 assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_GETTERS));
 }

 /**
 * Test method for canOverrideAccessModifiers()
 */
 @Test
 public void testCanOverrideAccessModifiers() {
 assertTrue(context.canOverrideAccessModifiers());
 }

 /**
 * Test method for getActiveView()
 */
 @Test
 public void testGetActiveView() {
 assertNull(context.getActiveView());
 }

 /**
 * Test method for getLocale()
 */
 @Test
 public void testGetLocale() {
 assertEquals(Locale.getDefault(), context.getLocale());
 }

 /**
 * Test method for getTimeZone()
 */
 @Test
 public void testGetTimeZone() {
 assertEquals(TimeZone.getDefault(), context.getTimeZone());
 }

 /**
 * Test method for getDefaultPropertyFormat()
 */
 @Test
 public void testGetDefaultPropertyFormat() throws JsonMappingException {
 JavaType javaType = context.getTypeFactory().constructType(String.class);
 JsonFormat.Value defaultPropertyFormat = context.getDefaultPropertyFormat(javaType);
 assertEquals(JsonFormat.Value.standardSerializable(), defaultPropertyFormat);
 }

 /**
 * Test method for getAttribute()
 */
 @Test
 public void testGetAttribute() {
 Object attribute = context.getAttribute("testKey");
 assertNull(attribute);
 }

 /**
 * Test method for setAttribute()
 */
 @Test
 public void testSetAttribute() {
 context.setAttribute("testKey", "testValue");
 Object attribute = context.getAttribute("testKey");
 assertEquals("testValue", attribute);
 }

 /**
 * Test method for constructType()
 */
 @Test
 public void testConstructType() {
 Type type = String[].class;
 JavaType javaType = context.constructType(type);
 assertEquals(javaType.getRawClass(), String[].class);
 }

 /**
 * Test method for constructSpecializedType()
 */
 @Test
 public void testConstructSpecializedType() {
 JavaType baseType = context.getTypeFactory().constructType(String.class);
 Class<?> subclass = String[].class;
 JavaType javaType = context.constructSpecializedType(baseType, subclass);
 assertEquals(javaType.getRawClass(), String[].class);
 }

 /**
 * Test method for resolveSubType()
 */
 @Test
 public void testResolveSubType() throws JsonMappingException {
 JavaType baseType = context.getTypeFactory().constructType(String.class);
 String subClass = "java.lang.String[]";
 JavaType javaType = context.resolveSubType(baseType, subClass);
 assertEquals(javaType.getRawClass(), String[].class);
 }

 /**
 * Test method for invalidTypeIdException()
 */
 @Test(expected = JsonMappingException.class)
 public void testInvalidTypeIdException() throws JsonMappingException {
 JavaType baseType = context.getTypeFactory().constructType(String.class);
 String typeId = "InvalidType";
 context.invalidTypeIdException(baseType, typeId, "Test");
 }

 /**
 * Test method for getTypeFactory()
 */
 @Test
 public void testGetTypeFactory() {
 TypeFactory typeFactory = context.getTypeFactory();
 assertEquals(TypeFactory.class, typeFactory.getClass());
 }

 /**
 * Test method for objectIdGeneratorInstance()
 */
 @Test
 public void testObjectIdGeneratorInstance() throws JsonMappingException {
 Annotated annotated = null;
 ObjectIdInfo objectIdInfo = null;
 ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
 assertNull(objectIdGenerator);
 }

 /**
 * Test method for objectIdResolverInstance()
 */
 @Test
 public void testObjectIdResolverInstance() {
 Annotated annotated = null;
 ObjectIdInfo objectIdInfo = null;
 ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
 assertNull(objectIdResolver);
 }

 /**
 * Test method for converterInstance()
 */
 @Test
 public void testConverterInstance() throws JsonMappingException {
 Annotated annotated = null;
 Object converterDef = String.class;
 Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
 assertNull(converter);
 }

 /**
 * Test method for reportBadDefinition()
 */
 @Test(expected = JsonMappingException.class)
 public void testReportBadDefinition() throws JsonMappingException {
 JavaType type = null;
 String msg = "Test Message";
 context.reportBadDefinition(type, msg);
 }
}