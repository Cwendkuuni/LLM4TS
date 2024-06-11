import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.NoSuchElementException;

public class CSVParserTest {

    private CSVParser csvParser;

    @Before
    public void setup() {
        csvParser = new CSVParser(new StringReader("A,B,C\n1,2,3"), CSVFormat.DEFAULT);
    }

    @Test
    public void testParseFile() throws IOException {
        CSVParser parser = CSVParser.parse(new File("test.csv"), CSVFormat.DEFAULT);
        assertNotNull(parser);
    }

    @Test
    public void testParseString() throws IOException {
        CSVParser parser = CSVParser.parse("A,B,C\n1,2,3", CSVFormat.EXCEL);
        assertNotNull(parser);
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/test.csv");
        CSVParser parser = CSVParser.parse(url, Charset.defaultCharset(), CSVFormat.EXCEL);
        assertNotNull(parser);
    }

    @Test
    public void testClose() throws IOException {
        csvParser.close();
        assertTrue(csvParser.isClosed());
    }

    @Test
    public void testGetCurrentLineNumber() {
        assertEquals(1, csvParser.getCurrentLineNumber());
    }

    @Test
    public void testGetHeaderMap() {
        assertNotNull(csvParser.getHeaderMap());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(0, csvParser.getRecordNumber());
    }

    @Test
    public void testGetRecords() throws IOException {
        List<CSVRecord> records = csvParser.getRecords();
        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        assertNotNull(record);
        assertEquals("1", record.get(0));
        assertEquals("2", record.get(1));
        assertEquals("3", record.get(2));
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorNoSuchElementException() {
        Iterator<CSVRecord> iterator = csvParser.iterator();
        iterator.next();
    }
    
    @Test
    public void testNextRecord() throws IOException {
        CSVRecord record = csvParser.nextRecord();
        assertNotNull(record);
        assertEquals("1", record.get(0));
        assertEquals("2", record.get(1));
        assertEquals("3", record.get(2));
    }

}