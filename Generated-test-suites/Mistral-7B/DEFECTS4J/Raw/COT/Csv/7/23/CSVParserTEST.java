import org.apache.commons.csv.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

/**
 * Test class for CSVParser
 */
public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        String filePath = "src/test/resources/sample.csv"; // Replace with the actual path to your test CSV file
        CSVParser parser = CSVParser.parse(new File(filePath), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertFalse("Parser should not be empty", records.isEmpty());

        for (CSVRecord record : records) {
            Assert.assertNotNull(record);
            Assert.assertFalse(record.isHeader());
            Assert.assertFalse(record.isComment());
            Assert.assertEquals(record.size(), 3); // Assuming the CSV file has 3 columns
            // Add more assertions based on the structure and contents of your CSV file
        }
    }

    @Test
    public void testParseString() throws IOException {
        String csvContent = "Name,Age,City\nJohn,30,New York\nJane,25,Chicago";
        CSVParser parser = new CSVParser(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertFalse("Parser should not be empty", records.isEmpty());

        for (CSVRecord record : records) {
            Assert.assertNotNull(record);
            Assert.assertFalse(record.isHeader());
            Assert.assertFalse(record.isComment());
            Assert.assertEquals(record.size(), 3);
            // Add more assertions based on the structure and contents of the CSV string
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFile() {
        CSVParser.parse(null, CSVFormat.DEFAULT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFormat() throws IOException {
        File file = new File("src/test/resources/sample.csv");
        CSVParser.parse(file, null);
    }

    @Test(expected = IOException.class)
    public void testParseInvalidFile() throws IOException {
        File file = new File("src/test/resources/invalid.csv"); // Replace with the actual path to an invalid CSV file
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertTrue("Parser should be empty", records.isEmpty());
    }

    @Test
    public void testParseEmptyString() throws IOException {
        String csvContent = "";
        CSVParser parser = new CSVParser(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertTrue("Parser should be empty", records.isEmpty());
    }

    @Test
    public void testParseCommentedString() throws IOException {
        String csvContent = "Name,Age,City\nJohn,30,New York#This is a comment\nJane,25,Chicago";
        CSVParser parser = new CSVParser(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertFalse("Parser should not be empty", records.isEmpty());

        for (CSVRecord record : records) {
            Assert.assertNotNull(record);
            Assert.assertFalse(record.isHeader());
            Assert.assertFalse(record.isComment());
            Assert.assertEquals(record.size(), 3);
            // Add more assertions based on the structure and contents of the CSV string
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void testParseEmptyParser() throws IOException {
        CSVParser parser = new CSVParser(new StringReader(""), CSVFormat.DEFAULT);
        parser.getRecords();
    }

    @Test
    public void testCloseParser() throws IOException {
        String csvContent = "Name,Age,City\nJohn,30,New York";
        CSVParser parser = new CSVParser(new StringReader(csvContent), CSVFormat.DEFAULT);
        parser.close();

        List<CSVRecord> records = parser.getRecords();
        Assert.assertFalse("Parser should not be empty", records.isEmpty());

        try {
            records = parser.getRecords();
            Assert.fail("Parser should be closed and empty");
        } catch (IOException e) {
            // Expected
        }
    }
}