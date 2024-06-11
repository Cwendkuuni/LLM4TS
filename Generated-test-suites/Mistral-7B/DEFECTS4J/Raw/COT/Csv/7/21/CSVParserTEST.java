package org.apache.commons.csv.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.tokenizer.Token;
import org.junit.Test;

/**
 * Test cases for CSVParser class.
 */
public class CSVParserTest {

    /**
     * Test constructor with valid input.
     *
     * @throws IOException on error
     */
    @Test
    public void testConstructor() throws IOException {
        final String csv = "name,age,city\nAlice,25,New York\nBob,30,Chicago";

        final CSVParser parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csv.getBytes()), Charset.forName("UTF-8")),
                CSVFormat.DEFAULT.withHeader());

        assertSame(CSVFormat.DEFAULT, parser.getFormat());
        assertFalse(parser.isClosed());
    }

    /**
     * Test parse method with valid input.
     *
     * @throws IOException on error
     */
    @Test
    public void testParse() throws IOException {
        final String csv = "name,age,city\nAlice,25,New York\nBob,30,Chicago";

        final CSVParser parser = CSVParser.parse(new File("test.csv"), CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Alice", "25", "New York"}, record1.toArray());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Bob", "30", "Chicago"}, record2.toArray());
    }

    /**
     * Test parse method with empty CSV file.
     *
     * @throws IOException on error
     */
    @Test
    public void testParseEmptyCSV() throws IOException {
        final String csv = "";

        final CSVParser parser = CSVParser.parse(new File("test.csv"), CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = parser.getRecords();
        assertTrue(records.isEmpty());
    }

    /**
     * Test parse method with invalid CSV file.
     *
     * @throws IOException on error
     */
    @Test(expected = IOException.class)
    public void testParseInvalidCSV() throws IOException {
        final String csv = "name,age,city\nAlice,25,New York\nBob,30,Chicago\n";

        final CSVParser parser = CSVParser.parse(new File("test.csv"), CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Alice", "25", "New York"}, record1.toArray());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Bob", "30", "Chicago"}, record2.toArray());

        // Should throw an IOException
        parser.nextRecord();
    }

    /**
     * Test parse method with null input.
     *
     * @throws IOException on error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseNullInput() throws IOException {
        final CSVParser parser = CSVParser.parse(null, CSVFormat.DEFAULT.withHeader());
    }

    /**
     * Test parse method with null format.
     *
     * @throws IOException on error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFormat() throws IOException {
        final String csv = "name,age,city\nAlice,25,New York\nBob,30,Chicago";

        final CSVParser parser = CSVParser.parse(new File("test.csv"), null);
    }

    /**
     * Test getRecords method with valid input.
     *
     * @throws IOException on error
     */
    @Test
    public void testGetRecords() throws IOException {
        final String csv = "name,age,city\nAlice,25,New York\nBob,30,Chicago";

        final CSVParser parser = CSVParser.parse(new File("test.csv"), CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Alice", "25", "New York"}, record1.toArray());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Bob", "30", "Chicago"}, record2.toArray());
    }

    /**
     * Test getRecords method with empty CSV file.
     *
     * @throws IOException on error
     */
    @Test
    public void testGetRecordsEmptyCSV() throws IOException {
        final CSVParser parser = CSVParser.parse(new File("test.csv"), CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = parser.getRecords();
        assertTrue(records.isEmpty());
    }

    /**
     * Test getRecords method with null input.
     *
     * @throws IOException on error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetRecordsNullInput() throws IOException {
        final CSVParser parser = CSVParser.parse(null, CSVFormat.DEFAULT.withHeader());
        parser.getRecords();
    }

    /**
     * Test getRecords method with null format.
     *
     * @throws IOException on error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetRecordsNullFormat() throws IOException {
        final CSVParser parser = CSVParser.parse(new File("test.csv"), null);
        parser.getRecords();
    }

    /**
     * Test getHeaderMap method with valid input.
     *
     * @throws IOException on error
     */
    @Test
    public void testGetHeaderMap() throws IOException {
        final String csv = "name,age,city\nAlice,25,New York\nBob,30,Chicago";

        final CSVParser parser = CSVParser.parse(new File("test.csv"), CSVFormat.DEFAULT.withHeader());
        Map<String, Integer> headerMap = parser.getHeaderMap();
        Map<String, Integer> expectedHeaderMap = new HashMap<>();
        expectedHeaderMap.put("name", 0);
        expectedHeaderMap.put("age", 1);
        expectedHeaderMap.put("city", 2);

        assertEquals(expectedHeaderMap, headerMap);
    }

    /**
     * Test getHeaderMap method with no header.
     *
     * @throws IOException on error
     */
    @Test
    public void testGetHeaderMapNoHeader() throws IOException {
        final String csv = "Alice,25\nBob,30";

        final CSVParser parser = CSVParser.parse(new File("test.csv"), CSVFormat.DEFAULT.withoutHeader());
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertNull(headerMap);
    }

    /**
     * Test getHeaderMap method with null input.
     *
     * @throws IOException on error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetHeaderMapNullInput() throws IOException {
        final CSVParser parser = CSVParser.parse(null, CSVFormat.DEFAULT.withHeader());
        parser.getHeaderMap();
    }

    /**
     * Test getHeaderMap method with null format.
     *
     * @throws IOException on error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetHeaderMapNullFormat() throws IOException {
        final CSVParser parser = CSVParser.parse(new File("test.csv"), null);
        parser.getHeaderMap();
    }

    /**
     * Test nextRecord method with valid input.
     *
     * @throws IOException on error
     */
    @Test
    public void testNextRecordValidInput() throws IOException {
        final String csv = "name,age,city\nAlice,25,New York\nBob,30,Chicago";

        final CSVParser parser = CSVParser.parse(new File("test.csv"), CSVFormat.DEFAULT.withHeader());
        CSVRecord record1 = parser.nextRecord();
        CSVRecord record2 = parser.nextRecord();

        assertSame(CSVRecord.class, record1.getClass());
        assertSame(CSVRecord.class, record2.getClass());

        CSVRecord expectedRecord1 = new CSVRecord(new String[]{"Alice", "25", "New York"}, new HashMap<>(), null, 1);
        CSVRecord expectedRecord2 = new CSVRecord(new String[]{"Bob", "30", "Chicago"}, new HashMap<>(), null, 2);

        assertArrayEquals(expectedRecord1.toArray(), record1.toArray());
        assertArrayEquals(expectedRecord2.toArray(), record2.toArray());
    }

    /**
     * Test nextRecord method with empty CSV file.
     *
     * @throws IOException on error
     */
    @Test
    public void testNextRecordEmptyCSV() throws IOException {
        final CSVParser parser = CSVParser.parse(new File("test.csv"), CSVFormat.DEFAULT.withHeader());
        CSVRecord record = parser.nextRecord();
        assertNull(record);
    }

    /**
     * Test nextRecord method with null input.
     *
     * @throws IOException on error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextRecordNullInput() throws IOException {
        final CSVParser parser = CSVParser.parse(null, CSVFormat.DEFAULT.withHeader());
        parser.nextRecord();
    }

    /**
     * Test nextRecord method with null format.
     *
     * @throws IOException on error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextRecordNullFormat() throws IOException {
        final CSVParser parser = CSVParser.parse(new File("test.csv"), null);
        parser.nextRecord();
    }

    /**
     * Test nextRecord method with invalid input.
     *
     * @throws IOException on error
     */
    @Test(expected = IOException.class)
    public void testNextRecordInvalidInput() throws IOException {
        final String csv = "name,age,city\nAlice,25,New York\nBob,30,Chicago";

        final CSVParser parser = CSVParser.parse(new File("test.csv"), CSVFormat.DEFAULT.withHeader());
        CSVRecord record = parser.nextRecord();
        CSVRecord record1 = parser.nextRecord();
        CSVRecord record2 = parser.nextRecord();

        assertArrayEquals(new String[]{"Alice", "25", "New York"}, record.toArray());
        assertArrayEquals(new String[]{"Alice", "25", "New York"}, record1.toArray());
        assertArrayEquals(new String[]{"Bob", "30", "Chicago"}, record2.toArray());

        // Should throw an IOException
        parser.nextRecord();
    }
}