package org.apache.commons.csv.test;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVParser.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;
import org.junit.Test;

/**
 * Test class for CSVParser.
 * @author <a href="mailto:gene@thegenehunter.net">Gene Homboy</a>
 */
public class CSVParserTest {

    /**
     * Test constructor.
     * @throws IOException
     */
    @Test
    public void testCSVParser() throws IOException {
        final CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader();
        final CSVParser csvParser = parse(new StringReader(""), csvFormat);
        assertSame(csvFormat, csvParser.getFormat());
        assertFalse(csvParser.isClosed());
        assertTrue(csvParser.iterator().hasNext());
    }

    /**
     * Test parse file.
     * @throws IOException
     */
    @Test
    public void testParseFile() throws IOException {
        final String filePath = "src/test/resources/testCSV.csv";
        final CSVRecord record = new CSVRecord(new String[]{"Name", "Age"}, Arrays.asList("John Doe", "25"));
        final List<CSVRecord> records = Arrays.asList(record);
        final CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader();
        final CSVParser csvParser = parse(new File(filePath), csvFormat);
        assertSame(csvFormat, csvParser.getFormat());
        assertFalse(csvParser.isClosed());
        assertTrue(csvParser.iterator().hasNext());
        final CSVRecord parsedRecord = csvParser.nextRecord();
        assertSame(records.get(0), parsedRecord);
        csvParser.close();
    }

    /**
     * Test parse String.
     * @throws IOException
     */
    @Test
    public void testParseString() throws IOException {
        final String csvString = "Name,Age\nJohn Doe,25\nJane Doe,30";
        final CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader();
        final CSVParser csvParser = parse(new StringReader(csvString), csvFormat);
        assertSame(csvFormat, csvParser.getFormat());
        assertFalse(csvParser.isClosed());
        assertTrue(csvParser.iterator().hasNext());
        final CSVRecord parsedRecord = csvParser.nextRecord();
        assertEquals("Name", parsedRecord.get(0));
        assertEquals("Age", parsedRecord.get(1));
        assertEquals("John Doe", parsedRecord.get(2));
        assertEquals(25, parsedRecord.getInteger(3));
        csvParser.close();
    }

    /**
     * Test parse URL.
     * @throws IOException
     */
    @Test
    public void testParseURL() throws IOException {
        final String urlString = "http://example.com/testCSV.csv";
        final CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader();
        final CSVParser csvParser = parse(new URL(urlString), csvFormat);
        assertSame(csvFormat, csvParser.getFormat());
        assertFalse(csvParser.isClosed());
        assertTrue(csvParser.iterator().hasNext());
        final CSVRecord parsedRecord = csvParser.nextRecord();
        assertEquals("Name", parsedRecord.get(0));
        assertEquals("Age", parsedRecord.get(1));
        assertEquals("John Doe", parsedRecord.get(2));
        assertEquals(25, parsedRecord.getInteger(3));
        csvParser.close();
    }

    /**
     * Test parse InputStream.
     * @throws IOException
     */
    @Test
    public void testParseInputStream() throws IOException {
        final String csvString = "Name,Age\nJohn Doe,25\nJane Doe,30";
        final InputStream inputStream = new ByteArrayInputStream(csvString.getBytes());
        final CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader();
        final CSVParser csvParser = parse(new InputStreamReader(inputStream), csvFormat);
        assertSame(csvFormat, csvParser.getFormat());
        assertFalse(csvParser.isClosed());
        assertTrue(csvParser.iterator().hasNext());
        final CSVRecord parsedRecord = csvParser.nextRecord();
        assertEquals("Name", parsedRecord.get(0));
        assertEquals("Age", parsedRecord.get(1));
        assertEquals("John Doe", parsedRecord.get(2));
        assertEquals(25, parsedRecord.getInteger(3));
        csvParser.close();
    }

    /**
     * Test getRecords.
     * @throws IOException
     */
    @Test
    public void testGetRecords() throws IOException {
        final String csvString = "Name,Age\nJohn Doe,25\nJane Doe,30";
        final CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader();
        final CSVParser csvParser = parse(new StringReader(csvString), csvFormat);
        final List<CSVRecord> records = csvParser.getRecords();
        assertFalse(csvParser.isClosed());
        assertFalse(csvParser.iterator().hasNext());
        assertEquals(2, records.size());
        assertEquals("Name", records.get(0).get(0));
        assertEquals("Name", records.get(1).get(0));
        assertEquals("Age", records.get(0).get(1));
        assertEquals("Age", records.get(1).get(1));
        assertEquals("John Doe", records.get(0).get(2));
        assertEquals("Jane Doe", records.get(1).get(2));
        assertEquals(25, records.get(0).getInteger(3));
        assertEquals(30, records.get(1).getInteger(3));
        csvParser.close();
    }

    /**
     * Test getHeaderMap.
     * @throws IOException
     */
    @Test
    public void testGetHeaderMap() throws IOException {
        final String csvString = "Name,Age\nJohn Doe,25\nJane Doe,30";
        final CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader();
        final CSVParser csvParser = parse(new StringReader(csvString), csvFormat);
        final Map<String, Integer> headerMap = csvParser.getHeaderMap();
        assertFalse(csvParser.isClosed());
        assertEquals(2, headerMap.size());
        assertEquals("Name", headerMap.get("Name"));
        assertEquals("Age", headerMap.get("Age"));
        csvParser.close();
    }

    /**
     * Test getRecordNumber.
     * @throws IOException
     */
    @Test
    public void testGetRecordNumber() throws IOException {
        final String csvString = "Name,Age\nJohn Doe,25\nJane Doe,30";
        final CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader();
        final CSVParser csvParser = parse(new StringReader(csvString), csvFormat);
        final long recordNumber = csvParser.getRecordNumber();
        assertEquals(0, recordNumber);
        csvParser.nextRecord();
        assertEquals(1, recordNumber);
        csvParser.nextRecord();
        assertEquals(2, recordNumber);
        csvParser.close();
    }

    /**
     * Test iterator.
     * @throws IOException
     */
    @Test
    public void testIterator() throws IOException {
        final String csvString = "Name,Age\nJohn Doe,25\nJane Doe,30";
        final CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader();
        final CSVParser csvParser = parse(new StringReader(csvString), csvFormat);
        final List<CSVRecord> records = new ArrayList<>();
        for (final CSVRecord record : csvParser) {
            records.add(record);
        }
        assertEquals(2, records.size());
        csvParser.close();
    }

    /**
     * Test isClosed.
     * @throws IOException
     */
    @Test
    public void testIsClosed() throws IOException {
        final String csvString = "Name,Age\nJohn Doe,25\nJane Doe,30";
        final CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader();
        final CSVParser csvParser = parse(new StringReader(csvString), csvFormat);
        assertFalse(csvParser.isClosed());
        csvParser.close();
        assertTrue(csvParser.isClosed());
    }

    /**
     * Test getRecords with empty CSV.
     * @throws IOException
     */
    @Test
    public void testGetRecordsEmptyCSV() throws IOException {
        final String csvString = "";
        final CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader();
        final CSVParser csvParser = parse(new StringReader(csvString), csvFormat);
        final List<CSVRecord> records = csvParser.getRecords();
        assertFalse(csvParser.isClosed());
        assertEquals(0, records.size());
        csvParser.close();
    }

    /**
     * Test getRecords with no header CSV.
     * @throws IOException
     */
    @Test
    public void testGetRecordsNoHeaderCSV() throws IOException {
        final String csvString = "Name, Age\nJohn, 25\nJane, 30";
        final CSVFormat csvFormat = CSVFormat.DEFAULT.withNoHeader();
        final CSVParser csvParser = parse(new StringReader(csvString), csvFormat);
        final List<CSVRecord> records = csvParser.getRecords();
        assertFalse(csvParser.isClosed());
        assertEquals(2, records.size());
        assertEquals(0, records.get(0).size());
        assertEquals(2, records.get(1).size());
        assertEquals("John", records.get(0).get(0));
        assertEquals("25", records.get(0).get(1));
        assertEquals("Jane", records.get(1).get(0));
        assertEquals("30", records.get(1).get(1));
        csvParser.close();
    }

    /**
     * Test getRecords with header and no data CSV.
     * @throws IOException
     */
    @Test
    public void testGetRecordsHeaderNoDataCSV() throws IOException {
        final String csvString = "Name, Age";
        final CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader();
        final CSVParser csvParser = parse(new StringReader(csvString), csvFormat);
        final List<CSVRecord> records = csvParser.getRecords();
        assertFalse(csvParser.isClosed());
        assertEquals(0, records.size());
        csvParser.close();
    }

    /**
     * Test getRecords with CSV containing only a comment.
     * @throws IOException
     */
    @Test
    public void testGetRecordsOnlyCommentCSV() throws IOException {
        final String csvString = "# This is a comment";
        final CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader();
        final CSVParser csvParser = parse(new StringReader(csvString), csvFormat);
        final List<CSVRecord> records = csvParser.getRecords();
        assertEquals(0, records.size());
        csvParser.close();
    }
}