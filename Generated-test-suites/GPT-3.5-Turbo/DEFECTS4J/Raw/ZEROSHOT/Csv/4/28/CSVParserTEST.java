import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVParserTest {

    private CSVParser csvParser;

    @Before
    public void setup() throws IOException {
        csvParser = new CSVParser(new StringReader("test1,test2,test3\ntest4,test5,test6\n"), CSVFormat.DEFAULT);
    }

    @Test
    public void testGetRecords() throws IOException {
        List<CSVRecord> records = csvParser.getRecords();
        assertEquals(2, records.size());
        assertEquals("test4", records.get(1).get(0));
        assertEquals("test3", records.get(0).get(2));
    }

    @Test
    public void testIterator() {
        Iterator<CSVRecord> iterator = csvParser.iterator();
        assertTrue(iterator.hasNext());
        CSVRecord record = iterator.next();
        assertEquals("test1", record.get(0));
        assertEquals("test2", record.get(1));
        assertEquals("test3", record.get(2));
        record = iterator.next();
        assertEquals("test4", record.get(0));
        assertEquals("test5", record.get(1));
        assertEquals("test6", record.get(2));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNextRecord() throws IOException {
        CSVRecord record = csvParser.nextRecord();
        assertEquals("test1", record.get(0));
        assertEquals("test2", record.get(1));
        assertEquals("test3", record.get(2));
        record = csvParser.nextRecord();
        assertEquals("test4", record.get(0));
        assertEquals("test5", record.get(1));
        assertEquals("test6", record.get(2));
    }

    @Test
    public void testGetCurrentLineNumber() {
        assertEquals(0, csvParser.getCurrentLineNumber());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(0, csvParser.getRecordNumber());
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        csvParser = new CSVParser(new StringReader("header1,header2,header3\ntest1,test2,test3\n"), CSVFormat.DEFAULT.withHeader());
        Map<String, Integer> headerMap = csvParser.getHeaderMap();
        assertEquals(Integer.valueOf(0), headerMap.get("header1"));
        assertEquals(Integer.valueOf(1), headerMap.get("header2"));
        assertEquals(Integer.valueOf(2), headerMap.get("header3"));
    }
}