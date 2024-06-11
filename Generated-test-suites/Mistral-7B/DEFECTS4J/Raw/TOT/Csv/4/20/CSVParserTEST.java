import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

public class CSVParserTest {

    @Test
    public void testParseSimpleCSV() throws Exception {
        String csvContent = "Name,Age,City\nJohn,25,New York\nMike,31,Los Angeles\n";

        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, record.headerMap.keySet().toArray(new String[0]));
            Assert.assertEquals("John", record.get("Name"));
            Assert.assertEquals(25, record.getInteger("Age"));
            Assert.assertEquals("New York", record.get("City"));
        }

        parser.close();
    }

    @Test(expected = IOException.class)
    public void testParseInvalidCSV() throws Exception {
        String csvContent = "Name,Age,City\nJohn,25,New York\nMike,31,Los Angeles\nName,City";

        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        parser.close();

        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, record.headerMap.keySet().toArray(new String[0]));
            Assert.assertEquals("John", record.get("Name"));
            Assert.assertEquals(25, record.getInteger("Age"));
            Assert.assertEquals("New York", record.get("City"));
        }

        String invalidContent = record.getRecordValue(1).get(1); // Access the second value of the second record
        Assert.fail("Should have thrown an IOException: " + invalidContent);
    }

    @Test
    public void testParseEmptyCSV() throws Exception {
        String csvContent = "";

        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertEquals(0, records.size());

        parser.close();
    }

    @Test
    public void testParseCSVWithNullValue() throws Exception {
        String csvContent = "Name,Age,City\nJohn,25,New York\nMike,31,null\n";

        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, record.headerMap.keySet().toArray(new String[0]));
            Assert.assertEquals("John", record.get("Name"));
            Assert.assertEquals(25, record.getInteger("Age"));
            Assert.assertNull(record.get("City"));
        }

        parser.close();
    }

    @Test
    public void testParseCSVWithCustomFormat() throws Exception {
        String csvContent = "Name,Age,City\nJohn,25,New York\nMike,31,Los Angeles\n";

        CSVFormat customFormat = new CSVFormat();
        customFormat.setHeaderNames(Arrays.asList("Name1", "Age1", "City1"));

        CSVParser parser = CSVParser.parse(new StringReader(csvContent), customFormat);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Name1", "Age1", "City1"}, record.headerMap.keySet().toArray(new String[0]));
            Assert.assertEquals("John", record.get("Name1"));
            Assert.assertEquals(25, record.getInteger("Age1"));
            Assert.assertEquals("New York", record.get("City1"));
        }

        parser.close();
    }
}