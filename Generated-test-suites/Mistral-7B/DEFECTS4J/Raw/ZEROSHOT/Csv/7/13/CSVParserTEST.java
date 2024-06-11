package org.apache.commons.csv;

import static org.apache.commons.csv.CSVParser.parse;
import static org.apache.commons.csv.CSVRecord.VALUE_SEPARATOR;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import org.junit.Test;

/**
 * Test CSVParser class
 */
public class CSVParserTest {

    /**
     * Test parsing CSV file with default format
     *
     * @throws IOException
     */
    @Test
    public void testParseDefaultFormat() throws IOException {
        final String csv = "col1,col2\nvalue1,value2";

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(csv.getBytes());
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final CSVParser parser = parse(inputStream, CSVFormat.DEFAULT);

        final List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertFalse(records.isEmpty());
        final CSVRecord record = records.get(0);
        assertNotNull(record);
        assertArrayEquals(new String[] { "col1", "col2" }, record.getHeaderMap().keySet().toArray(new String[0]));
        assertArrayEquals(new Object[] { "value1", "value2" }, record.getValues());

        parser.close();
    }

    /**
     * Test parsing CSV file with specified format
     *
     * @throws IOException
     */
    @Test
    public void testParseCustomFormat() throws IOException {
        final String csv = "col1;col2\nvalue1;value2";

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(csv.getBytes());
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final CSVFormat format = CSVFormat.newBuilder().setHeader("col1", "col2")
                .setDelimiter(VALUE_SEPARATOR.getValue(';')).build();
        final CSVParser parser = parse(inputStream, format);

        final List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertFalse(records.isEmpty());
        final CSVRecord record = records.get(0);
        assertNotNull(record);
        assertArrayEquals(new String[] { "col1", "col2" }, record.getHeaderMap().keySet().toArray(new String[0]));
        assertArrayEquals(new Object[] { "value1", "value2" }, record.getValues());

        parser.close();
    }

    /**
     * Test parsing CSV file with empty header
     *
     * @throws IOException
     */
    @Test
    public void testParseEmptyHeaderFormat() throws IOException {
        final String csv = "col1,col2\nvalue1,value2";

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(csv.getBytes());
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final CSVFormat format = CSVFormat.DEFAULT.withHeader();
        final CSVParser parser = parse(inputStream, format);

        final List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertFalse(records.isEmpty());
        assertNull(records.get(0).getHeaderMap());

        parser.close();
    }

    /**
     * Test parsing CSV file with empty content
     *
     * @throws IOException
     */
    @Test
    public void testParseEmptyContentFormat() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final CSVParser parser = parse(inputStream, CSVFormat.DEFAULT);

        final List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertTrue(records.isEmpty());

        parser.close();
    }

    /**
     * Test parsing CSV file with multi-line values
     *
     * @throws IOException
     */
    @Test
    public void testParseMultiLineValuesFormat() throws IOException {
        final String csv = "col1,col2\nvalue1\nline1\nline2\nvalue2";

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(csv.getBytes());
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final CSVParser parser = parse(inputStream, CSVFormat.DEFAULT);

        final List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertFalse(records.isEmpty());
        final CSVRecord record = records.get(0);
        assertNotNull(record);
        assertArrayEquals(new String[] { "col1", "col2" }, record.getHeaderMap().keySet().toArray(new String[0]));
        assertArrayEquals(new Object[] { "value1\nline1\nline2", "value2" }, record.getValues());

        parser.close();
    }

    /**
     * Test parsing CSV file with quote character
     *
     * @throws IOException
     */
    @Test
    public void testParseQuoteCharacterFormat() throws IOException {
        final String csv = "col1,\"col2,col3\"\nvalue1,\"value2,value3\"";

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(csv.getBytes());
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final CSVParser parser = parse(inputStream, CSVFormat.DEFAULT.withQuote('"'));

        final List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertFalse(records.isEmpty());
        final CSVRecord record = records.get(0);
        assertNotNull(record);
        assertArrayEquals(new String[] { "col1", "col2,col3" }, record.getHeaderMap().keySet().toArray(new String[0]));
        assertArrayEquals(new Object[] { "value1", "value2,value3" }, record.getValues());

        parser.close();
    }

    /**
     * Test parsing CSV file with empty values
     *
     * @throws IOException
     */
    @Test
    public void testParseEmptyValuesFormat() throws IOException {
        final String csv = "col1,col2\nvalue1,,value3";

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(csv.getBytes());
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final CSVParser parser = parse(inputStream, CSVFormat.DEFAULT);

        final List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertFalse(records.isEmpty());
        final CSVRecord record = records.get(0);
        assertNotNull(record);
        assertArrayEquals(new String[] { "col1", "col2" }, record.getHeaderMap().keySet().toArray(new String[0]));
        assertArrayEquals(new Object[] { "value1", null, "value3" }, record.getValues());

        parser.close();
    }

    /**
     * Test parsing CSV file with custom header and empty values
     *
     * @throws IOException
     */
    @Test
    public void testParseCustomHeaderAndEmptyValuesFormat() throws IOException {
        final String csv = "col1,col2\nvalue1,,value3";

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(csv.getBytes());
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final CSVFormat format = CSVFormat.newBuilder().setHeader("col1", "col2").build();
        final CSVParser parser = parse(inputStream, format);

        final List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertFalse(records.isEmpty());
        final CSVRecord record = records.get(0);
        assertNotNull(record);
        assertArrayEquals(new String[] { "col1", "col2" }, record.getHeaderMap().keySet().toArray(new String[0]));
        assertArrayEquals(new Object[] { "value1", null, "value3" }, record.getValues());

        parser.close();
    }

    /**
     * Test parsing CSV file with no values and custom header
     *
     * @throws IOException
     */
    @Test
    public void testParseNoValuesCustomHeaderFormat() throws IOException {
        final String csv = "col1,col2\n";

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(csv.getBytes());
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final CSVFormat format = CSVFormat.newBuilder().setHeader("col1", "col2").build();
        final CSVParser parser = parse(inputStream, format);

        final List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertTrue(records.isEmpty());

        parser.close();
    }

    /**
     * Test parsing CSV file with no header and empty content
     *
     * @throws IOException
     */
    @Test
    public void testParseNoHeaderEmptyContentFormat() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final CSVParser parser = parse(inputStream, CSVFormat.DEFAULT.withoutHeader());

        final List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertTrue(records.isEmpty());

        parser.close();
    }

    /**
     * Test parsing CSV file with no header and content
     *
     * @throws IOException
     */
    @Test
    public void testParseNoHeaderContentFormat() throws IOException {
        final String csv = "value1\nvalue2";

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(csv.getBytes());
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final CSVParser parser = parse(inputStream, CSVFormat.DEFAULT.withoutHeader());

        final List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertFalse(records.isEmpty());
        final CSVRecord record = records.get(0);
        assertArrayEquals(new String[0], record.getHeaderMap().keySet().toArray(new String[0]));
        assertArrayEquals(new Object[] { "value1", "value2" }, record.getValues());

        parser.close();
    }

    /**
     * Test parsing CSV file with no header and empty values
     *
     * @throws IOException
     */
    @Test
    public void testParseNoHeaderEmptyValuesFormat() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(Collections.emptyByteArray());
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final CSVParser parser = parse(inputStream, CSVFormat.DEFAULT.withoutHeader());

        final List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertTrue(records.isEmpty());

        parser.close();
    }

}