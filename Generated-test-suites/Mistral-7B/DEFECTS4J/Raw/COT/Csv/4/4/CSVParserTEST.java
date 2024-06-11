import org.apache.commons.csv.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVParserTest {

    

    @Test
    public void testParseFile() throws IOException {
        File csvFile = new File("test.csv");
        CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(5, records.size());

        CSVRecord record = records.get(0);
        assertNotNull(record);
        assertEquals(5, record.size());
        assertEquals("Name1", record.get(0));
        assertEquals("Age1", record.get(1));
        assertEquals("City1", record.get(2));
        assertEquals("Country1", record.get(3));
        assertEquals("Email1", record.get(4));

        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFileNullFormat() throws IOException {
        File csvFile = new File("test.csv");
        CSVParser parser = CSVParser.parse(csvFile, null);
    }

    @Test(expected = IOException.class)
    public void testParseFileNullFile() throws IOException {
        CSVParser parser = CSVParser.parse(null, CSVFormat.DEFAULT);
    }

    @Test
    public void testParseString() throws IOException {
        String csvString = "Name,Age,City,Country,Email\n" +
                "Name1,Age1,City1,Country1,Email1\n" +
                "Name2,Age2,City2,Country2,Email2\n";
        CSVParser parser = CSVParser.parse(csvString, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());

        CSVRecord record = records.get(0);
        assertNotNull(record);
        assertEquals(5, record.size());
        assertEquals("Name1", record.get(0));
        assertEquals("Age1", record.get(1));
        assertEquals("City1", record.get(2));
        assertEquals("Country1", record.get(3));
        assertEquals("Email1", record.get(4));

        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringNullFormat() throws IOException {
        String csvString = "Name,Age,City,Country,Email\n" +
                "Name1,Age1,City1,Country1,Email1\n" +
                "Name2,Age2,City2,Country2,Email2\n";
        CSVParser parser = CSVParser.parse(csvString, null);
    }

    @Test(expected = IOException.class)
    public void testParseStringNullCSVData() throws IOException {
        CSVParser parser = CSVParser.parse(null, CSVFormat.DEFAULT);
    }

    @Test
    public void testParseURL() throws IOException {
        URL csvUrl = new URL("http://example.com/test.csv");
        CSVParser parser = CSVParser.parse(csvUrl, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(5, records.size());

        CSVRecord record = records.get(0);
        assertNotNull(record);
        assertEquals(5, record.size());
        assertEquals("Name1", record.get(0));
        assertEquals("Age1", record.get(1));
        assertEquals("City1", record.get(2));
        assertEquals("Country1", record.get(3));
        assertEquals("Email1", record.get(4));

        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseURLNullFormat() throws IOException {
        URL csvUrl = new URL("http://example.com/test.csv");
        CSVParser parser = CSVParser.parse(csvUrl, null);
    }

    @Test(expected = IOException.class)
    public void testParseURLNullCSVData() throws IOException {
        CSVParser parser = CSVParser.parse(null, CSVFormat.DEFAULT);
    }

    @Test
    public void testGetRecords() throws IOException {
        File csvFile = new File("test.csv");
        CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(5, records.size());

        parser.close();
    }

    @Test(expected = IOException.class)
    public void testGetRecordsNullParser() throws IOException {
        List<CSVRecord> records = CSVParser.parse(null, CSVFormat.DEFAULT).getRecords();
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        File csvFile = new File("test.csv");
        CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);
        Map<String, Integer> headerMap = parser.getHeaderMap();

        assertNotNull(headerMap);
        assertEquals(5, headerMap.size());
        assertTrue(headerMap.containsKey("Name"));
        assertEquals(0, headerMap.get("Name"));
        assertTrue(headerMap.containsKey("Age"));
        assertEquals(1, headerMap.get("Age"));
        assertTrue(headerMap.containsKey("City"));
        assertEquals(2, headerMap.get("City"));
        assertTrue(headerMap.containsKey("Country"));
        assertEquals(3, headerMap.get("Country"));
        assertTrue(headerMap.containsKey("Email"));
        assertEquals(4, headerMap.get("Email"));

        parser.close();
    }

    @Test(expected = IOException.class)
    public void testGetHeaderMapNullParser() throws IOException {
        Map<String, Integer> headerMap = CSVParser.parse(null, CSVFormat.DEFAULT).getHeaderMap();
    }

    @Test
    public void testIsClosed() throws IOException {
        File csvFile = new File("test.csv");
        CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);
        assertTrue(!parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test(expected = IOException.class)
    public void testIsClosedNullParser() throws IOException {
        assertTrue(CSVParser.parse(null, CSVFormat.DEFAULT).isClosed());
    }

    @Test
    public void testIterator() throws IOException {
        File csvFile = new File("test.csv");
        CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);

        Iterator<CSVRecord> iterator = parser.iterator();
        CSVRecord record = iterator.next();
        assertNotNull(record);
        assertEquals(5, record.size());
        assertEquals("Name1", record.get(0));
        assertEquals("Age1", record.get(1));
        assertEquals("City1", record.get(2));
        assertEquals("Country1", record.get(3));
        assertEquals("Email1", record.get(4));

        record = iterator.next();
        assertNotNull(record);
        assertEquals(5, record.size());
        assertEquals("Name2", record.get(0));
        assertEquals("Age2", record.get(1));
        assertEquals("City2", record.get(2));
        assertEquals("Country2", record.get(3));
        assertEquals("Email2", record.get(4));

        record = iterator.next();
        assertNull(record);

        parser.close();
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorNoMoreRecords() throws IOException {
        File csvFile = new File("test.csv");
        CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);
        Iterator<CSVRecord> iterator = parser.iterator();
        CSVRecord record = iterator.next();
        assertNotNull(record);
        iterator.next();
        iterator.next();

        record = iterator.next();
        assertNull(record);

        parser.close();
    }

    
}