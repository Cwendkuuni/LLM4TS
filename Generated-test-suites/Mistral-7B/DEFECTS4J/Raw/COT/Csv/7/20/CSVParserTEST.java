package org.apache.commons.csv.test;

import static org.apache.commons.csv.CSVParser.*;
import static org.apache.commons.csv.CSVRecord.*;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.csv.*;
import org.junit.Test;

/**
 * Test cases for CSVParser class.
 */
public class CSVParserTest {

    /**
     * Test parse method with valid file path and format.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testParseFile() throws IOException {
        File file = new File("src/test/resources/validCSVFile.csv");
        CSVParser parser = parse(file, CSVFormat.DEFAULT);

        List<CSVRecord> records = new ArrayList<>();
        records.addAll(parser.getRecords());
        parser.close();

        assertEquals(5, records.size());
        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertEquals(i + 1, record.getRecordNumber());
            assertArrayEquals(new String[]{"A1", "B1", "C1"}, record.getValues());
        }
    }

    /**
     * Test parse method with valid string and format.
     */
    @Test
    public void testParseString() {
        String csvContent = "A1,B1,C1\n" +
                           "A2,B2,C2\n" +
                           "A3,B3,C3";
        CSVParser parser = parse(new StringReader(csvContent), CSVFormat.DEFAULT);

        List<CSVRecord> records = new ArrayList<>();
        records.addAll(parser.getRecords());
        parser.close();

        assertEquals(3, records.size());
        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertEquals(i + 1, record.getRecordNumber());
            assertArrayEquals(new String[]{"A" + (i + 1), "B" + (i + 1), "C" + (i + 1)}, record.getValues());
        }
    }

    /**
     * Test parse method with valid URL and format.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testParseURL() throws IOException {
        URL url = getClass().getResource("/validCSVFile.csv");
        CSVParser parser = parse(url, Charset.forName("UTF-8"), CSVFormat.DEFAULT);

        List<CSVRecord> records = new ArrayList<>();
        records.addAll(parser.getRecords());
        parser.close();

        assertEquals(5, records.size());
        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertEquals(i + 1, record.getRecordNumber());
            assertArrayEquals(new String[]{"A1", "B1", "C1"}, record.getValues());
        }
    }

    /**
     * Test parse method with null file.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFile() {
        File file = null;
        CSVParser parser = parse(file, CSVFormat.DEFAULT);
    }

    /**
     * Test parse method with null CSVFormat.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFormat() {
        File file = new File("src/test/resources/validCSVFile.csv");
        CSVParser parser = parse(file, null);
    }

    /**
     * Test parse method with empty CSV content.
     */
    @Test
    public void testParseEmptyCSV() {
        StringReader reader = new StringReader("");
        CSVParser parser = parse(reader, CSVFormat.DEFAULT);
        parser.getRecords();
        parser.close();

        assertTrue(parser.isClosed());
    }

    /**
     * Test parse method with invalid CSV content.
     */
    @Test(expected = IOException.class)
    public void testParseInvalidCSV() throws IOException {
        StringReader reader = new StringReader("InvalidCSVContent");
        CSVParser parser = parse(reader, CSVFormat.DEFAULT);
        parser.getRecords();
        parser.close();
    }

    /**
     * Test parse method with empty CSV file.
     */
    @Test
    public void testParseEmptyCSVFile() throws IOException {
        File file = new File("src/test/resources/emptyCSVFile.csv");
        CSVParser parser = parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertTrue(parser.isClosed());
        assertEquals(0, records.size());
    }

    /**
     * Test parse method with CSV file with only comments.
     */
    @Test
    public void testParseCSVFileWithComments() throws IOException {
        File file = new File("src/test/resources/commentsCSVFile.csv");
        CSVParser parser = parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertTrue(parser.isClosed());
        assertEquals(0, records.size());
        assertTrue(parser.getHeaderMap() == null);
    }

    /**
     * Test parse method with CSV file with invalid header record.
     */
    @Test(expected = IOException.class)
    public void testParseInvalidHeaderRecordCSVFile() throws IOException {
        File file = new File("src/test/resources/invalidHeaderRecordCSVFile.csv");
        CSVParser parser = parse(file, CSVFormat.DEFAULT);
    }

    /**
     * Test parse method with CSV file with header record and data record with different number of columns.
     */
    @Test(expected = IOException.class)
    public void testParseCSVFileWithMismatchedColumns() throws IOException {
        File file = new File("src/test/resources/mismatchedColumnsCSVFile.csv");
        CSVParser parser = parse(file, CSVFormat.DEFAULT);
    }

    /**
     * Test parse method with CSV file with empty record.
     */
    @Test
    public void testParseCSVFileWithEmptyRecord() throws IOException {
        File file = new File("src/test/resources/emptyRecordCSVFile.csv");
        CSVParser parser = parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertTrue(parser.isClosed());
        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        assertArrayEquals(new String[]{}, record.getValues());
    }

    /**
     * Test parse method with CSV file with record without values.
     */
    @Test
    public void testParseCSVFileWithRecordWithoutValues() throws IOException {
        File file = new File("src/test/resources/recordWithoutValuesCSVFile.csv");
        CSVParser parser = parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertTrue(parser.isClosed());
        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        assertNotNull(record.getValues());
        assertEquals(0, record.getValues().length);
    }

    /**
     * Test parse method with CSV file with record with only comments.
     */
    @Test
    public void testParseCSVFileWithRecordOnlyComments() throws IOException {
        File file = new File("src/test/resources/recordOnlyCommentsCSVFile.csv");
        CSVParser parser = parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertTrue(parser.isClosed());
        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        assertEquals("", record.getValues()[0]);
    }

    /**
     * Test parse method with CSV file with record with empty string value.
     */
    @Test
    public void testParseCSVFileWithRecordEmptyStringValue() throws IOException {
        File file = new File("src/test/resources/recordEmptyStringValueCSVFile.csv");
        CSVParser parser = parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertTrue(parser.isClosed());
        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        String[] values = record.getValues();
        assertEquals("", values[0]);
        assertEquals("", values[1]);
        assertEquals("", values[2]);
    }

    /**
     * Test parse method with CSV file with record with null value.
     */
    @Test
    public void testParseCSVFileWithRecordNullValue() throws IOException {
        File file = new File("src/test/resources/recordNullValueCSVFile.csv");
        CSVParser parser = parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertTrue(parser.isClosed());
        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        String[] values = record.getValues();
        assertEquals(null, values[0]);
        assertEquals("", values[1]);
        assertEquals("", values[2]);
    }

    /**
     * Test parse method with CSV file with record with escaped values.
     */
    @Test
    public void testParseCSVFileWithRecordEscapedValues() throws IOException {
        File file = new File("src/test/resources/recordEscapedValuesCSVFile.csv");
        CSVParser parser = parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertTrue(parser.isClosed());
        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        String[] values = record.getValues();
        assertEquals("value1,\"value2 with double quotes\",value3", Arrays.toString(values));
    }

    /**
     * Test parse method with CSV file with record with empty line.
     */
    @Test
    public void testParseCSVFileWithRecordEmptyLine() throws IOException {
        File file = new File("src/test/resources/recordEmptyLineCSVFile.csv");
        CSVParser parser = parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertTrue(parser.isClosed());
        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        assertArrayEquals(new String[]{}, record.getValues());
    }
}