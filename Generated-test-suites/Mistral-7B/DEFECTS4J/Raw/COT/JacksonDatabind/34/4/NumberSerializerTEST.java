import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class NumberSerializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testSerializeNumber() {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = null;
 try {
 jsonGenerator = objectMapper.getFactory().createGenerator(new java.io.FileOutputStream("output.json"), null);
 NumberSerializer.NumberSerializer spyNumberSerializer = spy(NumberSerializer.class, "serialize");
 when(spyNumberSerializer.serialize(any(Number.class), eq(jsonGenerator), any(SerializerProvider.class))).thenCallRealMethod();
 SerializerProvider serializerProvider = new SerializerProvider() {
 @Override
 public void writeValue(JsonGenerator jg, Object value) throws IOException {
 jg.writeNumber(value.toString());
 }
 };
 ObjectMapper objectMapperSpy = spy(ObjectMapper.class);
 when(objectMapperSpy.getSerializerProvider()).thenReturn(serializerProvider);
 ObjectMapper objectMapperWithSpySerializer = new ObjectMapper();
 objectMapperWithSpySerializer.setSerializerProvider(objectMapperSpy);
 Number testNumber = 123;
 Number expectedNumber = 123;
 Number serializedNumber = objectMapperWithSpySerializer.writeValueAsNumber(testNumber);
 Number deserializedNumber = objectMapper.readValue(objectMapper.writeValueAsString(serializedNumber), Number.class);
 assertEquals(expectedNumber, deserializedNumber);
 verify(numberSerializer, times(1)).serialize(testNumber, eq(jsonGenerator), any(SerializerProvider.class));
 } catch (IOException e) {
 e.printStackTrace();
 } finally {
 try {
 jsonGenerator.close();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }

 @Test
 public void testSerializeBigDecimal() {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigDecimal testBigDecimal = new BigDecimal("3.14");
 JsonGenerator jsonGenerator = null;
 try {
 jsonGenerator = objectMapper.getFactory().createGenerator(new java.io.FileOutputStream("output.json"), null);
 NumberSerializer.NumberSerializer spyNumberSerializer = spy(NumberSerializer.class, "serialize");
 when(spyNumberSerializer.serialize(eq(testBigDecimal), any(JsonGenerator.class), any(SerializerProvider.class))).thenCallRealMethod();
 SerializerProvider serializerProvider = new SerializerProvider() {
 @Override
 public void writeValue(JsonGenerator jg, Object value) throws IOException {
 jg.writeNumber(value.toString());
 }
 };
 ObjectMapper objectMapperSpy = spy(ObjectMapper.class);
 when(objectMapperSpy.getSerializerProvider()).thenReturn(serializerProvider);
 ObjectMapper objectMapperWithSpySerializer = new ObjectMapper();
 objectMapperWithSpySerializer.setSerializerProvider(objectMapperSpy);
 JsonParser jsonParser = objectMapperWithSpySerializer.getFactory().createParser("\"3.14\"");
 Number deserializedNumber = objectMapper.readValue(jsonParser, Number.class);
 BigDecimal expectedBigDecimal = testBigDecimal;
 assertEquals(expectedBigDecimal, deserializedNumber);
 verify(numberSerializer, times(1)).serialize(testBigDecimal, eq(jsonGenerator), any(SerializerProvider.class));
 } catch (IOException e) {
 e.printStackTrace();
 } finally {
 try {
 jsonGenerator.close();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }

 @Test
 public void testSerializeBigInteger() {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigInteger testBigInteger = new BigInteger("123456789012345678901234567890");
 JsonGenerator jsonGenerator = null;
 try {
 jsonGenerator = objectMapper.getFactory().createGenerator(new java.io.FileOutputStream("output.json"), null);
 NumberSerializer.NumberSerializer spyNumberSerializer = spy(NumberSerializer.class, "serialize");
 when(spyNumberSerializer.serialize(eq(testBigInteger), any(JsonGenerator.class), any(SerializerProvider.class))).thenCallRealMethod();
 SerializerProvider serializerProvider = new SerializerProvider() {
 @Override
 public void writeValue(JsonGenerator jg, Object value) throws IOException {
 jg.writeNumber(value.toString());
 }
 };
 ObjectMapper objectMapperSpy = spy(ObjectMapper.class);
 when(objectMapperSpy.getSerializerProvider()).thenReturn(serializerProvider);
 ObjectMapper objectMapperWithSpySerializer = new ObjectMapper();
 objectMapperWithSpySerializer.setSerializerProvider(objectMapperSpy);
 JsonParser jsonParser = objectMapperWithSpySerializer.getFactory().createParser("\"123456789012345678901234567890\"");
 Number deserializedNumber = objectMapper.readValue(jsonParser, Number.class);
 BigInteger expectedBigInteger = testBigInteger;
 assertEquals(expectedBigInteger, deserializedNumber);
 verify(numberSerializer, times(1)).serialize(testBigInteger, eq(jsonGenerator), any(SerializerProvider.class));
 } catch (IOException e) {
 e.printStackTrace();
 } finally {
 try {
 jsonGenerator.close();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeNull() {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = null;
 Number testNumber = null;
 try {
 jsonGenerator = objectMapper.getFactory().createGenerator(new java.io.FileOutputStream("output.json"), null);
 NumberSerializer.NumberSerializer spyNumberSerializer = spy(NumberSerializer.class, "serialize");
 when(spyNumberSerializer.serialize(eq(testNumber), any(JsonGenerator.class), any(SerializerProvider.class))).thenCallRealMethod();
 SerializerProvider serializerProvider = new SerializerProvider() {
 @Override
 public void writeValue(JsonGenerator jg, Object value) throws IOException {
 jg.writeNumber(value.toString());
 }
 };
 ObjectMapper objectMapperSpy = spy(ObjectMapper.class);
 when(objectMapperSpy.getSerializerProvider()).thenReturn(serializerProvider);
 ObjectMapper objectMapperWithSpySerializer = new ObjectMapper();
 objectMapperWithSpySerializer.setSerializerProvider(objectMapperSpy);
 verify(numberSerializer, times(1)).serialize(testNumber, eq(jsonGenerator), any(SerializerProvider.class));
 } catch (IOException e) {
 e.printStackTrace();
 } finally {
 try {
 jsonGenerator.close();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeNonNumber() {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 String testString = "Hello World";
 JsonGenerator jsonGenerator = null;
 try {
 jsonGenerator = objectMapper.getFactory().createGenerator(new java.io.FileOutputStream("output.json"), null);
 NumberSerializer.NumberSerializer spyNumberSerializer = spy(NumberSerializer.class, "serialize");
 when(spyNumberSerializer.serialize(eq(testString), any(JsonGenerator.class), any(SerializerProvider.class))).thenCallRealMethod();
 SerializerProvider serializerProvider = new SerializerProvider() {
 @Override
 public void writeValue(JsonGenerator jg, Object value) throws IOException {
 jg.writeNumber(value.toString());
 }
 };
 ObjectMapper objectMapperSpy = spy(ObjectMapper.class);
 when(objectMapperSpy.getSerializerProvider()).thenReturn(serializerProvider);
 ObjectMapper objectMapperWithSpySerializer = new ObjectMapper();
 objectMapperWithSpySerializer.setSerializerProvider(objectMapperSpy);
 verify(numberSerializer, times(1)).serialize(testString, eq(jsonGenerator), any(SerializerProvider.class));
 } catch (IOException e) {
 e.printStackTrace();
 } finally {
 try {
 jsonGenerator.close();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }
}