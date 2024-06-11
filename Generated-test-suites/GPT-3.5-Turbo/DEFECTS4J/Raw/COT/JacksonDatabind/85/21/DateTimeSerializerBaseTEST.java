import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class DateTimeSerializerBaseTest {

   private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
   private final TimeZone timeZone = TimeZone.getTimeZone("UTC");
   private final Locale locale = Locale.US;
   private final ObjectMapper objectMapper = new ObjectMapper();

   @Test
   public void testSerializeWithCustomFormat() throws IOException {
       DateTimeSerializerBase<CustomDate> serializer = new DateTimeSerializer();
       serializer.setFormat(true, dateFormat);
       SimpleModule module = new SimpleModule();
       module.addSerializer(CustomDate.class, serializer);
       objectMapper.registerModule(module);
       CustomDate date = new CustomDate();
       date.setDate(new Date());
       String json = objectMapper.writeValueAsString(date);
       Assert.assertTrue(json.contains(dateFormat.format(date.getDate())));
   }

   @Test
   public void testSerializeWithTimestamp() throws IOException {
       DateTimeSerializerBase<CustomDate> serializer = new DateTimeSerializer();
       serializer.setFormat(false, null);
       SimpleModule module = new SimpleModule();
       module.addSerializer(CustomDate.class, serializer);
       objectMapper.registerModule(module);
       CustomDate date = new CustomDate();
       date.setDate(new Date());
       String json = objectMapper.writeValueAsString(date);
       long timestamp = date.getDate().getTime();
       Assert.assertTrue(json.contains("\"" + timestamp + "\""));
   }

   @Test
   public void testSerializeWithDefaultFormat() throws IOException {
       DateTimeSerializerBase<CustomDate> serializer = new DateTimeSerializer();
       serializer.setFormat(false, null);
       SimpleModule module = new SimpleModule();
       module.addSerializer(CustomDate.class, serializer);
       objectMapper.registerModule(module);
       CustomDate date = new CustomDate();
       date.setDate(new Date());
       String json = objectMapper.writeValueAsString(date);
       String expectedDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date.getDate());
       Assert.assertTrue(json.contains("\"" + expectedDateFormat + "\""));
   }

   @Test
   public void testSerializeWithEmptyDate() throws IOException {
       DateTimeSerializerBase<CustomDate> serializer = new DateTimeSerializer();
       serializer.setFormat(false, null);
       SimpleModule module = new SimpleModule();
       module.addSerializer(CustomDate.class, serializer);
       objectMapper.registerModule(module);
       CustomDate date = new CustomDate();
       String json = objectMapper.writeValueAsString(date);
       Assert.assertEquals("{}", json);
   }

   @Test
   public void testSerializeWithNullDate() throws IOException {
       DateTimeSerializerBase<CustomDate> serializer = new DateTimeSerializer();
       serializer.setFormat(false, null);
       SimpleModule module = new SimpleModule();
       module.addSerializer(CustomDate.class, serializer);
       objectMapper.registerModule(module);
       CustomDate date = new CustomDate();
       date.setDate(null);
       String json = objectMapper.writeValueAsString(date);
       Assert.assertEquals("{}", json);
   }

   @Test
   public void testWithCustomFormat() throws ParseException {
       DateTimeSerializerBase<CustomDate> serializer = new DateTimeSerializer();
       serializer.setFormat(true, dateFormat);
       SimpleModule module = new SimpleModule();
       module.addSerializer(CustomDate.class, serializer);
       objectMapper.registerModule(module);
       String dateStr = "2020-01-01";
       CustomDate date = new CustomDate();
       date.setDate(dateFormat.parse(dateStr));
       String json = objectMapper.writeValueAsString(date);
       Assert.assertTrue(json.contains(dateStr));
   }

   @Test
   public void testGetSchema() {
       DateTimeSerializerBase<CustomDate> serializer = new DateTimeSerializer();
       serializer.setFormat(false, null);
       SimpleModule module = new SimpleModule();
       module.addSerializer(CustomDate.class, serializer);
       objectMapper.registerModule(module);
       String expectedSchema = "{\"type\":\"string\",\"format\":\"date-time\"}";
       Assert.assertEquals(expectedSchema, serializer.getSchema(null, null).toString());
   }

   private class CustomDate {
       private Date date;

       public Date getDate() {
           return date;
       }

       public void setDate(Date date) {
           this.date = date;
       }
   }
}