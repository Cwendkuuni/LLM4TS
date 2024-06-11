package org.apache.commons.csv;

import static org.apache.commons.csv.CSVRecord.class$;
import static org.apache.commons.csv.CSVParser.parse;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVParserTest {

    /**
     * Test method for {@link CSVParser#parse(java.io.File, CSVFormat)}
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testParseFile() throws IOException {
        String filePath = "src/test/resources/CSVParserTest/CSVParserTest.csv";
        CSVFormat csvFormat = new CSVFormat();
        CSVParser parser = parse(new File(filePath), csvFormat);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(13, records.size());

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Name1", "Value1", "Value2", "Value3"}, record1.values());
        assertEquals(0, record1.getRecordNumber());

        CSVRecord record12 = records.get(12);
        assertArrayEquals(new String[]{"Name13", "Value13", "Value14", "Value15"}, record12.values());
        assertEquals(12, record12.getRecordNumber());

        parser.close();
    }

    /**
     * Test method for {@link CSVParser#parse(java.io.InputStreamReader)}
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testParseInputStream() throws IOException {
        String filePath = "src/test/resources/CSVParserTest/CSVParserTest.csv";
        List<String> lines = Files.readAllLines(Paths.get(filePath), Charset.forName("UTF-8"));
        String csvContent = String.join(System.lineSeparator(), lines);

        CSVFormat csvFormat = new CSVFormat();
        CSVParser parser = new CSVParser(new InputStreamReader(new StringReader(csvContent), Charset.forName("UTF-8")),
                csvFormat);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(13, records.size());

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Name1", "Value1", "Value2", "Value3"}, record1.values());
        assertEquals(0, record1.getRecordNumber());

        CSVRecord record12 = records.get(12);
        assertArrayEquals(new String[]{"Name13", "Value13", "Value14", "Value15"}, record12.values());
        assertEquals(12, record12.getRecordNumber());

        parser.close();
    }

    /**
     * Test method for {@link CSVParser#parse(String, CSVFormat)}
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testParseString() throws IOException {
        String csvContent = "Name1,Value1,Value2,Value3\n" + "Name2,Value4,Value5,Value6\n" + "Name3,Value7,Value8,Value9\n"
                + "Name13,Value13,Value14,Value15";

        CSVFormat csvFormat = new CSVFormat();
        CSVParser parser = parse(csvContent, csvFormat);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(13, records.size());

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Name1", "Value1", "Value2", "Value3"}, record1.values());
        assertEquals(0, record1.getRecordNumber());

        CSVRecord record12 = records.get(12);
        assertArrayEquals(new String[]{"Name13", "Value13", "Value14", "Value15"}, record12.values());
        assertEquals(12, record12.getRecordNumber());

        parser.close();
    }

    /**
     * Test method for {@link CSVParser#nextRecord()}
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testNextRecord() throws IOException {
        String filePath = "src/test/resources/CSVParserTest/CSVParserTest.csv";
        CSVFormat csvFormat = new CSVFormat();
        CSVParser parser = parse(new File(filePath), csvFormat);

        CSVRecord record1 = parser.nextRecord();
        assertNull(record1);

        CSVRecord record2 = parser.nextRecord();
        assertArrayEquals(new String[]{"Name1", "Value1", "Value2", "Value3"}, record2.values());
        assertEquals(0, record2.getRecordNumber());

        CSVRecord record13 = parser.nextRecord();
        assertArrayEquals(new String[]{"Name13", "Value13", "Value14", "Value15"}, record13.values());
        assertEquals(12, record13.getRecordNumber());

        CSVRecord record14 = parser.nextRecord();
        assertNull(record14);

        parser.close();
    }

    /**
     * Test method for {@link CSVParser#hasNext()}
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testHasNext() throws IOException {
        String filePath = "src/test/resources/CSVParserTest/CSVParserTest.csv";
        CSVFormat csvFormat = new CSVFormat();
        CSVParser parser = parse(new File(filePath), csvFormat);

        assertFalse(parser.hasNext());

        parser.nextRecord();

        assertTrue(parser.hasNext());

        parser.nextRecord();

        assertTrue(parser.hasNext());

        parser.nextRecord();

        assertFalse(parser.hasNext());

        parser.close();
    }

    /**
     * Test method for {@link CSVParser#iterator()}
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testIterator() throws IOException {
        String filePath = "src/test/resources/CSVParserTest/CSVParserTest.csv";
        CSVFormat csvFormat = new CSVFormat();
        CSVParser parser = parse(new File(filePath), csvFormat);

        List<CSVRecord> records = new ArrayList<CSVRecord>();
        for (CSVRecord record : parser) {
            records.add(record);
        }

        assertEquals(13, records.size());

        parser.close();

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Name1", "Value1", "Value2", "Value3"}, record1.values());
        assertEquals(0, record1.getRecordNumber());

        CSVRecord record12 = records.get(12);
        assertArrayEquals(new String[]{"Name13", "Value13", "Value14", "Value15"}, record12.values());
        assertEquals(12, record12.getRecordNumber());
    }

    /**
     * Test method for {@link CSVParser#getRecords()}
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testGetRecords() throws IOException {
        String filePath = "src/test/resources/CSVParserTest/CSVParserTest.csv";
        CSVFormat csvFormat = new CSVFormat();
        CSVParser parser = parse(new File(filePath), csvFormat);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(13, records.size());

        parser.close();

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Name1", "Value1", "Value2", "Value3"}, record1.values());
        assertEquals(0, record1.getRecordNumber());

        CSVRecord record12 = records.get(12);
        assertArrayEquals(new String[]{"Name13", "Value13", "Value14", "Value15"}, record12.values());
        assertEquals(12, record12.getRecordNumber());
    }

    /**
     * Test method for {@link CSVParser#getHeaderMap()}
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testGetHeaderMap() throws IOException {
        String filePath = "src/test/resources/CSVParserTest/CSVParserTest.csv";
        CSVFormat csvFormat = new CSVFormat();
        CSVParser parser = parse(new File(filePath), csvFormat);

        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertEquals(Collections.emptyMap(), headerMap);

        parser.close();
    }

    /**
     * Test method for {@link CSVParser#getRecordNumber()}
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testGetRecordNumber() throws IOException {
        String filePath = "src/test/resources/CSVParserTest/CSVParserTest.csv";
        CSVFormat csvFormat = new CSVFormat();
        CSVParser parser = parse(new File(filePath), csvFormat);

        CSVRecord record1 = parser.nextRecord();
        assertEquals(0, record1.getRecordNumber());

        CSVRecord record12 = parser.nextRecord();
        assertEquals(12, record12.getRecordNumber());

        parser.close();
    }
}