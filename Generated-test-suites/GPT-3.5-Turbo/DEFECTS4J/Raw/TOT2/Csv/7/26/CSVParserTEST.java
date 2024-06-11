import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180);
        assertNotNull(parser);
        
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertEquals(3, records.size());
    }

    @Test
    public void testParseString() throws IOException {
        String csvString = "name,age\nJohn,30\nJane,25";
        CSVParser parser = CSVParser.parse(csvString, CSVFormat.EXCEL);
        assertNotNull(parser);
        
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertEquals(2, records.size());
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/data.csv");
        CSVParser parser = CSVParser.parse(url, Charset.defaultCharset(), CSVFormat.RFC4180);
        assertNotNull(parser);
        
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertEquals(5, records.size());
    }

    @Test
    public void testHeaderMap() throws IOException {
        String csvString = "name,age\nJohn,30\nJane,25";
        CSVParser parser = CSVParser.parse(csvString, CSVFormat.EXCEL);
        assertNotNull(parser);
        
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        
        assertEquals(2, records.get(0).size());
        assertEquals("name", records.get(0).get(0));
        assertEquals("age", records.get(0).get(1));
    }

    @Test(expected = IOException.class)
    public void testInvalidFormat() throws IOException {
        File file = new File("test.csv");
        // Invalid format specification causing IOException
        CSVParser parser = CSVParser.parse(file, null);
    }

}