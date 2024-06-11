import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CSVParserTest {

    @Test
    public void testParseSimpleCSV() throws IOException {
        String csvData = "name,age\nJohn,30\nMary,25";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Assert.assertEquals(2, record.size());
            Assert.assertEquals("name", record.getHeaderMap().get("name"));
            Assert.assertEquals("age", record.getHeaderMap().get("age"));
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals("30", record.get(1));
            Assert.assertNull(record.get(2));
        }
    }

    @Test
    public void testParseEmptyCSV() throws IOException {
        String csvData = "";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(0, records.size());
    }

    @Test
    public void testParseCSVWithInvalidCharacter() throws IOException {
        String csvData = "name,age\nJohn,30\nMary,25%";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        try {
            List<CSVRecord> records = parser.getRecords();
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().contains("invalid character"));
        }
    }

    @Test
    public void testParseCSVWithMissingHeader() throws IOException {
        String csvData = "name,age\nJohn,30\nMary,25";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT.withHeader(false));

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        Assert.assertEquals(0, records.get(0).size());
        Assert.assertEquals(0, records.get(1).size());
    }

    @Test
    public void testParseCSVWithQuotedValues() throws IOException {
        String csvData = "name,\"John, Doe\"\nage,25";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(1, records.size());
        Assert.assertEquals(2, records.get(0).size());
        Assert.assertEquals("name", records.get(0).getHeaderMap().get("name"));
        Assert.assertEquals("age", records.get(0).getHeaderMap().get("age"));
        Assert.assertEquals("John, Doe", records.get(0).get(0));
        Assert.assertEquals("25", records.get(0).get(1));
    }

    @Test
    public void testParseCSVWithExtraSpaces() throws IOException {
        String csvData = "name,age\nJohn      ,30\nMary     ,25";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Assert.assertEquals(2, record.size());
            Assert.assertEquals("name", record.getHeaderMap().get("name"));
            Assert.assertEquals("age", record.getHeaderMap().get("age"));
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals("30", record.get(1));
        }
    }

    @Test
    public void testParseCSVWithMissingValues() throws IOException {
        String csvData = "name,age\nJohn,30\nMary,";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Assert.assertEquals(2, record.size());
            Assert.assertEquals("name", record.getHeaderMap().get("name"));
            Assert.assertEquals("age", record.getHeaderMap().get("age"));
            Assert.assertEquals("John", record.get(0));
            Assert.assertNull(record.get(1));
        }
    }

    @Test
    public void testParseCSVWithMultipleRecordsPerLine() throws IOException {
        String csvData = "name,age name2,age2\nJohn,30 Mary,25";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(1, records.size());
        Assert.assertEquals(4, records.get(0).size());
        Assert.assertEquals("name", records.get(0).getHeaderMap().get("name"));
        Assert.assertEquals("age", records.get(0).getHeaderMap().get("age"));
        Assert.assertEquals("name2", records.get(0).getHeaderMap().get("name2"));
        Assert.assertEquals("age2", records.get(0).getHeaderMap().get("age2"));
        Assert.assertEquals("John", records.get(0).get(0));
        Assert.assertEquals("30", records.get(0).get(1));
        Assert.assertEquals("Mary", records.get(0).get(2));
        Assert.assertEquals("25", records.get(0).get(3));
    }

    @Test
    public void testParseCSVWithEmptyLines() throws IOException {
        String csvData = "name,age\nJohn,30\n\nMary,25";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Assert.assertEquals(2, record.size());
            Assert.assertEquals("name", record.getHeaderMap().get("name"));
            Assert.assertEquals("age", record.getHeaderMap().get("age"));
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals("30", record.get(1));
        }
    }

    @Test
    public void testParseCSVWithMixedRecordLengths() throws IOException {
        String csvData = "name,age\nJohn,30\nMary,25,color\nBob,";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(3, records.size());

        for (CSVRecord record : records) {
            Assert.assertEquals(2, record.size());
            if (record.get(0).equals("Mary")) {
                Assert.assertEquals(3, record.size());
                Assert.assertEquals("color", record.get(2));
            }
        }
    }

    @Test
    public void testParseCSVWithInvalidFormat() throws IOException {
        String csvData = "name,age\nJohn,30\nMary,25color";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        try {
            List<CSVRecord> records = parser.getRecords();
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().contains("number of columns does not match"));
        }
    }

    @Test
    public void testParseCSVWithNullValues() throws IOException {
        String csvData = "name,age\nJohn,30\nMary,null";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Assert.assertEquals(2, record.size());
            Assert.assertEquals("name", record.getHeaderMap().get("name"));
            Assert.assertEquals("age", record.getHeaderMap().get("age"));
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals(null, record.get(1));
        }
    }

    @Test
    public void testParseCSVWithComplexValues() throws IOException {
        String csvData = "name,age\nJohn,\"John Doe\"\nMary,25.5";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Assert.assertEquals(2, record.size());
            Assert.assertEquals("name", record.getHeaderMap().get("name"));
            Assert.assertEquals("age", record.getHeaderMap().get("age"));
            Assert.assertEquals("John Doe", record.get(0));
            Assert.assertEquals("25.5", record.get(1));
        }
    }

    @Test
    public void testParseCSVWithCustomHeader() throws IOException {
        String csvData = "column1,column2\nname,age\nJohn,30\nMary,25";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT.withHeader(new String[]{"column1", "column2"}));

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Assert.assertEquals(2, record.size());
            Assert.assertEquals("column1", record.getHeaderMap().get("column1"));
            Assert.assertEquals("column2", record.getHeaderMap().get("column2"));
            Assert.assertEquals("name", record.getHeaderMap().get("name"));
            Assert.assertEquals("age", record.getHeaderMap().get("age"));
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals("30", record.get(1));
        }
    }

    @Test
    public void testParseCSVWithQuotedEmptyValues() throws IOException {
        String csvData = "name,age\nJohn,\"\nMary,25";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Assert.assertEquals(2, record.size());
            Assert.assertEquals("name", record.getHeaderMap().get("name"));
            Assert.assertEquals("age", record.getHeaderMap().get("age"));
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals(Collections.emptyList(), record.getValues());
        }
    }

    @Test
    public void testParseCSVWithEmptyCSV() throws IOException {
        String csvData = "";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(0, records.size());
    }
}