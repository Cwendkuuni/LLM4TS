import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVParserTest {
    private static final String TEST_CSV_STRING = "Name,Age,Gender\nJohn,25,Male\nJane,30,Female";
    
    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        // Create a CSVParser with a custom format
        CSVFormat format = CSVFormat.EXCEL;
        CSVParser parser = CSVParser.parse(file, format);
        
        // Check if parser is not null
        assertNotNull(parser);
        
        // Get records from the parser
        List<CSVRecord> records = parser.getRecords();
        
        // Check if records are not null
        assertNotNull(records);
        
        // Check if the number of records is correct
        assertEquals(2, records.size());
    }
    
    @Test
    public void testParseString() throws IOException {
        StringReader stringReader = new StringReader(TEST_CSV_STRING);
        // Create a CSVParser with a custom format
        CSVFormat format = CSVFormat.EXCEL;
        CSVParser parser = CSVParser.parse(stringReader, format);
        
        // Check if parser is not null
        assertNotNull(parser);
        
        // Get records from the parser
        List<CSVRecord> records = parser.getRecords();
        
        // Check if records are not null
        assertNotNull(records);
        
        // Check if the number of records is correct
        assertEquals(2, records.size());
    }
    
    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("https://example.com/test.csv");
        Charset charset = Charset.defaultCharset();
        // Create a CSVParser with a custom format
        CSVFormat format = CSVFormat.EXCEL;
        CSVParser parser = CSVParser.parse(url, charset, format);
        
        // Check if parser is not null
        assertNotNull(parser);
        
        // Get records from the parser
        List<CSVRecord> records = parser.getRecords();
        
        // Check if records are not null
        assertNotNull(records);
        
        // Check if the number of records is correct
        assertEquals(2, records.size());
    }

    @Test
    public void testInvalidFile() {
        try {
            File file = new File("invalidFile.csv");
            CSVFormat format = CSVFormat.EXCEL;
            CSVParser parser = CSVParser.parse(file, format);
            fail("Expected IOException was not thrown");
        } catch (IOException e) {
            // Expected IOException
        }
    }

    @Test
    public void testInvalidString() {
        try {
            String invalidString = null;
            CSVFormat format = CSVFormat.EXCEL;
            CSVParser parser = CSVParser.parse(invalidString, format);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
        } catch (IOException e) {
            fail("Unexpected IOException was thrown");
        }
    }

    @Test
    public void testInvalidURL() {
        try {
            URL url = new URL("https://example.com/invalid.csv");
            Charset charset = Charset.defaultCharset();
            CSVFormat format = CSVFormat.EXCEL;
            CSVParser parser = CSVParser.parse(url, charset, format);
            fail("Expected IOException was not thrown");
        } catch (IOException e) {
            // Expected IOException
        }
    }
    
}