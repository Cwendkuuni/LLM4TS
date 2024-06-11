import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class CSVParserTest {

    private CSVParser csvParser;

    @Before
    public void setUp() throws IOException {
        File file = new File("test.csv");
        CSVFormat format = CSVFormat.EXCEL;
        csvParser = CSVParser.parse(file, format);
    }

    @Test
    public void testGetCurrentLineNumber() {
        long lineNumber = csvParser.getCurrentLineNumber();
        assertEquals(1, lineNumber);
    }

    @Test
    public void testGetHeaderMap() {
        Map<String, Integer> headerMap = csvParser.getHeaderMap();
        assertNotNull(headerMap);
    }

    @Test
    public void testGetRecordNumber() {
        long recordNumber = csvParser.getRecordNumber();
        assertEquals(0, recordNumber);
    }

    @Test
    public void testGetRecords() throws IOException {
        List<CSVRecord> records = csvParser.getRecords();
        assertNotNull(records);
        assertEquals(3, records.size());
    }

    @Test
    public void testIsClosed() {
        assertFalse(csvParser.isClosed());
    }

    @Test
    public void testIterator() {
        Iterator<CSVRecord> iterator = csvParser.iterator();
        assertNotNull(iterator);
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        CSVFormat format = CSVFormat.EXCEL;
        CSVParser parser = CSVParser.parse(file, format);
        assertNotNull(parser);
    }

    @Test
    public void testParseString() throws IOException {
        String string = "a,b,c\nd,e,f\ng,h,i\n";
        CSVFormat format = CSVFormat.EXCEL;
        CSVParser parser = CSVParser.parse(string, format);
        assertNotNull(parser);
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/data.csv");
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = CSVFormat.EXCEL;
        CSVParser parser = CSVParser.parse(url, charset, format);
        assertNotNull(parser);
    }

    @Test
    public void testClose() throws IOException {
        csvParser.close();
        assertTrue(csvParser.isClosed());
    }

    @Test
    public void testNextRecord() throws IOException {
        CSVRecord record = csvParser.nextRecord();
        assertNotNull(record);
    }

}