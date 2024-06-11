
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

public class CSVParserTest {
    private static final String TEST_CSV_FILE_PATH = "test.csv";

    @Test
    public void testParseFile() throws IOException {
        File file = getFileFromResource(TEST_CSV_FILE_PATH);
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertEquals(3, records.size());

        CSVRecord firstRecord = records.get(0);
        Assert.assertEquals("John", firstRecord.get(0));
        Assert.assertEquals("Doe", firstRecord.get(1));
        Assert.assertEquals("30", firstRecord.get(2));
    }

    @Test
    public void testParseString() throws IOException {
        String csvData = "name,age\nJohn,30\nJane,25";
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertEquals(2, records.size());

        CSVRecord firstRecord = records.get(0);
        Assert.assertEquals("John", firstRecord.get(0));
        Assert.assertEquals("30", firstRecord.get(1));
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = getClass().getClassLoader().getResource(TEST_CSV_FILE_PATH);
        CSVParser parser = CSVParser.parse(url, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertEquals(3, records.size());

        CSVRecord firstRecord = records.get(0);
        Assert.assertEquals("John", firstRecord.get(0));
        Assert.assertEquals("Doe", firstRecord.get(1));
        Assert.assertEquals("30", firstRecord.get(2));
    }

    private File getFileFromResource(String resourcePath) {
        try {
            return Paths.get(getClass().getClassLoader().getResource(resourcePath).toURI()).toFile();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to get file from resource", e);
        }
    }
}
