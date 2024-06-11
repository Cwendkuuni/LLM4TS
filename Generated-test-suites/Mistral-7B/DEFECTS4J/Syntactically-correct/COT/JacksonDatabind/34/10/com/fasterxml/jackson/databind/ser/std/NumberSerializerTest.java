import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class NumberSerializerTest {

 private static final NumberSerializer NUMBER_SERIALIZER = new NumberSerializer(Number.class);

 @Test
 public void testSerializeBigDecimal() throws IOException {
 BigDecimal bigDecimal = new BigDecimal("123.45");
 JsonGenerator generator = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);

 NUMBER_SERIALIZER.serialize(bigDecimal, generator, provider);

 // Verify that the correct value was written to the generator
 when(generator.getOutputFactory().createNumberWriter(mock(OutputStream.class), null)).thenReturn(mock(JsonNumberWriter.class));
 when(generator.getOutputFactory().createCharacterEscaper()).thenReturn(mock(JsonStringEscaper.class));
 when(generator.writeNumber(eq((Number) anyObject()))).thenAnswer(invocation -> invocation.getArgument(0).toString());

 assertEquals("123.45", generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeBigInteger() throws IOException {
 BigInteger bigInteger = new BigInteger("12345678901234567890");
 JsonGenerator generator = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);

 NUMBER_SERIALIZER.serialize(bigInteger, generator, provider);

 // Verify that the correct value was written to the generator
 when(generator.getOutputFactory().createNumberWriter(mock(OutputStream.class), null)).thenReturn(mock(JsonNumberWriter.class));
 when(generator.getOutputFactory().createCharacterEscaper()).thenReturn(mock(JsonStringEscaper.class));
 when(generator.writeNumber(eq((Number) anyObject()))).thenAnswer(invocation -> invocation.getArgument(0).toString());

 assertEquals("12345678901234567890", generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeInteger() throws IOException {
 Integer integer = 123;
 JsonGenerator generator = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);

 NUMBER_SERIALIZER.serialize(integer, generator, provider);

 // Verify that the correct value was written to the generator
 when(generator.getOutputFactory().createNumberWriter(mock(OutputStream.class), null)).thenReturn(mock(JsonNumberWriter.class));
 when(generator.getOutputFactory().createCharacterEscaper()).thenReturn(mock(JsonStringEscaper.class));
 when(generator.writeNumber(eq((Number) anyObject()))).thenAnswer(invocation -> String.valueOf(invocation.getArgument(0).intValue()));

 assertEquals("123", generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeLong() throws IOException {
 Long longNumber = 1234567890123456789L;
 JsonGenerator generator = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);

 NUMBER_SERIALIZER.serialize(longNumber, generator, provider);

 // Verify that the correct value was written to the generator
 when(generator.getOutputFactory().createNumberWriter(mock(OutputStream.class), null)).thenReturn(mock(JsonNumberWriter.class));
 when(generator.getOutputFactory().createCharacterEscaper()).thenReturn(mock(JsonStringEscaper.class));
 when(generator.writeNumber(eq((Number) anyObject()))).thenAnswer(invocation -> String.valueOf(invocation.getArgument(0).longValue()));

 assertEquals("1234567890123456789", generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeDouble() throws IOException {
 Double doubleNumber = 123.45;
 JsonGenerator generator = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);

 NUMBER_SERIALIZER.serialize(doubleNumber, generator, provider);

 // Verify that the correct value was written to the generator
 when(generator.getOutputFactory().createNumberWriter(mock(OutputStream.class), null)).thenReturn(mock(JsonNumberWriter.class));
 when(generator.getOutputFactory().createCharacterEscaper()).thenReturn(mock(JsonStringEscaper.class));
 when(generator.writeNumber(eq((Number) anyObject()))).thenAnswer(invocation -> invocation.getArgument(0).toString());

 assertEquals("123.45", generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeFloat() throws IOException {
 Float floatNumber = 123.45f;
 JsonGenerator generator = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);

 NUMBER_SERIALIZER.serialize(floatNumber, generator, provider);
 // Verify that the correct value was written to the generator
 when(generator.getOutputFactory().createNumberWriter(mock(OutputStream.class), null)).thenReturn(mock(JsonNumberWriter.class));
 when(generator.getOutputFactory().createCharacterEscaper()).thenReturn(mock(JsonStringEscaper.class));
 when(generator.writeNumber(eq((Number) anyObject()))).thenAnswer(invocation -> invocation.getArgument(0).toString());

 assertEquals("123.45", generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeByte() throws IOException {
 Byte byteNumber = (byte) 123;
 JsonGenerator generator = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);

 NUMBER_SERIALIZER.serialize(byteNumber, generator, provider);

 // Verify that the correct value was written to the generator
 when(generator.getOutputFactory().createNumberWriter(mock(OutputStream.class), null)).thenReturn(mock(JsonNumberWriter.class));
 when(generator.getOutputFactory().createCharacterEscaper()).thenReturn(mock(JsonStringEscaper.class));
 when(generator.writeNumber(eq((Number) anyObject()))).thenAnswer(invocation -> String.valueOf(invocation.getArgument(0).intValue()));

 assertEquals("123", generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeShort() throws IOException {
 Short shortNumber = (short) 123;
 JsonGenerator generator = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);

 NUMBER_SERIALIZER.serialize(shortNumber, generator, provider);

 // Verify that the correct value was written to the generator
 when(generator.getOutputFactory().createNumberWriter(mock(OutputStream.class), null)).thenReturn(mock(JsonNumberWriter.class));
 when(generator.getOutputFactory().createCharacterEscaper()).thenReturn(mock(JsonStringEscaper.class));
 when(generator.writeNumber(eq((Number) anyObject()))).thenAnswer(invocation -> String.valueOf(invocation.getArgument(0).intValue()));

 assertEquals("123", generator.getOutputBuffer().toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeNull() throws IOException {
 JsonGenerator generator = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);

 NUMBER_SERIALIZER.serialize(null, generator, provider);
 }

 @Test(expected = IOException.class)
 public void testSerializeInvalidNumber() throws IOException {
 Object invalidNumber = new Object() {
 @Override
 public String toString() {
 throw new NumberFormatException("Invalid number: " + this.getClass().getName());
 }
 };
 JsonGenerator generator = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);

 NUMBER_SERIALIZER.serialize(invalidNumber, generator, provider);
 }
}