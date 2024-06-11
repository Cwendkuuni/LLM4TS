import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVParserTest {

    private CSVParser parser;
    private String csvData = "a,b\nc,d\ne,f";

    @Test
    public void testParseSimpleCSV() throws IOException {
        parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        Assert.assertArrayEquals(new String[]{"a", "b"}, record1.values());
        CSVRecord record2 = records.get(1);
        Assert.assertArrayEquals(new String[]{"c", "d"}, record2.values());
    }

    @Test
    public void testParseCSVWithHeader() throws IOException {
        String csvDataWithHeader = "Name,Age,City\nJohn,25,New York\nMike,30,Chicago";
        parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csvDataWithHeader.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8), CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = parser.getRecords();

        Assert.assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        Assert.assertEquals("Name", record1.getHeaderMap().get("Name"));
        Assert.assertArrayEquals(new String[]{"John"}, record1.values());
        CSVRecord record2 = records.get(1);
        Assert.assertEquals("Name", record2.getHeaderMap().get("Name"));
        Assert.assertArrayEquals(new String[]{"Mike"}, record2.values());
        Assert.assertEquals("Age", record1.getHeaderMap().get("Age"));
        Assert.assertEquals("City", record1.getHeaderMap().get("City"));
        Assert.assertEquals(25, record1.get("Age").getValue().intValue());
        Assert.assertEquals("New York", record1.get("City").getValue().toString());
        Assert.assertEquals(30, record2.get("Age").getValue().intValue());
        Assert.assertEquals("Chicago", record2.get("City").getValue().toString());
    }

    @Test
    public void testParseEmptyCSV() throws IOException {
        String csvDataEmpty = "";
        parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csvDataEmpty.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertEquals(0, records.size());
    }

    @Test
    public void testParseCSVWithQuotes() throws IOException {
        String csvDataWithQuotes = "Name,\"John Doe\",City\nMike,\"Apple, Banana\"";
        parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csvDataWithQuotes.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        Assert.assertArrayEquals(new String[]{"Name", "John Doe"}, record1.values());
        CSVRecord record2 = records.get(1);
        Assert.assertArrayEquals(new String[]{"Mike", "Apple, Banana"}, record2.values());
    }

    @Test(expected = IOException.class)
    public void testParseInvalidCSV() throws IOException {
        String csvDataInvalid = "a,b\nc,d\ne,f\n"; // missing comma in second record
        parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csvDataInvalid.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8), CSVFormat.DEFAULT);
        parser.getRecords();
    }

    @Test
    public void testParseEmptyRecord() throws IOException {
        String csvDataEmptyRecord = "Name,\nAge,City\nJohn,25,New York";
        parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csvDataEmptyRecord.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        Assert.assertArrayEquals(new String[]{"Name"}, record1.values());
        CSVRecord record2 = records.get(1);
        Assert.assertArrayEquals(new String[]{"John", "25", "New York"}, record2.values());
    }

    @Test
    public void testParseCSVWithMissingHeader() throws IOException {
        String csvDataWithoutHeader = "a,b\nc,d\ne,f";
        parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csvDataWithoutHeader.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        Assert.assertArrayEquals(new String[]{"a"}, record1.values());
        CSVRecord record2 = records.get(1);
        Assert.assertArrayEquals(new String[]{"c"}, record2.values());
    }

    @Test
    public void testParseCSVWithCustomFormat() throws IOException {
        String csvDataWithCustomFormat = "Name,Age\nJohn,25\nMike,30";
        CSVFormat customFormat = CSVFormat.DEFAULT.withHeader().withColumnNames("Name", "Age");
        parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csvDataWithCustomFormat.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8), customFormat);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        Assert.assertEquals("Name", record1.getHeaderMap().get("Name"));
        Assert.assertArrayEquals(new String[]{"John"}, record1.values());
        CSVRecord record2 = records.get(1);
        Assert.assertEquals("Name", record2.getHeaderMap().get("Name"));
        Assert.assertArrayEquals(new String[]{"Mike"}, record2.values());
        Assert.assertEquals("Age", record1.getHeaderMap().get("Age"));
        Assert.assertEquals(25, record1.get("Age").getValue().intValue());
        Assert.assertEquals(30, record2.get("Age").getValue().intValue());
    }

    @After
    public void tearDown() throws IOException {
        if (parser != null) {
            parser.close();
        }
    }
}