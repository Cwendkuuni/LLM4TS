import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.std.*;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Long> longSerializer;
 private DateTimeSerializerBase<String> stringSerializer;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 longSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
 stringSerializer = new DateTimeSerializerBase<String>(String.class, false, null);
 }

 @Test
 public void testWithFormat() {
 DateTimeSerializerBase<Long> timestampSerializer = longSerializer.withFormat(true, null);
 DateTimeSerializerBase<Long> customFormatSerializer = longSerializer.withFormat(false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));

 Assert.assertNotSame(longSerializer, timestampSerializer);
 Assert.assertNotSame(longSerializer, customFormatSerializer);

 Assert.assertFalse(timestampSerializer._customFormat instanceof SimpleDateFormat);
 Assert.assertNull(timestampSerializer._customFormat);
 Assert.assertTrue(timestampSerializer._useTimestamp);

 Assert.assertFalse(customFormatSerializer._useTimestamp);
 Assert.assertNotNull(customFormatSerializer._customFormat);
 Assert.assertTrue(customFormatSerializer._customFormat instanceof SimpleDateFormat);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateContextualNullProperty() throws JsonMappingException {
 SerializerProvider serializers = new SerializerProvider();
 JsonFormat.Value format = new JsonFormat.Value();
 DateTimeSerializerBase<Object> datetimeSerializer = stringSerializer.createContextual(serializers, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateContextualNullHandledType() throws JsonMappingException {
 SerializerProvider serializers = new SerializerProvider();
 DateTimeSerializerBase<Object> datetimeSerializer = stringSerializer.createContextual(serializers, new BeanProperty());
 }

 @Test
 public void testCreateContextualDefault() throws JsonMappingException {
 SerializerProvider serializers = new SerializerProvider();
 JsonFormat.Value format = new JsonFormat.Value();
 DateTimeSerializerBase<Object> datetimeSerializer = stringSerializer.createContextual(serializers, new BeanProperty());
 Assert.assertSame(stringSerializer, datetimeSerializer);
 }

 @Test
 public void testCreateContextualWithFormat() throws JsonMappingException {
 SerializerProvider serializers = new SerializerProvider();
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 format.setLocale(Locale.UK);
 DateTimeSerializerBase<Object> datetimeSerializer = stringSerializer.createContextual(serializers, new BeanProperty());
 Assert.assertFalse(stringSerializer._customFormat instanceof SimpleDateFormat);
 Assert.assertNotNull(datetimeSerializer._customFormat);
 Assert.assertTrue(datetimeSerializer._customFormat instanceof SimpleDateFormat);
 Assert.assertEquals(Locale.UK, datetimeSerializer._customFormat.getLocale());
 Assert.assertFalse(datetimeSerializer._useTimestamp);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNullValue() throws JsonMappingException, IOException {
 JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
 longSerializer.serialize(null, gen, new SerializerProvider());
 }

 @Test
 public void testSerializeTimestamp() throws JsonMappingException, IOException {
 long timestamp = 1633046123000L;
 JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
 longSerializer.serialize(timestamp, gen, new SerializerProvider());
 String result = gen.getOutput().toString();
 Assert.assertEquals("1633046123000", result);
 }

 @Test
 public void testSerializeCustomFormat() throws JsonMappingException, IOException {
 Long date = new Long(1633046123000L);
 SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
 JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
 longSerializer.setCustomFormat(df);
 longSerializer.serialize(date, gen, new SerializerProvider());
 String result = gen.getOutput().toString();
 Assert.assertEquals("25-02-2023 14:21:23", result);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetSchemaNullTypeHint() {
 SerializerProvider serializers = new SerializerProvider();
 DateTimeSerializerBase<Object> datetimeSerializer = stringSerializer;
 JsonNode schema = datetimeSerializer.getSchema(serializers, null);
 }

 @Test
 public void testGetSchemaAsString() {
 SerializerProvider serializers = new SerializerProvider();
 DateTimeSerializerBase<String> stringSerializer = new DateTimeSerializerBase<String>(String.class, false, null);
 JsonNode schema = stringSerializer.getSchema(serializers, String.class);
 Assert.assertEquals("string", schema.getClass().getSimpleName());
 }

 @Test
 public void testGetSchemaAsNumber() {
 SerializerProvider serializers = new SerializerProvider();
 DateTimeSerializerBase<Long> longSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
 JsonNode schema = longSerializer.getSchema(serializers, Long.TYPE);
 Assert.assertEquals("number", schema.getClass().getSimpleName());
 }

}