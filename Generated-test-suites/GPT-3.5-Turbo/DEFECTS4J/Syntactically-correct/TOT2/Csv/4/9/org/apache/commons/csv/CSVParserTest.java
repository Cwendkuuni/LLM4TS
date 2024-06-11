import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        assertNotNull(parser);
    }

    @Test
    public void testParseString() throws IOException {
        String csvString = "name,age\nAlice,30\nBob,25";
        CSVParser parser = CSVParser.parse(csvString, CSVFormat.DEFAULT);
        assertNotNull(parser);
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/test.csv");
        CSVParser parser = CSVParser.parse(url, Charset.forName("UTF-8"), CSVFormat.DEFAULT);
        assertNotNull(parser);
    }

    @Test
    public void testClose() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testGetCurrentLineNumber() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        assertEquals(0, parser.getCurrentLineNumber());
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        assertEquals(0, parser.getRecordNumber());
    }

    @Test
    public void testGetRecords() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertFalse(records.isEmpty());
    }

    @Test
    public void testIterator() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        Iterator<CSVRecord> iterator = parser.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
    }

    @Test
    public void testNextRecord() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        CSVRecord record = parser.nextRecord();
        assertNotNull(record);
    }

}