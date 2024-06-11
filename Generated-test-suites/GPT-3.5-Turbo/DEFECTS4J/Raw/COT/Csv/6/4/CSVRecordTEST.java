import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    // Test get(Enum<?> e)
    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(null, null, null, 1L);
        assertEquals(null, record.get(TestEnum.VALUE1));
    }

    // Test get(int i)
    @Test
    public void testGetInt() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        assertEquals("value1", record.get(0));
    }

    // Test get(String name)
    @Test
    public void testGetString() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals("value1", record.get("column1"));
    }

    // Test getComment()
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(null, null, "This is a comment", 1L);
        assertEquals("This is a comment", record.getComment());
    }

    // Test getRecordNumber()
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(null, null, null, 1L);
        assertEquals(1L, record.getRecordNumber());
    }

    // Test isConsistent() with mapping
    @Test
    public void testIsConsistentWithMapping() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertTrue(record.isConsistent());
    }

    // Test isConsistent() without mapping
    @Test
    public void testIsConsistentWithoutMapping() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        assertTrue(record.isConsistent());
    }

    // Test isMapped(String name)
    @Test
    public void testIsMapped() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertTrue(record.isMapped("column1"));
        assertFalse(record.isMapped("column2"));
    }

    // Test isSet(String name)
    @Test
    public void testIsSet() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertTrue(record.isSet("column1"));
        assertFalse(record.isSet("column2"));
    }

    // Test iterator()
    @Test
    public void testIterator() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        Iterator<String> iterator = record.iterator();
        assertEquals("value1", iterator.next());
        assertEquals("value2", iterator.next());
        assertFalse(iterator.hasNext());
    }

    // Test putIn(M map)
    @Test
    public void testPutIn() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        Map<String, String> map = record.putIn(new HashMap<>());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
    }

    // Test size()
    @Test
    public void testSize() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        assertEquals(2, record.size());
    }

    // Test toMap()
    @Test
    public void testToMap() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        Map<String, String> map = record.toMap();
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
    }

    // Test toString()
    @Test
    public void testToString() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        assertEquals("[value1, value2]", record.toString());
    }

    // Test values()
    @Test
    public void testValues() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        assertArrayEquals(values, record.values());
    }

}