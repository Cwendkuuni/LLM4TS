import org.apache.commons.csv.*;
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test cases for CSVParser class
 */
public class CSVParserTest {

    private CSVParser parser;

    @Before
    public void setUp() throws IOException {
        String csvData = "a,b\n" +
                "1,2\n" +
                "3,4\n" +
                "5,6";
        parser = new CSVParser(new StringReader(csvData), CSVFormat.RFC4180);
    }

    /**
     * Test parsing records from the input stream
     */
    @Test
    public void testParseRecords() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());

        CSVRecord record1 = records.get(0);
        Assert.assertArrayEquals(new String[]{"a", "1"}, record1.getValues());

        CSVRecord record2 = records.get(1);
        Assert.assertArrayEquals(new String[]{"b", "2"}, record2.getValues());

        CSVRecord record3 = records.get(2);
        Assert.assertArrayEquals(new String[]{"3", "4"}, record3.getValues());
    }

    /**
     * Test parsing empty line
     */
    @Test
    public void testParseEmptyLine() throws IOException {
        parser.nextRecord();
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        Assert.assertArrayEquals(new String[]{}, records.get(1).getValues());
    }

    /**
     * Test parsing a CSV file with custom format
     */
    @Test
    public void testParseCSVWithCustomFormat() throws IOException {
        String csvData = "Name,Age\n" +
                "John,25\n" +
                "Mike,30";

        parser = new CSVParser(new StringReader(csvData), new CSVFormat(",", null, new String[]{"Name", "Age"}));
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        Assert.assertArrayEquals(new String[]{"Name", "John"}, record1.getHeaderMap().keySet().toArray());
        Assert.assertArrayEquals(new String[]{"Name", "John", "Age", "25"}, record1.toArray());

        CSVRecord record2 = records.get(1);
        Assert.assertArrayEquals(new String[]{"Name", "Mike"}, record2.getHeaderMap().keySet().toArray());
        Assert.assertArrayEquals(new String[]{"Name", "Mike", "Age", "30"}, record2.toArray());
    }

    /**
     * Test parsing a CSV file with header
     */
    @Test
    public void testParseCSVWithHeader() throws IOException {
        String csvData = "Name,Age\n" +
                "John,25\n" +
                "Mike,30";

        parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        Map<String, Integer> headerMap = record1.getHeaderMap();
        Assert.assertEquals("Name", headerMap.get(0));
        Assert.assertEquals("Age", headerMap.get(1));
        Assert.assertArrayEquals(new String[]{"Name", "John"}, record1.getValues());

        CSVRecord record2 = records.get(1);
        Assert.assertArrayEquals(new String[]{"Name", "Mike"}, record2.getValues());
    }

    /**
     * Test parsing a CSV file with comment
     */
    @Test
    public void testParseCSVWithComment() throws IOException {
        String csvData = "Name,Age# This is a comment\n" +
                "John,25\n" +
                "Mike,30";

        parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        Assert.assertArrayEquals(new String[]{"Name", "John"}, record1.getValues());
        Assert.assertNull(record1.getComment());

        CSVRecord record2 = records.get(1);
        Assert.assertArrayEquals(new String[]{"Name", "Mike"}, record2.getValues());
        Assert.assertEquals("# This is a comment", record2.getComment());
    }

    /**
     * Test parsing a CSV file with skipped header
     */
    @Test
    public void testParseCSVWithSkippedHeader() throws IOException {
        String csvData = "Name,Age\n" +
                "John,25\n" +
                "Mike,30";

        parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT, true);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        Assert.assertArrayEquals(new String[]{"John", "25"}, record1.getValues());

        CSVRecord record2 = records.get(1);
        Assert.assertArrayEquals(new String[]{"Mike", "30"}, record2.getValues());

        // header map should be empty
        Assert.assertEquals(0, record1.getHeaderMap().size());
    }

    /**
     * Test parsing a CSV file with empty lines
     */
    @Test
    public void testParseCSVWithEmptyLines() throws IOException {
        String csvData = "Name,Age\n" +
                "John,25\n" +
                "" +
                "Mike,30";

        parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(3, records.size());

        CSVRecord record1 = records.get(0);
        Assert.assertArrayEquals(new String[]{"Name", "John"}, record1.getValues());

        CSVRecord record2 = records.get(1);
        Assert.assertArrayEquals(new String[]{}, record2.getValues());

        CSVRecord record3 = records.get(2);
        Assert.assertArrayEquals(new String[]{"Name", "Mike"}, record3.getValues());
    }

    /**
     * Test parsing a CSV file with unescaped comma
     */
    @Test
    public void testParseCSVWithUnescapedComma() throws IOException {
        String csvData = "Name,Age\n" +
                "John,1,25\n" +
                "Mike,1,30";

        parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        Assert.assertArrayEquals(new String[]{"Name", "John", "1", "25"}, record1.getValues());

        CSVRecord record2 = records.get(1);
        Assert.assertArrayEquals(new String[]{"Name", "Mike", "1", "30"}, record2.getValues());
    }

    /**
     * Test parsing a CSV file with escaped comma
     */
    @Test
    public void testParseCSVWithEscapedComma() throws IOException {
        String csvData = "Name,Age\n" +
                "John,1,\"25,26\"\n" +
                "Mike,1,\"30,31\"";

        parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        Assert.assertArrayEquals(new String[]{"Name", "John", "1", "25,26"}, record1.getValues());

        CSVRecord record2 = records.get(1);
        Assert.assertArrayEquals(new String[]{"Name", "Mike", "1", "30,31"}, record2.getValues());
    }

    /**
     * Test parsing a CSV file with quoted values
     */
    @Test
    public void testParseCSVWithQuotedValues() throws IOException {
        String csvData = "Name,Age\n" +
                "John,\"John Doe\",25\n" +
                "Mike,\"Mike Smith\",30";

        parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        Assert.assertArrayEquals(new String[]{"Name", "John Doe", "25"}, record1.getValues());

        CSVRecord record2 = records.get(1);
        Assert.assertArrayEquals(new String[]{"Name", "Mike Smith", "30"}, record2.getValues());
    }

    /**
     * Test parsing a CSV file with multiple types of quoted values
     */
    @Test
    public void testParseCSVWithDifferentQuotedValues() throws IOException {
        String csvData = "Name,Age\n" +
                "John,\"John Doe\",25\n" +
                "Mike,\"Mike Smith\",30\n" +
                "Paul,\"Paul Jones\",\"35,36\"";

        parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(3, records.size());

        CSVRecord record1 = records.get(0);
        Assert.assertArrayEquals(new String[]{"Name", "John Doe", "25"}, record1.getValues());

        CSVRecord record2 = records.get(1);
        Assert.assertArrayEquals(new String[]{"Name", "Mike Smith", "30"}, record2.getValues());

        CSVRecord record3 = records.get(2);
        Assert.assertArrayEquals(new String[]{"Name", "Paul Jones", "35,36"}, record3.getValues());
    }

    /**
     * Test parsing a CSV file with empty values
     */
    @Test
    public void testParseCSVWithEmptyValues() throws IOException {
        String csvData = "Name,Age\n" +
                "John,\"\",25\n" +
                "Mike,\" ,30\n" +
                "Paul,\"\",\"\"";

        parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(3, records.size());

        CSVRecord record1 = records.get(0);
        Assert.assertArrayEquals(new String[]{"Name", "", "25"}, record1.getValues());

        CSVRecord record2 = records.get(1);
        Assert.assertArrayEquals(new String[]{"Name", " ", "30"}, record2.getValues());

        CSVRecord record3 = records.get(2);
        Assert.assertArrayEquals(new String[]{"Name", "", ""}, record3.getValues());
    }

    /**
     * Test parsing a CSV file with incorrect format
     */
    @Test(expected = IOException.class)
    public void testParseCSVWithIncorrectFormat() throws IOException {
        String csvData = "Name,Age\n" +
                "John,25,\n" +
                "Mike,30";

        parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        parser.getRecords();
    }

    /**
     * Test parsing a CSV file with missing values
     */
    @Test(expected = IOException.class)
    public void testParseCSVWithMissingValues() throws IOException {
        String csvData = "Name,Age\n" +
                "John,\n" +
                "Mike,30";

        parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        parser.getRecords();
    }
}