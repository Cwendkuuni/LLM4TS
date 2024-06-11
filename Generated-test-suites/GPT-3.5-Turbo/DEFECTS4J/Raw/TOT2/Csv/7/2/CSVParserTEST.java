import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CSVParserTest {

    private CSVParser csvParser;

    @Before
    public void setUp() {
        // Initialize CSVParser with a StringReader and CSVFormat for testing
        String csvData = "Name,Age,Location\nJohn,25,USA\nJane,30,UK";
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("Name", "Age", "Location");
        csvParser = new CSVParser(new StringReader(csvData), csvFormat);
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();

        assertNotNull(records);
        assertEquals(2, records.size());
    }

    @Test
    public void testParseString() throws IOException {
        String csvData = "Name,Age,Location\nJohn,25,USA\nJane,30,UK";
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();

        assertNotNull(records);
        assertEquals(2, records.size());
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/data.csv");
        CSVParser parser = CSVParser.parse(url, Charset.forName("UTF-8"), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();

        assertNotNull(records);
        assertTrue(records.size() > 0);
    }

    @Test
    public void testGetRecords() throws IOException {
        List<CSVRecord> records = csvParser.getRecords();

        assertNotNull(records);
        assertEquals(2, records.size());
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        csvParser.getRecord();
        long recordNumber = csvParser.getRecordNumber();

        assertEquals(1, recordNumber);
    }

    @Test
    public void testHeaderMap() throws IOException {
        Map<String, Integer> headerMap = csvParser.getHeaderMap();

        assertNotNull(headerMap);
        assertEquals(3, headerMap.size());
        assertTrue(headerMap.containsKey("Name"));
        assertTrue(headerMap.containsKey("Age"));
        assertTrue(headerMap.containsKey("Location"));
    }

    // Add more test cases as needed to cover edge cases and error scenarios

}