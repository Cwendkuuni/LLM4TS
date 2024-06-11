package org.apache.commons.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Test cases for {@link CSVParser}.
 */
public class CSVParserTest {

    private static final String TEST_CSV = "a,b,c\n1,2,3\n4,5,6\n";

    private CSVParser parser;
    private CSVFormat format;

    @Before
    public void setUp() throws IOException {
        format = CSVFormat.DEFAULT.withHeader();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_CSV.getBytes());
        ByteArrayReader reader = new ByteArrayReader(inputStream.readAllBytes());
        parser = new CSVParser(reader, format);
    }

    @After
    public void tearDown() throws IOException {
        if (parser != null) {
            parser.close();
        }
    }

    /**
     * Test parsing records from a file with default format.
     *
     * @throws IOException on parsing errors
     */
    @Test
    public void testParseRecords() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals(3, record1.size());
        assertEquals("a", record1.get(0));
        assertEquals("1", record1.get(1));
        assertEquals("c", record1.get(2));

        CSVRecord record2 = records.get(1);
        assertEquals(3, record2.size());
        assertEquals("b", record2.get(0));
        assertEquals("2", record2.get(1));
        assertEquals("3", record2.get(2));

        CSVRecord record3 = records.get(2);
        assertEquals(3, record3.size());
        assertEquals("c", record3.get(0));
        assertEquals("4", record3.get(1));
        assertEquals("6", record3.get(2));
    }

    /**
     * Test parsing records from a file with custom format.
     *
     * @throws IOException on parsing errors
     */
    @Test
    public void testParseRecordsWithCustomFormat() throws IOException {
        CSVFormat customFormat = CSVFormat.newBuilder()
                .setHeader(Arrays.asList("Header1", "Header2", "Header3"))
                .setDelimiter(',')
                .build();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_CSV.getBytes());
        ByteArrayReader reader = new ByteArrayReader(inputStream.readAllBytes());
        CSVParser parser = new CSVParser(reader, customFormat);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals(3, record1.size());
        assertEquals("Header1", record1.getHeaderMap().get("a"));
        assertEquals("1", record1.get(0));
        assertEquals("Header2", record1.getHeaderMap().get("b"));
        assertEquals("3", record1.get(1));
        assertEquals("Header3", record1.getHeaderMap().get("c"));

        CSVRecord record2 = records.get(1);
        assertEquals(3, record2.size());
        assertEquals("Header2", record2.getHeaderMap().get("b"));
        assertEquals("2", record2.get(0));
        assertEquals(null, record2.get(1)); // null value
        assertEquals("3", record2.get(2));

        CSVRecord record3 = records.get(2);
        assertEquals(3, record3.size());
        assertEquals("Header3", record3.getHeaderMap().get("c"));
        assertEquals(null, record3.get(0)); // missing value
        assertEquals("6", record3.get(1));

        assertNull(parser.getRecords().get(3)); // no more records
    }

    /**
     * Test parsing records from an empty file.
     */
    @Test
    public void testParseEmptyFile() throws IOException {
        CSVParser parser = new CSVParser(new StringReader(""), format);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(0, records.size());
    }

    /**
     * Test parsing records from a file with invalid CSV format.
     *
     * @throws IOException on parsing errors
     */
    @Test
    public void testParseInvalidCSV() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("a,b\na".getBytes());
        ByteArrayReader reader = new ByteArrayReader(inputStream.readAllBytes());
        assertThrows(IllegalArgumentException.class, () -> new CSVParser(reader, format));
    }

    /**
     * Test parsing records from a file with a missing delimiter.
     *
     * @throws IOException on parsing errors
     */
    @Test
    public void testParseMissingDelimiter() throws IOException {
        CSVFormat customFormat = CSVFormat.newBuilder().setHeader(false).build();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("a1b2c3\na1b2c3\na1b2c3".getBytes());
        ByteArrayReader reader = new ByteArrayReader(inputStream.readAllBytes());
        CSVParser parser = new CSVParser(reader, customFormat);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        assertEquals(3, record.size());
        assertEquals("1", record.get(0));
        assertEquals("2", record.get(1));
        assertEquals("3", record.get(2));

        assertNull(parser.getRecords().get(1)); // no more records
    }

    /**
     * Test parsing records from a file with an empty record.
     *
     * @throws IOException on parsing errors
     */
    @Test
    public void testParseEmptyRecord() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("a,\nb\nc\n".getBytes());
        ByteArrayReader reader = new ByteArrayReader(inputStream.readAllBytes());
        CSVParser parser = new CSVParser(reader, format);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals(3, record1.size());
        assertEquals("a", record1.get(0));
        assertEquals(Collections.emptyList(), record1.getValues());

        CSVRecord record2 = records.get(1);
        assertEquals(3, record2.size());
        assertEquals("b", record2.get(0));
        assertEquals(Arrays.asList(""), record2.getValues());

        CSVRecord record3 = records.get(2);
        assertEquals(3, record3.size());
        assertEquals("c", record3.get(0));
        assertEquals(Arrays.asList(""), record3.getValues());
    }
}