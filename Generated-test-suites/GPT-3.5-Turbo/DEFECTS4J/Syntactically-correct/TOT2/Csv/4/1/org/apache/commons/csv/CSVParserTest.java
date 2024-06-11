import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        assertNotNull(parser);
    }

    @Test
    public void testParseString() throws IOException {
        String csvString = "name,age\nJohn,25";
        CSVParser parser = CSVParser.parse(csvString, CSVFormat.DEFAULT);
        assertNotNull(parser);
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/test.csv");
        CSVParser parser = CSVParser.parse(url, Charset.forName("UTF-8"), CSVFormat.DEFAULT);
        assertNotNull(parser);
    }

    @Test
    public void testGetRecords() throws IOException {
        String csvString = "name,age\nJohn,25\nJane,30";
        CSVParser parser = CSVParser.parse(csvString, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord firstRecord = records.get(0);
        assertEquals("John", firstRecord.get("name"));
        assertEquals("25", firstRecord.get("age"));

        CSVRecord secondRecord = records.get(1);
        assertEquals("Jane", secondRecord.get("name"));
        assertEquals("30", secondRecord.get("age"));
    }

}