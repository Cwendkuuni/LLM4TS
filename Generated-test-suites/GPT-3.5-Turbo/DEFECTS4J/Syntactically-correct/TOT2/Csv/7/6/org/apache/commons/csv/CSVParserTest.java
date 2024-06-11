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
        String csvString = "A,B,C\n1,2,3";
        CSVParser parser = CSVParser.parse(csvString, CSVFormat.EXCEL);
        assertNotNull(parser);
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/test.csv");
        CSVParser parser = CSVParser.parse(url, Charset.defaultCharset(), CSVFormat.EXCEL);
        assertNotNull(parser);
    }

    @Test
    public void testGetRecords() throws IOException {
        String csvString = "Name, Age\nJohn, 30\nJane, 25";
        CSVParser parser = CSVParser.parse(csvString, CSVFormat.EXCEL);
        assertEquals(2, parser.getRecords().size());
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        String csvString = "Column1, Column2\nValue1, Value2";
        CSVParser parser = CSVParser.parse(csvString, CSVFormat.EXCEL);
        assertNotNull(parser.getHeaderMap());
    }
}