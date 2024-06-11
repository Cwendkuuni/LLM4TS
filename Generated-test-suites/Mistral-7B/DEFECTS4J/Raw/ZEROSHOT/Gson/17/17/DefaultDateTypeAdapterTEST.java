import org.junit.Assert;
 import org.junit.Before;
 import org.junit.Test;
 import org.mockito.Mockito;
 import com.google.gson.DateTypeAdapter;
 import com.google.gson.DefaultDateTypeAdapter;
 import com.google.gson.Gson;
 import com.google.gson.JsonElement;
 import com.google.gson.JsonReader;
 import com.google.gson.JsonWriter;
 import com.google.gson.StreamJsonWriter;
 import java.io.ByteArrayInputStream;
 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
 import java.sql.Timestamp;
 import java.text.DateFormat;
 import java.text.ParseException;
 import java.text.SimpleDateFormat;
 import java.util.Date;
 import java.util.Locale;

 public class DefaultDateTypeAdapterTest {

 private DefaultDateTypeAdapter dateTypeAdapter;
 private DateFormat enUsFormat;
 private DateFormat localFormat;
 private Gson gson;

 @Before
 public void setup() {
 dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 enUsFormat = Mockito.spy(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US));
 localFormat = Mockito.spy(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
 gson = new Gson();
 }

 @Test
 public void testDefaultConstructor() {
 DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 DefaultDateTypeAdapter expectedDefaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class, enUsFormat, localFormat);
 Assert.assertEquals(expectedDefaultDateTypeAdapter.dateType, defaultDateTypeAdapter.dateType);
 Assert.assertEquals(expectedDefaultDateTypeAdapter.enUsFormat, defaultDateTypeAdapter.enUsFormat);
 Assert.assertEquals(expectedDefaultDateTypeAdapter.localFormat, defaultDateTypeAdapter.localFormat);
 }

 @Test
 public void testStringConstructor() {
 String datePattern = "dd-MM-yyyy HH:mm:ss";
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class, datePattern);
 SimpleDateFormat expectedDateFormat = new SimpleDateFormat(datePattern, Locale.US);
 Assert.assertEquals(expectedDateFormat, dateTypeAdapter.enUsFormat);
 Assert.assertEquals(expectedDateFormat, dateTypeAdapter.localFormat);
 }

 @Test
 public void testIntConstructor() {
 int dateStyle = DateFormat.SHORT;
 int timeStyle = DateFormat.SHORT;
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(dateStyle, timeStyle);
 DateFormat expectedDateFormat = DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.US);
 Assert.assertEquals(expectedDateFormat, dateTypeAdapter.enUsFormat);
 Assert.assertEquals(expectedDateFormat, dateTypeAdapter.localFormat);
 }

 @Test
 public void testWrite() throws IOException {
 Date date = new Date();
 byte[] expectedJsonBytes = new ByteArrayOutputStream() {{
 writeTo(gson.newJsonWriter(this));
 gson.getAdapter(JsonWriter.class).serialize(date, this);
 writeTo(new ByteArrayOutputStream());
 close();
 }}.toByteArray();
 String expectedJsonString = new String(expectedJsonBytes);
 byte[] jsonBytes = new byte[expectedJsonBytes.length];
 JsonReader jsonReader = new JsonReader(new ByteArrayInputStream(expectedJsonBytes));
 JsonWriter jsonWriter = gson.newJsonWriter(new ByteArrayOutputStream(jsonBytes.length));
 dateTypeAdapter.write(jsonWriter, date);
 jsonWriter.flush();
 jsonReader.close();
 Assert.assertArrayEquals(expectedJsonBytes, jsonBytes);
 }

 @Test
 public void testReadDate() throws IOException, ParseException {
 String dateString = "2022-12-31 23:59:59";
 JsonElement jsonElement = new JsonElement() {{
 value = new JsonPrimitive(dateString);
 }};
 Date date = dateTypeAdapter.read(new JsonReader(new ByteArrayInputStream(new byte[]{})) {
 @Override
 public JsonToken peek() throws IOException {
 return JsonToken.NULL;
 }

 @Override
 public JsonToken next() throws IOException {
 return JsonToken.STRING;
 }

 @Override
 public String nextString() throws IOException {
 return dateString;
 }
 }.asJsonReader());
 Assert.assertEquals(new Date(1671142399000L), date);
 }

 @Test
 public void testReadTimestamp() throws IOException, ParseException {
 String dateString = "2022-12-31 23:59:59.123";
 JsonElement jsonElement = new JsonElement() {{
 value = new JsonPrimitive(dateString);
 }};
 Timestamp timestamp = dateTypeAdapter.read(new JsonReader(new ByteArrayInputStream(new byte[]{})) {
 @Override
 public JsonToken peek() throws IOException {
 return JsonToken.NULL;
 }

 @Override
 public JsonToken next() throws IOException {
 return JsonToken.STRING;
 }

 @Override
 public String nextString() throws IOException {
 return dateString;
 }
 }.asJsonReader());
 Assert.assertEquals(new Timestamp(1671142399123L), timestamp);
 }

 @Test
 public void testReadSqlDate() throws IOException, ParseException {
 String dateString = "2022-12-31";
 JsonElement jsonElement = new JsonElement() {{
 value = new JsonPrimitive(dateString);
 }};
 java.sql.Date sqlDate = (java.sql.Date) dateTypeAdapter.read(new JsonReader(new ByteArrayInputStream(new byte[]{})) {
 @Override
 public JsonToken peek() throws IOException {
 return JsonToken.NULL;
 }

 @Override
 public JsonToken next() throws IOException {
 return JsonToken.STRING;
 }

 @Override
 public String nextString() throws IOException {
 return dateString;
 }
 }.asJsonReader());
 Assert.assertEquals(new java.sql.Date(1671142399000L), sqlDate);
 }

 @Test(expected = AssertionError.class)
 public void testUnknownDateType() {
 Class<?> unknownDateType = Integer.class;
 dateTypeAdapter = new DefaultDateTypeAdapter(unknownDateType, enUsFormat, localFormat);
 }
 }