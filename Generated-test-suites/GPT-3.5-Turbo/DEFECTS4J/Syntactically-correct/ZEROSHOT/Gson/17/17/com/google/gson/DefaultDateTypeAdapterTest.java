import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {
  
  @Test
  public void testWrite_NullValue() throws IOException {
    JsonWriter writer = new JsonWriter();
    Date value = null;
    
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.write(writer, value);
    
    String expectedResult = "null";
    String actualResult = writer.toString();
    
    Assert.assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testWrite_NonNullValue() throws IOException {
    JsonWriter writer = new JsonWriter();
    Date value = new Date();
    
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.write(writer, value);
    
    DateFormat enUsFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
    String expectedResult = enUsFormat.format(value);
    String actualResult = writer.toString();
    
    Assert.assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testRead_ValidDateString() throws IOException {
    String dateString = "2020-01-01 12:00:00";
    JsonReader reader = new JsonReader();
    reader.beginObject();
    reader.name("date").value(dateString);
    reader.endObject();
    
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    Date expectedResult = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
    Date actualResult = adapter.read(reader);
    
    Assert.assertEquals(expectedResult, actualResult);
  }
  
  @Test(expected = JsonParseException.class)
  public void testRead_InvalidDateString() throws IOException {
    String dateString = "2020-01-01";
    JsonReader reader = new JsonReader();
    reader.beginObject();
    reader.name("date").value(dateString);
    reader.endObject();
    
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.read(reader);
  }
  
  @Test
  public void testDeserializeToDate_ValidDateString() {
    String dateString = "2020-01-01 12:00:00";
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    
    Date expectedResult = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
    Date actualResult = adapter.deserializeToDate(dateString);
    
    Assert.assertEquals(expectedResult, actualResult);
  }
  
  @Test(expected = JsonSyntaxException.class)
  public void testDeserializeToDate_InvalidDateString() {
    String dateString = "2020-01-01";
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    
    adapter.deserializeToDate(dateString);
  }
  
  @Test
  public void testToString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    
    String expectedResult = "DefaultDateTypeAdapter(SimpleDateFormat)";
    String actualResult = adapter.toString();
    
    Assert.assertEquals(expectedResult, actualResult);
  }
}