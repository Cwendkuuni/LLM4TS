import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        String[] values = new String[] { "John", "Doe", "30" };
        HashMap<String, Integer> mapping = new HashMap<>();
        mapping.put("firstName", 0);
        mapping.put("lastName", 1);
        mapping.put("age", 2);
        String comment = "Test comment";
        long recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByIndex() {
        assertEquals("John", csvRecord.get(0));
        assertEquals("Doe", csvRecord.get(1));
        assertEquals("30", csvRecord.get(2));
    }

    @Test
    public void testGetByName() {
        assertEquals("John", csvRecord.get("firstName"));
        assertEquals("Doe", csvRecord.get("lastName"));
        assertEquals("30", csvRecord.get("age"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord recordWithoutMapping = new CSVRecord(new String[] { "John" }, null, null, 1);
        recordWithoutMapping.get("name");
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
        assertFalse(csvRecord.isMapped("nonExistingColumn"));
        assertFalse(csvRecord.isSet("nonExistingColumn"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : csvRecord) {
            assertNotNull(value);
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testValues() {
        assertArrayEquals(new String[] { "John", "Doe", "30" }, csvRecord.values());
    }

    @Test
    public void testGetComment() {
        assertEquals("Test comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToString() {
        assertEquals("[John, Doe, 30]", csvRecord.toString());
    }
}