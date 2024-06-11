import org.junit.Test;
import static org.junit.Assert.*;
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
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(file, format);
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
    }

    @Test
    public void testParseString() throws IOException {
        String csvString = "name,age\nAlice,30\nBob,25";
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(csvString, format);
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertEquals(2, records.size());
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/test.csv");
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(url, charset, format);
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
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
    public void testGetHeaderMap() throws IOException {
        File file = new File("test.csv");
        CSVFormat format = CSVFormat.DEFAULT.withHeader("name", "age");
        CSVParser parser = CSVParser.parse(file, format);
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertEquals(2, headerMap.size());
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        File file = new File("test.csv");
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(file, format);
        long recordNumber = parser.getRecordNumber();
        assertEquals(0, recordNumber);
    }

    @Test
    public void testIsClosed() throws IOException {
        File file = new File("test.csv");
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(file, format);
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testIterator() throws IOException {
        File file = new File("test.csv");
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(file, format);
        int count = 0;
        for (CSVRecord record : parser) {
            count++;
        }
        assertEquals(2, count);
    }

}