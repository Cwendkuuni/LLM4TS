import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get(TestEnum.VALUE1));
        assertEquals("value2", record.get(TestEnum.VALUE2));
        assertEquals("value3", record.get(TestEnum.VALUE3));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        assertEquals("value1", record.get("name1"));
        assertEquals("value2", record.get("name2"));
        assertEquals("value3", record.get("name3"));
    }

    @Test
    public void testGetByNameWithNull() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", null}, mapping, null, 1);
        assertEquals("value1", record.get("name1"));
        assertEquals("value2", record.get("name2"));
        assertNull(record.get("name3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        record.get("name1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        record.get("invalidName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidIndex() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        record.get("name3");
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
    public void testIsConsistent() {
        CSVRecord consistentRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                new HashMap<>(), null, 1);
        assertEquals(true, consistentRecord.isConsistent());

        CSVRecord inconsistentRecord = new CSVRecord(new String[]{"value1", "value2"},
                new HashMap<>(), null, 1);
        assertEquals(false, inconsistentRecord.isConsistent());

        CSVRecord recordWithoutMapping = new CSVRecord(new String[]{"value1", "value2", "value3"},
                null, null, 1);
        assertEquals(true, recordWithoutMapping.isConsistent());
    }

    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertEquals(true, record.isMapped("name1"));
        assertEquals(true, record.isMapped("name2"));
        assertEquals(false, record.isMapped("name3"));
    }

    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertEquals(true, record.isSet("name1"));
        assertEquals(true, record.isSet("name2"));
        assertEquals(false, record.isSet("name3"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        int count = 0;
        for (String value : record) {
            count++;
        }
        assertEquals(record.size(), count);
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        record.putIn(map);
        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals(3, record.size());
    }
    
    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        int count = 0;
        for (String value : record.toList()) {
            count++;
        }
        assertEquals(record.size(), count);
    }

    @Test
    public void testToMap() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        Map<String, String> map = record.toMap();
        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("[value1, value2, value3]", record.toString());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, record.values());
    }
    
    private enum TestEnum {
        VALUE1, VALUE2, VALUE3;
    }

}