import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        assertEquals(null, record.get(TestEnum.FIRST));
        assertEquals(null, record.get(TestEnum.SECOND));
    }

    @Test
    public void testGetInt() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
    }

    @Test
    public void testGetString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetStringNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        record.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStringMappingNotFound() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        record.get("column3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStringIndexOutOfBounds() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(new String[]{"value1"}, mapping, null, 0);
        record.get("column1");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(null, null, "This is a comment", 0);
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(null, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistentNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 0);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsInconsistentWithMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1"}, mapping, null, 0);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(null, mapping, null, 0);
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertFalse(record.isMapped("column3"));
    }

    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1"}, mapping, null, 0);
        assertTrue(record.isSet("column1"));
        assertFalse(record.isSet("column2"));
        assertFalse(record.isSet("column3"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        int count = 0;
        for (String value : record) {
            count++;
            assertTrue(value.equals("value1") || value.equals("value2"));
        }
        assertEquals(2, count);
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 0);
        record.putIn(map);
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        assertEquals(2, record.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        List<String> list = record.toList();
        assertEquals(2, list.size());
        assertTrue(list.contains("value1"));
        assertTrue(list.contains("value2"));
    }

    @Test
    public void testToMap() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 0);
        Map<String, String> map = record.toMap();
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        assertEquals("[value1, value2]", record.toString());
    }

    private enum TestEnum {
        FIRST, SECOND
    }
}