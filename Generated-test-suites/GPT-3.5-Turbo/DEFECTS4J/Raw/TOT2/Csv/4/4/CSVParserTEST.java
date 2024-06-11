import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File csvFile = new File("test.csv");
        CSVFormat format = CSVFormat.RFC4180;

        CSVParser parser = CSVParser.parse(csvFile, format);

        assertNotNull(parser.getHeaderMap());
        assertNotNull(parser.getRecords());
        assertFalse(parser.isClosed());
    }

    @Test
    public void testParseString() throws IOException {
        String csvString = "Name,Age\nJohn,30\n";

        CSVFormat format = CSVFormat.DEFAULT;

        CSVParser parser = CSVParser.parse(csvString, format);

        assertNotNull(parser.getHeaderMap());
        assertNotNull(parser.getRecords());
        assertFalse(parser.isClosed());
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/test.csv");

        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = CSVFormat.EXCEL;

        CSVParser parser = CSVParser.parse(url, charset, format);

        assertNotNull(parser.getHeaderMap());
        assertNotNull(parser.getRecords());
        assertFalse(parser.isClosed());
    }

    @Test
    public void testClose() throws IOException {
        File csvFile = new File("test.csv");
        CSVFormat format = CSVFormat.RFC4180;

        CSVParser parser = CSVParser.parse(csvFile, format);

        parser.close();

        assertTrue(parser.isClosed());
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        File csvFile = new File("test.csv");
        CSVFormat format = CSVFormat.RFC4180;

        CSVParser parser = CSVParser.parse(csvFile, format);

        long recordNumber = parser.getRecordNumber();

        assertEquals(0, recordNumber);
    }

}