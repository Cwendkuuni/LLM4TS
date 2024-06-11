import com.google.gson.DefaultDateTypeAdapter;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DefaultDateTypeAdapterTest {

 private DefaultDateTypeAdapter dateTypeAdapter;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 }

 @Test
 public void testDefaultConstructor() {
 dateTypeAdapter = new DefaultDateTypeAdapter();
 assertEquals(Date.class, dateTypeAdapter.dateType);
 }

 @Test
 public void testDateConstructor() {
 dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 assertEquals(Date.class, dateTypeAdapter.dateType);
 }

 @Test
 public void testStringToDate_ValidDate_USFormat() throws Exception {
 String dateString = "2022-01-01 12:00:00";
 Date expectedDate = new Date(1641312000000L); //2022-01-01 12:00:00 in milliseconds
 Date actualDate = dateTypeAdapter.read(new JsonReader(new StringReader(dateString)));
 assertEquals(expectedDate, actualDate);
 }

 @Test
 public void testStringToDate_ValidDate_LocalFormat() throws Exception {
 String dateString = "01.01.2022."; //assuming local format is d.M.yyyy.
 Date expectedDate = new Date(1641312000000L); //2022-01-01 12:00:00 in milliseconds
 Date actualDate = dateTypeAdapter.read(new JsonReader(new StringReader(dateString)));
 assertEquals(expectedDate, actualDate);
 }

 @Test
 public void testStringToDate_ValidDate_ISO8601Format() throws Exception {
 String dateString = "2022-01-01T12:00:00.000Z";
 Date expectedDate = new Date(1641312000000L); //2022-01-01 12:00:00 in milliseconds
 Date actualDate = dateTypeAdapter.read(new JsonReader(new StringReader(dateString)));
 assertEquals(expectedDate, actualDate);
 }

 @Test(expected = JsonParseException.class)
 public void testStringToDate_InvalidFormat_USFormat() throws Exception {
 String dateString = "InvalidFormat";
 dateTypeAdapter.read(new JsonReader(new StringReader(dateString)));
 }

 @Test(expected = JsonParseException.class)
 public void testStringToDate_InvalidFormat_LocalFormat() throws Exception {
 String dateString = "InvalidFormat";
 dateTypeAdapter = new DefaultDateTypeAdapter("", new SimpleDateFormat(""));
 dateTypeAdapter.read(new JsonReader(new StringReader(dateString)));
 }

 @Test(expected = JsonSyntaxException.class)
 public void testStringToDate_InvalidDate() throws Exception {
 String dateString = "29-02-2022"; //Invalid date for non-leap year
 dateTypeAdapter.read(new JsonReader(new StringReader(dateString)));
 }

 @Test
 public void testWriteReadRoundTrip() throws Exception {
 String originalJson = "\"2022-01-01 12:00:00\"";
 String expectedJson = "\"2022-01-01 12:00:00\"";
 Date date = new Date(1641312000000L); //2022-01-01 12:00:00 in milliseconds
 JsonElement jsonElement = JsonParser.parseString(originalJson).getAsJsonElement();
 Date deserializedDate = dateTypeAdapter.read(jsonElement.getAsJsonPrimitive().getAsString());
 assertEquals(date, deserializedDate);
 String serializedJson = dateTypeAdapter.toString().replaceAll("\\[|\\]", "").replaceAll("\\s+", " ");
 assertEquals(expectedJson, serializedJson);
 }

 @Test
 public void testWriteReadRoundTrip_Timestamp() throws Exception {
 long timestamp = 1641312000000L; //2022-01-01 12:00:00 in milliseconds
 Timestamp expectedTimestamp = new Timestamp(timestamp);
 JsonElement jsonElement = JsonParser.parseString("\"2022-01-01 12:00:00\"").getAsJsonElement();
 Timestamp deserializedTimestamp = (Timestamp) dateTypeAdapter.read(jsonElement.getAsJsonPrimitive().getAsString());
 assertEquals(expectedTimestamp, deserializedTimestamp);
 String serializedTimestamp = dateTypeAdapter.toString().replaceAll("\\[|\\]", "").replaceAll("\\s+", " ");
 assertEquals("\"2022-01-01 12:00:00\"", serializedTimestamp);
 }

 @Test
 public void testWriteReadRoundTrip_SQLDate() throws Exception {
 long sqlDateMilliseconds = 1641312000000L; //2022-01-01 00:00:00 in milliseconds
 java.sql.Date expectedSqlDate = new java.sql.Date(sqlDateMilliseconds);
 JsonElement jsonElement = JsonParser.parseString("\"2022-01-01\"").getAsJsonElement();
 java.sql.Date deserializedSqlDate = (java.sql.Date) dateTypeAdapter.read(jsonElement.getAsJsonPrimitive().getAsString());
 assertEquals(expectedSqlDate, deserializedSqlDate);
 String serializedSqlDate = dateTypeAdapter.toString().replaceAll("\\[|\\]", "").replaceAll("\\s+", " ");
 assertEquals("\"2022-01-01\"", serializedSqlDate);
 }

 @Test(expected = AssertionError.class)
 public void testWriteReadRoundTrip_InvalidType() throws Exception {
 Date date = new Date(1641312000000L); //2022-01-01 12:00:00 in milliseconds
 JsonElement jsonElement = JsonParser.parseString("\"InvalidType\"").getAsJsonElement();
 dateTypeAdapter.write(null, date);
 dateTypeAdapter.read(jsonElement.getAsJsonPrimitive().getAsString());
 }

}