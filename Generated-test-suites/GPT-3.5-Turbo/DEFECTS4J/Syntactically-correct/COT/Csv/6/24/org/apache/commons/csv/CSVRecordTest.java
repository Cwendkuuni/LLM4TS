
import org.junit.Test;
import org.apache.commons.csv.CSVRecord;

public class CSVRecordTest {

    // Method: get(Enum)
    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(null, null, null, 0L);
        String result = record.get(TestEnum.TEST);
        assertNull(result);
    }

    // Method: get(int)
    @Test
    public void testGetIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0L);
        String result = record.get(1);
        assertEquals("value2", result);
    }

    // Method: get(String)
    @Test
    public void testGetName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0L);
        String result = record.get("name");
        assertNull(result);
    }

    // Method: get(String)
    @Test(expected = IllegalStateException.class)
    public void testGetNameNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0L);
        record.get("name");
    }

    // Method: get(String)
    @Test(expected = IllegalArgumentException.class)
    public void testGetNameNotFound() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0L);
        record.get("name");
    }

    // Method: get(String)
    @Test(expected = IllegalArgumentException.class)
    public void testGetNameOutOfBounds() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0L);
        record.get("name");
    }

    // Method: getComment()
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(null, null, "comment", 0L);
        String result = record.getComment();
        assertEquals("comment", result);
    }

    // Method: getRecordNumber()
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(null, null, null, 5L);
        long result = record.getRecordNumber();
        assertEquals(5L, result);
    }

    // Method: isConsistent()
    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0L);
        boolean result = record.isConsistent();
        assertTrue(result);
    }

    // Method: isConsistent()
    @Test
    public void testIsNotConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"value1"}, null, null, 0L);
        boolean result = record.isConsistent();
        assertFalse(result);
    }

    // Method: isSet(String)
    @Test
    public void testIsSetMappedAndSet() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0L);
        boolean result = record.isSet("name");
        assertTrue(result);
    }

    // Method: isSet(String)
    @Test
    public void testIsSetMappedAndNotSet() {
        CSVRecord record = new CSVRecord(new String[]{"value1"}, null, null, 0L);
        boolean result = record.isSet("name");
        assertFalse(result);
    }

    // Method: isMapped(String)
    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(null, null, null, 0L);
        boolean result = record.isMapped("name");
        assertFalse(result);
    }

    // Method: iterator()
    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0L);
        Iterator<String> iterator = record.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value2", iterator.next());
        assertFalse(iterator.hasNext());
    }

    // Method: putIn(M)
    @Test
    public void testPutIn() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0L);
        Map<String, String> map = record.putIn(new HashMap<String, String>());
        assertNotNull(map);
        assertEquals(2, map.size());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
    }

    // Method: size()
    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0L);
        int result = record.size();
        assertEquals(2, result);
    }

    // Method: toList()
    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0L);
        List<String> list = record.toList();
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals("value1", list.get(0));
        assertEquals("value2", list.get(1));
    }

    // Method: toMap()
    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0L);
        Map<String, String> map = record.toMap();
        assertNotNull(map);
        assertEquals(2, map.size());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
    }

    // Method: toString()
    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0L);
        String result = record.toString();
        assertEquals("[value1, value2]", result);
    }

    // Method: values()
    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0L);
        String[] result = record.values();
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("value1", result[0]);
        assertEquals("value2", result[1]);
    }
}

enum TestEnum {
    TEST
}

