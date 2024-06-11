import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import static org.junit.Assert.assertEquals;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        CSVFormat format = CSVFormat.EXCEL;
        CSVParser parser = CSVParser.parse(file, format);
        assertEquals(parser.getHeaderMap(), null);
        assertEquals(parser.getCurrentLineNumber(), 0);
        assertEquals(parser.getRecordNumber(), 0);
    }

    @Test
    public void testParseString() throws IOException {
        String string = "1,2,3\n4,5,6\n";
        CSVFormat format = CSVFormat.RFC4180;
        CSVParser parser = CSVParser.parse(string, format);
        assertEquals(parser.getHeaderMap(), null);
        assertEquals(parser.getCurrentLineNumber(), 0);
        assertEquals(parser.getRecordNumber(), 0);
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://www.example.com/csvfile.csv");
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(url, charset, format);
        assertEquals(parser.getHeaderMap(), null);
        assertEquals(parser.getCurrentLineNumber(), 0);
        assertEquals(parser.getRecordNumber(), 0);
    }

    @Test
    public void testGetRecords() throws IOException {
        String string = "1,2,3\n4,5,6\n";
        CSVFormat format = CSVFormat.RFC4180;
        CSVParser parser = CSVParser.parse(string, format);
        assertEquals(parser.getRecords().size(), 2);
    }
}