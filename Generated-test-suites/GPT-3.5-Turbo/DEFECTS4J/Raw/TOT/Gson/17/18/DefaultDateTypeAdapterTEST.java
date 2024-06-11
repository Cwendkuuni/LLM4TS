import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DefaultDateTypeAdapterTest {

    private DefaultDateTypeAdapter defaultDateTypeAdapter;

    @Mock
    private DateFormat mockedFormat;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    }

    @Test
    public void write_WithValidDate_ShouldWriteDateAsString() throws Exception {
        // Arrange
        Date date = new Date(1628304671000L);
        String expectedDateFormat = "Updated Date";
        
        when(mockedFormat.format(date)).thenReturn(expectedDateFormat);

        // Act
        defaultDateTypeAdapter.write(mockedJsonWriter, date);

        // Assert
        verify(mockedJsonWriter).value(expectedDateFormat);
    }

    @Test
    public void write_WithNullDate_ShouldWriteNullValue() throws Exception {
        // Arrange
        Date date = null;

        // Act
        defaultDateTypeAdapter.write(mockedJsonWriter, date);

        // Assert
        verify(mockedJsonWriter).nullValue();
    }

    @Test
    public void read_WithValidDateString_ShouldReturnDate() throws Exception {
        // Arrange
        String dateString = "2021-08-07 12:00:00";
        Date expectedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(dateString);

        when(mockedJsonReader.peek()).thenReturn(JsonToken.STRING);
        when(mockedJsonReader.nextString()).thenReturn(dateString);

        // Act
        Date result = defaultDateTypeAdapter.read(mockedJsonReader);

        // Assert
        assertEquals(expectedDate, result);
    }
    
    @Test(expected = JsonParseException.class)
    public void read_WithInvalidValue_ShouldThrowJsonParseException() throws Exception {
        // Arrange
        String invalidValue = "Invalid Date Value";

        when(mockedJsonReader.peek()).thenReturn(JsonToken.STRING);
        when(mockedJsonReader.nextString()).thenReturn(invalidValue);

        // Act
        defaultDateTypeAdapter.read(mockedJsonReader);

        // Assert (Exception expected)
    }
    
    @Test
    public void read_WithValidDateStringAndDateTypeIsTimestamp_ShouldReturnTimestamp() throws Exception {
        // Arrange
        String dateString = "2021-08-07 12:00:00";
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(dateString);
        Timestamp expectedTimestamp = new Timestamp(date.getTime());

        when(mockedJsonReader.peek()).thenReturn(JsonToken.STRING);
        when(mockedJsonReader.nextString()).thenReturn(dateString);
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);
        
        // Act
        Timestamp result = (Timestamp) defaultDateTypeAdapter.read(mockedJsonReader);

        // Assert
        assertEquals(expectedTimestamp, result);
    }
    
    @Test
    public void read_WithValidDateStringAndDateTypeIsSqlDate_ShouldReturnSqlDate() throws Exception {
        // Arrange
        String dateString = "2021-08-07";
        Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateString);
        java.sql.Date expectedSqlDate = new java.sql.Date(date.getTime());

        when(mockedJsonReader.peek()).thenReturn(JsonToken.STRING);
        when(mockedJsonReader.nextString()).thenReturn(dateString);
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        
        // Act
        java.sql.Date result = (java.sql.Date) defaultDateTypeAdapter.read(mockedJsonReader);

        // Assert
        assertEquals(expectedSqlDate, result);
    }
    
    @Test(expected = AssertionError.class)
    public void read_WithInvalidDateType_ShouldThrowAssertionError() throws Exception {
        // Arrange
        String dateString = "2021-08-07";
        Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateString);

        when(mockedJsonReader.peek()).thenReturn(JsonToken.STRING);
        when(mockedJsonReader.nextString()).thenReturn(dateString);
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(String.class);
        
        // Act
        defaultDateTypeAdapter.read(mockedJsonReader);

        // Assert (Exception expected)
    }
  
}