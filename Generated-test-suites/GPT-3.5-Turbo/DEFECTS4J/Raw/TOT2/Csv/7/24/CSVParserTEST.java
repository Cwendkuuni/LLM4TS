import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import org.apache.commons.csv.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class CSVParserTest {

    private CSVFormat csvFormat;

    @Before
    public void setUp() {
        csvFormat = CSVFormat.EXCEL;
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, csvFormat);
        assertNotNull(parser);
        parser.close();
    }

    @Test
    public void testParseString() throws IOException {
        String csvData = "Name, Age\nJohn, 30\nJane, 25";
        CSVParser parser = CSVParser.parse(csvData, csvFormat);
        assertNotNull(parser);
        parser.close();
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/test.csv");
        CSVParser parser = CSVParser.parse(url, StandardCharsets.UTF_8, csvFormat);
        assertNotNull(parser);
        parser.close();
    }

    @Test
    public void testGetRecords() throws IOException {
        String csvData = "Name, Age\nJohn, 30\nJane, 25";
        CSVParser parser = new CSVParser(csvData, csvFormat);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        parser.close();
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        String csvData = "Name, Age\nJohn, 30\nJane, 25";
        CSVParser parser = new CSVParser(csvData, csvFormat);
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertTrue(headerMap.containsKey("Name"));
        assertTrue(headerMap.containsKey("Age"));
        parser.close();
    }

    @Test
    public void testIterator() throws IOException {
        String csvData = "Name, Age\nJohn, 30\nJane, 25";
        CSVParser parser = new CSVParser(csvData, csvFormat);
        int count = 0;
        for (CSVRecord record : parser) {
            count++;
        }
        assertEquals(2, count);
        parser.close();
    }

    @Test
    public void testClose() throws IOException {
        CSVParser parser = new CSVParser("Name, Age\nJohn, 30\nJane, 25", csvFormat);
        parser.close();
        assertTrue(parser.isClosed());
    }

}