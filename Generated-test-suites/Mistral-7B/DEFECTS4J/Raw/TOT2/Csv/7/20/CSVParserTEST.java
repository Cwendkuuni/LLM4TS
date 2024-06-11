package org.apache.commons.csv;

import static org.apache.commons.csv.CSVParser.parse;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Test cases for {@link CSVParser} class.
 */
public class CSVParserTest {

    /**
     * Test method for {@link CSVParser#parse(java.io.File, CSVFormat)} using RFC4180 format.
     */
    @org.junit.Test
    public void testParseFileRFC4180() throws IOException {
        final File file = new File("src/test/resources/csv/rfc4180.csv");
        final List<CSVRecord> records = parse(file, CSVFormat.RFC4180);
        assertEquals(5, records.size());

        final CSVRecord firstRecord = records.get(0);
        assertArrayEquals(new String[]{"Name1", "Value1", "Value2", "Value3"}, firstRecord.toArray());
        assertEquals("Name1", firstRecord.getHeaderMap().get("Name1"));
        assertNull(firstRecord.getComment());
        assertEquals(1, firstRecord.getRecordNumber());

        final CSVRecord lastRecord = records.get(4);
        assertArrayEquals(new String[]{"Name5", "Value5"}, lastRecord.toArray());
        assertSame("Name5", lastRecord.getHeaderMap().get("Name5"));
        assertNull(lastRecord.getComment());
        assertEquals(5, lastRecord.getRecordNumber());
    }

    /**
     * Test method for {@link CSVParser#parse(java.io.File, CSVFormat)} using EXCEL format.
     */
    @org.junit.Test
    public void testParseFileEXCEL() throws IOException {
        final File file = new File("src/test/resources/csv/excel.csv");
        final List<CSVRecord> records = parse(file, CSVFormat.EXCEL);
        assertEquals(3, records.size());

        final CSVRecord firstRecord = records.get(0);
        assertArrayEquals(new String[]{"Empty", "1", "2"}, firstRecord.toArray());
        assertNull(firstRecord.getHeaderMap());
        assertNull(firstRecord.getComment());
        assertEquals(1, firstRecord.getRecordNumber());

        final CSVRecord secondRecord = records.get(1);
        assertArrayEquals(new String[]{"Name2", "Value2.1", "Value2.2"}, secondRecord.toArray());
        assertNull(secondRecord.getHeaderMap());
        assertNull(secondRecord.getComment());
        assertEquals(2, secondRecord.getRecordNumber());

        final CSVRecord lastRecord = records.get(2);
        assertArrayEquals(new String[]{"Name3", "Value3.1", "Value3.2"}, lastRecord.toArray());
        assertNull(lastRecord.getHeaderMap());
        assertNull(lastRecord.getComment());
        assertEquals(3, lastRecord.getRecordNumber());
    }

    /**
     * Test method for {@link CSVParser#parse(Reader)} using RFC4180 format.
     */
    @org.junit.Test
    public void testParseReaderRFC4180() throws IOException {
        final String csvData = "Name1,Value1,Value2,Value3\n" + "Name1,Value11,Value21,Value31\n"
                + "Name2,Value2,Value22,Value32\n" + "Name2,Value21,Value221,Value321\n";
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(csvData.getBytes());
        final InputStreamReader inputStreamReader = new InputStreamReader(new ByteArrayInputStream(outputStream.toByteArray()));

        final List<CSVRecord> records = parse(inputStreamReader, CSVFormat.RFC4180);
        assertEquals(4, records.size());

        final CSVRecord firstRecord = records.get(0);
        assertArrayEquals(new String[]{"Name1", "Value1", "Value2", "Value3"}, firstRecord.toArray());
        assertNull(firstRecord.getHeaderMap());
        assertNull(firstRecord.getComment());
        assertEquals(1, firstRecord.getRecordNumber());

        final CSVRecord lastRecord = records.get(3);
        assertArrayEquals(new String[]{"Name2", "Value21", "Value221", "Value321"}, lastRecord.toArray());
        assertNull(lastRecord.getHeaderMap());
        assertNull(lastRecord.getComment());
        assertEquals(4, lastRecord.getRecordNumber());
    }

    /**
     * Test method for {@link CSVParser#parse(Reader)} using EXCEL format.
     */
    @org.junit.Test
    public void testParseReaderEXCEL() throws IOException {
        final String csvData = "Empty\n" + "1,2\n" + "Name2,Value2.1,Value2.2\n" + "Name3,Value3.1,Value3.2\n";
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(csvData.getBytes());
        final InputStreamReader inputStreamReader = new InputStreamReader(new ByteArrayInputStream(outputStream.toByteArray()));

        final List<CSVRecord> records = parse(inputStreamReader, CSVFormat.EXCEL);
        assertEquals(3, records.size());

        final CSVRecord firstRecord = records.get(0);
        assertArrayEquals(new String[]{"Empty"}, firstRecord.toArray());
        assertNull(firstRecord.getHeaderMap());
        assertNull(firstRecord.getComment());
        assertEquals(1, firstRecord.getRecordNumber());

        final CSVRecord secondRecord = records.get(1);
        assertArrayEquals(new String[]{"Name2", "Value2.1", "Value2.2"}, secondRecord.toArray());
        assertNull(secondRecord.getHeaderMap());
        assertNull(secondRecord.getComment());
        assertEquals(2, secondRecord.getRecordNumber());

        final CSVRecord lastRecord = records.get(2);
        assertArrayEquals(new String[]{"Name3", "Value3.1", "Value3.2"}, lastRecord.toArray());
        assertNull(lastRecord.getHeaderMap());
        assertNull(lastRecord.getComment());
        assertEquals(3, lastRecord.getRecordNumber());
    }

    /**
     * Test method for {@link CSVParser#parse(InputStreamReader)} using RFC4180 format.
     */
    @org.junit.Test
    public void testParseInputStreamReaderRFC4180() throws IOException {
        final URL url = getClass().getResource("/csv/rfc4180.csv");
        final InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());

        final List<CSVRecord> records = parse(inputStreamReader, CSVFormat.RFC4180);
        assertEquals(5, records.size());

        final CSVRecord firstRecord = records.get(0);
        assertArrayEquals(new String[]{"Name1", "Value1", "Value2", "Value3"}, firstRecord.toArray());
        assertNull(firstRecord.getHeaderMap());
        assertNull(firstRecord.getComment());
        assertEquals(1, firstRecord.getRecordNumber());

        final CSVRecord lastRecord = records.get(4);
        assertArrayEquals(new String[]{"Name5", "Value5"}, lastRecord.toArray());
        assertNull(lastRecord.getHeaderMap());
        assertNull(lastRecord.getComment());
        assertEquals(5, lastRecord.getRecordNumber());

        // Closing the input stream should close the parser
        inputStreamReader.close();
        assertFalse(parse(inputStreamReader, CSVFormat.RFC4180).iterator().hasNext());
    }

    /**
     * Test method for {@link CSVParser#parse(InputStreamReader)} using EXCEL format.
     */
    @org.junit.Test
    public void testParseInputStreamReaderEXCEL() throws IOException {
        final URL url = getClass().getResource("/csv/excel.csv");
        final InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());

        final List<CSVRecord> records = parse(inputStreamReader, CSVFormat.EXCEL);
        assertEquals(3, records.size());

        final CSVRecord firstRecord = records.get(0);
        assertArrayEquals(new String[]{"Empty"}, firstRecord.toArray());
        assertNull(firstRecord.getHeaderMap());
        assertNull(firstRecord.getComment());
        assertEquals(1, firstRecord.getRecordNumber());

        final CSVRecord secondRecord = records.get(1);
        assertArrayEquals(new String[]{"Name2", "Value2.1", "Value2.2"}, secondRecord.toArray());
        assertNull(secondRecord.getHeaderMap());
        assertNull(secondRecord.getComment());
        assertEquals(2, secondRecord.getRecordNumber());

        final CSVRecord lastRecord = records.get(2);
        assertArrayEquals(new String[]{"Name3", "Value3.1", "Value3.2"}, lastRecord.toArray());
        assertNull(lastRecord.getHeaderMap());
        assertNull(lastRecord.getComment());
        assertEquals(3, lastRecord.getRecordNumber());

        // Closing the input stream should close the parser
        inputStreamReader.close();
        assertFalse(parse(inputStreamReader, CSVFormat.EXCEL).iterator().hasNext());
    }

    /**
     * Test method for {@link CSVParser#getRecords()} using RFC4180 format.
     */
    @org.junit.Test
    public void testGetRecordsRFC4180() throws IOException {
        final File file = new File("src/test/resources/csv/rfc4180.csv");
        final List<CSVRecord> records = parse(file, CSVFormat.RFC4180).getRecords();
        assertEquals(5, records.size());

        final CSVRecord firstRecord = records.get(0);
        assertArrayEquals(new String[]{"Name1", "Value1", "Value2", "Value3"}, firstRecord.toArray());
        assertNull(firstRecord.getComment());
        assertSame(firstRecord, records.get(0));

        final CSVRecord lastRecord = records.get(4);
        assertArrayEquals(new String[]{"Name5", "Value5"}, lastRecord.toArray());
        assertNull(lastRecord.getComment());
        assertSame(lastRecord, records.get(4));
    }

    /**
     * Test method for {@link CSVParser#getRecords()} using EXCEL format.
     */
    @org.junit.Test
    public void testGetRecordsEXCEL() throws IOException {
        final File file = new File("src/test/resources/csv/excel.csv");
        final List<CSVRecord> records = parse(file, CSVFormat.EXCEL).getRecords();
        assertEquals(3, records.size());

        final CSVRecord firstRecord = records.get(0);
        assertArrayEquals(new String[]{"Empty"}, firstRecord.toArray());
        assertNull(firstRecord.getComment());
        assertSame(firstRecord, records.get(0));

        final CSVRecord lastRecord = records.get(2);
        assertArrayEquals(new String[]{"Name3", "Value3.1", "Value3.2"}, lastRecord.toArray());
        assertNull(lastRecord.getComment());
        assertSame(lastRecord, records.get(2));
    }

    /**
     * Test method for {@link CSVParser#getRecords()} with empty input.
     */
    @org.junit.Test
    public void testGetRecordsEmpty() throws IOException {
        final String csvData = "";
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(csvData.getBytes());
        final InputStreamReader inputStreamReader = new InputStreamReader(new ByteArrayInputStream(outputStream.toByteArray()));

        final List<CSVRecord> records = parse(inputStreamReader, CSVFormat.RFC4180).getRecords();
        assertEquals(Collections.emptyList(), records);
    }

    /**
     * Test method for {@link CSVParser#close()}.
     */
    @org.junit.Test
    public void testClose() throws IOException {
        final File file = new File("src/test/resources/csv/rfc4180.csv");
        final CSVParser parser = parse(file, CSVFormat.RFC4180);
        parser.close();
        assertFalse(parse(file, CSVFormat.RFC4180).iterator().hasNext());
    }
}