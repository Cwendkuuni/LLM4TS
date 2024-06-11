import static org.junit.Assert.*;
import static com.google.gson.internal.bind.util.ISO8601Utils.parse;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonReader;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonWriter;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

@RunWith(JUnit4.class)
public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapterConstructorWithDateClass() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals(Date.class, adapter.dateType);
  }

  @Test
  public void testDefaultDateTypeAdapterConstructorWithTimestampClass() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    assertEquals(Timestamp.class, adapter.dateType);
  }

  @Test
  public void testDefaultDateTypeAdapterConstructorWithDateClassAndUSLocale() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, "dd-MM-yyyy HH:mm:ss");
    assertEquals(Date.class, adapter.dateType);
  }

  @Test
  public void testDefaultDateTypeAdapterConstructorWithDateClassAndDefaultStyle() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.DEFAULT);
    assertEquals(Date.class, adapter.dateType);
  }

  @Test
  public void testDefaultDateTypeAdapterConstructorWithDateClassAndCustomDateFormat() {
    DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, customFormat);
    assertEquals(Date.class, adapter.dateType);
  }

  @Test
  public void testWriteMethod() throws IOException {
    Gson gson = new Gson();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    Date date = new Date();
    JsonWriter jsonWriter = new JsonWriter(System.out);
    adapter.write(jsonWriter, date);
    jsonWriter.flush();
    String expectedOutput = "\"2022-12-31 12:00:00\""; // Assuming the output is the date in yyyy-MM-dd HH:mm:ss format
    assertEquals(expectedOutput, System.out.getBytes().toString());
  }

  @Test
  public void testWriteMethodWithNullValue() throws IOException {
    Gson gson = new Gson();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter jsonWriter = new JsonWriter(System.out);
    adapter.write(jsonWriter, null);
    jsonWriter.flush();
    assertEquals("null", System.out.getBytes().toString());
  }

  @Test
  public void testReadMethodWithStringInputDate() throws IOException {
    Gson gson = new Gson();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String dateString = "2022-12-31 12:00:00";
    JsonReader jsonReader = new JsonReader(new StringReader(dateString));
    Date date = adapter.read(jsonReader);
    assertEquals(new Date(System.currentTimeMillis()), date); // Assuming the current date is different from the test input
  }

  @Test(expected = JsonParseException.class)
  public void testReadMethodWithInvalidStringInput() throws IOException {
    Gson gson = new Gson();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String invalidStringInput = "InvalidStringInput";
    JsonReader jsonReader = new JsonReader(new StringReader(invalidStringInput));
    adapter.read(jsonReader);
  }

  @Test
  public void testReadMethodWithNullJsonReader() throws IOException {
    Gson gson = new Gson();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader jsonReader = null;
    try {
      adapter.read(jsonReader);
      fail("Should throw an exception when JsonReader is null");
    } catch (NullPointerException e) {
      // Expected behavior
    }
  }

  @Test
  public void testDeserializeToDateMethodWithValidStringInput() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String validStringInput = "2022-12-31 12:00:00";
    Date date = adapter.deserializeToDate(validStringInput);
    assertEquals(new Date(System.currentTimeMillis()), date); // Assuming the current date is different from the test input
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDeserializeToDateMethodWithInvalidStringInput() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String invalidStringInput = "InvalidStringInput";
    adapter.deserializeToDate(invalidStringInput);
  }

  @Test
  public void testToStringMethod() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"), new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
    String expectedToStringOutput = "DefaultDateTypeAdapter(com.sun.org.apache.xerces.internal.impl.datatype.XMLGregorianCalendar)";
    String toStringOutput = adapter.toString();
    assertNotEquals(expectedToStringOutput, toStringOutput);
  }
}