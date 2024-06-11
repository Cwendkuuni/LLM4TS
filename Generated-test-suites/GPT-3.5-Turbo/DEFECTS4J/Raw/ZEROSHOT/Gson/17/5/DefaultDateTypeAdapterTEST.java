import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

    private DefaultDateTypeAdapter adapter;
  
    @Before
    public void setup() {
        adapter = new DefaultDateTypeAdapter(Date.class);
    }
  
    @Test
    public void write_NullValue_WritesNull() throws Exception {
        // Arrange
        JsonWriter writer = new JsonWriter(new StringWriter());
      
        // Act
        adapter.write(writer, null);
        writer.close();
      
        // Assert
        assertEquals("null", writer.toString());
    }
    
    @Test
    public void write_DateValue_WritesFormattedDate() throws Exception {
        // Arrange
        Date date = new Date();
        DateFormat enUsFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        DateFormat localFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
        String expectedDate = enUsFormat.format(date);
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
      
        // Act
        adapter.write(writer, date);
        writer.close();
        String actualDate = stringWriter.toString();
      
        // Assert
        assertEquals(expectedDate, actualDate);
    }
    
    @Test
    public void read_NonStringValue_ThrowsJsonParseException() throws Exception {
        // Arrange
        JsonReader reader = new JsonReader(new StringReader("123"));
      
        // Act and Assert
        assertThrows(JsonParseException.class, () -> adapter.read(reader));
    }
    
    @Test
    public void read_DateStringValue_ReturnsDateObject() throws Exception {
        // Arrange
        String dateString = "2021-10-01T10:30:00";
        DateFormat enUsFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        DateFormat localFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
        Date expectedDate = localFormat.parse(dateString);
        JsonReader reader = new JsonReader(new StringReader("\""+dateString+"\""));
      
        // Act
        Date actualDate = adapter.read(reader);
      
        // Assert
        assertEquals(expectedDate, actualDate);
    }
    
    @Test
    public void read_NullDateType_ReturnsDateObject() throws Exception {
        // Arrange
        String dateString = "2021-10-01T10:30:00";
        DateFormat enUsFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        DateFormat localFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
        Date expectedDate = localFormat.parse(dateString);
        JsonReader reader = new JsonReader(new StringReader("\""+dateString+"\""));
        adapter = new DefaultDateTypeAdapter(null, enUsFormat, localFormat);
      
        // Act
        Date actualDate = adapter.read(reader);
      
        // Assert
        assertEquals(expectedDate, actualDate);
    }
    
    @Test
    public void read_TimestampDateType_ReturnsTimestampObject() throws Exception {
        // Arrange
        String dateString = "2021-10-01T10:30:00";
        DateFormat enUsFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        DateFormat localFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
        Date date = localFormat.parse(dateString);
        Timestamp expectedTimestamp = new Timestamp(date.getTime());
        JsonReader reader = new JsonReader(new StringReader("\""+dateString+"\""));
        adapter = new DefaultDateTypeAdapter(Timestamp.class, enUsFormat, localFormat);
      
        // Act
        Timestamp actualTimestamp = adapter.read(reader);
      
        // Assert
        assertEquals(expectedTimestamp, actualTimestamp);
    }
    
    @Test
    public void read_SqlDateDateType_ReturnsSqlDateObject() throws Exception {
        // Arrange
        String dateString = "2021-10-01T10:30:00";
        DateFormat enUsFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        DateFormat localFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
        Date date = localFormat.parse(dateString);
        java.sql.Date expectedSqlDate = new java.sql.Date(date.getTime());
        JsonReader reader = new JsonReader(new StringReader("\""+dateString+"\""));
        adapter = new DefaultDateTypeAdapter(java.sql.Date.class, enUsFormat, localFormat);
      
        // Act
        java.sql.Date actualSqlDate = adapter.read(reader);
      
        // Assert
        assertEquals(expectedSqlDate, actualSqlDate);
    }
  
    @Test
    public void deserializeToDate_ValidDateString_ReturnsDateObject() throws Exception {
        // Arrange
        String dateString = "2021-10-01T10:30:00";
      
        // Act
        Date actualDate = adapter.deserializeToDate(dateString);
      
        // Assert
        assertNotNull(actualDate);
    }
  
    @Test
    public void toString_ReturnsExpectedStringRepresentation() {
        // Arrange
        String expectedString = "DefaultDateTypeAdapter(SimpleDateFormat)";
      
        // Act
        String actualString = adapter.toString();
      
        // Assert
        assertEquals(expectedString, actualString);
    }
}