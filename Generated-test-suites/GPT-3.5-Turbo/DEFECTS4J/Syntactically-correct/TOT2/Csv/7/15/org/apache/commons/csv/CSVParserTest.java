import org.junit.Test;

import static org.junit.Assert.*;

public class CSVParserTest {

    @Test
    public void testParseFile() {
        // Test parsing a file
        File csvFile = new File("test.csv");
        CSVFormat format = CSVFormat.EXCEL;
        try {
            CSVParser parser = CSVParser.parse(csvFile, format);
            assertNotNull(parser);
        } catch (IOException e) {
            fail("Exception thrown while parsing file: " + e.getMessage());
        }
    }

    @Test
    public void testParseString() {
        // Test parsing a string
        String csvString = "name,age\nJohn,25\nAlice,30";
        CSVFormat format = CSVFormat.RFC4180;
        try {
            CSVParser parser = CSVParser.parse(csvString, format);
            assertNotNull(parser);
        } catch (IOException e) {
            fail("Exception thrown while parsing string: " + e.getMessage());
        }
    }

    @Test
    public void testParseURL() {
        // Test parsing from a URL
        URL url = new URL("http://example.com/test.csv");
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = CSVFormat.DEFAULT;
        try {
            CSVParser parser = CSVParser.parse(url, charset, format);
            assertNotNull(parser);
        } catch (IOException e) {
            fail("Exception thrown while parsing URL: " + e.getMessage());
        }
    }

    @Test
    public void testGetRecords() {
        // Test getting records from parser
        String csvString = "name,age\nJohn,25\nAlice,30";
        CSVFormat format = CSVFormat.DEFAULT;
        try {
            CSVParser parser = CSVParser.parse(csvString, format);
            List<CSVRecord> records = parser.getRecords();
            assertNotNull(records);
            assertEquals(2, records.size());
        } catch (IOException e) {
            fail("Exception thrown while getting records: " + e.getMessage());
        }
    }
}