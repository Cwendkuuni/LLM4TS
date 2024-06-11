import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

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
        CSVFormat format = CSVFormat.RFC4180;
        csvParser = CSVParser.parse(file, format);
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
        assertTrue(headerMap.isEmpty());
    }

    @Test
    public void testGetRecords() throws IOException {
        List<CSVRecord> records = csvParser.getRecords();
        assertTrue(records.isEmpty());
    }

    @Test
    public void testIsClosed() {
        assertFalse(csvParser.isClosed());
    }

    @Test
    public void testIterator() {
        assertTrue(csvParser.iterator().hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorThrowException() {
        csvParser.close();
        csvParser.iterator().next();
    }

    @Test
    public void testNextRecord() throws IOException {
        CSVRecord record = csvParser.nextRecord();
        assertNull(record);
    }

    @Test
    public void testClose() throws IOException {
        csvParser.close();
        assertTrue(csvParser.isClosed());
    }
}