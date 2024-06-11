import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        csvRecord = new CSVRecord(values, mapping, "comment", 1);
    }

    @Test
    public void testGetByEnum() {
        assertEquals("value1", csvRecord.get(Enum.COLUMN1));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value2", csvRecord.get(1));
    }

    @Test
    public void testGetByName() {
        assertEquals("value3", csvRecord.get("column3"));
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
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("column1"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("column2"));
    }

    @Test
    public void testIterator() {
        Iterator<String> iterator = csvRecord.iterator();
        assertEquals("value1", iterator.next());
        assertEquals("value2", iterator.next());
        assertEquals("value3", iterator.next());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        map.put("column1", "value1");
        map.put("column2", "value2");
        map.put("column3", "value3");

        assertEquals(map, csvRecord.putIn(new HashMap<>()));
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