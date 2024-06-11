import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord record;

    @Before
    public void setUp() {
        String[] values = new String[] {"value1", "value2", "value3"};
        record = new CSVRecord(values, null, null, 1);
    }

    @Test
    public void testGetEnum() {
        assertEquals("value1", record.get(TestEnum.TEST1));
        assertEquals("value2", record.get(TestEnum.TEST2));
        assertEquals("value3", record.get(TestEnum.TEST3));
    }

    @Test
    public void testGetIndex() {
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetName() {
        assertNull(record.get("name1"));
        assertNull(record.get("name2"));
        assertNull(record.get("name3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameNoMapping() {
        CSVRecord recordNoMapping = new CSVRecord(new String[] {"value1", "value2", "value3"}, null, null, 1);
        recordNoMapping.get("name1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameInvalidMapping() {
        CSVRecord recordInvalidMapping = new CSVRecord(new String[] {"value1", "value2", "value3"}, new HashMap<String, Integer>(), null, 1);
        recordInvalidMapping.get("name1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameIndexOutOfBounds() {
        CSVRecord recordIndexOutOfBounds = new CSVRecord(new String[] {"value1", "value2"}, new HashMap<String, Integer>(), null, 1);
        recordIndexOutOfBounds.get("name1");
    }

    @Test
    public void testGetComment() {
        assertNull(record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentNoMapping() {
        CSVRecord recordNoMapping = new CSVRecord(new String[] {"value1", "value2", "value3"}, null, null, 1);
        assertTrue(recordNoMapping.isConsistent());
    }

    @Test
    public void testIsConsistentInvalidMapping() {
        CSVRecord recordInvalidMapping = new CSVRecord(new String[] {"value1", "value2", "value3"}, new HashMap<String, Integer>(), null, 1);
        assertFalse(recordInvalidMapping.isConsistent());
    }

    @Test
    public void testIsConsistentIndexOutOfBounds() {
        CSVRecord recordIndexOutOfBounds = new CSVRecord(new String[] {"value1", "value2"}, new HashMap<String, Integer>(), null, 1);
        assertFalse(recordIndexOutOfBounds.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertFalse(record.isMapped("name1"));
        assertFalse(record.isMapped("name2"));
        assertFalse(record.isMapped("name3"));
    }

    @Test
    public void testIsSet() {
        assertFalse(record.isSet("name1"));
        assertFalse(record.isSet("name2"));
        assertFalse(record.isSet("name3"));
    }

    @Test
    public void testIterator() {
        assertNotNull(record.iterator());
        assertTrue(record.iterator().hasNext());
        assertEquals("value1", record.iterator().next());
        assertTrue(record.iterator().hasNext());
        assertEquals("value2", record.iterator().next());
        assertTrue(record.iterator().hasNext());
        assertEquals("value3", record.iterator().next());
        assertFalse(record.iterator().hasNext());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
        assertEquals("value3", map.get("name3"));
    }

    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        assertNotNull(record.toList());
        assertEquals(3, record.toList().size());
        assertEquals("value1", record.toList().get(0));
        assertEquals("value2", record.toList().get(1));
        assertEquals("value3", record.toList().get(2));
    }

    @Test
    public void testToMap() {
        Map<String, String> map = record.toMap();
        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
        assertEquals("value3", map.get("name3"));
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", record.toString());
    }

    @Test
    public void testValues() {
        assertArrayEquals(new String[] {"value1", "value2", "value3"}, record.values());
    }

    private enum TestEnum {
        TEST1, TEST2, TEST3;
    }

}