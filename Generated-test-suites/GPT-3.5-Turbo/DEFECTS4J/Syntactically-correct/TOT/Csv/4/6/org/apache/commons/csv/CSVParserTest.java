import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVParserTest {

    private CSVParser csvParser;

    @Before
    public void setUp() throws Exception {
        csvParser = new CSVParser(new StringReader("name,age\nJohn,25\nJane,30"), CSVFormat.DEFAULT);
    }

    @Test
    public void testGetRecords() throws Exception {
        List<CSVRecord> records = csvParser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals("John", record1.get("name"));
        assertEquals("25", record1.get("age"));

        CSVRecord record2 = records.get(1);
        assertEquals("Jane", record2.get("name"));
        assertEquals("30", record2.get("age"));
    }

    @Test
    public void testIterator() throws Exception {
        Iterator<CSVRecord> iterator = csvParser.iterator();
        assertTrue(iterator.hasNext());

        CSVRecord record1 = iterator.next();
        assertEquals("John", record1.get("name"));
        assertEquals("25", record1.get("age"));

        assertTrue(iterator.hasNext());

        CSVRecord record2 = iterator.next();
        assertEquals("Jane", record2.get("name"));
        assertEquals("30", record2.get("age"));

        assertFalse(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorThrowsNoSuchElementException() throws Exception {
        Iterator<CSVRecord> iterator = csvParser.iterator();
        iterator.next(); // skip first record
        iterator.next(); // skip second record
        assertFalse(iterator.hasNext());
        iterator.next(); // NoSuchElementException should be thrown here
    }

    @Test
    public void testGetCurrentLineNumber() throws Exception {
        assertEquals(0, csvParser.getCurrentLineNumber());
        csvParser.nextRecord(); // read first record
        assertEquals(2, csvParser.getCurrentLineNumber());
        csvParser.nextRecord(); // read second record
        assertEquals(3, csvParser.getCurrentLineNumber());
    }

    @Test
    public void testGetRecordNumber() throws Exception {
        assertEquals(0, csvParser.getRecordNumber());
        csvParser.nextRecord(); // read first record
        assertEquals(1, csvParser.getRecordNumber());
        csvParser.nextRecord(); // read second record
        assertEquals(2, csvParser.getRecordNumber());
    }

    @Test
    public void testIsClosed() throws Exception {
        assertFalse(csvParser.isClosed());
        csvParser.close();
        assertTrue(csvParser.isClosed());
    }

    @Test
    public void testClose() throws Exception {
        csvParser.close();
        assertTrue(csvParser.isClosed());
    }
}