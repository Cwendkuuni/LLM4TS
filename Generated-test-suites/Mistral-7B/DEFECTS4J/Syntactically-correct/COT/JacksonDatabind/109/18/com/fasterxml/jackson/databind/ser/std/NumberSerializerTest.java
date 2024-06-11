import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NumberSerializerTest {

 private static final NumberSerializer NUMBER_SERIALIZER = new NumberSerializer(Number.class);

 @Test
 public void testInstance() {
 assertNotNull(NUMBER_SERIALIZER);
assertEquals(Number.class, NUMBER_SERIALIZER.handledType());
 }

 @Test
 public void testCreateContextualNull() {
 assertSame(NUMBER_SERIALIZER, NUMBER_SERIALIZER.createContextual(null, null));
 }

 @Test
 public void testCreateContextualProperty() {
 JsonFormat format = new JsonFormat();
 assertNotSame(NUMBER_SERIALIZER, NUMBER_SERIALIZER.createContextual(null, new BeanProperty("testProperty")));
 }

 @Test
 public void testCreateContextualWithFormat() {
 JsonFormat format = new JsonFormat(new StandardSerializer(String.class) {}.constructor(null));
 assertNotSame(NUMBER_SERIALIZER, NUMBER_SERIALIZER.createContextual(null, new BeanProperty("testProperty"), format));
 }

 @Test
 public void testSerializeBigDecimal() throws IOException {
 BigDecimal bigDecimal = new BigDecimal("123.456");
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
 serializer.serialize(bigDecimal, jsonGenerator, null);
 assertEquals("123.456", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeBigInteger() throws IOException {
 BigInteger bigInteger = new BigInteger("12345678901234567890");
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 NumberSerializer serializer = new NumberSerializer(BigInteger.class);
 serializer.serialize(bigInteger, jsonGenerator, null);
 assertEquals("12345678901234567890", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeLong() throws IOException {
 Long longValue = 123L;
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 NumberSerializer serializer = new NumberSerializer(Long.class);
 serializer.serialize(longValue, jsonGenerator, null);
 assertEquals("123", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeDouble() throws IOException {
 Double doubleValue = 123.456;
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 NumberSerializer serializer = new NumberSerializer(Double.class);
 serializer.serialize(doubleValue, jsonGenerator, null);
 assertEquals("123.456", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeFloat() throws IOException {
 Float floatValue = 123.45f;
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 NumberSerializer serializer = new NumberSerializer(Float.class);
 serializer.serialize(floatValue, jsonGenerator, null);
 assertEquals("123.45", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeInteger() throws IOException {
 Integer integerValue = 123;
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 NumberSerializer serializer = new NumberSerializer(Integer.class);
 serializer.serialize(integerValue, jsonGenerator, null);
 assertEquals("123", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeByte() throws IOException {
 Byte byteValue = (byte) 123;
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 NumberSerializer serializer = new NumberSerializer(Byte.class);
 serializer.serialize(byteValue, jsonGenerator, null);
 assertEquals("123", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeShort() throws IOException {
 Short shortValue = (short) 123;
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 NumberSerializer serializer = new NumberSerializer(Short.class);
 serializer.serialize(shortValue, jsonGenerator, null);
 assertEquals("123", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeString() throws IOException {
 String stringValue = "123";
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 NumberSerializer serializer = new NumberSerializer(String.class);
 serializer.serialize(stringValue, jsonGenerator, null);
 assertEquals("123", jsonGenerator.getOutputBuffer().toString());
 }

 @Test(expected = IOException.class)
 public void testSerializeNull() throws IOException {
 NumberSerializer serializer = new NumberSerializer(Number.class);
 JsonGenerator jsonGenerator = createMockJsonGenerator();
 serializer.serialize(null, jsonGenerator, null);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonFormatVisitorWrapper visitor = objectMapper.getSerializerProvider().getObjectWriter()
         .getSchemaFactory()
         .findSchema(Object.class, JsonFormat.Value.builder()
                 .withShape(JsonFormat.Shape.NUMBER)
                 .build());
 NumberSerializer serializer = new NumberSerializer(Number.class);
 serializer.acceptJsonFormatVisitor(visitor, null);
 assertTrue(visitor.isValid());
 }

 private JsonGenerator createMockJsonGenerator() {
 return mock(JsonGenerator.class);
 }

}