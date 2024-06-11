import org.apache.commons.csv.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CSVParserTest {

    private CSVFormat csvFormat;
    private CSVParser csvParser;

    @Before
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    public void testParseFile() throws IOException {
        File testFile = new File("test.csv");
        CSVParser parser = CSVParser.parse(testFile, csvFormat);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertFalse(records.isEmpty());

        for (CSVRecord record : records) {
            Assert.assertNotNull(record);
            Assert.assertFalse(record.isHeader());
            Assert.assertFalse(record.isComment());

            List<String> values = new ArrayList<>(record.values().length);
            for (Token value : record.values()) {
                values.add(value.toString());
            }
            Assert.assertEquals("Expected record values", testRecordValues, values.toArray());
        }

        parser.close();
    }

    @Test
    public void testParseString() throws IOException {
        String testString = "first,second,third\nthird,fourth,fifth\n";
        CSVParser parser = new CSVParser(new StringReader(testString), csvFormat);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertFalse(records.isEmpty());

        for (CSVRecord record : records) {
            Assert.assertNotNull(record);
            Assert.assertFalse(record.isHeader());
            Assert.assertFalse(record.isComment());

            List<String> values = new ArrayList<>(record.values().length);
            for (Token value : record.values()) {
                values.add(value.toString());
            }
            Assert.assertEquals("Expected record values", testRecordValues, values.toArray());
        }

        parser.close();
    }

    @Test
    public void testParseException() throws IOException {
        String testString = "first,second,third\nthird,fourth,fifth\nException: Test";
        CSVParser parser = new CSVParser(new StringReader(testString), csvFormat);

        try {
            parser.getRecords();
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().contains("Exception: Test"));
        }

        parser.close();
    }

    @Test
    public void testParseEmptyFile() throws IOException {
        File testFile = new File("empty.csv");
        CSVParser parser = CSVParser.parse(testFile, csvFormat);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertTrue(records.isEmpty());

        parser.close();
    }

    @Test
    public void testParseEmptyString() throws IOException {
        String testString = "";
        CSVParser parser = new CSVParser(new StringReader(testString), csvFormat);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertTrue(records.isEmpty());

        parser.close();
    }

    @Test
    public void testParseInvalidFile() throws IOException {
        File testFile = new File("invalid.csv");
        CSVParser parser = CSVParser.parse(testFile, csvFormat);

        try {
            parser.getRecords();
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().contains("Invalid CSV format"));
        }

        parser.close();
    }

    @Test
    public void testParseInputStream() throws IOException {
        String testString = "first,second,third\nthird,fourth,fifth\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        ByteArrayReader reader = new ByteArrayReader(inputStream.readAllBytes());

        CSVParser parser = new CSVParser(reader, csvFormat);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertFalse(records.isEmpty());

        for (CSVRecord record : records) {
            Assert.assertNotNull(record);
            Assert.assertFalse(record.isHeader());
            Assert.assertFalse(record.isComment());

            List<String> values = new ArrayList<>(record.values().length);
            for (Token value : record.values()) {
                values.add(value.toString());
            }
            Assert.assertEquals("Expected record values", testRecordValues, values.toArray());
        }

        parser.close();
    }

    @Test
    public void testParseInputStreamException() throws IOException {
        String testString = "first,second,third\nthird,fourth,fifth\nException: Test";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        ByteArrayReader reader = new ByteArrayReader(inputStream.readAllBytes());

        CSVParser parser = new CSVParser(reader, csvFormat);

        try {
            parser.getRecords();
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().contains("Exception: Test"));
        }

        parser.close();
    }

    @Test
    public void testParseURL() throws IOException {
        String testString = "first,second,third\nthird,fourth,fifth\n";
        URL testURL = new URL("test", "", "", new ByteArrayInputStream(testString.getBytes()));

        CSVParser parser = new CSVParser(new InputStreamReader(testURL.openStream()), csvFormat);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertFalse(records.isEmpty());

        for (CSVRecord record : records) {
            Assert.assertNotNull(record);
            Assert.assertFalse(record.isHeader());
            Assert.assertFalse(record.isComment());

            List<String> values = new ArrayList<>(record.values().length);
            for (Token value : record.values()) {
                values.add(value.toString());
            }
            Assert.assertEquals("Expected record values", testRecordValues, values.toArray());
        }

        parser.close();
    }

    @Test
    public void testParseURLException() throws IOException {
        String testString = "first,second,third\nthird,fourth,fifth\nException: Test";
        URL testURL = new URL("test", "", "", new ByteArrayInputStream(testString.getBytes()));

        CSVParser parser = new CSVParser(new InputStreamReader(testURL.openStream()), csvFormat);

        try {
            parser.getRecords();
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().contains("Exception: Test"));
        }

        parser.close();
    }

    private static final String[] testRecordValues = {"first", "second", "third"};
}