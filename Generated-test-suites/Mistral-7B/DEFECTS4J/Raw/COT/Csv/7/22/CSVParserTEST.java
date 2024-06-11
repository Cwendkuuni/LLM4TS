import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVParser.parse;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

/**
 * Test class for CSVParser
 */
public class CSVParserTest {

    /**
     * Test the constructor with valid arguments
     */
    @Test
    public void testParseFile() throws IOException {
        String filePath = "src/test/resources/csv/testFile.csv";
        CSVParser parser = parse(new File(filePath), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record = records.get(0);
        assertArrayEquals(new String[]{"Header1", "Header2", "Header3"}, record.getNames());
        assertArrayEquals(new String[]{"Value1", "Value2", "Value3"}, record.toArray());
        parser.close();
    }

    /**
     * Test the constructor with null arguments
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseFileWithNullFile() throws IOException {
        parse(null, DEFAULT);
    }

    /**
     * Test the constructor with null CSVFormat
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseFileWithNullFormat() throws IOException {
        String filePath = "src/test/resources/csv/testFile.csv";
        parse(new File(filePath), null);
    }

    /**
     * Test parse method with empty string as input
     */
    @Test
    public void testParseEmptyString() throws IOException {
        String input = "";
        CSVParser parser = new CSVParser(new StringReader(input), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertTrue(records.isEmpty());
    }

    /**
     * Test parse method with null input
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseNullInput() throws IOException {
        CSVParser parser = new CSVParser(null, DEFAULT);
    }

    /**
     * Test parse method with invalid CSVFormat
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidFormat() throws IOException {
        String input = "a,b,c";
        CSVFormat format = new CSVFormat(',', null, null, null, null, null, null, null, null, null, null, null, null,
                null);
        format.setDelimiter('a');
        CSVParser parser = new CSVParser(new StringReader(input), format);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
    }

    /**
     * Test getRecordNumber() method
     */
    @Test
    public void testGetRecordNumber() throws IOException {
        String filePath = "src/test/resources/csv/testFile.csv";
        CSVParser parser = parse(new File(filePath), DEFAULT);
        long recordNumber = parser.getRecordNumber();
        parser.close();
        assertTrue(recordNumber > 0);
    }

    /**
     * Test getRecordNumber() method when parser is closed
     */
    @Test
    public void testGetRecordNumberAfterClose() {
        String filePath = "src/test/resources/csv/testFile.csv";
        try (CSVParser parser = parse(new File(filePath), DEFAULT)) {
            long recordNumber = parser.getRecordNumber();
            parser.close();
        }
        long recordNumber = CSVParser.parse(new File(filePath), DEFAULT).getRecordNumber();
        assertTrue(recordNumber > 0);
    }

    /**
     * Test getRecords() method with empty input
     */
    @Test
    public void testGetRecordsEmptyInput() throws IOException {
        String input = "";
        CSVParser parser = new CSVParser(new StringReader(input), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertTrue(records.isEmpty());
    }

    /**
     * Test getRecords() method with null input
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetRecordsNullInput() throws IOException {
        CSVParser parser = new CSVParser(null, DEFAULT);
        parser.getRecords();
    }

    /**
     * Test getRecords() method with empty collection
     */
    @Test
    public void testGetRecordsEmptyCollection() throws IOException {
        CSVParser parser = new CSVParser(new StringReader("a,b\nc,d"), DEFAULT);
        List<CSVRecord> records = Collections.emptyList();
        assertArrayEquals(records.toArray(), parser.getRecords(records).toArray());
        parser.close();
    }

    /**
     * Test getRecords() method with null collection
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testGetRecordsNullCollection() throws IOException {
        CSVParser parser = new CSVParser(new StringReader("a,b\nc,d"), DEFAULT);
        parser.getRecords(null);
    }

    /**
     * Test getRecords() method with custom collection
     */
    @Test
    public void testGetRecordsCustomCollection() throws IOException {
        String input = "a,b\nc,d\ne,f";
        List<CSVRecord> records = new ArrayList<>();
        CSVParser parser = new CSVParser(new StringReader(input), DEFAULT);
        parser.getRecords(records);
        parser.close();
        assertArrayEquals(records.toArray(), parser.getRecords().toArray());
    }

    /**
     * Test iterator() method with empty input
     */
    @Test
    public void testIteratorEmptyInput() throws IOException {
        String input = "";
        CSVParser parser = new CSVParser(new StringReader(input), DEFAULT);
        Iterator<CSVRecord> iterator = parser.iterator();
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());
        iterator.next();
        parser.close();
    }

    /**
     * Test iterator() method with null input
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIteratorNullInput() throws IOException {
        CSVParser parser = new CSVParser(null, DEFAULT);
        parser.iterator();
    }

    /**
     * Test iterator() method with empty collection
     */
    @Test
    public void testIteratorEmptyCollection() throws IOException {
        String input = "a,b\nc,d";
        List<CSVRecord> records = Collections.emptyList();
        CSVParser parser = new CSVParser(new StringReader(input), DEFAULT);
        Iterator<CSVRecord> iterator = parser.iterator();
        assertFalse(iterator.hasNext());
        parser.close();
    }

    /**
     * Test iterator() method with custom collection
     */
    @Test
    public void testIteratorCustomCollection() throws IOException {
        String input = "a,b\nc,d\ne,f";
        List<CSVRecord> records = new ArrayList<>();
        records.add(new CSVRecord(new String[]{"a", "b"}, null, null, 0));
        records.add(new CSVRecord(new String[]{"c", "d"}, null, null, 1));
        records.add(new CSVRecord(new String[]{"e", "f"}, null, null, 2));
        CSVParser parser = new CSVParser(new StringReader(input), DEFAULT);
        Iterator<CSVRecord> iterator = parser.iterator();
        assertArrayEquals(records.get(0).toArray(), iterator.next().toArray());
        assertArrayEquals(records.get(1).toArray(), iterator.next().toArray());
        assertArrayEquals(records.get(2).toArray(), iterator.next().toArray());
        assertFalse(iterator.hasNext());
        parser.close();
    }

    /**
     * Test nextRecord() method with null input
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextRecordNullInput() throws IOException {
        CSVParser parser = new CSVParser(null, DEFAULT);
        parser.nextRecord();
    }

    /**
     * Test nextRecord() method with empty input
     */
    @Test
    public void testNextRecordEmptyInput() throws IOException {
        String input = "";
        CSVParser parser = new CSVParser(new StringReader(input), DEFAULT);
        CSVRecord record = parser.nextRecord();
        assertNull(record);
        parser.close();
    }

    /**
     * Test nextRecord() method with empty record
     */
    @Test
    public void testNextRecordEmptyRecord() throws IOException {
        String input = "a,";
        CSVParser parser = new CSVParser(new StringReader(input), DEFAULT);
        CSVRecord record = parser.nextRecord();
        assertArrayEquals(new String[0], record.toArray());
        parser.close();
    }

    /**
     * Test nextRecord() method with one record
     */
    @Test
    public void testNextRecordOneRecord() throws IOException {
        String input = "a,b,c";
        CSVParser parser = new CSVParser(new StringReader(input), DEFAULT);
        CSVRecord record = parser.nextRecord();
        assertArrayEquals(new String[]{"a", "b", "c"}, record.toArray());
        parser.close();
    }

    /**
     * Test nextRecord() method with multiple records
     */
    @Test
    public void testNextRecordMultipleRecords() throws IOException {
        String input = "a,b,c\nd,e,f\n";
        CSVParser parser = new CSVParser(new StringReader(input), DEFAULT);
        CSVRecord record1 = parser.nextRecord();
        CSVRecord record2 = parser.nextRecord();
        assertArrayEquals(new String[]{"a", "b", "c"}, record1.toArray());
        assertArrayEquals(new String[]{"d", "e", "f"}, record2.toArray());
        parser.close();
    }

    /**
     * Test nextRecord() method when end of input reached
     */
    @Test
    public void testNextRecordEndOfInput() throws IOException {
        String input = "a,b,c";
        CSVParser parser = new CSVParser(new StringReader(input), DEFAULT);
        CSVRecord record = parser.nextRecord();
        CSVRecord record2 = parser.nextRecord();
        assertArrayEquals(new String[]{"a", "b", "c"}, record.toArray());
        assertNull(record2);
        parser.close();
    }

    /**
     * Test nextRecord() method when parser is closed
     */
    @Test
    public void testNextRecordAfterClose() {
        String input = "a,b,c";
        try (CSVParser parser = new CSVParser(new StringReader(input), DEFAULT)) {
            parser.nextRecord();
            parser.nextRecord();
        }
        CSVParser parser2 = new CSVParser(new StringReader(input), DEFAULT);
        CSVRecord record = parser2.nextRecord();
        assertNull(record);
        parser2.close();
    }

    /**
     * Test parse method with custom input stream
     */
    @Test
    public void testParseCustomInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("a,b,c\nd,e,f".getBytes());
        CSVParser parser = new CSVParser(inputStream, DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        parser.close();
        assertArrayEquals(new String[]{"a", "b", "c"}, record1.toArray());
        assertArrayEquals(new String[]{"d", "e", "f"}, record2.toArray());
    }

    /**
     * Test parse method with custom charset and URL
     */
    @Test
    public void testParseCustomCharsetURL() throws IOException {
        String url = "http://example.com/test.csv";
        CSVParser parser = CSVParser.parse(new URL(url), DEFAULT, "UTF-8");
        List<CSVRecord> records = parser.getRecords();
        parser.close();
    }

    /**
     * Test close() method
     */
    @Test
    public void testClose() throws IOException {
        String input = "a,b,c\nd,e,f\n";
        CSVParser parser = new CSVParser(new StringReader(input), DEFAULT);
        parser.close();
        try {
            parser.getRecords();
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Stream closed"));
        }
    }
}