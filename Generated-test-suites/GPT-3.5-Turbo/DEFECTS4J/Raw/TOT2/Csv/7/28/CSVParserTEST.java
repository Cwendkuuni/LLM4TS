import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVParserTest {

    private CSVParser csvParser;

    @Before
    public void setUp() {
        // Initialize CSVParser for testing
        csvParser = new CSVParser(new StringReader("name,age\nJohn,30\nDoe,25\n"), CSVFormat.EXCEL);
    }

    @After
    public void tearDown() {
        // Close CSVParser after testing
        try {
            csvParser.close();
        } catch (IOException e) {
            // Handle IOException
            e.printStackTrace();
        }
    }

    @Test
    public void testParseFile() {
        try {
            CSVParser parser = CSVParser.parse(new File("test.csv"), CSVFormat.EXCEL);
            assertNotNull(parser);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testParseString() {
        try {
            CSVParser parser = CSVParser.parse("name,age\nJohn,30\nDoe,25\n", CSVFormat.EXCEL);
            assertNotNull(parser);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testParseURL() {
        try {
            CSVParser parser = CSVParser.parse(new URL("http://example.com/test.csv"), Charset.defaultCharset(), CSVFormat.EXCEL);
            assertNotNull(parser);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testGetCurrentLineNumber() {
        long lineNumber = csvParser.getCurrentLineNumber();
        assertEquals(0, lineNumber);
    }

    @Test
    public void testGetRecordNumber() {
        long recordNumber = csvParser.getRecordNumber();
        assertEquals(0, recordNumber);
    }

    @Test
    public void testGetHeaderMap() {
        Map<String, Integer> headerMap = csvParser.getHeaderMap();
        assertNotNull(headerMap);
        assertEquals(2, headerMap.size());
        assertTrue(headerMap.containsKey("name"));
        assertTrue(headerMap.containsKey("age"));
    }

    // Add more test cases as needed

}