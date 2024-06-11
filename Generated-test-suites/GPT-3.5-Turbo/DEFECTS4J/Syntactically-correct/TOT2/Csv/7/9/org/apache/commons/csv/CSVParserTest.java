import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        CSVParser csvParser = CSVParser.parse(file, CSVFormat.EXCEL);
        assertNotNull(csvParser);
    }

    @Test
    public void testParseString() throws IOException {
        String data = "name,age\nJohn,25";
        CSVParser csvParser = CSVParser.parse(data, CSVFormat.EXCEL);
        assertNotNull(csvParser);
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/test.csv");
        CSVParser csvParser = CSVParser.parse(url, StandardCharsets.UTF_8, CSVFormat.EXCEL);
        assertNotNull(csvParser);
    }

    @Test
    public void testGetRecords() throws IOException {
        File file = new File("test.csv");
        CSVParser csvParser = CSVParser.parse(file, CSVFormat.EXCEL);
        List<CSVRecord> records = csvParser.getRecords();
        assertFalse(records.isEmpty());
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        File file = new File("test.csv");
        CSVParser csvParser = CSVParser.parse(file, CSVFormat.EXCEL);
        Map<String, Integer> headerMap = csvParser.getHeaderMap();
        assertNotNull(headerMap);
    }

    @Test
    public void testGetCurrentLineNumber() throws IOException {
        File file = new File("test.csv");
        CSVParser csvParser = CSVParser.parse(file, CSVFormat.EXCEL);
        long lineNumber = csvParser.getCurrentLineNumber();
        assertTrue(lineNumber >= 0);
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        File file = new File("test.csv");
        CSVParser csvParser = CSVParser.parse(file, CSVFormat.EXCEL);
        long recordNumber = csvParser.getRecordNumber();
        assertTrue(recordNumber == 0);
    }

    @Test
    public void testNextRecord() throws IOException {
        File file = new File("test.csv");
        CSVParser csvParser = CSVParser.parse(file, CSVFormat.EXCEL);
        CSVRecord record = csvParser.nextRecord();
        assertNotNull(record);
    }

    @Test
    public void testIsClosed() throws IOException {
        File file = new File("test.csv");
        CSVParser csvParser = CSVParser.parse(file, CSVFormat.EXCEL);
        csvParser.close();
        assertTrue(csvParser.isClosed());
    }

}