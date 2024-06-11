import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    private CSVRecord record;

    @Before
    public void setUp() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        String comment = "This is a comment";
        long recordNumber = 1;
        record = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByEnum() {
        assertEquals("value1", record.get(Enum.COL1));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value2", record.get(1));
    }

    @Test
    public void testGetByName() {
        assertEquals("value3", record.get("col3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        CSVRecord recordWithoutMapping = new CSVRecord(new String[]{"value1", "value2"},
                null, null, 1);
        recordWithoutMapping.get("col1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameNotFound() {
        record.get("col4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameIndexOutOfBounds() {
        record.get("col2");
    }

    @Test
    public void testGetComment() {
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithMappingNull() {
        CSVRecord recordWithoutMapping = new CSVRecord(new String[]{"value1", "value2"},
                null, null, 1);
        assertTrue(recordWithoutMapping.isConsistent());
    }

    @Test
    public void testIsConsistentWithMappingSizeMismatch() {
        Map<String, Integer> mappingWithSizeMismatch = new HashMap<>();
        mappingWithSizeMismatch.put("col1", 0);
        mappingWithSizeMismatch.put("col2", 1);
        String[] valuesWithSizeMismatch = {"value1"};
        CSVRecord recordWithSizeMismatch = new CSVRecord(valuesWithSizeMismatch,
                mappingWithSizeMismatch, null, 1);
        assertFalse(recordWithSizeMismatch.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("col1"));
    }

    @Test
    public void testIsMappedFalse() {
        assertFalse(record.isMapped("col4"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("col1"));
    }

    @Test
    public void testIsSetFalse() {
        assertFalse(record.isSet("col3"));
    }

    @Test
    public void testIterator() {
        assertNotNull(record.iterator());
    }

    @Test
    public void testPutIn() {
        HashMap<String, String> map = record.putIn(new HashMap<>());
        assertEquals("value1", map.get("col1"));
        assertEquals("value2", map.get("col2"));
        assertEquals("value3", map.get("col3"));
    }

    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        assertEquals("value1", record.toList().get(0));
        assertEquals("value2", record.toList().get(1));
        assertEquals("value3", record.toList().get(2));
    }

    @Test
    public void testToMap() {
        Map<String, String> map = record.toMap();
        assertEquals("value1", map.get("col1"));
        assertEquals("value2", map.get("col2"));
        assertEquals("value3", map.get("col3"));
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", record.toString());
    }

    @Test
    public void testValues() {
        String[] values = record.values();
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
        assertEquals("value3", values[2]);
    }

    enum Enum {
        COL1, COL2, COL3
    }
}