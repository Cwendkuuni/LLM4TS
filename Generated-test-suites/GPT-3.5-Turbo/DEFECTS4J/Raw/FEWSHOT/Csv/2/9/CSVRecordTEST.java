import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGetSize() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals(3, record.size());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 5);
        assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testGetComment() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, "This is a comment", 0);
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("value1", record.get("name1"));
        assertEquals("value2", record.get("name2"));
        assertEquals("value3", record.get("name3"));
    }

    @Test(expected=IllegalStateException.class)
    public void testGetByNameNoMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        record.get("name");
    }

    @Test
    public void testIsMapped() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertTrue(record.isMapped("name3"));
        assertFalse(record.isMapped("name4"));
    }

    @Test
    public void testIsSet() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        assertTrue(record.isSet("name3"));
        assertFalse(record.isSet("name4"));
    }

    @Test
    public void testIsConsistent() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentNoMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsNotConsistent() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        int count = 0;
        for (String value : record) {
            assertEquals(values[count], value);
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testToString() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("[value1, value2, value3]", record.toString());
    }

}