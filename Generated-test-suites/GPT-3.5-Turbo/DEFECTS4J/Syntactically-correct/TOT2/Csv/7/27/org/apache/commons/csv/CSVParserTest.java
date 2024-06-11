import org.junit.Test;
import static org.junit.Assert.*;

public class CSVParserTest {
    
    @Test
    public void testParseFile() {
        // Test parsing a CSV file
        File file = new File("test.csv");
        CSVFormat format = CSVFormat.DEFAULT;
        try {
            CSVParser parser = CSVParser.parse(file, format);
            assertNotNull(parser);
        } catch (IOException e) {
            fail("Exception thrown while parsing file");
        }
    }
    
    @Test
    public void testParseString() {
        // Test parsing a CSV string
        String csvString = "Name, Age\nJohn, 30\nJane, 25";
        CSVFormat format = CSVFormat.DEFAULT;
        try {
            CSVParser parser = CSVParser.parse(csvString, format);
            assertNotNull(parser);
        } catch (IOException e) {
            fail("Exception thrown while parsing string");
        }
    }
    
    @Test
    public void testParseURL() {
        // Test parsing from a URL
        URL url = new URL("http://example.com/data.csv");
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = CSVFormat.DEFAULT;
        try {
            CSVParser parser = CSVParser.parse(url, charset, format);
            assertNotNull(parser);
        } catch (IOException e) {
            fail("Exception thrown while parsing URL");
        }
    }
    
    @Test
    public void testGetRecords() {
        // Test getting records from the parser
        String csvString = "Name, Age\nJohn, 30\nJane, 25";
        CSVFormat format = CSVFormat.DEFAULT;
        try {
            CSVParser parser = CSVParser.parse(csvString, format);
            List<CSVRecord> records = parser.getRecords();
            assertNotNull(records);
            assertEquals(2, records.size());
        } catch (IOException e) {
            fail("Exception thrown while getting records");
        }
    }

}