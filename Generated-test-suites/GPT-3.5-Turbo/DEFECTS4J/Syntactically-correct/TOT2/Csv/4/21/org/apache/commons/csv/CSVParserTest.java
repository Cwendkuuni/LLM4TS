import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

public class CSVParserTest {

    private CSVParser parser;
    
    @Before
    public void setup() {
        this.parser = null;
    }

    @Test
    public void testParseFile() throws IOException, URISyntaxException {
        URL url = this.getClass().getResource("/test.csv");
        File file = new File(url.toURI());
        this.parser = CSVParser.parse(file, CSVFormat.EXCEL);
        assertTrue(this.parser != null);
    }

    @Test
    public void testParseString() throws IOException {
        String csvString = "Name, Age\nJohn, 25\nAlice, 30";
        this.parser = CSVParser.parse(csvString, CSVFormat.RFC4180);
        assertTrue(this.parser != null);
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("https://example.com/test.csv");
        this.parser = CSVParser.parse(url, Charset.defaultCharset(), CSVFormat.DEFAULT);
        assertTrue(this.parser != null);
    }

    @Test
    public void testGetRecords() throws IOException, URISyntaxException {
        URL url = this.getClass().getResource("/test.csv");
        File file = new File(url.toURI());
        this.parser = CSVParser.parse(file, CSVFormat.EXCEL);
        assertNotNull(this.parser.getRecords());
    }

    @Test
    public void testClose() throws IOException, URISyntaxException {
        URL url = this.getClass().getResource("/test.csv");
        File file = new File(url.toURI());
        this.parser = CSVParser.parse(file, CSVFormat.EXCEL);
        this.parser.close();
        assertTrue(this.parser.isClosed());
    }
}