package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        String input = "a,b\n1,2\n3,4";
        CSVParser parser = CSVParser.parse(new ByteArrayInputStream(input.getBytes()), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        assertArrayEquals(new String[]{"a", "b"}, record1.getNames());
        assertArrayEquals(new String[]{"1", "2"}, record1.getValues());
        assertArrayEquals(new String[]{"1", "2"}, record2.getValues());
        assertEquals(2, records.size());
    }

    @Test
    public void testParseString() throws IOException {
        String input = "a,b\n1,2\n3,4";
        CSVParser parser = CSVParser.parse(new StringReader(input), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        assertArrayEquals(new String[]{"a", "b"}, record1.getNames());
        assertArrayEquals(new String[]{"1", "2"}, record1.getValues());
        assertArrayEquals(new String[]{"1", "2"}, record2.getValues());
        assertEquals(2, records.size());
    }

    @Test
    public void testParseUrl() throws IOException {
        String input = "a,b\n1,2\n3,4";
        URL url = new URL("http://localhost/test.csv");
        CSVParser parser = CSVParser.parse(new InputStreamReader(url.openStream()), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        assertArrayEquals(new String[]{"a", "b"}, record1.getNames());
        assertArrayEquals(new String[]{"1", "2"}, record1.getValues());
        assertArrayEquals(new String[]{"1", "2"}, record2.getValues());
        assertEquals(2, records.size());
    }

    @Test
    public void testParseWithCustomFormat() throws IOException {
        String input = "a,b\n1,2\n3,4";
        CSVFormat format = new CSVFormat(new String[]{"ColumnA", "ColumnB"}, '\t');
        CSVParser parser = CSVParser.parse(new StringReader(input), format);
        List<CSVRecord> records = parser.getRecords();

        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        assertArrayEquals(new String[]{"ColumnA", "ColumnB"}, record1.getNames());
        assertArrayEquals(new String[]{"1", "2"}, record1.getValues());
        assertArrayEquals(new String[]{"1", "2"}, record2.getValues());
        assertEquals(2, records.size());
    }

    @Test
    public void testEmptyFile() throws IOException {
        String input = "";
        CSVParser parser = CSVParser.parse(new FileReader(new File("empty.csv")), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertTrue(records.isEmpty());
        parser.close();
    }

    @Test
    public void testEmptyString() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader(""), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertTrue(records.isEmpty());
        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullReader() throws IOException {
        CSVParser parser = new CSVParser(null, CSVFormat.DEFAULT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFormat() throws IOException {
        CSVParser parser = new CSVParser(new StringReader(""), null);
    }

    @Test
    public void testCloseParser() throws IOException {
        String input = "a,b\n1,2\n3,4";
        CSVParser parser = CSVParser.parse(new StringReader(input), CSVFormat.DEFAULT);
        parser.close();

        CSVRecord record1 = parser.nextRecord();
        assertNull(record1);
    }

    @Test(expected = IOException.class)
    public void testNextRecordAfterClose() throws IOException {
        String input = "a,b\n1,2\n3,4";
        CSVParser parser = CSVParser.parse(new StringReader(input), CSVFormat.DEFAULT);
        parser.close();
        parser.nextRecord();
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        String input = "a,b\n1,2\n3,4";
        CSVParser parser = CSVParser.parse(new StringReader(input), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, parser.getRecordNumber());
    }

    @Test
    public void testEmptyRecord() throws IOException {
        String input = ",\n";
        CSVParser parser = CSVParser.parse(new StringReader(input), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        CSVRecord record = records.get(0);
        assertArrayEquals(new String[0], record.getValues());
    }

    @Test
    public void testRecordWithNullValue() throws IOException {
        String input = "a,b,,\n1,2,3";
        CSVParser parser = CSVParser.parse(new StringReader(input), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        CSVRecord record = records.get(0);
        assertArrayEquals(new String[]{"a", "b", null}, record.getValues());
    }

    @Test
    public void testRecordWithComment() throws IOException {
        String input = "a,b\n1,2# this is a comment\n3,4";
        CSVParser parser = CSVParser.parse(new StringReader(input), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        CSVRecord record = records.get(1);
        assertEquals("this is a comment", record.getComment());
    }

    @Test
    public void testRecordWithEscapeCharacter() throws IOException {
        String input = "a,\"b,c\"\n1,2,3";
        CSVParser parser = CSVParser.parse(new StringReader(input), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        CSVRecord record = records.get(0);
        String[] values = record.getValues();

        assertEquals(2, values.length);
        assertEquals("b,c", values[0]);
    }

    @Test
    public void testCustomHeaderMap() throws IOException {
        String input = "Name,Age\nAlice,25\nBob,30";
        Map<String, Integer> headerMap = Collections.singletonMap("Name", 0);

        CSVParser parser = new CSVParser(new StringReader(input), CSVFormat.DEFAULT) {
            @Override
            Map<String, Integer> initializeHeader() throws IOException {
                return headerMap;
            }
        };

        List<CSVRecord> records = parser.getRecords();
        CSVRecord record = records.get(0);

        assertEquals("Name", record.getHeaderName(0));
        assertEquals("Alice", record.get(0));
    }
}