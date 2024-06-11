import org.junit.Before;
import org.junit.Test;
import org.apache.commons.csv.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CSVParserTest {

    private CSVParser csvParser;

    @Before
    public void setUp() throws IOException {
        File csvFile = new File("test.csv");
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        csvParser = CSVParser.parse(csvFile, csvFormat);
    }

    @Test
    public void testParse() throws IOException {
        List<CSVRecord> records = csvParser.getRecords();
        assertEquals(3, records.size());
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        Map<String, Integer> headerMap = csvParser.getHeaderMap();
        assertTrue(headerMap.containsKey("Column1"));
        assertTrue(headerMap.containsKey("Column2"));
        assertTrue(headerMap.containsKey("Column3"));
    }

    @Test
    public void testGetCurrentLineNumber() throws IOException {
        long lineNumber = csvParser.getCurrentLineNumber();
        assertEquals(1, lineNumber);
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        long recordNumber = csvParser.getRecordNumber();
        assertEquals(0, recordNumber);
    }

    @Test
    public void testIterator() throws IOException {
        Iterator<CSVRecord> iterator = csvParser.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        assertEquals(3, count);
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorError() throws IOException {
        Iterator<CSVRecord> iterator = csvParser.iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        iterator.next(); // should throw NoSuchElementException
    }

    @Test
    public void testClose() throws IOException {
        csvParser.close();
        assertTrue(csvParser.isClosed());
    }
}