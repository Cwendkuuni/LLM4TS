import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        csvRecord = new CSVRecord(values, mapping, "comment", 1);
    }

    @Test
    public void testGetEnum() {
        assertEquals("value1", csvRecord.get(TestEnum.TEST1));
        assertEquals("value2", csvRecord.get(TestEnum.TEST2));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
    }

    @Test
    public void testGetName() {
        assertEquals("value1", csvRecord.get("col1"));
        assertEquals("value2", csvRecord.get("col2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameNoMapping() {
        csvRecord = new CSVRecord(new String[] {"value1"}, null, "comment", 1);
        csvRecord.get("col1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameInvalidName() {
        csvRecord.get("invalidName");
    }

    @Test
    public void testGetComment() {
        assertEquals("comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistentMappingNull() {
        csvRecord = new CSVRecord(new String[] {"value1"}, null, "comment", 1);
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistentMappingSizeMismatch() {
        csvRecord = new CSVRecord(new String[] {"value1"}, new HashMap<>(), "comment", 1);
        assertFalse(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("col1"));
        assertTrue(csvRecord.isMapped("col2"));
    }

    @Test
    public void testIsMappedInvalidName() {
        assertFalse(csvRecord.isMapped("invalidName"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("col1"));
        assertTrue(csvRecord.isSet("col2"));
    }

    @Test
    public void testIsSetInvalidName() {
        assertFalse(csvRecord.isSet("invalidName"));
    }

    @Test
    public void testIterator() {
        Iterator<String> iterator = csvRecord.iterator();
        assertEquals("value1", iterator.next());
        assertEquals("value2", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = csvRecord.putIn(new HashMap<>());
        assertEquals("value1", map.get("col1"));
        assertEquals("value2", map.get("col2"));
    }

    @Test
    public void testSize() {
        assertEquals(2, csvRecord.size());
    }

    @Test
    public void testToList() {
        List<String> list = csvRecord.toList();
        assertEquals("value1", list.get(0));
        assertEquals("value2", list.get(1));
    }

    @Test
    public void testToMap() {
        Map<String, String> map = csvRecord.toMap();
        assertEquals("value1", map.get("col1"));
        assertEquals("value2", map.get("col2"));
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2]", csvRecord.toString());
    }

    @Test
    public void testValues() {
        assertArrayEquals(new String[]{"value1", "value2"}, csvRecord.values());
    }

    private enum TestEnum {
        TEST1, TEST2
    }
}