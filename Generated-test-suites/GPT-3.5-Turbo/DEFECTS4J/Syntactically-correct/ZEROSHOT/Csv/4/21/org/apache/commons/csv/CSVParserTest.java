import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class CSVParserTest {

    private CSVParser csvParser;

    @Before
    public void setUp() throws IOException {
        File file = new File("path/to/csv");
        CSVFormat format = CSVFormat.RFC4180;
        csvParser = CSVParser.parse(file, format);
    }

    @Test
    public void testGetCurrentLineNumber() {
        long lineNum = csvParser.getCurrentLineNumber();

        assertEquals(0, lineNum);
    }

    @Test
    public void testGetRecordNumber() {
        long recordNum = csvParser.getRecordNumber();

        assertEquals(0, recordNum);
    }

    @Test
    public void testGetRecords() throws IOException {
        List<CSVRecord> records = csvParser.getRecords();

        assertEquals(0, records.size());
    }

    @Test
    public void testIsClosed() throws IOException {
        assertFalse(csvParser.isClosed());
    }

    @Test
    public void testIterator() {
        Iterator<CSVRecord> iterator = csvParser.iterator();

        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorNextThrowsNoSuchElementException() {
        Iterator<CSVRecord> iterator = csvParser.iterator();
        iterator.next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemoveThrowsUnsupportedOperationException() {
        Iterator<CSVRecord> iterator = csvParser.iterator();
        iterator.remove();
    }

    @Test
    public void testClose() throws IOException {
        csvParser.close();

        assertTrue(csvParser.isClosed());
    }
}