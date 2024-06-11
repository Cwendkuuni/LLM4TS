import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;

public class CSVParserTest {

    @Test
    public void testParseSimpleCSV() throws IOException {
        String csvData = "name,age\nJohn,30\nMary,25";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
            Assert.assertEquals(2, record.size());
            Assert.assertEquals("name", record.getHeaderMap().get("name").toString());
            Assert.assertEquals("John", record.get("name"));
            Assert.assertEquals("age", record.getHeaderMap().get("age").toString());
            Assert.assertEquals("30", record.get("age"));
        }
    }

    @Test
    public void testParseCSVWithEmptyValuesAndNullValues() throws IOException {
        String csvData = "name,age\nJohn,30,\nMary,25,null";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
            Assert.assertEquals(3, record.size());
            Assert.assertEquals("name", record.getHeaderMap().get("name").toString());
            Assert.assertEquals("John", record.get("name"));
            Assert.assertEquals("age", record.getHeaderMap().get("age").toString());
            Assert.assertNull(record.get("age"));
        }
    }

    @Test
    public void testParseCSVWithInvalidFormat() throws IOException {
        String csvData = "name,age\nJohn,30\nMary,25a";
        try {
            CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
            parser.getRecords();
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertEquals("Invalid record format (line 2): expected ',' but was 'a'", e.getMessage());
        }
    }

    @Test
    public void testParseCSVWithComments() throws IOException {
        String csvData = "name,age\nJohn,30#comment1\nMary,25#comment2";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record = records.get(0);
        Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
        Assert.assertEquals(2, record.size());
        Assert.assertEquals("name", record.getHeaderMap().get("name").toString());
        Assert.assertEquals("John", record.get("name"));
        Assert.assertEquals("age", record.getHeaderMap().get("age").toString());
        Assert.assertEquals("30", record.get("age"));
        Assert.assertEquals("comment1", record.getComment());

        record = records.get(1);
        Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
        Assert.assertEquals(2, record.size());
        Assert.assertEquals("name", record.getHeaderMap().get("name").toString());
        Assert.assertEquals("Mary", record.get("name"));
        Assert.assertEquals("age", record.getHeaderMap().get("age").toString());
        Assert.assertNull(record.get("age"));
        Assert.assertEquals("comment2", record.getComment());
    }

    @Test
    public void testParseCSVWithQuotedValues() throws IOException {
        String csvData = "name,age\nJohn,\"John Doe\",30\nMary,\"Mary Smith\",25";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record = records.get(0);
        Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
        Assert.assertEquals(3, record.size());
        Assert.assertEquals("name", record.getHeaderMap().get("name").toString());
        Assert.assertEquals("John Doe", record.get("name"));
        Assert.assertEquals("age", record.getHeaderMap().get("age").toString());
        Assert.assertEquals("30", record.get("age"));

        record = records.get(1);
        Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
        Assert.assertEquals(3, record.size());
        Assert.assertEquals("name", record.getHeaderMap().get("name").toString());
        Assert.assertEquals("Mary Smith", record.get("name"));
        Assert.assertEquals("age", record.getHeaderMap().get("age").toString());
        Assert.assertNull(record.get("age"));
    }

    @Test
    public void testParseCSVWithMissingValues() throws IOException {
        String csvData = "name,age\nJohn,30\nMary,";
        CSVParser parser = new StringReader(csvData, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record = records.get(0);
        Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
        Assert.assertEquals(2, record.size());
        Assert.assertEquals("name", record.getHeaderMap().get("name").toString());
        Assert.assertEquals("John", record.get("name"));
        Assert.assertNull(record.get("age"));

        record = records.get(1);
        Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
        Assert.assertEquals(1, record.size());
        Assert.assertEquals("name", record.getHeaderMap().get("name").toString());
        Assert.assertNull(record.get("name"));
        Assert.assertNull(record.get("age"));
    }

    @Test
    public void testParseCSVWithCustomFormat() throws IOException {
        String csvData = "name,age\nJohn,30\nMary,25";
        CSVFormat customFormat = new CSVFormat(';', '\'\'', null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null);
        CSVParser parser = new CSVParser(new StringReader(csvData), customFormat);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record = records.get(0);
        Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
        Assert.assertEquals(2, record.size());
        Assert.assertEquals("name", record.getHeaderMap().get("name").toString());
        Assert.assertEquals("John", record.get("name"));
        Assert.assertEquals("age", record.getHeaderMap().get("age").toString());
        Assert.assertEquals("30", record.get("age"));

        record = records.get(1);
        Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
        Assert.assertEquals(2, record.size());
        Assert.assertEquals("name", record.getHeaderMap().get("name").toString());
        Assert.assertEquals("Mary", record.get("name"));
        Assert.assertEquals("age", record.getHeaderMap().get("age").toString());
        Assert.assertEquals("25", record.get("age"));
    }

    @Test
    public void testParseCSVWithCustomComment() throws IOException {
        String csvData = "name,age\nJohn,30#comment1\nMary,25#comment2";
        CSVFormat customFormat = new CSVFormat(';', '\'\'', null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null);
        customFormat.setCommentPrefix("#");
        CSVParser parser = new CSVParser(new StringReader(csvData), customFormat);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record = records.get(0);
        Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
        Assert.assertEquals(3, record.size());
        Assert.assertEquals("name", record.getHeaderMap().get("name").toString());
        Assert.assertEquals("John", record.get("name"));
        Assert.assertEquals("age", record.getHeaderMap().get("age").toString());
        Assert.assertEquals("30", record.get("age"));
        Assert.assertEquals("comment1", record.getComment());

        record = records.get(1);
        Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
        Assert.assertEquals(3, record.size());
        Assert.assertEquals("name", record.getHeaderMap().get("name").toString());
        Assert.assertEquals("Mary", record.get("name"));
        Assert.assertEquals("age", record.getHeaderMap().get("age").toString());
        Assert.assertNull(record.get("age"));
        Assert.assertEquals("comment2", record.getComment());
    }

    @Test
    public void testParseCSVWithCustomNullString() throws IOException {
        String csvData = "name,age\nJohn,30,,\nMary,25,NULL";
        CSVFormat customFormat = new CSVFormat(';', '\'\'', null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null);
        customFormat.setNullValue("NULL");
        CSVParser parser = new CSVParser(new StringReader(csvData), customFormat);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record = records.get(0);
        Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
        Assert.assertEquals(3, record.size());
        Assert.assertEquals("name", record.getHeaderMap().get("name").toString());
        Assert.assertEquals("John", record.get("name"));
        Assert.assertEquals("age", record.getHeaderMap().get("age").toString());
        Assert.assertEquals(null, record.get("age"));

        record = records.get(1);
        Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
        Assert.assertEquals(3, record.size());
        Assert.assertEquals("name", record.getHeaderMap().get("name").toString());
        Assert.assertEquals("Mary", record.get("name"));
        Assert.assertEquals("age", record.getHeaderMap().get("age").toString());
        Assert.assertEquals(null, record.get("age"));
    }

    @Test
    public void testParseEmptyCSV() throws IOException {
        String csvData = "";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(Collections.emptyList(), records);
    }

    @Test
    public void testParseCSVWithCustomFormatAndEmptyValues() throws IOException {
        String csvData = "name,age\nJohn,30,\nMary,25,";
        CSVFormat customFormat = new CSVFormat(';', '\'\'', null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null);
        customFormat.setNullValue("");
        CSVParser parser = new CSVParser(new StringReader(csvData), customFormat);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record = records.get(0);
        Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
        Assert.assertEquals(3, record.size());
        Assert.assertEquals("name", record.getHeaderMap().get("name").toString());
        Assert.assertEquals("John", record.get("name"));
        Assert.assertEquals("age", record.getHeaderMap().get("age").toString());
        Assert.assertEquals("", record.get("age"));

        record = records.get(1);
        Assert.assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(new String[0]));
        Assert.assertEquals(3, record.size());
        Assert.assertEquals("name", record.getHeaderMap().get("name").toString());
        Assert.assertEquals("Mary", record.get("name"));
        Assert.assertEquals("age", record.getHeaderMap().get("age").toString());
        Assert.assertEquals("", record.get("age"));
    }
}