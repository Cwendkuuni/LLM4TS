import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    private static final String[] VALUES = {"value1", "value2", "value3"};
    private static final Map<String, Integer> MAPPING = new HashMap<>();
    private static final String COMMENT = "Test comment";
    private static final long RECORD_NUMBER = 1;

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
        assertEquals("value1", record.get(TestEnum.VALUE1));
        assertEquals("value2", record.get(TestEnum.VALUE2));
        assertEquals("value3", record.get(TestEnum.VALUE3));
    }

    @Test
    public void testGetIndex() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetName() {
        MAPPING.put("column1", 0);
        MAPPING.put("column2", 1);
        MAPPING.put("column3", 2);
        CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
        assertEquals("value3", record.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameNoMapping() {
        CSVRecord record = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        record.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameMappingNotFound() {
        MAPPING.put("column1", 0);
        CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
        record.get("column2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameIndexOutOfBounds() {
        MAPPING.put("column1", 3);
        CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
        record.get("column1");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
        assertEquals(COMMENT, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
        assertEquals(RECORD_NUMBER, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record1 = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
        assertTrue(record1.isConsistent());

        CSVRecord record2 = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
        record2 = new CSVRecord(new String[] {"value1"}, MAPPING, COMMENT, RECORD_NUMBER);
        assertFalse(record2.isConsistent());
    }

    @Test
    public void testIsMapped() {
        MAPPING.put("column1", 0);
        MAPPING.put("column2", 1);
        MAPPING.put("column3", 2);
        CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertTrue(record.isMapped("column3"));
        assertFalse(record.isMapped("column4"));
    }

    @Test
    public void testIsSet() {
        MAPPING.put("column1", 0);
        MAPPING.put("column2", 1);
        MAPPING.put("column3", 2);
        CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertTrue(record.isSet("column3"));
        assertFalse(record.isSet("column4"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
        Iterator<String> iterator = record.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value3", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutIn() {
        MAPPING.put("column1", 0);
        MAPPING.put("column2", 1);
        MAPPING.put("column3", 2);
        CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
        Map<String, String> map = new HashMap<>();

        record.putIn(map);
        
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
        assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
        List<String> list = record.toList();
        assertEquals(3, list.size());
        assertEquals("value1", list.get(0));
        assertEquals("value2", list.get(1));
        assertEquals("value3", list.get(2));
    }

    @Test
    public void testToMap() {
        MAPPING.put("column1", 0);
        MAPPING.put("column2", 1);
        MAPPING.put("column3", 2);
        CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);

        Map<String, String> map = record.toMap();

        assertEquals(3, map.size());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
        assertEquals("[value1, value2, value3]", record.toString());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
        assertArrayEquals(VALUES, record.values());
    }

    private enum TestEnum {
        VALUE1,
        VALUE2,
        VALUE3
    }
}