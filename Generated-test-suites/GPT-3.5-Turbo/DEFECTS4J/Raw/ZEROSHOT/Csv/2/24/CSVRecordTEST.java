import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord record;
    private String[] values = {"value1", "value2", "value3"};
    private Map<String, Integer> mapping = null;
    private String comment = "This is a comment";
    private long recordNumber = 1;

    @Before
    public void setUp() {
        record = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        assertNull(record.get("name1"));
        assertNull(record.get("name2"));
        assertNull(record.get("name3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithNoMapping() {
        record.get("name1");
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
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
    public void testValues() {
        assertArrayEquals(values, record.values());
    }

    @Test
    public void testGetComment() {
        assertEquals(comment, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", record.toString());
    }

}