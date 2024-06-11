package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Test class for {@link CSVParser}.
 */
public class CSVParserTest {

    /**
     * Test method for {@link CSVParser#parse(File, CSVFormat)}.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @org.junit.Test
    public void testParseFile() throws IOException {
        // given
        final File inputFile = new File("src/test/resources/csv/testParseFile.csv");
        final CSVFormat format = CSVFormat.DEFAULT;

        // when
        final CSVParser parser = CSVParser.parse(inputFile, format);

        // then
        assertTrue(parser.isOpen());
        assertEquals(2, parser.getRecordNumber());
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        assertEquals("Header1", record1.get(0));
        assertEquals("Header2", record1.get(1));
        assertEquals(1, record1.getRecordNumber());
        assertNull(record1.getComment());

        assertEquals("Value1", record2.get(0));
        assertEquals("Value2", record2.get(1));
        assertEquals(2, record2.getRecordNumber());
        assertEquals("This is a comment", record2.getComment());

        parser.close();
        assertFalse(parser.isOpen());
    }

    /**
     * Test method for {@link CSVParser#parse(String, CSVFormat)}.
     */
    @org.junit.Test
    public void testParseString() throws IOException {
        // given
        final String input = "Header1,Header2\nValue1,Value2\n";
        final CSVFormat format = CSVFormat.DEFAULT;

        // when
        final CSVParser parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(input.getBytes())), format);

        // then
        assertEquals(2, parser.getRecordNumber());
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        assertEquals("Header1", record1.get(0));
        assertEquals("Header2", record1.get(1));
        assertEquals(1, record1.getRecordNumber());
        assertNull(record1.getComment());

        assertEquals("Value1", record2.get(0));
        assertEquals("Value2", record2.get(1));
        assertEquals(2, record2.getRecordNumber());
        assertEquals("", record2.getComment());

        parser.close();
    }

    /**
     * Test method for {@link CSVParser#parse(URL, Charset, CSVFormat)}.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @org.junit.Test
    public void testParseUrl() throws IOException {
        // given
        final String urlString = "classpath:csv/testParseUrl.csv";
        final Charset charset = Charset.forName("UTF-8");
        final CSVFormat format = CSVFormat.DEFAULT;

        // when
        final CSVParser parser = CSVParser.parse(new URL(urlString), charset, format);

        // then
        assertTrue(parser.isOpen());
        assertEquals(3, parser.getRecordNumber());
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        CSVRecord record3 = records.get(2);

        assertEquals("Header1", record1.get(0));
        assertEquals("Header2", record1.get(1));
        assertEquals(1, record1.getRecordNumber());
        assertNull(record1.getComment());

        assertEquals("Value1", record2.get(0));
        assertEquals("Value2", record2.get(1));
        assertEquals(2, record2.getRecordNumber());
        assertEquals("", record2.getComment());

        assertEquals("Value3", record3.get(0));
        assertEquals("Value4", record3.get(1));
        assertEquals(3, record3.getRecordNumber());
        assertEquals("Comment3", record3.getComment());

        parser.close();
        assertFalse(parser.isOpen());
    }

    /**
     * Test method for {@link CSVParser#getRecords()}.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @org.junit.Test
    public void testGetRecords() throws IOException {
        // given
        final File inputFile = new File("src/test/resources/csv/testParseFile.csv");
        final CSVFormat format = CSVFormat.DEFAULT;

        // when
        final CSVParser parser = CSVParser.parse(inputFile, format);
        List<CSVRecord> records = parser.getRecords();

        // then
        assertEquals(2, records.size());

        parser.close();
    }

    /**
     * Test method for {@link CSVParser#iterator()}.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @org.junit.Test
    public void testIterator() throws IOException {
        // given
        final File inputFile = new File("src/test/resources/csv/testParseFile.csv");
        final CSVFormat format = CSVFormat.DEFAULT;

        // when
        final CSVParser parser = CSVParser.parse(inputFile, format);
        Iterator<CSVRecord> iterator = parser.iterator();

        // then
        assertTrue(iterator.hasNext());
        CSVRecord record1 = iterator.next();
        assertEquals("Header1", record1.get(0));
        assertEquals("Header2", record1.get(1));
        assertEquals(1, record1.getRecordNumber());
        assertNull(record1.getComment());

        assertTrue(iterator.hasNext());
        CSVRecord record2 = iterator.next();
        assertEquals("Value1", record2.get(0));
        assertEquals("Value2", record2.get(1));
        assertEquals(2, record2.getRecordNumber());
        assertEquals("This is a comment", record2.getComment());

        assertFalse(iterator.hasNext());

        parser.close();
    }

    /**
     * Test method for {@link CSVParser#getRecords(List)}.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @org.junit.Test
    public void testGetRecordsList() throws IOException {
        // given
        final File inputFile = new File("src/test/resources/csv/testParseFile.csv");
        final CSVFormat format = CSVFormat.DEFAULT;

        // when
        final List<CSVRecord> records = new ArrayList<>();
        CSVParser parser = CSVParser.parse(inputFile, format);
        parser.getRecords(records);

        // then
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        assertEquals("Header1", record1.get(0));
        assertEquals("Header2", record1.get(1));
        assertEquals(1, record1.getRecordNumber());
        assertNull(record1.getComment());

        assertEquals("Value1", record2.get(0));
        assertEquals("Value2", record2.get(1));
        assertEquals(2, record2.getRecordNumber());
        assertEquals("This is a comment", record2.getComment());

        parser.close();
    }

    /**
     * Test method for {@link CSVParser#getRecords(Collection)}.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @org.junit.Test
    public void testGetRecordsCollection() throws IOException {
        // given
        final File inputFile = new File("src/test/resources/csv/testParseFile.csv");
        final CSVFormat format = CSVFormat.DEFAULT;

        // when
        final List<CSVRecord> records = Collections.synchronizedList(new ArrayList<>());
        CSVParser parser = CSVParser.parse(inputFile, format);
        parser.getRecords(records);

        // then
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        assertEquals("Header1", record1.get(0));
        assertEquals("Header2", record1.get(1));
        assertEquals(1, record1.getRecordNumber());
        assertNull(record1.getComment());

        assertEquals("Value1", record2.get(0));
        assertEquals("Value2", record2.get(1));
        assertEquals(2, record2.getRecordNumber());
        assertEquals("This is a comment", record2.getComment());

        parser.close();
    }

    /**
     * Test method for {@link CSVParser#getRecords(Collection, int)}.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @org.junit.Test
    public void testGetRecordsCollectionLimit() throws IOException {
        // given
        final File inputFile = new File("src/test/resources/csv/testParseFile.csv");
        final CSVFormat format = CSVFormat.DEFAULT;

        // when
        final List<CSVRecord> records = new ArrayList<>();
        CSVParser parser = CSVParser.parse(inputFile, format);
        parser.getRecords(records, 1);

        // then
        assertEquals(1, records.size());
        CSVRecord record1 = records.get(0);
        assertEquals("Header1", record1.get(0));
        assertEquals("Header2", record1.get(1));
        assertEquals(1, record1.getRecordNumber());
        assertNull(record1.getComment());

        parser.close();
    }

    /**
     * Test method for {@link CSVParser#getRecords(Collection, int, String[])}.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @org.junit.Test
    public void testGetRecordsCollectionLimitHeaders() throws IOException {
        // given
        final File inputFile = new File("src/test/resources/csv/testParseFile.csv");
        final CSVFormat format = CSVFormat.DEFAULT;

        // when
        final List<CSVRecord> records = Arrays.asList(new CSVRecord[3]);
        final String[] headers = {"Header1", "Header2"};
        CSVParser parser = CSVParser.parse(inputFile, format);
        parser.getRecords(records, 1, headers);

        // then
        assertEquals(1, records.size());
        CSVRecord record1 = records.get(0);
        assertEquals("Header1", record1.get(0));
        assertEquals("Header2", record1.get(1));
        assertEquals(1, record1.getRecordNumber());
        assertNull(record1.getComment());

        parser.close();
    }

}