import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        String result = record.get(SomeEnum.VALUE1);
        assertNull(result);
    }

    @Test
    public void testGetIndex() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        String result = record.get(1);
        assertEquals("value2", result);
    }

    @Test
    public void testGetName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        String[] values = {"value"};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        String result = record.get("name");
        assertEquals("value", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameNoMapping() {
        String[] values = {"value"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameNotFound() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        String[] values = {"value"};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get("otherName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameIndexOutOfBounds() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 1);
        String[] values = {"value"};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get("name");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(null, null, "comment", 0);
        String result = record.getComment();
        assertEquals("comment", result);
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(null, null, null, 10);
        long result = record.getRecordNumber();
        assertEquals(10, result);
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record1 = new CSVRecord(null, null, null, 0);
        assertTrue(record1.isConsistent());

        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        String[] values = {"value1"};
        CSVRecord record2 = new CSVRecord(values, mapping, null, 0);
        assertFalse(record2.isConsistent());

        mapping.put("name3", 2);
        String[] values2 = {"value1", "value2"};
        CSVRecord record3 = new CSVRecord(values2, mapping, null, 0);
        assertTrue(record3.isConsistent());
    }

    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        CSVRecord record = new CSVRecord(null, mapping, null, 0);
        assertTrue(record.isMapped("name"));
        assertFalse(record.isMapped("otherName"));
    }

    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        String[] values = {"value"};
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue(record.isSet("name"));
        assertFalse(record.isSet("otherName"));
    }

    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        int count = 0;
        for (String value : record) {
            assertEquals(values[count], value);
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        Map<String, String> result = record.putIn(map);
        assertEquals(map, result);
        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
    }

    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        int size = record.size();
        assertEquals(3, size);
    }

    @Test
    public void testToMap() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        String[] values = {"value1"};
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        Map<String, String> result = record.toMap();
        Map<String, String> expected = new HashMap<>();
        expected.put("name1", "value1");
        assertEquals(expected, result);
    }

    @Test
    public void testToString() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        String result = record.toString();
        assertEquals("[value1, value2, value3]", result);
    }

    @Test
    public void testValues() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        String[] result = record.values();
        assertArrayEquals(values, result);
    }

}