import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CSVParserTest {

    private CSVParser csvParser;

    @Before
    public void setup() {
        // Initialize CSVParser with sample CSV data
        String csvData = "Name,Age,Location\nJohn,25,USA\nAlice,30,Canada";
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        csvParser = new CSVParser(csvData, csvFormat);
    }

    @Test
    public void testParseFile() throws IOException {
        // Test parsing a CSV file
        String filePath = "sample.csv";
        File file = new File(filePath);
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        assertNotNull(parser);
    }

    @Test
    public void testParseString() throws IOException {
        // Test parsing a CSV string
        String csvData = "Name,Age,Location\nJohn,25,USA\nAlice,30,Canada";
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.DEFAULT);
        assertNotNull(parser);
    }

    @Test
    public void testParseURL() throws IOException {
        // Test parsing a CSV file from URL
        URL url = new URL("http://example.com/sample.csv");
        CSVParser parser = CSVParser.parse(url, Charset.defaultCharset(), CSVFormat.DEFAULT);
        assertNotNull(parser);
    }

    @Test
    public void testGetHeaderMap() {
        // Test getting header map
        Map<String, Integer> headerMap = csvParser.getHeaderMap();
        assertNotNull(headerMap);
        assertEquals(3, headerMap.size());
    }

    @Test
    public void testGetRecords() throws IOException {
        // Test getting records
        List<CSVRecord> records = csvParser.getRecords();
        assertNotNull(records);
        assertEquals(2, records.size());
    }

    @Test
    public void testIterator() {
        // Test iterator
        int count = 0;
        for (CSVRecord record : csvParser) {
            assertNotNull(record);
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testNextRecord() throws IOException {
        // Test nextRecord method
        CSVRecord record = csvParser.nextRecord();
        assertNotNull(record);
        assertEquals("John", record.get(0));
        assertEquals("25", record.get(1));
        assertEquals("USA", record.get(2));
    }

}