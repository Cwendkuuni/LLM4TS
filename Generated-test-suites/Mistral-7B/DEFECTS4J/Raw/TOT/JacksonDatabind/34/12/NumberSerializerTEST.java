import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

 @Test
 public void testSerializeBigDecimal() throws JsonProcessingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();
 BigDecimal bigDecimal = new BigDecimal("123.456");

 numberSerializer.serialize(bigDecimal, jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 String jsonString = jsonGenerator.getOutputString();
 assertEquals("123.456", jsonString);
 }

 @Test
 public void testSerializeBigInteger() throws JsonProcessingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();
 BigInteger bigInteger = BigInteger.valueOf(1234567890123456L);

 numberSerializer.serialize(bigInteger, jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 String jsonString = jsonGenerator.getOutputString();
 assertEquals("1234567890123456", jsonString);
 }

 @Test
 public void testSerializeInteger() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();
 Integer integer = 123;

 numberSerializer.serialize(integer, jsonGenerator, mapper.getSerializerProvider());
 jsonGenerator.flush();
 String jsonString = jsonGenerator.getOutputString();
 assertEquals("123", jsonString);
 }

 @Test(expected = JsonProcessingException.class)
 public void testSerializeNull() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();

 numberSerializer.serialize(null, jsonGenerator, mapper.getSerializerProvider());
 }

 @Test
 public void testGetSchemaInteger() throws JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 Schema schema = mapper.getTypeFactory().constructSchema(Schema.Type.NUMBER, Number.class);
 assertEquals("integer", numberSerializer.getSchema(mapper.getSerializerProvider(), Number.class).getSchemaName());
 }

 @Test
 public void testGetSchemaBigDecimal() throws JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 Schema schema = mapper.getTypeFactory().constructSchema(Schema.Type.NUMBER, BigDecimal.class);
 assertEquals("number", numberSerializer.getSchema(mapper.getSerializerProvider(), BigDecimal.class).getSchemaName());
 }

 @Test
 public void testAcceptJsonFormatVisitorBigDecimal() throws JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getDefaultSerializerProvider().getSerializerProvider().getProviderInstance(JsonFormatVisitorWrapper.class);

 numberSerializer.acceptJsonFormatVisitor(visitor, mapper.getTypeFactory().constructType(Number.class, BigDecimal.class));
 assertTrue(visitor.isAccepted());
 }

 @Test
 public void testAcceptJsonFormatVisitorBigInteger() throws JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getProviderInstance(JsonFormatVisitorWrapper.class);

 numberSerializer.acceptJsonFormatVisitor(visitor, mapper.getTypeFactory().constructType(Number.class, BigInteger.class));
 assertTrue(visitor.isAccepted());
 }
}