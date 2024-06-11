import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.csv.*;

public class CSVParserTest {

    private CSVFormat csvFormat;

    @Before
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    public void testParseFile() {
        try {
            CSVParser parser = CSVParser.parse(new File("test.csv"), csvFormat);
            assertNotNull(parser);
        } catch (IOException e) {
            fail("IOException thrown while parsing file");
        }
    }

    @Test
    public void testParseString() {
        try {
            CSVParser parser = CSVParser.parse("test,string", csvFormat);
            assertNotNull(parser);
        } catch (IOException e) {
            fail("IOException thrown while parsing string");
        }
    }

    @Test
    public void testParseURL() {
        try {
            CSVParser parser = CSVParser.parse(new URL("http://example.com/test.csv"), Charset.defaultCharset(), csvFormat);
            assertNotNull(parser);
        } catch (IOException e) {
            fail("IOException thrown while parsing URL");
        }
    }

    @Test
    public void testGetRecords() {
        try {
            CSVParser parser = new CSVParser(new StringReader("a,b\nc,d"), csvFormat);
            assertTrue(parser.getRecords().size() == 2);
        } catch (IOException e) {
            fail("IOException thrown while getting records");
        }
    }

    @Test
    public void testGetCurrentLineNumber() {
        try {
            CSVParser parser = new CSVParser(new StringReader("a,b\nc,d"), csvFormat);
            assertTrue(parser.getCurrentLineNumber() == 1);
        } catch (IOException e) {
            fail("IOException thrown while getting current line number");
        }
    }

    @Test
    public void testGetRecordNumber() {
        try {
            CSVParser parser = new CSVParser(new StringReader("a,b\nc,d"), csvFormat);
            assertTrue(parser.getRecordNumber() == 0);
            // Iterate through records to increment record number
            for (CSVRecord record : parser) {
                // Do nothing
            }
            assertTrue(parser.getRecordNumber() == 2);
        } catch (IOException e) {
            fail("IOException thrown while getting record number");
        }
    }

    @Test
    public void testClose() {
        try {
            CSVParser parser = new CSVParser(new StringReader("a,b\nc,d"), csvFormat);
            assertFalse(parser.isClosed());
            parser.close();
            assertTrue(parser.isClosed());
        } catch (IOException e) {
            fail("IOException thrown while closing parser");
        }
    }

    @Test
    public void testIterator() {
        try {
            CSVParser parser = new CSVParser(new StringReader("a,b\nc,d"), csvFormat);
            int count = 0;
            for (CSVRecord record : parser) {
                count++;
            }
            assertTrue(count == 2);
        } catch (IOException e) {
            fail("IOException thrown while iterating through records");
        }
    }

    @Test
    public void testNextRecord() {
        try {
            CSVParser parser = new CSVParser(new StringReader("a,b\nc,d"), csvFormat);
            CSVRecord record = parser.nextRecord();
            assertNotNull(record);
        } catch (IOException e) {
            fail("IOException thrown while getting next record");
        }
    }

}