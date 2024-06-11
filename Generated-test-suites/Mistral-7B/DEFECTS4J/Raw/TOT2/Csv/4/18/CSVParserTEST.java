import org.apache.commons.csv.*;
import org.junit.Assert.*;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVParserTest {

    @Test
    public void testParseSimpleCSVFile() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(new StringReader("Name,Age\nJohn,25\nMary,30"), format);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        Assert.assertEquals(2, records.get(0).size());
        Assert.assertEquals("Name", records.get(0).getHeaderMap().get("Name"));
        Assert.assertEquals("Age", records.get(0).getHeaderMap().get("Age"));
        Assert.assertEquals("John", records.get(0).get(0));
        Assert.assertEquals(25, records.get(0).get(1).intValue());
        Assert.assertEquals("Mary", records.get(1).get(0));
        Assert.assertEquals(30, records.get(1).get(1).intValue());
    }

    @Test
    public void testParseCSVFileWithHeader() throws IOException {
        CSVFormat format = new CSVFormat(new String[]{"Name", "Age", "City"}, '\t');
        CSVParser parser = CSVParser.parse(new StringReader("Name\tAge\tCity\nJohn\t25\tNew York\nMary\t30\tLos Angeles"), format);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        CSVRecord record = records.get(0);
        Assert.assertEquals("Name", record.getHeaderMap().get("Name"));
        Assert.assertEquals("Age", record.getHeaderMap().get("Age"));
        Assert.assertEquals("City", record.getHeaderMap().get("City"));
        Assert.assertEquals("John", record.get(0));
        Assert.assertEquals(25, record.get(1).intValue());
        Assert.assertEquals("New York", record.get(2));
        record = records.get(1);
        Assert.assertEquals("Mary", record.get(0));
        Assert.assertEquals(30, record.get(1).intValue());
        Assert.assertEquals("Los Angeles", record.get(2));
    }

    @Test(expected = IOException.class)
    public void testParseInvalidCSVFile() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = null;
        try {
            parser = CSVParser.parse(new StringReader("Name,Age\nJohn,25\nMary,30\nName,30"), format);
        } catch (IllegalArgumentException e) {
            throw new IOException("Expected IllegalArgumentException but got: " + e.getMessage());
        }
        Assert.assertNotNull(parser);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        Assert.assertEquals(2, records.get(0).size());
        Assert.assertEquals("Name", records.get(0).getHeaderMap().get("Name"));
        Assert.assertEquals("Age", records.get(0).getHeaderMap().get("Age"));
        Assert.assertEquals("John", records.get(0).get(0));
        Assert.assertEquals(25, records.get(0).get(1).intValue());
        Assert.assertEquals("Mary", records.get(1).get(0));
        Assert.assertEquals(30, records.get(1).get(1).intValue());

        // Should throw an exception
        parser = CSVParser.parse(new StringReader("Name,Age\nJohn,25\nName,30"), format);
    }

    @Test
    public void testParseEmptyCSVFile() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(new StringReader(""), format);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertTrue(records.isEmpty());
    }

    @Test
    public void testParseIgnoreComments() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker("#");
        CSVParser parser = CSVParser.parse(new StringReader("Name,Age#This is a comment\nJohn,25\nMary,30"), format);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        Assert.assertEquals(2, records.get(0).size());
        Assert.assertEquals("Name", records.get(0).getHeaderMap().get("Name"));
        Assert.assertEquals("Age", records.get(0).getHeaderMap().get("Age"));
        Assert.assertEquals("John", records.get(0).get(0));
        Assert.assertEquals(25, records.get(0).get(1).intValue());
        Assert.assertEquals("Mary", records.get(1).get(0));
        Assert.assertEquals(30, records.get(1).get(1).intValue());
    }

    @Test
    public void testParseCSVFileWithQuotedValues() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(new StringReader("Name,\"John Doe\"\nAge,25"), format);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        Assert.assertEquals(2, record.size());
        Assert.assertEquals("Name", record.getHeaderMap().get("Name"));
        Assert.assertEquals("Name", record.get(0));
        Assert.assertEquals("John Doe", record.get(1));
    }

    @Test
    public void testParseCSVFileWithQuotedValuesAndEmptyCells() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(new StringReader("Name,\"John Doe\",\"\",\"\",Age\nName,\"Mary\",\"\",\"Smith\",30"), format);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        Assert.assertEquals(5, record.size());
        Assert.assertEquals("Name", record.getHeaderMap().get("Name"));
        Assert.assertEquals("Name", record.get(0));
        Assert.assertEquals("John Doe", record.get(1));
        Assert.assertEquals("", record.get(2));
        Assert.assertEquals("", record.get(3));
        Assert.assertEquals("Age", record.getHeaderMap().get("Age"));
        Assert.assertEquals(30, record.get(4).intValue());
    }

    @Test
    public void testParseCSVFileWithEmptyValues() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(new StringReader("Name,,\"\",Age\nJohn,\"\",25"), format);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        Assert.assertEquals(3, record.size());
        Assert.assertEquals("Name", record.getHeaderMap().get("Name"));
        Assert.assertEquals("", record.get(0));
        Assert.assertEquals("", record.get(1));
        Assert.assertEquals("Age", record.getHeaderMap().get("Age"));
        Assert.assertEquals(25, record.get(2).intValue());
    }

    @Test
    public void testParseCSVFileWithQuotedValuesAndEmptyCellsAndComments() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker("#");
        CSVParser parser = CSVParser.parse(new StringReader("Name,\"John Doe\",\"\",\"\",Age#comment\nName,\"Mary\",\"\",\"Smith\",30"), format);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        Assert.assertEquals(5, record.size());
        Assert.assertEquals("Name", record.getHeaderMap().get("Name"));
        Assert.assertEquals("Name", record.get(0));
        Assert.assertEquals("John Doe", record.get(1));
        Assert.assertEquals("", record.get(2));
        Assert.assertEquals("", record.get(3));
        Assert.assertEquals("Age", record.getHeaderMap().get("Age"));
        Assert.assertEquals(30, record.get(4).intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseCSVFileWithInvalidFormat() throws IOException {
        CSVFormat format = new CSVFormat(new String[]{"Name", "Invalid"});
        CSVParser parser = null;
        try {
            parser = CSVParser.parse(new StringReader("Name,Age\nJohn,25"), format);
        } catch (IOException e) {
            throw new IllegalArgumentException("Expected IllegalArgumentException but got: " + e.getMessage());
        }
        Assert.assertNull(parser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseCSVFileWithInvalidFormatWithEmptyCells() throws IOException {
        CSVFormat format = new CSVFormat(new String[]{"Name", "Invalid", ""});
        CSVParser parser = null;
        try {
            parser = CSVParser.parse(new StringReader("Name,\"John Doe\",\"\",\"\",Age\nName,\"Mary\",\"\",\"Smith\",30"), format);
        } catch (IOException e) {
            throw new IllegalArgumentException("Expected IllegalArgumentException but got: " + e.getMessage());
        }
        Assert.assertNull(parser);
    }
}