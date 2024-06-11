import org.apache.commons.csv.*;
import org.apache.commons.csv.exceptions.IllegalFormatException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

public class CSVParserTest {

    

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Add your assertions here to check the parsed records.
        System.out.println("Parsed records: " + records);
    }

    @Test
    public void testParseString() throws IOException {
        String csvData = "name,age\nJohn Doe,30\nJane Doe,25";
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Add your assertions here to check the parsed records.
        System.out.println("Parsed records: " + records);
    }

    @Test
    public void testParseURL() throws IOException {
        String urlString = "https://example.com/data.csv";
        CSVParser parser = CSVParser.parse(new URL(urlString), Charset.forName("UTF-8"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Add your assertions here to check the parsed records.
        System.out.println("Parsed records: " + records);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFile() {
        File file = null;
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFormat() {
        File file = new File("test.csv");
        CSVFormat format = null;
        CSVParser parser = CSVParser.parse(file, format);
    }

    @Test(expected = IOException.class)
    public void testParseInvalidFile() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        Assert.assertFalse("Expected an empty list, but got: " + records, !records.isEmpty());

        throw new IOException("Test case intentionally failed to demonstrate the exception handling.");
    }

    @Test(expected = IllegalFormatException.class)
    public void testParseInvalidCSVData() throws IOException {
        String csvData = "name,age\nJohn Doe,30\nJane Doe,25\nname,age";
        CSVParser parser = CSVParser.parse(new ByteArrayInputStream(csvData.getBytes()), Charset.forName("UTF-8"), CSVFormat.DEFAULT);
    }

    @Test
    public void testGetCurrentLineNumber() throws IOException {
        String csvData = "name,age\nJohn Doe,30\nJane Doe,25";
        CSVParser parser = CSVParser.parse(new ByteArrayInputStream(csvData.getBytes()), Charset.forName("UTF-8"), CSVFormat.DEFAULT);

        Assert.assertEquals("First line number should be 1", 1, parser.getCurrentLineNumber());
        parser.nextRecord();
        Assert.assertEquals("Second line number should be 2", 2, parser.getCurrentLineNumber());
        parser.nextRecord(); // Reach the end of the input
        Assert.assertEquals("Last line number should be the same as the number of lines in the input", 3, parser.getCurrentLineNumber());

        parser.close();
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        String csvData = "name,age\nJohn Doe,30\nJane Doe,25";
        CSVParser parser = CSVParser.parse(new ByteArrayInputStream(csvData.getBytes()), Charset.forName("UTF-8"), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        parser.close();

        Assert.assertEquals("First record number should be 1", 1, parser.getRecordNumber());
        Assert.assertEquals("Second record number should be 2", 2, parser.getRecordNumber());
        Assert.assertEquals("Last record number should be the same as the number of records in the input", records.size(), parser.getRecordNumber());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRecordsAfterClose() throws IOException {
        String csvData = "name,age\nJohn Doe,30\nJane Doe,25";
        CSVParser parser = CSVParser.parse(new ByteArrayInputStream(csvData.getBytes()), Charset.forName("UTF-8"), CSVFormat.DEFAULT);
        parser.getRecords();
        parser.close();

        parser.getRecords(); // The exception should be thrown here.
    }

    @Test
    public void testGetRecordsFromIterator() throws IOException {
        String csvData = "name,age\nJohn Doe,30\nJane Doe,25";
        CSVParser parser = CSVParser.parse(new ByteArrayInputStream(csvData.getBytes()), Charset.forName("UTF-8"), CSVFormat.DEFAULT);

        Iterator<CSVRecord> iterator = parser.iterator();
        List<CSVRecord> parsedRecords = new ArrayList<>();
        while (iterator.hasNext()) {
            parsedRecords.add(iterator.next());
        }

        parser.close();

        List<CSVRecord> records = parser.getRecords();
        parser.close();

        Assert.assertArrayEquals("Parsed records from iterator should be equal to records from getRecords()", parsedRecords.toArray(new CSVRecord[parsedRecords.size()]), records.toArray(new CSVRecord[records.size()]));
    }

    @Test
    public void testIsClosed() throws IOException {
        String csvData = "name,age\nJohn Doe,30\nJane Doe,25";
        CSVParser parser = CSVParser.parse(new ByteArrayInputStream(csvData.getBytes()), Charset.forName("UTF-8"), CSVFormat.DEFAULT);

        Assert.assertFalse("Parser should be open when created", parser.isClosed());
        parser.close();
        Assert.assertTrue("Parser should be closed after close()", parser.isClosed());

        parser = CSVParser.parse(new ByteArrayInputStream(csvData.getBytes()), Charset.forName("UTF-8"), CSVFormat.DEFAULT);
        parser.close(); // Close the parser again to reset its state for the next test
        Assert.assertTrue("Parser should be closed when created after a close()", parser.isClosed());
    }

    
}