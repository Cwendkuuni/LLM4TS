package org.apache.commons.csv; // Make sure to import all necessary classes

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;
import org.junit.Test;

/**
 * JUnit 4 test cases for the CSVParser class.
 */
public class CSVParserTest {
    // Test data
    private static final String TEST_CSV_STRING = "name,age\nAlice,25\nBob,30\n";

    private static final String TEST_CSV_FILE = "target/test-classes/testCSV.csv";

    @Test
    public void testParseFileWithHeader() throws IOException {
        CSVParser parser = CSVParser.parse(new File(TEST_CSV_FILE), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);
        Map<String, Integer> headerMap = parser.getHeaderMap();

        assertEquals(2, records.size());
        assertEquals("name", headerMap.get("name"));
        assertEquals("age", headerMap.get("age"));
        assertEquals("Alice", firstRecord.get("name"));
        assertEquals(25, firstRecord.getInteger("age"));
        parser.close();
    }

    @Test
    public void testParseStringWithHeader() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_CSV_STRING.getBytes(Charset.forName("UTF-8")));
        CSVParser parser = CSVParser.parse(inputStream, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);
        Map<String, Integer> headerMap = parser.getHeaderMap();

        assertEquals(2, records.size());
        assertEquals("name", headerMap.get("name"));
        assertEquals("age", headerMap.get("age"));
        assertEquals("Alice", firstRecord.get("name"));
        assertEquals(25, firstRecord.getInteger("age"));
        parser.close();
        inputStream.close();
    }

    @Test
    public void testParseFileWithoutHeader() throws IOException {
        CSVParser parser = CSVParser.parse(new File(TEST_CSV_FILE), CSVFormat.DEFAULT.withHeaderCount(0));
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        CSVRecord firstRecord = records.get(0);
        assertEquals("Alice", firstRecord.get(0));
        assertEquals(25, firstRecord.getInteger(1));
        parser.close();
    }

    @Test
    public void testParseStringWithoutHeader() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_CSV_STRING.getBytes(Charset.forName("UTF-8")));
        CSVParser parser = CSVParser.parse(inputStream, CSVFormat.DEFAULT.withHeaderCount(0));
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        CSVRecord firstRecord = records.get(0);
        assertEquals("Alice", firstRecord.get(0));
        assertEquals(25, firstRecord.getInteger(1));
        parser.close();
        inputStream.close();
    }

    @Test
    public void testParseStringWithCustomFormat() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_CSV_STRING.getBytes(Charset.forName("UTF-8")));
        CSVFormat customFormat = new CSVFormat(';', '\\', '\'', null, null, null, null, null, null, null, null, null,
                Token.ALL);
        CSVParser parser = CSVParser.parse(inputStream, customFormat);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);

        assertEquals(2, records.size());
        assertEquals("name", firstRecord.getName(0));
        assertEquals("age", firstRecord.getName(1));
        assertEquals("Alice", firstRecord.get(0));
        assertEquals(25, firstRecord.getInteger(1));
        parser.close();
        inputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFile() throws IOException {
        CSVParser.parse(null, CSVFormat.DEFAULT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFormat() throws IOException {
        File file = new File(TEST_CSV_FILE);
        CSVParser.parse(file, null);
    }

    @Test(expected = IOException.class)
    public void testParseNullInputStream() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(null, format);
        parser.getRecords();
        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullCSVFormat() throws IOException {
        File file = new File(TEST_CSV_FILE);
        CSVParser parser = CSVParser.parse(file, null);
        parser.close();
    }

    @Test
    public void testEmptyCsv() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        CSVParser parser = CSVParser.parse(inputStream, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        inputStream.close();
        assertTrue(records.isEmpty());
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_CSV_STRING.getBytes(Charset.forName("UTF-8")));
        CSVParser parser = CSVParser.parse(inputStream, CSVFormat.DEFAULT);
        Map<String, Integer> headerMap = parser.getHeaderMap();
        parser.close();
        inputStream.close();

        assertEquals(2, headerMap.size());
        assertEquals("name", headerMap.get("name"));
        assertEquals("age", headerMap.get("age"));
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetRecordAfterClosedParser() throws IOException {
        File file = new File(TEST_CSV_FILE);
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        parser.close();
        parser.nextRecord();
    }

    @Test
    public void testGetRecordBeforeEndOfStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_CSV_STRING.getBytes(Charset.forName("UTF-8")));
        CSVParser parser = CSVParser.parse(inputStream, CSVFormat.DEFAULT);
        CSVRecord record = parser.nextRecord();
        parser.close();
        inputStream.close();

        assertSame(CSVRecord.class, record.getClass());
        assertEquals(2, record.size());
        assertEquals("Alice", record.get(0));
        assertEquals(25, record.getInteger(1));
    }

    @Test
    public void testGetRecordAtEndOfStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_CSV_STRING.getBytes(Charset.forName("UTF-8")));
        CSVParser parser = CSVParser.parse(inputStream, CSVFormat.DEFAULT);
        CSVRecord record = parser.nextRecord();
        CSVRecord recordAfterEnd = parser.nextRecord();
        parser.close();
        inputStream.close();

        assertSame(CSVRecord.class, record.getClass());
        assertEquals(2, record.size());
        assertEquals("Alice", record.get(0));
        assertEquals(25, record.getInteger(1));
        assertNull(recordAfterEnd);
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorAfterEndOfStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_CSV_STRING.getBytes(Charset.forName("UTF-8")));
        Iterator<CSVRecord> iterator = CSVParser.parse(inputStream, CSVFormat.DEFAULT).iterator();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorBeforeStart() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_CSV_STRING.getBytes(Charset.forName("UTF-8")));
        Iterator<CSVRecord> iterator = CSVParser.parse(inputStream, CSVFormat.DEFAULT).iterator();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.previous();
    }

    @Test
    public void testIterator() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_CSV_STRING.getBytes(Charset.forName("UTF-8")));
        Iterator<CSVRecord> iterator = CSVParser.parse(inputStream, CSVFormat.DEFAULT).iterator();

        List<CSVRecord> expectedRecords = Arrays.asList(
                new CSVRecord(new String[]{"Alice", "25"}, new LinkedHashMap<String, Integer>() {{
                    put("name", 0);
                    put("age", 1);
                }}),
                new CSVRecord(new String[]{"Bob", "30"}, new LinkedHashMap<String, Integer>() {{
                    put("name", 0);
                    put("age", 1);
                }}));

        assertEquals(expectedRecords.size(), iterator.size());

        for (CSVRecord record : expectedRecords) {
            CSVRecord testRecord = iterator.next();
            assertSame(record.getClass(), testRecord.getClass());
            assertEquals(record.size(), testRecord.size());
            assertArrayEquals(record.toArray(), testRecord.toArray());
        }

        assertFalse(iterator.hasNext());
        parser.close();
        inputStream.close();
    }
}