
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);
        assertEquals("value1", record.get(TestEnum.VALUE1));
        assertEquals("value2", record.get(TestEnum.VALUE2));
        assertEquals("value3", record.get(TestEnum.VALUE3));
    }

    @Test
    public void testGetInt() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetString() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("location", 2);
        CSVRecord record = new CSVRecord(new String[]{"John", "30", "USA"}, mapping, "comment", 1);
        assertEquals("John", record.get("name"));
        assertEquals("30", record.get("age"));
        assertEquals("USA", record.get("location"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetStringNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStringInvalidMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        CSVRecord record = new CSVRecord(new String[]{"John", "30"}, mapping, "comment", 1);
        record.get("location");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStringIndexOutOfBounds() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        CSVRecord record = new CSVRecord(new String[]{"John"}, mapping, "comment", 1);
        record.get("name");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("location", 2);
        CSVRecord consistentRecord = new CSVRecord(new String[]{"John", "30", "USA"}, mapping, "comment", 1);
        assertTrue(consistentRecord.isConsistent());

        Map<String, Integer> inconsistentMapping = new HashMap<>();
        inconsistentMapping.put("name", 0);
        inconsistentMapping.put("age", 1);
        CSVRecord inconsistentRecord = new CSVRecord(new String[]{"John", "30", "USA"}, inconsistentMapping, "comment", 1);
        assertFalse(inconsistentRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("location", 2);
        CSVRecord record = new CSVRecord(new String[]{"John", "30", "USA"}, mapping, "comment", 1);
        assertTrue(record.isMapped("name"));
        assertTrue(record.isMapped("age"));
        assertTrue(record.isMapped("location"));
        assertFalse(record.isMapped("gender"));
    }

    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("location", 2);
        CSVRecord record = new CSVRecord(new String[]{"John", "30", "USA"}, mapping, "comment", 1);
        assertTrue(record.isSet("name"));
        assertTrue(record.isSet("age"));
        assertTrue(record.isSet("location"));
        assertFalse(record.isSet("gender"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);
        int count = 0;
        for (String value : record) {
            assertEquals("value" + (count+1), value);
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("location", 2);
        CSVRecord record = new CSVRecord(new String[]{"John", "30", "USA"}, mapping, "comment", 1);
        record.putIn(map);
        assertEquals("John", map.get("name"));
        assertEquals("30", map.get("age"));
        assertEquals("USA", map.get("location"));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);
        assertEquals(3, record.size());
    }

    @Test
    public void testToMap() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("location", 2);
        CSVRecord record = new CSVRecord(new String[]{"John", "30", "USA"}, mapping, "comment", 1);
        Map<String, String> map = record.toMap();
        assertEquals("John", map.get("name"));
        assertEquals("30", map.get("age"));
        assertEquals("USA", map.get("location"));
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);
        assertEquals("[value1, value2, value3]", record.toString());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, record.values());
    }

    private enum TestEnum {
        VALUE1, VALUE2, VALUE3
    }
}
