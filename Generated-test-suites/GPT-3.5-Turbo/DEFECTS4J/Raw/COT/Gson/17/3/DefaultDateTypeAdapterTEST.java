import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

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

public class DefaultDateTypeAdapterTest {

    private DateFormat enUsFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
    private DateFormat localFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);

    @Test
    public void testWrite_NullDate() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, enUsFormat, localFormat);
        JsonWriter writer = new JsonWriter(null);
        
        adapter.write(writer, null);
        
        Assert.assertEquals("null", writer.toString());
    }
    
    @Test
    public void testWrite_ValidDate() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, enUsFormat, localFormat);
        JsonWriter writer = new JsonWriter(null);
        Date date = new Date();
        
        adapter.write(writer, date);
        
        Assert.assertEquals(enUsFormat.format(date), writer.toString());
    }

    @Test
    public void testRead_InvalidDateFormat() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, enUsFormat, localFormat);
        JsonReader reader = new JsonReader(null);
        reader.setLenient(false);
        reader.addToken(JsonToken.STRING);
        reader.addValue("invalid");
        
        try {
            adapter.read(reader);
            Assert.fail("Expected JsonParseException was not thrown");
        } catch (JsonParseException e) {
            // Exception caught successfully
        }
    }

    // Additional test cases can be added for other public methods in the DefaultDateTypeAdapter class

}