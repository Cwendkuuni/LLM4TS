import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class DefaultDateTypeAdapterTest {

 private static final String SIMPLE_NAME = "DefaultDateTypeAdapter";
 private static final DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
 private static final DefaultDateTypeAdapter dateAdapterWithUsFormat = new DefaultDateTypeAdapter(Date.class, "dd-MM-yyyy HH:mm:ss.SSS z");
 private static final DefaultDateTypeAdapter dateAdapterWithLocalFormat = new DefaultDateTypeAdapter(Date.class, "dd-MM-yyyy HH:mm:ss.SSS", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS", Locale.US));
 private static final DefaultDateTypeAdapter dateAdapterWithInvalidFormat = new DefaultDateTypeAdapter(Date.class, "dd-MM-yyyy HH:mm:ss.SSSXXX");
 private static final DefaultDateTypeAdapter dateAdapterWithNull = new DefaultDateTypeAdapter(Date.class);

 @Test
 public void testDefaultConstructor() {
 dateTypeMatches(dateAdapter, Date.class);
 }

 @Test
 public void testConstructorWithDateType() {
 dateTypeMatches(dateAdapterWithUsFormat, Date.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidDateType() {
 new DefaultDateTypeAdapter(Integer.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
 }

 @Test
 public void testWriteNullValue() {
 writeReadRoundTrip(null, dateAdapter);
 }

 @Test
 public void testWriteAndReadDateFormatUs() {
 Date date = new Date();
 writeReadRoundTrip(date.toString(), dateAdapterWithUsFormat);
 }

 @Test
 public void testWriteAndReadDateFormatLocal() {
 Date date = new Date();
 writeReadRoundTrip(localFormatString(date), dateAdapterWithLocalFormat);
 }

 @Test
 public void testWriteAndReadDateFormatISO8601() {
 Date date = new Date();
 writeReadRoundTrip(ISO8601Utils.format(date), dateAdapter);
 }

 @Test(expected = JsonParseException.class)
 public void testReadInvalidJsonString() {
 JsonElement element = new JsonParser().parse("{\"date\":\"abcdefg\"}");
 readRoundTrip(element);
 }

 @Test(expected = JsonSyntaxException.class)
 public void testReadInvalidDateString() {
 readRoundTrip("abcdefg");
 }

 @Test(expected = JsonSyntaxException.class)
 public void testReadDateStringWithInvalidFormat() {
 Date date = new Date();
 readRoundTrip(localFormatString(date) + "XXX");
 }

 @Test(expected = JsonSyntaxException.class)
 public void testReadDateStringWithInvalidFormatAndLocale() {
 Date date = new Date();
 readRoundTrip(localFormatString(date) + "XXX", new Locale("en", "US"));
 }

 private void writeReadRoundTrip(String dateString, DefaultDateTypeAdapter adapter) {
 writeReadRoundTrip(dateString, adapter, null);
 }

 private void writeReadRoundTrip(String dateString, DefaultDateTypeAdapter adapter, Locale locale) {
 Date date = adapter.read(new JsonReader(new StringReader(dateString)));
 String dateStringRoundTrip = adapter.toString().writeToString(new JsonWriter(new StringWriter())).get(0).getAsString();
 assertEquals(dateString, dateStringRoundTrip);
}

 private void dateTypeMatches(DefaultDateTypeAdapter adapter, Class<? extends Date> dateType) {
 assertEquals(dateType, adapter.dateType);
}

 private SimpleDateFormat localFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
 private StringWriter stringWriter = new StringWriter();
 private StringReader stringReader = new StringReader(" ");

}