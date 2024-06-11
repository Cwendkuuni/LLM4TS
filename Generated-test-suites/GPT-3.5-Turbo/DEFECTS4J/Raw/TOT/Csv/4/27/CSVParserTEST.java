import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

public class CSVParserTest {

    private final String testFile = "test.csv";

    // Test parse(File file, CSVFormat format) method
    @Test
    public void testParseFile() throws Exception {
        CSVParser parser = CSVParser.parse(new File(testFile), CSVFormat.EXCEL);
        assertNotNull(parser);
    }

    // Test parse(String string, CSVFormat format) method
    @Test
    public void testParseString() throws Exception {
        String csvString = "John,Doe,25";
        CSVParser parser = CSVParser.parse(csvString, CSVFormat.EXCEL);
        assertNotNull(parser);
    }

    // Test parse(URL url, Charset charset, CSVFormat format) method
    @Test
    public void testParseURL() throws Exception {
        URL url = new URL("https://www.example.com/test.csv");
        CSVParser parser = CSVParser.parse(url, Charset.defaultCharset(), CSVFormat.EXCEL);
        assertNotNull(parser);
    }

    // Test getCurrentLineNumber() method
    @Test
    public void testGetCurrentLineNumber() throws Exception {
        CSVParser parser = CSVParser.parse(new File(testFile), CSVFormat.EXCEL);
        long lineNumber = parser.getCurrentLineNumber();
        assertEquals(0, lineNumber);
    }

    // Test getRecordNumber() method
    @Test
    public void testGetRecordNumber() throws Exception {
        CSVParser parser = CSVParser.parse(new File(testFile), CSVFormat.EXCEL);
        long recordNumber = parser.getRecordNumber();
        assertEquals(0, recordNumber);
    }

    // Test getRecords() method
    @Test
    public void testGetRecords() throws Exception {
        CSVParser parser = CSVParser.parse(new File(testFile), CSVFormat.EXCEL);
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertTrue(records.size() > 0);
    }

    // Test iterator() method
    @Test
    public void testIterator() throws Exception {
        CSVParser parser = CSVParser.parse(new File(testFile), CSVFormat.EXCEL);
        Iterator<CSVRecord> iterator = parser.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
    }

    // Test close() method
    @Test
    public void testClose() throws Exception {
        CSVParser parser = CSVParser.parse(new File(testFile), CSVFormat.EXCEL);
        parser.close();
        assertTrue(parser.isClosed());
    }

}