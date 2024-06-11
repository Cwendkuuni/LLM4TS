import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class DatabindContextTest {

 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testGetConfig() {
 DatabindContext databindContext = objectMapper.getDeserializationContext();
 MapperConfig<?> config = databindContext.getConfig();
 assertNotNull(config);
 }

 @Test
 public void testIsEnabled() {
 DatabindContext databindContext = objectMapper.getDeserializationContext();
 assertTrue(databindContext.isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS));
 assertFalse(databindContext.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
 }

 @Test
 public void testGetActiveView() {
 DatabindContext databindContext = objectMapper.getDeserializationContext();
 assertNull(databindContext.getActiveView());
 }

 @Test
 public void testGetLocale() {
 DatabindContext databindContext = objectMapper.getDeserializationContext();
 assertEquals(Locale.getDefault(), databindContext.getLocale());
 }

 @Test
 public void testGetTimeZone() {
 DatabindContext databindContext = objectMapper.getDeserializationContext();
 assertEquals(TimeZone.getDefault(), databindContext.getTimeZone());
 }

 @Test
 public void testgetDefaultPropertyFormat() {
 DatabindContext databindContext = objectMapper.getDeserializationContext();
 assertEquals(null, databindContext.getDefaultPropertyFormat(null));
 }

 @Test
 public void testGetAttribute() {
 DatabindContext databindContext = objectMapper.getDeserializationContext();
 databindContext.setAttribute("test", "testValue");
 assertEquals("testValue", databindContext.getAttribute("test"));
 }

 @Test
 public void testConstructType() throws JsonMappingException {
 DatabindContext databindContext = objectMapper.getDeserializationContext();
 TypeFactory typeFactory = databindContext.getTypeFactory();
 assertEquals(int.class, typeFactory.constructType(int.class));
 }

 @Test
 public void testResolveSubType() throws JsonMappingException {
 DatabindContext databindContext = objectMapper.getDeserializationContext();
 JavaType baseType = databindContext.getTypeFactory().constructType(String.class);
 JavaType subType = databindContext.resolveSubType(baseType, "java.lang.StringBuffer");
 assertEquals(StringBuffer.class, subType.getRawClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidTypeIdException() throws JsonMappingException {
 DatabindContext databindContext = objectMapper.getDeserializationContext();
 JavaType baseType = databindContext.getTypeFactory().constructType(String.class);
 databindContext.invalidTypeIdException(baseType, "NotAString");
 }

 @Test
 public void testReportBadDefinition() throws JsonMappingException {
 DatabindContext databindContext = objectMapper.getDeserializationContext();
 try {
 databindContext.reportBadDefinition(int.class, "Bad int definition");
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }
}