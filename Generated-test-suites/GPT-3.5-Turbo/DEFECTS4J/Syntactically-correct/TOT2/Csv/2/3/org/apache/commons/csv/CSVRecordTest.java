import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CSVRecordTest {

    // Test case for get(int i) method
    @Test
    public void testGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertEquals("value2", record.get(1));
    }

    // Test case for get(String name) method
    @Test
    public void testGetByName() {
        String[] values = {"name1", "name2", "name3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals("name3", record.get("name3"));
    }

    // Test case for isConsistent() method
    @Test
    public void testIsConsistent() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isConsistent());
    }

    // Test case for isMapped() and isSet() methods
    @Test
    public void testIsMappedAndIsSet() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isMapped("col1"));
        assertTrue(record.isSet("col1"));
        assertFalse(record.isMapped("col2"));
        assertFalse(record.isSet("col2"));
    }

    // Test case for iterator() method
    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        Iterator<String> iterator = record.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next());
    }

    // Test case for getComment() method
    @Test
    public void testGetComment() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, "This is a comment", 1);

        assertEquals("This is a comment", record.getComment());
    }

    // Test case for getRecordNumber() method
    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 5);

        assertEquals(5, record.getRecordNumber());
    }

    // Test case for size() method
    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals(3, record.size());
    }

    // Test case for toString() method
    @Test
    public void testToString() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals("[value1, value2, value3]", record.toString());
    }

}