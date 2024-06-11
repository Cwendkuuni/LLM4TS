package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Test for {@link CSVParser} class.
 */
public class CSVParserTest {

    /**
     * Test parsing a simple CSV file.
     *
     * @throws IOException
     *             if an error occurs during the test.
     */
    @org.junit.Test
    public void testParseSimpleCsv() throws IOException {
        final String csvData = "A,B\n1,2\n3,4";

        final CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes())),
                CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertSame(2, record1.size());
        assertEquals("1", record1.get(0));
        assertEquals("2", record1.get(1));

        CSVRecord record2 = records.get(1);
        assertSame(2, record2.size());
        assertEquals("3", record2.get(0));
        assertEquals("4", record2.get(1));

        parser.close();
    }

    /**
     * Test parsing a CSV file with no header.
     *
     * @throws IOException
     *             if an error occurs during the test.
     */
    @org.junit.Test
    public void testParseCsvWithoutHeader() throws IOException {
        final String csvData = "1,2\n3,4";

        final CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes())),
                CSVFormat.DEFAULT.withHeaderCount(0));
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertSame(2, record1.size());
        assertEquals("1", record1.get(0));
        assertEquals("2", record1.get(1));

        CSVRecord record2 = records.get(1);
        assertSame(2, record2.size());
        assertEquals("3", record2.get(0));
        assertEquals("4", record2.get(1));

        parser.close();
    }

    /**
     * Test parsing a CSV file with a header.
     *
     * @throws IOException
     *             if an error occurs during the test.
     */
    @org.junit.Test
    public void testParseCsvWithHeader() throws IOException {
        final String csvData = "Name,Age\nJohn,25\nMary,30";

        final CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes())),
                CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals(2, record1.size());
        assertSame("Name", record1.getHeader().get(0));
        assertEquals("Age", record1.getHeader().get(1));
        assertEquals("John", record1.get(0));
        assertEquals("25", record1.get(1));

        CSVRecord record2 = records.get(1);
        assertEquals(2, record2.size());
        assertSame("Name", record2.getHeader().get(0));
        assertSame("Age", record2.getHeader().get(1));
        assertEquals("Mary", record2.get(0));
        assertEquals("30", record2.get(1));

        parser.close();
    }

    /**
     * Test parsing a CSV file with an empty line.
     *
     * @throws IOException
     *             if an error occurs during the test.
     */
    @org.junit.Test
    public void testParseCsvWithEmptyLine() throws IOException {
        final String csvData = "Name,Age\nJohn,25\n\nMary,30";

        final CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes())),
                CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals(2, record1.size());
        assertSame("Name", record1.getHeader().get(0));
        assertSame("Age", record1.getHeader().get(1));
        assertEquals("John", record1.get(0));
        assertEquals("25", record1.get(1));

        CSVRecord record2 = records.get(1);
        assertEquals(2, record2.size());
        assertSame("Name", record2.getHeader().get(0));
        assertSame("Age", record2.getHeader().get(1));
        assertEquals("Mary", record2.get(0));
        assertEquals("30", record2.get(1));

        parser.close();
    }

    /**
     * Test parsing a CSV file with a comment.
     *
     * @throws IOException
     *             if an error occurs during the test.
     */
    @org.junit.Test
    public void testParseCsvWithComment() throws IOException {
        final String csvData = "A,B\n1,2\n-- This is a comment\n3,4";

        final CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes())),
                CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertSame(2, record1.size());
        assertEquals("1", record1.get(0));
        assertEquals("2", record1.get(1));

        CSVRecord record2 = records.get(1);
        assertSame(2, record2.size());
        assertEquals("3", record2.get(0));
        assertEquals("4", record2.get(1));

        String comment = record2.getComment();
        assertNotNull(comment);
        assertEquals("This is a comment", comment);

        parser.close();
    }

    /**
     * Test parsing a CSV file with an invalid format.
     *
     * @throws IOException
     *             if an error occurs during the test.
     */
    @org.junit.Test(expected = IOException.class)
    public void testParseCsvWithInvalidFormat() throws IOException {
        final String csvData = "A,B\n1,2\n3,4\n5";

        final CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes())),
                CSVFormat.DEFAULT);

        parser.getRecords();

        fail("Unexpected success");
    }

    /**
     * Test closing the parser.
     *
     * @throws IOException
     *             if an error occurs during the test.
     */
    @org.junit.Test
    public void testCloseParser() throws IOException {
        final String csvData = "A,B\n1,2\n3,4";

        final CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes())),
                CSVFormat.DEFAULT);
        parser.close();

        try {
            parser.getRecords();
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    /**
     * Test getting the header map.
     *
     * @throws IOException
     *             if an error occurs during the test.
     */
    @org.junit.Test
    public void testGetHeaderMap() throws IOException {
        final String csvData = "Name,Age\nJohn,25\nMary,30";

        final CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes())),
                CSVFormat.DEFAULT);
        Map<String, Integer> headerMap = parser.getHeaderMap();

        assertEquals(2, headerMap.size());
        assertEquals("Name", headerMap.get("Name"));
        assertEquals("Age", headerMap.get("Age"));

        parser.close();
    }

    /**
     * Test getting the header map with a CSV file without a header.
     *
     * @throws IOException
     *             if an error occurs during the test.
     */
    @org.junit.Test
    public void testGetHeaderMapWithoutHeader() throws IOException {
        final String csvData = "1,2\n3,4";

        final CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes())),
                CSVFormat.DEFAULT.withHeaderCount(0));
        Map<String, Integer> headerMap = parser.getHeaderMap();

        assertTrue(Collections.isEmpty(headerMap));

        parser.close();
    }

    /**
     * Test getting the header map with an empty CSV file.
     *
     * @throws IOException
     *             if an error occurs during the test.
     */
    @org.junit.Test
    public void testGetHeaderMapWithEmptyCsv() throws IOException {
        final String csvData = "";

        final CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes())),
                CSVFormat.DEFAULT);
        Map<String, Integer> headerMap = parser.getHeaderMap();

        assertTrue(Collections.isEmpty(headerMap));

        parser.close();
    }

    /**
     * Test parsing a CSV file with a large number of records.
     *
     * @throws IOException
     *             if an error occurs during the test.
     */
    @org.junit.Test
    public void testParseLargeCsvFile() throws IOException {
        final int recordCount = 100_000;
        final String csvData = String.join(System.lineSeparator(), Arrays.asList(new String[recordCount + 1]));
        for (int i = 0; i <= recordCount; i++) {
            csvData += String.join(",", Arrays.asList(new String[] {"Column " + i, "Value " + i}));
            if (i < recordCount) {
                csvData += System.lineSeparator();
            }
        }

        final CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes())),
                CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(recordCount + 1, records.size());

        parser.close();
    }

    /**
     * Test parsing a CSV file with a large number of columns.
     *
     * @throws IOException
     *             if an error occurs during the test.
     */
    @org.junit.Test
    public void testParseLargeCsvFileWithManyColumns() throws IOException {
        final int columnCount = 1000;
        final String csvData = "Column";
        for (int i = 0; i < columnCount; i++) {
            csvData += ",Column_" + i;
        }
        csvData += "\n";
        for (int i = 0; i < 10; i++) {
            csvData += String.join(",", Arrays.asList(new String[columnCount + 1]));
            if (i < 9) {
                csvData += System.lineSeparator();
            }
        }

        final CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes())),
                CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(10, records.size());

        parser.close();
    }

    /**
     * Test parsing a CSV file with a large number of columns and records.
     *
     * @throws IOException
     *             if an error occurs during the test.
     */
    @org.junit.Test
    public void testParseLargeCsvFileWithManyColumnsAndRecords() throws IOException {
        final int columnCount = 1000;
        final int recordCount = 100_000;
        final String csvData = "Column";
        for (int i = 0; i < columnCount; i++) {
            csvData += ",Column_" + i;
        }
        csvData += "\n";
        for (int i = 0; i < recordCount; i++) {
            csvData += String.join(",", Arrays.asList(new String[columnCount + 1]));
            if (i < recordCount - 1) {
                csvData += System.lineSeparator();
            }
        }

        final CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes())),
                CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(recordCount, records.size());

        parser.close();
    }

    /**
     * Test parsing a CSV file with invalid data.
     *
     * @throws IOException
     *             if an error occurs during the test.
     */
    @org.junit.Test(expected = IOException.class)
    public void testParseCsvWithInvalidData() throws IOException {
        final String csvData = "A,B\n1,2\n3,\"Invalid\n4,5";

        final CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes())),
                CSVFormat.DEFAULT);

        parser.getRecords();

        fail("Unexpected success");
    }
}