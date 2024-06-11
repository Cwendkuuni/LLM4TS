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
        File file = new File("test.csv");
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(file, format);

        assertNotNull(parser);
        assertTrue(parser.getRecordNumber() == 0);

        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertTrue(records.size() > 0);
    }

    @Test
    public void testParseString() throws IOException {
        String csvString = "name,age\nJohn,25\nAlice,30";
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(csvString, format);

        assertNotNull(parser);
        assertTrue(parser.getRecordNumber() == 0);

        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertTrue(records.size() == 2);
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/test.csv");
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(url, charset, format);

        assertNotNull(parser);
        assertTrue(parser.getRecordNumber() == 0);

        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertTrue(records.size() > 0);
    }
}