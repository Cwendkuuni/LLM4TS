package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;

/**
 * JUnit 4 test file for the CSVParser class.
 */
public class CSVParserTest {

    /**
     * Tests the creation of a CSVParser instance using a File.
     *
     * @throws IOException on failure
     */
    @org.junit.Test
    public void testParseFile() throws IOException {
        final File file = new File("src/test/resources/test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        assertEquals(3, records.size());

        CSVRecord record0 = records.get(0);
        assertNull(record0.getComment());
        assertEquals(4, record0.size());
        assertEquals("Name1", record0.get(0));
        assertEquals("Value1", record0.get(1));
        assertEquals("Value2", record0.get(2));
        assertEquals("Value3", record0.get(3));

        CSVRecord record1 = records.get(1);
        assertSame(record1.getComment(), record0.getComment());
        assertEquals(4, record1.size());
        assertEquals("Name2", record1.get(0));
        assertEquals("Value4", record1.get(1));
        assertEquals("Value5", record1.get(2));
        assertEquals("Value6", record1.get(3));

        CSVRecord record2 = records.get(2);
        assertSame(record2.getComment(), record0.getComment());
        assertEquals(3, record2.size());
        assertEquals("Name3", record2.get(0));
        assertEquals("Value7", record2.get(1));
        assertNull(record2.get(2));

        parser.close();
    }

    /**
     * Tests the creation of a CSVParser instance using a String.
     *
     * @throws IOException on failure
     */
    @org.junit.Test
    public void testParseString() throws IOException {
        CSVParser parser = CSVParser.parse(
                "Name1,Value1,Value2\n"
                + "Name2,Value4,Value5,Value6\n"
                + "Name3,Value7",
            CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        assertEquals(3, records.size());

        CSVRecord record0 = records.get(0);
        assertNull(record0.getComment());
        assertEquals(4, record0.size());
        assertEquals("Name1", record0.get(0));
        assertEquals("Value1", record0.get(1));
        assertEquals("Value2", record0.get(2));
        assertEquals("Value3", record0.get(3));

        CSVRecord record1 = records.get(1);
        assertSame(record1.getComment(), record0.getComment());
        assertEquals(4, record1.size());
        assertEquals("Name2", record1.get(0));
        assertEquals("Value4", record1.get(1));
        assertEquals("Value5", record1.get(2));
        assertEquals("Value6", record1.get(3));

        CSVRecord record2 = records.get(2);
        assertSame(record2.getComment(), record0.getComment());
        assertEquals(3, record2.size());
        assertEquals("Name3", record2.get(0));
        assertEquals("Value7", record2.get(1));
        assertNull(record2.get(2));

        parser.close();
    }

    /**
     * Tests the creation of a CSVParser instance using a URL.
     *
     * @throws IOException on failure
     */
    @org.junit.Test
    public void testParseURL() throws IOException {
        CSVParser parser = CSVParser.parse(
                new URL("classpath:test/resources/test.csv"),
                CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        assertEquals(3, records.size());

        CSVRecord record0 = records.get(0);
        assertNull(record0.getComment());
        assertEquals(4, record0.size());
        assertEquals("Name1", record0.get(0));
        assertEquals("Value1", record0.get(1));
        assertEquals("Value2", record0.get(2));
        assertEquals("Value3", record0.get(3));

        CSVRecord record1 = records.get(1);
        assertSame(record1.getComment(), record0.getComment());
        assertEquals(4, record1.size());
        assertEquals("Name2", record1.get(0));
        assertEquals("Value4", record1.get(1));
        assertEquals("Value5", record1.get(2));
        assertEquals("Value6", record1.get(3));

        CSVRecord record2 = records.get(2);
        assertSame(record2.getComment(), record0.getComment());
        assertEquals(3, record2.size());
        assertEquals("Name3", record2.get(0));
        assertEquals("Value7", record2.get(1));
        assertNull(record2.get(2));

        parser.close();
    }

    /**
     * Tests the creation of a CSVParser instance using a Reader.
     *
     * @throws IOException on failure
     */
    @org.junit.Test
    public void testParseReader() throws IOException {
        String csvContent = "Name1,Value1,Value2\n"
                + "Name2,Value4,Value5,Value6\n"
                + "Name3,Value7";

        InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes()));
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        assertEquals(3, records.size());

        CSVRecord record0 = records.get(0);
        assertNull(record0.getComment());
        assertEquals(4, record0.size());
        assertEquals("Name1", record0.get(0));
        assertEquals("Value1", record0.get(1));
        assertEquals("Value2", record0.get(2));
        assertEquals("Value3", record0.get(3));

        CSVRecord record1 = records.get(1);
        assertSame(record1.getComment(), record0.getComment());
        assertEquals(4, record1.size());
        assertEquals("Name2", record1.get(0));
        assertEquals("Value4", record1.get(1));
        assertEquals("Value5", record1.get(2));
        assertEquals("Value6", record1.get(3));

        CSVRecord record2 = records.get(2);
        assertSame(record2.getComment(), record0.getComment());
        assertEquals(3, record2.size());
        assertEquals("Name3", record2.get(0));
        assertEquals("Value7", record2.get(1));
        assertNull(record2.get(2));

        parser.close();
        reader.close();
    }

    /**
     * Tests the getRecords() method.
     *
     * @throws IOException on failure
     */
    @org.junit.Test
    public void testGetRecords() throws IOException {
        String csvContent = "Name1,Value1,Value2\n"
                + "Name2,Value4,Value5,Value6\n"
                + "Name3,Value7";

        InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes()));
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);

        List<CSVRecord> allRecords = parser.getRecords();
        List<CSVRecord> firstHalf = new ArrayList<>(allRecords.subList(0, allRecords.size() / 2));

        parser.close();
        reader.close();

        List<CSVRecord> newRecords = parser.getRecords();
        List<CSVRecord> secondHalf = newRecords.subList(0, allRecords.size() / 2);

        assertEquals(allRecords, new ArrayList<>(Arrays.asList(allRecords)));
        assertTrue(firstHalf.equals(secondHalf));
    }

    /**
     * Tests the getRecords() method with an empty CSV file.
     *
     * @throws IOException on failure
     */
    @org.junit.Test
    public void testGetRecordsEmptyFile() throws IOException {
        File emptyFile = new File("src/test/resources/empty.csv");
        CSVParser parser = CSVParser.parse(emptyFile, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertTrue(records.isEmpty());

        parser.close();
    }

    /**
     * Tests the getRecords() method with an invalid CSV file.
     *
     * @throws IOException on failure
     */
    @org.junit.Test(expected = IOException.class)
    public void testGetRecordsInvalidFile() throws IOException {
        File invalidFile = new File("src/test/resources/invalid.csv");
        CSVParser parser = CSVParser.parse(invalidFile, CSVFormat.DEFAULT);

        parser.getRecords();
    }

    /**
     * Tests the getRecords(Collection) method.
     *
     * @throws IOException on failure
     */
    @org.junit.Test
    public void testGetRecordsCollection() throws IOException {
        String csvContent = "Name1,Value1,Value2\n"
                + "Name2,Value4,Value5,Value6\n"
                + "Name3,Value7";

        InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes()));
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);

        List<CSVRecord> result = new ArrayList<>();
        parser.getRecords(result);

        parser.close();
        reader.close();

        assertEquals(3, result.size());
    }

    /**
     * Tests the getRecords(Collection) method with an empty collection.
     */
    @org.junit.Test
    public void testGetRecordsCollectionEmpty() {
        CSVParser parser = CSVParser.parse(
                new StringReader(""),
                CSVFormat.DEFAULT);

        List<CSVRecord> result = new ArrayList<>();
        parser.getRecords(result);

        parser.close();

        assertTrue(Collections.isEmpty(result));
    }

    /**
     * Tests the getRecords(Collection) method with an invalid CSV stream.
     *
     * @throws IOException on failure
     */
    @org.junit.Test(expected = IOException.class)
    public void testGetRecordsCollectionInvalid() throws IOException {
        InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream("invalid".getBytes()));
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);

        List<CSVRecord> result = new ArrayList<>();
        parser.getRecords(result);
    }
}