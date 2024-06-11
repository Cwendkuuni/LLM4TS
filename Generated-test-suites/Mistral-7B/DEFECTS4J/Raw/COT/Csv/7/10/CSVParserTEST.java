package org.apache.commons.csv; // update the package name if necessary

import static org.apache.commons.csv.CSVParser.parse;
import static org.apache.commons.csv.CSVRecord.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CSVParserTest {

    private static final String TEST_FILE_PATH = "testData.csv";
    private static final String TEST_STRING = "a,b\nc,d";

    private CSVFormat format;
    private CSVParser parser;

    public CSVParserTest(CSVFormat format) {
        this.format = format;
    }

    @Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { CSVFormat.DEFAULT },
                { CSVFormat.DEFAULT.withHeader(), new File(TEST_FILE_PATH) }, // add file path if necessary
                { CSVFormat.DEFAULT.withHeader().withDelimiter(';'), new StringReader(TEST_STRING) },
                { CSVFormat.EXCEL }
        });
    }

    @Before
    public void setup() throws IOException {
        parser = parse(new StringReader(TEST_STRING), this.format);
    }

    @Test
    public void testHasNextRecord() throws IOException {
        assertTrue(parser.hasNext());
        assertTrue(parser.hasNext());
        assertFalse(parser.hasNext());
        parser.close();
        assertFalse(parser.hasNext());
    }

    @Test
    public void testGetRecords() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        assertArrayEquals(new String[]{"a", "b"}, record1.getValues());
        assertArrayEquals(new String[]{"c", "d"}, record2.getValues());
        assertEquals(1, record1.getRecordNumber());
        assertEquals(2, record2.getRecordNumber());
    }

    @Test
    public void testGetRecordsWithEmptyList() throws IOException {
        List<CSVRecord> records = new ArrayList<>();
        parser.getRecords(records);
        assertTrue(records.isEmpty());
        parser.close();
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertNull(headerMap);

        parser = parse(new StringReader(TEST_STRING + "\na,b\nc,d\nA,B"), this.format);
        headerMap = parser.getHeaderMap();
        assertEquals(2, headerMap.size());
        assertSame("A", headerMap.get("a"));
        assertSame("B", headerMap.get("b"));
    }

    @Test
    public void testGetRecordsWithInvalidFormat() throws IOException {
        try {
            parser = parse(new StringReader("a,b"), null);
            parser.getRecords();
            // this assertion should never be reached
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetRecordsWithNullFormat() throws IOException {
        try {
            parser = parse(new StringReader(TEST_STRING), null);
            parser.getRecords();
            // this assertion should never be reached
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseEmptyFile() throws IOException {
        parser = parse(new FileReader(new File("")), this.format);
        List<CSVRecord> records = parser.getRecords();
        assertTrue(records.isEmpty());
        parser.close();
    }

    @Test
    public void testParseNullReader() {
        try {
            new CSVParser(null, this.format);
            // this assertion should never be reached
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseNullFormat() {
        try {
            new CSVParser(new StringReader(TEST_STRING), null);
            // this assertion should never be reached
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IOException.class)
    public void testParseInvalidInput() throws IOException {
        parser = new CSVParser(new StringReader("invalid"), this.format);
        parser.getRecords();
    }

    @Test
    public void testCloseParser() throws IOException {
        parser.close();
        assertFalse(parser.isOpen());
    }

    @Test
    public void testIterateParser() throws IOException {
        Iterator<CSVRecord> iterator = parser.iterator();
        CSVRecord record = iterator.next();
        assertEquals(record1(0), record);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterateParserWithEmptyList() {
        parser = parse(new StringReader(""), this.format);
        Iterator<CSVRecord> iterator = parser.iterator();
        assertTrue(iterator.isEmpty());
    }

    @Test
    public void testIterateParserWithCustomFormat() throws IOException {
        parser = parse(new StringReader(TEST_STRING), this.format.withQuoteMode(QuoteMode.MINIMAL));
        Iterator<CSVRecord> iterator = parser.iterator();
        CSVRecord record = iterator.next();
        assertArrayEquals(new String[]{"a", "b"}, record.getValues());
        CSVRecord record2 = iterator.next();
        assertArrayEquals(new String[]{"c", "d"}, record2.getValues());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterateParserWithComment() throws IOException {
        parser = parse(new StringReader(TEST_STRING + "\n--Comment"), this.format);
        Iterator<CSVRecord> iterator = parser.iterator();
        CSVRecord record = iterator.next();
        assertArrayEquals(new String[]{"a", "b"}, record.getValues());
        String comment = record.getComment();
        assertEquals("--Comment", comment);
        CSVRecord record2 = iterator.next();
        assertArrayEquals(new String[]{"c", "d"}, record2.getValues());
        assertFalse(iterator.hasNext());
    }

}

class Constants {
    static final String LF = "\n";
}

class RecordBuilder {
    private final List<String> values;

    private RecordBuilder() {
        values = new ArrayList<>();
    }

    static CSVRecord record(int recordNumber, String... values) {
        CSVRecordBuilder builder = new CSVRecordBuilder();
        for (String value : values) {
            builder.addValue(value);
        }
        return builder.build(recordNumber);
    }

    static CSVRecord record(String... values) {
        return record(0, values);
    }

    static CSVRecord record1(int i) {
        return record("v" + i, "value" + i);
    }

    static CSVRecord record1(int i, String value) {
        return record("v" + i, value);
    }

    private void addValue(String value) {
        this.values.add(value);
    }

    private CSVRecord build(int recordNumber) {
        String[] values = this.values.toArray(new String[this.values.size()]);
        return new CSVRecord(values, Collections.emptyMap(), null, recordNumber);
    }
}

class CSVRecordBuilder {
    private final List<String> values;
    private Map<String, Integer> headerMap;
    private String comment;
    private int recordNumber;

    private CSVRecordBuilder() {
        values = new ArrayList<>();
    }

    CSVRecordBuilder addValue(String value) {
        values.add(value);
        return this;
    }

    CSVRecordBuilder setHeaderMap(Map<String, Integer> headerMap) {
        this.headerMap = headerMap;
        return this;
    }

    CSVRecordBuilder setComment(String comment) {
        this.comment = comment;
        return this;
    }

    CSVRecordBuilder setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
        return this;
    }

    CSVRecord build(int recordNumber) {
        return new CSVRecord(values.toArray(new String[values.size()]), headerMap, comment, recordNumber);
    }
}