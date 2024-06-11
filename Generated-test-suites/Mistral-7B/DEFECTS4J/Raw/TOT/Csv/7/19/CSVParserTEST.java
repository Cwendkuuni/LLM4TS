package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Test cases for {@link CSVParser} class.
 */
public class CSVParserTest {

    /**
     * Test parsing a simple CSV file using default format.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testSimpleCSV() throws IOException {
        final String csvContent = "a,1\nb,2\nc,3";

        final CSVParser parser = new CSVParser(new StringReader(csvContent), CSVFormat.getDefault());
        final List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        final CSVRecord firstRecord = records.get(0);
        final CSVRecord secondRecord = records.get(1);

        assertEquals(2, firstRecord.size());
        assertEquals("a", firstRecord.get(0));
        assertEquals("1", firstRecord.get(1));

        assertEquals(2, secondRecord.size());
        assertEquals("b", secondRecord.get(0));
        assertEquals("2", secondRecord.get(1));
    }

    /**
     * Test parsing a CSV file with custom format.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testCustomCSVFormat() throws IOException {
        final String csvContent = "Name,Age\nJohn Doe,30\nJane Doe,25";

        final CSVFormat customFormat = new CSVFormatBuilder().setHeaderNames(Arrays.asList("Name", "Age"))
                .setDelimiter(';').build();

        final CSVParser parser = new CSVParser(new StringReader(csvContent), customFormat);
        final List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        final CSVRecord firstRecord = records.get(0);
        final CSVRecord secondRecord = records.get(1);

        assertEquals(2, firstRecord.size());
        assertEquals("Name", firstRecord.getName(0));
        assertEquals("John Doe", firstRecord.get(0));
        assertEquals("Age", firstRecord.get(1));
        assertEquals("30", firstRecord.get(1));

        assertEquals(2, secondRecord.size());
        assertEquals("Name", secondRecord.getName(0));
        assertEquals("Jane Doe", secondRecord.get(0));
        assertEquals("Age", secondRecord.get(1));
        assertEquals("25", secondRecord.get(1));
    }

    /**
     * Test parsing a CSV file with empty line.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testEmptyLine() throws IOException {
        final String csvContent = "a,1\nb\nc,3";

        final CSVParser parser = new CSVParser(new StringReader(csvContent), CSVFormat.getDefault());
        final List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        final CSVRecord firstRecord = records.get(0);
        final CSVRecord secondRecord = records.get(1);

        assertEquals(2, firstRecord.size());
        assertEquals("a", firstRecord.get(0));
        assertEquals("1", firstRecord.get(1));

        assertEquals(0, secondRecord.size());
    }

    /**
     * Test parsing a CSV file with quoted values.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testQuotedValues() throws IOException {
        final String csvContent = "Name,\"John Doe, Jr.\",Age,30";

        final CSVParser parser = new CSVParser(new StringReader(csvContent), CSVFormat.getDefault());
        final List<CSVRecord> records = parser.getRecords();

        assertEquals(1, records.size());
        final CSVRecord record = records.get(0);

        assertEquals(4, record.size());
        assertEquals("Name", record.get(0));
        assertEquals("John Doe, Jr.", record.get(1));
        assertEquals("Age", record.get(2));
        assertEquals("30", record.get(3));
    }

    /**
     * Test parsing a CSV file with empty values.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testEmptyValues() throws IOException {
        final String csvContent = "Name,," + Constants.LF + "Age,30";

        final CSVParser parser = new CSVParser(new StringReader(csvContent), CSVFormat.getDefault());
        final List<CSVRecord> records = parser.getRecords();

        assertEquals(1, records.size());
        final CSVRecord record = records.get(0);

        assertEquals(2, record.size());
        assertEquals("Name", record.get(0));
        assertEquals(null, record.get(1));

        assertEquals(1, record.getRecordNumber());
    }

    /**
     * Test parsing a CSV file with commented values.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testCommentedValues() throws IOException {
        final String csvContent = "Name,Age\n" + "John Doe,30\n" + "! This is a comment line\n" + "Jane Doe,25";

        final CSVParser parser = new CSVParser(new StringReader(csvContent), CSVFormat.getDefault());
        final List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        final CSVRecord firstRecord = records.get(0);
        final CSVRecord secondRecord = records.get(1);

        assertEquals(2, firstRecord.size());
        assertEquals("Name", firstRecord.get(0));
        assertEquals("John Doe", firstRecord.get(1));
        assertEquals(null, firstRecord.getComment());

        assertEquals(2, secondRecord.size());
        assertEquals("Name", secondRecord.get(0));
        assertEquals("Jane Doe", secondRecord.get(1));
        assertEquals("This is a comment line", secondRecord.getComment());

        assertEquals(1, firstRecord.getRecordNumber());
        assertEquals(2, secondRecord.getRecordNumber());
    }

    /**
     * Test parsing a CSV file with malformed line.
     *
     * @throws IOException
     */
    @org.junit.Test(expected = IOException.class)
    public void testMalformedLine() throws IOException {
        final String csvContent = "Name,Age\n" + "John Doe,30,\nJane Doe,25";

        new CSVParser(new StringReader(csvContent), CSVFormat.getDefault()).getRecords();
    }

    /**
     * Test parsing a CSV file with a header line and no data.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testHeaderWithoutData() throws IOException {
        final String csvContent = "Name,Age";

        final CSVParser parser = new CSVParser(new StringReader(csvContent), CSVFormat.getDefault());
        final List<CSVRecord> records = parser.getRecords();

        assertEquals(0, records.size());
    }

    /**
     * Test parsing a CSV file using a custom header map.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testCustomHeaderMap() throws IOException {
        final Map<String, Integer> headerMap = new HashMap<>();
        headerMap.put("Name", 0);
        headerMap.put("Age", 1);

        final String csvContent = "Name,Age\n" + "John Doe,30\n" + "Jane Doe,25";

        final CSVParser parser = new CSVParser(new StringReader(csvContent), CSVFormat.getDefault()) {
            @Override
            protected Map<String, Integer> initializeHeader() throws IOException {
                return headerMap;
            }
        };

        final List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        final CSVRecord firstRecord = records.get(0);
        final CSVRecord secondRecord = records.get(1);

        assertEquals(2, firstRecord.size());
        assertEquals("Name", firstRecord.getName(0));
        assertEquals("John Doe", firstRecord.get(0));
        assertEquals("Age", firstRecord.getName(1));
        assertEquals("30", firstRecord.get(1));

        assertEquals(2, secondRecord.size());
        assertEquals("Name", secondRecord.getName(0));
        assertEquals("Jane Doe", secondRecord.get(0));
        assertEquals("Age", secondRecord.getName(1));
        assertEquals("25", secondRecord.get(1));
    }

    /**
     * Test parsing a CSV file with different record delimiters.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testDifferentDelimiters() throws IOException {
        final String csvContent = "Name|Age\nJohn Doe|30\nJane Doe|25";

        final CSVFormat customFormat = new CSVFormatBuilder().setDelimiter('|').build();

        final CSVParser parser = new CSVParser(new StringReader(csvContent), customFormat);
        final List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        final CSVRecord firstRecord = records.get(0);
        final CSVRecord secondRecord = records.get(1);

        assertEquals(2, firstRecord.size());
        assertEquals("Name", firstRecord.getName(0));
        assertEquals("John Doe", firstRecord.get(0));
        assertEquals("Age", firstRecord.getName(1));
        assertEquals("30", firstRecord.get(1));

        assertEquals(2, secondRecord.size());
        assertEquals("Name", secondRecord.getName(0));
        assertEquals("Jane Doe", secondRecord.get(0));
        assertEquals("Age", secondRecord.getName(1));
        assertEquals("25", secondRecord.get(1));
    }

    /**
     * Test parsing a CSV file with a custom comment character.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testCustomCommentCharacter() throws IOException {
        final String csvContent = "Name,Age\n" + "John Doe,30\n" + "# This is a comment line\n" + "Jane Doe,25";

        final CSVFormat customFormat = new CSVFormatBuilder().setComment('%').build();

        final CSVParser parser = new CSVParser(new StringReader(csvContent), customFormat);
        final List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        final CSVRecord firstRecord = records.get(0);
        final CSVRecord secondRecord = records.get(1);

        assertEquals(2, firstRecord.size());
        assertEquals("Name", firstRecord.get(0));
        assertEquals("John Doe", firstRecord.get(1));
        assertEquals(null, firstRecord.getComment());

        assertEquals(2, secondRecord.size());
        assertEquals("Name", secondRecord.get(0));
        assertEquals("Jane Doe", secondRecord.get(1));
        assertEquals("This is a comment line", secondRecord.getComment());
    }

    /**
     * Test parsing a CSV file with a custom null string.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testCustomNullString() throws IOException {
        final String csvContent = "Name,Age\n" + "John Doe,30\n" + "NULL,25";

        final CSVFormat customFormat = new CSVFormatBuilder().setNullString("NULL").build();

        final CSVParser parser = new CSVParser(new StringReader(csvContent), customFormat);
        final List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        final CSVRecord firstRecord = records.get(0);
        final CSVRecord secondRecord = records.get(1);

        assertEquals(2, firstRecord.size());
        assertEquals("Name", firstRecord.get(0));
        assertEquals("John Doe", firstRecord.get(1));
        assertEquals(null, firstRecord.get(1), "First record's second value is not null");

        assertEquals(2, secondRecord.size());
        assertEquals("Name", secondRecord.get(0));
        assertEquals("NULL", secondRecord.get(0));
        assertEquals("25", secondRecord.get(1));
    }

    /**
     * Test parsing a CSV file with a custom record separator.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testCustomRecordSeparator() throws IOException {
        final String csvContent = "Name:Age\nJohn Doe:30\nJane Doe:25";

        final CSVFormat customFormat = new CSVFormatBuilder().setRecordSeparator(':').build();

        final CSVParser parser = new CSVParser(new ByteArrayInputStream(csvContent.getBytes()), customFormat);
        final List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        final CSVRecord firstRecord = records.get(0);
        final CSVRecord secondRecord = records.get(1);

        assertEquals(2, firstRecord.size());
        assertEquals("Name", firstRecord.getName(0));
        assertEquals("John Doe", firstRecord.get(0));
        assertEquals("Age", firstRecord.getName(1));
        assertEquals("30", firstRecord.get(1));

        assertEquals(2, secondRecord.size());
        assertEquals("Name", secondRecord.getName(0));
        assertEquals("Jane Doe", secondRecord.get(0));
        assertEquals("Age", secondRecord.getName(1));
        assertEquals("25", secondRecord.get(1));
    }
}