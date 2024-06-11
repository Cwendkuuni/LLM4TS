import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CSVParserTest {

    private CSVParser csvParser;

    @Before
    public void setUp() throws IOException {
        csvParser = CSVParser.parse(new File("test.csv"), CSVFormat.EXCEL);
    }

    @After
    public void tearDown() throws IOException {
        if (csvParser != null) {
            csvParser.close();
        }
    }

    @Test
    public void testGetRecords() throws IOException {
        List<CSVRecord> records = csvParser.getRecords();
        assertNotNull(records);
        assertEquals(2, records.size());
    }

    @Test
    public void testNextRecord() throws IOException {
        CSVRecord record = csvParser.nextRecord();
        assertNotNull(record);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
    }

    @Test
    public void testGetCurrentLineNumber() {
        long lineNumber = csvParser.getCurrentLineNumber();
        assertEquals(0, lineNumber);
    }

    @Test
    public void testGetRecordNumber() {
        long recordNumber = csvParser.getRecordNumber();
        assertEquals(0, recordNumber);
    }

    @Test(expected = IOException.class)
    public void testInvalidFilePath() throws IOException {
        CSVParser.parse(new File("invalid.csv"), CSVFormat.EXCEL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFile() throws IOException {
        CSVParser.parse(null, CSVFormat.EXCEL);
    }

    @Test
    public void testClose() throws IOException {
        csvParser.close();
        assertTrue(csvParser.isClosed());
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (CSVRecord record : csvParser) {
            count++;
        }
        assertEquals(2, count);
    }
}