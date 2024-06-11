package org.apache.commons.csv;

import static org.apache.commons.csv.CSVParser.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link CSVParser}.
 */
public class CSVParserTest {

    private CSVParser parser;
    private CSVFormat format;

    @Before
    public void setUp() throws IOException {
        format = new CSVFormatBuilder()
                .setHeaderNames(Arrays.asList("header1", "header2"))
                .setNullString("NA")
                .build();
        parser = parse(new ByteArrayInputStream(getTestData().getBytes(StandardCharsets.UTF_8)), format);
    }

    @Test
    public void testParse() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord firstRecord = records.get(0);
        assertEquals(2, firstRecord.size());
        assertEquals("value1", firstRecord.get("header1"));
        assertEquals("value2", firstRecord.get("header2"));

        CSVRecord secondRecord = records.get(1);
        assertEquals(2, secondRecord.size());
        assertEquals("value3", secondRecord.get("header1"));
        assertEquals("value4", secondRecord.get("header2"));
    }

    @Test
    public void testIterator() throws IOException {
        List<CSVRecord> records = parser.getRecords();

        Iterator<CSVRecord> iterator = records.iterator();
        CSVRecord firstRecord = iterator.next();
        assertTrue(iterator.hasNext());
        CSVRecord secondRecord = iterator.next();
        assertFalse(iterator.hasNext());

        assertEquals(firstRecord, firstRecord);
        assertSame(firstRecord, firstRecord);
        assertSame(firstRecord, firstRecord);
        assertSame(firstRecord.hashCode(), firstRecord.hashCode());

        assertEquals(secondRecord, secondRecord);
        assertSame(secondRecord, secondRecord);
        assertSame(secondRecord, secondRecord);
        assertSame(secondRecord.hashCode(), secondRecord.hashCode());
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        Map<String, Integer> headerMap = parser.getHeaderMap();

        assertEquals(2, headerMap.size());
        assertEquals("header1", headerMap.getKey(0));
        assertEquals(0, headerMap.getValue(0).intValue());
        assertEquals("header2", headerMap.getKey(1));
        assertEquals(1, headerMap.getValue(1).intValue());

        assertEquals("header1", records.get(0).getHeaderMap().getKey(0));
        assertEquals("header2", records.get(0).getHeaderMap().getKey(1));
        assertEquals("header1", records.get(1).getHeaderMap().getKey(0));
        assertEquals("header2", records.get(1).getHeaderMap().getKey(1));
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        List<CSVRecord> records = parser.getRecords();

        assertEquals(0, parser.getRecordNumber());
        parser.nextRecord();
        assertEquals(1, parser.getRecordNumber());
        parser.nextRecord();
        assertEquals(2, parser.getRecordNumber());
        parser.nextRecord();
        assertNull(parser.nextRecord());
    }

    @Test
    public void testGetCurrentLineNumber() throws IOException {
        assertEquals(0, parser.getCurrentLineNumber());

        parser.nextRecord();
        parser.nextRecord();
        parser.nextRecord();
        parser.nextRecord();

        assertEquals(5, parser.getCurrentLineNumber());
    }

    @Test
    public void testClose() throws IOException {
        List<CSVRecord> records = parser.getRecords();

        parser.close();
        assertFalse(parser.isOpen());
        assertNull(parser.nextRecord());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoMoreRecords() throws IOException {
        parser.close();
        parser.nextRecord();
    }

    @Test
    public void testParseEmptyFile() throws IOException {
        parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(new byte[0])), format);
        parser.close();
        List<CSVRecord> records = parser.getRecords();
        assertEquals(0, records.size());
    }

    @Test
    public void testParseEmptyString() throws IOException {
        parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(new byte[0])), format);
        parser.close();
        List<CSVRecord> records = parser.getRecords();
        assertEquals(0, records.size());

        parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(new byte[0])), format);
        parser.close();
        records = parser.getRecords();
        assertEquals(0, records.size());

        parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(new byte[0])), format);
        parser.close();
        records = parser.getRecords();
        assertEquals(0, records.size());
    }

    @Test
    public void testParseNullFormat() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> {
            parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(getTestData().getBytes(StandardCharsets.UTF_8))), null);
            parser.getRecords();
        });
    }

    @Test
    public void testParseNullFile() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> {
            parser = new CSVParser(null, format);
            parser.getRecords();
        });
    }

    @Test
    public void testParseNullData() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> {
            parser = new CSVParser(new InputStreamReader(null), format);
            parser.getRecords();
        });

        assertThrows(IllegalArgumentException.class, () -> {
            parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(new byte[0])), null);
            parser.getRecords();
        });
    }

    @Test
    public void testParseEmptyFormat() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> {
            parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(getTestData().getBytes(StandardCharsets.UTF_8))), new CSVFormat());
            parser.getRecords();
        });
    }

    @Test
    public void testParseEmptyDataWithFormat() throws IOException {
        assertThrows(IOException.class, () -> {
            parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(new byte[0])), format);
            parser.getRecords();
        });
    }

    @Test
    public void testParseInvalidDataWithFormat() throws IOException {
        parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream("invalid data".getBytes(StandardCharsets.UTF_8))), format);
        assertThrows(IOException.class, () -> {
            parser.getRecords();
        });
    }

    private byte[] getTestData() {
        return new byte[] {
            (byte) 'h', (byte) 'e', (byte) 'a', (byte) 'd', (byte) 'e', (byte) 'r', (byte) '1', (byte) ',',
            (byte) 'v', (byte) 'a', (byte) 'l', (byte) 'u', (byte) 'e', (byte) '1', (byte) ',',
            (byte) 'v', (byte) 'a', (byte) 'l', (byte) 'u', (byte) 'e', (byte) '2', (byte) '\r', (byte) '\n',
            (byte) 'h', (byte) 'e', (byte) 'a', (byte) 'd', (byte) 'e', (byte) 'r', (byte) '2', (byte) ',',
            (byte) 'v', (byte) 'a', (byte) 'l', (byte) 'u', (byte) 'e', (byte) '3', (byte) '\r', (byte) '\n',
            (byte) 'h', (byte) 'e', (byte) 'a', (byte) 'd', (byte) 'e', (byte) 'r', (byte) '3', (byte) ',',
            (byte) 'v', (byte) 'a', (byte) 'l', (byte) 'u', (byte) 'e', (byte) '4'
        };
    }
}