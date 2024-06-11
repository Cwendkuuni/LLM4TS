import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGet() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetWithName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertNull(record.get("column1"));
        assertNull(record.get("column2"));
        assertNull(record.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithNameNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        record.get("column1");
    }

    @Test
    public void testGetWithNameMapped() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, new HashMap<String, Integer>(), null, 1);
        assertNull(record.get("column1"));
        assertNull(record.get("column2"));
        assertNull(record.get("column3"));
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithMapping() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithMappingMismatch() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertFalse(record.isMapped("column1"));
        assertFalse(record.isMapped("column2"));
        assertFalse(record.isMapped("column3"));
    }

    @Test
    public void testIsMappedWithMapping() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertFalse(record.isMapped("column3"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertFalse(record.isSet("column1"));
        assertFalse(record.isSet("column2"));
        assertFalse(record.isSet("column3"));
    }

    @Test
    public void testIsSetWithMapping() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertFalse(record.isSet("column3"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Iterator<String> iterator = record.iterator();
        assertEquals("value1", iterator.next());
        assertEquals("value2", iterator.next());
        assertEquals("value3", iterator.next());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, record.values());
    }
    
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);
        assertEquals("comment", record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }
    
    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals(3, record.size());
    }
    
    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("[value1, value2, value3]", record.toString());
    }
    
}