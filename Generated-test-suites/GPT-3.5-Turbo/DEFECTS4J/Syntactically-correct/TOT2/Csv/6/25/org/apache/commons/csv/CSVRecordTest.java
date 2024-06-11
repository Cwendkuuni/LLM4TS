import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        // Initialize a sample CSVRecord instance for testing
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);

        csvRecord = new CSVRecord(values, mapping, "Sample comment", 1);
    }

    @Test
    public void testGetByEnum() {
        assertEquals("value1", csvRecord.get(Enum.valueOf("column1")));
        assertEquals("value2", csvRecord.get(Enum.valueOf("column2")));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
    }

    @Test
    public void testGetByName() {
        assertEquals("value1", csvRecord.get("column1"));
        assertEquals("value2", csvRecord.get("column2"));
    }

    @Test
    public void testGetComment() {
        assertEquals("Sample comment", csvRecord.getComment());
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
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("column1"));
        assertFalse(csvRecord.isMapped("unknownColumn"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("column1"));
        assertFalse(csvRecord.isSet("column2"));
    }

    @Test
    public void testIterator() {
        Iterator<String> iterator = csvRecord.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value2", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        map.put("column1", "value1");
        map.put("column2", "value2");

        assertEquals(map, csvRecord.putIn(new HashMap<>()));
    }

    @Test
    public void testSize() {
        assertEquals(2, csvRecord.size());
    }

    @Test
    public void testToMap() {
        Map<String, String> map = new HashMap<>();
        map.put("column1", "value1");
        map.put("column2", "value2");

        assertEquals(map, csvRecord.toMap());
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2]", csvRecord.toString());
    }

    @Test
    public void testValues() {
        assertArrayEquals(new String[]{"value1", "value2"}, csvRecord.values());
    }
}