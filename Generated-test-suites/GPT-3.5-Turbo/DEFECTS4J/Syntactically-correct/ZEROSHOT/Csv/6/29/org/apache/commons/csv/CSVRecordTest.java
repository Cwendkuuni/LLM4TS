import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        String[] values = {"Value1", "Value2", "Value3"};
        csvRecord = new CSVRecord(values, null, "Comment", 1);
    }

    @Test
    public void testGetByEnum() {
        assertEquals("Value1", csvRecord.get(TestEnum.VALUE1));
        assertEquals("Value2", csvRecord.get(TestEnum.VALUE2));
        assertEquals("Value3", csvRecord.get(TestEnum.VALUE3));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("Value1", csvRecord.get(0));
        assertEquals("Value2", csvRecord.get(1));
        assertEquals("Value3", csvRecord.get(2));
    }

    @Test
    public void testGetByName() {
        assertEquals("Value1", csvRecord.get("name1"));
        assertEquals("Value2", csvRecord.get("name2"));
        assertEquals("Value3", csvRecord.get("name3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetThrowsIllegalStateException() {
        CSVRecord record = new CSVRecord(null, null, null, 1);
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetThrowsIllegalArgumentException() {
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, null, null, 1);
        record.get("name");
    }

    @Test
    public void testGetComment() {
        assertEquals("Comment", csvRecord.getComment());
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
        assertFalse(csvRecord.isMapped("name1"));
        assertFalse(csvRecord.isMapped("name2"));
        assertFalse(csvRecord.isMapped("name3"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("name1"));
        assertTrue(csvRecord.isSet("name2"));
        assertTrue(csvRecord.isSet("name3"));
    }

    @Test
    public void testIterator() {
        assertNotNull(csvRecord.iterator());
    }

    @Test
    public void testPutIn() {
        // Setup
        Map<String, String> map = new HashMap<String, String>();

        // Run
        csvRecord.putIn(map);

        // Verify
        assertEquals("Value1", map.get("name1"));
        assertEquals("Value2", map.get("name2"));
        assertEquals("Value3", map.get("name3"));
    }

    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToMap() {
        // Run
        Map<String, String> map = csvRecord.toMap();

        // Verify
        assertEquals("Value1", map.get("name1"));
        assertEquals("Value2", map.get("name2"));
        assertEquals("Value3", map.get("name3"));
    }

    @Test
    public void testToString() {
        assertEquals("[Value1, Value2, Value3]", csvRecord.toString());
    }

    @Test
    public void testValues() {
        assertArrayEquals(new String[]{"Value1", "Value2", "Value3"}, csvRecord.values());
    }

    private enum TestEnum {
        VALUE1,
        VALUE2,
        VALUE3
    }
}