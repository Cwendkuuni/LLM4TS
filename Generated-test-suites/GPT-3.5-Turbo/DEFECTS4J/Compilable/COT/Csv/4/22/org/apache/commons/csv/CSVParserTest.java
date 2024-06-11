package org.apache.commons.csv;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        CSVParser parser = CSVParser.parse(new File("test.csv"), CSVFormat.DEFAULT);
        assertNotNull(parser);
    }

    @Test
    public void testParseString() throws IOException {
        String csvString = "Name,Age\nJohn,25\nJane,30\n";
        CSVParser parser = CSVParser.parse(csvString, CSVFormat.DEFAULT);
        assertNotNull(parser);
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/file.csv");
        Charset charset = Charset.forName("UTF-8");
        CSVParser parser = CSVParser.parse(url, charset, CSVFormat.DEFAULT);
        assertNotNull(parser);
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        String csvString = "Name,Age\nJohn,25\nJane,30\n";
        CSVParser parser = CSVParser.parse(csvString, CSVFormat.DEFAULT);
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertNotNull(headerMap);
        assertEquals(2, headerMap.size());
        assertTrue(headerMap.containsKey("Name"));
        assertTrue(headerMap.containsKey("Age"));
        assertEquals(0, headerMap.get("Name").intValue());
        assertEquals(1, headerMap.get("Age").intValue());
    }

    @Test
    public void testGetRecords() throws IOException {
        String csvString = "Name,Age\nJohn,25\nJane,30\n";
        CSVParser parser = CSVParser.parse(csvString, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertNotNull(record1);
        assertNotNull(record2);
        assertEquals("John", record1.get("Name"));
        assertEquals("25", record1.get("Age"));
        assertEquals("Jane", record2.get("Name"));
        assertEquals("30", record2.get("Age"));
    }
}