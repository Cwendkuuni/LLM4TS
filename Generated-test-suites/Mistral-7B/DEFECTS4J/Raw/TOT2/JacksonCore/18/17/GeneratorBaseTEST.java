import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonFactory.Feature;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GeneratorBaseTest {

 private JsonGenerator generator;
 private ObjectCodec objectCodec;
 private JsonFactory jsonFactory;

 @Before
 public void setUp() throws Exception {
 objectCodec = new ObjectMapper().getFactory();
 jsonFactory = new JsonFactory(objectCodec);
 generator = jsonFactory.createGenerator(new ByteArrayOutputStream(), objectCodec);
 }

 @Test
 public void testWriteNull() throws IOException {
 generator.writeNull();
 String result = generator.getOutputBuffer().toString();
 Assert.assertEquals("null", result);
 }

 @Test
 public void testWriteBoolean() throws IOException {
 generator.writeBoolean(true);
 String result = generator.getOutputBuffer().toString();
 Assert.assertEquals("true", result);
 }

 @Test
 public void testWriteBoolean_false() throws IOException {
 generator.writeBoolean(false);
 String result = generator.getOutputBuffer().toString();
 Assert.assertEquals("false", result);
 }

 @Test
 public void testWriteNumberInt() throws IOException {
 generator.writeNumber(123);
 String result = generator.getOutputBuffer().toString();
 Assert.assertEquals("123", result);
 }

 @Test
 public void testWriteNumberLong() throws IOException {
 generator.writeNumber(1234567890L);
 String result = generator.getOutputBuffer().toString();
 Assert.assertEquals("1234567890", result);
 }

 @Test
 public void testWriteNumberFloat() throws IOException {
 generator.writeNumber(123.45F);
 String result = generator.getOutputBuffer().toString();
 Assert.assertEquals("123.45", result);
 }

 @Test
 public void testWriteNumberDouble() throws IOException {
 generator.writeNumber(123.456789);
 String result = generator.getOutputBuffer().toString();
 Assert.assertEquals("123.456789", result);
 }

 @Test
 public void testWriteNumberBigDecimal() throws IOException {
 BigDecimal bd = new BigDecimal("123.456789");
 generator.writeNumber(bd);
 String result = generator.getOutputBuffer().toString();
 Assert.assertEquals(bd.toString(), result);
 }

 @Test
 public void testWriteString() throws IOException {
 generator.writeString("testString");
 String result = generator.getOutputBuffer().toString();
 Assert.assertEquals("testString", result);
 }

 @Test
 public void testWriteStringEmpty() throws IOException {
 generator.writeString("");
 String result = generator.getOutputBuffer().toString();
 Assert.assertEquals("", result);
 }

 @Test
 public void testWriteRawValueString() throws IOException {
 generator.writeRawValue("testString");
 String result = generator.getOutputBuffer().toString();
 Assert.assertEquals("testString", result);
 }

 @Test
 public void testWriteRawValueCharArray() throws IOException {
 char[] cArray = {'h', 'e', 'l', 'l', 'o'};
 generator.writeRawValue(cArray);
 String result = generator.getOutputBuffer().toString();
 Assert.assertEquals(new String(cArray), result);
 }

 @Test
 public void testWriteRawValueEmptyCharArray() throws IOException {
 char[] cArray = new char[0];
 generator.writeRawValue(cArray);
 String result = generator.getOutputBuffer().toString();
 Assert.assertEquals("", result);
 }

 @Test
 public void testWriteRawValueByteArray() throws IOException {
 byte[] bArray = {72, 101, 108, 108, 111};
 generator.writeRawValue(bArray);
 String result = generator.getOutputBuffer().toString();
 Assert.assertEquals(new String(bArray), result);
 }

 @Test
 public void testWriteRawValueEmptyByteArray() throws IOException {
 byte[] bArray = new byte[0];
 generator.writeRawValue(bArray);
 String result = generator.getOutputBuffer().toString();
 Assert.assertEquals("", result);
 }

 @Test
 public void testWriteBoolean_enableWriteNumbersAsStrings() throws IOException {
 generator = jsonFactory.createGenerator(new ByteArrayOutputStream(), objectCodec.writerWithFeatures(Feature.WRITE_NUMBERS_AS_STRINGS));
 generator.writeBoolean(true);
 String result = generator.getOutputBuffer().toString();
 Assert.assertEquals("true", result);
 }

 @Test
 public void testWriteBoolean_disableWriteNumbersAsStrings() throws IOException {
 generator = jsonFactory.createGenerator(new ByteArrayOutputStream(), objectCodec.writerWithFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.FLIP()));
 generator.writeBoolean(true);
 String result = generator.getOutputBuffer().toString();
 Assert.assertEquals("true", result);
 }

 @Test
 public void testWriteRawValueWithSurrogate() throws IOException {
 generator.writeRawValue("\uD842\uDC00\uD842\uDC01");
 String result = generator.getOutputBuffer().toString();
 Assert.assertEquals("\uD842\uDC00\uD842\uDC01", result);
 }

 @Test
 public void testWriteRawValueWithSurrogate_overflow() throws IOException {
 try {
 generator.writeRawValue("\uD800\uDBFF\uD800\uDBFF");
 fail("Expected JsonGenerationException");
 } catch (JsonGenerationException e) {
 // expected
 }
 generator.close();
 }

 @Test
 public void testWriteObject_withDefaultCodec() throws IOException {
 generator.writeObject(new TestObject("test", 123));
 String result = generator.getOutputBuffer().toString();
 Assert.assertEquals("{\"test\":\"test\",\"i\":123}", result);
 }

 @Test
 public void testWriteObject_customCodec() throws IOException {
 generator = jsonFactory.createGenerator(new ByteArrayOutputStream(), MyCustomCodec.instance());
 generator.writeObject(new TestObject("test", 123));
 String result = generator.getOutputBuffer().toString();
 Assert.assertEquals("{\"test\":\"test\",\"i\":123}", result);
 }

 @Test
 public void testWriteObjectNull() throws IOException {
 generator.writeObject(null);
 String result = generator.getOutputBuffer().toString();
 Assert.assertEquals("null", result);
 }

 @Test
 public void testWriteTree() throws IOException {
 generator.writeTree(new JsonNode("{\"test\":\"test\",\"i\":123,\"arr\":[1,2,3]}"));
 String result = generator.getOutputBuffer().toString();
 Assert.assertEquals("{\"test\":\"test\",\"i\":123,\"arr\":[1,2,3]}", result);
 }

 @Test
 public void testClose() throws IOException {
 generator.close();
 String result = generator.getOutputBuffer().toString();
 Assert.assertEquals("", result);
 }

 @Test(expected = JsonGenerationException.class)
 public void testWriteNumberIntOverflow() throws IOException {
 generator.writeNumber(Integer.MAX_VALUE + 1);
 }

 @Test(expected = JsonGenerationException.class)
 public void testWriteNumberLongOverflow() throws IOException {
 generator.writeNumber(Long.MAX_VALUE + 1L);
 }

 @Test(expected = JsonGenerationException.class)
 public void testWriteNumberFloatNaN() throws IOException {
 generator.writeNumber(Float.NaN);
 }

 @Test(expected = JsonGenerationException.class)
 public void testWriteNumberDoubleNaN() throws IOException {
 generator.writeNumber(Double.NaN);
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteObjectInvalidJson() throws IOException {
 generator.writeObject("[1, 2, {", JsonToken.START_OBJECT);
 }

 @Test(expected = JsonMappingException.class)
 public void testWriteObjectInvalidJsonObject() throws IOException {
 generator.writeObject(new JsonNode("{test: [1, 2]}"));
 }

 @Test(expected = IllegalStateException.class)
 public void testWriteObjectAlreadyClosed() throws IOException {
 generator.close();
 generator.writeObject(new TestObject("test", 123));
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteTreeInvalidJson() throws IOException {
 generator.writeTree(new JsonNode("[1, 2, {"));
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteTreeInvalidJsonObject() throws IOException {
 generator.writeTree(new JsonNode("{test: [1, 2]}"));
 }

 @Test(expected = IllegalStateException.class)
 public void testWriteTreeAlreadyClosed() throws IOException {
 generator.close();
 generator.writeTree(new JsonNode("{}"));
 }

 @Test(expected = IOException.class)
 public void testWriteObjectStreamClosed() throws IOException {
 generator = jsonFactory.createGenerator(new ByteArrayOutputStream(), objectCodec);
 generator.writeObject(new TestObject("test", 123));
 generator.close();
 generator.writeObject(new TestObject("test2", 456));
 }

 @Test(expected = IOException.class)
 public void testWriteTreeStreamClosed() throws IOException {
 generator = jsonFactory.createGenerator(new ByteArrayOutputStream(), objectCodec);
 generator.writeTree(new JsonNode("{}"));
 generator.close();
 generator.writeTree(new JsonNode("{}"));
 }

 private static class TestObject {
 String name;
 int i;

 TestObject(String name, int i) {
 this.name = name;
 this.i = i;
 }
 }

 private static class MyCustomCodec extends JsonSerializer<TestObject> {
 @Override public void serialize(TestObject value, JsonGenerator generator, SerializerProvider serializers) throws IOException {
 generator.writeStartObject();
 generator.writeFieldName("name");
 generator.writeString(value.name);
 generator.writeFieldName("i");
 generator.writeNumber(value.i * 2);
 generator.writeEndObject();
 }

 @Override public Class<TestObject> handledType() {
 return TestObject.class;
 }
 }
}