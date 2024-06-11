package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Test class for {@link CSVParser}
 */
public class CSVParserTest {

    /**
     * Test parsing a simple CSV file
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testParseSimpleCSV() throws IOException {
        // Given
        final String testCSV = "Name,Age\nJohn Doe,32\nJane Smith,28";
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVParser csvParser = CSVParser.parse(new StringReader(testCSV), csvFormat);
        List<CSVRecord> records = csvParser.getRecords();

        // Then
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        // Check first record
        assertEquals("Name", record1.get(0));
        assertEquals("John Doe", record1.get(1));
        assertEquals(32, record1.getInteger(1));

        // Check second record
        assertEquals("Name", record2.get(0));
        assertEquals("Jane Smith", record2.get(1));
        assertEquals(28, record2.getInteger(1));

        // Verify closing resources
        csvParser.close();
    }

    /**
     * Test parsing an empty CSV file
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testParseEmptyCSV() throws IOException {
        // Given
        final String testCSV = "";
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVParser csvParser = CSVParser.parse(new StringReader(testCSV), csvFormat);
        List<CSVRecord> records = csvParser.getRecords();

        // Then
        assertTrue(records.isEmpty());

        // Verify closing resources
        csvParser.close();
    }

    /**
     * Test parsing a CSV file with a header row
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testParseCSVWithHeader() throws IOException {
        // Given
        final String testCSV = "Name,Age\nJohn Doe,32\nJane Smith,28";
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader();

        // When
        CSVParser csvParser = CSVParser.parse(new StringReader(testCSV), csvFormat);
        List<CSVRecord> records = csvParser.getRecords();

        // Then
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        // Check first record
        assertEquals("Name", record1.get(0));
        assertEquals("John Doe", record1.get(1));
        assertEquals(32, record1.getInteger(1));

        // Check second record
        assertEquals("Age", record2.get(0));
        assertEquals(32, record2.getInteger(1));
        assertEquals(28, record2.getInteger(2));

        // Verify closing resources
        csvParser.close();
    }

    /**
     * Test parsing a CSV file with a custom format
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testParseCSVWithCustomFormat() throws IOException {
        // Given
        final String testCSV = "Name,Age\nJohn Doe,32\nJane Smith,28";
        CSVFormat csvFormat = new CSVFormat(',', '\n')
                .withHeader()
                .withIgnoreLeadingWhiteSpace()
                .withIgnoreQuotations(true)
                .withTrimWhiteSpace(true);

        // When
        CSVParser csvParser = CSVParser.parse(new StringReader(testCSV), csvFormat);
        List<CSVRecord> records = csvParser.getRecords();

        // Then
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        // Check first record
        assertEquals("Name", record1.get(0));
        assertEquals("John Doe", record1.get(1));
        assertEquals(32, record1.getInteger(1));

        // Check second record
        assertEquals("Age", record2.get(0));
        assertEquals(32, record2.getInteger(1));
        assertEquals(28, record2.getInteger(2));

        // Verify closing resources
        csvParser.close();
    }

    /**
     * Test parsing a CSV file with a custom format and header name mapping
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testParseCSVWithCustomFormatAndHeaderMapping() throws IOException {
        // Given
        final String testCSV = "Name,Age\nJohn Doe,32\nJane Smith,28";
        CSVFormat csvFormat = new CSVFormat(',', '\n')
                .withHeader()
                .withHeaderMap(Map.of("Name", "personName", "Age", "age"))
                .withIgnoreLeadingWhiteSpace()
                .withIgnoreQuotations(true)
                .withTrimWhiteSpace(true);

        // When
        CSVParser csvParser = CSVParser.parse(new StringReader(testCSV), csvFormat);
        List<CSVRecord> records = csvParser.getRecords();

        // Then
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        // Check first record
        assertEquals("personName", record1.get(0));
        assertEquals("John Doe", record1.get(1));
        assertEquals(32, record1.getInteger(1));

        // Check second record
        assertEquals("age", record2.get(0));
        assertEquals(32, record2.getInteger(1));
        assertEquals(28, record2.getInteger(2));

        // Verify closing resources
        csvParser.close();
    }

    /**
     * Test parsing a CSV file and parsing into memory
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testParseCSVIntoMemory() throws IOException {
        // Given
        final String testCSV = "Name,Age\nJohn Doe,32\nJane Smith,28";
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVParser csvParser = new CSVParser(new StringReader(testCSV), csvFormat);
        List<CSVRecord> records = csvParser.getRecords();

        // Then
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        // Check first record
        assertEquals("Name", record1.get(0));
        assertEquals("John Doe", record1.get(1));
        assertEquals(32, record1.getInteger(1));

        // Check second record
        assertEquals("Name", record2.get(0));
        assertEquals("Jane Smith", record2.get(1));
        assertEquals(28, record2.getInteger(1));

        // Verify closing resources
        csvParser.close();
    }

    /**
     * Test parsing a CSV file with comments
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testParseCSVWithComments() throws IOException {
        // Given
        final String testCSV = "Name,Age\nJohn Doe,32\n# This is a comment\nJane Smith,28";
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVParser csvParser = CSVParser.parse(new StringReader(testCSV), csvFormat);
        List<CSVRecord> records = csvParser.getRecords();

        // Then
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        // Check first record
        assertEquals("Name", record1.get(0));
        assertEquals("John Doe", record1.get(1));
        assertEquals(32, record1.getInteger(1));
        assertNull(record1.getComment());

        // Check second record
        assertEquals("Name", record2.get(0));
        assertEquals("Jane Smith", record2.get(1));
        assertEquals(28, record2.getInteger(1));
        assertEquals("# This is a comment", record2.getComment());

        // Verify closing resources
        csvParser.close();
    }
}