
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class CSVRecordTest {
    private static final String[] VALUES = {"value1", "value2", "value3"};
    private static final Map<String, Integer> MAPPING = new HashMap<>();
    static {
        MAPPING.put("column1", 0);
        MAPPING.put("column2", 1);
        MAPPING.put("column3", 2);
    }

    @Test
    public void testConstructorAndGetters() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, "comment", 1);
        assertArrayEquals(VALUES, record.values());
        assertEquals(MAPPING, record.isMapped());
        assertEquals("comment", record.getComment());
        assertEquals(1, record.getRecordNumber());
        assertEquals(3, record.size());
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, "comment", 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, "comment", 1);
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
        assertEquals("value3", record.get("column3"));
        assertNull(record.get("column4"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord record = new CSVRecord(VALUES, null, "comment", 1);
        record.get("column1");
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, "comment", 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithoutMapping() {
        CSVRecord record = new CSVRecord(VALUES, null, "comment", 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsNotConsistent() {
        Map<String, Integer> inconsistentMapping = new HashMap<>(MAPPING);
        inconsistentMapping.remove("column1");
        CSVRecord record = new CSVRecord(VALUES, inconsistentMapping, "comment", 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, "comment", 1);
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertTrue(record.isMapped("column3"));
        assertFalse(record.isMapped("column4"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, "comment", 1);
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertTrue(record.isSet("column3"));
        assertFalse(record.isSet("column4"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, "comment", 1);
        int i = 0;
        for (String value : record) {
            assertEquals(VALUES[i++], value);
        }
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, "comment", 1);
        assertEquals("[value1, value2, value3]", record.toString());
    }
}
