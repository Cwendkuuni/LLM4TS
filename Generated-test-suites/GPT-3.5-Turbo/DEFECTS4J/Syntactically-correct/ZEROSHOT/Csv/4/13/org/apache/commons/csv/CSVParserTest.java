import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CSVParserTest {

    private CSVFormat format;
    private CSVParser parser;

    @Before
    public void setUp() throws Exception {
        format = CSVFormat.DEFAULT;
        parser = new CSVParser(new FileReader("test.csv"), format);
    }

    @Test
    public void testParse() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals("John", record1.get(0));
        assertEquals("Doe", record1.get(1));

        CSVRecord record2 = records.get(1);
        assertEquals("Jane", record2.get(0));
        assertEquals("Smith", record2.get(1));
    }

    @Test
    public void testGetCurrentLineNumber() {
        long lineNumber = parser.getCurrentLineNumber();
        assertEquals(1, lineNumber);
    }

    @Test
    public void testGetRecordNumber() {
        long recordNumber = parser.getRecordNumber();
        assertEquals(0, recordNumber);
    }

    @Test
    public void testGetHeaderMap() {
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertNotNull(headerMap);
        assertEquals(2, headerMap.size());
        assertTrue(headerMap.containsKey("FirstName"));
        assertTrue(headerMap.containsKey("LastName"));
    }

    @Test
    public void testIsClosed() {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }
}