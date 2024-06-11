import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {
    
    @Test
    public void testGetByEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals("value1", record.get(TestEnum.VALUE1));
        assertEquals("value2", record.get(TestEnum.VALUE2));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
    }

    @Test
    public void testGetByName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertEquals("value1", record.get("name1"));
        assertEquals("value2", record.get("name2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        record.get("name1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameMappingNotFound() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        record.get("name3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameIndexOutOfBounds() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        CSVRecord record = new CSVRecord(new String[]{"value1"}, mapping, null, 1);
        record.get("name1");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "This is a comment.", 1);
        assertEquals("This is a comment.", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 5);
        assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertTrue(record.isConsistent());

        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertTrue(record.isConsistent());

        mapping.put("name3", 2);
        record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertFalse(record.isMapped("name3"));
    }

    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        assertFalse(record.isSet("name3"));
        assertFalse(record.isSet("name4"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        String[] expectedValues = new String[]{"value1", "value2"};
        int i = 0;
        for (String value : record) {
            assertEquals(expectedValues[i], value);
            i++;
        }
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        map = record.putIn(map);

        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
        assertNull(map.get("name3"));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals(2, record.size());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        String[] expectedValues = new String[]{"value1", "value2"};
        assertArrayEquals(expectedValues, record.values());
    }

    private enum TestEnum {
        VALUE1, VALUE2
    }
}