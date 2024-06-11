package org.apache.commons.csv;

import static org.apache.commons.csv.CSVParser.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Test class for {@link CSVParser}
 */
public class CSVParserTest {

    /**
     * Test parsing a CSV file with valid content.
     */
    @Test
    public void testValidCSVFile() throws IOException {
        String csvContent = "Name,Age,City\nJohn Doe,30,New York\nJane Smith,25,Chicago";

        List<CSVRecord> records = parse(new StringReader(csvContent), CSVFormat.RFC4180).getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals("Name", record1.get(0));
        assertEquals("John Doe", record1.get(1));
        assertEquals("Age", record1.get(2));
        assertEquals(30, record1.get(2), Integer.class);
        assertEquals("City", record1.get(3));
        assertEquals("New York", record1.get(3));

        CSVRecord record2 = records.get(1);
        assertEquals("Name", record2.get(0));
        assertEquals("Jane Smith", record2.get(1));
        assertEquals("Age", record2.get(2));
        assertEquals(25, record2.get(2), Integer.class);
        assertEquals("City", record2.get(3));
        assertEquals("Chicago", record2.get(3));
    }

    /**
     * Test parsing a CSV file with an empty line.
     */
    @Test
    public void testEmptyLine() throws IOException {
        String csvContent = "Name,Age,City\nJohn Doe,30,New York\n\nJane Smith,25,Chicago";

        List<CSVRecord> records = parse(new StringReader(csvContent), CSVFormat.RFC4180).getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals("Name", record1.get(0));
        assertEquals("John Doe", record1.get(1));
        assertEquals("Age", record1.get(2));
        assertEquals(30, record1.get(2), Integer.class);
        assertEquals("City", record1.get(3));
        assertEquals("New York", record1.get(3));

        CSVRecord record2 = records.get(1);
        assertEquals("Name", record2.get(0));
        assertEquals("Jane Smith", record2.get(1));
        assertEquals("Age", record2.get(2));
        assertEquals(25, record2.get(2), Integer.class);
        assertEquals("City", record2.get(3));
        assertEquals("Chicago", record2.get(3));
    }

    /**
     * Test parsing a CSV file with an invalid content.
     */
    @Test(expected = IOException.class)
    public void testInvalidCSVFile() throws IOException {
        String csvContent = "Name,Age,City\nJohn Doe,30,New York\nName,25,Chicago";

        parse(new StringReader(csvContent), CSVFormat.RFC4180).getRecords();
    }

    /**
     * Test parsing an empty CSV file.
     */
    @Test
    public void testEmptyCSVFile() throws IOException {
        List<CSVRecord> records = parse(new StringReader(""), CSVFormat.RFC4180).getRecords();
        assertEquals(0, records.size());
    }

    /**
     * Test parsing a CSV file with a custom format.
     */
    @Test
    public void testCustomFormat() throws IOException {
        String csvContent = "Name;Age;City\nJohn Doe;30;New York\nJane Smith;25;Chicago";

        List<CSVRecord> records = parse(new StringReader(csvContent), CSVFormat.EXCEL.withHeader("Name;Age;City")).getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals("Name", record1.get(0));
        assertEquals("John Doe", record1.get(1));
        assertEquals("Age", record1.get(2));
        assertEquals(30, record1.get(2), Integer.class);
        assertEquals("City", record1.get(3));
        assertEquals("New York", record1.get(3));

        CSVRecord record2 = records.get(1);
        assertEquals("Name", record2.get(0));
        assertEquals("Jane Smith", record2.get(1));
        assertEquals("Age", record2.get(2));
        assertEquals(25, record2.get(2), Integer.class);
        assertEquals("City", record2.get(3));
        assertEquals("Chicago", record2.get(3));
    }

    /**
     * Test parsing a CSV file with a null header.
     */
    @Test
    public void testNullHeader() throws IOException {
        String csvContent = "Name,Age,City\nJohn Doe,30,New York\nJane Smith,25,Chicago";

        List<CSVRecord> records = parse(new StringReader(csvContent), CSVFormat.RFC4180).getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals(2, record1.size());
        assertEquals("John Doe", record1.get(0));
        assertEquals("30", record1.get(1));

        CSVRecord record2 = records.get(1);
        assertEquals(2, record2.size());
        assertEquals("Jane Smith", record2.get(0));
        assertEquals("25", record2.get(1));
    }

    /**
     * Test parsing a CSV file with an empty header.
     */
    @Test
    public void testEmptyHeader() throws IOException {
        String csvContent = "Name,Age,City\nJohn Doe,30,New York\nJane Smith,25,Chicago";

        List<CSVRecord> records = parse(new StringReader(csvContent), new CSVFormat().build()).getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals(3, record1.size());
        assertEquals("Name", record1.get(0));
        assertEquals("", record1.get(1));
        assertEquals("", record1.get(2));

        CSVRecord record2 = records.get(1);
        assertEquals(3, record2.size());
        assertEquals("", record2.get(0));
        assertEquals("", record2.get(1));
        assertEquals("", record2.get(2));
    }

    /**
     * Test parsing a CSV file with a missing value.
     */
    @Test
    public void testMissingValue() throws IOException {
        String csvContent = "Name,Age,City\nJohn Doe,30,\nJane Smith,25,Chicago";

        List<CSVRecord> records = parse(new StringReader(csvContent), CSVFormat.RFC4180).getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals("Name", record1.get(0));
        assertEquals("John Doe", record1.get(1));
        assertEquals("Age", record1.get(2));
        assertNull(record1.get(3));

        CSVRecord record2 = records.get(1);
        assertEquals("Name", record2.get(0));
        assertEquals("Jane Smith", record2.get(1));
        assertEquals("Age", record2.get(2));
        assertEquals("Chicago", record2.get(3));
    }

    /**
     * Test parsing a CSV file with a missing header.
     */
    @Test
    public void testMissingHeader() throws IOException {
        String csvContent = "Name,Age,City\nJohn Doe,30,New York\nJane Smith,25,Chicago";

        List<CSVRecord> records = parse(new StringReader(csvContent), new CSVFormat().build()).getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals(3, record1.size());
        assertEquals("", record1.get(0));
        assertEquals("Name", record1.get(1));
        assertEquals("John Doe", record1.get(2));

        CSVRecord record2 = records.get(1);
        assertEquals(3, record2.size());
        assertEquals("", record2.get(0));
        assertEquals("Age", record2.get(1));
        assertEquals("Jane Smith", record2.get(2));
    }

    /**
     * Test parsing a CSV file with an invalid character encoding.
     */
    @Test(expected = IOException.class)
    public void testInvalidCharacterEncoding() throws IOException {
        String csvContent = "Name,Age,City\nJohn Doe,30,New York\nJane Smith,25,Chicago";

        parse(new InputStreamReader(new ByteArrayInputStream("invalid bytes".getBytes("UTF-16")), "UTF-8"),
                CSVFormat.RFC4180).getRecords();
    }

    /**
     * Test parsing a CSV file with a custom delimiter.
     */
    @Test
    public void testCustomDelimiter() throws IOException {
        String csvContent = "Name|Age|City\nJohn Doe|30|New York\nJane Smith|25|Chicago";

        List<CSVRecord> records = parse(new StringReader(csvContent), CSVFormat.DEFAULT.withDelimiter('|')).getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals("Name", record1.get(0));
        assertEquals("John Doe", record1.get(1));
        assertEquals("Age", record1.get(2));
        assertEquals(30, record1.get(2), Integer.class);
        assertEquals("City", record1.get(3));
        assertEquals("New York", record1.get(3));

        CSVRecord record2 = records.get(1);
        assertEquals("Name", record2.get(0));
        assertEquals("Jane Smith", record2.get(1));
        assertEquals("Age", record2.get(2));
        assertEquals(25, record2.get(2), Integer.class);
        assertEquals("City", record2.get(3));
        assertEquals("Chicago", record2.get(3));
    }

    /**
     * Test parsing a CSV file with a custom null string.
     */
    @Test
    public void testCustomNullString() throws IOException {
        String csvContent = "Name,Age,City\nJohn Doe,30,New York\nJane Smith,25,Chicago\n";

        List<CSVRecord> records = parse(new StringReader(csvContent), CSVFormat.RFC4180.withNullString("")).getRecords();
        assertEquals(3, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals("Name", record1.get(0));
        assertEquals("John Doe", record1.get(1));
        assertEquals("Age", record1.get(2));
        assertEquals(30, record1.get(2), Integer.class);
        assertEquals("City", record1.get(3));
        assertEquals("New York", record1.get(3));
        assertEquals(null, record1.getComment());

        CSVRecord record2 = records.get(1);
        assertEquals("Name", record2.get(0));
        assertEquals("Jane Smith", record2.get(1));
        assertEquals("Age", record2.get(2));
        assertEquals(25, record2.get(2), Integer.class);
        assertEquals("City", record2.get(3));
        assertEquals("Chicago", record2.get(3));
        assertEquals("", record2.getComment());

        CSVRecord record3 = records.get(2);
        assertNull(record3);
    }
}