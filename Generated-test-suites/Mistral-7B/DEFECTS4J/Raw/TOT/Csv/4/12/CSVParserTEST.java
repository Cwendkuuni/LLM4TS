package org.apache.commons.csv.test;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVFormat.EXCEL;
import static org.apache.commons.csv.CSVParser.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringReader;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Test class for {@link CSVParser}.
 */
public class CSVParserTest {

    /**
     * Test parsing a simple CSV file with no header.
     */
    @org.junit.Test
    public void testParseSimpleCSV() throws Exception {
        String input = "a,b\nc,d";
        CSVParser parser = parse(new StringReader(input), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertNull(parser.getHeaderMap());
        assertEquals(1, records.size());

        CSVRecord record = records.get(0);
        assertNull(record.getHeaderMap());
        assertEquals(2, record.size());
        assertEquals("a", record.get(0));
        assertEquals("b", record.get(1));
        assertEquals("c", record.get(2));
        assertEquals("d", record.get(3));
    }

    /**
     * Test parsing a CSV file with a header.
     */
    @org.junit.Test
    public void testParseCSVWithHeader() throws Exception {
        String input = "Name,Age,Gender\nJohn Doe,35,M\nJane Doe,28,F";
        CSVParser parser = parse(new StringReader(input), EXCEL);
        List<CSVRecord> records = parser.getRecords();
        Map<String, Integer> headerMap = parser.getHeaderMap();

        assertEquals(1, records.size());
        assertEquals(3, records.get(0).size());
        assertEquals("Name", headerMap.get("Name"));
        assertEquals("Age", headerMap.get("Age"));
        assertEquals("Gender", headerMap.get("Gender"));

        CSVRecord record = records.get(0);
        assertEquals("Name", record.getHeaderMap().get("Name"));
        assertEquals("Age", record.getHeaderMap().get("Age"));
        assertEquals("Gender", record.getHeaderMap().get("Gender"));

        assertEquals("John Doe", record.get("Name"));
        assertEquals("35", record.get("Age"));
        assertEquals("M", record.get("Gender"));
        assertEquals("Jane Doe", record.get("Name"));
        assertEquals("28", record.get("Age"));
        assertEquals("F", record.get("Gender"));
    }

    /**
     * Test parsing a CSV file with a comment.
     */
    @org.junit.Test
    public void testParseCSVWithComment() throws Exception {
        String input = "a,b\nc,d# This is a comment\ne,f";
        CSVParser parser = parse(new StringReader(input), DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(1, records.size());
        assertEquals(3, records.get(0).size());
        assertEquals("c", records.get(0).get(1));

        String comment = parser.getComment();
        assertEquals("This is a comment", comment);
    }

    /**
     * Test parsing a CSV file with an empty record.
     */
    @org.junit.Test
    public void testParseCSVWithEmptyRecord() throws Exception {
        String input = "a,b\nc,d\na";
        CSVParser parser = parse(new StringReader(input), DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        assertEquals(2, records.get(0).size());
        assertEquals(null, records.get(1).get(0));
    }

    /**
     * Test parsing a CSV file with an empty line.
     */
    @org.junit.Test
    public void testParseCSVWithEmptyLine() throws Exception {
        String input = "a,b\nc,d\n";
        CSVParser parser = parse(new StringReader(input), DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        assertEquals(2, records.get(0).size());
    }

    /**
     * Test parsing a CSV file with an invalid record.
     */
    @org.junit.Test(expected = IOException.class)
    public void testParseInvalidCSV() throws Exception {
        String input = "a,b\nc,d\ne";
        parse(new StringReader(input), DEFAULT).getRecords();
    }

    /**
     * Test parsing a CSV file with an invalid character set.
     */
    @org.junit.Test(expected = IOException.class)
    public void testParseInvalidCharset() throws Exception {
        CSVParser parser = parse(new StringReader("a,b"), DEFAULT);
        try {
            parser.getRecords();
        } finally {
            parser.close();
        }

        parser = parse(new InputStreamReader(System.in), DEFAULT);
        try {
            parser.getRecords();
        } finally {
            parser.close();
        }
    }
}