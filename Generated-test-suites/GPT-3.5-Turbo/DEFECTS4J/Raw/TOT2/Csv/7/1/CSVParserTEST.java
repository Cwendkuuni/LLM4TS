import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CSVParserTest {

    private CSVParser csvParser;

    @Before
    public void setUp() throws IOException {
        String csvString = "John,Doe,25\nJane,Smith,30";
        csvParser = CSVParser.parse(new File("test.csv"), CSVFormat.DEFAULT);
    }

    @Test
    public void testParseFile() throws IOException {
        List<CSVRecord> records = csvParser.getRecords();
        assertNotNull(records);
        assertEquals(2, records.size());
    }

    @Test
    public void testParseString() throws IOException {
        CSVParser stringParser = CSVParser.parse("Mary,Jane,40", CSVFormat.DEFAULT);
        List<CSVRecord> records = stringParser.getRecords();
        assertNotNull(records);
        assertEquals(1, records.size());
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("file:///path/to/csv");
        CSVParser urlParser = CSVParser.parse(url, StandardCharsets.UTF_8, CSVFormat.DEFAULT);
        List<CSVRecord> records = urlParser.getRecords();
        assertNotNull(records);
        assertEquals(2, records.size());
    }

    @Test
    public void testClose() throws IOException {
        csvParser.close();
        assertEquals(true, csvParser.isClosed());
    }

}