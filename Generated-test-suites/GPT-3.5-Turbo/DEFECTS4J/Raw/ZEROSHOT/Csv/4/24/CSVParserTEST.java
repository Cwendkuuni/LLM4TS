import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CSVParserTest {

    private CSVParser parser;
    private String csvData;

    @Before
    public void setUp() {
        csvData = "Name, Age, Gender\nJohn, 25, Male\nAlice, 28, Female";
        parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
    }

    @Test
    public void testParse() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertEquals(2, records.size());
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertNotNull(headerMap);
        assertFalse(headerMap.isEmpty());
        assertEquals(3, headerMap.size());
    }

    @Test
    public void testGetCurrentLineNumber() throws IOException {
        long lineNumber = parser.getCurrentLineNumber();
        assertEquals(0, lineNumber);
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        long recordNumber = parser.getRecordNumber();
        assertEquals(0, recordNumber);
    }

    @Test
    public void testNextRecord() throws IOException {
        CSVRecord record1 = parser.nextRecord();
        assertNotNull(record1);
        assertEquals("John", record1.get(0));
        assertEquals("25", record1.get(1));
        assertEquals("Male", record1.get(2));

        CSVRecord record2 = parser.nextRecord();
        assertNotNull(record2);
        assertEquals("Alice", record2.get(0));
        assertEquals("28", record2.get(1));
        assertEquals("Female", record2.get(2));

        CSVRecord record3 = parser.nextRecord();
        assertNull(record3);
    }
}