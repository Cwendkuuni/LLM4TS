import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1L);
        assertEquals("value1", record.get(Enum.VALUE1));
        assertEquals("value2", record.get(Enum.VALUE2));
    }

    @Test
    public void testGetIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1L);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1L);
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameNotFound() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, new HashMap<String, Integer>(), null, 1L);
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameOutOfBounds() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, new HashMap<String, Integer>() {{
            put("name", 2);
        }}, null, 1L);
        record.get("name");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1L);
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1L);
        assertEquals(1L, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1L);
        assertTrue(record1.isConsistent());

        CSVRecord record2 = new CSVRecord(new String[]{"value1"}, null, null, 1L);
        assertFalse(record2.isConsistent());

        CSVRecord record3 = new CSVRecord(new String[]{"value1", "value2", "value3"}, new HashMap<String, Integer>() {{
            put("name1", 0);
            put("name2", 1);
        }}, null, 1L);
        assertTrue(record3.isConsistent());

        CSVRecord record4 = new CSVRecord(new String[]{"value1", "value2"}, new HashMap<String, Integer>() {{
            put("name1", 0);
            put("name2", 1);
        }}, null, 1L);
        assertFalse(record4.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1L);
        assertFalse(record1.isMapped("name"));

        CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2"}, new HashMap<String, Integer>() {{
            put("name1", 0);
            put("name2", 1);
        }}, null, 1L);
        assertTrue(record2.isMapped("name1"));
        assertTrue(record2.isMapped("name2"));
        assertFalse(record2.isMapped("name3"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1L);
        assertFalse(record1.isSet("name"));

        CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2"}, new HashMap<String, Integer>() {{
            put("name1", 0);
            put("name2", 1);
        }}, null, 1L);
        assertTrue(record2.isSet("name1"));
        assertTrue(record2.isSet("name2"));
        assertFalse(record2.isSet("name3"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1L);
        Iterator<String> iterator = record.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value2", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutIn() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, new HashMap<String, Integer>() {{
            put("name1", 0);
            put("name2", 1);
            put("name3", 2);
        }}, null, 1L);

        Map<String, String> map = record.putIn(new HashMap<>());
        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
        assertNull(map.get("name3"));
    }

    @Test
    public void testSize() {
        CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1L);
        assertEquals(2, record1.size());

        CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2", "value3"}, new HashMap<String, Integer>() {{
            put("name1", 0);
            put("name2", 1);
        }}, null, 1L);
        assertEquals(3, record2.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1L);
        List<String> list = record.toList();
        assertEquals(2, list.size());
        assertEquals("value1", list.get(0));
        assertEquals("value2", list.get(1));
    }

    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, new HashMap<String, Integer>() {{
            put("name1", 0);
            put("name2", 1);
            put("name3", 2);
        }}, null, 1L);

        Map<String, String> map = record.toMap();
        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
        assertNull(map.get("name3"));
    }
}