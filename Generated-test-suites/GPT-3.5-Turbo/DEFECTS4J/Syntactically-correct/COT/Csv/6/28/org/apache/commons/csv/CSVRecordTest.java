
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, null, 0);
        assertEquals("1", record.get(TestEnum.TEST1));
    }

    @Test
    public void testGetIndex() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, null, 0);
        assertEquals("2", record.get(1));
    }

    @Test
    public void testGetName() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, null, 0);
        assertEquals("3", record.get("ColumnName"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameWithNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, null, 0);
        record.get("ColumnName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameWithInvalidMappingName() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, null, 0);
        record.get("InvalidColumnName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameWithInvalidMappingIndex() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("ColumnName", 4);
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, mapping, null, 0);
        record.get("ColumnName");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, "Comment", 0);
        assertEquals("Comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, null, 10);
        assertEquals(10, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, null, 0);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, null, 0);
        assertFalse(record.isMapped("ColumnName"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, null, 0);
        assertFalse(record.isSet("ColumnName"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, null, 0);
        Iterator<String> iterator = record.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("3", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, null, 0);
        record.putIn(map);
        assertEquals(0, map.size());
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, null, 0);
        assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, null, 0);
        List<String> list = record.toList();
        assertEquals(3, list.size());
        assertEquals("1", list.get(0));
        assertEquals("2", list.get(1));
        assertEquals("3", list.get(2));
    }

    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, null, 0);
        Map<String, String> map = record.toMap();
        assertEquals(0, map.size());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, null, 0);
        assertArrayEquals(new String[]{"1", "2", "3"}, record.values());
    }

    enum TestEnum {
        TEST1,
        TEST2,
        TEST3
    }
}
