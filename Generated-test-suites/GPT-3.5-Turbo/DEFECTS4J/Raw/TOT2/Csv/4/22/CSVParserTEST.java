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
        CSVParser parser = CSVParser.parse(file, CSVFormat.EXCEL);
        assertNotNull(parser);
    }

    @Test
    public void testParseString() throws IOException {
        String data = "name,age\nJohn,25\nAlice,30\n";
        CSVParser parser = CSVParser.parse(data, CSVFormat.DEFAULT);
        assertNotNull(parser);
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("https://example.com/test.csv");
        CSVParser parser = CSVParser.parse(url, Charset.forName("UTF-8"), CSVFormat.EXCEL);
        assertNotNull(parser);
    }

    @Test
    public void testGetRecords() throws IOException {
        String data = "header1,header2\nvalue1,value2\nvalue3,value4\n";
        CSVParser parser = CSVParser.parse(data, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        assertEquals("value1", records.get(0).get(0));
        assertEquals("value2", records.get(0).get(1));
        assertEquals("value3", records.get(1).get(0));
        assertEquals("value4", records.get(1).get(1));
    }

    @Test(expected = IOException.class)
    public void testInvalidFile() throws IOException {
        File file = new File("invalid.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.EXCEL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFile() throws IOException {
        CSVParser parser = CSVParser.parse(null, CSVFormat.EXCEL);
    }

}