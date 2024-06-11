import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DateTimeSerializerBaseTest {

 private static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");

 /**
 * Test constructor with default settings.
 */
 @Test
 public void testConstructorWithDefaultSettings() {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 assertEquals(Date.class, serializer.handledType());
 assertNull(serializer._useTimestamp);
 assertNull(serializer._customFormat);
 }

 /**
 * Test constructor with timestamp setting.
 */
 @Test
 public void testConstructorWithTimestampSetting() {
 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, true, null);
 assertEquals(Long.class, serializer.handledType());
 assertTrue(serializer._useTimestamp);
 assertNull(serializer._customFormat);
 }

 /**
 * Test constructor with custom format setting.
 */
 @Test
 public void testConstructorWithCustomFormatSetting() {
 DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, DEFAULT_DATE_FORMAT);
 assertEquals(String.class, serializer.handledType());
 assertFalse(serializer._useTimestamp);
 assertEquals(DEFAULT_DATE_FORMAT, serializer._customFormat);
 }

 /**
 * Test method 'withFormat'.
 */
 @Test
 public void testWithFormat() {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 DateTimeSerializerBase<Date> serializedWithTimestamp = serializer.withFormat(true, null);
 DateTimeSerializerBase<Date> serializedWithFormat = serializer.withFormat(false, DEFAULT_DATE_FORMAT);

 assertEquals(Date.class, serializedWithTimestamp.handledType());
 assertTrue(serializedWithTimestamp._useTimestamp);
 assertNull(serializedWithTimestamp._customFormat);

 assertEquals(Date.class, serializedWithFormat.handledType());
 assertFalse(serializedWithFormat._useTimestamp);
 assertEquals(DEFAULT_DATE_FORMAT, serializedWithFormat._customFormat);
 }

 /**
 * Test method 'serialize'.
 * @throws IOException
 */
 @Test
 public void testSerialize() throws IOException {
 Date date = new Date();
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
 ObjectWriter objectWriter = Mockito.mock(ObjectWriter.class);
 ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);

 when(serializers.getProvider()).thenReturn(objectWriter);
 when(objectWriter.writeValueAsString(eq(date), any())).thenReturn("2022-12-31T23:59:59.000Z");

 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 serializer.serialize(date, gen, serializers);

 String serializedDate = gen.getOutputшин();
 assertEquals("2022-12-31T23:59:59.000Z", serializedDate);
 }

 /**
 * Test method 'serialize' with timestamp setting.
 * @throws IOException
 */
 @Test
 public void testSerializeWithTimestampSetting() throws IOException {
 Long timestamp = 1672492800000L; // 1 January 2023, 00:00:00 UTC
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
 ObjectWriter objectWriter = Mockito.mock(ObjectWriter.class);
 ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);

 when(serializers.getProvider()).thenReturn(objectWriter);
 when(objectWriter.writeNumber(eq(timestamp), any())).thenReturn(null);

 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, true, null);
 serializer.serialize(timestamp, gen, serializers);

 long serializedTimestamp = gen.getOutputLong();
 assertEquals(timestamp, serializedTimestamp);
 }

 /**
 * Test method 'serialize' with custom format setting.
 * @throws IOException
 */
 @Test
 public void testSerializeWithCustomFormatSetting() throws IOException {
 Date date = new Date();
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
 ObjectWriter objectWriter = Mockito.mock(ObjectWriter.class);
 ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);

 when(serializers.getProvider()).thenReturn(objectWriter);
 when(objectWriter.writeStringValue(eq(date), any())).thenReturn("31-12-2022 23:59:59 +0200");

 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, DEFAULT_DATE_FORMAT);
 serializer.serialize(date, gen, serializers);

 String serializedDate = gen.getOutputString();
 assertEquals("31-12-2022 23:59:59 +0200", serializedDate);
 }

 /**
 * Test method 'acceptJsonFormatVisitor'.
 */
 @Test
 public void testAcceptJsonFormatVisitor() {
 JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
 JsonValueFormat jsonValueFormat = JsonValueFormat.DATE_TIME;
 JsonParser.NumberType numberType = JsonParser.NumberType.LONG;

 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 serializer._acceptJsonFormatVisitor(visitor, Date.class.getType(), false);

 verify(visitor).visitStringFormat(eq(Date.class), eq(jsonValueFormat), any());
 }
}