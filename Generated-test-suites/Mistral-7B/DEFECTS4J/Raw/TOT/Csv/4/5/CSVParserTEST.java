package org.apache.commons.csv.test;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVFormat.EXCEL;
import static org.apache.commons.csv.CSVParser.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Test class for {@link CSVParser}
 */
public class CSVParserTest {

    /**
     * Test parsing a simple CSV file with default format.
     *
     * @throws IOException
     *             on parse error or input read-failure
     */
    @org.junit.Test
    public void testParseSimpleCSV() throws IOException {
        final String csvData = "a,b\nc,d";
        final CSVParser parser = parse(new StringReader(csvData), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertSame(2, records.size());
        CSVRecord record = records.get(0);
        assertEquals("a", record.get(0));
        assertEquals("b", record.get(1));

        record = records.get(1);
        assertEquals("c", record.get(0));
        assertEquals("d", record.get(1));
        parser.close();
    }

    /**
     * Test parsing a CSV file with Excel format.
     *
     * @throws IOException
     *             on parse error or input read-failure
     */
    @org.junit.Test
    public void testParseCSVWithExcelFormat() throws IOException {
        final String csvData = "a;b\nc;d";
        final CSVParser parser = parse(new StringReader(csvData), EXCEL);
        List<CSVRecord> records = parser.getRecords();
        assertSame(2, records.size());
        CSVRecord record = records.get(0);
        assertEquals("a", record.get(0));
        assertEquals("b", record.get(1));

        record = records.get(1);
        assertEquals("c", record.get(0));
        assertEquals("d", record.get(1));
        parser.close();
    }

    /**
     * Test parsing an empty CSV file.
     *
     * @throws IOException
     *             on parse error or input read-failure
     */
    @org.junit.Test
    public void testParseEmptyCSV() throws IOException {
        final String csvData = "";
        final CSVParser parser = parse(new StringReader(csvData), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertSame(0, records.size());
        parser.close();
    }

    /**
     * Test parsing a CSV file with invalid format.
     *
     * @throws IOException
     *             on parse error or input read-failure
     */
    @org.junit.Test(expected = IOException.class)
    public void testParseInvalidCSV() throws IOException {
        final String csvData = "a;b\nc:d";
        final CSVParser parser = parse(new StringReader(csvData), EXCEL);
        parser.getRecords();
    }

    /**
     * Test parsing a CSV file with comment.
     *
     * @throws IOException
     *             on parse error or input read-failure
     */
    @org.junit.Test
    public void testParseCSVWithComment() throws IOException {
        final String csvData = "a,b\n# This is a comment\nc,d";
        final CSVParser parser = parse(new StringReader(csvData), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertSame(1, records.size());
        CSVRecord record = records.get(0);
        assertEquals("a", record.get(0));
        assertEquals("c", record.get(1));
        assertNull(record.getComment());
        parser.close();
    }

    /**
     * Test parsing a CSV file with comment on every line.
     *
     * @throws IOException
     *             on parse error or input read-failure
     */
    @org.junit.Test
    public void testParseCSVWithCommentOnEveryLine() throws IOException {
        final String csvData = "a,b\n# This is a comment\nc,d\n# Another comment\ne,f";
        final CSVParser parser = parse(new StringReader(csvData), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertSame(2, records.size());

        CSVRecord record = records.get(0);
        assertEquals("a", record.get(0));
        assertEquals("c", record.get(1));
        assertEquals("This is a comment", record.getComment());

        record = records.get(1);
        assertEquals("e", record.get(0));
        assertEquals("f", record.get(1));
        assertNull(record.getComment());
        parser.close();
    }

}