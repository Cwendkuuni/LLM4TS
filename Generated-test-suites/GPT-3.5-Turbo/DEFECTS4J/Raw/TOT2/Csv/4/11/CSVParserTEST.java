import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.io.StringReader;

public class CSVParserTest {

    @Test
    public void testParseFile() {
        File file = new File("test.csv");
        try {
            CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
            assertNotNull(parser);
            assertTrue(parser.iterator().hasNext());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testParseString() {
        String csvString = "Name,Age\nJohn,25\nJane,30";
        try {
            CSVParser parser = CSVParser.parse(csvString, CSVFormat.DEFAULT);
            assertNotNull(parser);
            assertTrue(parser.iterator().hasNext());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testParseURL() {
        URL url = new URL("https://example.com/test.csv");
        try {
            CSVParser parser = CSVParser.parse(url, Charset.defaultCharset(), CSVFormat.DEFAULT);
            assertNotNull(parser);
            assertTrue(parser.iterator().hasNext());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testClose() {
        File file = new File("test.csv");
        try {
            CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
            parser.close();
            assertTrue(parser.isClosed());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

}