import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        String[] values = {"value1", "value2", "value3"};
        csvRecord = new CSVRecord(values, null, null, 1L);
    }

    @Test
    public void testGetEnum() {
        assertEquals("value1", csvRecord.get(TestEnum.TEST1));
        assertEquals("value2", csvRecord.get(TestEnum.TEST2));
        assertEquals("value3", csvRecord.get(TestEnum.TEST3));
    }

    @Test
    public void testGetInt() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }

    @Test
    public void testGetString() {
        assertNull(csvRecord.get("column1"));
        assertNull(csvRecord.get("column2"));
        assertNull(csvRecord.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetStringWithoutHeaderMapping() {
        csvRecord.get("column1");
    }

    @Test
    public void testGetComment() {
        assertNull(csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1L, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertFalse(csvRecord.isMapped("column1"));
        assertFalse(csvRecord.isMapped("column2"));
        assertFalse(csvRecord.isMapped("column3"));
    }

    @Test
    public void testIsSet() {
        assertFalse(csvRecord.isSet("column1"));
        assertFalse(csvRecord.isSet("column2"));
        assertFalse(csvRecord.isSet("column3"));
    }

    @Test
    public void testIterator() {
        assertFalse(csvRecord.iterator().hasNext());
    }

    @Test
    public void testPutIn() {
        csvRecord.putIn(new HashMap<String, String>());
    }

    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToList() {
        assertEquals(3, csvRecord.toList().size());
    }

    @Test
    public void testToMap() {
        assertEquals(0, csvRecord.toMap().size());
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", csvRecord.toString());
    }

    @Test
    public void testValues() {
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, csvRecord.values());
    }

    private enum TestEnum {
        TEST1,
        TEST2,
        TEST3
    }

}