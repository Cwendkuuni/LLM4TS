package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.generic.DefaultCSVParser;
import org.apache.commons.csv.generic.GenericCSVParser;

/**
 * Test class for {@link CSVParser}.
 */
public class CSVParserTest {

    /**
     * Tests parsing a simple CSV file using the default format.
     */
    @org.junit.Test
    public void testSimpleCSVFile() throws IOException {
        final String csvContent = "a,b\n1,2\n3,4";
        final CSVParser parser = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvContent.getBytes())));
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        for (int i = 0; i < records.size(); i++) {
            final CSVRecord record = records.get(i);
            assertEquals(2, record.size());
            assertEquals("a", record.get(0));
            assertEquals("b", record.get(1));
            if (i == 0) {
                assertEquals(1, record.getRecordNumber());
                assertEquals(1, record.getHeaderNumber());
                assertEquals("", record.getComment());
            } else {
                assertEquals(2, record.getRecordNumber());
                assertEquals(null, record.getHeaderNumber());
                assertNull(record.getComment());
            }
        }
    }

    /**
     * Tests parsing a CSV file with custom format.
     */
    @org.junit.Test
    public void testCustomCSVFormat() throws IOException {
        final String csvContent = "a;b\n1;2\n3;4";
        final CSVFormat csvFormat = CSVFormat.EXCEL.withHeader();
        final CSVParser parser = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvContent.getBytes())), csvFormat);
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        for (int i = 0; i < records.size(); i++) {
            final CSVRecord record = records.get(i);
            assertEquals(2, record.size());
            assertEquals("a", record.get(0));
            assertEquals("b", record.get(1));
            if (i == 0) {
                assertEquals(1, record.getRecordNumber());
                assertEquals(1, record.getHeaderNumber());
                assertNull(record.getComment());
            } else {
                assertEquals(2, record.getRecordNumber());
                assertEquals(null, record.getHeaderNumber());
                assertNull(record.getComment());
            }
        }
    }

    /**
     * Tests handling empty lines.
     */
    @org.junit.Test
    public void testHandlingEmptyLines() throws IOException {
        final String csvContent = "a,b\n1,2\n\n3,4";
        final CSVParser parser = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvContent.getBytes())));
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        for (int i = 0; i < records.size(); i++) {
            final CSVRecord record = records.get(i);
            assertEquals(2, record.size());
            if (i == 1) {
                assertEquals(null, record.get(0));
                assertEquals(null, record.get(1));
                assertEquals(Empty.EMPTY_STRING, record.getComment());
                assertEquals(2, record.getRecordNumber());
                assertEquals(null, record.getHeaderNumber());
            } else {
                assertEquals("a", record.get(0));
                assertEquals("b", record.get(1));
                assertNull(record.getComment());
                if (i == 0) {
                    assertEquals(1, record.getRecordNumber());
                    assertEquals(1, record.getHeaderNumber());
                    assertNull(record.getComment());
                } else if (i == 2) {
                    assertEquals(3, record.getRecordNumber());
                    assertEquals(null, record.getHeaderNumber());
                    assertNull(record.getComment());
                }
            }
        }
    }

    /**
     * Tests handling trailing commas.
     */
    @org.junit.Test
    public void testHandlingTrailingCommas() throws IOException {
        final String csvContent = "a,b,c\n1,2,3\n4,5";
        final CSVParser parser = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvContent.getBytes())));
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        for (int i = 0; i < records.size(); i++) {
            final CSVRecord record = records.get(i);
            assertEquals(3, record.size());
            if (i == 0) {
                assertEquals("a", record.get(0));
                assertEquals("b", record.get(1));
                assertEquals("c", record.get(2));
                assertEquals(1, record.getRecordNumber());
                assertEquals(1, record.getHeaderNumber());
                assertNull(record.getComment());
            } else {
                assertEquals("4", record.get(0));
                assertEquals("5", record.get(1));
                assertEquals(null, record.get(2));
                assertEquals(3, record.getRecordNumber());
                assertEquals(null, record.getHeaderNumber());
                assertNull(record.getComment());
            }
        }
    }

    /**
     * Tests handling different delimiters.
     */
    @org.junit.Test
    public void testHandlingDifferentDelimiters() throws IOException {
        final String csvContent = "col1|col2|col3\n1|2|3\n4|5|";
        final CSVFormat csvFormat = CSVFormat.DEFAULT.withDelimiter('|');
        final CSVParser parser = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvContent.getBytes())), csvFormat);
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        for (int i = 0; i < records.size(); i++) {
            final CSVRecord record = records.get(i);
            assertEquals(3, record.size());
            if (i == 0) {
                assertEquals("col1", record.get(0));
                assertEquals("col2", record.get(1));
                assertEquals("col3", record.get(2));
                assertEquals(1, record.getRecordNumber());
                assertEquals(1, record.getHeaderNumber());
                assertNull(record.getComment());
            } else {
                assertEquals("4", record.get(0));
                assertEquals(null, record.get(1));
                assertEquals(null, record.get(2));
                assertEquals(2, record.getRecordNumber());
                assertEquals(null, record.getHeaderNumber());
                assertNull(record.getComment());
            }
        }
    }

    /**
     * Tests handling different line endings.
     */
    @org.junit.Test
    public void testHandlingDifferentLineEndings() throws IOException {
        final String csvContent = "a,b\r\n1,2\r\n3,4";
        final CSVParser parser = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvContent.getBytes())));
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        for (int i = 0; i < records.size(); i++) {
            final CSVRecord record = records.get(i);
            assertEquals(2, record.size());
            if (i == 0) {
                assertEquals("a", record.get(0));
                assertEquals("b", record.get(1));
                assertEquals(1, record.getRecordNumber());
                assertEquals(1, record.getHeaderNumber());
                assertNull(record.getComment());
            } else {
                assertEquals("1", record.get(0));
                assertEquals("2", record.get(1));
                assertEquals(2, record.getRecordNumber());
                assertEquals(null, record.getHeaderNumber());
                assertNull(record.getComment());
            }
        }
    }

    /**
     * Tests handling exceptions when parsing a CSV file.
     */
    @org.junit.Test(expected = IOException.class)
    public void testHandlingExceptions() throws IOException {
        final String csvContent = "a,b\n1,2\n3,\t4";
        final CSVParser parser = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvContent.getBytes())),
                CSVFormat.DEFAULT.withHeader().withIgnoreLeadingWhiteSpace(false));
        parser.nextRecord();
        parser.nextRecord();
        parser.nextRecord();
    }

    /**
     * Tests parsing a CSV file using the default parser (CSVParser) and a generic parser (GenericCSVParser).
     */
    @org.junit.Test
    public void testParsingWithDefaultAndGenericParser() throws IOException {
        final String csvContent = "a,b\n1,2\n3,4";
        final CSVParser csvParser = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvContent.getBytes())));
        final List<CSVRecord> csvRecords = csvParser.getRecords();

        final GenericCSVParser genericCSVParser = new DefaultCSVParser(new ByteArrayReader(new ByteArrayInputStream(csvContent.getBytes())));
        final List<CSVRecord> genericCSVRecords = genericCSVParser.getRecords();

        assertEquals(csvRecords.size(), genericCSVRecords.size());
        for (int i = 0; i < csvRecords.size(); i++) {
            final CSVRecord csvRecord = csvRecords.get(i);
            final CSVRecord genericCSVRecord = genericCSVRecords.get(i);
            assertEquals(csvRecord.get(0), genericCSVRecord.get(0));
            assertEquals(csvRecord.get(1), genericCSVRecord.get(1));
            assertEquals(csvRecord.getRecordNumber(), genericCSVRecord.getRecordNumber());
            assertEquals(csvRecord.getHeaderNumber(), genericCSVRecord.getHeaderNumber());
            assertEquals(csvRecord.getComment(), genericCSVRecord.getComment());
        }
    }

    /**
     * Tests parsing a CSV file using the given parser with a custom format.
     */
    @org.junit.Test
    public void testParsingWithCustomFormat() throws IOException {
        final String csvContent = "a,b\n1,2\n3,4";
        final CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader().withComments(Constants.COMMENT_PREFIX, COMMENT_DELIMITER);
        final CSVParser parser = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvContent.getBytes())), csvFormat);
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        for (int i = 0; i < records.size(); i++) {
            final CSVRecord record = records.get(i);
            assertEquals(2, record.size());
            if (i == 0) {
                assertEquals("a", record.get(0));
                assertEquals("b", record.get(1));
                assertEquals(1, record.getRecordNumber());
                assertEquals(1, record.getHeaderNumber());
                assertEquals("", record.getComment());
            } else {
                assertEquals("1", record.get(0));
                assertEquals("2", record.get(1));
                assertEquals(2, record.getRecordNumber());
                assertEquals(null, record.getHeaderNumber());
                assertEquals("", record.getComment());
            }
        }
    }

    private static final String COMMENT_DELIMITER = ";";

}