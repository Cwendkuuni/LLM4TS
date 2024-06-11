package org.apache.commons.csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;

/**
 * Test class for {@link CSVParser}.
 */
public class CSVParserTest {

    /**
     * Test parsing records using default format.
     */
    @org.junit.Test
    public void testParseDefaultFormat() throws IOException {
        final String csv = "a,b,c\n1,2,3\n4,5,6";
        final CSVParser parser = CSVParser.parse(new StringReader(csv), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        assertRecordEquals(new String[]{"1", "2", "3"}, records.get(0));
        assertRecordEquals(new String[]{"4", "5", "6"}, records.get(1));
    }

    /**
     * Test parsing records with explicit format.
     */
    @org.junit.Test
    public void testParseExplicitFormat() throws IOException {
        final String csv = "Name,Age,Sex\nJohn Doe,30,M\nJane Doe,25,F";
        final CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withHeader());
        final CSVParser parser = CSVParser.parse(new StringReader(csv), format);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        assertRecordEquals(new String[]{"Name", "Age", "Sex"}, records.get(0).getHeaderMap().keySet().toArray(new String[0]));
        assertRecordEquals(new String[]{"John Doe", "30", "M"}, records.get(0));
        assertRecordEquals(new String[]{"Jane Doe", "25", "F"}, records.get(1));
    }

    /**
     * Test parsing records with skip header.
     */
    @org.junit.Test
    public void testParseSkipHeader() throws IOException {
        final String csv = "Name,Age,Sex\nJohn Doe,30,M\nJane Doe,25,F";
        final CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withoutHeader());
        final CSVParser parser = CSVParser.parse(new StringReader(csv), format);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        assertRecordEquals(new String[]{"John Doe", "30"}, records.get(0));
        assertRecordEquals(new String[]{"Jane Doe", "25"}, records.get(1));
    }

    /**
     * Test parsing records with null values.
     */
    @org.junit.Test
    public void testParseNullValues() throws IOException {
        final String csv = "Name,Age,Null\nJohn Doe,30,Null\nJane Doe,25,";
        final CSVParser parser = CSVParser.parse(new StringReader(csv), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        assertRecordEquals(new String[]{"John Doe", "30", null}, records.get(0));
        assertRecordEquals(new String[]{"Jane Doe", null, null}, records.get(1));
    }

    /**
     * Test parsing records with empty values.
     */
    @org.junit.Test
    public void testParseEmptyValues() throws IOException {
        final String csv = "Name,Age,Empty\nJohn Doe,30,,\nJane Doe,25,";
        final CSVParser parser = CSVParser.parse(new StringReader(csv), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        assertRecordEquals(new String[]{"John Doe", "30", ""}, records.get(0));
        assertRecordEquals(new String[]{"Jane Doe", "", ""}, records.get(1));
    }

    /**
     * Test parsing records with comments.
     */
    @org.junit.Test
    public void testParseRecordsWithComments() throws IOException {
        final String csv = "Name,Age,Comment\nJohn Doe,30,#This is a comment\nJane Doe,25,";
        final CSVParser parser = CSVParser.parse(new StringReader(csv), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        assertRecordEquals(new String[]{"John Doe", "30"}, records.get(0));
        assertRecordEquals(new String[]{"Jane Doe", ""}, records.get(1));
        assertTrue(records.get(0).getComment().contains("This is a comment"));
        assertNull(records.get(1).getComment());
    }

    /**
     * Test parsing records with header and null values.
     */
    @org.junit.Test
    public void testParseRecordsWithHeaderAndNullValues() throws IOException {
        final String csv = "Name,Age,Null\nName,Age,Null\nJohn Doe,30,Null\nJane Doe,25,";
        final CSVParser parser = CSVParser.parse(new StringReader(csv), CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        assertRecordEquals(new String[]{"Name", "Age", "Null"}, records.get(0).getHeaderMap().keySet().toArray(new String[0]));
        assertRecordEquals(new String[]{"Name", "Age", null}, records.get(1));
        assertRecordEquals(new String[]{"Name", "Age", null}, records.get(2));
    }

    /**
     * Test parsing records with empty values and header.
     */
    @org.junit.Test
    public void testParseRecordsWithEmptyValuesAndHeader() throws IOException {
        final String csv = "Name,Age,Empty\nName,Age,Empty\nJohn Doe,30,,\nJane Doe,25,";
        final CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withHeader());
        final CSVParser parser = CSVParser.parse(new StringReader(csv), format);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        assertRecordEquals(new String[]{"Name", "Age", "Empty"}, records.get(0).getHeaderMap().keySet().toArray(new String[0]));
        assertRecordEquals(new String[]{"Name", "Age", ""}, records.get(1));
        assertRecordEquals(new String[]{"Name", "Age", ""}, records.get(2));
    }

    /**
     * Test parsing empty CSV file.
     */
    @org.junit.Test
    public void testParseEmptyFile() throws IOException {
        final String csv = "";
        final CSVParser parser = CSVParser.parse(new StringReader(csv), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertTrue(records.isEmpty());
    }

    /**
     * Test parsing CSV file with invalid format.
     */
    @org.junit.Test(expected = IOException.class)
    public void testParseInvalidFormat() throws IOException {
        final String csv = "a,b,c\n1,2,3\n4,5,6\n";
        final CSVParser parser = CSVParser.parse(new StringReader(csv), new CSVFormat("InvalidFormat"));
        parser.getRecords();
    }

    /**
     * Test closing parser.
     */
    @org.junit.Test
    public void testCloseParser() throws IOException {
        final String csv = "a,b,c\n1,2,3\n4,5,6";
        final CSVParser parser = CSVParser.parse(new StringReader(csv), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        try {
            parser.getRecords();
            fail("Expected IOException");
        } catch (final IOException e) {
            // Expected
        }
        try {
            records.get(0).getHeaderMap().clear();
            fail("Expected UnsupportedOperationException");
        } catch (final UnsupportedOperationException e) {
            // Expected
        }
    }

    /**
     * Test iterating records.
     */
    @org.junit.Test
    public void testIterateRecords() throws IOException {
        final String csv = "a,b,c\n1,2,3\n4,5,6";
        final CSVParser parser = CSVParser.parse(new StringReader(csv), CSVFormat.DEFAULT);
        Iterator<CSVRecord> iterator = parser.iterator();
        List<CSVRecord> records = new ArrayList<>(Collections.nCopies(2, null));
        int index = 0;
        while (iterator.hasNext()) {
            records.set(index++, iterator.next());
        }
        assertEquals(2, records.size());
        assertRecordEquals(new String[]{"1", "2", "3"}, records.get(0));
        assertRecordEquals(new String[]{"4", "5", "6"}, records.get(1));
    }

    /**
     * Test reading all records into memory.
     */
    @org.junit.Test
    public void testReadAllRecordsIntoMemory() throws IOException {
        final String csv = "a,b,c\n1,2,3\n4,5,6";
        final CSVParser parser = CSVParser.parse(new StringReader(csv), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        List<CSVRecord> allRecords = parser.getRecords(Collections.synchronizedList(new ArrayList<>()));
        assertEquals(2, allRecords.size());
        assertRecordEquals(new String[]{"1", "2", "3"}, records.get(0));
        assertRecordEquals(new String[]{"1", "2", "3"}, allRecords.get(0));
        assertRecordEquals(new String[]{"4", "5", "6"}, records.get(1));
        assertRecordEquals(new String[]{"4", "5", "6"}, allRecords.get(1));
    }

    /**
     * Test parsing large CSV file.
     */
    @org.junit.Test
    public void testParseLargeFile() throws IOException {
        final int recordCount = 10000;
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < recordCount; i++) {
            sb.append("column").append((i + 1)).append(",");
            if (i == recordCount - 1) {
                sb.deleteCharAt(sb.length() - 1);
                sb.append("\n");
            } else {
                sb.append("\n");
            }
        }
        final String csvData = sb.toString();
        final CSVFormat format = CSVFormat.DEFAULT;
        final File file = File.createTempFile("CSVParserTest", ".csv");
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.print(csvData);
        }
        final CSVParser parser = CSVParser.parse(file, format);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(recordCount, records.size());
        parser.close();
        file.delete();
    }

    /**
     * Utility method for asserting record equality.
     *
     * @param expected the expected record values
     * @param record the record to compare
     */
    private void assertRecordEquals(final String[] expected, final CSVRecord record) {
        assertArrayEquals(expected, record.values());
        assertArrayEquals(expected.length, record.getHeaderMap().size(), "Invalid header map size");
        for (String key : record.getHeaderMap().keySet()) {
            assertEquals(key, record.getHeaderMap().get(key));
        }
    }
}