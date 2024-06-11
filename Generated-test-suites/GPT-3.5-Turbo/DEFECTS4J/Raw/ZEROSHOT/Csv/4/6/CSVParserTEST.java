import org.junit.Before;
import org.junit.Test;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class CSVParserTest {

    private CSVParser csvParser;

    @Before
    public void setUp() throws IOException {
        File csvFile = new File("test.csv");
        Charset charset = Charset.forName("UTF-8");
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader().withDelimiter(';');
        csvParser = CSVParser.parse(csvFile, charset, csvFormat);
    }

    @Test
    public void testParse() throws IOException {
        List<CSVRecord> records = csvParser.getRecords();
        assertEquals(3, records.size());
    }

    @Test
    public void testGetHeaderMap() {
        Map<String, Integer> headerMap = csvParser.getHeaderMap();
        assertNotNull(headerMap);
        assertEquals(3, headerMap.size());
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
    public void testGetRecords() throws IOException {
        List<CSVRecord> records = csvParser.getRecords();
        assertEquals(3, records.size());
    }

    @Test(expected = NoSuchElementException.class)
    public void testIterator() throws IOException {
        csvParser.iterator().next();
    }

    @Test
    public void testIsClosed() throws IOException {
        assertFalse(csvParser.isClosed());
        csvParser.close();
        assertTrue(csvParser.isClosed());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextRecord() throws IOException {
        csvParser.nextRecord();
    }
}