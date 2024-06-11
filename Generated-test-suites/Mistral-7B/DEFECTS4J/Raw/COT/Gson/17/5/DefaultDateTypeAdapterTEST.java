import static org.junit.Assert.*;
import com.google.gson.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

 private DefaultDateTypeAdapter dateAdapter;

 @Before
 public void setup() {
 dateAdapter = new DefaultDateTypeAdapter(Date.class);
 }

 @Test
 public void testDefaultConstructor() {
 // Test default constructor
 dateAdapter = new DefaultDateTypeAdapter();
 assertEquals(Date.class, dateAdapter.dateType);
 assertNotNull(dateAdapter.enUsFormat);
 assertNotNull(dateAdapter.localFormat);
 }

 @Test
 public void testDateWithUSFormatConstructor() {
 // Test constructor with US format
 dateAdapter = new DefaultDateTypeAdapter(Date.class, "dd/MM/yyyy hh:mm a");
 assertEquals(Date.class, dateAdapter.dateType);
 assertNotNull(dateAdapter.enUsFormat);
 assertNotNull(dateAdapter.localFormat);
 SimpleDateFormat expectedFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US);
 assertEquals(expectedFormat.toString(), dateAdapter.enUsFormat.toString());
 }

 @Test
 public void testDateWithLocalFormatConstructor() {
 // Test constructor with local format
 dateAdapter = new DefaultDateTypeAdapter(Date.class, DateFormat.SHORT, DateFormat.SHORT);
 assertEquals(Date.class, dateAdapter.dateType);
 assertNotNull(dateAdapter.enUsFormat);
 assertNotNull(dateAdapter.localFormat);
 DateFormat expectedFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
 assertEquals(expectedFormat.toString(), dateAdapter.localFormat.toString());
 }

 @Test
 public void testDateStyleAndTimeStyleConstructor() {
 // Test constructor with specific date and time styles
 dateAdapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
 assertEquals(Date.class, dateAdapter.dateType);
 assertNotNull(dateAdapter.enUsFormat);
 assertNotNull(dateAdapter.localFormat);
 DateFormat expectedFormat = DateFormat.getDateTimeInstance(Calendar.SHORT, Calendar.SHORT);
 assertEquals(expectedFormat.toString(), dateAdapter.enUsFormat.toString());
 }

 @Test
 public void testWrite() throws IOException {
 // Test write method
 Date date = new Date();
 String expectedJson = "\"" + dateAdapter.enUsFormat.format(date) + "\"";
 JsonWriter writer = new JsonWriter(System.out);
 dateAdapter.write(writer, date);
 assertEquals(expectedJson, writer.toString());
 }

 @Test
 public void testRead() throws IOException, ParseException {
 // Test read method with valid date strings
 Date expectedDate = new Date();
 String inputJson = "\"" + dateAdapter.enUsFormat.format(expectedDate) + "\"";
 JsonReader reader = new JsonReader(new StringReader(inputJson));
 Date date = dateAdapter.read(reader);
 assertEquals(expectedDate, date);

 // Test read method with date strings in different formats
 SimpleDateFormat simpleFormat = new SimpleDateFormat("dd-MM-yy hh:mm a");
 expectedDate = simpleFormat.parse("01-01-20 12:00 PM");
 inputJson = "\"" + simpleFormat.format(expectedDate) + "\"";
 reader = new JsonReader(new StringReader(inputJson));
 date = dateAdapter.read(reader);
 assertEquals(expectedDate, date);
 }

 @Test(expected = JsonParseException.class)
 public void testReadWithNullJson() throws IOException {
 // Test read method with null JSON
 JsonReader reader = new JsonReader(new StringReader(null));
 dateAdapter.read(reader);
 }

 @Test(expected = JsonParseException.class)
 public void testReadWithInvalidJson() throws IOException, ParseException {
 // Test read method with invalid JSON
 JsonReader reader = new JsonReader(new StringReader("123"));
 reader.setLenient(true);
 dateAdapter.read(reader);
 }

 @Test(expected = JsonParseException.class)
 public void testReadWithDateFormatException() throws IOException, ParseException, SQLException {
 // Test read method with date format exception
 SimpleDateFormat invalidFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
 String invalidJson = "\"" + invalidFormat.format(new Date()) + "\"";
 JsonReader reader = new JsonReader(new StringReader(invalidJson));
 dateAdapter.read(reader);
 }
}