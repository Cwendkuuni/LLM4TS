import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        CSVFormat format = CSVFormat.RFC4180;
        CSVParser parser = CSVParser.parse(file, format);
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertFalse(records.isEmpty());
    }

    @Test
    public void testParseString() throws IOException {
        String data = "John,Doe,30\nJane,Smith,25\n";
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(data, format);
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertEquals(2, records.size());
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/data.csv");
        Charset charset = Charset.defaultCharset();
        CSVFormat format = CSVFormat.EXCEL;
        CSVParser parser = CSVParser.parse(url, charset, format);
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertTrue(records.size() > 0);
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        File file = new File("test.csv");
        CSVFormat format = CSVFormat.DEFAULT.withHeader("Name", "Age");
        CSVParser parser = CSVParser.parse(file, format);
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertNotNull(headerMap);
        assertEquals(2, headerMap.size());
        assertTrue(headerMap.containsKey("Name"));
        assertTrue(headerMap.containsKey("Age"));
    }

    @Test
    public void testGetCurrentLineNumber() throws IOException {
        File file = new File("test.csv");
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(file, format);
        long lineNumber = parser.getCurrentLineNumber();
        assertEquals(0, lineNumber);
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        File file = new File("test.csv");
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(file, format);
        long recordNumber = parser.getRecordNumber();
        assertEquals(0, recordNumber);
    }

}