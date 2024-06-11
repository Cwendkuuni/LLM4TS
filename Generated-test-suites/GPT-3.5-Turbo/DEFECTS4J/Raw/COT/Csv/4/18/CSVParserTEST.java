import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180);
        assertNotNull(parser);
        parser.close();
    }

    @Test
    public void testParseString() throws IOException {
        String string = "a,b,c\n1,2,3\n";
        CSVParser parser = CSVParser.parse(string, CSVFormat.RFC4180);
        assertNotNull(parser);
        parser.close();
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/test.csv");
        Charset charset = Charset.forName("UTF-8");
        CSVParser parser = CSVParser.parse(url, charset, CSVFormat.RFC4180);
        assertNotNull(parser);
        parser.close();
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180);
        assertNotNull(parser.getHeaderMap());
        parser.close();
    }

    @Test
    public void testGetRecords() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180);
        assertNotNull(parser.getRecords());
        parser.close();
    }

    @Test
    public void testGetCurrentLineNumber() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180);
        assertNotNull(parser.getCurrentLineNumber());
        parser.close();
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180);
        assertNotNull(parser.getRecordNumber());
        parser.close();
    }
}