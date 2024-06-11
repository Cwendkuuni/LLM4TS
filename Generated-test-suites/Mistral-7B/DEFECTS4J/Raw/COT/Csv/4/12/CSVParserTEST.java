import org.apache.commons.csv.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVParserTest {

    private CSVFormat csvFormat;
    private CSVParser csvParser;
    private String testCsvData = "data1,data2\n" +
            "record1,record2\n" +
            "record3,record4";

    @Before
    public void setUp() throws IOException {
        csvFormat = new CSVFormat();
        StringReader stringReader = new StringReader(testCsvData);
        csvParser = new CSVParser(stringReader, csvFormat);
    }

    @Test
    public void testParseFile() throws IOException {
        File testFile = new File("test.csv");
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write(testCsvData);
        fileWriter.close();

        CSVParser parser = CSVParser.parse(testFile, csvFormat);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(3, records.size());
        parser.close();
        testFile.deleteOnExit();
    }

    @Test
    public void testParseString() throws IOException {
        List<CSVRecord> records = csvParser.getRecords();

        assertEquals(3, records.size());
        CSVRecord record1 = records.get(0);
        assertEquals("data1", record1.get(0));
        assertEquals("data2", record1.get(1));

        CSVRecord record2 = records.get(1);
        assertEquals("record1", record2.get(0));
        assertEquals("record2", record2.get(1));

        CSVRecord record3 = records.get(2);
        assertEquals("record3", record3.get(0));
        assertEquals("record4", record3.get(1));
    }

    @Test
    public void testParseUrl() throws IOException {
        String urlString = "http://example.com/test.csv";
        Reader reader = new InputStreamReader(new URL(urlString).openStream());
        CSVParser parser = new CSVParser(reader, csvFormat);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(3, records.size());
        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFile() {
        CSVParser parser = CSVParser.parse(null, csvFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFormat() {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, null);
    }

    @Test(expected = IOException.class)
    public void testParseInvalidFile() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, csvFormat);
        parser.getRecords();
        assertFalse(parser.isClosed());
        parser.close();
        file.delete();

        file.createNewFile();
        Writer writer = new FileWriter(file);
        writer.write("invalid data");
        writer.close();

        try {
            parser = CSVParser.parse(file, csvFormat);
            parser.getRecords();
            fail("Expected IOException");
        } finally {
            parser.close();
            file.delete();
        }
    }

    @Test
    public void testIterateRecords() throws IOException {
        List<CSVRecord> records = csvParser.getRecords();

        Iterator<CSVRecord> iterator = records.iterator();
        CSVRecord record1 = iterator.next();
        CSVRecord record2 = iterator.next();
        CSVRecord record3 = iterator.next();
        assertFalse(iterator.hasNext());

        assertEquals(record1, record1); // check for object identity
        assertEquals(record2, record2); // check for object identity
        assertEquals(record3, record3); // check for object identity
    }

    @Test(expected = NoSuchElementException.class)
    public void testIterateEndOfRecords() throws IOException {
        List<CSVRecord> records = csvParser.getRecords();
        Iterator<CSVRecord> iterator = records.iterator();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
    }

    @Test
    public void testCloseParser() throws IOException {
        List<CSVRecord> records = csvParser.getRecords();
        csvParser.close();
        assertFalse(csvParser.isOpen());
    }

    @Test
    public void testGetCurrentLineNumber() throws IOException {
        assertEquals(1, csvParser.getCurrentLineNumber());
        csvParser.nextRecord();
        assertEquals(2, csvParser.getCurrentLineNumber());
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        assertEquals(0, csvParser.getRecordNumber());
        csvParser.nextRecord();
        assertEquals(1, csvParser.getRecordNumber());
    }

}