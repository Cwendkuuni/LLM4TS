import static org.apache.commons.csv.CSVParser.parse;
import static org.apache.commons.csv.CSVRecord.type;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.ext.ExtendedBufferedReader;
import org.junit.Before;
import org.junit.Test;

public class CSVParserTest {

    private CSVParser parser;
    private static final String TEST_CSV_DATA = "test,test2,test3,test4\n"
            + "value1,value2,value3,value4\n"
            + "value5,value6,value7,value8\n";

    @Before
    public void setup() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_CSV_DATA.getBytes(StandardCharsets.UTF_8));
        InputStreamReader reader = new InputStreamReader(inputStream);
        parser = parse(reader, CSVFormat.DEFAULT);
    }

    @Test
    public void testParseFile() throws IOException {
        CSVParser parser = parse(new File("test.csv"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        parser.close();
    }

    @Test
    public void testParseString() throws IOException {
        CSVParser parser = parse(TEST_CSV_DATA, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        parser.close();
    }

    @Test
    public void testParseUrl() throws IOException {
        CSVParser parser = parse(new URL("http://example.com/test.csv"), CSVFormat.DEFAULT, Charset.forName("UTF-8"));
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        parser.close();
    }

    @Test
    public void testParseReader() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_CSV_DATA.getBytes(StandardCharsets.UTF_8));
        CSVParser parser = new CSVParser(new InputStreamReader(inputStream), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        parser.close();
    }

    @Test
    public void testGetCurrentLineNumber() throws IOException {
        assertEquals(1, parser.getCurrentLineNumber());
        parser.nextRecord();
        parser.nextRecord();
        assertEquals(3, parser.getCurrentLineNumber());
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertEquals(4, headerMap.size());
        List<String> keys = new ArrayList<>(headerMap.keySet());
        List<Integer> values = new ArrayList<>(headerMap.values());
        List<String> expectedKeys = Arrays.asList("test", "test2", "test3", "test4");
        List<Integer> expectedValues = Arrays.asList(0, 1, 2, 3);
        assertEquals(expectedKeys, keys);
        assertEquals(expectedValues, values);
    }

    @Test
    public void testGetRecords() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        for (CSVRecord record : records) {
            List<String> values = Arrays.asList(record.getValues());
            if (record.getRecordNumber() == 1) {
                assertEquals("value1", values.get(0));
                assertEquals("value2", values.get(1));
                assertEquals("value3", values.get(2));
                assertEquals("value4", values.get(3));
            } else if (record.getRecordNumber() == 2) {
                assertEquals("value5", values.get(0));
                assertEquals("value6", values.get(1));
                assertEquals("value7", values.get(2));
                assertEquals("value8", values.get(3));
            }
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoMoreRecords() throws IOException {
        // No more records after parsing all records
        parser.getRecords();
        parser.nextRecord();
    }

    @Test(expected = IOException.class)
    public void testInvalidCsv() throws IOException {
        String invalidCsvData = "test,test2,test3,test4\n"
                + "value1,value2,value3,value4\n"
                + "value5,value6,value7,value8\n"
                + "test,\n";
        CSVParser parser = parse(new ByteArrayInputStream(invalidCsvData.getBytes(StandardCharsets.UTF_8)), CSVFormat.DEFAULT);
        try {
            parser.nextRecord();
            parser.nextRecord();
            parser.nextRecord();
        } catch (IOException e) {
            assertEquals("(line 4) malformed at line 4 column 0", e.getMessage());
            throw e;
        } finally {
            parser.close();
        }
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        assertEquals(0, parser.getRecordNumber());
        parser.nextRecord();
        assertEquals(1, parser.getRecordNumber());
        parser.nextRecord();
        assertEquals(2, parser.getRecordNumber());
        parser.nextRecord(); // No more records
        assertEquals(2, parser.getRecordNumber());
    }

    @Test
    public void testNextRecord() throws IOException {
        CSVRecord record = parser.nextRecord();
        assertEquals(type.RECORD, record.getType());
        parser.nextRecord();
        parser.nextRecord();
        CSVRecord lastRecord = parser.nextRecord();
        assertNull(lastRecord);
    }

    @Test(expected = IOException.class)
    public void testNotClosed() throws IOException {
        parser.getRecords();
        parser.nextRecord();
        parser.nextRecord();
        parser.getRecords();
    }
}