import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

 private DefaultDateTypeAdapter dateAdapter;
 private DefaultDateTypeAdapter dateAdapterWithUsFormat;
 private DefaultDateTypeAdapter dateAdapterWithCustomFormat;
 private DefaultDateTypeAdapter dateAdapterWithCustomDateStyle;
 private DefaultDateTypeAdapter dateAdapterWithCustomTimeStyle;
 private DefaultDateTypeAdapter dateAdapterWithCustomDateStyleAndTimeStyle;

 @Before
 public void setUp() {
 dateAdapter = new DefaultDateTypeAdapter(Date.class);
 dateAdapterWithUsFormat = new DefaultDateTypeAdapter(Date.class, "US/Pacific");
 dateAdapterWithCustomFormat = new DefaultDateTypeAdapter(Date.class, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US));
 dateAdapterWithCustomDateStyle = new DefaultDateTypeAdapter(Date.class, DateFormat.ABBREVIATED);
 dateAdapterWithCustomTimeStyle = new DefaultDateTypeAdapter(Date.class, DateFormat.SHORT);
 dateAdapterWithCustomDateStyleAndTimeStyle = new DefaultDateTypeAdapter(Date.class, DateFormat.ABBREVIATED, DateFormat.SHORT);
 }

 @Test
 public void testWrite() throws IOException {
 JsonWriter writer = new JsonWriter(System.out);
 Gson gson = new GsonBuilder().setPrettyPrinting().create();

 Date date = new Date();
 dateAdapter.write(writer, date);
 writer.flush();
 assertEquals("\"2022-12-14 12:34:56.789\"", writer.toString());

 dateAdapterWithUsFormat.write(writer, date);
 writer.flush();
 assertEquals("\"2022-12-14 12:34:56.789 Pacific Standard Time\"", writer.toString());

 dateAdapterWithCustomFormat.write(writer, date);
 writer.flush();
 assertEquals("\"14-12-2022 12:34:56.789\"", writer.toString());

 dateAdapterWithCustomDateStyle.write(writer, date);
 writer.flush();
 assertEquals("\"Dec 14, 2022\"", writer.toString());

 dateAdapterWithCustomTimeStyle.write(writer, date);
 writer.flush();
 assertEquals("\"12:34 PM\"", writer.toString());

 dateAdapterWithCustomDateStyleAndTimeStyle.write(writer, date);
 writer.flush();
 assertEquals("\"Dec 14, 12:34 PM\"", writer.toString());
 }

 @Test
 public void testReadDate() throws IOException {
 JsonReader reader = new JsonReader(new StringReader("\"2022-12-14 12:34:56.789\""));
 Gson gson = new GsonBuilder().create();

 Date date = dateAdapter.read(reader);
 assertEquals(new Date(1671099596789L), date);

 reader.setLenient(true);
 reader.nextString(); // consume the string
 date = dateAdapter.read(reader);
 assertEquals(new Date(1671099596789L), date);

 reader.setLenient(false);
 reader = new JsonReader(new StringReader("\"2022-12-14 12:34:56.789 Pacific Standard Time\""));
 date = dateAdapterWithUsFormat.read(reader);
 assertEquals(new Date(1671099596789L), date);

 reader.setLenient(false);
 reader = new JsonReader(new StringReader("\"14-12-2022 12:34:56.789\""));
 date = dateAdapterWithCustomFormat.read(reader);
 assertEquals(new Date(1671099596789L), date);
 }

 @Test(expected = JsonParseException.class)
 public void testReadInvalidDate() throws IOException {
 JsonReader reader = new JsonReader(new StringReader("\"invalid_date\""));
 reader.setLenient(false);
 dateAdapter.read(reader);
 }

 @Test(expected = JsonParseException.class)
 public void testReadNullDate() throws IOException {
 JsonReader reader = new JsonReader(new StringReader("null"));
 dateAdapter.read(reader);
 }

 @Test
 public void testReadTimestamp() throws IOException {
 JsonReader reader = new JsonReader(new StringReader("\"2022-12-14 12:34:56.789\""));
 Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, new DefaultDateTypeAdapter(Timestamp.class)).create();

 Timestamp timestamp = gson.fromJson(reader, Timestamp.class);
 assertEquals(new Timestamp(1671099596789L), timestamp);
 }

 @Test
 public void testReadSqlDate() throws IOException {
 JsonReader reader = new JsonReader(new StringReader("\"2022-12-14\""));
 Gson gson = new GsonBuilder().registerTypeAdapter(java.sql.Date.class, new DefaultDateTypeAdapter(java.sql.Date.class)).create();

 java.sql.Date sqlDate = gson.fromJson(reader, java.sql.Date.class);
 assertEquals(new java.sql.Date(1671099596000L), sqlDate);
 }
}