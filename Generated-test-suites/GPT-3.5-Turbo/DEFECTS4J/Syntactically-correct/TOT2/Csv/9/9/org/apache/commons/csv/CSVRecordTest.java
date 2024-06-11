import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a test comment";
        long recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByEnum() {
        assertEquals("value1", csvRecord.get(TestEnum.COLUMN1));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value2", csvRecord.get(1));
    }

    @Test
    public void testGetByName() {
        assertEquals("value3", csvRecord.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        csvRecord = new CSVRecord(new String[]{"value"}, null, "Test comment", 1);
        csvRecord.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByInvalidName() {
        csvRecord.get("invalidColumn");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByInvalidIndex() {
        csvRecord.get(10);
    }

    @Test
    public void testGetComment() {
        assertEquals("This is a test comment", csvRecord.getComment());
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
        assertFalse(csvRecord.isMapped("invalidColumn"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("column3"));
        assertFalse(csvRecord.isSet("column4"));
    }

    @Test
    public void testIterator() {
        Iterator<String> iterator = csvRecord.iterator();
        assertNotNull(iterator);
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
        Map<String, String> map = new HashMap<>();
        map.put("column1", "value1");
        map.put("column2", "value2");
        map.put("column3", "value3");
        assertEquals(map, csvRecord.putIn(new HashMap<String, String>()));
    }

    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToMap() {
        Map<String, String> map = new HashMap<>();
        map.put("column1", "value1");
        map.put("column2", "value2");
        map.put("column3", "value3");
        assertEquals(map, csvRecord.toMap());
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", csvRecord.toString());
    }

    @Test
    public void testValues() {
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, csvRecord.values());
    }
}