import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.net.URL;

public class CSVParserTest {
    
    private CSVParser csvParser;
    
    @Before
    public void setUp() throws IOException {
        File file = new File("test.csv");
        CSVFormat format = CSVFormat.RFC4180;
        csvParser = CSVParser.parse(file, format);
    }
    
    @Test
    public void testGetCurrentLineNumber() {
        long lineNumber = csvParser.getCurrentLineNumber();
        assertEquals(0, lineNumber); // Expected line number is 0
    }
    
    @Test
    public void testGetRecordNumber() {
        long recordNumber = csvParser.getRecordNumber();
        assertEquals(0, recordNumber); // Expected record number is 0
    }
    
    @Test
    public void testIterator() {
        Iterator<CSVRecord> iterator = csvParser.iterator();
        assertTrue(iterator.hasNext()); // Expected hasNext() to return true
    }
    
    @Test
    public void testisClosed() {
        boolean closed = csvParser.isClosed();
        assertFalse(closed); // Expected isClosed() to return false
    }
    
    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        CSVParser csvParser = CSVParser.parse(file, CSVFormat.EXCEL);
        assertNotNull(csvParser);
    }
    
    @Test
    public void testParseString() throws IOException {
        String string = "test,csv";
        CSVParser csvParser = CSVParser.parse(string, CSVFormat.EXCEL);
        assertNotNull(csvParser);
    }
    
    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/test.csv");
        Charset charset = Charset.forName("UTF-8");
        CSVParser csvParser = CSVParser.parse(url, charset, CSVFormat.EXCEL);
        assertNotNull(csvParser);
    }
    
    @Test
    public void testClose() throws IOException {
        csvParser.close();
        assertTrue(csvParser.isClosed()); // Expected isClosed() to return true
    }
}