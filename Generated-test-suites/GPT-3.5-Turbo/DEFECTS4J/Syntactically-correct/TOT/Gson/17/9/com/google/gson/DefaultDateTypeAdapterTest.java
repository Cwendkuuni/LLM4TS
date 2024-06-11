import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DefaultDateTypeAdapterTest {

    private DefaultDateTypeAdapter adapter;
    
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    
    @Before
    public void setup() {
        adapter = new DefaultDateTypeAdapter(Date.class);
    }

    @Test
    public void testWrite_NullDate_ShouldWriteNullValue() {
        // Arrange
        MockJsonWriter writer = new MockJsonWriter();
        
        // Act
        adapter.write(writer, null);
        
        // Assert
        assertEquals(JsonToken.NULL, writer.getCurrentToken());
    }
    
    @Test
    public void testWrite_DateValue_ShouldWriteFormattedDate() {
        // Arrange
        MockJsonWriter writer = new MockJsonWriter();
        Date date = Date.valueOf("2021-01-20");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        
        // Act
        adapter.write(writer, date);
        
        // Assert
        assertEquals(JsonToken.STRING, writer.getCurrentToken());
        assertEquals(sdf.format(date), writer.getCurrentValue());
    }
    
    @Test
    public void testRead_NonStringValue_ShouldThrowException() throws IOException {
        // Arrange
        MockJsonReader reader = new MockJsonReader();
        reader.setNextToken(JsonToken.INTEGER);
        
        // Assert
        exceptionRule.expect(JsonParseException.class);
        exceptionRule.expectMessage("The date should be a string value");
        
        // Act
        adapter.read(reader);
    }
    
    @Test
    public void testRead_InvalidDateFormat_ShouldThrowException() throws IOException {
        // Arrange
        MockJsonReader reader = new MockJsonReader();
        reader.setNextToken(JsonToken.STRING);
        reader.setNextValue("2021-20-01");
        
        // Assert
        exceptionRule.expect(JsonSyntaxException.class);
        exceptionRule.expectMessage("2021-20-01");
        
        // Act
        adapter.read(reader);
    }
    
    @Test
    public void testRead_ValidDateValue_ShouldReturnParsedDate() throws IOException {
        // Arrange
        MockJsonReader reader = new MockJsonReader();
        reader.setNextToken(JsonToken.STRING);
        reader.setNextValue("2021-01-20");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        
        // Act
        Date date = adapter.read(reader);
        
        // Assert
        assertEquals("2021-01-20", sdf.format(date));
    }
    
    @Test
    public void testRead_ValidTimestampValue_ShouldReturnParsedTimestamp() throws IOException {
        // Arrange
        MockJsonReader reader = new MockJsonReader();
        reader.setNextToken(JsonToken.STRING);
        reader.setNextValue("2021-01-20 10:30:00");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        
        // Act
        Timestamp timestamp = adapter.read(reader);
        
        // Assert
        assertEquals("2021-01-20 10:30:00", sdf.format(timestamp));
    }
    
    @Test
    public void testRead_ValidSqlDateValue_ShouldReturnParsedSqlDate() throws IOException {
        // Arrange
        MockJsonReader reader = new MockJsonReader();
        reader.setNextToken(JsonToken.STRING);
        reader.setNextValue("2021-01-20");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        
        // Act
        Date sqlDate = adapter.read(reader);
        
        // Assert
        assertEquals("2021-01-20", sdf.format(sqlDate));
    }
    
    private class MockJsonWriter extends JsonWriter {
        private JsonToken currentToken;
        private String currentValue;
        
        public MockJsonWriter() {
            super(null);
        }
        
        @Override
        public void value(String value) {
            currentToken = JsonToken.STRING;
            currentValue = value;
        }
        
        @Override
        public void nullValue() {
            currentToken = JsonToken.NULL;
        }
        
        public JsonToken getCurrentToken() {
            return currentToken;
        }
        
        public String getCurrentValue() {
            return currentValue;
        }
    }
    
    private class MockJsonReader extends JsonReader {
        private JsonToken currentToken;
        private String currentValue;
        
        public MockJsonReader() {
            super(null);
        }
        
        public void setNextToken(JsonToken token) {
            currentToken = token;
        }
        
        public void setNextValue(String value) {
            currentValue = value;
        }
        
        @Override
        public JsonToken peek() throws IOException {
            return currentToken;
        }
        
        @Override
        public String nextString() throws IOException {
            return currentValue;
        }
    }
}