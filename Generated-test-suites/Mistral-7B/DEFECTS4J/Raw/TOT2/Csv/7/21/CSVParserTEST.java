import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CSVParserTest {

    @Test
    public void testParseSimpleCSVFile() throws IOException {
        CSVFormat format = new CSVFormat(';', '\'\'');
        String csvContent = "Name;Age;City\nJohn;28;New York\nMike;30;Chicago";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, record.getHeaderMap().keySet().toArray());
            Assert.assertEquals(3, record.size());
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals("28", record.get(1));
            Assert.assertEquals("New York", record.get(2));
        }
        parser.close();
    }

    @Test
    public void testParseCSVFileWithEmptyLines() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        String csvContent = "Name,Age,City\nJohn,28,New York\n\nMike,30,Chicago";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, record.getHeaderMap().keySet().toArray());
            Assert.assertEquals(3, record.size());
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals("28", record.get(1));
            Assert.assertEquals("New York", record.get(2));
            Assert.assertEquals("Mike", record.get(records.get(1).size() - 1));
            Assert.assertEquals("30", record.get(records.get(1).size() - 2));
            Assert.assertEquals("Chicago", record.get(records.get(1).size() - 3));
        }
        parser.close();
    }

    @Test(expected = IOException.class)
    public void testParseInvalidCSVFile() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        String csvContent = "Name,Age,City\nJohn,28,New York\nMike,30,Chicago\nName,30,City";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        parser.close();

        // This call should throw an IOException
        parser = CSVParser.parse(new StringReader(csvContent), format);
    }

    @Test(expected = NoSuchElementException.class)
    public void testParseEmptyCSVFile() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        String csvContent = "";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(0, records.size());
        parser.close();

        // This call should throw a NoSuchElementException
        CSVRecord record = parser.getRecords().get(0);
    }

    @Test
    public void testParseCSVFileWithQuotes() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        String csvContent = "Name,\"O'Reilly\",Age,City\nJohn,\"O'Malley\",28,New York\nMike,\"van der Merwe\",30,Chicago";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, record.getHeaderMap().keySet().toArray());
            Assert.assertEquals(4, record.size());
            Assert.assertEquals("O'Reilly", record.get(0));
            Assert.assertEquals("O'Malley", record.get(1));
            Assert.assertEquals("28", record.get(2));
            Assert.assertEquals("New York", record.get(3));
            Assert.assertEquals("van der Merwe", record.get(records.get(1).size() - 1));
            Assert.assertEquals("30", record.get(records.get(1).size() - 2));
            Assert.assertEquals("Chicago", record.get(records.get(1).size() - 3));
        }
        parser.close();
    }

    @Test
    public void testParseCSVFileWithQuotesAndEscapedQuotes() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        String csvContent = "Name,\"O'Reilly\",\"O' \"Riley\",Age,City\nJohn,\"O'Malley\",\"O' \"Malley\",28,New York\nMike,\"van der Merwe\",\"van der  Merwe\",30,Chicago";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, record.getHeaderMap().keySet().toArray());
            Assert.assertEquals(4, record.size());
            Assert.assertEquals("O'Reilly", record.get(0));
            Assert.assertEquals("O' \"Riley\"", record.get(1));
            Assert.assertEquals("28", record.get(2));
            Assert.assertEquals("New York", record.get(3));
            Assert.assertEquals("O'Malley", record.get(records.get(1).size() - 1));
            Assert.assertEquals("O' \"Malley\"", record.get(records.get(1).size() - 2));
            Assert.assertEquals("30", record.get(records.get(1).size() - 3));
            Assert.assertEquals("Chicago", record.get(records.get(1).size() - 4));
        }
        parser.close();
    }

    @Test
    public void testParseCSVFileWithMultiLineValues() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        String csvContent = "Name,Description\nJohn,First name: John\nLast name: Doe\nAge,30\nCity,New York";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        Assert.assertArrayEquals(new String[]{"Name", "Description"}, record.getHeaderMap().keySet().toArray());
        Assert.assertEquals(5, record.size());
        Assert.assertEquals("First name: John", record.get(0));
        Assert.assertEquals("Last name: Doe", record.get(1));
        Assert.assertEquals("30", record.get(2));
        Assert.assertEquals("New York", record.get(3));
        Assert.assertEquals("", record.get(4)); // Empty value
        parser.close();
    }

    @Test
    public void testParseCSVFileWithMissingValues() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        String csvContent = "Name,Age\nJohn,28\nMike, ,30";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Name", "Age"}, record.getHeaderMap().keySet().toArray());
            Assert.assertEquals(3, record.size());
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals("28", record.get(1));
            Assert.assertNull(record.get(2)); // Missing value
        }
        parser.close();
    }

    @Test
    public void testParseCSVFileWithMalformedValues() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        String csvContent = "Name,Age,City\nJohn,28,New York\nMike,30,Chicago\nName,30,\"City\"";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(3, records.size());
        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, record.getHeaderMap().keySet().toArray());
            Assert.assertEquals(4, record.size());
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals("28", record.get(1));
            Assert.assertEquals("New York", record.get(2));
            Assert.assertEquals("City", record.get(3)); // Malformed value
        }
        parser.close();
    }

    @Test
    public void testParseCSVFileWithSkippedHeaderLine() throws IOException {
        CSVFormat format = new CSVFormat(';', '\'\'');
        String csvContent = "Name;Age;City\nMike;30;Chicago\nJohn;28;New York";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);
        parser.nextRecord(); // Skip header line

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Age", "Name", "City"}, record.getHeaderMap().keySet().toArray());
            Assert.assertEquals(3, record.size());
            Assert.assertEquals("30", record.get(0));
            Assert.assertEquals("Mike", record.get(1));
            Assert.assertEquals("Chicago", record.get(2));
        }
        parser.close();
    }
}