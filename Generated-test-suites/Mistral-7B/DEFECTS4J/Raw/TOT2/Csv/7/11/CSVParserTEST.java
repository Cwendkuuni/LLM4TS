package org.apache.commons.csv.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordIterator;
import org.apache.commons.csv.Token;
import org.apache.commons.csv.test.util.TestUtil;

/**
 * Test class for {@link CSVParser}.
 */
public class CSVParserTest {

    /**
     * Test parsing a simple CSV file with no header.
     */
    @org.junit.Test
    public void testParseSimpleCSV() throws Exception {
        String csv = "a,b\n1,2\n3,4";
        CSVParser parser = CSVParser.parse(new StringReader(csv), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"a", "b"}, record1.getColumnNames());
        assertEquals("1", record1.get(0));
        assertEquals("2", record1.get(1));
        assertArrayEquals(new String[]{"a", "b"}, record2.getColumnNames());
        assertEquals("3", record2.get(0));
        assertEquals("4", record2.get(1));
        parser.close();
    }

    /**
     * Test parsing a CSV file with header.
     */
    @org.junit.Test
    public void testParseCSVWithHeader() throws Exception {
        String csv = "Name,Age\nJohn Doe,30\nJane Doe,25";
        CSVParser parser = CSVParser.parse(new StringReader(csv), CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Name", "Age"}, record1.getColumnNames());
        assertEquals("John Doe", record1.get("Name"));
        assertEquals("30", record1.get("Age"));
        assertArrayEquals(new String[]{"Name", "Age"}, record2.getColumnNames());
        assertEquals("Jane Doe", record2.get("Name"));
        assertEquals("25", record2.get("Age"));
        parser.close();
    }

    /**
     * Test parsing a CSV file with empty values.
     */
    @org.junit.Test
    public void testParseCSVWithEmptyValues() throws Exception {
        String csv = "Name,Age\nJohn Doe,,30\nJane Doe, ,25";
        CSVParser parser = CSVParser.parse(new StringReader(csv), CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Name", "Age"}, record1.getColumnNames());
        assertNull(record1.get("Name"));
        assertEquals("30", record1.get("Age"));
        assertArrayEquals(new String[]{"Name", "Age"}, record2.getColumnNames());
        assertEquals("Jane Doe", record2.get("Name"));
        assertNull(record2.get("Age"));
        parser.close();
    }

    /**
     * Test parsing a CSV file with a comment line.
     */
    @org.junit.Test
    public void testParseCSVWithComment() throws Exception {
        String csv = "Name,Age\nJohn Doe,30\n# This is a comment\nJane Doe,25";
        CSVParser parser = CSVParser.parse(new StringReader(csv), CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Name", "Age"}, record1.getColumnNames());
        assertEquals("John Doe", record1.get("Name"));
        assertEquals("30", record1.get("Age"));
        assertArrayEquals(new String[]{"Name", "Age"}, record2.getColumnNames());
        assertEquals("Jane Doe", record2.get("Name"));
        assertEquals("25", record2.get("Age"));
        assertEquals("", record2.getComment());
        parser.close();
    }

    /**
     * Test parsing a CSV file with malformed data.
     */
    @org.junit.Test
    public void testParseCSVWithMalformedData() throws Exception {
        String csv = "Name,Age\nJohn Doe,30\nJane Doe,\n25";
        CSVParser parser = CSVParser.parse(new StringReader(csv), CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Name", "Age"}, record1.getColumnNames());
        assertEquals("John Doe", record1.get("Name"));
        assertEquals("30", record1.get("Age"));
        assertArrayEquals(new String[]{"Name", "Age"}, record2.getColumnNames());
        assertEquals("Jane Doe", record2.get("Name"));
        assertNull(record2.get("Age"));
        parser.close();
    }

    /**
     * Test parsing a CSV file with custom format.
     */
    @org.junit.Test
    public void testParseCSVWithCustomFormat() throws Exception {
        String csv = "Name,Age,BirthDate\nJohn Doe,30,1980-01-01\nJane Doe,25,1995-12-12";
        CSVFormat format = CSVFormat.DEFAULT.withHeader().withIgnoreEmptyLines(true)
                .withColumnFormat(new CustomDateFormat());
        CSVParser parser = CSVParser.parse(new StringReader(csv), format);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Name", "Age", "BirthDate"}, record1.getColumnNames());
        assertEquals("John Doe", record1.get("Name"));
        assertEquals("30", record1.get("Age"));
        assertEquals("1980-01-01", record1.get("BirthDate"));
        assertArrayEquals(new String[]{"Name", "Age", "BirthDate"}, record2.getColumnNames());
        assertEquals("Jane Doe", record2.get("Name"));
        assertEquals("25", record2.get("Age"));
        assertEquals("1995-12-12", record2.get("BirthDate"));
        parser.close();
    }

    /**
     * Test iterator over CSV records.
     */
    @org.junit.Test
    public void testIteratorOverCSVRecords() throws Exception {
        String csv = "Name,Age,BirthDate\nJohn Doe,30,1980-01-01\nJane Doe,25,1995-12-12";
        CSVFormat format = CSVFormat.DEFAULT.withHeader().withIgnoreEmptyLines(true)
                .withColumnFormat(new CustomDateFormat());
        CSVParser parser = CSVParser.parse(new StringReader(csv), format);
        CSVRecordIterator iterator = parser.iterator();
        List<CSVRecord> records = new ArrayList<>();
        while (iterator.hasNext()) {
            records.add(iterator.next());
        }
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Name", "Age", "BirthDate"}, record1.getColumnNames());
        assertEquals("John Doe", record1.get("Name"));
        assertEquals("30", record1.get("Age"));
        assertEquals("1980-01-01", record1.get("BirthDate"));
        assertArrayEquals(new String[]{"Name", "Age", "BirthDate"}, record2.getColumnNames());
        assertEquals("Jane Doe", record2.get("Name"));
        assertEquals("25", record2.get("Age"));
        assertEquals("1995-12-12", record2.get("BirthDate"));
        parser.close();
    }

    /**
     * Test parsing a large CSV file.
     */
    @org.junit.Test
    public void testParseLargeCSVFile() throws Exception {
        String csv = TestUtil.generateLargeCSVData(10000, 100);
        CSVParser parser = CSVParser.parse(new StringReader(csv), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(10000, records.size());
        parser.close();
    }

    /**
     * Test closing a parser that already has been closed.
     */
    @org.junit.Test
    public void testCloseParserTwice() throws Exception {
        String csv = "Name,Age\nJohn Doe,30\nJane Doe,25";
        CSVParser parser = CSVParser.parse(new StringReader(csv), CSVFormat.DEFAULT);
        parser.close();
        parser.close();
    }

    /**
     * Test getting records from an empty parser.
     */
    @org.junit.Test(expected = NoSuchElementException.class)
    public void testGetRecordsFromEmptyParser() throws Exception {
        CSVParser parser = CSVParser.parse(new StringReader(""), CSVFormat.DEFAULT);
        parser.getRecords();
    }

    /**
     * Test getting records from a closed parser.
     */
    @org.junit.Test(expected = NoSuchElementException.class)
    public void testGetRecordsFromClosedParser() throws Exception {
        String csv = "Name,Age\nJohn Doe,30\nJane Doe,25";
        CSVParser parser = CSVParser.parse(new StringReader(csv), CSVFormat.DEFAULT);
        parser.getRecords();
        parser.close();
        parser.getRecords();
    }

    // Custom date format for testing
    static class CustomDateFormat implements CSVParser.DateFormat {
        @Override
        public int getLength() {
            return 10;
        }

        @Override
        public int getLength(String string, int index, CSVParser parser) {
            return 10;
        }

        @Override
        public int parse(String string, int index, CSVParser parser) {
            return parseDate(string, index);
        }

        @Override
        public String format(long date) {
            return formatDate(date);
        }

        private int parseDate(String string, int index) {
            int year = Integer.parseInt(string.substring(index, index + 4));
            int month = Integer.parseInt(string.substring(index + 5, index + 7)) - 1;
            int day = Integer.parseInt(string.substring(index + 8, index + 10));
            return year * 10000 + month * 100 + day;
        }

        private String formatDate(long date) {
            return String.format("%04d-%02d-%02d", new Object[]{Integer.valueOf((int) (date / 10000)),
                    Integer.valueOf((int) ((date % 10000) / 100)), Integer.valueOf((int) (date % 100))});
        }
    }

}