package org.apache.commons.csv.test;

import static org.junit.Assert.*;
import static org.apache.commons.csv.CSVRecord.*;

import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Test cases for {@link CSVParser}.
 */
public class CSVParserTest {

    /**
     * Test parsing a simple CSV file.
     *
     * @throws IOException
     *             if the test fails
     */
    @Test
    public void testSimpleCSV() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader("a,b\nc,d\ne,f"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals(2, record1.size());
        assertEquals("a", record1.get(0));
        assertEquals("b", record1.get(1));

        CSVRecord record2 = records.get(1);
        assertEquals(2, record2.size());
        assertEquals("c", record2.get(0));
        assertEquals("d", record2.get(1));

        CSVRecord record3 = records.get(2);
        assertEquals(2, record3.size());
        assertEquals("e", record3.get(0));
        assertEquals("f", record3.get(1));
    }

    /**
     * Test parsing a CSV file with a header.
     *
     * @throws IOException
     *             if the test fails
     */
    @Test
    public void testCSVWithHeader() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader("Name,Age\nAlice,25\nBob,30"), CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals(2, record1.size());
        assertEquals("Name", record1.getHeaderName(0));
        assertEquals("Alice", record1.get(0));
        assertEquals("Age", record1.getHeaderName(1));
        assertEquals(25, record1.get(1).intValue());

        CSVRecord record2 = records.get(1);
        assertEquals(2, record2.size());
        assertEquals("Name", record2.getHeaderName(0));
        assertEquals("Bob", record2.get(0));
        assertEquals("Age", record2.getHeaderName(1));
        assertEquals(30, record2.get(1).intValue());
    }

    /**
     * Test parsing a CSV file with a custom format.
     *
     * @throws IOException
     *             if the test fails
     */
    @Test
    public void testCSVWithCustomFormat() throws IOException {
        CSVFormat customFormat = new CSVFormatBuilder()
                .setDelimiter(',')
                .setHeader("Name", "Age")
                .setHeaderSeparator(';')
                .build();

        CSVParser parser = CSVParser.parse(new StringReader("Name;Age\nAlice,25\nBob,30"), customFormat);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals(2, record1.size());
        assertEquals("Name", record1.getHeaderName(0));
        assertEquals("Alice", record1.get(0));
        assertEquals("Age", record1.getHeaderName(1));
        assertEquals(25, record1.get(1).intValue());

        CSVRecord record2 = records.get(1);
        assertEquals(2, record2.size());
        assertEquals("Name", record2.getHeaderName(0));
        assertEquals("Bob", record2.get(0));
        assertEquals("Age", record2.getHeaderName(1));
        assertEquals(30, record2.get(1).intValue());
    }

    /**
     * Test parsing a CSV file with empty lines.
     *
     * @throws IOException
     *             if the test fails
     */
    @Test
    public void testCSVWithEmptyLines() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader("a,b\n\nc,d\ne,f"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals(2, record1.size());
        assertEquals("a", record1.get(0));
        assertEquals("b", record1.get(1));

        CSVRecord record3 = records.get(2);
        assertEquals(2, record3.size());
        assertEquals("e", record3.get(0));
        assertEquals("f", record3.get(1));

        // record2 is empty
        assertEquals(0, records.get(1).size());
    }

    /**
     * Test parsing a CSV file with a comment.
     *
     * @throws IOException
     *             if the test fails
     */
    @Test
    public void testCSVWithComment() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader("a,b\n#This is a comment\nc,d"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());

        CSVRecord record = records.get(0);
        assertEquals(2, record.size());
        assertEquals("a", record.get(0));
        assertEquals("c", record.get(1));
        assertNull(record.getComment());
    }
}