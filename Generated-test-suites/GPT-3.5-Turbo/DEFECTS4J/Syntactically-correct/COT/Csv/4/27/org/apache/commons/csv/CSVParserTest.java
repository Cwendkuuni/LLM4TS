import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVParserTest {

    private CSVFormat format;
    private CSVParser parser;

    @Before
    public void setUp() throws Exception {
        format = CSVFormat.DEFAULT;
        parser = CSVParser.parse("id,name,age\n1,Alice,25\n2,Bob,30\n3,Charlie,35", format);
    }

    @Test
    public void testParseFile() {
        assertNotNull(parser);
    }

    @Test
    public void testGetHeaderMap() {
        assertEquals(3, parser.getHeaderMap().size());
        assertTrue(parser.getHeaderMap().containsKey("id"));
        assertTrue(parser.getHeaderMap().containsKey("name"));
        assertTrue(parser.getHeaderMap().containsKey("age"));
    }

    @Test
    public void testGetRecords() {
        assertNotNull(parser.getRecords());
        assertEquals(3, parser.getRecords().size());
    }

    @Test
    public void testGetCurrentLineNumber() {
        assertEquals(1, parser.getCurrentLineNumber());
        parser.nextRecord();
        assertEquals(2, parser.getCurrentLineNumber());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(0, parser.getRecordNumber());
        parser.nextRecord();
        assertEquals(1, parser.getRecordNumber());
    }

    @Test
    public void testIterator() {
        assertNotNull(parser.iterator());
        int count = 0;
        for (CSVRecord record : parser) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test(expected=NoSuchElementException.class)
    public void testIteratorNoSuchElementException() {
        parser.iterator().next();
    }

    @Test
    public void testClose() throws Exception {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testNextRecord() throws Exception {
        assertNotNull(parser.nextRecord());
        assertNotNull(parser.nextRecord());
        assertNotNull(parser.nextRecord());
        assertNull(parser.nextRecord());
    }

    @Test
    public void testNextRecordWithComments() throws Exception {
        parser = CSVParser.parse("#comment\n1,Alice,25\n2,Bob,30", format);
        assertNotNull(parser.nextRecord());
        assertNotNull(parser.nextRecord());
        assertNull(parser.nextRecord());
    }

    @Test(expected=IOException.class)
    public void testNextRecordInvalidParseSequence() throws Exception {
        parser = CSVParser.parse("1,Alice,25\n2,Bob,30,35", format);
        parser.nextRecord();
    }

}