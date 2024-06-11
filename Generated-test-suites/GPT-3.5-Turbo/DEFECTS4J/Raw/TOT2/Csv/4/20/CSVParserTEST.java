import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.csv.*;

public class CSVParserTest {

    @Test
    public void testParseFile() {
        // Test parsing a CSV file with RFC4180 format
        File file = new File("test.csv");
        CSVFormat format = CSVFormat.RFC4180;
        try {
            CSVParser parser = CSVParser.parse(file, format);
            assertNotNull(parser);
        } catch (IOException e) {
            fail("Exception thrown while parsing file");
        }
    }

    @Test
    public void testParseString() {
        // Test parsing a CSV string with RFC4180 format
        String csvString = "a,b,c\n1,2,3";
        CSVFormat format = CSVFormat.RFC4180;
        try {
            CSVParser parser = CSVParser.parse(csvString, format);
            assertNotNull(parser);
        } catch (IOException e) {
            fail("Exception thrown while parsing string");
        }
    }

    @Test
    public void testParseURL() {
        // Test parsing a CSV from a URL with Excel format
        URL url = new URL("http://example.com/test.csv");
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = CSVFormat.EXCEL;
        try {
            CSVParser parser = CSVParser.parse(url, charset, format);
            assertNotNull(parser);
        } catch (IOException e) {
            fail("Exception thrown while parsing URL");
        }
    }

    @Test
    public void testGetRecords() {
        // Test getting records from CSVParser
        String csvString = "a,b,c\n1,2,3\n4,5,6";
        CSVFormat format = CSVFormat.RFC4180;
        try {
            CSVParser parser = CSVParser.parse(new StringReader(csvString), format);
            List<CSVRecord> records = parser.getRecords();
            assertNotNull(records);
            assertEquals(2, records.size());
        } catch (IOException e) {
            fail("Exception thrown while getting records");
        }
    }

    @Test
    public void testIOException() {
        // Test IOException when creating parser
        File file = new File("invalid.csv");
        CSVFormat format = CSVFormat.EXCEL;
        try {
            CSVParser parser = CSVParser.parse(file, format);
            fail("IOException not thrown");
        } catch (IOException e) {
            assertTrue(true);
        }
    }
}