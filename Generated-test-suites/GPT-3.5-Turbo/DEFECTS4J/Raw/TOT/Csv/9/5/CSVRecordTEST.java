import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {
    private CSVRecord record;
    private final String[] values = {"1", "2", "3"};
    private final Map<String, Integer> mapping = new HashMap<String, Integer>();
    private final String comment = "This is a comment";
    private final long recordNumber = 1L;

    @Before
    public void setUp() {
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        record = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByEnum_Success() {
        assertEquals("1", record.get(0));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetByEnum_ArrayIndexOutOfBounds() {
        record.get(5);
    }

    @Test
    public void testGetByIndex_Success() {
        assertEquals("1", record.get(0));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetByIndex_ArrayIndexOutOfBounds() {
        record.get(5);
    }

    @Test
    public void testGetByName_Success() {
        assertEquals("1", record.get("column1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByName_NotMapped() {
        record.get("column4");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetByName_ArrayIndexOutOfBounds() {
        record.get("column3");
    }

    @Test
    public void testGetComment_Success() {
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber_Success() {
        assertEquals(1L, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent_Success() {
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped_Success() {
        assertTrue(record.isMapped("column1"));
    }

    @Test
    public void testIsMapped_NotMapped() {
        assertFalse(record.isMapped("column4"));
    }

    @Test
    public void testIsSet_Success() {
        assertTrue(record.isSet("column1"));
    }

    @Test
    public void testIsSet_NotSet() {
        assertFalse(record.isSet("column3"));
    }

    @Test
    public void testIterator_Success() {
        Iterator<String> iterator = record.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals("1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("3", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutIn_Success() {
        Map<String, String> map = new HashMap<String, String>();
        map = record.putIn(map);
        assertEquals(3, map.size());
        assertEquals("1", map.get("column1"));
        assertEquals("2", map.get("column2"));
        assertEquals("3", map.get("column3"));
    }

    @Test
    public void testSize_Success() {
        assertEquals(3, record.size());
    }

    @Test
    public void testToList_Success() {
        List<String> list = record.toList();
        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals("1", list.get(0));
        assertEquals("2", list.get(1));
        assertEquals("3", list.get(2));
    }

    @Test
    public void testToMap_Success() {
        Map<String, String> map = record.toMap();
        assertEquals(3, map.size());
        assertEquals("1", map.get("column1"));
        assertEquals("2", map.get("column2"));
        assertEquals("3", map.get("column3"));
    }

    @Test
    public void testToString_Success() {
        String expected = "[1, 2, 3]";
        assertEquals(expected, record.toString());
    }

    @Test
    public void testValues_Success() {
        String[] expected = {"1", "2", "3"};
        assertArrayEquals(expected, record.values());
    }
}