import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class DateTimeSerializerBaseTest {

 private ObjectMapper objectMapper;
 private SerializerProvider serializerProvider;

 @Before
 public void setup() {
 ObjectMapper.StdDateFormat serializer = new StdDateFormat();
 SimpleModule module = new SimpleModule();
 module.addSerializer(Date.class, new DateTimeSerializerBase<Date>(Date.class, false, serializer));
 objectMapper = new ObjectMapper();
 objectMapper.registerModule(module);
 serializerProvider = objectMapper.getSerializerProvider();
 }

 @Test
 public void testSerializeDateWithDefaultFormat() throws Exception {
 Date date = new Date();
 String json = "\"2023-03-10T15:15:15.000Z\""; // expected JSON representation

 JsonGenerator gen = new JsonFactory().createGenerator(System.out);
 DateTimeSerializerBase<Date> serializer = (DateTimeSerializerBase<Date>) objectMapper.findValueSerializer(Date.class, serializerProvider, null);
 serializer.serialize(date, gen, serializerProvider);
 gen.flush();

 String actualJson = gen.getOutputContent();
 assertEquals(json, actualJson);
 }

 @Test
 public void testSerializeDateWithCustomFormat() throws Exception {
 Date date = new Date();
 SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
 String json = "\"10-03-2023 15:15:15\""; // expected JSON representation

 JsonGenerator gen = new JsonFactory().createGenerator(System.out);
 serializer.serialize(date, gen, serializerProvider);
 gen.flush();

 String actualJson = gen.getOutputContent();
 assertEquals(json, actualJson);
 }

 @Test
 public void testSerializeDateAsTimestamp() throws Exception {
 Date date = new Date();
 long timestamp = date.getTime();
 String json = String.valueOf(timestamp); // expected JSON representation

 JsonGenerator gen = new JsonFactory().createGenerator(System.out);
 DateTimeSerializerBase<Date> serializer = (DateTimeSerializerBase<Date>) objectMapper.findValueSerializer(Date.class, serializerProvider, null);
 serializer.serialize(date, gen, serializerProvider);
 gen.writeNumber(timestamp);
 gen.flush();

 String actualJson = gen.getOutputContent();
 assertEquals(json, actualJson);
 }

 @Test
 public void testDeserializeDateFromDefaultFormat() throws Exception {
 String json = "\"2023-03-10T15:15:15.000Z\""; // expected JSON representation
 Date date = new Date();

 JsonParser parser = new JsonFactory().createParser(json);
 Object readerValue = objectMapper.readValue(parser, Date.class);
 DateTimeSerializerBase<Date> deserializer = (DateTimeSerializerBase<Date>) objectMapper.findValueDeserializer(Date.class, serializerProvider, null);
 Date deserializedDate = deserializer.deserialize(parser, serializerProvider, null);

 assertEquals(date, deserializedDate);
 }

 @Test
 public void testDeserializeDateFromCustomFormat() throws Exception {
 String json = "\"10-03-2023 15:15:15\""; // expected JSON representation
 SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
 Date date = new Date();

 JsonParser parser = new JsonFactory().createParser(json);
 Object readerValue = objectMapper.readValue(parser, Date.class);
 DateTimeSerializerBase<Date> deserializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
 Date deserializedDate = deserializer.deserialize(parser, serializerProvider, null);

 assertEquals(date, deserializedDate);
 }

 @Test
 public void testDeserializeDateAsTimestamp() throws Exception {
 String json = "1582332955000"; // expected JSON representation
 Date date = new Date();

 JsonParser parser = new JsonFactory().createParser(json);
 Object readerValue = objectMapper.readValue(parser, Long.class);
 long timestamp = ((Number) readerValue).longValue();
 DateTimeSerializerBase<Date> deserializer = (DateTimeSerializerBase<Date>) objectMapper.findValueDeserializer(Date.class, serializerProvider, null);
 Date deserializedDate = deserializer.deserialize(parser, serializerProvider, null);

 assertEquals(date, deserializedDate);
 }
}