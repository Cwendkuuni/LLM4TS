import org.junit.Test;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

public class CSVParserTest {

    // Test case for CSVParser.parse(File file, CSVFormat format)
    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        assertNotNull(parser);
    }

    // Test case for CSVParser.parse(String string, CSVFormat format)
    @Test
    public void testParseString() throws IOException {
        String csvString = "Name, Age, City\nJohn, 25, New York\nJane, 30, Los Angeles";
        CSVParser parser = CSVParser.parse(csvString, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
    }

    // Test case for CSVParser.parse(URL url, Charset charset, CSVFormat format)
    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/test.csv");
        CSVParser parser = CSVParser.parse(url, Charset.defaultCharset(), CSVFormat.DEFAULT);
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertTrue(headerMap.containsKey("Name"));
    }

    // Test case for getRecords() method
    @Test
    public void testGetRecords() throws IOException {
        String csvString = "A,B,C\n1,2,3\n4,5,6";
        CSVParser parser = CSVParser.parse(csvString, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
    }

    // Test case for nextRecord() method
    @Test
    public void testNextRecord() throws IOException {
        String csvString = "A,B\n1,2\n3,4";
        CSVParser parser = CSVParser.parse(csvString, CSVFormat.DEFAULT);
        CSVRecord record = parser.nextRecord();
        assertNotNull(record);
    }
}