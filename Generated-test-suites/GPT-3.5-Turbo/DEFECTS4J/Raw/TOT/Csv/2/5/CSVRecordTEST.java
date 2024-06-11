import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord record;

    @Before
    public void setUp() {
        String[] values = {"A", "B", "C"};
        record = new CSVRecord(values, null, null, 1);
    }

    @Test
    public void testGetByIndex() {
        assertEquals("A", record.get(0));
        assertEquals("B", record.get(1));
        assertEquals("C", record.get(2));
    }

    @Test
    public void testGetByName() {
        assertNull(record.get("name"));
        assertTrue(record.isMapped("name"));
        assertFalse(record.isSet("name"));

        assertNull(record.get(null));
        assertFalse(record.isMapped(null));
        assertFalse(record.isSet(null));

        assertNull(record.get(""));
        assertFalse(record.isMapped(""));
        assertFalse(record.isSet(""));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByInvalidNameWithNoMapping() {
        record.get("name");
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithHeaderMapping() {
        record = new CSVRecord(new String[] {"A", "B", "C"}, null, null, 1);
        assertFalse(record.isConsistent());

        record = new CSVRecord(new String[] {"A", "B", "C"}, null, null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertFalse(record.isMapped("name"));
        assertFalse(record.isMapped(null));
        assertFalse(record.isMapped(""));
    }

    @Test
    public void testIsSet() {
        assertFalse(record.isSet("name"));
        assertFalse(record.isSet(null));
        assertFalse(record.isSet(""));
    }

    @Test
    public void testIterator() {
        assertNotNull(record.iterator());
    }

    @Test
    public void testValues() {
        assertArrayEquals(new String[] {"A", "B", "C"}, record.values());
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
    public void testSize() {
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        assertEquals("[A, B, C]", record.toString());
    }
}