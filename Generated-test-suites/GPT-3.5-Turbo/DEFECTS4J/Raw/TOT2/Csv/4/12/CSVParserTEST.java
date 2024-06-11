import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.EXCEL);
        List<CSVRecord> records = parser.getRecords();
        
        assertFalse(records.isEmpty());
        assertEquals(2, records.size());
        
        // Add more assertions as needed
    }

    @Test
    public void testParseString() throws IOException {
        String data = "name,age\nJohn,25\nAlice,30";
        CSVParser parser = CSVParser.parse(data, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        
        assertFalse(records.isEmpty());
        assertEquals(2, records.size());
        
        // Add more assertions as needed
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/test.csv");
        CSVParser parser = CSVParser.parse(url, Charset.forName("UTF-8"), CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        
        assertFalse(records.isEmpty());
        
        // Add more assertions as needed
    }

    // Add more test cases for other methods in CSVParser class

}