import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

public class CSVParserTest {

    private CSVParser csvParser;

    @Before
    public void setUp() {
        csvParser = new CSVParser();
    }

    @Test
    public void testParseFile() {
        File file = new File("test.csv");
        CSVFormat format = CSVFormat.DEFAULT;
        try {
            CSVParser parser = CSVParser.parse(file, format);
            assertNotNull(parser);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testParseString() {
        String string = "a,b,c\n1,2,3";
        CSVFormat format = CSVFormat.DEFAULT;
        try {
            CSVParser parser = CSVParser.parse(string, format);
            assertNotNull(parser);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testParseURL() {
        URL url = new URL("http://example.com/test.csv");
        Charset charset = Charset.defaultCharset();
        CSVFormat format = CSVFormat.DEFAULT;
        try {
            CSVParser parser = CSVParser.parse(url, charset, format);
            assertNotNull(parser);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testGetRecords() {
        String input = "name,age\nJohn,25\nJane,30";
        CSVParser parser = new CSVParser(new StringReader(input), CSVFormat.DEFAULT);
        try {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
}