import static org.junit.Assert.*;
import org.junit.Test;

public class CSVParserTest {

    @Test
    public void testParseFile() {
        // Test parsing a CSV file
        try {
            CSVParser parser = CSVParser.parse(new File("test.csv"), CSVFormat.EXCEL);
            assertNotNull(parser);
        } catch (IOException e) {
            fail("Exception thrown while parsing CSV file");
        }
    }

    @Test
    public void testParseString() {
        // Test parsing a CSV string
        try {
            CSVParser parser = CSVParser.parse("value1,value2,value3", CSVFormat.EXCEL);
            assertNotNull(parser);
        } catch (IOException e) {
            fail("Exception thrown while parsing CSV string");
        }
    }

    @Test
    public void testParseURL() {
        // Test parsing from a URL
        try {
            URL url = new URL("https://example.com/test.csv");
            CSVParser parser = CSVParser.parse(url, Charset.forName("UTF-8"), CSVFormat.EXCEL);
            assertNotNull(parser);
        } catch (IOException e) {
            fail("Exception thrown while parsing CSV from URL");
        }
    }

    @Test
    public void testGetRecords() {
        // Test retrieving records from parser
        try {
            CSVParser parser = CSVParser.parse("value1,value2,value3\nvalue4,value5,value6", CSVFormat.EXCEL);
            List<CSVRecord> records = parser.getRecords();
            assertNotNull(records);
            assertEquals(2, records.size());
        } catch (IOException e) {
            fail("Exception thrown while getting records");
        }
    }

    @Test
    public void testCloseParser() {
        // Test closing the parser
        try {
            CSVParser parser = CSVParser.parse("value1,value2,value3", CSVFormat.EXCEL);
            parser.close();
            assertTrue(parser.isClosed());
        } catch (IOException e) {
            fail("Exception thrown while closing parser");
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextRecordOnClosedParser() {
        // Test calling nextRecord on a closed parser
        CSVParser parser = null;
        try {
            parser = CSVParser.parse("value1,value2,value3", CSVFormat.EXCEL);
            parser.close();
        } catch (IOException e) {
            fail("Exception thrown while closing parser");
        }

        parser.nextRecord();
    }
}