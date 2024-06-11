import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, new HashMap<>(), "", 1);
        assertEquals("value2", record.get(TestEnum.VALUE2));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, new HashMap<>(), "", 1);
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, "", 1);
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
        assertEquals("value3", record.get("column3"));

        try {
            record.get("column4");
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            record.get("column1");
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("Unexpected ArrayIndexOutOfBoundsException was thrown");
        }
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, new HashMap<>(), "This is a comment", 1);
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, new HashMap<>(), "", 5);
        assertEquals(5L, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2"}, new HashMap<>(), "", 1);
        assertTrue(record1.isConsistent());

        Map<String, Integer> mapping2 = new HashMap<>();
        mapping2.put("column1", 0);
        mapping2.put("column2", 1);
        CSVRecord record2 = new CSVRecord(new String[]{"value1"}, mapping2, "", 1);
        assertFalse(record2.isConsistent());
    }

    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(new String[]{"value1"}, mapping, "", 1);
        assertTrue(record.isMapped("column1"));
        assertFalse(record.isMapped("column2"));
    }

    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(new String[]{"value1"}, mapping, "", 1);
        assertTrue(record.isSet("column1"));
        assertFalse(record.isSet("column2"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, new HashMap<>(), "", 1);
        int count = 0;
        for (String value : record) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testPutIn() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, "", 1);
        Map<String, String> map = record.putIn(new HashMap<>());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, new HashMap<>(), "", 1);
        assertEquals(2, record.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, new HashMap<>(), "", 1);
        assertEquals("value1", record.toList().get(0));
        assertEquals("value2", record.toList().get(1));
        assertEquals("value3", record.toList().get(2));
    }

    @Test
    public void testToMap() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, "", 1);
        Map<String, String> map = record.toMap();
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    private enum TestEnum {
        VALUE1, VALUE2, VALUE3
    }
}