import org.junit.Test;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVFormat;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        assertNotNull(parser);
    }

    @Test
    public void testParseString() throws IOException {
        String csvString = "name,age\nJohn,25\nJane,30";
        CSVParser parser = CSVParser.parse(csvString, CSVFormat.DEFAULT);
        assertNotNull(parser);
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/test.csv");
        CSVParser parser = CSVParser.parse(url, Charset.defaultCharset(), CSVFormat.DEFAULT);
        assertNotNull(parser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFile_NullFile() throws IOException {
        CSVParser.parse(null, CSVFormat.DEFAULT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseString_NullString() throws IOException {
        CSVParser.parse(null, CSVFormat.DEFAULT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseURL_NullURL() throws IOException {
        CSVParser.parse(null, Charset.defaultCharset(), CSVFormat.DEFAULT);
    }

}