import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;

    @Before
    public void setUp() {
        String[] values = {"value1", "value2", "value3"};
        mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        csvRecord = new CSVRecord(values, mapping, "This is a comment", 1);
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value1", csvRecord.get(0));
    }

    @Test
    public void testGetByName() {
        assertEquals("value2", csvRecord.get("col2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1"}, null, null, 1);
        record.get("col1");
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("col1"));
        assertFalse(csvRecord.isMapped("col4"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("col2"));
        assertFalse(csvRecord.isSet("col3"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : csvRecord) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testGetComment() {
        assertEquals("This is a comment", csvRecord.getComment());
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
        assertEquals("[value1, value2, value3]", csvRecord.toString());
    }
}