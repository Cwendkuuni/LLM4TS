import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

 private static ObjectMapper mapper = new JsonMapper();

 @Test
 public void testBooleanType() throws IOException {
 StdKeyDeserializer booleanKD = StdKeyDeserializer.forType(Boolean.class);
 String key = "true";
 Object result = booleanKD.deserializeKey(key, new TestContext());
 assertEquals(Boolean.TRUE, result);
 key = "false";
 result = booleanKD.deserializeKey(key, new TestContext());
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testByteType() throws IOException {
 StdKeyDeserializer byteKD = StdKeyDeserializer.forType(Byte.class);
 String key = "10";
 Object result = byteKD.deserializeKey(key, new TestContext());
 assertEquals((byte) 10, result);
 key = "256";
 result = byteKD.deserializeKey(key, new TestContext());
 assertNull(result);
 }

 @Test
 public void testShortType() throws IOException {
 StdKeyDeserializer shortKD = StdKeyDeserializer.forType(Short.class);
 String key = "100";
 Object result = shortKD.deserializeKey(key, new TestContext());
 assertEquals((short) 100, result);
 key = "32768";
 result = shortKD.deserializeKey(key, new TestContext());
 assertNull(result);
 }

 @Test
 public void testCharType() throws IOException {
 StdKeyDeserializer charKD = StdKeyDeserializer.forType(Character.class);
 String key = "\u0041";
 Object result = charKD.deserializeKey(key, new TestContext());
 assertEquals('A', result);
 key = "invalid";
 result = charKD.deserializeKey(key, new TestContext());
 assertNull(result);
 }

 @Test
 public void testIntType() throws IOException {
 StdKeyDeserializer intKD = StdKeyDeserializer.forType(Integer.class);
 String key = "100";
 Object result = intKD.deserializeKey(key, new TestContext());
 assertEquals(100, result);
 }

 @Test
 public void testLongType() throws IOException {
 StdKeyDeserializer longKD = StdKeyDeserializer.forType(Long.class);
 String key = "10000000000";
 Object result = longKD.deserializeKey(key, new TestContext());
 assertEquals(10000000000L, result);
 }

 @Test
 public void testFloatType() throws IOException {
 StdKeyDeserializer floatKD = StdKeyDeserializer.forType(Float.class);
 String key = "1.5";
 Object result = floatKD.deserializeKey(key, new TestContext());
 assertEquals(1.5f, result, 0.001f);
 }

 @Test
 public void testDoubleType() throws IOException {
 StdKeyDeserializer doubleKD = StdKeyDeserializer.forType(Double.class);
 String key = "3.14";
 Object result = doubleKD.deserializeKey(key, new TestContext());
 assertEquals(3.14, result, 0.001);
 }

 @Test
 public void testLocaleType() throws IOException {
 StdKeyDeserializer localeKD = StdKeyDeserializer.forType(Locale.class);
 String key = "en_US";
 Object result = localeKD.deserializeKey(key, new TestContext());
 assertEquals(Locale.US, result);
 }

 @Test
 public void testDateType() throws IOException {
 StdKeyDeserializer dateKD = StdKeyDeserializer.forType(Date.class);
 String key = "2022-01-01";
 Object result = dateKD.deserializeKey(key, new TestContext());
 assertEquals(new Date(1643312000000L), result);
 }

 @Test
 public void testCalendarType() throws IOException {
 StdKeyDeserializer calendarKD = StdKeyDeserializer.forType(Calendar.class);
 String key = "2022-01-01";
 Object result = calendarKD.deserializeKey(key, new TestContext());
 Calendar cal = (Calendar) result;
 assertEquals(2022, cal.get(Calendar.YEAR));
 assertEquals(1, cal.get(Calendar.MONTH));
 assertEquals(1, cal.get(Calendar.DAY_OF_MONTH));
 }

 @Test
 public void testUriType() throws IOException {
 StdKeyDeserializer uriKD = StdKeyDeserializer.forType(URI.class);
 String key = "http://example.com";
 Object result = uriKD.deserializeKey(key, new TestContext());
 assertEquals(new URI("http://example.com"), result);
 }

 @Test
 public void testUrlType() throws IOException {
 StdKeyDeserializer urlKD = StdKeyDeserializer.forType(URL.class);
 String key = "http://example.com";
 Object result = urlKD.deserializeKey(key, new TestContext());
 assertEquals(new URL("http://example.com"), result);
 }

 @Test
 public void testClassType() throws IOException {
 StdKeyDeserializer classKD = StdKeyDeserializer.forType(String.class);
 String key = "java.lang.String";
 Object result = classKD.deserializeKey(key, new TestContext());
 assertEquals(String.class, result);
 }

 @Test
 public void testNullType() throws IOException {
 StdKeyDeserializer nullKD = StdKeyDeserializer.forType(Void.TYPE);
 String key = null;
 Object result = nullKD.deserializeKey(key, new TestContext());
 assertEquals(null, result);
 }

 @Test
 public void testUnknownType() throws IOException {
 StdKeyDeserializer unknownKD = StdKeyDeserializer.forType(Object.class);
 String key = "notValidType";
 Object result = unknownKD.deserializeKey(key, new TestContext());
 assertNull(result);
 }

 @Test
 public void testInvalidInput() throws IOException {
 StdKeyDeserializer doubleKD = StdKeyDeserializer.forType(Double.class);
 String key = "invalidInput";
 Object result = doubleKD.deserializeKey(key, new TestContext());
 assertNull(result);
 }

 @Test
 public void testExceptionHandling() throws IOException {
 StdKeyDeserializer uriKD = StdKeyDeserializer.forType(URI.class);
 String key = "invalid:/";
 try {
 uriKD.deserializeKey(key, new TestContext());
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid URI: The scheme must not be empty", e.getMessage());
 }
 }

 @Test
 public void testInvalidEnum() throws IOException {
 StdKeyDeserializer enumKD = StdKeyDeserializer.forType(MyEnum.class);
 String key = "InvalidValue";
 Object result = enumKD.deserializeKey(key, new TestContext());
 assertNull(result);
 }

 @Test
 public void testEnumWithToString() throws IOException {
 StdKeyDeserializer enumKD = StdKeyDeserializer.forType(MyEnum.class);
 String key = "VALUE_B";
 Object result = enumKD.deserializeKey(key, new TestContext());
 assertEquals(MyEnum.VALUE_B, result);
 }

 @Test
 public void testEnumWithFactory() throws IOException {
 StdKeyDeserializer enumKD = StdKeyDeserializer.forType(MyEnumWithFactory.class);
 String key = "VALUE_C";
 Object result = enumKD.deserializeKey(key, new TestContext());
 assertEquals(MyEnumWithFactory.VALUE_C, result);
 }

 private static class TestContext extends DeserializationContext {
 @Override
 public ObjectReader _rootValueInst() {
 return mapper.getFactory().getSimpleValueSerializerProvider().getNullValueSerializer().getSerializer(null);
 }

 @Override
 public ObjectReader _valueInst() {
 return mapper.getFactory().getSimpleValueSerializerProvider().getNullValueSerializer().getSerializer(null);
 }

 @Override
 public JsonParser _currentLocationParser() {
 return null;
 }

 @Override
 public Object _valueInHandler() {
 return null;
 }

 @Override
 public void _close() throws IOException {
 // no-op
 }

 @Override
 public MapperConfig<?> _config() {
 MapperConfig<?> config = mapper.getConfig();
 config.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 config.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 return config;
 }

 @Override
 public void _reset() {
 // no-op
 }

 @Override
 public Object _valueHandler() {
 return null;
 }

 @Override
 public Object _valueHandlerInstantiator() {
 return null;
 }

 @Override
 public JsonParser _parser() {
 JsonParser parser = mapper.getFactory().createParser(getClass().getResourceAsStream("/test.json"), Base64Variants.US_ASCII);
 parser.setCodec(mapper);
 return parser;
 }

 @Override
 public BeanDescription _activeDataModel() {
 return null;
 }

 @Override
 public JavaType _constructType(Class<?> rawType) {
 return mapper.getTypeFactory().constructType(rawType);
 }
 }
}