import org.apache.commons.csv.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CSVParser.
 */
public class CSVParserTest {

    private static final String TEST_CSV = "Name,Age,City\nJohn,30,New York\nMary,25,Boston\n";

    /**
     * The CSVParser instance for testing.
     */
    private CSVParser parser;

    /**
     * Sets up the test by initializing the CSVParser instance.
     * @throws IOException on parse error or input read-failure
     */
    @Before
    public void setUp() throws IOException {
        parser = new CSVParser(new StringReader(TEST_CSV), CSVFormat.DEFAULT);
    }

    /**
     * Tests the parse(File, CSVFormat) method.
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testParseFile() throws IOException {
        Assert.assertNotNull(CSVParser.parse(new File("src/test/resources/test.csv"), CSVFormat.DEFAULT));
    }

    /**
     * Tests the parse(String, CSVFormat) method.
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testParseString() throws IOException {
        Assert.assertNotNull(CSVParser.parse(TEST_CSV, CSVFormat.DEFAULT));
    }

    /**
     * Tests the parse(URL, Charset, CSVFormat) method.
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testParseURL() throws IOException {
        Assert.assertNotNull(CSVParser.parse(new URL("file:src/test/resources/test.csv"), Charset.forName("UTF-8"),
                CSVFormat.DEFAULT));
    }

    /**
     * Tests the parse(Reader) method.
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testParseReader() throws IOException {
        Assert.assertNotNull(CSVParser.parse(new StringReader(TEST_CSV), CSVFormat.DEFAULT));
    }

    /**
     * Tests the getRecords() method.
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testGetRecords() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals("Name", record1.getHeaderMap().get("Name"));
        assertEquals("John", record1.get(0));
        assertEquals(30, record1.get(1).intValue());
        assertEquals("New York", record1.get(2));

        CSVRecord record2 = records.get(1);
        assertEquals("Name", record2.getHeaderMap().get("Name"));
        assertEquals("Mary", record2.get(0));
        assertEquals(25, record2.get(1).intValue());
        assertEquals("Boston", record2.get(2));
    }

    /**
     * Tests the nextRecord() method in an iterator.
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testNextRecordIterator() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        Iterator<CSVRecord> iterator = records.iterator();

        CSVRecord record1 = iterator.next();
        assertEquals("Name", record1.getHeaderMap().get("Name"));
        assertEquals("John", record1.get(0));
        assertEquals(30, record1.get(1).intValue());
        assertEquals("New York", record1.get(2));

        CSVRecord record2 = iterator.next();
        assertEquals("Name", record2.getHeaderMap().get("Name"));
        assertEquals("Mary", record2.get(0));
        assertEquals(25, record2.get(1).intValue());
        assertEquals("Boston", record2.get(2));

        assertFalse(iterator.hasNext());
    }

    /**
     * Tests the getRecordNumber() method.
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testGetRecordNumber() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, parser.getRecordNumber());

        for (CSVRecord record : records) {
            assertEquals(parser.getRecordNumber() - 1, record.getRecordNumber());
            parser.nextRecord();
            assertEquals(parser.getRecordNumber(), record.getRecordNumber());
        }
    }

    /**
     * Tests the getHeaderMap() method.
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testGetHeaderMap() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        Map<String, Integer> headerMap = parser.getHeaderMap();

        assertEquals(3, headerMap.size());
        assertEquals("Name", headerMap.get("Name"));
        assertEquals("Age", headerMap.get("Age"));
        assertEquals("City", headerMap.get("City"));

        for (CSVRecord record : records) {
            assertEquals("Name", record.getHeaderMap().get("Name"));
            assertEquals("Age", record.getHeaderMap().get("Age"));
            assertEquals("City", record.getHeaderMap().get("City"));
        }
    }

    /**
     * Tests the isClosed() method.
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testIsClosed() throws IOException {
        List<CSVRecord> records = parser.getRecords();

        assertFalse(parser.isClosed());

        parser.close();

        assertTrue(parser.isClosed());

        try {
            parser.nextRecord();
            Assert.fail("Expected an IOException when the parser is closed.");
        } catch (IOException e) {
            // Expected
        }
    }

    /**
     * Tests the close() method.
     * @throws IOException on parse error or input read-failure
     */
    @After
    public void tearDown() throws IOException {
        parser.close();
    }

}