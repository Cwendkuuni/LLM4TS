package org.apache.commons.csv;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        CSVParser parser = CSVParser.parse(new File("test.csv"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
    }

    @Test
    public void testParseString() throws IOException {
        CSVParser parser = CSVParser.parse("a,b\n1,2\n3,4", CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.get(0).size());
        assertEquals("1", records.get(0).get(0));
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("https://example.com/test.csv");
        CSVParser parser = CSVParser.parse(url, Charset.defaultCharset(), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertTrue(records.size() > 0);
    }
}