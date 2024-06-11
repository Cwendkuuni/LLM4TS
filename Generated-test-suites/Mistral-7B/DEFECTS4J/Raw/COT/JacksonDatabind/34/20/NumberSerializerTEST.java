import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class NumberSerializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testSerializeNumber() {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

 Number number = new BigDecimal("123.45");

 numberSerializer.serialize(number, jsonGenerator, serializerProvider);

 // Verify writeNumber method was called with correct argument
 Mockito.verify(jsonGenerator).writeNumber(number);
 }

 @Test
 public void testSerializeBigDecimal() {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

 BigDecimal bigDecimal = new BigDecimal("123.45");

 numberSerializer.serialize(bigDecimal, jsonGenerator, serializerProvider);

 // Verify writeNumber method was called with correct argument
 Mockito.verify(jsonGenerator).writeNumber(bigDecimal);
 }

 @Test
 public void testSerializeBigInteger() {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

 BigInteger bigInteger = new BigInteger("123");

 numberSerializer.serialize(bigInteger, jsonGenerator, serializerProvider);

 // Verify writeNumber method was called with correct argument
 Mockito.verify(jsonGenerator).writeNumber(bigInteger);
 }

 @Test
 public void testSerializeInteger() {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

 Integer integer = new Integer(123);

 numberSerializer.serialize(integer, jsonGenerator, serializerProvider);

 // Verify writeNumber method was called with correct argument
 Mockito.verify(jsonGenerator).writeNumber(integer.intValue());
 }

 @Test
 public void testSerializeLong() {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

 Long longNumber = new Long("123");

 numberSerializer.serialize(longNumber, jsonGenerator, serializerProvider);

 // Verify writeNumber method was called with correct argument
 Mockito.verify(jsonGenerator).writeNumber(longNumber.longValue());
 }

 @Test
 public void testSerializeDouble() {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

 Double doubleNumber = new Double("123.45");

 numberSerializer.serialize(doubleNumber, jsonGenerator, serializerProvider);

 // Verify writeNumber method was called with correct argument
 Mockito.verify(jsonGenerator).writeNumber(doubleNumber.doubleValue());
 }

 @Test
 public void testSerializeFloat() {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

 Float floatNumber = new Float("123.45");

 numberSerializer.serialize(floatNumber, jsonGenerator, serializerProvider);

 // Verify writeNumber method was called with correct argument
 Mockito.verify(jsonGenerator).writeNumber(floatNumber.floatValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeNullNumber() {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

 Number number = null;

 numberSerializer.serialize(number, jsonGenerator, serializerProvider);
 }

 @Test(expected = IOException.class)
 public void testSerializeException() {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

 doThrow(IOException.class).when(jsonGenerator).writeNumber(anyInt());

 Number number = new Integer(123);

 numberSerializer.serialize(number, jsonGenerator, serializerProvider);
 }
}