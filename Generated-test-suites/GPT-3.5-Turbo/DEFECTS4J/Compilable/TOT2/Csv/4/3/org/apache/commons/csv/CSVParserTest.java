package org.apache.commons.csv;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.EXCEL);
        assertNotNull(parser);
    }

    @Test
    public void testParseString() throws IOException {
        String csvString = "Name, Age\nJohn, 25\n";
        CSVParser parser = CSVParser.parse(csvString, CSVFormat.EXCEL);
        assertNotNull(parser);
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("https://example.com/test.csv");
        CSVParser parser = CSVParser.parse(url, Charset.defaultCharset(), CSVFormat.EXCEL);
        assertNotNull(parser);
    }

    @Test
    public void testGetRecords() throws IOException {
        String csvString = "Name, Age\nJohn, 25\nAlice, 30\n";
        CSVParser parser = CSVParser.parse(csvString, CSVFormat.EXCEL);
        assertNotNull(parser);

        assertTrue(parser.iterator().hasNext());

        int recordCount = 0;
        for (CSVRecord record : parser) {
            recordCount++;
        }

        assertEquals(2, recordCount);
    }

}