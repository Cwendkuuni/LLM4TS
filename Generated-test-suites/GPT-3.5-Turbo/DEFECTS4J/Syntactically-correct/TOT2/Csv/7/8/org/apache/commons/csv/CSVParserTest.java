import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.apache.commons.csv.*;

public class CSVParserTest {

    private CSVParser parser;

    @Before
    public void setUp() {
        CSVFormat format = CSVFormat.DEFAULT;
        String csvData = "Name,Age,Gender\nAlice,25,Female\nBob,30,Male";
        try {
            parser = CSVParser.parse(csvData, format);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetRecords() {
        try {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            assertEquals("Alice", records.get(0).get(0));
            assertEquals("30", records.get(1).get("Age"));
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception thrown during getRecords test");
        }
    }

    @Test
    public void testHeaderMap() {
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertNotNull(headerMap);
        assertEquals(3, headerMap.size());
        assertTrue(headerMap.containsKey("Name"));
        assertEquals(Integer.valueOf(1), headerMap.get("Age"));
    }

    @Test
    public void testIterator() {
        Iterator<CSVRecord> iterator = parser.iterator();
        assertTrue(iterator.hasNext());
        CSVRecord record = iterator.next();
        assertEquals("Bob", record.get(0));
        assertTrue(iterator.hasNext());
        record = iterator.next();
        assertEquals("Female", record.get("Gender"));
    }

    @After
    public void tearDown() {
        try {
            parser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}