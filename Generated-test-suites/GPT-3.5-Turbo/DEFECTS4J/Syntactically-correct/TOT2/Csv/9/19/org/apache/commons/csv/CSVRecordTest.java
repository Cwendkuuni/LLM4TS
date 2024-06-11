import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        String[] values = {"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First Name", 0);
        mapping.put("Last Name", 1);
        mapping.put("Age", 2);
        String comment = "Sample comment";
        long recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByEnum() {
        assertEquals("John", csvRecord.get(SomeEnum.FIRST_NAME));
        assertEquals("Doe", csvRecord.get(SomeEnum.LAST_NAME));
        assertEquals("30", csvRecord.get(SomeEnum.AGE));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("John", csvRecord.get(0));
        assertEquals("Doe", csvRecord.get(1));
        assertEquals("30", csvRecord.get(2));
    }

    @Test
    public void testGetByName() {
        assertEquals("John", csvRecord.get("First Name"));
        assertEquals("Doe", csvRecord.get("Last Name"));
        assertEquals("30", csvRecord.get("Age"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidName() {
        csvRecord.get("InvalidColumn");
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"Name"}, null, "Comment", 1);
        record.get("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameIndexOutOfBounds() {
        csvRecord.get("Age");
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
        assertTrue(csvRecord.isMapped("First Name"));
        assertFalse(csvRecord.isMapped("InvalidColumn"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("First Name"));
        assertFalse(csvRecord.isSet("InvalidColumn"));
    }

    @Test
    public void testIterator() {
        Iterator<String> iterator = csvRecord.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("John", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("Doe", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("30", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = csvRecord.putIn(new HashMap<>());
        assertEquals(3, map.size());
        assertEquals("John", map.get("First Name"));
        assertEquals("Doe", map.get("Last Name"));
        assertEquals("30", map.get("Age"));
    }

    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToMap() {
        Map<String, String> map = csvRecord.toMap();
        assertEquals(3, map.size());
        assertEquals("John", map.get("First Name"));
        assertEquals("Doe", map.get("Last Name"));
        assertEquals("30", map.get("Age"));
    }

    @Test
    public void testToString() {
        assertEquals("[John, Doe, 30]", csvRecord.toString());
    }

    @Test
    public void testValues() {
        assertArrayEquals(new String[]{"John", "Doe", "30"}, csvRecord.values());
    }

}